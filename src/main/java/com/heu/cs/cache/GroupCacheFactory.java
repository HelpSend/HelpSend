package com.heu.cs.cache;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 简单的内存缓存实现，实现group概念，<br/>
 * 一个group里面是个有序的集合，<br/>
 * 集合支持key-value expire弥补redis list的不足
 *
 */
public class GroupCacheFactory {

    // 数据容器
    private Map<String, Object> container;

    public GroupCacheFactory() {
        container = new LinkedHashMap<String, Object>();
    }

    /**
     * 如果组存在就返回，不存在就创建，保证不为null
     *
     * @param key
     * @return
     */
    public Group group(String key, int capacity) {

        Group group = null;
        Object entry = container.get(key);
        if (entry != null) {
            group = (Group) entry;
        } else {
            group = new Group(capacity);
            container.put(key, group);
        }

        return group;
    }

    /**
     * 如果组存在就返回，不存在就创建，默认容量1000
     *
     * @param key
     * @return
     */
    public Group group(String key) {

        return this.group(key, 1000);
    }
}