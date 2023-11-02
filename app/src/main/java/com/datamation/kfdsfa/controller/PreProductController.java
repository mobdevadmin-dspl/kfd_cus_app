package com.datamation.kfdsfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.PreProduct;

import java.util.ArrayList;

public class PreProductController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    public PreProductController(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public boolean tableHasRecords() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE, null);
            Log.d("1017 - Table has ",">>>>>>"+cursor.getCount());
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


    public ArrayList<PreProduct> getAllItems(String newText) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
             cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE itemcode || itemname LIKE '%" + newText + "%'  group by itemcode order by CAST(qoh AS FLOAT) desc", null);


            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRICE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
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
    public void updateProductQty(String itemCode, String qty, String type) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(ValueHolder.QTY, qty);
            dB.update(ValueHolder.TABLE_PRODUCT_PRE, values,  ValueHolder.ITEMCODE
                    + " = '" + itemCode + "'  ", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }
    public int updateQuantities(String itemCode,String itemname, String price, String qty, String refno) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE " + ValueHolder.ITEMCODE
                    + " = '" + itemCode + "' ";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();
            values.put(ValueHolder.ITEMCODE, itemCode);
            values.put(ValueHolder.ITEMNAME, itemname);
            values.put(ValueHolder.PRICE, price);
           // values.put(ValueHolder.QOH, qoh);
            values.put(ValueHolder.QTY, qty);


            int cn = cursor.getCount();
            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_PRODUCT_PRE, values, ValueHolder.ITEMCODE
                        + " = '" + itemCode + "'",
                        null);
            } else {
                count = (int) dB.insert(ValueHolder.TABLE_PRODUCT_PRE, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return count;
    }
   public ArrayList<PreProduct> getSelectedItems() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE   qty<>'0'", null);

            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRICE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));

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
    public void mClearTables() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(ValueHolder.TABLE_PRODUCT_PRE, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }

    public void insertIntoProductAsBulkForPre(String LocCode)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try
        {

                String insertQuery2;
                insertQuery2 = "INSERT INTO TblProductsPre (itemcode,itemname,price,qoh,qty) " +
                        "SELECT  itm.ItemCode AS ItemCode , itm.ItemName AS ItemName ,  \n" +
                        "                        IFNULL(pri.Price,0.0) AS Price , \n" +

                        "                        loc.QOH AS QOH , '0' AS Qty " +
                        " FROM TblItem itm\n" +
                        "                        INNER JOIN TblItemLoc loc ON loc.ItemCode = itm.ItemCode \n" +
                        "                        LEFT JOIN TblItemPri pri ON pri.ItemCode = itm.ItemCode  \n" +
                        "                        WHERE loc.LocCode = '"+LocCode+"' AND pri.ItemCode = itm.ItemCode \n" +
                        "                        Group by  itm.ItemCode ORDER BY CAST(QOH AS FLOAT) DESC ";

                dB.execSQL(insertQuery2);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(dB.isOpen())
            {
                dB.close();
            }
        }
    }
}
