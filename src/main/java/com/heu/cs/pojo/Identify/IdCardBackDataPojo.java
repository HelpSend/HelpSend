package com.heu.cs.pojo.Identify;

import java.util.List;

/**
 * Created by memgq on 2017/6/17.
 */
public class IdCardBackDataPojo {

    private String authority;//	string	发证机关
    private String  valid_date;//	string	证件有效期
    private List<Integer> authority_confidence_all;//	array(int)	发证机关置信度
    private List<Integer> valid_date_confidence_all;//	array(int)	证件有效期置信度


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public List<Integer> getAuthority_confidence_all() {
        return authority_confidence_all;
    }

    public void setAuthority_confidence_all(List<Integer> authority_confidence_all) {
        this.authority_confidence_all = authority_confidence_all;
    }

    public List<Integer> getValid_date_confidence_all() {
        return valid_date_confidence_all;
    }

    public void setValid_date_confidence_all(List<Integer> valid_date_confidence_all) {
        this.valid_date_confidence_all = valid_date_confidence_all;
    }
}
