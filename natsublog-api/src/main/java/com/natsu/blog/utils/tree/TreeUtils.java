package com.natsu.blog.utils.tree;

import com.natsu.blog.model.entity.Comment;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtils {

    /*批量构建树节点*/
    public static List<TreeNode> buildCommentTreeNode(List<Comment> comments) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String,Object> map = new HashMap<>();
            TreeNode treeNode = new TreeNode(comment.getId(),comment.getParentCommentId(),map,null);
            treeNode.getContent().put("nickname",comment.getNickname());
            treeNode.getContent().put("originId" , comment.getOriginId());
            treeNode.getContent().put("content",comment.getContent());
            treeNode.getContent().put("avatar",comment.getAvatar());
            treeNode.getContent().put("createTime",comment.getCreateTime());
            treeNode.getContent().put("replyNickname",comment.getReplyNickname());
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }


    public static List<TreeNode> buildCommentTree(List<TreeNode> zoneList , int topId) {
        Map<Integer, List<TreeNode>> zoneByParentIdMap = new HashMap<>();

        // 将parentId相同的Zone放在同一个列表中，parentId作为key
        for (TreeNode treeNode : zoneList) {
            List<TreeNode> children = zoneByParentIdMap.getOrDefault(treeNode.getPid(), new ArrayList<>());
            children.add(treeNode);
            zoneByParentIdMap.put(treeNode.getPid(), children);
        }

        // 从map中查询当前节点所有的子节点，并放在当前节点的children列表中
        for (TreeNode treeNode : zoneList) {
            treeNode.setChildren(zoneByParentIdMap.get(treeNode.getId()));
        }

        // 过滤出顶级菜单列表
        return zoneList.stream()
                .filter(treeNode -> treeNode.getPid() == topId)
                .collect(Collectors.toList());
    }

    /*层序遍历树*/
    public static TreeNode conTwoLevelCommentTree(TreeNode head) {
        List<TreeNode> result = new ArrayList<>();

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(head);

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                result.add(node);
                if (node == null) {
                    continue;
                }
                List<TreeNode> childNodes = node.getChildren();
                if (childNodes != null && !childNodes.isEmpty()) {
                    queue.addAll(childNodes);
                }
            }
        }
        result.remove(0);
        for (TreeNode treeNode : result) {
            treeNode.setChildren(null);
        }
        head.setChildren(result);
        return head;
    }
}
