package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.CommentMapper;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.model.vo.PageResult;
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

    @Override
    public Map<String , Object> buildCommentTree(CommentQueryDTO commentQueryDTO) {
        PageResult<Comment> pageResult = this.getRootComments(commentQueryDTO);
        List<Comment> rootComments = pageResult.getDataList();
        List<Comment> childComments = new ArrayList<>();
        /*根据rootComment查找childComment*/
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getIsPublished , commentQueryDTO.getIsPublished());
        for (Comment comment : rootComments) {
            wrapper.eq(Comment::getOriginId , comment.getOriginId());
            wrapper.ne(Comment::getId , comment.getId());
            wrapper.orderByDesc(Comment::getCreateTime);
            childComments.addAll(commentMapper.selectList(wrapper));
        }

        /*将所rootComment和childComment放在一个集合*/
        List<Comment> allComment = new ArrayList<>();
        allComment.addAll(rootComments);
        allComment.addAll(childComments);

        /*开始构建commentTree*/
        List<TreeNode> comments = new ArrayList<>();
        Map<String , Object> result = new HashMap<>();
        List<TreeNode> treeNodes = TreeUtils.buildCommentTreeNode(allComment);
        treeNodes = TreeUtils.buildCommentTree(treeNodes,-1);

        /*遍历评论树，并转为两级评论树*/
        for (TreeNode treeNode : treeNodes) {
            if (treeNode.getChildren() == null) {
                comments.add(treeNode);
            }else {
                comments.add(TreeUtils.conTwoLevelCommentTree(treeNode));
            }
        }

        /*获取当前页面的评论数量和分页总页数*/
        LambdaQueryWrapper<Comment> queryCount = new LambdaQueryWrapper<>();
        queryCount.eq(Comment::getIsPublished , true);
        queryCount.eq(Comment::getPage , commentQueryDTO.getPage());
        Integer commentCount = commentMapper.selectCount(queryCount);
        Integer totalPage = (int) pageResult.getTotalPage();

        /*封装结果集*/
        result.put("count" , commentCount);
        result.put("totalPage" , totalPage);
        result.put("comments" , comments);
        return result;

    }

    @Override
    public PageResult<Comment> getCommentsByQueryParams(CommentQueryDTO commentQueryDTO) {
        IPage<Comment> page = new Page<>(commentQueryDTO.getPageNo(),commentQueryDTO.getPageSize());
        IPage<Comment> comments = commentMapper.getCommentsByQueryParams(page,commentQueryDTO);
        return new PageResult<>(comments.getPages(), comments.getRecords());
    }

    public PageResult<Comment> getRootComments(CommentQueryDTO commentQueryDTO) {
        commentQueryDTO.setParentCommentId(-1);
        commentQueryDTO.setIsPublished(true);
        IPage<Comment> page = new Page<>(commentQueryDTO.getPageNo() , commentQueryDTO.getPageSize());
        IPage<Comment> rootComments = commentMapper.getCommentsByQueryParams(page , commentQueryDTO);
        return new PageResult<>(rootComments.getPages(),rootComments.getRecords());
    }

}
