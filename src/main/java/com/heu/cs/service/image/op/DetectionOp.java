/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heu.cs.service.image.op;

import com.heu.cs.service.image.ClientConfig;
import com.heu.cs.service.image.common_utils.CommonCodecUtils;
import com.heu.cs.service.image.http.HttpContentType;
import com.heu.cs.service.image.http.HttpMethod;
import com.heu.cs.service.image.sign.Sign;
import com.heu.cs.service.image.http.HttpRequest;
import com.heu.cs.service.image.sign.Credentials;
import com.heu.cs.service.image.exception.AbstractImageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heu.cs.service.image.http.AbstractImageHttpClient;
import com.heu.cs.service.image.http.RequestBodyKey;
import com.heu.cs.service.image.http.RequestHeaderKey;
import com.heu.cs.service.image.request.PornDetectRequest;
import com.heu.cs.service.image.request.TagDetectRequest;
import com.heu.cs.service.image.request.IdcardDetectRequest;
import com.heu.cs.service.image.request.NamecardDetectRequest;
import com.heu.cs.service.image.request.FaceDetectRequest;
import com.heu.cs.service.image.request.FaceShapeRequest;
import com.heu.cs.service.image.request.FaceNewPersonRequest;
import com.heu.cs.service.image.request.FaceDelPersonRequest;
import com.heu.cs.service.image.request.FaceAddFaceRequest;
import com.heu.cs.service.image.request.FaceDelFaceRequest;
import com.heu.cs.service.image.request.FaceSetInfoRequest;
import com.heu.cs.service.image.request.FaceGetInfoRequest;
import com.heu.cs.service.image.request.FaceGetGroupIdsRequest;
import com.heu.cs.service.image.request.FaceGetPersonIdsRequest;
import com.heu.cs.service.image.request.FaceGetFaceIdsRequest;
import com.heu.cs.service.image.request.FaceGetFaceInfoRequest;
import com.heu.cs.service.image.request.FaceIdentifyRequest;
import com.heu.cs.service.image.request.FaceVerifyRequest;
import com.heu.cs.service.image.request.FaceCompareRequest;
import com.heu.cs.service.image.request.FaceIdCardCompareRequest;
import com.heu.cs.service.image.request.FaceIdCardLiveDetectFourRequest;
import com.heu.cs.service.image.request.FaceLiveDetectFourRequest;
import com.heu.cs.service.image.request.FaceLiveGetFourRequest;


/**
 *
 * @author jusisli 此类封装了图片识别操作
 */
public class DetectionOp extends BaseOp {
    private static final Logger LOG = LoggerFactory.getLogger(DetectionOp.class);

    public DetectionOp(ClientConfig config, Credentials cred, AbstractImageHttpClient client) {
        super(config, cred, client);
    }
    
    /**
     * 黄图识别请求
     * 
     * @param request 黄图识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String pornDetect(PornDetectRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getDetectionPorn();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.addParam(RequestBodyKey.URL_LIST, (request.getUrlList())); 
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON); 
        } else {         
            httpRequest.setImageList(request.getImageList());
            httpRequest.setKeyList(request.getKeyList());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
        /**
     * 标签识别请求
     * 
     * @param request 标签识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String tagDetect(TagDetectRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getDetectionTag();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.setMethod(HttpMethod.POST);
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON); 
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
        } else {
            String image = CommonCodecUtils.Base64Encode(request.getImage());
            httpRequest.addParam(RequestBodyKey.IMAGE, image);                 
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 身份证识别请求
     * 
     * @param request 标签识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String idcardDetect(IdcardDetectRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getDetectionIdcard();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.CARD_TYPE, String.valueOf(request.getCardType()));
        
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL_LIST, (request.getUrlList())); 
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON); 
        } else {
            httpRequest.setImageList(request.getImageList());
            httpRequest.setKeyList(request.getKeyList());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
     /**
     * 名片识别请求
     * 
     * @param request 标签识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String namecardDetect(NamecardDetectRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getDetectionNamecard();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.setMethod(HttpMethod.POST);
        
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.RET_IMAGE, String.valueOf(request.getRetImage()));              
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL_LIST, (request.getUrlList())); 
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON); 
        } else {
            httpRequest.setImageList(request.getImageList());
            httpRequest.setKeyList(request.getKeyList());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 人脸检测请求
     * 
     * @param request 人脸检测请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceDetect(FaceDetectRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getDetectionFace();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.setMethod(HttpMethod.POST);
        
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.MODE, (request.getMode()));        
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
        } else {
            httpRequest.setImage(request.getImage());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 人脸定位请求
     * 
     * @param request 人脸定位请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceShape(FaceShapeRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceShape();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.setMethod(HttpMethod.POST);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.MODE, request.getMode());
        
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
        } else {
            httpRequest.setImage(request.getImage());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 个体创建请求
     * 
     * @param request 个体创建请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceNewPerson(FaceNewPersonRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceNewPerson();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, request.getPersonId());
        httpRequest.addParam(RequestBodyKey.PERSON_NAME, request.getPersonName());        
        httpRequest.addParam(RequestBodyKey.TAG, request.getPersonTag());
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
            httpRequest.addParam(RequestBodyKey.GROUP_IDS, request.getGroupIds());
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
        } else {
            int index;
            String[] groupIds = request.getGroupIds();
            for (index = 0;index < groupIds.length; index++) {
                String key =  String.format("group_ids[%d]", index);
                String data = groupIds[index];
                httpRequest.addParam(key, data); 
            } 
            httpRequest.setImage(request.getImage());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 个体删除请求
     * 
     * @param request 个体删除请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceDelPerson(FaceDelPersonRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceDelPerson();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, request.getPersonId());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
      /**
     * 增加人脸请求
     * 
     * @param request 增加人脸请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceAddFace(FaceAddFaceRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceAddFace();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, String.valueOf(request.getPersonId()));
        httpRequest.addParam(RequestBodyKey.TAG, String.valueOf(request.getPersonTag()));        
        httpRequest.setMethod(HttpMethod.POST);       
        if (request.isUrl()) {
            httpRequest.addParam(RequestBodyKey.URLS, (request.getUrlList()));  
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON); 
        } else {
            httpRequest.setKeyList(request.getKeyList());
            httpRequest.setImageList(request.getImageList());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
     /**
     * 人脸删除请求
     * 
     * @param request 人脸删除请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceDelFace(FaceDelFaceRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceDelFace();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, request.getPersonId());
        httpRequest.addParam(RequestBodyKey.FACE_IDS, request.getFaceIds());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
     /**
     * 人脸设置信息请求
     * 
     * @param request 人脸设置信息请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceSetInfo(FaceSetInfoRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceSetInfo();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, request.getPersonId());
        httpRequest.addParam(RequestBodyKey.PERSON_NAME, request.getPersonName());
        httpRequest.addParam(RequestBodyKey.TAG, request.getPersonTag());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 人脸获取信息请求
     * 
     * @param request 人脸获取信息请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceGetInfo(FaceGetInfoRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceGetInfo();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, request.getPersonId());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);      
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 获取组列表请求
     * 
     * @param request 获取组列表请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceGetGroupIds(FaceGetGroupIdsRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceGetGroupIdsInfo();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     *  获取人列表请求
     * 
     * @param request  获取人列表请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceGetPersonIds(FaceGetPersonIdsRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceGetPersonIdsInfo();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.GROUP_ID, request.getGroupId());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
     /**
     *  获取人脸列表请求
     * 
     * @param request  获取人脸列表请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceGetFaceIds(FaceGetFaceIdsRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceGetFaceIdsInfo();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, request.getPersonId());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     *  获取人脸信息请求
     * 
     * @param request  获取人脸信息请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceGetFaceInfo(FaceGetFaceInfoRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceGetFaceInfo();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.FACE_ID, request.getFaceId());
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 人脸识别请求
     * 
     * @param request 人脸识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceIdentify(FaceIdentifyRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceIdentify();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.GROUP_ID, (request.getGroupId()));
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
        } else {
            httpRequest.setImage(request.getImage());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
     /**
     * 人脸验证请求
     * 
     * @param request 人脸验证请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceVerify(FaceVerifyRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceVerify();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.PERSON_ID, (request.getPersonId()));
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
        } else {
            httpRequest.setImage(request.getImage());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    /**
     * 人脸对比请求
     * 
     * @param request 人脸对比请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceCompare(FaceCompareRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceCompare();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());  
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URLA, (request.getUrlA()));
            httpRequest.addParam(RequestBodyKey.URLB, (request.getUrlB()));
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
        } else {
            httpRequest.setImageList(request.getImageList());
            httpRequest.setKeyList(request.getKeyList());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 身份证识别对比接口
     * 
     * @param request 身份证识别对比接口参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceIdCardCompare(FaceIdCardCompareRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceIdcardCompare();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.IDCARDNUMBER, (request.getIdcardNumber()));
        httpRequest.addParam(RequestBodyKey.IDCARDNAME, (request.getIdcardName()));
        httpRequest.addParam(RequestBodyKey.SESSONID, request.getSessionId());
        
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
        } else {
            httpRequest.setImage(request.getImage());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     *  获取验证码请求
     * 
     * @param request  获取验证码请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceLiveGetFour(FaceLiveGetFourRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceLiveGetFour();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        if ( (request.getSeq()!=null) && (request.getSeq()).trim().length() != 0 ) {
            httpRequest.addParam(RequestBodyKey.SEQ, request.getSeq());
        }
        httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
        httpRequest.setContentType(HttpContentType.APPLICATION_JSON);  
    
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 人脸识别请求
     * 
     * @param request 人脸识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceIdCardLiveDetectFour(FaceIdCardLiveDetectFourRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceIdCardLiveDetectFour();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.IDCARDNUMBER, (request.getIdcardNumber()));
        httpRequest.addParam(RequestBodyKey.IDCARDNAME, (request.getIdcardName()));
        httpRequest.addParam(RequestBodyKey.VALIDATE_DATA, (request.getValidate()));
        if ( (request.getSeq()!=null) && (request.getSeq()).trim().length() != 0 ) {
            httpRequest.addParam(RequestBodyKey.SEQ, request.getSeq());
        }
        httpRequest.setImage(request.getVideo());
        httpRequest.setImageKey("video");
        httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        httpRequest.setMethod(HttpMethod.POST);  
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    /**
     * 检测接口请求
     * 
     * @param request 检测接口请求
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String faceLiveDetectFour(FaceLiveDetectFourRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getFaceLiveDetectFour();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.VALIDATE_DATA, request.getValidate());
        httpRequest.addParam(RequestBodyKey.COMPARE_FLAG, request.getCompareFlag());
        if ( (request.getSeq()!=null) && (request.getSeq()).trim().length() != 0 ) {
            httpRequest.addParam(RequestBodyKey.SEQ, request.getSeq());
        }
          
        httpRequest.setImageList(request.getImageList());
        httpRequest.setKeyList(request.getKeyList());
        httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
   
        return httpClient.sendHttpRequest(httpRequest);
    }
    
    
}
