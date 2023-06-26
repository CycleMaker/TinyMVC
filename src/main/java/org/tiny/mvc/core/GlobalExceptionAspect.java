package org.tiny.mvc.core;


import org.tiny.spring.core.aop.Aspect;

import java.lang.reflect.Method;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-31 17 :20
 * @description
 */
public class GlobalExceptionAspect implements Aspect {
    @Override
    final public void before(Object o, Method method, Object[] objects) {
        return;
    }

    @Override
    final public void after(Object o, Method method, Object[] objects, Object o1) {
        return;
    }

    @Override
    public Object afterException(Object o, Method m, Object[] args, Exception e) throws Exception {
        return Aspect.super.afterException(o, m, args, e);
    }
}
