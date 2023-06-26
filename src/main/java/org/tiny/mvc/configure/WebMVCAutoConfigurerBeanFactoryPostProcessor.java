package org.tiny.mvc.configure;

import org.tiny.mvc.core.GlobalExceptionAspect;
import org.tiny.mvc.core.ContextStarter;
import org.tiny.mvc.core.MVCServlet;
import org.tiny.mvc.core.response.handler.HTMLResponseHandler;
import org.tiny.mvc.core.response.handler.JSONResponseHandler;
import org.tiny.mvc.core.MVCBeanPostProcessor;
import org.tiny.mvc.core.arg.resolver.BaseArgResolver;
import org.tiny.spring.Container;
import org.tiny.spring.core.processor.BeanFactoryPostProcessor;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-18 14 :24
 * @description
 */
public class WebMVCAutoConfigurerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void process(Container container) {
        registAutoConfiguer(container);
        registMVCServlet(container);
        registMVCBeanPostProcessor(container);
        registContextStarter(container);
        registJSONResponseHandler(container);
        registHTMLResponseHandler(container);
        registBaseArgResolver(container);
        registGlobalExceptionAspect(container);
    }

    private void registMVCServlet(Container container) {
        container.registerBean(new MVCServlet());
    }

    private void registAutoConfiguer(Container container) {
        container.registerBean(new WebMVCAutoConfiguer());
    }

    private void registBaseArgResolver(Container container) {
        container.registerBean(new BaseArgResolver());
    }

    private void registMVCBeanPostProcessor(Container container) {
        MVCBeanPostProcessor mvcBeanPostProcessor = new MVCBeanPostProcessor();
        container.registerBean(mvcBeanPostProcessor);
    }

    private void registContextStarter(Container container) {
        ContextStarter contextStarter = new ContextStarter();
        container.registerBean(contextStarter);
    }

    private void registJSONResponseHandler(Container container) {
        JSONResponseHandler responseHandler = new JSONResponseHandler();
        container.registerBean(responseHandler);
    }

    private void registHTMLResponseHandler(Container container) {
        HTMLResponseHandler htmlResponseHandler = new HTMLResponseHandler();
        container.registerBean(htmlResponseHandler);
    }

    private void registGlobalExceptionAspect(Container container) {
        container.registerBean(new GlobalExceptionAspect());
    }
}
