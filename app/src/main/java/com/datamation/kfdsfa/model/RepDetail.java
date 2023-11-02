package com.datamation.kfdsfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/*
    create by kaveesha - 16-12-2020
 */

public class RepDetail {

    @SerializedName("RepCode")
    private String FREP_CODE;
    @SerializedName("RepName")
    private String FREP_NAME;

    public String getFREP_CODE() {
        return FREP_CODE;
    }

    public void setFREP_CODE(String FREP_CODE) {
        this.FREP_CODE = FREP_CODE;
    }

    public String getFREP_NAME() {
        return FREP_NAME;
    }

    public void setFREP_NAME(String FREP_NAME) {
        this.FREP_NAME = FREP_NAME;
    }

    public static RepDetail parseRepDetail(JSONObject instance) throws JSONException{

        if(instance != null)
        {
            RepDetail repDetail = new RepDetail();

            repDetail.setFREP_CODE(instance.getString("repCode").trim());
            repDetail.setFREP_NAME(instance.getString("repName").trim());

            return repDetail;
        }
        return null;
    }
}
