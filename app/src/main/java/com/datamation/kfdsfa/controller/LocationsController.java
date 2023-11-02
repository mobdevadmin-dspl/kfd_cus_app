package com.datamation.kfdsfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.FreeDeb;
import com.datamation.kfdsfa.model.Locations;

import java.util.ArrayList;

/*
  create by kaveesha - 05-10-2020
 */

public class LocationsController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "LocationsController";

    public LocationsController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFLocations(ArrayList<Locations> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_LOCATIONS +
                    " (LocCode," +
                    "LocName," +
                    "LoctCode," +
                 //   "RepCode," +
                    "CostCode) " + " VALUES (?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (Locations locations : list) {

                stmt.bindString(1, locations.getFLOCATIONS_LOC_CODE());
                stmt.bindString(2, locations.getFLOCATIONS_LOC_NAME());
                stmt.bindString(3, locations.getFLOCATIONS_LOC_T_CODE());
               // stmt.bindString(4, locations.getFLOCATIONS_REP_CODE());
                stmt.bindString(4, locations.getFLOCATIONS_COST_CODE());

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

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_LOCATIONS, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_LOCATIONS, null, null);
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

}
