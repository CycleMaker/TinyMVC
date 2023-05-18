package org.tiny.mvc.common;

import lombok.Data;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    public Invoker(Object bean,Method method) {
        this.bean = bean;
        this.method = method;
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
