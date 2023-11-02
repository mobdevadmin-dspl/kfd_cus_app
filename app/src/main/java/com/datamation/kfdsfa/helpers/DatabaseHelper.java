package com.datamation.kfdsfa.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.file.FileVisitOption;


public class DatabaseHelper extends SQLiteOpenHelper {
    // database information
    public static final String DATABASE_NAME = "kfdsfa_database.db";

    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String CREATE_FDEBTOR_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_DEBTOR +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.DEBCODE + " TEXT, " +
            ValueHolder.FDEBTOR_NAME + " TEXT, " + ValueHolder.FDEBTOR_ADD1 + " TEXT, " + ValueHolder.FDEBTOR_ADD2 + " TEXT, "  +
            ValueHolder.FDEBTOR_ADD3 + " TEXT, " + ValueHolder.FDEBTOR_TELE + " TEXT, " + ValueHolder.FDEBTOR_MOB + " TEXT, " +
            ValueHolder.FDEBTOR_EMAIL + " TEXT, " + ValueHolder.FDEBTOR_TOWN_CODE + " TEXT, " + ValueHolder.AREACODE + " TEXT, " +
            ValueHolder.FDEBTOR_DBGR_CODE + " TEXT, " + ValueHolder.STATUS + " TEXT, "  + ValueHolder.FDEBTOR_ID + " TEXT, " +
            ValueHolder.FDEBTOR_PASSWORD + " TEXT, " + ValueHolder.FDEBTOR_CRD_PERIOD + " TEXT, " + ValueHolder.FDEBTOR_CHK_CRD_PRD + " TEXT, " +
            ValueHolder.FDEBTOR_CRD_LIMIT + " TEXT, " + ValueHolder.FDEBTOR_CHK_CRD_LIMIT + " TEXT, " +
            ValueHolder.REPCODE + " TEXT, " + ValueHolder.FDEBTOR_RANK_CODE + " TEXT, "  + ValueHolder.TAX_REG + " TEXT, "+ValueHolder.FDEBTOR_FIRSTLOGIN + " TEXT, "+
            ValueHolder.ROUTECODE + " TEXT," + ValueHolder.PRILCODE + " TEXT," + ValueHolder.LOCCODE + " TEXT," + ValueHolder.IS_SYNC + " TEXT ); ";

    public static final String CREATE_CUSP_RECHED_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_CUSP_RECHED +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ADDDATE + " TEXT, " +
            ValueHolder.ADDMACH + " TEXT, " + ValueHolder.ADDUSER + " TEXT, " +
            ValueHolder.RECHED_CHQDATE + " TEXT, " + ValueHolder.RECHED_CHQNO + " TEXT, " + ValueHolder.DEBCODE + " TEXT, "+
            ValueHolder.RECHED_PAYTYPE + " TEXT, " + ValueHolder.REFNO + " TEXT, "+  ValueHolder.REPCODE + " TEXT, " +
            ValueHolder.TOTALAMT + " TEXT," + ValueHolder.TXNDATE + " TEXT); ";

    public static final String CREATE_CUSP_RECDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_CUSP_RECDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.RECDET_ALOAMT + " TEXT, " +
            ValueHolder.AMT + " TEXT, " + ValueHolder.RECDET_INVNO + " TEXT, " + ValueHolder.RECDET_INVTXNDATE + " TEXT, " +
            ValueHolder.REFNO + " TEXT, " + ValueHolder.REPCODE + " TEXT, " +ValueHolder.TXNDATE + " TEXT); ";

    public static final String CREATE_PMSG_HEDDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_PMSG_HEDDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.PMSG_HEDDET_MBODY + " TEXT, " +
            ValueHolder.PMSG_HEDDET_MGRP + " TEXT, " + ValueHolder.PMSG_HEDDET_MTYPE + " TEXT, " +
            ValueHolder.REFNO+ " TEXT, " + ValueHolder.PMSG_HEDDET_SUBJECT + " TEXT, " + ValueHolder.PMSG_HEDDET_SUPCODE + " TEXT); ";

    public static final String CREATE_FITEM_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ITEM + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +ValueHolder.ADDMACH + " TEXT, " + ValueHolder.ADDUSER + " TEXT, " +
            ValueHolder.AVG_PRICE + " TEXT, " + ValueHolder.BRAND_CODE + " TEXT, " + ValueHolder.GROUP_CODE + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.ITEMNAME + " TEXT, " + ValueHolder.ITEM_STATUS + " TEXT, " +
            ValueHolder.MUST_SALE + " TEXT, " + ValueHolder.NOU_CASE + " TEXT, " + ValueHolder.ORD_SEQ + " TEXT, " +
            ValueHolder.PRILCODE + " TEXT, " + ValueHolder.RE_ORDER_LVL + " TEXT, " + ValueHolder.RE_ORDER_QTY + " TEXT, " +
            ValueHolder.TAXCOMCODE + " TEXT, " + ValueHolder.TYPE_CODE + " TEXT, " + ValueHolder.UNIT_CODE + " TEXT, " +
            ValueHolder.CAT_CODE + " TEXT, " + ValueHolder.PACK + " TEXT, "+ValueHolder.PACK_SIZE + " TEXT, " +
            ValueHolder.SUP_CODE + " TEXT, " + ValueHolder.VEN_P_CODE + " TEXT, " + ValueHolder.MUST_FREE + " TEXT); ";

    public static final String CREATE_FGROUP_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_GROUP + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.GROUP_CODE + " TEXT, " + ValueHolder.FGROUP_NAME +  " TEXT); ";

    public static final String CREATE_FORDDISC_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDDISC +
            " (" + ValueHolder.REFNO + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.REFNO1 + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.DISAMT + " TEXT, " + ValueHolder.DISPER + " TEXT ); ";

    public static final String CREATE_FITEMLOC_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ITEMLOC +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ITEMCODE + " TEXT, " +
            ValueHolder.LOCCODE + " TEXT, " + ValueHolder.QOH + " TEXT); ";

    public static final String TESTITEMLOC = "CREATE UNIQUE INDEX IF NOT EXISTS idxitemloc_something ON " + ValueHolder.TABLE_ITEMLOC +
            " (" + ValueHolder.ITEMCODE + "," + ValueHolder.LOCCODE + ")";

    public static final String CREATE_FITEMPRI_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ITEMPRI +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ITEMCODE + " TEXT, " +
            ValueHolder.PRICE + " TEXT, " + ValueHolder.PRILCODE + " TEXT, " + ValueHolder.COSTCODE + " TEXT, " +
            ValueHolder.SKUCODE + " TEXT); ";

    public static final String CREATE_FORDFREEISS_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDFREEISS +
            " (" + ValueHolder.REFNO + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.REFNO1 + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.QTY + " TEXT ); ";

    public static final String CREATE_FTAXDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_TAXDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TAXCOMCODE + " TEXT, " +
            ValueHolder.TAXCODE + " TEXT, " + ValueHolder.RATE + " TEXT, " + ValueHolder.TAXVAL + " TEXT, " +
            ValueHolder.TAXTYPE + " TEXT, " + ValueHolder.TAXSEQ + " TEXT ); ";

    public static final String CREATE_FDEBTAX_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_DEBTAX +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.DEBCODE + " TEXT, " +
            ValueHolder.TAXCODE + " TEXT, " + ValueHolder.TAXREGNO + " TEXT); ";

    public static final String CREATE_FORDDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.AMT + " TEXT, " +
            ValueHolder.BAL_QTY + " TEXT, " + ValueHolder.B_AMT + " TEXT, " + ValueHolder.B_DIS_AMT + " TEXT, " +
            ValueHolder.BP_DIS_AMT + " TEXT, " + ValueHolder.B_SELL_PRICE + " TEXT, " + ValueHolder.BT_TAX_AMT + " TEXT, " +
            ValueHolder.BT_SELL_PRICE + " TEXT, " + ValueHolder.CASE + " TEXT, " + ValueHolder.CASE_QTY + " TEXT, " +
            ValueHolder.DISAMT + " TEXT, " + ValueHolder.DISPER + " TEXT, " + ValueHolder.FREE_QTY + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.P_DIS_AMT + " TEXT, " + ValueHolder.PRILCODE + " TEXT, " +
            ValueHolder.QTY + " TEXT, " + ValueHolder.DIS_VAL_AMT + " TEXT, " + ValueHolder.PICE_QTY + " TEXT, " +
            ValueHolder.REACODE + " TEXT, " + ValueHolder.TYPE + " TEXT, " + ValueHolder.REFNO + " TEXT, " +
            ValueHolder.SELL_PRICE + " TEXT, " + ValueHolder.SEQNO + " TEXT, " + ValueHolder.TAXAMT + " TEXT, " +
            ValueHolder.TAXCOMCODE + " TEXT, " + ValueHolder.T_SELL_PRICE + " TEXT, " + ValueHolder.ITEMNAME + " TEXT, " +
            ValueHolder.PACKSIZE + " TEXT, " + ValueHolder.COSTPRICE + " TEXT, " + ValueHolder.DISCTYPE + " TEXT, " +
            ValueHolder.SCH_DISPER + " TEXT, "+ ValueHolder.DISFLAG + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " +
            ValueHolder.IS_ACTIVE + " TEXT, "+ ValueHolder.ORDERID + " TEXT,"+ ValueHolder.TXNTYPE + " TEXT); ";

    public static final String CREATE_FPRODUCT_PRE_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_PRODUCT_PRE + " ("
            + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.ITEMNAME + " TEXT, "
            + ValueHolder.PRICE + " TEXT, " + ValueHolder.QOH + " TEXT, " + ValueHolder.QTY + " TEXT); ";

    public static final String INDEX_PRODUCTS = "CREATE UNIQUE INDEX IF NOT EXISTS ui_fProducts_pre ON fProducts_pre (itemcode,itemname);";

    public static final String CREATE_FPRETAXRG_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_PRETAXRG + " ("
            + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.TAXCODE + " TEXT, " +
            ValueHolder.TAXREGNO + " TEXT ); ";

    public static final String CREATE_FTAX_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_TAX + " (" +
            ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TAXCODE + " TEXT, " + ValueHolder.TAXNAME + " TEXT, " +
            ValueHolder.TAXPER + " TEXT, " + ValueHolder.TAXREGNO + " TEXT ); ";

    public static final String CREATE_FPRETAXDT_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_PRETAXDT + " (" +
            ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.ITEMCODE + " TEXT, " +
            ValueHolder.TAXCOMCODE + " TEXT, " + ValueHolder.TAXCODE + " TEXT, " + ValueHolder.TAXPER + " TEXT, " +
            ValueHolder.RATE + " TEXT, " + ValueHolder.TAXSEQ + " TEXT, " + ValueHolder.DETAMT + " TEXT, " +
            ValueHolder.TAXTYPE + " TEXT, " + ValueHolder.BDETAMT + " TEXT ); ";

    private static final String CREATE_FORDHED_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDHED + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ADDMACH + " TEXT, " + ValueHolder.ADDDATE + " TEXT," +//1,2
            ValueHolder.ADDUSER + " TEXT, " + ValueHolder.APP_DATE + " TEXT, " + ValueHolder.ADDRESS + " TEXT, " + //3,4,5
            ValueHolder.APPSTS + " TEXT, " + ValueHolder.APP_USER + " TEXT, " + ValueHolder.BP_TOTAL_DIS + " TEXT, " +//6,7,8
            ValueHolder.B_TOTAL_AMT + " TEXT, " + ValueHolder.B_TOTAL_DIS + " TEXT, " + ValueHolder.B_TOTAL_TAX + " TEXT, " + //9,10,11
            ValueHolder.COSTCODE + " TEXT, " + ValueHolder.CUR_CODE + " TEXT, " + ValueHolder.CUR_RATE + " TEXT, " + //12,13,14
            ValueHolder.DEBCODE + " TEXT, " + ValueHolder.LOCCODE + " TEXT, " + ValueHolder.MANU_REF + " TEXT, " +//15,16,17
            ValueHolder.DISPER + " TEXT, " + ValueHolder.APPVERSION + " TEXT, " + ValueHolder.REFNO + " TEXT, " + //18,19,20
            ValueHolder.REMARKS + " TEXT, " + ValueHolder.REPCODE + " TEXT, " +//21,22
            ValueHolder.TAX_REG + " TEXT, "  + ValueHolder.TOTAL_TAX + " TEXT, " + ValueHolder.TOTALAMT + " TEXT, " +//23,24
            ValueHolder.TOTALDIS + " TEXT, " + ValueHolder.TOTAL_ITM_DIS + " TEXT, " + ValueHolder.TOT_MKR_AMT + " TEXT, " +//25,26,27
            ValueHolder.TXNTYPE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.LONGITUDE + " TEXT, " +//28,29,30
            ValueHolder.LATITUDE + " TEXT, " + ValueHolder.START_TIME_SO + " TEXT, " + ValueHolder.IS_SYNC + " TEXT, " +//31,32,33
            ValueHolder.IS_ACTIVE + " TEXT, " + ValueHolder.DELV_DATE + " TEXT, " + ValueHolder.ROUTECODE + " TEXT, " +//34,35,36
            ValueHolder.DIS_VAL + " TEXT, " + ValueHolder.DIS_PER_VAL + " TEXT," + ValueHolder.PAYMENT_TYPE + " TEXT," +//37,38,39
            ValueHolder.END_TIME_SO + " TEXT," + ValueHolder.ORDERID + " TEXT,"+ ValueHolder.STATUS + " TEXT,"+ //40,41,42
            ValueHolder.UPLOAD_TIME + " TEXT); ";//43


    public static final String CREATE_FCONTROL_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_CONTROL + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.COM_NAME + " TEXT, " + ValueHolder.COM_ADD1 + " TEXT, " +
            ValueHolder.COM_ADD2 + " TEXT, " + ValueHolder.COM_ADD3 + " TEXT, " + ValueHolder.COM_TEL1 + " TEXT, " +
            ValueHolder.COM_TEL2 + " TEXT, " + ValueHolder.COM_FAX + " TEXT, " + ValueHolder.COM_EMAIL + " TEXT, " +
            ValueHolder.COM_WEB + " TEXT, " + ValueHolder.COM_REGNO + " TEXT, " + ValueHolder.SYSTYPE + " TEXT, " +
            ValueHolder.DEALCODE + " TEXT, " + ValueHolder.BASECUR + " TEXT, " + ValueHolder.CONAGE1 + " TEXT, " +
            ValueHolder.CONAGE2 + " TEXT, " +ValueHolder.CONAGE3 + " TEXT, " + ValueHolder.CONAGE4 + " TEXT, " +
            ValueHolder.CONAGE5 + " TEXT, " + ValueHolder.CONAGE6 + " TEXT, " + ValueHolder.CONAGE7 + " TEXT, " +
            ValueHolder.CONAGE8 + " TEXT, " + ValueHolder.CONAGE9 + " TEXT, " + ValueHolder.CONAGE10 + " TEXT, " +
            ValueHolder.CONAGE11 + " TEXT, " + ValueHolder.CONAGE12 + " TEXT, " + ValueHolder.CONAGE13 + " TEXT, " +
            ValueHolder.CONAGE14 + " TEXT, " + ValueHolder.APPVERSION + " TEXT, " + ValueHolder.SEQNO + " TEXT); ";

    public static final String CREATE_FCOMPANYSETTING_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_COMPANYSETTING + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.SETTINGS_CODE + " TEXT, " + ValueHolder.GRP + " TEXT, " +
            ValueHolder.LOCATION_CHAR + " TEXT, " + ValueHolder.CHAR_VAL + " TEXT, " + ValueHolder.NUM_VAL + " TEXT, " +
            ValueHolder.REMARKS + " TEXT, " + ValueHolder.TYPE + " TEXT, " + ValueHolder.COMPANY_CODE + " TEXT); ";

    public static final String CREATE_FROUTEDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ROUTEDET + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.DEBCODE + " TEXT, " +ValueHolder.ROUTE_CODE + " TEXT); ";

    public static final String CREATE_FFREEITEM_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEITEM + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.ITEMCODE + " TEXT); ";

    public static final String CREATE_FFREEHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEHED + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " +
            ValueHolder.DISC_DESC + " TEXT, " + ValueHolder.PRIORITY + " TEXT, " + ValueHolder.VDATEF + " TEXT, " +
            ValueHolder.VDATET + " TEXT, " + ValueHolder.REMARKS + " TEXT, " + ValueHolder.QTY + " TEXT, " + ValueHolder.FREE_IT_QTY + " TEXT, " +
            ValueHolder.TYPE + " TEXT, " +ValueHolder.MUSTFLG + " TEXT, " +ValueHolder.REC_CNT + " TEXT); ";


    public static final String CREATE_FFREEMSLAB_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEMSLAB + " (" + ValueHolder.ID + " " +
            "INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.QTY_F + " TEXT, " +
            ValueHolder.QTY_T + " TEXT, " + ValueHolder.QTY + " TEXT, " + ValueHolder.FREE_IT_QTY + " TEXT, " +
            ValueHolder.SEQNO + " TEXT); ";

    public static final String CREATE_FFREEDEB_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEDEB + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.DEBCODE + " TEXT); ";

    public static final String CREATE_FFREEDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEDET + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " +  ValueHolder.ITEMCODE + " TEXT); ";


    public static final String CREATE_FREASON_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_REASON + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REACODE + " TEXT, " + ValueHolder.REANAME + " TEXT, " +
            ValueHolder.REATCODE + " TEXT, " + ValueHolder.TYPE + " TEXT); ";

    public static final String CREATE_FROUTE_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ROUTE + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.ROUTECODE + " TEXT, " +
            ValueHolder.ROUTE_NAME + " TEXT, " + ValueHolder.AREACODE + " TEXT, " + ValueHolder.DEALCODE + " TEXT, " +
            ValueHolder.FREQNO + " TEXT, " + ValueHolder.KM + " TEXT, " + ValueHolder.MINPROCALL + " TEXT, " + ValueHolder.REMARKS + " TEXT, " +
            ValueHolder.STATUS + " TEXT, " + ValueHolder.RDALORATE + " TEXT); ";

    public static final String CREATE_FDDBNOTE_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_FDDBNOTE + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.REPNAME + " TEXT, " + //1,2
            ValueHolder.REMARKS + " TEXT, " + ValueHolder.ENT_REMARK + " TEXT, " + ValueHolder.PDAAMT + " TEXT, " +//3,4,5
            ValueHolder.REF_INV + " TEXT, " + ValueHolder.AMT + " TEXT, " + ValueHolder.SALE_REF_NO + " TEXT, " +//6,7,8
            ValueHolder.MANU_REF + " TEXT, " + ValueHolder.TXNTYPE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " +//9,10,11
            ValueHolder.CUR_CODE + " TEXT, " + ValueHolder.CUR_RATE + " TEXT, " + ValueHolder.DEBCODE + " TEXT, " +//12,13,14
            ValueHolder.REPCODE + " TEXT, " + ValueHolder.TAXCOMCODE + " TEXT, " +ValueHolder.TAXAMT + " TEXT, " +//15,16,17
            ValueHolder.BT_TAX_AMT + " TEXT, " + ValueHolder.B_AMT + " TEXT, " + ValueHolder.TOT_BAL + " TEXT, " +//18,19,20
            ValueHolder.TOT_BAL1 + " TEXT, " + ValueHolder.OV_PAY_AMT + " TEXT, " + ValueHolder.REFNO1 + " TEXT); ";//21,22,23

    public static final String CREATE_FBANK_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_BANK + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.BANK_CODE + " TEXT, " + ValueHolder.BANK_NAME + " TEXT, " +
            ValueHolder.BANK_ACC_NO + " TEXT, " + ValueHolder.BRANCH + " TEXT, " + ValueHolder.ADD1 + " TEXT, " +
            ValueHolder.ADD2 + " TEXT); ";

    public static final String CREATE_FAREA_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_AREA + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.AREA_CODE + " TEXT, " + ValueHolder.AREA_NAME + " TEXT, " +
            ValueHolder.DEALCODE + " TEXT, " + ValueHolder.REG_CODE + " TEXT); ";

    public static final String CREATE_FLOCATIONS_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_LOCATIONS + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.LOCCODE + " TEXT, " + ValueHolder.LOC_NAME + " TEXT, " +
            ValueHolder.LOC_T_CODE + " TEXT, " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.COSTCODE + " TEXT); ";

    public static final String CREATE_FTOWN_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_TOWN + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TOWN_CODE + " TEXT, " + ValueHolder.TOWN_NAME + " TEXT, " +
            ValueHolder.DISTR_CODE + " TEXT); ";

    public static final String CREATE_FTYPE_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_TYPE + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TYPE_CODE + " TEXT, " + ValueHolder.TYPE_NAME + " TEXT); ";

    public static final String CREATE_FBRAND_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_BRAND + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.BRAND_CODE + " TEXT, " + ValueHolder.BRAND_NAME + " TEXT); ";

    public static final String CREATE_FPAYMENT_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_PAYMENTS + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.PAYMENT_INVOICE_DATE + " TEXT, " + ValueHolder.PAYMENT_INVOICE_NO + " TEXT, " +
            ValueHolder.PAYMENT_AMT + " TEXT, " + ValueHolder.PAYMENT_TYPE  + " TEXT, " + ValueHolder.PAYMENT_RECEIPT_DATE + " TEXT, " +
            ValueHolder.PAYMENT_RECEIPT_NO + " TEXT); ";

    public static final String IDXFREEHED = "CREATE UNIQUE INDEX IF NOT EXISTS idxfdebtor_something ON " + ValueHolder.TABLE_DEBTOR + " (" + ValueHolder.DEBCODE + ")";

    public static final String CREATE_FREPDETAILS_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_REPDETAILS + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.REPNAME + " TEXT); ";

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(CREATE_FDEBTOR_TABLE);
        arg0.execSQL(CREATE_CUSP_RECHED_TABLE);
        arg0.execSQL(CREATE_CUSP_RECDET_TABLE);
        arg0.execSQL(CREATE_PMSG_HEDDET_TABLE);
        arg0.execSQL(CREATE_FITEM_TABLE);
        arg0.execSQL(CREATE_FGROUP_TABLE);
        arg0.execSQL(CREATE_FORDDISC_TABLE);
        arg0.execSQL(CREATE_FITEMLOC_TABLE);
        arg0.execSQL(CREATE_FITEMPRI_TABLE);
        arg0.execSQL(CREATE_FORDFREEISS_TABLE);
        arg0.execSQL(CREATE_FTAXDET_TABLE);
        arg0.execSQL(CREATE_FDEBTAX_TABLE);
        arg0.execSQL(CREATE_FORDDET_TABLE);
        arg0.execSQL(CREATE_FPRODUCT_PRE_TABLE);
        arg0.execSQL(CREATE_FPRETAXRG_TABLE);
        arg0.execSQL(CREATE_FTAX_TABLE);
        arg0.execSQL(CREATE_FPRETAXDT_TABLE);
        arg0.execSQL(CREATE_FORDHED_TABLE);
        arg0.execSQL(CREATE_FCONTROL_TABLE);
        arg0.execSQL(CREATE_FCOMPANYSETTING_TABLE);
        arg0.execSQL(CREATE_FROUTEDET_TABLE);
        arg0.execSQL(CREATE_FFREEITEM_TABLE);
        arg0.execSQL(CREATE_FFREEHED_TABLE);
        arg0.execSQL(CREATE_FFREEMSLAB_TABLE);
        arg0.execSQL(CREATE_FREASON_TABLE);
        arg0.execSQL(CREATE_FROUTE_TABLE);
        arg0.execSQL(CREATE_FDDBNOTE_TABLE);
        arg0.execSQL(CREATE_FBANK_TABLE);
        arg0.execSQL(CREATE_FAREA_TABLE);
        arg0.execSQL(CREATE_FLOCATIONS_TABLE);
        arg0.execSQL(CREATE_FTOWN_TABLE);
        arg0.execSQL(CREATE_FFREEDEB_TABLE);
        arg0.execSQL(CREATE_FFREEDET_TABLE);
        arg0.execSQL(CREATE_FTYPE_TABLE);
        arg0.execSQL(CREATE_FBRAND_TABLE);
        arg0.execSQL(CREATE_FPAYMENT_TABLE);
        arg0.execSQL(IDXFREEHED);
        arg0.execSQL(CREATE_FREPDETAILS_TABLE);
    }
    // --------------------------------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        this.onCreate(arg0);

        try {

            arg0.execSQL(CREATE_FDEBTOR_TABLE);
            arg0.execSQL(CREATE_CUSP_RECHED_TABLE);
            arg0.execSQL(CREATE_CUSP_RECDET_TABLE);
            arg0.execSQL(CREATE_PMSG_HEDDET_TABLE);
            arg0.execSQL(CREATE_FITEM_TABLE);
            arg0.execSQL(CREATE_FGROUP_TABLE);
            arg0.execSQL(CREATE_FORDDISC_TABLE);
            arg0.execSQL(CREATE_FITEMLOC_TABLE);
            arg0.execSQL(CREATE_FITEMPRI_TABLE);
            arg0.execSQL(CREATE_FORDFREEISS_TABLE);
            arg0.execSQL(CREATE_FTAXDET_TABLE);
            arg0.execSQL(CREATE_FDEBTAX_TABLE);
            arg0.execSQL(CREATE_FORDDET_TABLE);
            arg0.execSQL(CREATE_FPRODUCT_PRE_TABLE);
            arg0.execSQL(CREATE_FPRETAXRG_TABLE);
            arg0.execSQL(CREATE_FTAX_TABLE);
            arg0.execSQL(CREATE_FPRETAXDT_TABLE);
            arg0.execSQL(CREATE_FORDHED_TABLE);
            arg0.execSQL(CREATE_FCONTROL_TABLE);
            arg0.execSQL(CREATE_FCOMPANYSETTING_TABLE);
            arg0.execSQL(CREATE_FROUTEDET_TABLE);
            arg0.execSQL(CREATE_FFREEITEM_TABLE);
            arg0.execSQL(CREATE_FFREEHED_TABLE);
            arg0.execSQL(CREATE_FFREEMSLAB_TABLE);
            arg0.execSQL(CREATE_FREASON_TABLE);
            arg0.execSQL(CREATE_FROUTE_TABLE);
            arg0.execSQL(CREATE_FDDBNOTE_TABLE);
            arg0.execSQL(CREATE_FBANK_TABLE);
            arg0.execSQL(CREATE_FAREA_TABLE);
            arg0.execSQL(CREATE_FLOCATIONS_TABLE);
            arg0.execSQL(CREATE_FTOWN_TABLE);
            arg0.execSQL(CREATE_FFREEDEB_TABLE);
            arg0.execSQL(CREATE_FFREEDET_TABLE);
            arg0.execSQL(CREATE_FTYPE_TABLE);
            arg0.execSQL(CREATE_FBRAND_TABLE);
            arg0.execSQL(CREATE_FPAYMENT_TABLE);
            arg0.execSQL(IDXFREEHED);
            arg0.execSQL(CREATE_FREPDETAILS_TABLE);
        } catch (SQLiteException e) {
        }

    }
}