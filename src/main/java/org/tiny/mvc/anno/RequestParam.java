package org.tiny.mvc.anno;

import java.lang.annotation.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-24 19 :31
 * @description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
