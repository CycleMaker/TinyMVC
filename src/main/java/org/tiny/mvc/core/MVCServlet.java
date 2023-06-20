package org.tiny.mvc.core;

import org.tiny.mvc.common.GlobalExceptionAspect;
import org.tiny.mvc.common.Invoker;
import org.tiny.mvc.common.MethodEnum;
import org.tiny.mvc.core.arg.resolver.ArgResolverManager;
import org.tiny.spring.annotation.Extension;
import org.tiny.spring.core.aop.AopContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;


public class MVCServlet extends HttpServlet {

    private String servletName;

    private String path;

    private List<Invoker> invokers;

    private static ArgResolverManager argResolverManager = ArgResolverManager.getInstance();

    public String getPath() {
        return this.path;
    }


    public String getServletName() {
        return servletName;
    }

    public MVCServlet(String servletName, String path, List<Invoker> invokers) {
        if (invokers == null || invokers.size() == 0) {
            throw new IllegalArgumentException();
        }
        this.servletName = servletName;
        this.path = path;
        this.invokers = invokers;
    }

    // only for cglib
    public MVCServlet() {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAgent().handle(req, resp, MethodEnum.GetMethod);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        getAgent().handle(req, resp, MethodEnum.PostMethod);
    }


    @Extension(aspect = GlobalExceptionAspect.class)
    public void handle(HttpServletRequest req, HttpServletResponse resp, MethodEnum methodEnum) throws IOException {
        Optional<Invoker> invoker = invokers.stream().filter(item -> item.matchPath(req.getRequestURI())).findFirst();
        if (invoker.isPresent()) {
            invoke(invoker.get(), req, resp);
        }
    }

    private MVCServlet getAgent() {
        MVCServlet agent = AopContext.getAgent(this.getClass());
        if (Objects.nonNull(agent)) {
            return agent;
        }
        return this;
    }

    private void invoke(Invoker invoker, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Object> args = resolveArgument(invoker.getMethod(), req, resp, invoker);
        Object res = invoker.invoke(args);
        invoker.getResponseHandler().handleResponse(invoker.getContentType(), resp, invoker.getMethod(), res);
    }

    public List<Object> resolveArgument(Method method, HttpServletRequest req, HttpServletResponse resp, Invoker invoker) {
        List<Object> arguments = new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();
        if (parameterTypes.length == 0) {
            return null;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            Class clazz = parameterTypes[i];
            arguments.add(argResolverManager.resolveArg(clazz, invoker.getArgNameDiscover().getArg(method, i), parameters[i].getDeclaredAnnotations(), req, resp, invoker));
        }
        return arguments;
    }


}
