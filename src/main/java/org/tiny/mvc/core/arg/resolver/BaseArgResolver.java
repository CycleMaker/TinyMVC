package org.tiny.mvc.core.arg.resolver;

import com.alibaba.fastjson2.JSONObject;
import org.tiny.mvc.anno.RequestBody;
import org.tiny.mvc.anno.RequestParam;
import org.tiny.mvc.util.ByteArrayList;
import org.tiny.spring.common.Converter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-04 11 :45
 * @description
 */
public class BaseArgResolver implements ArgResolver {
    @Override
    public Object resolveArg(Class argClass, String argName, Annotation[] argAnnos, HttpServletRequest req, HttpServletResponse response) {
        if (argClass == HttpServletRequest.class) {
            return req;
        } else if (argClass == HttpServletResponse.class) {
            return response;
        } else {
            for (Annotation anno : argAnnos) {
                if (anno.annotationType() == RequestBody.class) {
                    return parseRequestBody(argClass, req);
                }
                if (anno.annotationType() == RequestParam.class) {
                    RequestParam reqAnno = (RequestParam) anno;
                    return parseRequestParam(argClass, argName, reqAnno, req);
                }
            }
        }
        return null;
    }

    private Object parseRequestParam(Class argClass, String argName, RequestParam anno, HttpServletRequest req) {
        if (anno.value() != null && anno.value().length() > 0) {
            String value = req.getParameter(anno.value());
            return Converter.convertStrToBaseType(value, argClass);
        }
        return Converter.convertStrToBaseType(req.getParameter(argName), argClass);
    }

    private <T> T parseRequestBody(Class<T> argClass, HttpServletRequest req) {
        try {
            String content = readFromStream(req.getInputStream());
            return JSONObject.parseObject(content, argClass);
        } catch (Exception e) {
            throw new RuntimeException("IOException");
        }
    }

    private String readFromStream(ServletInputStream inputStream) throws IOException {
        ByteArrayList arrayList = new ByteArrayList();
        while (!inputStream.isFinished()) {
            arrayList.add((byte) inputStream.read());
        }
        return new String(arrayList.get());
    }
}
