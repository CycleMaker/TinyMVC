package org.tiny.mvc.common;

import org.tiny.mvc.anno.*;

import java.lang.annotation.Annotation;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-20 15 :50
 * @description
 */
public enum MethodEnum {
    GetMethod(GetMapping.class),
    PostMethod(PostMapping.class),
    DeleteMethod(DeleteMapping.class),
    PutMethod(PutMapping.class)
    ;
    private Class annoClass;

    public Class getAnnoClass() {
        return annoClass;
    }

    public static String getPath(Annotation annotation) {
        Class<? extends Annotation> type = annotation.annotationType();
        if (type == GetMapping.class) {
            return ((GetMapping)annotation).value();
        }
        if (type == PostMapping.class) {
            return ((PostMapping)annotation).value();
        }
        if (type == PutMapping.class) {
            return ((PutMapping)annotation).value();
        }
        if (type == DeleteMapping.class) {
            return ((DeleteMapping)annotation).value();
        }
        throw new RuntimeException();
    }

    MethodEnum(Class anno) {
        this.annoClass = anno;
    }
}
