package com.datamation.kfdsfa.controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderNew;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "OrderController";

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";

    // rashmi - 2019-12-17 *******************************************************************************
    public OrderController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateOrdHed(ArrayList<Order> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Order ordHed : list) {

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID
                        + " = '" + ordHed.getOrderId() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.REFNO, ordHed.getFORDHED_REFNO());
                values.put(ValueHolder.ORDERID, ordHed.getOrderId());
                values.put(ValueHolder.ADDDATE, ordHed.getFORDHED_ADD_DATE());
                values.put(ValueHolder.ADDMACH, ordHed.getFORDHED_ADD_MACH());
                values.put(ValueHolder.ADDUSER, ordHed.getFORDHED_ADD_USER());
                values.put(ValueHolder.APP_DATE, ordHed.getFORDHED_APP_DATE());
                values.put(ValueHolder.APPSTS, ordHed.getFORDHED_APPSTS());
                values.put(ValueHolder.APP_USER, ordHed.getFORDHED_APP_USER());
                values.put(ValueHolder.BP_TOTAL_DIS, ordHed.getFORDHED_BP_TOTAL_DIS());
                values.put(ValueHolder.B_TOTAL_AMT, ordHed.getFORDHED_B_TOTAL_AMT());
                values.put(ValueHolder.B_TOTAL_DIS, ordHed.getFORDHED_B_TOTAL_DIS());
                values.put(ValueHolder.B_TOTAL_TAX, ordHed.getFORDHED_B_TOTAL_TAX());
                values.put(ValueHolder.COSTCODE, ordHed.getFORDHED_COST_CODE());
                values.put(ValueHolder.CUR_CODE, ordHed.getFORDHED_CUR_CODE());
                values.put(ValueHolder.CUR_RATE, ordHed.getFORDHED_CUR_RATE());
                values.put(ValueHolder.DEBCODE, ordHed.getFORDHED_DEB_CODE());
                values.put(ValueHolder.DISPER, ordHed.getFORDHED_DIS_PER());
                values.put(ValueHolder.START_TIME_SO, ordHed.getFORDHED_START_TIME_SO());
                values.put(ValueHolder.END_TIME_SO, ordHed.getFORDHED_END_TIME_SO());
                values.put(ValueHolder.LONGITUDE, ordHed.getFORDHED_LONGITUDE());
                values.put(ValueHolder.LATITUDE, ordHed.getFORDHED_LATITUDE());
                values.put(ValueHolder.LOCCODE, ordHed.getFORDHED_LOC_CODE());
                values.put(ValueHolder.MANU_REF, ordHed.getFORDHED_MANU_REF());
                values.put(ValueHolder.APPVERSION, ordHed.getFORDHED_RECORD_ID());
                values.put(ValueHolder.REMARKS, ordHed.getFORDHED_REMARKS());
                values.put(ValueHolder.REPCODE, ordHed.getFORDHED_REPCODE());
                values.put(ValueHolder.TAX_REG, ordHed.getFORDHED_TAX_REG());
                values.put(ValueHolder.TOTALAMT, ordHed.getFORDHED_TOTAL_AMT());
                values.put(ValueHolder.TOTALDIS, ordHed.getFORDHED_TOTALDIS());
                values.put(ValueHolder.TOTAL_TAX, ordHed.getFORDHED_TOTAL_TAX());
                values.put(ValueHolder.TXNTYPE, ordHed.getFORDHED_TXN_TYPE());
                values.put(ValueHolder.TXNDATE, ordHed.getFORDHED_TXN_DATE());
                values.put(ValueHolder.ADDRESS, ordHed.getFORDHED_ADDRESS());
                values.put(ValueHolder.TOTAL_ITM_DIS, ordHed.getFORDHED_TOTAL_ITM_DIS());
                values.put(ValueHolder.TOT_MKR_AMT, ordHed.getFORDHED_TOT_MKR_AMT());
                values.put(ValueHolder.DELV_DATE, ordHed.getFORDHED_DELV_DATE());
                values.put(ValueHolder.ROUTECODE, ordHed.getFORDHED_ROUTE_CODE());
                values.put(ValueHolder.DIS_VAL, ordHed.getFORDHED_HED_DIS_VAL());
                values.put(ValueHolder.DIS_PER_VAL, ordHed.getFORDHED_HED_DIS_PER_VAL());
                values.put(ValueHolder.IS_SYNC, "0");
                values.put(ValueHolder.IS_ACTIVE, ordHed.getFORDHED_IS_ACTIVE());
                values.put(ValueHolder.PAYMENT_TYPE, ordHed.getFORDHED_PAYMENT_TYPE());
                values.put(ValueHolder.STATUS, ordHed.getFORDHED_STATUS());

                int cn = cursor.getCount();
                if (cn > 0) {
                    count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?",
                            new String[] { String.valueOf(ordHed.getOrderId()) });
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_ORDHED, null, values);
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
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID + " = '"
                    + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_ORDHED, ValueHolder.ORDERID + " ='" + refno + "'", null);
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

    @SuppressWarnings("static-access")
    public int InactiveStatusUpdate(String refno) {

        int count = 0;
        String UploadDate = "";
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        UploadDate = df.format(cal.getTime());
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID + " = '"
                    + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.IS_ACTIVE, "0");
            values.put(ValueHolder.END_TIME_SO, UploadDate);
            values.put(ValueHolder.ADDDATE, UploadDate);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?",
                        new String[] { String.valueOf(refno) });
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

    @SuppressWarnings("static-access")
    public int updateIsSynced(Order hed) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();



            //if (hed.getORDER_IS_SYNCED().equals("1")) {
            values.put(ValueHolder.IS_SYNC, "1");
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?", new String[] { String.valueOf(hed.getFORDHED_REFNO()) });
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(ValueHolder.TABLE_ORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
//            }

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
    public int updateIsSynced(String refno,String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();



            //if (hed.getORDER_IS_SYNCED().equals("1")) {
            values.put(ValueHolder.IS_SYNC, res);
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?", new String[] { refno });
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(ValueHolder.TABLE_ORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
//            }

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

    public int updateIsSynced(String refno, String res, String stat, String activeStatus) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();


            //if (hed.getORDER_IS_SYNCED().equals("1")) {
            values.put(ValueHolder.IS_SYNC, res);
            values.put(ValueHolder.STATUS, stat);
            values.put(ValueHolder.IS_ACTIVE, activeStatus);
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?", new String[]{refno});
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(ValueHolder.TABLE_ORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
//            }

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

    public int updateIsActive(String refno,String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();
            values.put(ValueHolder.IS_ACTIVE, res);
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?", new String[] { refno });
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
    public int updateIsSynced(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();



            //if (hed.getORDER_IS_SYNCED().equals("1")) {
            values.put(ValueHolder.IS_SYNC, "1");
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?", new String[] { refno });
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(ValueHolder.TABLE_ORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
//            }

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
    public int updateFeedback(String feedback,String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID
                    + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.FEEDBACK, feedback);


            int cn = cursor.getCount();
            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?",
                        new String[] { String.valueOf(refno) });
            } else {
                count = (int) dB.insert(ValueHolder.TABLE_ORDHED, null, values);
            }


        } catch (Exception e) {

            Log.v(TAG + " ExcptnInFeedbackUpdate", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

    public int getUnsyncedOrderCount()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_SYNC + "= '0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        int count = 0;

        try {
            count = cursor.getCount();


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

    @SuppressLint("Range")
    @SuppressWarnings("static-access")
    public ArrayList<Order> getAllOrders() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " where "+ ValueHolder.IS_ACTIVE + " <> '1' AND OrderId In (Select OrderId from TblOrddet) ORDER BY Id DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {

            Order order = new Order();
            OrderDetailController detDS = new OrderDetailController(context);

            // order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
            order.setDistDB(SharedPref.getInstance(context).getDistDB().trim());
            //order.setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));

            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            order.setFORDHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));


            list.add(order);

        }

        return list;
    }
    public int IsSavedHeader(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;


        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            //  values.put(DatabaseHelper.FINVHED_IS_ACTIVE, "0");

            int cn = cursor.getCount();
            count = cn;

//            if (cn > 0) {
//                count = dB.update(DatabaseHelper.TABLE_FINVHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
//            } else {
//                count = (int) dB.insert(DatabaseHelper.TABLE_FINVHED, null, values);
//            }

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
    public int IsSyncedOrder(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;


        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID + " = '" + refno + "' and "+ ValueHolder.IS_SYNC+ " = '1' ";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            //  values.put(DatabaseHelper.FINVHED_IS_ACTIVE, "0");

            int cn = cursor.getCount();
            count = cn;

//            if (cn > 0) {
//                count = dB.update(DatabaseHelper.TABLE_FINVHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
//            } else {
//                count = (int) dB.insert(DatabaseHelper.TABLE_FINVHED, null, values);
//            }

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

    public Order getAllActiveOrdHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
                + "= '1' and " + ValueHolder.IS_SYNC + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        Order presale = new Order();

        while (cursor.moveToNext()) {

            OrderDetailController detDS = new OrderDetailController(context);

            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
            presale.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            presale.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            presale.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            presale.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            presale.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            presale.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            // presale.setFORDHED_ADDTIME(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            presale.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            presale.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            presale.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            presale.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            presale.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            presale.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            presale.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            presale.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            presale.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            presale.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            presale.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            presale.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
            presale.setFORDHED_RECORD_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setOrdDet(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));

        }

        return presale;

    }

    public Order getAllActiveOrdHedByID(String RefNo) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
//                + "='1' and " + ValueHolder.IS_SYNC + "='0' and "+ ValueHolder.ORDERID + "='"
//                + RefNo + "'";

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where "
                + ValueHolder.IS_SYNC + "='0' and "+ ValueHolder.ORDERID + "='"
                + RefNo + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        Order presale = new Order();

        while (cursor.moveToNext()) {

            OrderDetailController detDS = new OrderDetailController(context);

            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
            presale.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            presale.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            presale.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            presale.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            presale.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            presale.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            // presale.setFORDHED_ADDTIME(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            presale.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            presale.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            presale.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            presale.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            presale.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            presale.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            presale.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            presale.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            presale.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            presale.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            presale.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            presale.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
            presale.setFORDHED_RECORD_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setOrdDet(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));

        }

        return presale;

    }

//    public Order getAllActiveOrdHedByIDAfterSave(String RefNo) {
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
//                + "='0' and " + ValueHolder.IS_SYNC + "='0' and "+ ValueHolder.ORDERID + "='"
//                + RefNo + "'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//
//        Order presale = new Order();
//
//        while (cursor.moveToNext()) {
//
//            OrderDetailController detDS = new OrderDetailController(context);
//
//            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
//            presale.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
//            presale.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
//            presale.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
//            presale.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
//            presale.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
//            presale.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//            // presale.setFORDHED_ADDTIME(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
//            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
//            presale.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
//            presale.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
//            presale.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
//            presale.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
//            presale.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
//            presale.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
//            presale.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
//            presale.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
//            presale.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
//            presale.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
//            presale.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
//            presale.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
//            presale.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
//            presale.setOrdDet(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
//
//        }
//
//        return presale;
//
//    }

    public ArrayList<OrderNew> getAllUnSyncOrdHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderNew> list = new ArrayList<OrderNew>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
                + " <>'1' and " + ValueHolder.IS_SYNC + "='0'";
               // + " = '0' and " + ValueHolder.IS_SYNC + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {

            OrderNew order = new OrderNew();
            OrderDetailController detDS = new OrderDetailController(context);
            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);

         //   order.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID)));
            order.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
         //  order.setFORDHED_APP_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_USER)));
            order.setBptotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBtotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT))));
            order.setBtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS))));
            order.setBtotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX))));
            order.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
//            order.setCurCode(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
//            order.setCurRate(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
            order.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
           // order.setFORDHED_DIS_PER(cursor.getString(cursor.getColumnIndex(ValueHolder.DISPER)));
            order.setStartTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setEndTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
//            order.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE))));
//            order.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE))));
            order.setLocCode(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setManuRef(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setTaxReg(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
            order.setTotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT))));
            order.setTotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS))));
            order.setTotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX))));
            order.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
            order.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
          //  order.setFORDHED_TOTAL_ITM_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_ITM_DIS)));
         //   order.setFORDHED_TOT_MKR_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_MKR_AMT)));
          //  order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
        //    order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
          //  order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
//            order.setRouteCode(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
            order.setAppVersion(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
           // order.setFORDHED_HED_DIS_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
          //  order.setFORDHED_HED_DIS_PER_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
          //  order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            order.setOrderDetDetails(detDS.getAllUnSyncData(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            order.setFreeIssueDetails(freeIssDS.getAllFreeIssues(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));

            list.add(order);

        }

        return list;
    }

    public ArrayList<OrderNew> getAllUnSyncOrdHedByOrderId(String OrderId) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderNew> list = new ArrayList<OrderNew>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
                + " <>'1' and " + ValueHolder.IS_SYNC + "='0' AND " + ValueHolder.ORDERID + " = '" + OrderId + "'";
        // + " = '0' and " + ValueHolder.IS_SYNC + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {

            OrderNew order = new OrderNew();
            OrderDetailController detDS = new OrderDetailController(context);
            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);

            //   order.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID)));
            order.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            //  order.setFORDHED_APP_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_USER)));
            order.setBptotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBtotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT))));
            order.setBtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS))));
            order.setBtotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX))));
            order.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
//            order.setCurCode(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
//            order.setCurRate(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
            order.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            // order.setFORDHED_DIS_PER(cursor.getString(cursor.getColumnIndex(ValueHolder.DISPER)));
            order.setStartTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setEndTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
//            order.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE))));
//            order.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE))));
            order.setLocCode(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setManuRef(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setTaxReg(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
            order.setTotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT))));
            order.setTotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS))));
            order.setTotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX))));
            order.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
            order.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            //  order.setFORDHED_TOTAL_ITM_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_ITM_DIS)));
            //   order.setFORDHED_TOT_MKR_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_MKR_AMT)));
            //  order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
            //    order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            //  order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
//            order.setRouteCode(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
            order.setAppVersion(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
            // order.setFORDHED_HED_DIS_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
            //  order.setFORDHED_HED_DIS_PER_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
            //  order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            order.setOrderDetDetails(detDS.getAllUnSyncData(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            order.setFreeIssueDetails(freeIssDS.getAllFreeIssues(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));

            list.add(order);

        }

        return list;
    }

//    public ArrayList<Order> getAllUnSyncOrdHed() {
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<Order> list = new ArrayList<Order>();
//
//        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
//                + "<>'1' and " + ValueHolder.IS_SYNC + "='0'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
//
//            Order order = new Order();
//            OrderDetailController detDS = new OrderDetailController(context);
//            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);
////
////            CompanyBranchDS branchDS = new CompanyBranchDS(context);
////            preSalesMapper
////                    .setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//            order.setDistDB(new SharedPref(context).getDistDB());
//            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
//            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
//            order.setFORDHED_ORDERID(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID)));
//            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
//            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
//            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
//            order.setFORDHED_APP_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_DATE)));
//            order.setFORDHED_APPSTS(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS)));
//            order.setFORDHED_APP_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_USER)));
//            order.setFORDHED_BP_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT)));
//            order.setFORDHED_B_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX)));
//            order.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
//            order.setFORDHED_CUR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
//            order.setFORDHED_CUR_RATE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
//            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//            order.setFORDHED_DIS_PER(cursor.getString(cursor.getColumnIndex(ValueHolder.DISPER)));
//            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
//            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
//            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
//            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
//            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
//            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
//            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
//            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
//            order.setFORDHED_TAX_REG(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
//            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
//            order.setFORDHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS)));
//            order.setFORDHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX)));
//            order.setFORDHED_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
//            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
//            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
//            order.setFORDHED_TOTAL_ITM_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_ITM_DIS)));
//            order.setFORDHED_TOT_MKR_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_MKR_AMT)));
//            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
//            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
//            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
//            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
//            order.setFORDHED_HED_DIS_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_HED_DIS_PER_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
//            order.setOrdDet(detDS.getAllUnSync(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
//            order.setOrdFreeIss(freeIssDS.getAllFreeIssues(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
//
//            list.add(order);
//
//        }
//
//        return list;
//    }
    public String getRefnoByDebcode(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID + "='"
                + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE));

        }
        return "";

    }

    public boolean isAnyConfirmOrderHed(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean res = false;

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_ACTIVE + "='2'" + " AND " + ValueHolder.ORDERID + " = '" + RefNo + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                res = true;
            else
                res = false;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return res;

    }

    //-------------------kaveesha --------- 22/02/2021----------------------------------------
    public boolean isAnySyncOrderHed(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean res = false;

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_SYNC + "='1'" + " AND " + ValueHolder.ORDERID + " = '" + RefNo + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                res = true;
            else
                res = false;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return res;

    }

    public boolean isAnyActiveOrders() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean res = false;

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_ACTIVE + "='1' ";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                res = true;
            else
                res = false;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return res;

    }


    public String getActiveRefNoFromOrders()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String refNo = "";

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    refNo = cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID));
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

        return refNo;
    }

    //-----------------------------kaveeasha ---2/10/2020-----To get cost code-------------------------------------------------------
    public String getCostCodeByLocCode(String LocCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT CostCode FROM " + ValueHolder.TABLE_LOCATIONS + " WHERE " + ValueHolder.LOCCODE + "='"
                + LocCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }


    //-------------------kaveesha --------------------18/11/2020-------------------------------------------------
    public int CreateOrUpdateOrderStatus(ArrayList<Order> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Order order : list) {

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.ORDERID
                        + " = '" + order.getOrderId() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.STATUS, order.getFORDHED_STATUS());

                count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.ORDERID + " =?",
                        new String[] { String.valueOf(order.getOrderId()) });
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

    public ArrayList<Order> getAllOrdersBySearch(String key) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " where "+ ValueHolder.IS_ACTIVE + " <> '1' and " + ValueHolder.TXNDATE + " LIKE '%" + key + "%' ORDER BY Id DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            Order order = new Order();

            order.setDistDB(SharedPref.getInstance(context).getDistDB().trim());

            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            order.setFORDHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));


            list.add(order);

        }

        return list;
    }

    //-------------------------kaveesha ------ 21-03-2021 --------------------------------------------------------
    public ArrayList<String> getDeliveryDates(String debCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<String> list = new ArrayList<String>();

        String selectQuery =  "SELECT DelvDate FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.DEBCODE + "='"
                + debCode + "'";

        cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                String date = "";
                date = cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE));

                list.add(date);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }

}
