package org.tiny.mvc.core;

import com.alibaba.fastjson2.JSONObject;
import org.tiny.mvc.common.ContentTypeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-16 20 :05
 * @description
 */
public class JSONResponseHandler implements ResponseHandler{

    @Override
    public void handleResponse(String contentType, HttpServletResponse response, Method method, Object res) throws IOException {
        if (res instanceof String) {
            response.getWriter().write((String)res);
        } else {
            response.getWriter().write(JSONObject.toJSONString(res));
        }
    }

    @Override
    public boolean isSupport(String contentType, Method method) {
        return ContentTypeEnum.JSON.getType().equals(contentType);
    }
}
