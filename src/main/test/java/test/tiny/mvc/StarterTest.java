package test.tiny.mvc;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.tiny.spring.TinyApplication;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-19 10 :44
 * @description
 */
public class StarterTest {
    public static final int PORT = 8080;
    public static final String CONTEXT_PATH = "/";
    public static final String SERVLET_NAME = "indexServlet";

    public static void main(String[] args) throws Exception {
        TinyApplication.run(StarterTest.class);
    }
}
