
package com.heu.cs.pojo;

/**
 * Created by memgq on 2017/6/7.
 */
public class PornDetectDataPojo {
    private int result;

    private int forbid_status;
    private double confidence;
    private double hot_score;
    private double normal_score;
    private double porn_score;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getForbid_status() {
        return forbid_status;
    }

    public void setForbid_status(int forbid_status) {
        this.forbid_status = forbid_status;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getHot_score() {
        return hot_score;
    }

    public void setHot_score(double hot_score) {
        this.hot_score = hot_score;
    }

    public double getNormal_score() {
        return normal_score;
    }

    public void setNormal_score(double normal_score) {
        this.normal_score = normal_score;
    }

    public double getPorn_score() {
        return porn_score;
    }

    public void setPorn_score(double porn_score) {
        this.porn_score = porn_score;
    }
}
