package com.natsu.blog.utils.markdown;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.natsu.blog.utils.markdown.ext.cover.CoverExtension;
import com.natsu.blog.utils.markdown.ext.heimu.HeimuExtension;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MarkdownUtils {
    /**
     * 为h标签生成id
     */
    private static final Set<Extension> HEADING_ANCHOR_EXTENSIONS = Collections.singleton(HeadingAnchorExtension.create());
    /**
     * 转换table的HTML
     */
    private static final List<Extension> TABLE_EXTENSION = Collections.singletonList(TablesExtension.create());
    /**
     * 任务列表
     */
    private static final Set<Extension> TASK_LIST_EXTENSION = Collections.singleton(TaskListItemsExtension.create());
    /**
     * 删除线
     */
    private static final Set<Extension> DEL_EXTENSION = Collections.singleton(StrikethroughExtension.create());
    /**
     * 黑幕
     */
    private static final Set<Extension> HEIMU_EXTENSION = Collections.singleton(HeimuExtension.create());
    /**
     * 遮盖层
     */
    private static final Set<Extension> COVER_EXTENSION = Collections.singleton(CoverExtension.create());

    private static final Parser PARSER = Parser.builder()
            .extensions(TABLE_EXTENSION)
            .extensions(TASK_LIST_EXTENSION)
            .extensions(DEL_EXTENSION)
            .extensions(HEIMU_EXTENSION)
            .extensions(COVER_EXTENSION)
            .build();

    private static final HtmlRenderer RENDERER = HtmlRenderer.builder()
            .extensions(HEADING_ANCHOR_EXTENSIONS)
            .extensions(TABLE_EXTENSION)
            .extensions(TASK_LIST_EXTENSION)
            .extensions(DEL_EXTENSION)
            .extensions(HEIMU_EXTENSION)
            .extensions(COVER_EXTENSION)
            .attributeProviderFactory(context -> new CustomAttributeProvider())
            .build();

    /**
     * 自定义标签的属性
     */
    private static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //为一二三级标题生成ID
            if ("h1h2h3".contains(tagName)) {
                attributes.put("id", "h" + UUID.randomUUID().toString().replace("-", ""));
            }
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                Link n = (Link) node;
                String destination = n.getDestination();
                //判断是否页内锚点跳转
                if (destination.startsWith("#")) {
                    attributes.put("class", "toc-link");//针对tocbot锚点跳转的class属性
                } else {
                    //外部链接
                    attributes.put("target", "_blank");
                    attributes.put("rel", "external nofollow noopener");
                }
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");//针对 semantic-ui 的class属性
            }
        }
    }

    /**
     * markdown格式转换成HTML格式
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展
     */
    public static String markdownToHtmlExtensions(String markdown) {
        Node document = PARSER.parse(markdown);
        return RENDERER.render(document);
    }

    /**
     * 从html文本中获取目录
     *
     * @param htmlString html字符
     * @return JSON数组
     */
    public static JSONArray getCatalog(String htmlString) {
        Document document = Jsoup.parse(htmlString);
        Elements allElements = document.getAllElements();
        //遍历出h1h2h3
        JSONArray catalog = new JSONArray();
        for (Element element : allElements) {
            if ("h1h2h3".contains(element.tagName())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", element.attr("id"));
                jsonObject.put("title", element.text());
                catalog.add(jsonObject);
            }
        }
        return catalog;
    }

    public static void main(String[] args) {
        String markdown = "# 快速入门\n" +
                "## 安装步骤\n" +
                "### 环境要求\n" +
                "## 使用示例";
        String s = markdownToHtmlExtensions(markdown);
        System.out.println(s);
        System.out.println(getCatalog(s));
    }
}
