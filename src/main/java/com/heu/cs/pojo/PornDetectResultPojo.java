package com.heu.cs.pojo;

/**
 * Created by memgq on 2017/6/7.
 */
public class PornDetectResultPojo {
    private int code;
    private String message;
    private String url;
    private PornDetectDataPojo data;

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

    public PornDetectDataPojo getData() {
        return data;
    }

    public void setData(PornDetectDataPojo data) {
        this.data = data;
    }
}
