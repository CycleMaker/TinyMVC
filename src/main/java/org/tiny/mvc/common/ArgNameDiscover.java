package org.tiny.mvc.common;

import com.github.houbb.asm.tool.reflection.AsmMethods;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-27 16 :23
 * @description
 */
public class ArgNameDiscover {
    private Map<Method, List<String>> argNameMap = new ConcurrentHashMap<>();
    public String getArg(Method m, int i) {
        List<String> args = argNameMap.get(m);
        if (Objects.nonNull(args)) {
            return args.get(i);
        } else {
            args = AsmMethods.getParamNamesByAsm(m);
            argNameMap.put(m, args);
            return args.get(i);
        }
    }

    public List<String> discover(Method m) {
        List<String> args = argNameMap.get(m);
        if (Objects.nonNull(args)) {
            return args;
        }
        args = AsmMethods.getParamNamesByAsm(m);
        argNameMap.put(m, args);
        return args;
    }

}
