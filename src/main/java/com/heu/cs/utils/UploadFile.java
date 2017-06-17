package com.heu.cs.utils;

import java.io.InputStream;

/**
 * Created by memgq on 2017/6/17.
 */
public interface UploadFile {
    String uploadAndCompressImage(InputStream inputStream, String imageDir, String imageName);
}
