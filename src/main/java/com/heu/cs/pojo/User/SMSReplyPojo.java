package com.heu.cs.pojo.User;

/**
 * Created by memgq on 2017/6/6.
 */
public class SMSReplyPojo {
    private String base_extend;
    private String extend;
    private String id;
    private String mobile;
    private String reply_time;
    private String text;
    private String  _sign;

    public String getBase_extend() {
        return base_extend;
    }

    public void setBase_extend(String base_extend) {
        this.base_extend = base_extend;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String get_sign() {
        return _sign;
    }

    public void set_sign(String _sign) {
        this._sign = _sign;
    }
}
