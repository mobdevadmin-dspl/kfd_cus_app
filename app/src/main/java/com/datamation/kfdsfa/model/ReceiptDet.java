package com.datamation.kfdsfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


public class ReceiptDet{

	@SerializedName("AloAmt")
	private String FPRECDET_ALOAMT;
	@SerializedName("Amt")
	private String FPRECDET_AMT ;
	@SerializedName("InvNo")
	private String FPRECDET_INVNO ;
	@SerializedName("InvTxnDate")
	private String FPRECDET_INVTXNDATE ;
	@SerializedName("RefNo")
	private String FPRECDET_REFNO ;
	@SerializedName("RepCode")
	private String FPRECDET_REPCODE ;
	@SerializedName("TxnDate")
	private String FPRECDET_TXNDATE ;

	private String FPRECDET_PAYTYPE;

	public String getFPRECDET_PAYTYPE() {
		return FPRECDET_PAYTYPE;
	}

	public void setFPRECDET_PAYTYPE(String FPRECDET_PAYTYPE) {
		this.FPRECDET_PAYTYPE = FPRECDET_PAYTYPE;
	}

	public String getFPRECDET_ALOAMT() {
		return FPRECDET_ALOAMT;
	}

	public void setFPRECDET_ALOAMT(String FPRECDET_ALOAMT) {
		this.FPRECDET_ALOAMT = FPRECDET_ALOAMT;
	}

	public String getFPRECDET_AMT() {
		return FPRECDET_AMT;
	}

	public void setFPRECDET_AMT(String FPRECDET_AMT) {
		this.FPRECDET_AMT = FPRECDET_AMT;
	}

	public String getFPRECDET_INVNO() {
		return FPRECDET_INVNO;
	}

	public void setFPRECDET_INVNO(String FPRECDET_INVNO) {
		this.FPRECDET_INVNO = FPRECDET_INVNO;
	}

	public String getFPRECDET_INVTXNDATE() {
		return FPRECDET_INVTXNDATE;
	}

	public void setFPRECDET_INVTXNDATE(String FPRECDET_INVTXNDATE) {
		this.FPRECDET_INVTXNDATE = FPRECDET_INVTXNDATE;
	}

	public String getFPRECDET_REFNO() {
		return FPRECDET_REFNO;
	}

	public void setFPRECDET_REFNO(String FPRECDET_REFNO) {
		this.FPRECDET_REFNO = FPRECDET_REFNO;
	}

	public String getFPRECDET_REPCODE() {
		return FPRECDET_REPCODE;
	}

	public void setFPRECDET_REPCODE(String FPRECDET_REPCODE) {
		this.FPRECDET_REPCODE = FPRECDET_REPCODE;
	}

	public String getFPRECDET_TXNDATE() {
		return FPRECDET_TXNDATE;
	}

	public void setFPRECDET_TXNDATE(String FPRECDET_TXNDATE) {
		this.FPRECDET_TXNDATE = FPRECDET_TXNDATE;
	}

	public static ReceiptDet parseRecDet(JSONObject instance) throws JSONException{

		if(instance != null){

			ReceiptDet recDet = new ReceiptDet();

			recDet.setFPRECDET_ALOAMT(instance.getString("aloAmt").trim());
			recDet.setFPRECDET_AMT(instance.getString("amt").trim());
			recDet.setFPRECDET_INVNO(instance.getString("refNo1").trim());
			recDet.setFPRECDET_INVTXNDATE(instance.getString("dtxnDateOnly").trim());
			recDet.setFPRECDET_REFNO(instance.getString("refNo").trim());
			recDet.setFPRECDET_REPCODE(instance.getString("repCode").trim());
			recDet.setFPRECDET_TXNDATE(instance.getString("txnDateOnly").trim());


			return recDet;

		}
		return  null;

	}
}
