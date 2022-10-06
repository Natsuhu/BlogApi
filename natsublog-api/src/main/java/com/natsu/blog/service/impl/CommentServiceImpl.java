package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.CommentMapper;
import com.natsu.blog.pojo.Comment;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.utils.tree.TreeNode;
import com.natsu.blog.utils.tree.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper , Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public Integer getPublicCommentCount() {
        return commentMapper.getPublicCommentCount();
    }

    public List<Comment> getPublicCommentsByPage(int page) {
        return commentMapper.getPublicCommentsByPage(page);
    }

    public List<Comment> getPublicCommentsByArticleId(int articleId) {
        return commentMapper.getPublicCommentsByArticleId(articleId);
    }

    public Map getCommentsVOByPage(int page) {
        /*获取某页面的全部评论及其数量*/
        List<Comment> comments = getPublicCommentsByPage(page);
        int count = comments.size();

        /*评论数据封装在List，然后将数量和评论数据都封装在map*/
        List<TreeNode> commentData = new ArrayList<>();
        Map<String , Object> Result = new HashMap<>();

        /*先构建节点，再将节点构建为树*/
        List<TreeNode> treeNodes = TreeUtils.buildCommentTreeNode(comments);
        treeNodes = TreeUtils.buildCommentTree(treeNodes,-1);

        /*遍历评论树，如果此评论没有子评论则直接添加到List，否则就要转换为两级的树结构*/
        for (TreeNode treeNode : treeNodes) {
            if (treeNode.getChildren() == null) {
                commentData.add(treeNode);
            }else {
                TreeNode cache = TreeUtils.conTwoLevelCommentTree(treeNode);
                commentData.add(cache);
            }
        }

        /*将评论数量和评论数据放入Map*/
        Result.put("count",count);
        Result.put("commentData",commentData);
        return Result;
    }

    public Map getCommentsVOByArticleId(int articleId) {
        List<Comment> comments = getPublicCommentsByArticleId(articleId);
        int count = comments.size();
        List<TreeNode> commentData = new ArrayList<>();
        Map<String , Object> Result = new HashMap<>();
        List<TreeNode> treeNodes = TreeUtils.buildCommentTreeNode(comments);
        treeNodes = TreeUtils.buildCommentTree(treeNodes,-1);
        for (TreeNode treeNode : treeNodes) {
            if (treeNode.getChildren() == null) {
                commentData.add(treeNode);
            }else {
                TreeNode cache = TreeUtils.conTwoLevelCommentTree(treeNode);
                commentData.add(cache);
            }
        }
        Result.put("count",count);
        Result.put("commentData",commentData);
        return Result;
    }

    public String saveComment(Comment comment) {
        commentMapper.insert(comment);
        return "评论成功";
    }
}
