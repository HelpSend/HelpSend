package com.heu.cs.dao.IdentifyDao;

import com.heu.cs.utils.TencentYouTu;
import com.heu.cs.utils.TencentYouTuImpl;
import com.heu.cs.utils.UploadFile;
import com.heu.cs.utils.UploadFileImpl;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by memgq on 2017/6/18.
 */
public class GetIdentifyInfoDao {
    public void getIdentify(String textInfo,FormDataMultiPart formDataMultiPart){
        FormDataBodyPart idcardFace=formDataMultiPart.getField("idcardFace");
        FormDataBodyPart idcardBack=formDataMultiPart.getField("idcardBack");
        FormDataBodyPart realHead=formDataMultiPart.getField("realHead");
        List<FormDataBodyPart> list= new ArrayList<>();
        list.add(idcardFace);
        list.add(idcardBack);
        list.add(realHead);
        UploadFile uploadFile=new UploadFileImpl();
        String ROOTPATH = System.getProperty("user.dir");
        String RESOURCE_DIR="/src/main/identify_images/";
        String imageDir=ROOTPATH+RESOURCE_DIR;
        String[] identifyImagesPathList=new String[3];
        String[] nameList=new String[3];
        for(int i=0;i<list.size();i++){
            FormDataBodyPart part=list.get(i);
            String fileName=part.getContentDisposition().getFileName();
            InputStream inputStream=part.getValueAs(InputStream.class);
            uploadFile.uploadAndCompressImage(inputStream,imageDir,fileName);
            identifyImagesPathList[i]=imageDir+fileName;
            nameList[i]=fileName;
        }
        TencentYouTu tencentYouTu=new TencentYouTuImpl();
        tencentYouTu.ocrIdCard(textInfo,identifyImagesPathList,nameList);
    }
}
