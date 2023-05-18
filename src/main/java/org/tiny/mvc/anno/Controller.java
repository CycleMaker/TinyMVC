package org.tiny.mvc.anno;

import org.tiny.spring.annotation.Bean;

import java.lang.annotation.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-19 16 :31
 * @description
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface Controller {
    String path() default "/";

}
