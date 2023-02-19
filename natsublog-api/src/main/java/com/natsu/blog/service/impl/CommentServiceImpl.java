package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.CommentMapper;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.model.vo.PageResult;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.utils.tree.TreeNode;
import com.natsu.blog.utils.tree.TreeUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper , Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageResult<Comment> getCommentsByQueryParams(CommentQueryDTO commentQueryDTO) {
        IPage<Comment> page = new Page<>(commentQueryDTO.getPageNo(),commentQueryDTO.getPageSize());
        IPage<Comment> comments = commentMapper.getCommentsByQueryParams(page,commentQueryDTO);
        return new PageResult<>(comments.getPages(), comments.getRecords());
    }

    @Override
    public Map<String , Object> buildCommentTree(CommentQueryDTO commentQueryDTO) {
        PageResult<Comment> pageResult = this.getCommentsByQueryParams(commentQueryDTO);
        List<Comment> rootComments = pageResult.getDataList();

        /*//根据rootComment查找childComment。forEach操作
        for (Comment comment : rootComments) {
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getIsPublished , commentQueryDTO.getIsPublished());
            wrapper.eq(Comment::getOriginId , comment.getOriginId());
            wrapper.ne(Comment::getId , comment.getId());
            wrapper.orderByAsc(Comment::getCreateTime);
            childComments.addAll(commentMapper.selectList(wrapper));
        }*/

        //根据rootComment查找childComment。Stream操作。
        Map<Long , Long> queryMap = rootComments.stream().collect(Collectors.toMap(Comment::getId , Comment::getOriginId));
        List<Comment> childComments = new ArrayList<>();
        if (!MapUtils.isEmpty(queryMap)) {
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getIsPublished , commentQueryDTO.getIsPublished());
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
        Map<String , Object> result = new HashMap<>();
        List<TreeNode> treeNodes = buildCommentTreeNode(allComment);
        treeNodes = TreeUtils.listToTree(treeNodes, node -> node.getPid().equals(commentQueryDTO.getParentCommentId()));

        //遍历评论树，并转为两级评论树
        for (TreeNode treeNode : treeNodes) {
            if (treeNode.getChildren() == null) {
                comments.add(treeNode);
            }else {
                comments.add(conTwoLevelCommentTree(treeNode));
            }
        }

        //获取当前页面(或文章)的评论数量和分页总页数
        LambdaQueryWrapper<Comment> queryCount = new LambdaQueryWrapper<>();
        queryCount.eq(Comment::getIsPublished , commentQueryDTO.getIsPublished());
        queryCount.eq(Comment::getPage , commentQueryDTO.getPage());
        if (commentQueryDTO.getPage().equals(Constants.PAGE_READ_ARTICLE)) {
            queryCount.eq(Comment::getArticleId , commentQueryDTO.getArticleId());
        }
        Integer commentCount = commentMapper.selectCount(queryCount);
        Long totalPage =  pageResult.getTotalPage();

        //封装结果集
        result.put("count", commentCount);
        result.put("totalPage", totalPage);
        result.put("comments", comments);
        return result;

    }

    /**
     * 构建评论树Node
     * */
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
     * */
    private TreeNode conTwoLevelCommentTree(TreeNode treeNode) {
        List<TreeNode> childNodes = TreeUtils.getAllChildNodeByRootNode(treeNode);

        //对子评论按时间正序排序
        List<TreeNode> ascTreeNodes = childNodes.stream().sorted(Comparator.comparing(
                element -> (Date) element.getContent().get("createTime")
        )).collect(Collectors.toList());

        treeNode.setChildren(ascTreeNodes);
        return treeNode;
    }
}
