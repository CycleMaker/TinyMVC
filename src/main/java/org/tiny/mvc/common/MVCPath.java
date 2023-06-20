package org.tiny.mvc.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.tiny.mvc.util.ThreadUnsafeFixedCapacitySet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-06-12 19 :29
 * @description
 */
public class MVCPath {

    private static final String SLASH = "/";
    private List<PathItem> pathItems;

    private ThreadUnsafeFixedCapacitySet historyMappedPath = new ThreadUnsafeFixedCapacitySet(3);
    private static final String TEMPLATE_LEFT = "${";

    private static final String TEMPLTE_RIGHT = "}";

    public List<PathItem> getPathItems() {
        return pathItems;
    }


    public MVCPath(String path) {
        String[] pathStrItems = path.split(SLASH);
        pathItems = new ArrayList<PathItem>(pathStrItems.length);
        for (String item : pathStrItems) {
            if (item.startsWith(TEMPLATE_LEFT) && item.endsWith(TEMPLTE_RIGHT)) {
                pathItems.add(new PathItem(null, item.substring(2, item.length() - 1), 1));
            } else {
                pathItems.add(new PathItem(item, null, 0));
            }
        }
    }

    public  Map<String, String> getPathVariableMap(String requestPath) {
        String[] requestItems = requestPath.split(SLASH);
        Map<String, String> res = new HashMap<>();
        for (int i = 0;i <requestItems.length; i++) {
            if (pathItems.get(i).getType() == 1) {
                res.put(pathItems.get(i).getPathArg(), requestItems[i]);
            }
        }
        return res;
    }

    public boolean isSupport(String requestPath) {
        if (historyMappedPath.contains(requestPath)) {
            return true;
        }
        String[] requestItems = requestPath.split(SLASH);
        if (requestItems.length != pathItems.size()) {
            return false;
        }
        for (int i = 0;i < requestItems.length; i++) {
            PathItem pathItem = pathItems.get(i);
            if (pathItem.getType() == 0) {
                if (!pathItem.getPathValue().equals(requestItems[i])) {
                    return false;
                }
            }
        }
        historyMappedPath.add(requestPath);
        return true;
    }

    @Data
    @AllArgsConstructor
    class PathItem {
        private String pathValue;
        // meaningful when type equal 1
        private String pathArg;
        // 0 means normal
        // 1 means pathVariable
        private int type;
    }
}
