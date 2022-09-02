package test;

import java.util.HashMap;
import java.util.Map;

public class Test{
/*    @org.junit.jupiter.api.Test
    public void test() throws Exception{
        Class clazz = Class.forName("com.natsu.blog.pojo.Article");
        Field[] fields = clazz.getDeclaredFields();
        for (Field i : fields){
            System.out.println(i.getName());
        }
        Object object = clazz.newInstance();
        Field field = clazz.getDeclaredField("articleId");
        field.setAccessible(true);
        field.set(object,1);
        Article article = (Article) object;
        System.out.println(article.getArticleId());
    }*/

/*    @org.junit.jupiter.api.Test
    public void test1() throws CloneNotSupportedException {
        Article article = new Article();
        article.setArticleContent("cnm");
        Article article1 = (Article) article.clone();
        article1.setArticleContent("mnc");
        System.out.println(article.getArticleContent()==article1.getArticleContent());

    }*/

    @org.junit.jupiter.api.Test
    public void test() {
        int[] a = {1,2,45,22,2,3,3,5,6,5,7,1,1,1,2,3};
        HashMap<Integer,Integer> hashMap = new HashMap();
        for (int i = 0 ; i < a.length ; i++) {
            Integer integer = hashMap.getOrDefault(a[i],new Integer(0));
            integer++;
            hashMap.put(a[i],integer);
        }
        for (Map.Entry<Integer,Integer> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey()+"出现"+entry.getValue()+"次");
        }
    }



}
