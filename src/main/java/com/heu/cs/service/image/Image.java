package com.heu.cs.service.image;
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
import com.heu.cs.service.image.request.FaceCompareRequest;
import com.heu.cs.service.image.request.FaceIdCardCompareRequest;
import com.heu.cs.service.image.request.FaceLiveGetFourRequest;
import com.heu.cs.service.image.request.FaceIdCardLiveDetectFourRequest;
import com.heu.cs.service.image.request.FaceLiveDetectFourRequest;
import com.heu.cs.service.image.request.FaceVerifyRequest;

/**
 * @author chengwu
 * Image提供给用户使用的API接口
 */

public interface Image {

	/**
	 * 黄图识别接口
	 * 
	 * @param request
	 *            黄图识别请求
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */
    String pornDetect(PornDetectRequest request);
    
    /**
	 *标签识别接口
	 * 
	 * @param request
	 *            标签识别请求
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */
    String tagDetect(TagDetectRequest request);
    
    /**
	 *身份证识别接口
	 * 
	 * @param request
	 *            身份证识别请求
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */
    String idcardDetect(IdcardDetectRequest request);
    
        /**
	 *名片识别接口
	 * 
	 * @param request
	 *            名片ocr识别请求
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */
    String namecardDetect(NamecardDetectRequest request);
    
        /**
	 *人脸检测接口
	 * 
	 * @param request
	 *            人连检测请求
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */
    String faceDetect(FaceDetectRequest request);
    
        /**
	 *人脸定位接口
	 * 
	 * @param request
	 *            人脸定位接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */
    String faceShape(FaceShapeRequest request);
    
        /**
	 *个体创建接口
	 * 
	 * @param request
	 *            个体创建接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceNewPerson(FaceNewPersonRequest request);
     
     /**
	 *个体删除接口
	 * 
	 * @param request
	 *            个体删除接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceDelPerson(FaceDelPersonRequest request);
     
      /**
	 *增加人脸接口
	 * 
	 * @param request
	 *            增加人脸接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceAddFace(FaceAddFaceRequest request);
     
        /**
	 *删除人脸接口
	 * 
	 * @param request
	 *            删除人脸接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceDelFace(FaceDelFaceRequest request);

     /**
	 *个体设置信息接口
	 * 
	 * @param request
	 *            人脸设置信息接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceSetInfo(FaceSetInfoRequest request);
     
     /**
	 *个体获取信息接口
	 * 
	 * @param request
	 *            个体获取信息接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceGetInfo(FaceGetInfoRequest request);
     
      /**
	 *获取组列表接口
	 * 
	 * @param request
	 *            获取组列表接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceGetGroupIds(FaceGetGroupIdsRequest request);
     
      /**
	 *获取人列表接口
	 * 
	 * @param request
	 *            获取人列表接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceGetPersonIds(FaceGetPersonIdsRequest request);
     
    /**
	 *获取人脸列表接口
	 * 
	 * @param request
	 *            获取人脸列表接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceGetFaceIds(FaceGetFaceIdsRequest request);
     
      /**
	 *获取人脸信息接口
	 * 
	 * @param request
	 *            获取人脸信息接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceGetFaceInfo(FaceGetFaceInfoRequest request);
     
       /**
	 *获取人脸识别接口
	 * 
	 * @param request
	 *            获取人脸识别接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceIdentify(FaceIdentifyRequest request);
     
     /**
	 *人脸验证接口
	 * 
	 * @param request
	 *            人脸验证接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceVerify(FaceVerifyRequest request);
     
      /**
	 *人脸对比接口
	 * 
	 * @param request
	 *            人脸对比接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceCompare(FaceCompareRequest request);
     
     /**
	 *身份证对比接口
	 * 
	 * @param request
	 *            身份证对比接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceIdCardCompare(FaceIdCardCompareRequest request);
     
        /**
	 *获取验证码接口
	 * 
	 * @param request
	 *            获取验证码接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceLiveGetFour(FaceLiveGetFourRequest request);
     
       /**
	 *身份对比接口
	 * 
	 * @param request
	 *            身份对比接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceIdCardLiveDetectFour(FaceIdCardLiveDetectFourRequest request);
     
      /**
	 *身份检测接口
	 * 
	 * @param request
	 *            身份检测接口
	 * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功,
	 *         其他为失败, message为success或者失败原因
	 */    
     String faceLiveDetectFour(FaceLiveDetectFourRequest request);
     
    /**
     * 关闭Image客户端连接池，释放涉及的资源，释放后，不能再使用Image的接口，必须重新生成一个新对象
     */
    void shutdown();

}
