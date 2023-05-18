package org.tiny.mvc.anno;

import org.tiny.spring.annotation.Bean;

import java.lang.annotation.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 16 :09
 * @description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {
}
