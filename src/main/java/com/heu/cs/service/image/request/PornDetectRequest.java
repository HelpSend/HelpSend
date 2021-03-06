/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heu.cs.service.image.request;

import java.util.ArrayList;
import java.util.HashMap;

import com.heu.cs.service.image.ClientConfig;
import com.heu.cs.service.image.common_utils.CommonParamCheckUtils;
import com.heu.cs.service.image.exception.ParamException;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLEncoder;

/**
 *
 * @author jusisli 黄图识别请求
 */
public class PornDetectRequest extends AbstractBaseRequest {
        //黄图识别的类型，是否是url识别
        private boolean isUrl;
        
        //设置列表传参的key
        private HashMap<String, String> keyList = new HashMap<String, String>();

    	// url列表
	private ArrayList<String> urlList = new ArrayList<String>();
        
	// 图片内容列表,key=image name
        private HashMap<String, String> imageList = new HashMap<String, String>();
        
	public PornDetectRequest(String bucketName, String[] urlList) {
		super(bucketName);
		this.isUrl = true;
                for(int i = 0; i < urlList.length; i++){
                    this.urlList.add(urlList[i]);
                }
	}

        public PornDetectRequest(String bucketName, String[] name, String[] image) {
		super(bucketName);
		this.isUrl = false;
                String pornName;
                for(int i = 0; i < name.length; i++){
                    try {
                        pornName = URLEncoder.encode(name[i],"UTF-8");
                        this.imageList.put(pornName, image[i]);
                        this.keyList.put(pornName, String.format( "image[%d]", i));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(PornDetectRequest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
	}
        
        public boolean isUrl() {
            return isUrl;
        }
        
        public ArrayList<String> getUrlList() {
            return urlList;
        }
        
        public HashMap<String, String> getKeyList() {
            return keyList;
        }

        public void setUrlList(ArrayList<String> urlList) {
            this.urlList = urlList;
        }

        public HashMap<String, String> getImageList() {
            return imageList;
        }

        public void setImageList(HashMap<String, String> imageList) {
            this.imageList = imageList;
        }

	@Override
	public void check_param() throws ParamException {
		super.check_param();
                if(isUrl){
                    CommonParamCheckUtils.AssertNotZero("url list", urlList.size());
                    CommonParamCheckUtils.AssertExceed("url list", urlList.size(), ClientConfig.getMaxDetectionNum());
                }else{
                    CommonParamCheckUtils.AssertNotZero("image list", imageList.size());
                    CommonParamCheckUtils.AssertExceed("image list", imageList.size(), ClientConfig.getMaxDetectionNum());
                }
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
                if(isUrl){
                    sb.append(", [");
                    for(String url : urlList){
                        sb.append(url).append(", ");
                    }
                    sb.append("]");
                }else{
                    sb.append(", [");
                    for(String name : imageList.keySet()){
                        sb.append(name).append(", ");
                    }
                    sb.append("]");
                }
		return sb.toString();
	}
}
