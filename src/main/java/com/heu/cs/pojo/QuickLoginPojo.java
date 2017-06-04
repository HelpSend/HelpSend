package com.heu.cs.pojo;

/**
 * Created by memgq on 2017/6/3.
 */
public class QuickLoginPojo {
    private String telNumber;
    private String verificationCode;

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
