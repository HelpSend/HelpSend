package com.heu.cs.pojo.Identify;

import java.util.List;

/**
 * Created by memgq on 2017/6/17.
 */
public class IdCardBackResultPojo {
    private int code;//	int	服务器错误码，0为成功
    private String message;//	string	服务器返回的信息
    private String filename;//	string	当前图片的filename，与请求包中filename一致
    private IdCardBackDataPojo data;//		具体查询数据，内容见下表

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public IdCardBackDataPojo getData() {
        return data;
    }

    public void setData(IdCardBackDataPojo data) {
        this.data = data;
    }
}
