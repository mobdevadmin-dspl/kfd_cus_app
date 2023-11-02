package com.datamation.kfdsfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.RepDetail;
import com.datamation.kfdsfa.model.Type;

import java.util.ArrayList;

/*
 create by kaveesha - 16-12-2020
 */

public class RepDetailsController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RepDetailsController";

    public RepDetailsController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public void CreateOrUpdateRepDetails(ArrayList<RepDetail> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_REPDETAILS +
                    " (RepCode,RepName) " + " VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (RepDetail repDetail : list) {

                stmt.bindString(1, repDetail.getFREP_CODE());
                stmt.bindString(2, repDetail.getFREP_NAME());

                stmt.execute();
                stmt.clearBindings();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dB.setTransactionSuccessful();
            dB.endTransaction();
            dB.close();
        }
    }

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_REPDETAILS, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_REPDETAILS, null, null);
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

    public ArrayList<String> getAllSalesRep() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<String> list = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REPDETAILS;

        cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                String rep = cursor.getString(1) + "-" + cursor.getString(2);
                list.add(rep);
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
