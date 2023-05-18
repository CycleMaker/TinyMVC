package org.tiny.mvc.core;

import org.tiny.mvc.anno.ContentType;
import org.tiny.mvc.anno.Controller;
import org.tiny.mvc.anno.GetMapping;
import org.tiny.mvc.anno.PostMapping;
import org.tiny.mvc.common.ArgNameDiscover;
import org.tiny.mvc.common.ContentTypeEnum;
import org.tiny.mvc.common.Invoker;
import org.tiny.mvc.common.MethodEnum;
import org.tiny.spring.Container;
import org.tiny.spring.annotation.Autowired;
import org.tiny.spring.core.processor.BeanPostProcessor;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 15 :46
 * @description
 */

public class MVCBeanPostProcessor implements BeanPostProcessor {
    private static final String DEFAULT_CONTENT_TYPE = ContentTypeEnum.JSON.getType();
    @Autowired
    private Container container;

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Controller controllerAnno = getControllerAnno(bean);
        if (Objects.nonNull(controllerAnno)) {
            String controllerPath = controllerAnno.path();
            for (Method method : bean.getClass().getDeclaredMethods()) {
                ContentType contentTypeAnno = method.getDeclaredAnnotation(ContentType.class);
                String contentType = Objects.isNull(contentTypeAnno) ? DEFAULT_CONTENT_TYPE : contentTypeAnno.value();
                Map<MethodEnum, Invoker> methodMap = new HashMap<>();
                GetMapping getMapping = acquireGetMappingAnno(method);
                PostMapping postMapping = acquirePostMappingAnno(method);
                String path = fixPathWithSlash(controllerPath);
                String methodType = "";
                if (Objects.nonNull(getMapping)) {
                    path += fixPathWithoutSlash(getMapping.value());
                    methodType = MethodEnum.GetMethod.name();
                    methodMap.put(MethodEnum.GetMethod, new Invoker(bean, method));
                }
                if (Objects.nonNull(postMapping)) {
                    path += fixPathWithoutSlash(postMapping.value());
                    methodType = MethodEnum.PostMethod.name();
                    methodMap.put(MethodEnum.PostMethod, new Invoker(bean, method));
                }
                if (Objects.nonNull(getMapping) || Objects.nonNull(postMapping)) {
                    ArgNameDiscover argNameDiscover = new ArgNameDiscover();
                    argNameDiscover.discover(method);
                    String servletName = beanName + "@Servlet@" + path + "@" + methodType;
                    ResponseHandler responseHandler = getResponseHandler(contentType, method);
                    container.registerBean(servletName, new MVCServlet(methodMap, servletName, path, argNameDiscover, contentType, responseHandler), true);
                }
            }
        }
        return bean;
    }

    private Controller getControllerAnno(Object bean) {
        return bean.getClass().getAnnotation(Controller.class);
    }

    private ResponseHandler getResponseHandler(String contentType, Method method) {
        Optional<ResponseHandler> optionalResponseHandler = container.getBeanByType(ResponseHandler.class).stream().sorted(Comparator.comparingInt(ResponseHandler::priority)).filter(handler -> handler.isSupport(contentType, method)).findFirst();
        if (optionalResponseHandler.isPresent()) {
            return optionalResponseHandler.get();
        }
        throw new RuntimeException("cannot find suitable ResponseHandler for contentType:" + contentType);
    }

    private GetMapping acquireGetMappingAnno(Method method) {
        return method.getAnnotation(GetMapping.class);
    }

    private PostMapping acquirePostMappingAnno(Method method) {
        return method.getAnnotation(PostMapping.class);
    }


    private String fixPathWithSlash(String path) {
        if (path.startsWith("/")) {
            return path;
        }
        path = "/" + path;
        return path;
    }

    private String fixPathWithoutSlash(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }
}
