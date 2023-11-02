package com.datamation.kfdsfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanySetting {

    @SerializedName("cCharVal")
    private String FCOMPANYSETTING_CHAR_VAL;
    @SerializedName("cCompanyCode")
    private String FCOMPANYSETTING_COMPANY_CODE;
    @SerializedName("cLocationChar")
    private String FCOMPANYSETTING_LOCATION_CHAR;
    @SerializedName("cRemarks")
    private String FCOMPANYSETTING_REMARKS;
    @SerializedName("cSettingGrp")
    private String FCOMPANYSETTING_GRP;
    @SerializedName("cSettingsCode")
    private String FCOMPANYSETTING_SETTINGS_CODE;
    @SerializedName("nNumVal")
    private String FCOMPANYSETTING_NUM_VAL;
    @SerializedName("nType")
    private String FCOMPANYSETTING_TYPE;


    public String getFCOMPANYSETTING_SETTINGS_CODE() {
        return FCOMPANYSETTING_SETTINGS_CODE;
    }

    public void setFCOMPANYSETTING_SETTINGS_CODE(
            String fCOMPANYSETTING_SETTINGS_CODE) {
        FCOMPANYSETTING_SETTINGS_CODE = fCOMPANYSETTING_SETTINGS_CODE;
    }

    public String getFCOMPANYSETTING_GRP() {
        return FCOMPANYSETTING_GRP;
    }

    public void setFCOMPANYSETTING_GRP(String fCOMPANYSETTING_GRP) {
        FCOMPANYSETTING_GRP = fCOMPANYSETTING_GRP;
    }

    public String getFCOMPANYSETTING_LOCATION_CHAR() {
        return FCOMPANYSETTING_LOCATION_CHAR;
    }

    public void setFCOMPANYSETTING_LOCATION_CHAR(
            String fCOMPANYSETTING_LOCATION_CHAR) {
        FCOMPANYSETTING_LOCATION_CHAR = fCOMPANYSETTING_LOCATION_CHAR;
    }

    public String getFCOMPANYSETTING_CHAR_VAL() {
        return FCOMPANYSETTING_CHAR_VAL;
    }

    public void setFCOMPANYSETTING_CHAR_VAL(String fCOMPANYSETTING_CHAR_VAL) {
        FCOMPANYSETTING_CHAR_VAL = fCOMPANYSETTING_CHAR_VAL;
    }

    public String getFCOMPANYSETTING_NUM_VAL() {
        return FCOMPANYSETTING_NUM_VAL;
    }

    public void setFCOMPANYSETTING_NUM_VAL(String fCOMPANYSETTING_NUM_VAL) {
        FCOMPANYSETTING_NUM_VAL = fCOMPANYSETTING_NUM_VAL;
    }

    public String getFCOMPANYSETTING_REMARKS() {
        return FCOMPANYSETTING_REMARKS;
    }

    public void setFCOMPANYSETTING_REMARKS(String fCOMPANYSETTING_REMARKS) {
        FCOMPANYSETTING_REMARKS = fCOMPANYSETTING_REMARKS;
    }

    public String getFCOMPANYSETTING_TYPE() {
        return FCOMPANYSETTING_TYPE;
    }

    public void setFCOMPANYSETTING_TYPE(String fCOMPANYSETTING_TYPE) {
        FCOMPANYSETTING_TYPE = fCOMPANYSETTING_TYPE;
    }

    public String getFCOMPANYSETTING_COMPANY_CODE() {
        return FCOMPANYSETTING_COMPANY_CODE;
    }

    public void setFCOMPANYSETTING_COMPANY_CODE(String fCOMPANYSETTING_COMPANY_CODE) {
        FCOMPANYSETTING_COMPANY_CODE = fCOMPANYSETTING_COMPANY_CODE;
    }

    public static CompanySetting parseSettings(JSONObject instance) throws JSONException {

        if (instance != null) {
            CompanySetting setting = new CompanySetting();
            setting.setFCOMPANYSETTING_CHAR_VAL(instance.getString("cCharVal"));
            setting.setFCOMPANYSETTING_COMPANY_CODE(instance.getString("cCompanyCode"));
            setting.setFCOMPANYSETTING_LOCATION_CHAR(instance.getString("cLocationChar"));
            setting.setFCOMPANYSETTING_REMARKS(instance.getString("cRemarks"));
            setting.setFCOMPANYSETTING_GRP(instance.getString("cSettingGrp"));
            setting.setFCOMPANYSETTING_SETTINGS_CODE(instance.getString("cSettingsCode"));
            setting.setFCOMPANYSETTING_NUM_VAL(instance.getString("nNumVal"));
            setting.setFCOMPANYSETTING_TYPE(instance.getString("nType"));
            return setting;
        }

        return null;
    }
}
