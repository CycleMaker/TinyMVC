package org.tiny.mvc.core;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.tiny.mvc.configure.WebMVCAutoConfiguer;
import org.tiny.mvc.core.arg.resolver.ArgResolver;
import org.tiny.mvc.core.arg.resolver.ArgResolverManager;
import org.tiny.mvc.core.banner.DefaultBannerPrinter;
import org.tiny.mvc.core.banner.MVCBannerPrinter;
import org.tiny.spring.Container;
import org.tiny.spring.annotation.Autowired;
import org.tiny.spring.core.ContextFinishListener;

import java.util.List;
import java.util.Objects;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 17 :38
 * @description
 */

public class ContextStarter implements ContextFinishListener {
    @Autowired
    private WebMVCAutoConfiguer webMVCAutoConfiguer;
    private static final Integer DEFAULT_PORT = 8080;
    public static final String CONTEXT_PATH = "";
    @Override
    public void finishContext(Container container) {
        Integer port = webMVCAutoConfiguer.getPort() == null ? DEFAULT_PORT : webMVCAutoConfiguer.getPort();
        ArgResolverManager argResolverManager = ArgResolverManager.getInstance();
        container.getBeanByType(ArgResolver.class).forEach(argResolverManager::addArgResolver);
        Tomcat tomcat = initTomcat(port);
        initMVCContext(tomcat, container.getBeanByType(MVCServlet.class));
        printBanner(webMVCAutoConfiguer.getMvcBannerPrinter());
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        System.out.println("tomcat start success on port: " + port);
        tomcat.getServer().await();
    }

    private void printBanner(MVCBannerPrinter printer) {
        if (Objects.isNull(printer)) {
            new DefaultBannerPrinter().printBanner();
        } else {
            printer.printBanner();
        }
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

    private Tomcat initTomcat(int port) {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getHost().setAutoDeploy(false);
        return tomcat;
    }
}
