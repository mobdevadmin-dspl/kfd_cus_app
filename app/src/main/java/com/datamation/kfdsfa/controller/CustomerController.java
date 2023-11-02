package com.datamation.kfdsfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdsfa.helpers.ValueHolder;
import com.datamation.kfdsfa.model.Customer;
import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.model.Debtor;
import com.datamation.kfdsfa.model.FddbNote;
import com.datamation.kfdsfa.model.DebtorNew;

import java.util.ArrayList;


public class CustomerController {

    private SQLiteDatabase dB;
    private DatabaseHelper DbHelper;
    Context context;
    private String TAG = "CustomerController";

    public CustomerController(Context context) {
        this.context = context;
        DbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = DbHelper.getWritableDatabase();
    }

    public void InsertOrReplaceDebtor(ArrayList<Debtor> list) {
        Log.d("InsertOrReplaceDebtor", "" + list.size());
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_DEBTOR +
                    " (AreaCode," +
                    " ChkCrdLmt," +
                    " ChkCrdPrd," +
                    " CrdLimit," +
                    " CrdPeriod," +
                    " CusId," +
                    " CusPassword," +
                    " DebTele," +
                    " DebMob," +
                    " DebEMail," +
                    " DbGrCode," +
                    " DebAdd1," +
                    " DebAdd2," +
                    " DebAdd3," +
                    " RepCode," +
                    " PrilCode," +
                    " TaxReg," +
                    " RankCode," +
                    " TownCode," +
                    " DebCode," +
                    " RouteCode," +
                    " Status," +
                    " DebName," +
                    " LocCode) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FDEBTOR + " (DebCode,DebName,DebAdd1,DebAdd2,DebAdd3,DebTele,DebMob,DebEMail,TownCode,AreaCode,DbGrCode,Status,CrdPeriod,ChkCrdPrd,CrdLimit,ChkCrdLmt,RepCode,PrillCode,TaxReg,RankCode,Latitude,Longitude) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Debtor debtor : list) {
                stmt.bindString(1, debtor.getFDEBTOR_AREA_CODE().trim());
                stmt.bindString(2, debtor.getFDEBTOR_CHK_CRD_LIMIT().trim());
                stmt.bindString(3, debtor.getFDEBTOR_CHK_CRD_PERIOD().trim());
                stmt.bindString(4, debtor.getFDEBTOR_CRD_LIMIT().trim());
                stmt.bindString(5, debtor.getFDEBTOR_CRD_PERIOD().trim());
                stmt.bindString(6, debtor.getFDEBTOR_CUSID().trim());
                stmt.bindString(7, debtor.getFDEBTOR_PASSWORD().trim());
                stmt.bindString(8, debtor.getFDEBTOR_TELE().trim());
                stmt.bindString(9, debtor.getFDEBTOR_MOB().trim());
                stmt.bindString(10, debtor.getFDEBTOR_EMAIL().trim());
                stmt.bindString(11, debtor.getFDEBTOR_DBGR_CODE().trim());
                stmt.bindString(12, debtor.getFDEBTOR_ADD1().trim());
                stmt.bindString(13, debtor.getFDEBTOR_ADD2().trim());
                stmt.bindString(14, debtor.getFDEBTOR_ADD3().trim());
                stmt.bindString(15, debtor.getFDEBTOR_REPCODE().trim());
                stmt.bindString(16, debtor.getFDEBTOR_PRILLCODE().trim());
                stmt.bindString(17, debtor.getFDEBTOR_TAX_REG().trim());
                stmt.bindString(18, debtor.getFDEBTOR_RANK_CODE().trim());
                stmt.bindString(19, debtor.getFDEBTOR_TOWNCODE().trim());
                stmt.bindString(20, debtor.getFDEBTOR_CODE().trim());
                stmt.bindString(21, debtor.getFDEBTOR_ROUTE_CODE().trim());
                stmt.bindString(22, debtor.getFDEBTOR_STATUS().trim());
                stmt.bindString(23, debtor.getFDEBTOR_NAME().trim());
                stmt.bindString(24, debtor.getFDEBTOR_MAINLOC().trim());

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



    public String getTaxRegStatus(String code) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_DEBTOR  + " WHERE " + ValueHolder.DEBCODE + "='"
                    + code + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG));

            }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
                dB.close();
            }
        }
        return "";
    }
    public String getCusNameByCode(String debcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + ValueHolder.FDEBTOR_NAME + " FROM " + ValueHolder.TABLE_DEBTOR + " where " + ValueHolder.DEBCODE + " = '" + debcode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception get Name", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }

    public ArrayList<DebtorNew> uploadCustomerPassword(String debcode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DebtorNew> list = new ArrayList<DebtorNew>();
        Cursor cursor = null;
        try {
            String selectQuery = "select * from " + ValueHolder.TABLE_DEBTOR + " where DebCode = '"+ debcode + "' and "+ValueHolder.IS_SYNC+ " = '2'";

            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {

                DebtorNew customer = new DebtorNew();

                customer.setUserid(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ID)));
                customer.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                customer.setPassword(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_PASSWORD)));

                list.add(customer);

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
    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_DEBTOR, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_DEBTOR, null, null);
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

    public ArrayList<FddbNote> getOutStandingList(String debCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        Cursor cursor = null;
        try {
            String selectQuery = "select outs.*, deb.crdperiod from TblFDDbNote outs , TblDebtor deb where deb.debcode = outs.debcode and outs.debcode = '"+ debCode + "' ORDER BY date(Txndate) ASC";
           // String selectQuery = "select outs.*, deb.crdperiod from TblFDDbNote outs , TblDebtor deb where deb.debcode = outs.debcode and outs.debcode = '"+ debCode + "' ORDER BY date(Txndate) ASC";


            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {

                FddbNote fddbNote = new FddbNote();
//
                fddbNote.setFDDBNOTE_REPNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REPNAME)));
                fddbNote.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                fddbNote.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                fddbNote.setAmt(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                fddbNote.setCreditPeriod(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_PERIOD)));
                fddbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));

                list.add(fddbNote);

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


    public Customer getSelectedCustomerByCode(String code) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        try {
            String selectQuery = "select * from " + ValueHolder.TABLE_DEBTOR + " Where " + ValueHolder.DEBCODE + "='"
                    + code + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                Customer customer = new Customer();

                customer.setCusName(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME)));
                customer.setCusCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD1)));
                customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD2)));
                customer.setCusMob(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_MOB)));
                customer.setCusStatus(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));

                return customer;

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return null;

    }

    public int updateIsSynced(String debCode, String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            ContentValues values = new ContentValues();
            values.put(ValueHolder.IS_SYNC, res);

            if (res.equalsIgnoreCase("1")) {
                count = dB.update(ValueHolder.TABLE_DEBTOR, values, ValueHolder.DEBCODE + " =?", new String[]{String.valueOf(debCode)});
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            dB.close();
        }
        return count;
    }

    public String getCurrentLocCode() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + ValueHolder.LOCCODE + " FROM " + ValueHolder.TABLE_DEBTOR;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }
    public String getCurrentRepCode() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + ValueHolder.REPCODE + " FROM " + ValueHolder.TABLE_DEBTOR;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }

    public int updateNewPassword(String debCode, String newpw) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_DEBTOR + " WHERE " + ValueHolder.DEBCODE + " = '" + debCode + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.FDEBTOR_PASSWORD, newpw);
            values.put(ValueHolder.IS_SYNC, "2");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_DEBTOR, values, ValueHolder.DEBCODE + " =?", new String[] { String.valueOf(debCode)});
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

//
//    public int updateNewPassword(String debCode, String newpw) {
//
//        int count = 0;
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(ValueHolder.FDEBTOR_PASSWORD, newpw);
//            values.put(ValueHolder.IS_SYNC, "2");
//                count = dB.update(ValueHolder.TABLE_DEBTOR, values, ValueHolder.DEBCODE + " =?", new String[]{String.valueOf(debCode)});
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        } finally {
//
//            dB.close();
//        }
//        return count;
//    }

    public int updateforCus(Customer cus) {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            ContentValues values = new ContentValues();
            values.put(ValueHolder.FDEBTOR_NAME, cus.getCusName());
            values.put(ValueHolder.FDEBTOR_ADD1, cus.getCusAdd1());
            values.put(ValueHolder.FDEBTOR_ADD2, cus.getCusAdd2());
            values.put(ValueHolder.FDEBTOR_TELE, cus.getCusMob());
            values.put(ValueHolder.IS_SYNC, "4");

            if (!cus.equals(null)) {
                count = dB.update(ValueHolder.TABLE_DEBTOR, values, ValueHolder.DEBCODE + " =?", new String[]{String.valueOf(cus.getCusCode())});
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return count;
    }

}
