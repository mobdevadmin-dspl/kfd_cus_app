package com.datamation.kfdsfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Item {

	@SerializedName("AvgPrice")//1
	private String FITEM_AVGPRICE;
	@SerializedName("BrandCode")//2
	private String FITEM_BRANDCODE;
	@SerializedName("GroupCode")//3
	private String FITEM_GROUPCODE;
	@SerializedName("ItemCode")//4
	private String FITEM_ITEM_CODE;
	@SerializedName("ItemName")//5
	private String FITEM_ITEM_NAME;
	@SerializedName("ItemStatus")//6
	private String FITEM_ITEMSTATUS;
	@SerializedName("PrilCode")//7
	private String FITEM_PRILCODE;
	@SerializedName("TypeCode")//8
	private String FITEM_TYPECODE;
	@SerializedName("UnitCode")//9
	private String FITEM_UNITCODE;
	@SerializedName("VenPcode")//10
	private String FITEM_VENPCODE;
	@SerializedName("NOUCase")//11
	private String FITEM_NOUCASE;
	@SerializedName("ReOrderLvl")//12
	private String FITEM_REORDER_LVL;
	@SerializedName("ReOrderQty")//13
	private String FITEM_REORDER_QTY;
	@SerializedName("TaxComCode")//14
	private String FITEM_TAXCOMCODE;
	@SerializedName("AddUser")
	private String FITEM_ADDUSER;
	@SerializedName("AddMach")
	private String FITEM_ID;
	private String FITEM_SCATCODE;
	private String FITEM_SUBCATCODE;
	private String FITEM_COLORCODE;
	private String FITEM_DISCOUNT;
	private String FITEM_CLASSCODE;
	private String FITEM_ISSIZE;
	private String FITEM_ISDISCOUNT;
	private String FITEM_QOH;
	private String FITEM_CASEQTY;
	private String FITEM_PICEQTY;
	private String FITEM_PACK;

	public String getFITEM_ADDUSER() {
		return FITEM_ADDUSER;
	}

	public void setFITEM_ADDUSER(String FITEM_ADDUSER) {
		this.FITEM_ADDUSER = FITEM_ADDUSER;
	}

	public String getFITEM_PACK() {
		return FITEM_PACK;
	}

	public void setFITEM_PACK(String FITEM_PACK) {
		this.FITEM_PACK = FITEM_PACK;
	}

	public String getFITEM_PICEQTY() {
		return FITEM_PICEQTY;
	}

	public void setFITEM_PICEQTY(String FITEM_PICEQTY) {
		this.FITEM_PICEQTY = FITEM_PICEQTY;
	}

	public String getFITEM_CASEQTY() {
		return FITEM_CASEQTY;
	}

	public void setFITEM_CASEQTY(String FITEM_CASEQTY) {
		this.FITEM_CASEQTY = FITEM_CASEQTY;
	}

	public String getFITEM_VENPCODE() {
		return FITEM_VENPCODE;
	}

	public void setFITEM_VENPCODE(String fITEM_VENPCODE) {
		FITEM_VENPCODE = fITEM_VENPCODE;
	}

	public String getFITEM_GROUPCODE() {
		return FITEM_GROUPCODE;
	}

	public void setFITEM_GROUPCODE(String fITEM_GROUPCODE) {
		FITEM_GROUPCODE = fITEM_GROUPCODE;
	}

	public String getFITEM_TYPECODE() {
		return FITEM_TYPECODE;
	}

	public void setFITEM_TYPECODE(String fITEM_TYPECODE) {
		FITEM_TYPECODE = fITEM_TYPECODE;
	}

	public String getFITEM_TAXCOMCODE() {
		return FITEM_TAXCOMCODE;
	}

	public void setFITEM_TAXCOMCODE(String fITEM_TAXCOMCODE) {
		FITEM_TAXCOMCODE = fITEM_TAXCOMCODE;
	}

	public String getFITEM_UNITCODE() {
		return FITEM_UNITCODE;
	}

	public void setFITEM_UNITCODE(String fITEM_UNITCODE) {
		FITEM_UNITCODE = fITEM_UNITCODE;
	}

	public String getFITEM_ITEMSTATUS() {
		return FITEM_ITEMSTATUS;
	}

	public void setFITEM_ITEMSTATUS(String fITEM_ITEMSTATUS) {
		FITEM_ITEMSTATUS = fITEM_ITEMSTATUS;
	}

	public String getFITEM_AVGPRICE() {
		return FITEM_AVGPRICE;
	}

	public void setFITEM_AVGPRICE(String fITEM_AVGPRICE) {
		FITEM_AVGPRICE = fITEM_AVGPRICE;
	}

	public String getFITEM_PRILCODE() {
		return FITEM_PRILCODE;
	}

	public void setFITEM_PRILCODE(String fITEM_PRILCODE) {
		FITEM_PRILCODE = fITEM_PRILCODE;
	}

	public String getFITEM_SCATCODE() {
		return FITEM_SCATCODE;
	}

	public void setFITEM_SCATCODE(String fITEM_SCATCODE) {
		FITEM_SCATCODE = fITEM_SCATCODE;
	}

	public String getFITEM_SUBCATCODE() {
		return FITEM_SUBCATCODE;
	}

	public void setFITEM_SUBCATCODE(String fITEM_SUBCATCODE) {
		FITEM_SUBCATCODE = fITEM_SUBCATCODE;
	}

	public String getFITEM_BRANDCODE() {
		return FITEM_BRANDCODE;
	}

	public void setFITEM_BRANDCODE(String fITEM_BRANDCODE) {
		FITEM_BRANDCODE = fITEM_BRANDCODE;
	}

	public String getFITEM_COLORCODE() {
		return FITEM_COLORCODE;
	}

	public void setFITEM_COLORCODE(String fITEM_COLORCODE) {
		FITEM_COLORCODE = fITEM_COLORCODE;
	}

	public String getFITEM_DISCOUNT() {
		return FITEM_DISCOUNT;
	}

	public void setFITEM_DISCOUNT(String fITEM_DISCOUNT) {
		FITEM_DISCOUNT = fITEM_DISCOUNT;
	}

	public String getFITEM_CLASSCODE() {
		return FITEM_CLASSCODE;
	}

	public void setFITEM_CLASSCODE(String fITEM_CLASSCODE) {
		FITEM_CLASSCODE = fITEM_CLASSCODE;
	}

	public String getFITEM_ISSIZE() {
		return FITEM_ISSIZE;
	}

	public void setFITEM_ISSIZE(String fITEM_ISSIZE) {
		FITEM_ISSIZE = fITEM_ISSIZE;
	}

	public String getFITEM_ISDISCOUNT() {
		return FITEM_ISDISCOUNT;
	}

	public void setFITEM_ISDISCOUNT(String fITEM_ISDISCOUNT) {
		FITEM_ISDISCOUNT = fITEM_ISDISCOUNT;
	}

	public String getFITEM_QOH() {
		return FITEM_QOH;
	}

	public void setFITEM_QOH(String fITEM_QOH) {
		FITEM_QOH = fITEM_QOH;
	}

	public String getFITEM_ITEM_CODE() {
		return FITEM_ITEM_CODE;
	}

	public void setFITEM_ITEM_CODE(String fITEM_ITEM_CODE) {
		FITEM_ITEM_CODE = fITEM_ITEM_CODE;
	}

	public String getFITEM_ITEM_NAME() {
		return FITEM_ITEM_NAME;
	}

	public void setFITEM_ITEM_NAME(String fITEM_ITEM_NAME) {
		FITEM_ITEM_NAME = fITEM_ITEM_NAME;
	}

	public String getFITEM_ID() {
		return FITEM_ID;
	}

	public void setFITEM_ID(String fITEM_ID) {
		FITEM_ID = fITEM_ID;
	}

	public String getFITEM_NOUCASE() {
		return FITEM_NOUCASE;
	}

	public void setFITEM_NOUCASE(String fITEM_NOUCASE) {
		FITEM_NOUCASE = fITEM_NOUCASE;
	}

	public String getFITEM_REORDER_QTY() {
		return FITEM_REORDER_QTY;
	}

	public void setFITEM_REORDER_QTY(String fITEM_REORDER_QTY) {
		FITEM_REORDER_QTY = fITEM_REORDER_QTY;
	}

	public String getFITEM_REORDER_LVL() {
		return FITEM_REORDER_LVL;
	}

	public void setFITEM_REORDER_LVL(String fITEM_REORDER_LVL) {
		FITEM_REORDER_LVL = fITEM_REORDER_LVL;
	}
	public static Item parseItem(JSONObject instance) throws JSONException {

		if (instance != null) {
			Item itm = new Item();

			itm.setFITEM_AVGPRICE(instance.getString("avgPrice").trim());
			itm.setFITEM_BRANDCODE(instance.getString("brandCode").trim());
			itm.setFITEM_GROUPCODE(instance.getString("groupCode").trim());
			itm.setFITEM_ITEM_CODE(instance.getString("itemCode").trim());
			itm.setFITEM_ITEM_NAME(instance.getString("itemName").trim());
			itm.setFITEM_ITEMSTATUS(instance.getString("itemStatus").trim());
			itm.setFITEM_PRILCODE(instance.getString("prilCode").trim());
			itm.setFITEM_VENPCODE(instance.getString("venPcode").trim());
			itm.setFITEM_NOUCASE(instance.getString("noucase").trim());
			itm.setFITEM_REORDER_LVL(instance.getString("reOrderLvl").trim());
			itm.setFITEM_REORDER_QTY(instance.getString("reOrderQty").trim());
			itm.setFITEM_UNITCODE(instance.getString("unitCode").trim());
			itm.setFITEM_TYPECODE(instance.getString("typeCode").trim());
			itm.setFITEM_TAXCOMCODE(instance.getString("taxComCode").trim());
			itm.setFITEM_PACK(instance.getString("pack").trim());
			itm.setFITEM_ADDUSER(instance.getString("genericName").trim());
			//itm.setFITEM_ADDUSER(instance.getString("addUser").trim());

			return itm;
		}

		return null;
	}

	@Override
	public String toString() {
		return "Item{" + "FITEM_ID='" + FITEM_ID + '\'' + ", FITEM_ITEM_CODE='" + FITEM_ITEM_CODE + '\'' + ", FITEM_ITEM_NAME='" + FITEM_ITEM_NAME + '\'' + ", FITEM_VENPCODE='" + FITEM_VENPCODE + '\'' + ", FITEM_GROUPCODE='" + FITEM_GROUPCODE + '\'' + ", FITEM_TYPECODE='" + FITEM_TYPECODE + '\'' + ", FITEM_TAXCOMCODE='" + FITEM_TAXCOMCODE + '\'' + ", FITEM_UNITCODE='" + FITEM_UNITCODE + '\'' + ", FITEM_ITEMSTATUS='" + FITEM_ITEMSTATUS + '\'' + ", FITEM_AVGPRICE='" + FITEM_AVGPRICE + '\'' + ", FITEM_PRILCODE='" + FITEM_PRILCODE + '\'' + ", FITEM_SCATCODE='" + FITEM_SCATCODE + '\'' + ", FITEM_SUBCATCODE='" + FITEM_SUBCATCODE + '\'' + ", FITEM_BRANDCODE='" + FITEM_BRANDCODE + '\'' + ", FITEM_COLORCODE='" + FITEM_COLORCODE + '\'' + ", FITEM_DISCOUNT='" + FITEM_DISCOUNT + '\'' + ", FITEM_CLASSCODE='" + FITEM_CLASSCODE + '\'' + ", FITEM_ISSIZE='" + FITEM_ISSIZE + '\'' + ", FITEM_ISDISCOUNT='" + FITEM_ISDISCOUNT + '\'' + ", FITEM_NOUCASE='" + FITEM_NOUCASE + '\'' + ", FITEM_REORDER_QTY='" + FITEM_REORDER_QTY + '\'' + ", FITEM_REORDER_LVL='" + FITEM_REORDER_LVL + '\'' + ", FITEM_QOH='" + FITEM_QOH + '\'' + '}';
	}
}
