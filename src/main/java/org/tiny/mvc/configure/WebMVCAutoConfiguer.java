package org.tiny.mvc.configure;

import org.tiny.mvc.core.banner.MVCBannerPrinter;
import org.tiny.spring.annotation.Autowired;
import org.tiny.spring.annotation.Value;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-18 14 :58
 * @description
 */
public class WebMVCAutoConfiguer {
    @Value(key = "server.port")
    private Integer port;
    @Autowired(require = false)
    private MVCBannerPrinter mvcBannerPrinter;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public MVCBannerPrinter getMvcBannerPrinter() {
        return mvcBannerPrinter;
    }

    public void setMvcBannerPrinter(MVCBannerPrinter mvcBannerPrinter) {
        this.mvcBannerPrinter = mvcBannerPrinter;
    }
}
