package com.natsu.blog.utils.markdown.ext.heimu.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.HashMap;
import java.util.Map;

public class HeimuHtmlNodeRenderer extends AbstractHeimuNodeRenderer {
    private final HtmlNodeRendererContext context;
    private final HtmlWriter html;

    public HeimuHtmlNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public void render(Node node) {
        Map<String, String> attributes = new HashMap<>(4);
        attributes.put("class", "base_text_heimu");
        attributes.put("title", "你知道的太多了");
        html.tag("span", context.extendAttributes(node, "span", attributes));
        renderChildren(node);
        html.tag("/span");
    }

    private void renderChildren(Node parent) {
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            context.render(node);
            node = next;
        }
    }
}
