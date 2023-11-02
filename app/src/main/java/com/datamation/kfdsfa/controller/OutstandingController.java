package com.datamation.kfdsfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.TableRow;

import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.Control;
import com.datamation.kfdsfa.model.FddbNote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OutstandingController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "OutstandingController ";

    public OutstandingController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFDDbNote(ArrayList<FddbNote> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FDDBNOTE +
                    " (RefNo," +
                    " RepName," +
                    " Remarks," +
                    " EntRemark," +
                    " PdaAmt," +
                    " RefInv," +
                    " Amt," +
                    " SaleRefNo," +
                    " ManuRef," +
                    " TxnType," +
                    " TxnDate," +
                    " CurCode," +
                    " CurRate," +
                    " DebCode," +
                    " RepCode," +
                    " TaxComCode," +
                    " TaxAmt," +
                    " BTaxAmt," +
                    " BAmt," +
                    " TotBal," +
                    " TotBal1," +
                    " OvPayAmt," +
                    " RefNo1) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (FddbNote fddbNote : list) {
                stmt.bindString(1, fddbNote.getFDDBNOTE_REFNO());
                stmt.bindString(2, fddbNote.getFDDBNOTE_REPNAME());
                stmt.bindString(3, fddbNote.getFDDBNOTE_REMARKS());
                stmt.bindString(4, fddbNote.getFDDBNOTE_ENT_REMARK());
                stmt.bindString(5, fddbNote.getFDDBNOTE_PDA_AMT());
                stmt.bindString(6, fddbNote.getFDDBNOTE_REF_INV());
                stmt.bindString(7, fddbNote.getFDDBNOTE_AMT());
                stmt.bindString(8, fddbNote.getFDDBNOTE_SALE_REF_NO());
                stmt.bindString(9, fddbNote.getFDDBNOTE_MANU_REF());
                stmt.bindString(10,fddbNote.getFDDBNOTE_TXN_TYPE());
                stmt.bindString(11,fddbNote.getFDDBNOTE_TXN_DATE());
                stmt.bindString(12,fddbNote.getFDDBNOTE_CUR_CODE());
                stmt.bindString(13,fddbNote.getFDDBNOTE_CUR_RATE());
                stmt.bindString(14,fddbNote.getFDDBNOTE_DEB_CODE());
                stmt.bindString(15,fddbNote.getFDDBNOTE_REP_CODE());
                stmt.bindString(16,fddbNote.getFDDBNOTE_TAX_COM_CODE());
                stmt.bindString(17,fddbNote.getFDDBNOTE_TAX_AMT());
                stmt.bindString(18,fddbNote.getFDDBNOTE_B_TAX_AMT());
                stmt.bindString(19,fddbNote.getFDDBNOTE_B_AMT());
                stmt.bindString(20,fddbNote.getFDDBNOTE_TOT_BAL());
                stmt.bindString(21,fddbNote.getFDDBNOTE_TOT_BAL1());
                stmt.bindString(22,fddbNote.getFDDBNOTE_OV_PAY_AMT());
                stmt.bindString(23,fddbNote.getFDDBNOTE_REFNO1());

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

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int deleteAll() {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FDDBNOTE, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_FDDBNOTE, null, null);
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


    public ArrayList<FddbNote> getAllRecords(String debcode, boolean isSummery) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        try {

            String selectQuery;

            if (isSummery)
                selectQuery = "select * from " + ValueHolder.TABLE_FDDBNOTE + " WHERE " + " debcode='" + debcode + "' AND EnterAmt<>'' AND CAST(TotBal AS INT) > 0.0 Order By " + ValueHolder.TXNDATE;
            else
                selectQuery = "select * from " + ValueHolder.TABLE_FDDBNOTE + " WHERE " + " debcode='" + debcode + "' AND CAST(TotBal AS INT) > 0.0 Order By " + ValueHolder.TXNDATE;

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                FddbNote fdDbNote = new FddbNote();

                fdDbNote.setFDDBNOTE_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                fdDbNote.setFDDBNOTE_B_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.B_AMT)));
                fdDbNote.setFDDBNOTE_B_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.BT_TAX_AMT)));
                fdDbNote.setFDDBNOTE_CUR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
                fdDbNote.setFDDBNOTE_CUR_RATE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
                fdDbNote.setFDDBNOTE_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                fdDbNote.setFDDBNOTE_ENTER_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.ENTER_AMT)));
                fdDbNote.setFDDBNOTE_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                fdDbNote.setFDDBNOTE_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
                fdDbNote.setFDDBNOTE_OV_PAY_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.OV_PAY_AMT)));
                fdDbNote.setFDDBNOTE_REF_INV(cursor.getString(cursor.getColumnIndex(ValueHolder.REF_INV)));
                fdDbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                fdDbNote.setFDDBNOTE_REFNO1(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO1)));
                fdDbNote.setFDDBNOTE_REP_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                fdDbNote.setFDDBNOTE_SALE_REF_NO(cursor.getString(cursor.getColumnIndex(ValueHolder.SALE_REF_NO)));
                fdDbNote.setFDDBNOTE_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXAMT)));
                fdDbNote.setFDDBNOTE_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                fdDbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));
                fdDbNote.setFDDBNOTE_TOT_BAL1(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL1)));
                fdDbNote.setFDDBNOTE_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                fdDbNote.setFDDBNOTE_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
                fdDbNote.setFDDBNOTE_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                fdDbNote.setFDDBNOTE_REPNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REPNAME)));
                // fdDbNote.setFDDBNOTE_ENTREMARK(cursor.getString(cursor.getColumnIndex(FDDBNOTE_ENT_REMARK)));

                list.add(fdDbNote);

            }
            cursor.close();
        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    public double getDebtorBalance(String DebCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totbal = 0, totbal1 = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT TotBal,TotBal1 FROM " + ValueHolder.TABLE_FDDBNOTE + " WHERE DebCode ='" + DebCode + "'";
            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                totbal = totbal + Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));
                totbal1 = totbal1 + Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL1)));
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return totbal - totbal1;

    }

    public ArrayList<FddbNote> getDebtInfo(String DebCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        try {
            String selectQuery;

            if (DebCode.equals(""))
                selectQuery = "SELECT refno,totbal,totbal1,txndate FROM " + ValueHolder.TABLE_FDDBNOTE;
            else
                selectQuery = "SELECT refno,totbal,totbal1,txndate FROM " + ValueHolder.TABLE_FDDBNOTE + " WHERE DebCode ='" + DebCode + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                FddbNote dbNote = new FddbNote();
                dbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                dbNote.setFDDBNOTE_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                dbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));
                dbNote.setFDDBNOTE_TOT_BAL1(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL1)));
                list.add(dbNote);
            }

            cursor.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return list;

    }


}

