package org.tiny.mvc.common;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-16 19 :57
 * @description
 */
public enum ContentTypeEnum {
    JSON("application/json"),
    HTML("text/html;charset=UTF-8");
    private String type;
    ContentTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
