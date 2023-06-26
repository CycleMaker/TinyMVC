package org.tiny.mvc.core;

import lombok.Data;
import lombok.ToString;
import org.tiny.mvc.common.MVCPath;
import org.tiny.mvc.common.MethodEnum;
import org.tiny.mvc.core.ArgNameDiscover;
import org.tiny.mvc.core.response.handler.ResponseHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 15 :52
 * @description
 */
@Data
@ToString
public class Invoker {
    private Object bean;
    private Method method;
    private MethodEnum methodEnum;
    private MVCPath mvcPath;
    private ResponseHandler responseHandler;
    private ArgNameDiscover argNameDiscover;
    private String contentType;
    private String originPath;

    public Invoker(String contentType, Object bean, Method method, MethodEnum methodEnum, String path, ResponseHandler responseHandler, ArgNameDiscover argNameDiscover) {
        this.contentType = contentType;
        this.bean = bean;
        this.method = method;
        this.originPath = path;
        this.mvcPath = new MVCPath(path);
        this.responseHandler = responseHandler;
        this.methodEnum = methodEnum;
        this.argNameDiscover = argNameDiscover;
    }

    public boolean matchPath(String requestPath) {
        return mvcPath.isSupport(requestPath);
    }


    public Object invoke(List<Object> args){
        try {
            if (args == null || args.size() == 0) {
                return this.method.invoke(bean,null);
            }
            return this.method.invoke(bean, args.toArray());
        } catch (InvocationTargetException | IllegalAccessException e)  {
            throw new RuntimeException("Method cannot access:" + this);
        }
    }
}
