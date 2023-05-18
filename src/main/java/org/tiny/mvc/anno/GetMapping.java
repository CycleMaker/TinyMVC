package org.tiny.mvc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 17 :05
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    String value() default "/";
}
