package org.tiny.mvc.core;

import org.tiny.mvc.anno.ContentType;
import org.tiny.mvc.anno.Controller;
import org.tiny.mvc.anno.GetMapping;
import org.tiny.mvc.common.ArgNameDiscover;
import org.tiny.mvc.common.ContentTypeEnum;
import org.tiny.mvc.common.Invoker;
import org.tiny.mvc.common.MethodEnum;
import org.tiny.mvc.core.response.handler.ResponseHandler;
import org.tiny.spring.Container;
import org.tiny.spring.annotation.Autowired;
import org.tiny.spring.core.processor.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 15 :46
 * @description
 */

public class MVCBeanPostProcessor implements BeanPostProcessor {
    private static final String DEFAULT_CONTENT_TYPE = ContentTypeEnum.JSON.getType();

    private static final Integer PRIORITY = 90;

    @Autowired
    private Container container;

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Controller controllerAnno = getControllerAnno(bean);
        if (Objects.nonNull(controllerAnno)) {
            String controllerPath = fixPathWithSlash(controllerAnno.path());
            List<Invoker> invokers = listMappingAnnoClass(bean, bean.getClass().getDeclaredMethods(), new ArgNameDiscover());
            if (!invokers.isEmpty()) {
                String servletName = beanName + "@Servlet@" + controllerPath;
                container.registerBean(servletName, new MVCServlet(servletName, controllerPath, invokers), true);
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

    private List<Invoker> listMappingAnnoClass(Object bean, Method[] methods, ArgNameDiscover argNameDiscover) {
        List<Invoker> res = new ArrayList<>();
        for (Method m : methods) {
            if (Modifier.isPublic(m.getModifiers())) {
                List<Invoker> item = listMappingAnnoClass(bean, m, argNameDiscover);
                res.addAll(item);
            }
        }
        return res;
    }

    private List<Invoker> listMappingAnnoClass(Object bean, Method method, ArgNameDiscover argNameDiscover) {
        List<Invoker> res = new ArrayList<>();
        for (MethodEnum methodEnum : MethodEnum.values()) {
            Annotation annotation = method.getAnnotation(methodEnum.getAnnoClass());
            if (Objects.nonNull(annotation)) {
                argNameDiscover.discover(method);
                String path = MethodEnum.getPath(annotation);
                ContentType contentTypeAnno = method.getDeclaredAnnotation(ContentType.class);
                String contentType = Objects.isNull(contentTypeAnno) ? DEFAULT_CONTENT_TYPE : contentTypeAnno.value();
                ResponseHandler responseHandler = getResponseHandler(contentType, method);
                res.add(new Invoker(contentType, bean, method, methodEnum, fixPathWithoutSlash(path), responseHandler, argNameDiscover));
            }
        }
        return res;
    }



    private String fixPathWithSlash(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    private String fixPathWithoutSlash(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }

    @Override
    public int priority() {
        return PRIORITY;
    }

}
