package org.tiny.mvc.core;
import org.tiny.mvc.common.ArgNameDiscover;
import org.tiny.mvc.common.Invoker;
import org.tiny.mvc.common.MethodEnum;
import org.tiny.mvc.core.arg.resolver.ArgResolverManager;
import org.tiny.mvc.core.response.handler.ResponseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 15 :47
 * @description
 */
public class MVCServlet extends HttpServlet {

    private ArgNameDiscover argNameDiscover;

    private ResponseHandler responseHandler;

    private Map<MethodEnum, Invoker> httpMethodMap;

    private String contentType;

    private String servletName;

    private String path;

    private static ArgResolverManager argResolverManager = ArgResolverManager.getInstance();

    public String getPath() {
        return this.path;
    }


    public String getServletName() {
        return servletName;
    }

    public MVCServlet(Map<MethodEnum, Invoker> httpMethodMap, String servletName, String path, ArgNameDiscover argNameDiscover, String contentType, ResponseHandler responseHandler) {
        this.httpMethodMap = httpMethodMap;
        this.servletName = servletName;
        this.path = path;
        this.argNameDiscover = argNameDiscover;
        this.contentType = contentType;
        this.responseHandler = responseHandler;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Invoker getInvoker = httpMethodMap.get(MethodEnum.GetMethod);
        if (Objects.nonNull(getInvoker)) {
            invoke(getInvoker, req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
        Invoker postInvoker = httpMethodMap.get(MethodEnum.PostMethod);
        if (Objects.nonNull(postInvoker)) {
            invoke(postInvoker, req, resp);
        }
    }

    private void invoke(Invoker invoker, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Object> args = resolveArgument(invoker.getMethod(), req, resp);
        Object res = invoker.invoke(args);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(contentType);
        responseHandler.handleResponse(contentType, resp, invoker.getMethod(), res);
    }

    public List<Object> resolveArgument(Method method, HttpServletRequest req, HttpServletResponse resp) {
        List<Object> arguments = new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();
        if (parameterTypes.length == 0) {
            return null;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            Class clazz = parameterTypes[i];
            arguments.add(argResolverManager.resolveArg(clazz, argNameDiscover.getArg(method, i), parameters[i].getDeclaredAnnotations(), req, resp));
        }
        return arguments;
    }


}
