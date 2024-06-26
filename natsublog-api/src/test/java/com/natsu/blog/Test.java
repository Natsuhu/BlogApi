package com.natsu.blog;

import com.natsu.blog.service.CommentService;
import com.natsu.blog.utils.QQInfoUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.util.UUID;

@SpringBootTest
public class Test {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    CommentService commentService;


    @org.junit.jupiter.api.Test
    public void test() throws SQLException {
        HikariDataSource dataSource = applicationContext.getBean(HikariDataSource.class);
        dataSource.getConnection();
        System.out.println(dataSource.getPassword());
    }

    /*评论树测试*/
    @org.junit.jupiter.api.Test
    public void test2() {
//        List<Comment> commentList = commentService.getCommentsByQueryParams(2);
//        List<TreeNode> treeNodes = TreeUtils.buildCommentTreeNode(commentList);
//        List<TreeNode> commentListVO = TreeUtils.buildCommentTree(treeNodes,-1);
//        TreeNode treeNode = TreeUtils.conTwoLevelCommentTree(commentListVO.get(1));
//        System.out.println(JSONUtil.toJsonStr(treeNode));
    }

    /*图片上传测试*/
//    @org.junit.jupiter.api.Test
//    public void test3() throws Exception {
//        UploadUtil uploadUtils = applicationContext.getBean(UploadUtil.class);
//        ImageResource image = uploadUtils.getImageByRequest("http://175.178.247.100/avatar.jpg");
//        System.out.println(uploadUtils.upload(image));
//    }

    /*获取QQ昵称*/
    @org.junit.jupiter.api.Test
    public void test4() throws Exception {
        QQInfoUtils utils = applicationContext.getBean(QQInfoUtils.class);
        System.out.println(utils.getQQNickname("1127695505"));
    }

    @org.junit.jupiter.api.Test
    public void test5() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(uuid.hashCode());
    }

}
