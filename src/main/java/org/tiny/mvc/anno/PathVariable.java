package org.tiny.mvc.anno;

import java.lang.annotation.*;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-06-12 20 :13
 * @description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {
}
