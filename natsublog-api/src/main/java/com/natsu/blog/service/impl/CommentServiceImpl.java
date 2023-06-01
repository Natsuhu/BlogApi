package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.mapper.CommentMapper;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.model.entity.Setting;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.service.SettingService;
import com.natsu.blog.utils.QQInfoUtils;
import com.natsu.blog.utils.SpringContextUtils;
import com.natsu.blog.utils.tree.TreeNode;
import com.natsu.blog.utils.tree.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SettingService settingService;

    @Autowired
    private QQInfoUtils qqInfoUtils;

    @Override
    public void saveComment(CommentDTO commentDTO) {
        //验证目标页面能否评论
        if (!checkPageIsComment(commentDTO.getPage(), commentDTO.getArticleId())) {
            throw new RuntimeException("评论区已关闭");
        }
        //组装实体
        if (commentDTO.getParentCommentId().equals(Constants.TOP_COMMENT_PARENT_ID)) {
            //设置8位的数字ID
            int begin = RandomUtils.nextInt(1, 9);
            int end = begin + 8;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String unmUUid = new BigInteger(uuid, 16).toString().replace("0", String.valueOf(begin));
            Long uuidL = Long.parseLong(unmUUid.substring(begin, end));
            commentDTO.setOriginId(uuidL);
        } else {
            //如果是回复评论，则检查上级评论状态，并设置originId
            Comment parentComment = commentMapper.selectById(commentDTO.getParentCommentId());
            if (parentComment == null || !parentComment.getIsPublished()) {
                throw new RuntimeException("目标评论无法回复");
            }
            commentDTO.setReplyNickname(parentComment.getNickname());
            commentDTO.setOriginId(parentComment.getOriginId());
        }
        //填了QQ则直接使用QQ的头像和昵称
        String qqNum = commentDTO.getQq();
        if (StringUtils.isNotBlank(qqNum)) {
            if (!qqInfoUtils.isQQNumber(qqNum)) {
                throw new RuntimeException("QQ号格式错误");
            }
            try {
                commentDTO.setAvatar(qqInfoUtils.getQQAvatarUrl(qqNum));
                commentDTO.setNickname(qqInfoUtils.getQQNickname(qqNum));
            } catch (Exception e) {
                log.error("获取QQ信息昵称和头像失败，{}", e.getMessage());
                throw new RuntimeException("获取QQ信息昵称和头像失败");
            }
        } else {
            //没填QQ则检查昵称格式，并设置随机头像
            if (StringUtils.isBlank(commentDTO.getNickname()) || commentDTO.getNickname().length() > 10) {
                throw new RuntimeException("昵称格式错误");
            }
            //TODO 设置随机avatar，目前先空着
            commentDTO.setAvatar("");
        }
        commentDTO.setIsPublished(Constants.PUBLISHED);
        commentDTO.setIsAdminComment(false);
        //开始保存
        commentMapper.insert(commentDTO);
    }

    @Override
    public Map<String, Object> getComments(CommentQueryDTO commentQueryDTO) {
        //验证目标页面能否评论
        if (!checkPageIsComment(commentQueryDTO.getPage(), commentQueryDTO.getArticleId())) {
            throw new RuntimeException("评论区已关闭");
        }

        //分页获取root评论
        IPage<Comment> page = new Page<>(commentQueryDTO.getPageNo(), commentQueryDTO.getPageSize());
        IPage<Comment> pageResult = commentMapper.getRootComments(page, commentQueryDTO);
        List<Comment> rootComments = pageResult.getRecords();

        //根据rootComment查找childComment。Stream操作。
        Map<Long, Long> queryMap = rootComments.stream().collect(Collectors.toMap(Comment::getId, Comment::getOriginId));
        List<Comment> childComments = new ArrayList<>();
        if (!MapUtils.isEmpty(queryMap)) {
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getIsPublished, Constants.PUBLISHED);
            wrapper.notIn(Comment::getId, queryMap.keySet());
            wrapper.in(Comment::getOriginId, queryMap.values());
            childComments = commentMapper.selectList(wrapper);
        }

        //将所rootComment和childComment放在一个集合
        List<Comment> allComment = new ArrayList<>(rootComments.size() + childComments.size());
        allComment.addAll(rootComments);
        allComment.addAll(childComments);

        //开始构建commentTree
        List<TreeNode> comments = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
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
        queryCount.eq(Comment::getPage, commentQueryDTO.getPage());
        if (commentQueryDTO.getPage().equals(PageEnum.ARTICLE.getPageCode())) {
            queryCount.eq(Comment::getArticleId, commentQueryDTO.getArticleId());
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
     * 构建评论树Node
     */
    private List<TreeNode> buildCommentTreeNode(List<Comment> comments) {
        List<TreeNode> treeNodes = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            Map<String, Object> contentMap = new HashMap<>(6);
            contentMap.put("nickname", comment.getNickname());
            contentMap.put("originId", comment.getOriginId());
            contentMap.put("content", comment.getContent());
            contentMap.put("avatar", comment.getAvatar());
            contentMap.put("createTime", comment.getCreateTime());
            contentMap.put("replyNickname", comment.getReplyNickname());
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
        ArticleService articleService = SpringContextUtils.getBean(ArticleService.class);
        switch (page) {
            case 1:
                Article article = articleService.getById(articleId);
                if (article == null) {
                    return false;
                }
                return article.getIsPublished().equals(Constants.PUBLISHED) && article.getIsCommentEnabled().equals(Constants.ALLOW_COMMENT);
            case 2:
                LambdaQueryWrapper<Setting> friendPageQuery = new LambdaQueryWrapper<>();
                friendPageQuery.eq(Setting::getSettingKey, "isComment");
                friendPageQuery.eq(Setting::getPage, PageEnum.FRIEND.getPageCode());
                Setting friendPageSetting = settingService.getOne(friendPageQuery);
                return !friendPageSetting.getSettingValue().equals("false");
            case 3:
                LambdaQueryWrapper<Setting> aboutPageQuery = new LambdaQueryWrapper<>();
                aboutPageQuery.eq(Setting::getSettingKey, "isComment");
                aboutPageQuery.eq(Setting::getPage, PageEnum.ABOUT.getPageCode());
                Setting aboutPageSetting = settingService.getOne(aboutPageQuery);
                return !aboutPageSetting.getSettingValue().equals("false");
            default:
                return false;
        }
    }
}
