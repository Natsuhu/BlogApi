package com.natsu.blog.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.StorageType;
import com.natsu.blog.mapper.CommentMapper;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.service.MomentService;
import com.natsu.blog.service.SettingService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.QQInfoUtils;
import com.natsu.blog.utils.SpringContextUtils;
import com.natsu.blog.utils.tree.TreeNode;
import com.natsu.blog.utils.tree.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SettingService settingService;

    @Autowired
    private AnnexService annexService;

    @Override
    public Integer getCommentCount(Integer objectType, Long objectId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getObjectType, objectType);
        queryWrapper.eq(Comment::getIsPublished, true);
        if (objectId != null) {
            queryWrapper.eq(Comment::getObjectId, objectId);
        }
        return commentMapper.selectCount(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveComment(CommentDTO commentDTO) {
        //验证目标页面能否评论
        if (!checkPageIsComment(commentDTO.getObjectType(), commentDTO.getObjectId())) {
            throw new RuntimeException("评论区已关闭");
        }
        //组装实体
        if (commentDTO.getParentCommentId().equals(Constants.TOP_COMMENT_PARENT_ID)) {
            //设置8位的数字ID
            //int begin = RandomUtils.nextInt(1, 9);
            //int end = begin + 8;
            //String uuid = UUID.randomUUID().toString().replace("-", "");
            //String unmUUid = new BigInteger(uuid, 16).toString().replace("0", String.valueOf(begin));
            //Long uuidL = Long.parseLong(unmUUid.substring(begin, end));
            //改为用时间戳
            String treeId = System.currentTimeMillis() + commentDTO.getIp().replace(".", "");
            commentDTO.setTreeId(treeId);
        } else {
            //如果是回复评论，则检查上级评论状态，并设置originId
            Comment parentComment = commentMapper.selectById(commentDTO.getParentCommentId());
            if (parentComment == null || !parentComment.getIsPublished()) {
                throw new RuntimeException("目标评论无法回复");
            }
            commentDTO.setReplyNickname(parentComment.getNickname());
            commentDTO.setTreeId(parentComment.getTreeId());
        }
        //如果是QQ则直接使用QQ的头像和昵称
        String qqNum = commentDTO.getQq();
        //随机头像目前是这样的：在附件表中手动设置ID为1到5的图片，设置随机头像就从5个里面取1个。先从后台管理系统上传，然后手动修改ID。
        //此方法暂不清楚以后会不会改
        int randomAvatar = RandomUtil.randomInt(1, 6);
        if (QQInfoUtils.isQQNumber(qqNum)) {
            //尝试获取QQ头像和昵称，获取失败使用随机头像
            try {
                String nickname = QQInfoUtils.getQQNickname(qqNum);
                String annexId = annexService.saveQQAvatar(qqNum, nickname, StorageType.LOCAL.getType());
                commentDTO.setAvatar(annexId);
                commentDTO.setNickname(nickname);
            } catch (Exception e) {
                log.error("获取QQ头像失败：{}", e.getMessage());
                commentDTO.setNickname("nickname");
                commentDTO.setAvatar(String.valueOf(randomAvatar));
            }
        } else {
            if (qqNum.length() > 15) {
                throw new RuntimeException("昵称过长！");
            }
            commentDTO.setNickname(qqNum);
            commentDTO.setAvatar(String.valueOf(randomAvatar));
        }
        commentDTO.setIsPublished(Constants.PUBLISHED);
        //开始保存
        commentMapper.insert(commentDTO);
    }

    @Override
    public Map<String, Object> getComments(CommentQueryDTO commentQueryDTO) {
        //验证目标页面能否评论
        if (!checkPageIsComment(commentQueryDTO.getObjectType(), commentQueryDTO.getObjectId())) {
            throw new RuntimeException("评论区已关闭");
        }

        //分页获取root评论
        IPage<Comment> page = new Page<>(commentQueryDTO.getPageNo(), commentQueryDTO.getPageSize());
        IPage<Comment> pageResult = commentMapper.getRootComments(page, commentQueryDTO);
        List<Comment> rootComments = pageResult.getRecords();

        //根据rootComment查找childComment。Stream操作。
        Map<Long, String> queryMap = rootComments.stream().collect(Collectors.toMap(Comment::getId, Comment::getTreeId));
        List<Comment> childComments = new ArrayList<>();
        if (!MapUtils.isEmpty(queryMap)) {
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getIsPublished, Constants.PUBLISHED);
            wrapper.notIn(Comment::getId, queryMap.keySet());
            wrapper.in(Comment::getTreeId, queryMap.values());
            childComments = commentMapper.selectList(wrapper);
        }

        //将所rootComment和childComment放在一个集合
        List<Comment> allComment = new ArrayList<>(rootComments.size() + childComments.size());
        allComment.addAll(rootComments);
        allComment.addAll(childComments);

        //开始构建commentTree
        List<TreeNode> comments = new ArrayList<>();
        Map<String, Object> result = new HashMap<>(3);
        List<TreeNode> treeNodes = buildCommentTreeNode(allComment);
        treeNodes = TreeUtils.listToTree(treeNodes, node -> node.getPid().equals(Constants.TOP_COMMENT_PARENT_ID));

        //遍历评论树，并转为两级评论树
        for (TreeNode treeNode : treeNodes) {
            if (treeNode.getChildren() == null) {
                comments.add(treeNode);
            } else {
                comments.add(conTwoLevelCommentTree(treeNode));
            }
        }

        //获取当前页面的评论数量和分页总页数
        LambdaQueryWrapper<Comment> queryCount = new LambdaQueryWrapper<>();
        queryCount.eq(Comment::getIsPublished, Constants.PUBLISHED);
        queryCount.eq(Comment::getObjectType, commentQueryDTO.getObjectType());
        if (commentQueryDTO.getObjectId() != null) {
            queryCount.eq(Comment::getObjectId, commentQueryDTO.getObjectId());
        }
        Integer commentCount = commentMapper.selectCount(queryCount);
        Long totalPage = pageResult.getPages();

        //封装结果集
        result.put("count", commentCount);
        result.put("totalPage", totalPage);
        result.put("comments", comments);
        return result;

    }

    /**
     * 获取评论表格
     *
     * @param commentQueryDTO 查询DTO
     * @return 分页数据
     */
    @Override
    public IPage<CommentDTO> getCommentTable(CommentQueryDTO commentQueryDTO) {
        //查询
        IPage<CommentDTO> page = new Page<>(commentQueryDTO.getPageNo(), commentQueryDTO.getPageSize());
        IPage<CommentDTO> commentTable = commentMapper.getCommentTable(page, commentQueryDTO);
        //处理头像和IP
        List<CommentDTO> commentDTOList = commentTable.getRecords();
        for (CommentDTO commentDTO : commentDTOList) {
            commentDTO.setAvatar(annexService.getAnnexAccessAddress(commentDTO.getAvatar()));
            commentDTO.setCity(IPUtils.getCityInfo(commentDTO.getIp()));
        }
        //封装处理结果
        IPage<CommentDTO> pageResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        pageResult.setRecords(commentDTOList);
        return pageResult;
    }

    /**
     * 更新评论
     * 当设置评论公开\非公开时，会影响其子评论的权限
     *
     * @param commentDTO commentDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateComment(CommentDTO commentDTO) {
        //先查到这条记录
        Comment comment = commentMapper.selectById(commentDTO);
        //更新记录
        commentMapper.updateById(commentDTO);
        if (!comment.getIsPublished().equals(commentDTO.getIsPublished())) {
            //修改了权限，要同时修改所有子评论的权限,先获取同树的全部评论
            List<Comment> comments = getSameOrgCommons(comment.getTreeId());
            //转为树结构并过滤出目标子树
            List<TreeNode> treeNodes = buildCommentTreeNode(comments);
            TreeNode rootNode = TreeUtils.listToTree(treeNodes, node -> node.getId().equals(comment.getId())).get(Constants.COM_NUM_ZERO);
            List<TreeNode> childNode = TreeUtils.getAllChildNodeByRootNode(rootNode);
            if (!childNode.isEmpty()) {
                //更新子评论权限
                List<Comment> updateList = new ArrayList<>(childNode.size());
                for (TreeNode treeNode : childNode) {
                    Comment updateComment = new Comment();
                    updateComment.setId(treeNode.getId());
                    updateComment.setIsPublished(commentDTO.getIsPublished());
                    updateList.add(updateComment);
                }
                updateBatchById(updateList);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteComment(CommentDTO commentDTO) {
        //删除评论时，顺带删除所有子评论
        List<Comment> comments = getSameOrgCommons(commentDTO.getTreeId());
        //转为树结构并过滤出目标子树
        List<TreeNode> treeNodes = buildCommentTreeNode(comments);
        TreeNode rootNode = TreeUtils.listToTree(treeNodes, node -> node.getId().equals(commentDTO.getId())).get(Constants.COM_NUM_ZERO);
        List<TreeNode> childNode = TreeUtils.getAllChildNodeByRootNode(rootNode);
        //批量删除
        List<Long> ids = new ArrayList<>();
        ids.add(commentDTO.getId());
        for (TreeNode treeNode : childNode) {
            ids.add(treeNode.getId());
        }
        commentMapper.deleteBatchIds(ids);
        return ids.size();
    }

    /**
     * 获取评论表格的文章筛选下拉框
     *
     * @return ArticleList
     */
    @Override
    public List<Article> getArticleSelector() {
        return commentMapper.getArticleSelector();
    }

    /**
     * 获取同源排列
     *
     * @param treeId 树ID
     * @return comments
     */
    private List<Comment> getSameOrgCommons(String treeId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getTreeId, treeId);
        return commentMapper.selectList(queryWrapper);
    }

    /**
     * 构建评论树Node
     */
    private List<TreeNode> buildCommentTreeNode(List<Comment> comments) {
        List<TreeNode> treeNodes = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            Map<String, Object> contentMap = new HashMap<>(6);
            contentMap.put("nickname", comment.getNickname());
            contentMap.put("treeId", comment.getTreeId());
            contentMap.put("content", comment.getContent());
            contentMap.put("avatar", annexService.getAnnexAccessAddress(comment.getAvatar()));
            contentMap.put("createTime", comment.getCreateTime());
            contentMap.put("replyNickname", comment.getReplyNickname());
            contentMap.put("isAdminComment", comment.getIsAdminComment());
            TreeNode treeNode = new TreeNode(comment.getId(), comment.getParentCommentId(), contentMap, null);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    /**
     * 转两级评论树
     *
     * @param treeNode 树节点
     * @return TreeNode
     */
    private TreeNode conTwoLevelCommentTree(TreeNode treeNode) {
        List<TreeNode> childNodes = TreeUtils.getAllChildNodeByRootNode(treeNode);

        //对子评论按时间正序排序
        List<TreeNode> ascTreeNodes = childNodes.stream().sorted(Comparator.comparing(
                element -> (Date) element.getContent().get("createTime")
        )).collect(Collectors.toList());

        treeNode.setChildren(ascTreeNodes);
        return treeNode;
    }

    /**
     * 验证目标页面能否评论
     */
    private Boolean checkPageIsComment(Integer page, Long articleId) {
        switch (page) {
            case 1:
                ArticleService articleService = SpringContextUtils.getBean(ArticleService.class);
                Article article = articleService.getById(articleId);
                if (article == null) {
                    return false;
                }
                return article.getIsPublished().equals(Constants.PUBLISHED) && article.getIsCommentEnabled().equals(Constants.ALLOW_COMMENT);
            case 2:
                return !"false".equals(settingService.getSetting(Constants.SETTING_KEY_FRIEND_IS_COMMENT));
            case 3:
                return !"false".equals(settingService.getSetting(Constants.SETTING_KEY_ABOUT_IS_COMMENT));
            case 6:
                MomentService momentService = SpringContextUtils.getBean(MomentService.class);
                Moment moment = momentService.getById(articleId);
                if (moment == null) {
                    return false;
                }
                return moment.getIsPublished().equals(Constants.PUBLISHED) && moment.getIsCommentEnabled().equals(Constants.ALLOW_COMMENT);
            default:
                return false;
        }
    }
}
