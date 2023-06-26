package org.tiny.mvc.core.response.handler;

import org.tiny.mvc.common.ContentTypeEnum;
import org.tiny.mvc.core.response.handler.model.MVCView;
import org.tiny.mvc.util.ByteArrayList;
import org.tiny.spring.core.InitializingBean;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-06-21 10 :15
 * @description
 */
public class HTMLResponseHandler implements ResponseHandler, InitializingBean {

    private static final String HTML_PATH = "web/html";

    private static final Map<String,String> htmlContentMap = new HashMap<>();
    @Override
    public void handleResponse(String contentType, HttpServletResponse response, Method method, Object res) throws IOException {
        response.setContentType(ContentTypeEnum.HTML.getType());
        if (res instanceof String) {
            response.getWriter().write((String)res);
        } else if (res instanceof MVCView) {
            MVCView view = (MVCView) res;
            response.getWriter().write(htmlContentMap.get(view.getResource()));
        }
    }


    public void scanWebContent() throws IOException {
        Enumeration<URL> urls = this.getClass().getClassLoader().getResources(HTML_PATH);
        if (urls.hasMoreElements()) {
            URL directoryURL = urls.nextElement();
            File directory = new File(directoryURL.getPath());
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (Objects.nonNull(files)) {
                    for (File file : files) {
                        htmlContentMap.put(file.getName(), readFromFile(file));
                    }
                }
            }
        }
    }

    @Override
    public boolean isSupport(String contentType, Method method) {
        return contentType.contains("text/html");
    }

    @Override
    public void afterPropertiesSet() {
        try {
            scanWebContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readFromFile(File f){
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            ByteArrayList arrayList = new ByteArrayList();
            for (;;) {
                int readByte = fileInputStream.read();
                if (readByte == -1) { break; }
                arrayList.add((byte)readByte);
            }
            return new String(arrayList.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
