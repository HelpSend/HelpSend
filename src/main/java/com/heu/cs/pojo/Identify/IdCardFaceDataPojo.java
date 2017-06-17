package com.heu.cs.pojo.Identify;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memgq on 2017/6/17.
 */
public class IdCardFaceDataPojo {
    private String name;//	string	姓名
    private String sex;//	string	性别
    private String nation;	//string	民族
    private String birth;//	string	出生日期
    private String address;//	string	地址
    private String id;//	string	身份证号
    private List<Integer> name_confidence_all;//	array(int)	证件姓名置信度
    private List<Integer> sex_confidence_all;//	array(int)	性别置信度
    private List<Integer> nation_confidence_all;//	array(int)	民族置信度
    private List<Integer> birth_confidence_all	;//array(int)	出生日期置信度
    private List<Integer> address_confidence_all;//	array(int)	地址置信度
    private List<Integer> id_confidence_all;// array(int)	身份证号置信度


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getName_confidence_all() {
        return name_confidence_all;
    }

    public void setName_confidence_all(List<Integer> name_confidence_all) {
        this.name_confidence_all = name_confidence_all;
    }

    public List<Integer> getSex_confidence_all() {
        return sex_confidence_all;
    }

    public void setSex_confidence_all(List<Integer> sex_confidence_all) {
        this.sex_confidence_all = sex_confidence_all;
    }

    public List<Integer> getNation_confidence_all() {
        return nation_confidence_all;
    }

    public void setNation_confidence_all(List<Integer> nation_confidence_all) {
        this.nation_confidence_all = nation_confidence_all;
    }

    public List<Integer> getBirth_confidence_all() {
        return birth_confidence_all;
    }

    public void setBirth_confidence_all(List<Integer> birth_confidence_all) {
        this.birth_confidence_all = birth_confidence_all;
    }

    public List<Integer> getAddress_confidence_all() {
        return address_confidence_all;
    }

    public void setAddress_confidence_all(List<Integer> address_confidence_all) {
        this.address_confidence_all = address_confidence_all;
    }

    public List<Integer> getId_confidence_all() {
        return id_confidence_all;
    }

    public void setId_confidence_all(List<Integer> id_confidence_all) {
        this.id_confidence_all = id_confidence_all;
    }
}
