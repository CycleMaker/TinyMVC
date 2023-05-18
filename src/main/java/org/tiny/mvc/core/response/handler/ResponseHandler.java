package org.tiny.mvc.core.response.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-16 20 :03
 * @description
 */
public interface ResponseHandler {
    void handleResponse(String contentType, HttpServletResponse response, Method method, Object res) throws IOException;

    boolean isSupport(String contentType, Method method);

    default int priority() { return 100; }
}
