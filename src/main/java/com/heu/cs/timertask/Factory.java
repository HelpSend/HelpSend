package com.heu.cs.timertask;

import com.heu.cs.cache.Group;
import com.heu.cs.cache.GroupCacheFactory;

/**
 * Created by memgq on 2017/6/1.
 */
public interface Factory {

    public GroupCacheFactory factory=new GroupCacheFactory();
    public Group groupGrabOrder=factory.group("groupGrabOrder");

}
