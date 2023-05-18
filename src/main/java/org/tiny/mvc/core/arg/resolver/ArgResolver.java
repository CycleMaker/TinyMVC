package org.tiny.mvc.core.arg.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-04 11 :22
 * @description
 */
public interface ArgResolver {
    Object resolveArg(Class argClass, String argName, Annotation[] argAnnos, HttpServletRequest req, HttpServletResponse response);
}
