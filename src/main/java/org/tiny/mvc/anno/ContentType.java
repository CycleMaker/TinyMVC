package org.tiny.mvc.anno;

import org.tiny.mvc.common.ContentTypeEnum;
import org.tiny.spring.annotation.Bean;

import java.lang.annotation.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-16 19 :34
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface ContentType {
    String value() default "";
    // if value field is not blank,this field will ignore
    ContentTypeEnum type() default ContentTypeEnum.JSON;
}
