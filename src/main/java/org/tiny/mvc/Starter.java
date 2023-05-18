package org.tiny.mvc;

import org.tiny.mvc.anno.Controller;
import org.tiny.spring.TinyApplication;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-19 16 :32
 * @description
 */
@Controller
public class Starter {
    public static void main(String[] args) {
        TinyApplication.run(Starter.class);
    }
}
