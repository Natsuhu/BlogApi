package com.natsu.blog.utils.tree;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class TreeNode{

    private Long id;

    private Long pid;

    private Map<String,Object> content;

    private List<TreeNode> children;
}
