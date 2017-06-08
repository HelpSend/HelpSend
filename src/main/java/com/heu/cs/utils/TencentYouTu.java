package com.heu.cs.utils;

import com.heu.cs.service.image.ImageClient;

/**
 * Created by memgq on 2017/6/7.
 */
public interface TencentYouTu {


    String detectPorn(String url,ImageClient imageClient,String bucketName);
}
