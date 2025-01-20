package com.natsu.blog.utils.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TreeUtils {

    /**
     * List转树结构，并过滤出符合条件的子树
     */
    public static List<TreeNode> listToTree(List<TreeNode> zoneList, Predicate<? super TreeNode> predicate) {
        Map<Long, List<TreeNode>> zoneByParentIdMap = new HashMap<>(zoneList.size());

        //将parentId相同的Zone放在同一个列表中，parentId作为key
        for (TreeNode treeNode : zoneList) {
            List<TreeNode> children = zoneByParentIdMap.getOrDefault(treeNode.getPid(), new ArrayList<>());
            children.add(treeNode);
            zoneByParentIdMap.put(treeNode.getPid(), children);
        }

        //从map中查询当前节点所有的子节点，并放在当前节点的children列表中
        for (TreeNode treeNode : zoneList) {
            treeNode.setChildren(zoneByParentIdMap.get(treeNode.getId()));
        }

        //过滤出顶级菜单列表
        return zoneList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 层序遍历树
     */
    public static List<TreeNode> getAllChildNodeByRootNode(TreeNode rootNode) {
        List<TreeNode> result = new ArrayList<>();

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(rootNode);

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
        result.forEach(treeNode -> treeNode.setChildren(null));
        return result;
    }
}
