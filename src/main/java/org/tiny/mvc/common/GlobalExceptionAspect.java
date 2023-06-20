package org.tiny.mvc.common;


import org.tiny.spring.core.aop.Aspect;

import java.lang.reflect.Method;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-31 17 :20
 * @description
 */
public class GlobalExceptionAspect implements Aspect {
    @Override
    public void before(Object o, Method method, Object[] objects) {
        System.out.println("before");
    }

    @Override
    public void after(Object o, Method method, Object[] objects, Object o1) {
        System.out.println("after");
    }

    @Override
    public Object afterException(Object o, Method m, Object[] args, Exception e) throws Exception {
        return Aspect.super.afterException(o, m, args, e);
    }
}
