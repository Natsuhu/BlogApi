package com.natsu.blog.utils.markdown.ext.heimu.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import com.natsu.blog.utils.markdown.ext.heimu.Heimu;

import java.util.Collections;
import java.util.Set;

abstract class AbstractHeimuNodeRenderer implements NodeRenderer {
    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.<Class<? extends Node>>singleton(Heimu.class);
    }
}
