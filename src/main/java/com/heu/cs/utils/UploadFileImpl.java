package com.heu.cs.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by memgq on 2017/6/17.
 */
public class UploadFileImpl implements UploadFile{

    @Override
    public String  uploadAndCompressImage(InputStream inputStream, String imageDir, String imageName) {
        File file = new File(imageDir+imageName);
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
            ImageCompress imageCompress=new ImageCompress();
            imageCompress.compressPic(imageDir,imageDir,imageName,imageName,500,500,true);
            return "1";
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }

    }
}
