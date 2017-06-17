package com.heu.cs.pojo.Identify;

/**
 * Created by memgq on 2017/6/17.
 */
public class IdCardFaceResultPojo {
    private int code;//	int	服务器错误码，0为成功
    private String message;//	string	服务器返回的信息
    private String url;//	string	当前图片的url
    private IdCardFaceDataPojo data;//		具体查询数据，内容见下表


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IdCardFaceDataPojo getData() {
        return data;
    }

    public void setData(IdCardFaceDataPojo data) {
        this.data = data;
    }
}
