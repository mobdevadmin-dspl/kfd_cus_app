package com.datamation.kfdsfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.TaxDet;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TaxDetController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbeHelper;
    private String TAG = "TaxDetController";

    public TaxDetController(Context context) {
        this.context = context;
        dbeHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbeHelper.getWritableDatabase();
    }

    public int createOrUpdateTaxDet(ArrayList<TaxDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (TaxDet taxDet : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_TAXDET + " WHERE " + ValueHolder.TAXCOMCODE + " = '" + taxDet.getTAXCOMCODE() + "' AND " + ValueHolder.TAXCODE + "='" + taxDet.getTAXCODE() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(ValueHolder.TAXCOMCODE, taxDet.getTAXCOMCODE());
                values.put(ValueHolder.RATE, taxDet.getRATE());
                values.put(ValueHolder.TAXSEQ, taxDet.getSEQ());
                values.put(ValueHolder.TAXCODE, taxDet.getTAXCODE());
                values.put(ValueHolder.TAXVAL, taxDet.getTAXVAL());
                values.put(ValueHolder.TAXTYPE, taxDet.getTAXTYPE());

                int cn = cursor.getCount();
                if (cn > 0)
                    count = dB.update(ValueHolder.TABLE_TAXDET, values, ValueHolder.TAXCOMCODE + " =? AND " + ValueHolder.TAXCODE + "=?", new String[]{taxDet.getTAXCOMCODE().toString(), taxDet.getTAXCODE().toString()});
                else
                    count = (int) dB.insert(ValueHolder.TABLE_TAXDET, null, values);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/

    public ArrayList<TaxDet> getTaxInfoByComCode(String comCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<TaxDet> list = new ArrayList<TaxDet>();

        String selectQuery = "select * from " + ValueHolder.TABLE_TAXDET + " WHERE " + ValueHolder.TAXCOMCODE + "='" + comCode + "' ORDER BY Seq DESC";
        try {
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                TaxDet det = new TaxDet();

                det.setID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                det.setRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.RATE)));
                det.setSEQ(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXSEQ)));
                det.setTAXCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCODE)));
                det.setTAXCOMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                det.setTAXVAL(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXVAL)));
                det.setTAXTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXTYPE)));

                list.add(det);
            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return list;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public String calculateSellPrice(String itemCode, String price) {

        String comCode = new ItemController(context).getTaxComCodeByItemCode(itemCode);
        ArrayList<TaxDet> list = new TaxDetController(context).getTaxInfoByComCode(comCode);
        BigDecimal tax = new BigDecimal("0");
        BigDecimal amt = new BigDecimal(price);

        if (list.size() > 0) {
            for (TaxDet det : list) {
                tax = tax.add(new BigDecimal(det.getTAXVAL()).multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100")), 2)));
                amt = new BigDecimal("100").multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100")), 2));
            }
        }
        return amt.toString();
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public String calculateTax(String itemCode, BigDecimal amt) {

        String comCode = new ItemController(context).getTaxComCodeByItemCode(itemCode);
        ArrayList<TaxDet> list = new TaxDetController(context).getTaxInfoByComCode(comCode);
        BigDecimal tax = new BigDecimal("0");

        if (list.size() > 0) {

            for (TaxDet det : list) {
                tax = tax.add(new BigDecimal(det.getTAXVAL()).multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100")), 3, BigDecimal.ROUND_HALF_EVEN)));
                amt = new BigDecimal("100").multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100")), 3, BigDecimal.ROUND_HALF_EVEN));
            }
        }
        return String.format("%.2f", tax);
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public String[] calculateTaxForward(String itemCode, double amt) {

        String comCode = new ItemController(context).getTaxComCodeByItemCode(itemCode);
        ArrayList<TaxDet> list = new TaxDetController(context).getTaxInfoByComCode(comCode);
        double tax = 0;
        String sArray[] = new String[2];

        if (list.size() > 0) {

            for (int i = list.size() - 1; i > -1; i--) {
                tax += Double.parseDouble(list.get(i).getTAXVAL()) * (amt / 100);
                amt = (Double.parseDouble(list.get(i).getTAXVAL()) + 100) * (amt / 100);
            }
        }

        sArray[0] = String.format("%.2f", amt);
        sArray[1] = String.format("%.2f", tax);
        return sArray;
    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public String[] calculatePriceTaxForward(String itemCode, double price) {

        String comCode = new ItemController(context).getTaxComCodeByItemCode(itemCode);
        ArrayList<TaxDet> list = new TaxDetController(context).getTaxInfoByComCode(comCode);
        double tax = 0;
        String sArray[] = new String[2];

        if (list.size() > 0) {

            for (int i = list.size() - 1; i > -1; i--) {
                tax += Double.parseDouble(list.get(i).getTAXVAL()) * (price / 100);
                price = (Double.parseDouble(list.get(i).getTAXVAL()) + 100) * (price / 100);
            }
        }

        sArray[0] = String.format("%.2f", price);//return tax forward price
        sArray[1] = String.format("%.2f", tax); // return tax price
        return sArray;
    }

    public String calculateReverseTaxFromDebTax(String debtorCode, String itemCode, BigDecimal amt) {

        String comCode = new ItemController(context).getTaxComCodeByItemCodeBeforeDebTax(itemCode, debtorCode);
        ArrayList<TaxDet> list = new TaxDetController(context).getTaxInfoByComCode(comCode);
        BigDecimal tax = new BigDecimal("0");

        if (list.size() > 0) {

            for (TaxDet det : list) {
                tax = tax.add(new BigDecimal(det.getTAXVAL()).multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100")), 4, BigDecimal.ROUND_HALF_EVEN)));
                //amt = new BigDecimal("100").multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100")), 4, BigDecimal.ROUND_HALF_EVEN));
                //amt = new BigDecimal("100").multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100"))));
                amt = new BigDecimal("100").multiply(amt.divide(new BigDecimal(det.getTAXVAL()).add(new BigDecimal("100")), 8, BigDecimal.ROUND_HALF_EVEN));

            }
        }
        //return String.format("%.2f", tax);
        return String.valueOf(amt);
        //return String.format("%.2f", amt);
    }

    public String[] calculateTaxForwardFromDebTax(String debtorCode, String itemCode, double amt) {

        String comCode = new ItemController(context).getTaxComCodeByItemCodeBeforeDebTax(itemCode, debtorCode);
        ArrayList<TaxDet> list = new TaxDetController(context).getTaxInfoByComCode(comCode);
        double tax = 0;
        String sArray[] = new String[2];

        if (list.size() > 0) {

            for (int i = list.size() - 1; i > -1; i--) {
                tax += Double.parseDouble(list.get(i).getTAXVAL()) * (amt / 100);
                amt = (Double.parseDouble(list.get(i).getTAXVAL()) + 100) * (amt / 100);
            }
        }

        sArray[0] = String.format("%.2f", amt);
        sArray[1] = String.format("%.2f", tax);
        return sArray;
    }
}
