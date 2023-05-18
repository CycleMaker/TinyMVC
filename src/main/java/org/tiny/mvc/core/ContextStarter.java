package org.tiny.mvc.core;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.tiny.spring.Container;
import org.tiny.spring.annotation.Value;
import org.tiny.spring.core.ContextFinishListener;

import java.util.List;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 17 :38
 * @description
 */

public class ContextStarter implements ContextFinishListener {
    @Value(key = "server.port")
    public Integer port;
    public static final String CONTEXT_PATH = "";
    @Override
    public void finishContext(Container container) {
        if (port == null) { port = 8080; }
        ArgResolverManager argResolverManager = ArgResolverManager.getInstance();
        container.getBeanByType(ArgResolver.class).forEach(argResolverManager::addArgResolver);
        argResolverManager.addArgResolver(new BaseArgResolver());
        Tomcat tomcat = initTomcat();
        initMVCContext(tomcat, container.getBeanByType(MVCServlet.class));
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        System.out.println("tomcat start success on port: " + port);
        tomcat.getServer().await();
    }

    private void initMVCContext(Tomcat tomcat,List<MVCServlet> servlets) {
        StandardContext context = new StandardContext();
        context.setPath(CONTEXT_PATH);
        context.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.getHost().addChild(context);
        for (MVCServlet mvcServlet : servlets) {
            tomcat.addServlet(CONTEXT_PATH, mvcServlet.getServletName(), mvcServlet);
            context.addServletMappingDecoded(mvcServlet.getPath(),mvcServlet.getServletName());
        }
    }

    private Tomcat initTomcat() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getHost().setAutoDeploy(false);
        return tomcat;
    }
}
