package org.tiny.mvc.util;

import java.util.LinkedHashSet;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-06-16 17 :35
 * @description
 */
public class ThreadUnsafeFixedCapacitySet extends LinkedHashSet {

    private int maxSize;

    public ThreadUnsafeFixedCapacitySet(int fixSize) {
        super();
        this.maxSize = fixSize;
    }

    @Override
    // thread unsafe
    public boolean add(Object o) {
        if (this.size() >= maxSize){
            Object key = iterator().next();
            super.remove(key);
        }
        return super.add(o);
    }
}
