package org.tiny.mvc.core.arg.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-04 11 :25
 * @description
 */

public class ArgResolverManager {

    private List<ArgResolver> resolvers;

    private static ArgResolverManager instance = new ArgResolverManager();


    private ArgResolverManager() {
        resolvers = new ArrayList<>();
    }

    public Object resolveArg(Class argClass, String argName, Annotation[] argAnnos, HttpServletRequest req, HttpServletResponse response) {
        for (ArgResolver resolver : resolvers) {
            Object arg = resolver.resolveArg(argClass, argName, argAnnos, req, response);
            if (Objects.nonNull(arg)) {
                return arg;
            }
        }
        return null;
    }

    public synchronized void addArgResolver(ArgResolver argResolver) {
        if (argResolver != null) {
            resolvers.add(argResolver);
        }
    }

    public static ArgResolverManager getInstance() {
        return instance;
    }


}
