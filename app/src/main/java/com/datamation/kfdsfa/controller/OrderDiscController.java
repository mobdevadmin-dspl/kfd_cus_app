package com.datamation.kfdsfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.OrderDisc;

import java.util.ArrayList;

public class OrderDiscController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "OrderDiscController";
    // rashmi
    public OrderDiscController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }
    @SuppressWarnings("static-access")
    public int createOrUpdateOrdDisc(ArrayList<OrderDisc> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (OrderDisc orderDisc : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDISC + " WHERE " + ValueHolder.REFNO + " = '" + orderDisc.getRefNo() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(ValueHolder.REFNO, orderDisc.getRefNo());
                values.put(ValueHolder.TXNDATE, orderDisc.getTxnDate());
                values.put(ValueHolder.REFNO1, orderDisc.getRefNo1());
                values.put(ValueHolder.ITEMCODE, orderDisc.getItemCode());
                values.put(ValueHolder.DISAMT, orderDisc.getDisAmt());
                values.put(ValueHolder.DISPER, orderDisc.getDisPer());

                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(ValueHolder.TABLE_ORDDISC, values, ValueHolder.REFNO + " =?", new String[]{String.valueOf(orderDisc.getRefNo())});

                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_ORDDISC, null, values);
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
        return count;

    }

	/*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*--*-Update order discount table-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateOrderDiscount(OrderDisc orderDisc, String DiscRef, String DiscPer) {

		/* Remove record using order discount ref no & item code */
        RemoveOrderDiscRecord(orderDisc.getRefNo(), orderDisc.getItemCode());

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();

            values.put(ValueHolder.REFNO, orderDisc.getRefNo());
            values.put(ValueHolder.TXNDATE, orderDisc.getTxnDate());
            values.put(ValueHolder.REFNO1, DiscRef);
            values.put(ValueHolder.ITEMCODE, orderDisc.getItemCode());
            values.put(ValueHolder.DISAMT, orderDisc.getDisAmt());
            values.put(ValueHolder.DISPER, DiscPer);
            dB.insert(ValueHolder.TABLE_ORDDISC, null, values);

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {

            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*Check record availability using RefNo and Itemcode in FORDDISC*-*-*-*-*-*-*-*-*-*-*-*/

    public boolean isRecordAvailable(String RefNo, String ItemCode) {

        int count = 0;
        boolean Result = false;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDISC + " WHERE " + ValueHolder.REFNO + " = '" + RefNo + "' AND " + ValueHolder.ITEMCODE + " = '" + ItemCode + "'";
            cursor = dB.rawQuery(selectQuery, null);
            count = cursor.getCount();

            if (count > 0) {
                Result = true;
            } else {
                Result = false;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return Result;
    }

	/*-*-*-*-*-*-*-*- Remove particular record from Order Discount table -*-*-* *-*-*-*-*-*/

    public void RemoveOrderDiscRecord(String RefNo, String ItemCode) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDDISC + " WHERE " + ValueHolder.REFNO + " = '" + RefNo + "' AND " + ValueHolder.ITEMCODE + " = '" + ItemCode + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(ValueHolder.TABLE_ORDDISC, ValueHolder.REFNO + " = '" + RefNo + "' AND " + ValueHolder.ITEMCODE + " = '" + ItemCode + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }


    public void clearData(String RefNo) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDDISC + " WHERE " + ValueHolder.REFNO + " = '" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(ValueHolder.TABLE_ORDDISC, ValueHolder.REFNO + " = '" + RefNo + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

	

	/* Delete all records */

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDDISC, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_ORDDISC, null, null);
                Log.v("Success", success + "");
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

	/*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**--*--*-*-*-*-*-*-*-*-*-*-*-*-**/

    public ArrayList<OrderDisc> getAllOrderDiscs(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderDisc> list = new ArrayList<OrderDisc>();
        try {

            Cursor cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDDISC + " WHERE RefNo='" + Refno + "'", null);

            while (cursor.moveToNext()) {
                OrderDisc orderDisc = new OrderDisc();

                orderDisc.setDisAmt(cursor.getString(cursor.getColumnIndex(ValueHolder.DISAMT)));
                orderDisc.setDisPer(cursor.getString(cursor.getColumnIndex(ValueHolder.DISPER)));
                orderDisc.setItemCode(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                orderDisc.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                orderDisc.setRefNo1(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO1)));
                orderDisc.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                list.add(orderDisc);
            }

            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

    public void ClearDiscountForPreSale(String RefNo) { // ORDER DET REFNO

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDDISC + " WHERE " + ValueHolder.REFNO + " = '" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(ValueHolder.TABLE_ORDDISC, ValueHolder.REFNO + " = '" + RefNo + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

}
