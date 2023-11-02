package com.datamation.kfdsfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Debtor {

    @SerializedName("areaCode")
    private String FDEBTOR_AREA_CODE;//1
    @SerializedName("chkCrdLmt")
    private String FDEBTOR_CHK_CRD_LIMIT;//2
    @SerializedName("chkCrdPrd")
    private String FDEBTOR_CHK_CRD_PERIOD;//3
    @SerializedName("crdLimit")
    private String FDEBTOR_CRD_LIMIT;//4
    @SerializedName("crdPeriod")
    private String FDEBTOR_CRD_PERIOD;//5
    @SerializedName("cusid")
    private String FDEBTOR_CUSID;//6
    @SerializedName("cusPassword")
    private String FDEBTOR_PASSWORD;//7
    @SerializedName("dbGrCode")
    private String FDEBTOR_DBGR_CODE;//8
    @SerializedName("debAdd1")
    private String FDEBTOR_ADD1;//9
    @SerializedName("debAdd2")
    private String FDEBTOR_ADD2;//10
    @SerializedName("debAdd3")
    private String FDEBTOR_ADD3;//11
    @SerializedName("debCode")
    private String FDEBTOR_CODE;//12
    @SerializedName("debEmail")
    private String FDEBTOR_EMAIL;//13
    @SerializedName("debMob")
    private String FDEBTOR_MOB;//14
    @SerializedName("debName")
    private String FDEBTOR_NAME;//15
    @SerializedName("debTele")
    private String FDEBTOR_TELE;//16
    @SerializedName("prilCode")
    private String FDEBTOR_PRILLCODE;//17
    @SerializedName("rankCode")
    private String FDEBTOR_RANK_CODE;//18
    @SerializedName("repCode")
    private String FDEBTOR_REPCODE;//19
    @SerializedName("routeCode")
    private String FDEBTOR_ROUTE_CODE;//20
    @SerializedName("status")
    private String FDEBTOR_STATUS;//21
    @SerializedName("taxReg")
    private String FDEBTOR_TAX_REG;//22
    @SerializedName("townCode")
    private String FDEBTOR_TOWNCODE;//23
    @SerializedName("mainLoc")
    private String FDEBTOR_MAINLOC;
    @SerializedName("multiRflg")
    private String FDEBTOR_FIRSTLOGIN;

    private String FDEBTOR_IS_SYNC;
    private String FDEBTOR_OTP;
    //for upload
    private String KFDDB;
    private String LOGIN_DEBCODE;
    private String LOGIN_PASSWORD;

    public String getFDEBTOR_MAINLOC() {
        return FDEBTOR_MAINLOC;
    }

    public void setFDEBTOR_MAINLOC(String FDEBTOR_MAINLOC) {
        this.FDEBTOR_MAINLOC = FDEBTOR_MAINLOC;
    }

    public String getFDEBTOR_FIRSTLOGIN() {
        return FDEBTOR_FIRSTLOGIN;
    }

    public void setFDEBTOR_FIRSTLOGIN(String FDEBTOR_FIRSTLOGIN) {
        this.FDEBTOR_FIRSTLOGIN = FDEBTOR_FIRSTLOGIN;
    }

    public String getKFDDB() {
        return KFDDB;
    }

    public void setKFDDB(String KFDDB) {
        this.KFDDB = KFDDB;
    }

    public String getLOGIN_DEBCODE() {
        return LOGIN_DEBCODE;
    }

    public void setLOGIN_DEBCODE(String LOGIN_DEBCODE) {
        this.LOGIN_DEBCODE = LOGIN_DEBCODE;
    }

    public String getLOGIN_PASSWORD() {
        return LOGIN_PASSWORD;
    }

    public void setLOGIN_PASSWORD(String LOGIN_PASSWORD) {
        this.LOGIN_PASSWORD = LOGIN_PASSWORD;
    }

    public String getFDEBTOR_OTP() {
        return FDEBTOR_OTP;
    }

    public void setFDEBTOR_OTP(String FDEBTOR_OTP) {
        this.FDEBTOR_OTP = FDEBTOR_OTP;
    }

    public String getFDEBTOR_IS_SYNC() {
        return FDEBTOR_IS_SYNC;
    }

    public void setFDEBTOR_IS_SYNC(String FDEBTOR_IS_SYNC) {
        this.FDEBTOR_IS_SYNC = FDEBTOR_IS_SYNC;
    }

    public String getFDEBTOR_CHK_CRD_LIMIT() {
        return FDEBTOR_CHK_CRD_LIMIT;
    }

    public void setFDEBTOR_CHK_CRD_LIMIT(String FDEBTOR_CHK_CRD_LIMIT) {
        this.FDEBTOR_CHK_CRD_LIMIT = FDEBTOR_CHK_CRD_LIMIT;
    }

    public String getFDEBTOR_CHK_CRD_PERIOD() {
        return FDEBTOR_CHK_CRD_PERIOD;
    }

    public void setFDEBTOR_CHK_CRD_PERIOD(String FDEBTOR_CHK_CRD_PERIOD) {
        this.FDEBTOR_CHK_CRD_PERIOD = FDEBTOR_CHK_CRD_PERIOD;
    }

    public String getFDEBTOR_ROUTE_CODE() {
        return FDEBTOR_ROUTE_CODE;
    }

    public void setFDEBTOR_ROUTE_CODE(String FDEBTOR_ROUTE_CODE) {
        this.FDEBTOR_ROUTE_CODE = FDEBTOR_ROUTE_CODE;
    }

    public String getFDEBTOR_PASSWORD() {
        return FDEBTOR_PASSWORD;
    }

    public void setFDEBTOR_PASSWORD(String FDEBTOR_PASSWORD) {
        this.FDEBTOR_PASSWORD = FDEBTOR_PASSWORD;
    }

    public String getFDEBTOR_TOWNCODE() {
        return FDEBTOR_TOWNCODE;
    }

    public void setFDEBTOR_TOWNCODE(String FDEBTOR_TOWNCODE) {
        this.FDEBTOR_TOWNCODE = FDEBTOR_TOWNCODE;
    }

    public String getFDEBTOR_CUSID() {
        return FDEBTOR_CUSID;
    }

    public void setFDEBTOR_CUSID(String FDEBTOR_CUSID) {
        this.FDEBTOR_CUSID = FDEBTOR_CUSID;
    }

    public String getFDEBTOR_AREA_CODE() {
        return FDEBTOR_AREA_CODE;
    }

    public void setFDEBTOR_AREA_CODE(String FDEBTOR_AREA_CODE) {
        this.FDEBTOR_AREA_CODE = FDEBTOR_AREA_CODE;
    }

    public String getFDEBTOR_CRD_LIMIT() {
        return FDEBTOR_CRD_LIMIT;
    }

    public void setFDEBTOR_CRD_LIMIT(String FDEBTOR_CRD_LIMIT) {
        this.FDEBTOR_CRD_LIMIT = FDEBTOR_CRD_LIMIT;
    }

    public String getFDEBTOR_CRD_PERIOD() {
        return FDEBTOR_CRD_PERIOD;
    }

    public void setFDEBTOR_CRD_PERIOD(String FDEBTOR_CRD_PERIOD) {
        this.FDEBTOR_CRD_PERIOD = FDEBTOR_CRD_PERIOD;
    }

    public String getFDEBTOR_DBGR_CODE() {
        return FDEBTOR_DBGR_CODE;
    }

    public void setFDEBTOR_DBGR_CODE(String FDEBTOR_DBGR_CODE) {
        this.FDEBTOR_DBGR_CODE = FDEBTOR_DBGR_CODE;
    }

    public String getFDEBTOR_ADD1() {
        return FDEBTOR_ADD1;
    }

    public void setFDEBTOR_ADD1(String FDEBTOR_ADD1) {
        this.FDEBTOR_ADD1 = FDEBTOR_ADD1;
    }

    public String getFDEBTOR_ADD2() {
        return FDEBTOR_ADD2;
    }

    public void setFDEBTOR_ADD2(String FDEBTOR_ADD2) {
        this.FDEBTOR_ADD2 = FDEBTOR_ADD2;
    }

    public String getFDEBTOR_ADD3() {
        return FDEBTOR_ADD3;
    }

    public void setFDEBTOR_ADD3(String FDEBTOR_ADD3) {
        this.FDEBTOR_ADD3 = FDEBTOR_ADD3;
    }

    public String getFDEBTOR_CODE() {
        return FDEBTOR_CODE;
    }

    public void setFDEBTOR_CODE(String FDEBTOR_CODE) {
        this.FDEBTOR_CODE = FDEBTOR_CODE;
    }

    public String getFDEBTOR_EMAIL() {
        return FDEBTOR_EMAIL;
    }

    public void setFDEBTOR_EMAIL(String FDEBTOR_EMAIL) {
        this.FDEBTOR_EMAIL = FDEBTOR_EMAIL;
    }

    public String getFDEBTOR_MOB() {
        return FDEBTOR_MOB;
    }

    public void setFDEBTOR_MOB(String FDEBTOR_MOB) {
        this.FDEBTOR_MOB = FDEBTOR_MOB;
    }

    public String getFDEBTOR_NAME() {
        return FDEBTOR_NAME;
    }

    public void setFDEBTOR_NAME(String FDEBTOR_NAME) {
        this.FDEBTOR_NAME = FDEBTOR_NAME;
    }

    public String getFDEBTOR_TELE() {
        return FDEBTOR_TELE;
    }

    public void setFDEBTOR_TELE(String FDEBTOR_TELE) {
        this.FDEBTOR_TELE = FDEBTOR_TELE;
    }

    public String getFDEBTOR_PRILLCODE() {
        return FDEBTOR_PRILLCODE;
    }

    public void setFDEBTOR_PRILLCODE(String FDEBTOR_PRILLCODE) {
        this.FDEBTOR_PRILLCODE = FDEBTOR_PRILLCODE;
    }
    public String getFDEBTOR_RANK_CODE() {
        return FDEBTOR_RANK_CODE;
    }

    public void setFDEBTOR_RANK_CODE(String FDEBTOR_RANK_CODE) {
        this.FDEBTOR_RANK_CODE = FDEBTOR_RANK_CODE;
    }
    public String getFDEBTOR_STATUS() {
        return FDEBTOR_STATUS;
    }

    public void setFDEBTOR_STATUS(String FDEBTOR_STATUS) {
        this.FDEBTOR_STATUS = FDEBTOR_STATUS;
    }

    public String getFDEBTOR_TAX_REG() {
        return FDEBTOR_TAX_REG;
    }

    public void setFDEBTOR_TAX_REG(String FDEBTOR_TAX_REG) {
        this.FDEBTOR_TAX_REG = FDEBTOR_TAX_REG;
    }
    public String getFDEBTOR_REPCODE() {
        return FDEBTOR_REPCODE;
    }

    public void setFDEBTOR_REPCODE(String FDEBTOR_REPCODE) {
        this.FDEBTOR_REPCODE = FDEBTOR_REPCODE;
    }

    public static Debtor parseDebtor(JSONObject instance) throws JSONException{

        if(instance != null)
        {
            Debtor debtor = new Debtor();

            debtor.setFDEBTOR_CODE(instance.getString("debCode").trim());
            debtor.setFDEBTOR_NAME(instance.getString("debName").trim());
            debtor.setFDEBTOR_ADD1(instance.getString("debAdd1").trim());
            debtor.setFDEBTOR_ADD2(instance.getString("debAdd2"));
            debtor.setFDEBTOR_ADD3(instance.getString("debAdd3").trim());
            debtor.setFDEBTOR_TELE(instance.getString("debTele").trim());
            debtor.setFDEBTOR_MOB(instance.getString("debMob").trim());
            debtor.setFDEBTOR_EMAIL(instance.getString("debEmail"));
            debtor.setFDEBTOR_STATUS(instance.getString("status").trim());
            debtor.setFDEBTOR_DBGR_CODE(instance.getString("dbGrCode").trim());
            debtor.setFDEBTOR_AREA_CODE(instance.getString("areaCode").trim());
            debtor.setFDEBTOR_CRD_PERIOD(instance.getString("crdPeriod").trim());
            debtor.setFDEBTOR_CHK_CRD_PERIOD(instance.getString("chkCrdPrd").trim());
            debtor.setFDEBTOR_CRD_LIMIT(instance.getString("crdLimit").trim());
            debtor.setFDEBTOR_CHK_CRD_LIMIT(instance.getString("chkCrdLmt").trim());
            debtor.setFDEBTOR_REPCODE(instance.getString("repCode").trim());
            debtor.setFDEBTOR_RANK_CODE(instance.getString("rankCode").trim());
            debtor.setFDEBTOR_TAX_REG(instance.getString("taxReg").trim());
            debtor.setFDEBTOR_PRILLCODE(instance.getString("prilCode").trim());
            debtor.setFDEBTOR_ROUTE_CODE(instance.getString("routeCode").trim());
            debtor.setFDEBTOR_TOWNCODE(instance.getString("townCode").trim());
            debtor.setFDEBTOR_FIRSTLOGIN(instance.getString("multiRflg").trim());
            debtor.setFDEBTOR_CUSID(instance.getString("cusid").trim());
            debtor.setFDEBTOR_PASSWORD(instance.getString("cusPassword").trim());
            debtor.setFDEBTOR_MAINLOC(instance.getString("mainLoc").trim());

            return debtor;
        }
        return null;
    }

}
