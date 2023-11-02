package com.datamation.kfdsfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.OrderDetNew;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.model.OrderDisc;
import com.datamation.kfdsfa.model.PreProduct;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rashmi
 */

public class OrderDetailController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "OrderDetailController";
    public SharedPref mSharedPref;

    public OrderDetailController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        this.mSharedPref = SharedPref.getInstance(context);
    }

    public void open() throws SQLException {

        dB = dbHelper.getWritableDatabase();

    }

    @SuppressWarnings("static-access")
    public int createOrUpdateOrdDet(ArrayList<OrderDetail> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (OrderDetail ordDet : list) {

                Cursor cursor = null;
                ContentValues values = new ContentValues();


                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ITEMCODE
                        + " = '" + ordDet.getFORDDET_ITEM_CODE() + "' and " + ValueHolder.TYPE
                        + " = '" + ordDet.getFORDDET_TYPE() + "' and " + ValueHolder.ORDERID
                        + " = '" + ordDet.getOrderId() + "' ";

                cursor = dB.rawQuery(selectQuery, null);
                // commented due to table changed

                values.put(ValueHolder.AMT, ordDet.getFORDDET_AMT());
                values.put(ValueHolder.ITEMCODE, ordDet.getFORDDET_ITEM_CODE());
                values.put(ValueHolder.PRILCODE, ordDet.getFORDDET_PRIL_CODE());
                values.put(ValueHolder.QTY, ordDet.getFORDDET_QTY());
                values.put(ValueHolder.REFNO, ordDet.getFORDDET_REFNO());
                values.put(ValueHolder.ORDERID, ordDet.getOrderId());
                values.put(ValueHolder.SELL_PRICE, ordDet.getFORDDET_SELL_PRICE());
                values.put(ValueHolder.IS_ACTIVE, ordDet.getFORDDET_IS_ACTIVE());
                values.put(ValueHolder.ITEMNAME, ordDet.getFORDDET_ITEMNAME());
                values.put(ValueHolder.BAL_QTY, ordDet.getFORDDET_BAL_QTY());
                values.put(ValueHolder.B_AMT, ordDet.getFORDDET_B_AMT());
                values.put(ValueHolder.B_DIS_AMT, ordDet.getFORDDET_B_DIS_AMT());
                values.put(ValueHolder.BP_DIS_AMT, ordDet.getFORDDET_BP_DIS_AMT());
                values.put(ValueHolder.BT_TAX_AMT, ordDet.getFORDDET_BT_TAX_AMT());
                values.put(ValueHolder.TAXAMT, ordDet.getFORDDET_TAX_AMT());
                values.put(ValueHolder.DISAMT, ordDet.getFORDDET_DIS_AMT());
                values.put(ValueHolder.DISPER, ordDet.getFORDDET_DIS_PER());
                values.put(ValueHolder.PICE_QTY, ordDet.getFORDDET_PICE_QTY());
                values.put(ValueHolder.SELL_PRICE, ordDet.getFORDDET_SELL_PRICE());
                values.put(ValueHolder.B_SELL_PRICE, ordDet.getFORDDET_B_SELL_PRICE());
                values.put(ValueHolder.SEQNO, ordDet.getFORDDET_SEQNO());
                values.put(ValueHolder.TAXCOMCODE, ordDet.getFORDDET_TAX_COM_CODE());
                values.put(ValueHolder.T_SELL_PRICE, ordDet.getFORDDET_T_SELL_PRICE());
                values.put(ValueHolder.BT_SELL_PRICE, ordDet.getFORDDET_BT_SELL_PRICE());
                values.put(ValueHolder.TXNTYPE, ordDet.getFORDDET_TXN_TYPE());
                values.put(ValueHolder.TXNDATE, ordDet.getFORDDET_TXN_DATE());
                values.put(ValueHolder.P_DIS_AMT, ordDet.getFORDDET_P_DIS_AMT());
                values.put(ValueHolder.TYPE, ordDet.getFORDDET_TYPE());
                values.put(ValueHolder.PICE_QTY, ordDet.getFORDDET_PICE_QTY());
                values.put(ValueHolder.CASE_QTY, ordDet.getFORDDET_CASE_QTY());

                int cn = cursor.getCount();

                if (cn > 0) {
                    count = dB.update(ValueHolder.TABLE_ORDDET, values, ValueHolder.ITEMCODE+ " = '"+ordDet.getFORDDET_ITEM_CODE() +"' and " + ValueHolder.TYPE + " = '"+ordDet.getFORDDET_TYPE() +"' and " + ValueHolder.ORDERID+ " = '"+ordDet.getOrderId()+"'", null);
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_ORDDET, null, values);
                }

                cursor.close();
            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }
    public boolean tableHasRecords(String orderid) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDDET+ " Where "+ValueHolder.ORDERID+" = '"+orderid+"'", null);
            if (cursor.getCount() > 0)
                result = true;
            else
                result = false;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();

        }

        return result;

    }
    public ArrayList<PreProduct> getAllItems(String newText,String loccode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
            // cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE itemcode || itemname LIKE '%" + newText + "%'  group by itemcode order by CAST(qoh AS FLOAT) desc", null);
            cursor = dB.rawQuery("SELECT  itm.ItemCode AS ItemCode , itm.ItemName AS ItemName , itm.Pack AS Pack, " +
                    " IFNULL(pri.Price,0.0) AS Price , " +

                    "  loc.QOH AS QOH , '0' AS Qty " +
                    "  FROM TblItem itm " +
                    "  INNER JOIN TblItemLoc loc ON loc.ItemCode = itm.ItemCode " +
                    "  LEFT JOIN TblItemPri pri ON pri.ItemCode = itm.ItemCode  " +
                    "  WHERE loc.LocCode = '"+loccode+"' AND pri.ItemCode = itm.ItemCode AND " +
                    " itm.itemcode || itm.itemname LIKE '%" + newText + "%'  Group by  itm.ItemCode ORDER BY itm.itemname ", null);
//                    " itm.itemcode || itm.itemname LIKE '%" + newText + "%'  Group by  itm.ItemCode ORDER BY CAST(QOH AS FLOAT) DESC ", null);

            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setPREPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.PACK)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRICE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                product.setPREPRODUCT_BALQTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                product.setPREPRODUCT_Bonus("SHOW BONUS");
//                product.setPREPRODUCT_REACODE(new FreeMslabController(context).getFreeDetails(product.getPREPRODUCT_ITEMCODE() , mSharedPref.getSelectedDebCode()));
                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
    public ArrayList<PreProduct> getAlreadyOrderedItems(String newText,String loccode,String orderId) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
            // cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE itemcode || itemname LIKE '%" + newText + "%'  group by itemcode order by CAST(qoh AS FLOAT) desc", null);
            cursor = dB.rawQuery("          SELECT  itm.ItemCode AS ItemCode , itm.ItemName AS ItemName , itm.Pack AS Pack," +
                    " IFNULL(pri.Price,0.0) AS Price , " +

                    "  loc.QOH AS QOH , IFNULL(orddet.qty,0.0) AS Qty , IFNULL(orddet.BalQty,0.0) AS BalQty" +
                    "  FROM TblItem itm " +
                    "  INNER JOIN TblItemLoc loc ON loc.ItemCode = itm.ItemCode " +
                    "  LEFT JOIN TblItemPri pri ON pri.ItemCode = itm.ItemCode  " +
                    "  LEFT JOIN TblOrddet orddet ON orddet.ItemCode = itm.ItemCode AND orddet.orderId = '" + orderId + "' AND orddet.types='SA' " +
                    "  WHERE loc.LocCode = '"+loccode+"' AND pri.ItemCode = itm.ItemCode AND " +
                            " itm.itemcode || itm.itemname LIKE '%" + newText + "%' "+
//                    " ORDER BY itm.itemname ", null);
                    " Group by  itm.ItemCode ORDER BY itm.itemname ", null);
//            " Group by  itm.ItemCode ORDER BY CAST(QOH AS FLOAT) DESC ", null);

            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setPREPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.PACK)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRICE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                product.setPREPRODUCT_BALQTY(cursor.getString(cursor.getColumnIndex(ValueHolder.BAL_QTY)));
                product.setPREPRODUCT_Bonus("SHOW BONUS");
//                product.setPREPRODUCT_REACODE(new FreeMslabController(context).getFreeDetails(product.getPREPRODUCT_ITEMCODE() , mSharedPref.getSelectedDebCode()));
                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
    @SuppressWarnings("static-access")
    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + " = '" + refno + "' ";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_ORDDET, ValueHolder.ORDERID + " ='" + refno + "'", null);
                Log.v("Success Stauts", count + "");
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
    public int restZeroQtyData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + " = '" + refno + "' and "+ValueHolder.QTY+ " = '0'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_ORDDET, ValueHolder.ORDERID + " ='" + refno + "'  and "+ValueHolder.QTY+ " = '0'", null);
                Log.d(">>>dlt editOrder", count + "");
            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
            Log.d(">>>dlt editOrder", e.toString() + "");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }
    @SuppressWarnings("static-access")
    public int restFreeIssueData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + " ='" + refno + "' AND " + ValueHolder.TYPE + " = 'FD'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_ORDDET, ValueHolder.ORDERID + " ='" + refno + "' AND " + ValueHolder.TYPE + " = 'FD'", null);
                Log.v("Success Stauts", count + "");
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

    public boolean getCheckQOH(String RefNo, String ItemCode, int FreeQty, int QOH) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }


        Cursor cursor = null;
        String selectQuery = "SELECT QTY FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + "='" + RefNo + "' AND "+ValueHolder.ITEMCODE + "='" + ItemCode +"'";
        cursor = dB.rawQuery(selectQuery, null);

        try {


            //int cn = cursor.getCount();

            while (cursor.moveToNext()) {

                if (cursor.getString(cursor.getColumnIndex("Qty")) != null){
                    int SalQty =  Integer.parseInt(cursor.getString(cursor.getColumnIndex("Qty")));
                    if(QOH >= (SalQty+FreeQty)){
                        return true;
                    }else{
                        return false;
                    }

                }else{
                    return false;
                }
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return false;

    }

    public ArrayList<OrderDetail> getAllOrderDetailsForTaxUpdate(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE "
                + ValueHolder.ORDERID + "='" + refno + "' and "
                + ValueHolder.IS_ACTIVE +" = '0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();


                //commented due to table changed

                ordDet.setFORDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                ordDet.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setFORDDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
                ordDet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                ordDet.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
                ordDet.setFORDDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
                ordDet.setFORDDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                ordDet.setFORDDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                ordDet.setFORDDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));

                list.add(ordDet);

            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }
    public ArrayList<OrderDetail> getAllOrderDets(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE "
                + ValueHolder.ORDERID + "='" + refno + "' and " + ValueHolder.QTY+" <> '0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();


                //commented due to table changed

                ordDet.setFORDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                ordDet.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setFORDDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
                ordDet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                ordDet.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
                ordDet.setFORDDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
                ordDet.setFORDDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                ordDet.setFORDDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                ordDet.setFORDDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));

                list.add(ordDet);

            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

    public ArrayList<OrderDetail> getSAForFreeIssueCalc(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();
// Added Order by clause by Menaka 
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.TYPE + "='SA' AND " +  ValueHolder.AMT + "<>'0' AND " + ValueHolder.ORDERID + "='" + refno + "' Order by "+ValueHolder.ITEMCODE;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

                ordDet.setFORDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                ordDet.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setFORDDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
                ordDet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                ordDet.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
                ordDet.setFORDDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
                ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                ordDet.setFORDDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
                ordDet.setFORDDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));

                list.add(ordDet);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }
    public ArrayList<OrderDetail> mUpdatePrsSales(PreProduct tempOrder)
    {

        ArrayList<OrderDetail> SOList = new ArrayList<OrderDetail>();

        String UnitPrice = "";
        double amt = 0.0;
        int  totPieces= 0;
        OrderDetail ordDet = new OrderDetail();
        totPieces = Integer.parseInt(tempOrder.getPREPRODUCT_QTY());
        amt = totPieces * Double.parseDouble(tempOrder.getPREPRODUCT_PRICE());
        ordDet.setFORDDET_AMT(String.format("%.2f", amt));
        ordDet.setFORDDET_BAL_QTY(tempOrder.getPREPRODUCT_BALQTY() + "");
        ordDet.setFORDDET_B_AMT(String.format("%.2f", amt));
        ordDet.setFORDDET_B_DIS_AMT(String.format("%.2f", (Double.parseDouble(tempOrder.getPREPRODUCT_PRICE()) * ((double) totPieces)) * Double.parseDouble("0.00") / 100));
        ordDet.setFORDDET_BP_DIS_AMT(String.format("%.2f", (Double.parseDouble(tempOrder.getPREPRODUCT_PRICE()) * ((double) totPieces)) * Double.parseDouble("0.00") / 100));
        ordDet.setFORDDET_B_SELL_PRICE(tempOrder.getPREPRODUCT_PRICE());
        ordDet.setFORDDET_BT_TAX_AMT("0");
        ordDet.setFORDDET_BT_SELL_PRICE(tempOrder.getPREPRODUCT_PRICE());
        ordDet.setFORDDET_DIS_AMT(String.format("%.2f", (Double.parseDouble(tempOrder.getPREPRODUCT_PRICE()) * ((double) totPieces)) * Double.parseDouble("0.00") / 100));
        ordDet.setFORDDET_DIS_PER("0.00");
        ordDet.setFORDDET_FREE_QTY("0");
        ordDet.setFORDDET_ITEM_CODE(tempOrder.getPREPRODUCT_ITEMCODE());
        ordDet.setFORDDET_P_DIS_AMT(String.format("%.2f", (Double.parseDouble(tempOrder.getPREPRODUCT_PRICE()) * ((double) totPieces)) * Double.parseDouble("0.00") / 100));
        ordDet.setFORDDET_PRIL_CODE(new ItemPriceController(context).getPrilCodeByItemCode(tempOrder.getPREPRODUCT_ITEMCODE()));
        ordDet.setFORDDET_QTY(tempOrder.getPREPRODUCT_QTY() + "");
        ordDet.setFORDDET_DIS_VAL_AMT("0.00");
        ordDet.setFORDDET_PICE_QTY(tempOrder.getPREPRODUCT_QTY() + "");
        ordDet.setFORDDET_REA_CODE("");
        ordDet.setFORDDET_TYPE("SA");
        ordDet.setFORDDET_RECORD_ID("");
        ordDet.setFORDDET_REFNO("");

        if(new SharedPref(context).generateOrderId() == 0){
            long time = System.currentTimeMillis();
        ordDet.setOrderId(time);
        }else{
        ordDet.setOrderId(new SharedPref(context).generateOrderId());
        }
        ordDet.setFORDDET_SELL_PRICE(tempOrder.getPREPRODUCT_PRICE());
        ordDet.setFORDDET_SEQNO(1 + "");
        ordDet.setFORDDET_TAX_AMT("0.00");
        ordDet.setFORDDET_TAX_COM_CODE(new ItemController(context).getItemTaxComCode(tempOrder.getPREPRODUCT_ITEMCODE()));
        ordDet.setFORDDET_TIMESTAMP_COLUMN("");
        ordDet.setFORDDET_T_SELL_PRICE(tempOrder.getPREPRODUCT_PRICE());
        ordDet.setFORDDET_TXN_DATE(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));
        ordDet.setFORDDET_IS_ACTIVE("1");
        ordDet.setFORDDET_ITEMNAME(new ItemController(context).getItemNameByCode(tempOrder.getPREPRODUCT_ITEMCODE()));
        ordDet.setFORDDET_PACKSIZE(new ItemController(context).getPackSizeByCode(tempOrder.getPREPRODUCT_ITEMCODE()));

        SOList.add(ordDet);



        return SOList;
    }
    public ArrayList<OrderDetail> getAllOrderDetails(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + "='" + refno + "' AND " +ValueHolder.QTY + " <> '0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

                ordDet.setFORDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                ordDet.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_B_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_B_DIS_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.DISAMT)));
                ordDet.setFORDDET_BP_DIS_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.DISAMT)));
                ordDet.setFORDDET_B_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_BT_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXAMT)));
                ordDet.setFORDDET_BT_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_FREE_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_QTY)));
                ordDet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setFORDDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
                ordDet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                ordDet.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
                ordDet.setFORDDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_SEQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.SEQNO)));
                ordDet.setFORDDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXAMT)));
                ordDet.setFORDDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                ordDet.setFORDDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                ordDet.setFORDDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
                ordDet.setFORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
                ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));

                ordDet.setFORDDET_PACKSIZE(cursor.getString(cursor.getColumnIndex(ValueHolder.PACKSIZE)));
                ordDet.setFORDDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));

                list.add(ordDet);

            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

    @SuppressWarnings("static-access")
    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.IS_ACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_ORDDET, values, ValueHolder.ORDERID + " =?", new String[] { String.valueOf(refno) });
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



    public ArrayList<OrderDetail> getAllActives(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID
                + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();
                //commented due to table changed

                ordDet.setFORDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                ordDet.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setFORDDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
                ordDet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                ordDet.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
                ordDet.setFORDDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
                ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                ordDet.setFORDDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));

                list.add(ordDet);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

    public boolean isAnyActiveOrders()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount()>0)
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return false;
    }

    public ArrayList<OrderDetNew> getAllUnSyncData(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetNew> list = new ArrayList<OrderDetNew>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID
                + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetNew ordDet = new OrderDetNew();

                ordDet.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID)));
                ordDet.setAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT))));
                ordDet.setBamt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_AMT))));
                ordDet.setItemcode(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setPrilCode(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
//                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));

                if(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)).equals("FI")){
                    ordDet.setBalQty(0);
                    ordDet.setQty(0);
                    ordDet.setFreeQty(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY))));
                    ordDet.setFreeBalQty(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY))));
                    ordDet.setInvfbalqty(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY))));
                    ordDet.setInvbalqty(0);
                    ordDet.setFriss(1);
                }else{
                    ordDet.setBalQty(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY))));
                    ordDet.setQty(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY))));
                    ordDet.setFreeQty(0);
                    ordDet.setFreeBalQty(0);
                    ordDet.setInvfbalqty(0);
                    ordDet.setInvbalqty(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY))));
                    ordDet.setFriss(0);
                }

                ordDet.setSellPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE))));
                ordDet.setBsellPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_SELL_PRICE))));
                ordDet.setTsellPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.T_SELL_PRICE))));
                ordDet.setBtsellPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BT_SELL_PRICE))));
                ordDet.setTaxComCode(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
             //   ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                ordDet.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
                ordDet.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                ordDet.setSeqNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ValueHolder.SEQNO))));
                //ordDet.setFORDDET_PICE_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.PICE_QTY)));
              //  ordDet.setDisPer(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.SCH_DISPER))));//send dis per
                ordDet.setTaxAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXAMT))));
                ordDet.setBtaxAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BT_TAX_AMT))));

                list.add(ordDet);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

    public ArrayList<OrderDetail> getAllUnSync(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID
                + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();

                ordDet.setFORDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                ordDet.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_B_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.B_AMT)));
                ordDet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setFORDDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
                ordDet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                ordDet.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
                ordDet.setFORDDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_B_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.B_SELL_PRICE)));
                ordDet.setFORDDET_T_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.T_SELL_PRICE)));
                ordDet.setFORDDET_BT_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.BT_SELL_PRICE)));
                ordDet.setFORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
                ordDet.setFORDDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                ordDet.setFORDDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
                ordDet.setFORDDET_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                ordDet.setFORDDET_SEQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.SEQNO)));
                ordDet.setFORDDET_PICE_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.PICE_QTY)));
                ordDet.setFORDDET_BAL_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.BAL_QTY)));
                ordDet.setFORDDET_DIS_PER(cursor.getString(cursor.getColumnIndex(ValueHolder.SCH_DISPER)));//send dis per
                ordDet.setFORDDET_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXAMT)));
                ordDet.setFORDDET_BT_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.BT_TAX_AMT)));

                list.add(ordDet);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

    public void mDeleteRecords(String RefNo, String itemCode, String type) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(ValueHolder.TABLE_ORDDET, ValueHolder.ORDERID + " = '" + RefNo + "'  AND " + ValueHolder.ITEMCODE + " ='" + itemCode + "' AND "+ ValueHolder.TYPE + " ='" + type + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }

    public void deleteRecords(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(ValueHolder.TABLE_ORDDET, ValueHolder.ORDERID + " ='" + RefNo + "'" , null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }

    public String getLastSequnenceNo(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT Max(seqno) as seqno FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + "='" + RefNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);
            cursor.moveToFirst();

            return (cursor.getInt(cursor.getColumnIndex("seqno")) + 1) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        } finally {
            dB.close();
        }
    }

    public int getItemCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + ValueHolder.TABLE_ORDDET + " WHERE  " + ValueHolder.ORDERID + "='" + refNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("RefNo")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return 0;

    }

    public ArrayList<OrderDetail> getAllItemsAddedInCurrentSale(String refNo,String type,String type2)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();
        String selectQuery = "select ItemCode,Qty,Amt,ReaCode,Types from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.QTY + "<>'0' AND " + ValueHolder.AMT + "<>'0' AND " + ValueHolder.ORDERID + "='" + refNo + "' AND ("+ ValueHolder.TYPE + "= '"+type+"' OR "+ ValueHolder.TYPE +" = '"+type2+"')" ;
        Cursor cursor = dB.rawQuery(selectQuery, null);

        try
        {
            while (cursor.moveToNext())
            {
                OrderDetail orderDetail = new OrderDetail();
                // commented due to table changed
                orderDetail.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                orderDetail.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                orderDetail.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                orderDetail.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                list.add(orderDetail);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.v(TAG + " Exception", ex.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }

    public int updateProductPrice(String itemCode, String price,String amt, String type) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(ValueHolder.SELL_PRICE, price);
            values.put(ValueHolder.AMT, amt);
            count=(int)dB.update(ValueHolder.TABLE_ORDDET, values, ValueHolder.ITEMCODE
                    + " = '" + itemCode + "' and "+ValueHolder.TYPE + " = '" + type + "' ", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return  count;
    }



    public ArrayList<OrderDetail> getAllFreeIssue(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ORDERID + "='" + refno + "' AND " + ValueHolder.TYPE  + "='FI'" ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {

            while (cursor.moveToNext()) {

                OrderDetail tranSODet=new OrderDetail();
                tranSODet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                tranSODet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                list.add(tranSODet);
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

    public void UpdateEditedQty(String RefNo, String Task) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            //ArrayList<InvDet> list = new InvDetController(context).getAllItemsforPrint(RefNo);
            ArrayList<OrderDetail> list = new OrderDetailController(context).getAllUnSync(RefNo);//2019-11-18 rashmi

            for (OrderDetail item : list) {

                int qty = 0;

                Cursor cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDDET+ " WHERE " +ValueHolder.ITEMCODE + "='" + item.getFORDDET_ITEM_CODE() + "' AND " + ValueHolder.BAL_QTY + "<>'0' and "+ValueHolder.ORDERID + "='" + RefNo + "'", null);
                int Qty = Integer.parseInt(item.getFORDDET_QTY());
                double price = Double.parseDouble(item.getFORDDET_SELL_PRICE());


                if (cursor.getCount() > 0) {

                    while (cursor.moveToNext()) {
                        qty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ValueHolder.BAL_QTY)));
                    }

                    ContentValues values = new ContentValues();

                    if (Task.equals("+")) {
                        values.put(ValueHolder.QTY, String.valueOf(qty + Qty));
                        values.put(ValueHolder.AMT, String.valueOf(price*(Qty + qty)));
                        values.put(ValueHolder.BAL_QTY, "0");
                    } else {
                        Log.d(">>>balqtyinsummary",item.getFORDDET_ITEM_CODE()+">>>"+qty);
                        Log.d(">>>qtyinsummary",item.getFORDDET_ITEM_CODE()+">>>"+Qty);
                        values.put(ValueHolder.QTY, String.valueOf(Qty - qty));
                        values.put(ValueHolder.AMT, String.valueOf(price*(Qty - qty)));
                        values.put(ValueHolder.BAL_QTY, "0");
                    }


                    dB.update(ValueHolder.TABLE_ORDDET, values, ValueHolder.ITEMCODE + "=? AND " + ValueHolder.ORDERID + "=?", new String[]{item.getFORDDET_ITEM_CODE(), RefNo});

                }

                cursor.close();

            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

    public ArrayList<OrderDetail> getAllOrderDetsBySearch(String refno, String key) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDDET + " WHERE "
                + ValueHolder.ORDERID + "='" + refno + "' and " + ValueHolder.QTY+ " <> '0' and " + ValueHolder.TXNDATE + " LIKE '%" + key + "%'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                OrderDetail ordDet = new OrderDetail();


                //commented due to table changed

                ordDet.setFORDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                ordDet.setFORDDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                ordDet.setFORDDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                ordDet.setFORDDET_PRIL_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
                ordDet.setFORDDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                ordDet.setFORDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                ordDet.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
                ordDet.setFORDDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.SELL_PRICE)));
                ordDet.setFORDDET_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
                ordDet.setFORDDET_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                ordDet.setFORDDET_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                ordDet.setFORDDET_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                ordDet.setFORDDET_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));

                list.add(ordDet);

            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

    public int DeleteZeroValueData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDET+ " WHERE " + ValueHolder.ORDERID + "='" + refno + "' and " +
                    ValueHolder.QTY + "='0' and " + ValueHolder.AMT + "='0.00'";

            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_ORDDET, ValueHolder.ORDERID + " ='" + refno + "' and " +
                        ValueHolder.QTY + "='0' and " + ValueHolder.AMT + "='0.00'", null);

                Log.v("Success", count + "");
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



}
