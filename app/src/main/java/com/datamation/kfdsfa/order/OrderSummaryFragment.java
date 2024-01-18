package com.datamation.kfdsfa.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.adapter.OrderDetailsAdapter;
import com.datamation.kfdsfa.adapter.OrderFreeIssueDetailAdapter;
import com.datamation.kfdsfa.adapter.OrderFreeItemAdapter;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.controller.ItemLocController;
import com.datamation.kfdsfa.controller.OrderController;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.controller.OrderDiscController;
import com.datamation.kfdsfa.controller.PreProductController;
import com.datamation.kfdsfa.controller.PreSaleTaxDTController;
import com.datamation.kfdsfa.controller.PreSaleTaxRGController;
import com.datamation.kfdsfa.fragment.FragmentHome;
import com.datamation.kfdsfa.helpers.OrderResponseListener;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.Customer;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.model.OrderDisc;
import com.datamation.kfdsfa.model.OrderNew;
import com.datamation.kfdsfa.model.apimodel.Result;
import com.datamation.kfdsfa.utils.NetworkUtil;
import com.datamation.kfdsfa.utils.UtilityContainer;
import com.datamation.kfdsfa.view.ActivityHome;
import com.datamation.kfdsfa.view.OrderActivity;
import com.datamation.kfdsfa.view.dashboard.OrderFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderSummaryFragment extends Fragment {

    public static final String SETTINGS = "PreSalesSummary";
    View view;
    TextView lblGross,  lblNetVal, lblReplacements, lblQty,lblSummaryHeader;
    SharedPref mSharedPref;
    String RefNo = "", customerName = "";
    ArrayList<OrderDetail> list;
    ArrayList<OrderDisc> discList;
    String locCode;
    ListView lv_order_det, lvFree;
    ArrayList<OrderDetail> orderList;
    FloatingActionButton fabEdit, fabDiscard, fabSave , fabSave_Upload;
    FloatingActionMenu fam;
    MyReceiver r;
    int iTotFreeQty = 0;
    double totalMKReturn = 0;
    OrderActivity mainActivity;
    private Customer outlet;
    OrderResponseListener responseListener;
    List<String> resultListPreSale;
    Activity mactivity;
    private double currentLatitude, currentLongitude;
    private Customer customer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_order_summary_layout, container, false);

        mSharedPref = new SharedPref(getActivity());
        mainActivity = (OrderActivity) getActivity();

        fabEdit = (FloatingActionButton) view.findViewById(R.id.fab2);
        fabDiscard = (FloatingActionButton) view.findViewById(R.id.fab3);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fab1);
        fabSave_Upload = (FloatingActionButton) view.findViewById(R.id.fab4);
        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        lv_order_det = (ListView) view.findViewById(R.id.lvProducts_pre);
        lvFree = (ListView) view.findViewById(R.id.lvFreeIssue_Inv);

        lblNetVal = (TextView) view.findViewById(R.id.lblNetVal_Inv);
        lblSummaryHeader = (TextView) view.findViewById(R.id.summary_header);
        lblReplacements = (TextView) view.findViewById(R.id.lblReplacement);
        lblGross = (TextView) view.findViewById(R.id.lblGross_Inv);
        lblQty = (TextView) view.findViewById(R.id.lblQty_Inv);

        resultListPreSale = new ArrayList<>();
        mactivity = getActivity();
        customerName = new CustomerController(getActivity()).getCusNameByCode(SharedPref.getInstance(getActivity()).getSelectedDebCode());
        RefNo = ""+mSharedPref.generateOrderId();
        lblSummaryHeader.setText("ORDER SUMMARY - ("+customerName+")");

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseListener.moveBackToFragment(1);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSummaryDialog();
            }
        });

        fabSave_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkAvailable(getActivity()) && (NetworkUtil.isNotPoorConnection(getActivity())==false)) {
                    Toast.makeText(getActivity(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
                    saveSummaryDialog();
                }else
                {
                    SaveAndUploadDialog();
                }
            }
        });

        fabDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoEditingData();
            }
        });
        lv_order_det.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new OrderDetailController(getActivity()).restFreeIssueData(RefNo);
                newDeleteOrderDialog(position);
                return true;
            }
        });
        return view;
    }

    public void newDeleteOrderDialog(final int dltPosition) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirm Deletion !");
        alertDialogBuilder.setMessage("Do you want to delete this item ?");
        alertDialogBuilder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // new PreProductController(getActivity()).updateProductQty(orderList.get(dltPosition).getFORDERDET_ITEMCODE(), "0",orderList.get(dltPosition).getFORDERDET_TYPE());
                // new PreProductController(getActivity()).updateProductCase(orderList.get(dltPosition).getFORDERDET_ITEMCODE(), "0",orderList.get(dltPosition).getFORDERDET_TYPE());
                new OrderDetailController(getActivity()).mDeleteRecords(RefNo, orderList.get(dltPosition).getFORDDET_ITEM_CODE(),orderList.get(dltPosition).getFORDDET_TYPE());
                Toast.makeText(getActivity(), "Deleted successfully!", Toast.LENGTH_SHORT).show();
                showData();

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }
    public void undoEditingData() {

        Order hed = new OrderController(getActivity()).getAllActiveOrdHed();
        outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(hed.getFORDHED_DEB_CODE());

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Do you want to discard the order?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Log.d(">>>click discard"+mSharedPref.generateOrderId(), ">>>click discard"+mSharedPref.generateOrderId());
                        Log.d(">>>click discard"+mSharedPref.getEditOrderId(), ">>>click discard"+mSharedPref.getEditOrderId());
                        if(mSharedPref.generateOrderId()== mSharedPref.getEditOrderId()){
                            int result = new OrderController(getActivity()).restData(RefNo);
                            if (result > 0) {
                                new OrderDetailController(getActivity()).restData(RefNo);
                                mSharedPref.setDiscountClicked("0");
                                mSharedPref.setHeaderNextClicked("0");
                                mSharedPref.setOrderId(0);
                                mSharedPref.setEditOrderId(0);
                                mSharedPref.setGlobalVal("placeAnOrder", "");
                                new PreProductController(getActivity()).mClearTables();

                            }

                        }else{
                            new OrderDetailController(getActivity()).UpdateEditedQty(RefNo,"-");
                            new OrderDetailController(getActivity()).restZeroQtyData(RefNo);
                            Order ordHed = new Order();
                            ArrayList<Order> ordHedList = new ArrayList<Order>();
                            list = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
//        discList = new OrderDiscController(getActivity()).getAllOrderDiscs(RefNo);
                            double total = 0;
//
                            for (OrderDetail ordDet : list) {

                                total += Double.parseDouble(ordDet.getFORDDET_AMT());


                            }

                            Order presale = new OrderController(getActivity()).getAllActiveOrdHed();
                            ordHed.setFORDHED_REFNO("");
                            if(mSharedPref.generateOrderId() == 0){
                                long time = System.currentTimeMillis();
                                ordHed.setOrderId(time);
                            }else{
                                ordHed.setOrderId(mSharedPref.generateOrderId());
                            }
                            ordHed.setFORDHED_DEB_CODE(presale.getFORDHED_DEB_CODE());
                            ordHed.setFORDHED_ADD_DATE(presale.getFORDHED_ADD_DATE());
                            ordHed.setFORDHED_DELV_DATE(presale.getFORDHED_DELV_DATE());
                            ordHed.setFORDHED_ROUTE_CODE(presale.getFORDHED_ROUTE_CODE());
                            ordHed.setFORDHED_MANU_REF(presale.getFORDHED_MANU_REF());
                            ordHed.setFORDHED_REMARKS(presale.getFORDHED_REMARKS());
                            ordHed.setFORDHED_ADD_MACH(presale.getFORDHED_ADD_MACH());
                            ordHed.setFORDHED_ADD_USER(presale.getFORDHED_ADD_USER());
                            ordHed.setFORDHED_APP_DATE(presale.getFORDHED_APP_DATE());
                            ordHed.setFORDHED_APPSTS(presale.getFORDHED_APPSTS());
                            ordHed.setFORDHED_APP_USER(presale.getFORDHED_APP_USER());
                            ordHed.setFORDHED_CUR_CODE(presale.getFORDHED_CUR_CODE());
                            ordHed.setFORDHED_CUR_RATE(presale.getFORDHED_CUR_RATE());
                            ordHed.setFORDHED_LOC_CODE(locCode);
                            ordHed.setFORDHED_TAX_REG(new CustomerController(getActivity()).getTaxRegStatus(presale.getFORDHED_DEB_CODE()));
//                   ordHed.setFORDHED_BP_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+""); // String.format("%.2f",sHeaderDisPer)+""
                            ordHed.setFORDHED_B_TOTAL_AMT(lblGross.getText().toString());
//                    ordHed.setFORDHED_B_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                            ordHed.setFORDHED_BP_TOTAL_DIS("0.00");
                            ordHed.setFORDHED_B_TOTAL_DIS("0.00");
                            ordHed.setFORDHED_B_TOTAL_TAX("0.00");
                            ordHed.setFORDHED_DIS_PER("0.00");
                            //ordHed.setFORDHED_DIS_PER(String.format("%.2f",presale.getFORDHED_DIS_PER())+"");
                            ordHed.setFORDHED_END_TIME_SO(currentTime());
                            ordHed.setFORDHED_START_TIME_SO(currentTime());
                            ordHed.setFORDHED_ADDRESS("");
                            ordHed.setFORDHED_COST_CODE(presale.getFORDHED_COST_CODE());
                            ordHed.setFORDHED_TOTAL_TAX("0.00");
                            ordHed.setFORDHED_TOTALDIS("0.00");
                            ordHed.setFORDHED_TOTAL_ITM_DIS("0.00");
                            ordHed.setFORDHED_TOT_MKR_AMT("0.00");
                            // ordHed.setFORDHED_TOTALDIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
//                    ordHed.setFORDHED_TOTAL_ITM_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
//                    ordHed.setFORDHED_TOT_MKR_AMT(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                            ordHed.setFORDHED_TOTAL_AMT(""+(String.format("%.2f",total)));
                            ordHed.setFORDHED_TXN_DATE(currentDate());
                            ordHed.setFORDHED_TXN_TYPE("21");
                            ordHed.setFORDHED_REPCODE(new CustomerController(getActivity()).getCurrentRepCode());
                            ordHed.setFORDHED_IS_ACTIVE("3");//not delete
                            ordHed.setFORDHED_IS_SYNCED("0");
                            ordHed.setFORDHED_HED_DIS_VAL(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                            ordHed.setFORDHED_HED_DIS_PER_VAL(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");//String.format("%,.2f",sHeaderDisPer)
                            ordHed.setFORDHED_PAYMENT_TYPE(presale.getFORDHED_PAYMENT_TYPE());
                            hed.setFORDHED_STATUS("NOT SYNCED");
                            ordHedList.add(ordHed);
                            if (new OrderController(getActivity()).createOrUpdateOrdHed(ordHedList) > 0) {
                                /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/

                                mSharedPref.setDiscountClicked("0");
                                mSharedPref.setHeaderNextClicked("0");
                                mSharedPref.setOrderId(0);
                                mSharedPref.setEditOrderId(0);
                                mSharedPref.setGlobalVal("placeAnOrder", "");
                                mSharedPref.setGlobalVal("KeySelectedRep", "");
                                UpdateTaxDetails(RefNo);
                                new ItemLocController(getActivity()).UpdateOrderQOH(RefNo, "-", locCode);
                                UtilityContainer.ClearReturnSharedPref(getActivity());
                                outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(mSharedPref.getSelectedDebCode());
                                new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
                                new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);
                                //  Upload(new OrderController(getActivity()).getAllUnSyncOrdHed());
                            } else {
                                Toast.makeText(getActivity(), "Order Save Failed..", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Toast.makeText(getActivity(), "Order discarded successfully..!", Toast.LENGTH_SHORT).show();
                        Intent intnt = new Intent(getActivity(), ActivityHome.class);
                        intnt.putExtra("outlet", outlet);
                        startActivity(intnt);
                        getActivity().finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                })
                .build();
        materialDialog.setCanceledOnTouchOutside(false);
        materialDialog.show();
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Save primary & secondary invoice-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*/


    public void mRefreshData() {
        showData();
        if (mSharedPref.getDiscountClicked().equals("0")) {
            responseListener.moveBackToFragment(1);
            Toast.makeText(getActivity(), "Please tap on Arrow Button", Toast.LENGTH_LONG).show();
        }

        customerName = new CustomerController(getActivity()).getCusNameByCode(SharedPref.getInstance(getActivity()).getSelectedDebCode());

        lblSummaryHeader.setText("ORDER SUMMARY - ("+customerName+")");

        // String orRefNo = new OrderController(getActivity()).getActiveRefNoFromOrders();

        int ftotQty = 0, fTotFree = 0, returnQty = 0, replacements = 0;
        double ftotAmt = 0, fTotLineDisc = 0, fTotSchDisc = 0, totalReturn = 0;
        String itemCode = "";

        locCode = new CustomerController(getActivity()).getCurrentLocCode();
        list = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
//        discList = new OrderDiscController(getActivity()).getAllOrderDiscs(RefNo);
//
        for (OrderDetail ordDet : list) {

            ftotAmt += Double.parseDouble(ordDet.getFORDDET_AMT());

            if(ordDet.getFORDDET_TYPE().equals("FI"))
            {
                fTotFree += Integer.parseInt(ordDet.getFORDDET_QTY());
            }
            else {
                ftotQty += Integer.parseInt(ordDet.getFORDDET_QTY());
            }

        }


        Double gross = 0.0;
        Double net = 0.0;

        gross = ftotAmt + fTotSchDisc;
        net = gross + totalReturn - fTotSchDisc;

        iTotFreeQty = fTotFree;
        lblQty.setText(String.valueOf(ftotQty));

        lblGross.setText(String.format("%.2f", gross)); // SA type total amt + discount
        //  lblReturn.setText(String.format("%.2f", totalReturn)); // MR/UR type total amt
        // lblNetVal.setText(String.format("%.2f", net)); // total - discount - return

        //   lblReturnQty.setText(String.valueOf(returnQty));
        lblReplacements.setText(String.valueOf(fTotFree));



    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
    public void showData() {
        try
        {
            lv_order_det.setAdapter(null);
            orderList = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
            lv_order_det.setAdapter(new OrderDetailsAdapter(getActivity(), orderList, mSharedPref.getSelectedDebCode()));//2019-07-07 till error free


        } catch (NullPointerException e) {
            Log.v("SA Error", e.toString());
        }
    }
    public void saveSummaryDialog() {

        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Do you want to save the invoice ?");
            alertDialogBuilder.setView(promptView);

            final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
            ViewGroup.LayoutParams invItmparams = lvProducts_Invoice.getLayoutParams();
            ArrayList<OrderDetail> orderItemList = null;
            orderItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "SA","");
            if(orderItemList.size()>0){
                invItmparams.height = 200;
            }else {
                invItmparams.height = 0;
            }
            lvProducts_Invoice.setLayoutParams(invItmparams);

            lvProducts_Invoice.setAdapter(new OrderDetailsAdapter(getActivity(), orderItemList, mSharedPref.getSelectedDebCode()));

            //MMS - freeissues
            ListView lvProducts_freeIssue = (ListView) promptView.findViewById(R.id.lvProducts_Summary_freeIssue);
            ViewGroup.LayoutParams params = lvProducts_freeIssue.getLayoutParams();
            ArrayList<OrderDetail> orderFreeIssueItemList = null;
            orderFreeIssueItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "FI","FD");
            if(orderFreeIssueItemList.size()>0){
                params.height = 200;
            }else {
                params.height = 0;
            }

            lvProducts_freeIssue.setLayoutParams(params);
            lvProducts_freeIssue.setAdapter(new OrderFreeIssueDetailAdapter(getActivity(), orderFreeIssueItemList, mSharedPref.getSelectedDebCode()));

            alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(final DialogInterface dialog, int id) {

                    Order ordHed = new Order();
                    ArrayList<Order> ordHedList = new ArrayList<Order>();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                    Order presale = new Order();//new OrderController(getActivity()).getAllActiveOrdHed();
                    ordHed.setFORDHED_REFNO("");
                    if(mSharedPref.generateOrderId() == 0){
                        long time = System.currentTimeMillis();
                        ordHed.setOrderId(time);
                        presale = new OrderController(getActivity()).getAllActiveOrdHed();
                    }else{
                        ordHed.setOrderId(mSharedPref.generateOrderId());
                        presale = new OrderController(getActivity()).getAllActiveOrdHedByID(mSharedPref.generateOrderId()+"");
                    }
                    ordHed.setFORDHED_DEB_CODE(presale.getFORDHED_DEB_CODE());
                    ordHed.setFORDHED_ADD_DATE(presale.getFORDHED_ADD_DATE());
                    ordHed.setFORDHED_DELV_DATE(presale.getFORDHED_DELV_DATE());
                    ordHed.setFORDHED_ROUTE_CODE(presale.getFORDHED_ROUTE_CODE());
                    ordHed.setFORDHED_MANU_REF(presale.getFORDHED_MANU_REF());
                    ordHed.setFORDHED_REMARKS(presale.getFORDHED_REMARKS());
                    ordHed.setFORDHED_ADD_MACH(presale.getFORDHED_ADD_MACH());
                    ordHed.setFORDHED_ADD_USER(presale.getFORDHED_ADD_USER());
                    ordHed.setFORDHED_APP_DATE(presale.getFORDHED_APP_DATE());
                    ordHed.setFORDHED_APPSTS(presale.getFORDHED_APPSTS());
                    ordHed.setFORDHED_APP_USER(presale.getFORDHED_APP_USER());
                    ordHed.setFORDHED_CUR_CODE(presale.getFORDHED_CUR_CODE());
                    ordHed.setFORDHED_CUR_RATE(presale.getFORDHED_CUR_RATE());
                    ordHed.setFORDHED_RECORD_ID(presale.getFORDHED_RECORD_ID());
                    ordHed.setFORDHED_LOC_CODE(locCode);
                    ordHed.setFORDHED_TAX_REG(new CustomerController(getActivity()).getTaxRegStatus(presale.getFORDHED_DEB_CODE()));

//                   ordHed.setFORDHED_BP_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+""); // String.format("%.2f",sHeaderDisPer)+""
                    ordHed.setFORDHED_B_TOTAL_AMT(lblGross.getText().toString());
//                    ordHed.setFORDHED_B_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                    ordHed.setFORDHED_BP_TOTAL_DIS("0.00");
                    ordHed.setFORDHED_B_TOTAL_DIS("0.00");
                    ordHed.setFORDHED_B_TOTAL_TAX("0.00");
                    ordHed.setFORDHED_DIS_PER("0.00");
                    //ordHed.setFORDHED_DIS_PER(String.format("%.2f",presale.getFORDHED_DIS_PER())+"");
                    ordHed.setFORDHED_END_TIME_SO(df.format(new Date()));
                    ordHed.setFORDHED_START_TIME_SO(presale.getFORDHED_START_TIME_SO());
                    ordHed.setFORDHED_ADDRESS("");
                    ordHed.setFORDHED_COST_CODE(presale.getFORDHED_COST_CODE());
                    ordHed.setFORDHED_TOTAL_TAX("0.00");
                    ordHed.setFORDHED_TOTALDIS("0.00");
                    ordHed.setFORDHED_TOTAL_ITM_DIS("0.00");
                    ordHed.setFORDHED_TOT_MKR_AMT("0.00");
                    // ordHed.setFORDHED_TOTALDIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
//                    ordHed.setFORDHED_TOTAL_ITM_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
//                    ordHed.setFORDHED_TOT_MKR_AMT(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                    ordHed.setFORDHED_TOTAL_AMT(lblGross.getText().toString());
                    ordHed.setFORDHED_TXN_DATE(currentDate());
                    ordHed.setFORDHED_TXN_TYPE("21");
                    ordHed.setFORDHED_REPCODE(mSharedPref.getGlobalVal("KeySelectedRep"));
                    //ordHed.setFORDHED_REPCODE(new CustomerController(getActivity()).getCurrentRepCode());
                    ordHed.setFORDHED_IS_ACTIVE("0");
                    ordHed.setFORDHED_IS_SYNCED("0");
                    ordHed.setFORDHED_HED_DIS_VAL(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                    ordHed.setFORDHED_HED_DIS_PER_VAL(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");//String.format("%,.2f",sHeaderDisPer)

                    ordHed.setFORDHED_PAYMENT_TYPE(presale.getFORDHED_PAYMENT_TYPE());
                    ordHed.setFORDHED_STATUS("NOT SYNCED");
                    ordHedList.add(ordHed);

                    if (new OrderController(getActivity()).createOrUpdateOrdHed(ordHedList) > 0) {


                        final OrderActivity activity = (OrderActivity) getActivity();
                        /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/
                        UpdateTaxDetails(RefNo);
                        new ItemLocController(getActivity()).UpdateOrderQOH(RefNo, "-", locCode);
                        Toast.makeText(getActivity(), "Order saved successfully..!", Toast.LENGTH_SHORT).show();
                        mSharedPref.setDiscountClicked("0");
                        mSharedPref.setHeaderNextClicked("0");
                        mSharedPref.setOrderId(0);
                        mSharedPref.setEditOrderId(0);
                        mSharedPref.setGlobalVal("placeAnOrder", "");
                        mSharedPref.setGlobalVal("KeySelectedRep", "");
                        UtilityContainer.ClearReturnSharedPref(getActivity());
                        outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(mSharedPref.getSelectedDebCode());
                        new PreProductController(getActivity()).mClearTables();
                        new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
                        new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);
                        Intent intnt = new Intent(getActivity(), ActivityHome.class);
                        startActivity(intnt);
                        getActivity().finish();

                        //  Upload(new OrderController(getActivity()).getAllUnSyncOrdHed());

                    } else {
                        Toast.makeText(getActivity(), "Order Save Failed..", Toast.LENGTH_SHORT).show();
                    }

                }

            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertD = alertDialogBuilder.create();
            alertD.show();


        } else
            Toast.makeText(getActivity(), "Add items before save ...!", Toast.LENGTH_SHORT).show();


    }

    public void SaveAndUploadDialog() {

        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Do you want to save the invoice ?");
            alertDialogBuilder.setView(promptView);

            final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
            ViewGroup.LayoutParams invItmparams = lvProducts_Invoice.getLayoutParams();
            ArrayList<OrderDetail> orderItemList = null;
            orderItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "SA","");
            if(orderItemList.size()>0){
                invItmparams.height = 200;
            }else {
                invItmparams.height = 0;
            }
            lvProducts_Invoice.setLayoutParams(invItmparams);

            lvProducts_Invoice.setAdapter(new OrderDetailsAdapter(getActivity(), orderItemList, mSharedPref.getSelectedDebCode()));

            //MMS - freeissues
            ListView lvProducts_freeIssue = (ListView) promptView.findViewById(R.id.lvProducts_Summary_freeIssue);
            ViewGroup.LayoutParams params = lvProducts_freeIssue.getLayoutParams();
            ArrayList<OrderDetail> orderFreeIssueItemList = null;
            orderFreeIssueItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "FI","FD");
            if(orderFreeIssueItemList.size()>0){
                params.height = 200;
            }else {
                params.height = 0;
            }

            lvProducts_freeIssue.setLayoutParams(params);
            lvProducts_freeIssue.setAdapter(new OrderFreeIssueDetailAdapter(getActivity(), orderFreeIssueItemList, mSharedPref.getSelectedDebCode()));

            alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(final DialogInterface dialog, int id) {

                    Order ordHed = new Order();
                    ArrayList<Order> ordHedList = new ArrayList<Order>();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                    Order presale = new Order();//new OrderController(getActivity()).getAllActiveOrdHed();
                    ordHed.setFORDHED_REFNO("");
                    if(mSharedPref.generateOrderId() == 0){
                        long time = System.currentTimeMillis();
                        ordHed.setOrderId(time);
                        presale = new OrderController(getActivity()).getAllActiveOrdHed();
                    }else{
                        ordHed.setOrderId(mSharedPref.generateOrderId());
                        presale = new OrderController(getActivity()).getAllActiveOrdHedByID(mSharedPref.generateOrderId()+"");
                    }
                    ordHed.setFORDHED_DEB_CODE(presale.getFORDHED_DEB_CODE());
                    ordHed.setFORDHED_ADD_DATE(presale.getFORDHED_ADD_DATE());
                    ordHed.setFORDHED_DELV_DATE(presale.getFORDHED_DELV_DATE());
                    ordHed.setFORDHED_ROUTE_CODE(presale.getFORDHED_ROUTE_CODE());
                    ordHed.setFORDHED_MANU_REF(presale.getFORDHED_MANU_REF());
                    ordHed.setFORDHED_REMARKS(presale.getFORDHED_REMARKS());
                    ordHed.setFORDHED_ADD_MACH(presale.getFORDHED_ADD_MACH());
                    ordHed.setFORDHED_ADD_USER(presale.getFORDHED_ADD_USER());
                    ordHed.setFORDHED_APP_DATE(presale.getFORDHED_APP_DATE());
                    ordHed.setFORDHED_APPSTS(presale.getFORDHED_APPSTS());
                    ordHed.setFORDHED_APP_USER(presale.getFORDHED_APP_USER());
                    ordHed.setFORDHED_CUR_CODE(presale.getFORDHED_CUR_CODE());
                    ordHed.setFORDHED_CUR_RATE(presale.getFORDHED_CUR_RATE());
                    ordHed.setFORDHED_RECORD_ID(presale.getFORDHED_RECORD_ID());
                    ordHed.setFORDHED_LOC_CODE(locCode);
                    ordHed.setFORDHED_TAX_REG(new CustomerController(getActivity()).getTaxRegStatus(presale.getFORDHED_DEB_CODE()));

//                   ordHed.setFORDHED_BP_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+""); // String.format("%.2f",sHeaderDisPer)+""
                    ordHed.setFORDHED_B_TOTAL_AMT(lblGross.getText().toString());
//                    ordHed.setFORDHED_B_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                    ordHed.setFORDHED_BP_TOTAL_DIS("0.00");
                    ordHed.setFORDHED_B_TOTAL_DIS("0.00");
                    ordHed.setFORDHED_B_TOTAL_TAX("0.00");
                    ordHed.setFORDHED_DIS_PER("0.00");
                    //ordHed.setFORDHED_DIS_PER(String.format("%.2f",presale.getFORDHED_DIS_PER())+"");
                    ordHed.setFORDHED_END_TIME_SO(df.format(new Date()));
                    ordHed.setFORDHED_START_TIME_SO(presale.getFORDHED_START_TIME_SO());
                    ordHed.setFORDHED_ADDRESS("");
//                    ordHed.setFORDHED_COST_CODE("12334");
                    ordHed.setFORDHED_COST_CODE(presale.getFORDHED_COST_CODE());
                    ordHed.setFORDHED_TOTAL_TAX("0.00");
                    ordHed.setFORDHED_TOTALDIS("0.00");
                    ordHed.setFORDHED_TOTAL_ITM_DIS("0.00");
                    ordHed.setFORDHED_TOT_MKR_AMT("0.00");
                    // ordHed.setFORDHED_TOTALDIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
//                    ordHed.setFORDHED_TOTAL_ITM_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
//                    ordHed.setFORDHED_TOT_MKR_AMT(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                    ordHed.setFORDHED_TOTAL_AMT(lblGross.getText().toString());
                    ordHed.setFORDHED_TXN_DATE(currentDate());
                    ordHed.setFORDHED_TXN_TYPE("21");
                    ordHed.setFORDHED_REPCODE(mSharedPref.getGlobalVal("KeySelectedRep"));
                   // ordHed.setFORDHED_REPCODE(new CustomerController(getActivity()).getCurrentRepCode());
                    ordHed.setFORDHED_IS_ACTIVE("2");
                    ordHed.setFORDHED_IS_SYNCED("0");
                    ordHed.setFORDHED_HED_DIS_VAL(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
                    ordHed.setFORDHED_HED_DIS_PER_VAL(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");//String.format("%,.2f",sHeaderDisPer)

                    ordHed.setFORDHED_PAYMENT_TYPE(presale.getFORDHED_PAYMENT_TYPE());
                    ordHed.setFORDHED_STATUS("NOT SYNCED");
                    ordHedList.add(ordHed);

                    if (new OrderController(getActivity()).createOrUpdateOrdHed(ordHedList) > 0) {


                        final OrderActivity activity = (OrderActivity) getActivity();
                        /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/
                        UpdateTaxDetails(RefNo);
                        new ItemLocController(getActivity()).UpdateOrderQOH(RefNo, "-", locCode);
                        Toast.makeText(getActivity(), "Order saved successfully..!", Toast.LENGTH_LONG).show();
                        mSharedPref.setDiscountClicked("0");
                        mSharedPref.setHeaderNextClicked("0");
                        mSharedPref.setOrderId(0);
                        mSharedPref.setEditOrderId(0);
                        mSharedPref.setGlobalVal("placeAnOrder", "");
                        mSharedPref.setGlobalVal("KeySelectedRep", "");
                        UtilityContainer.ClearReturnSharedPref(getActivity());
                        outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(mSharedPref.getSelectedDebCode());
                        new PreProductController(getActivity()).mClearTables();
                        //   new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
                        new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);

                        Upload(new OrderController(getActivity()).getAllUnSyncOrdHed());

                    } else {
                        Toast.makeText(getActivity(), "Order Save Failed..", Toast.LENGTH_LONG).show();
                    }

                }

            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertD = alertDialogBuilder.create();
            alertD.show();


        } else
            Toast.makeText(getActivity(), "Add items before save ...!", Toast.LENGTH_LONG).show();


    }

    public void Upload(ArrayList<OrderNew> orders) {
        // new OrderController(getActivity()).updateIsActive(""+mSharedPref.generateOrderId(),"2");
       // if (NetworkUtil.isNetworkAvailable(getActivity())) {
            if (new OrderController(getActivity()).getAllUnSyncOrdHed().size() > 0 ) {
                for (final OrderNew c : orders) {
                    try {
                        String content_type = "application/json";
                        ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                        final Handler mHandler = new Handler(Looper.getMainLooper());
                        JsonParser jsonParser = new JsonParser();
                        String orderJson = new Gson().toJson(c);
                        JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                        Log.d(">>>Orderjson",">>>"+objectFromString);
                        JsonArray jsonArray = new JsonArray();
                        jsonArray.add(objectFromString);


                        try{

                            FileWriter writer=new FileWriter(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ "KFD_OrderJson.txt");
                            writer.write(objectFromString.toString());
                            writer.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        Call<Result> resultCall = apiInterface.uploadOrder(objectFromString, content_type);
                        resultCall.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                int status = response.code();

                                if(response.isSuccessful()){
                                    response.body(); // have your all data
                                    boolean result =response.body().isResponse();
                                    Log.d( ">>response"+status,result+">>"+c.getRefNo() );
                                    if(result){
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                c.setIsSync("1");
                                                c.setIsActive("2");

                                                new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED","2");

                                                addRefNoResults(c.getRefNo() +" --> Success\n",orders.size());
                                                //Toast.makeText(getActivity(), "Order Upload successfully..!", Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }else{
                                        c.setIsSync("0");
                                        c.setIsActive("0");
                                       // new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0");
                                        new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED","0");

                                       // new OrderController(getActivity()).updateIsActive(c.getRefNo(), "0");
                                        addRefNoResults(c.getRefNo() +" --> Failed\n",orders.size());
                                        //Toast.makeText(getActivity(), "Order Upload Failed.", Toast.LENGTH_LONG).show();

                                    }
                                }else {
                                    Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                    Log.d( ">>error response"+status,response.errorBody().toString()+">>"+c.getRefNo() );
                                }// this will tell you why your api doesnt work most of time


                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error response " + t.toString(), Toast.LENGTH_LONG).show();
                                Intent intnt = new Intent(getActivity(), ActivityHome.class);
                                startActivity(intnt);
                                getActivity().finish();
                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else{
                Toast.makeText(getActivity(), "No Records to upload !", android.widget.Toast.LENGTH_LONG).show();
            }
//        } else
//            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

    }

    private void addRefNoResults(String ref, int count) {
        resultListPreSale.add(ref);
        if(count == resultListPreSale.size()) {
            mUploadResult(resultListPreSale);
        }
    }

    public void mUploadResult(List<String> messages) {
        String msg = "";
        for (String s : messages) {
            msg += s;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle("Upload Order Summary");

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intnt = new Intent(getActivity(), ActivityHome.class);
                startActivity(intnt);
                getActivity().finish();

            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void requestReupload() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Order NOT SEND. Do you want to RESEND the order?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        ArrayList<OrderNew> orders = new OrderController(getActivity()).getAllUnSyncOrdHed();
                        try{
                            if (new OrderController(getActivity()).getAllUnSyncOrdHed().size() > 0 ) {
                                //   Log.d(">>>3", ">>>3 ");
                                for (final OrderNew c : orders) {
                                    try {
                                        //    Log.d(">>>4", ">>>4 ");
                                        //  final Handler mHandler = new Handler(Looper.getMainLooper());
                                        JsonParser jsonParser = new JsonParser();
                                        String orderJson = new Gson().toJson(c);
                                        JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                                        JsonArray jsonArray = new JsonArray();
                                        jsonArray.add(objectFromString);
                                        String content_type = "application/json";
                                        ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                                        Call<Result> resultCall = apiInterface.uploadOrder(objectFromString, content_type);
                                        resultCall.enqueue(new Callback<Result>() {
                                            @Override
                                            public void onResponse(Call<Result> call, Response<Result> response) {
                                                if (response != null && response.body() != null) {
                                                    int status = response.code();
                                                    Log.d(">>>response code", ">>>res " + status);
                                                    Log.d(">>>response message", ">>>res " + response.message());
                                                    Log.d(">>>response body", ">>>res " + response.body().toString());
                                                    int resLength = response.body().toString().trim().length();
                                                    String resmsg = "" + response.body().toString();
                                                    if (status == 200 && !resmsg.equals("") && !resmsg.equals(null) && resmsg.substring(0,3).equals("202")) {
//                                                        mHandler.post(new Runnable() {
//                                                            @Override
//                                                            public void run() {
                                                        c.setIsSync("1");
                                                        new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1");

                                                        Intent intnt = new Intent(getActivity(), ActivityHome.class);
                                                        startActivity(intnt);
                                                        getActivity().finish();
//                                                            listVanAdapter.notifyDataSetChanged();
                                                        //       }
                                                        //     });
                                                    } else {
                                                        c.setIsSync("0");
                                                        new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0");
                                                        Intent intnt = new Intent(getActivity(), ActivityHome.class);
                                                        startActivity(intnt);
                                                        getActivity().finish();
//                                                           listVanAdapter.notifyDataSetChanged();
//                                                            requestReupload();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Result> call, Throwable t) {
                                                Toast.makeText(getActivity(), "Error response " + t.toString(), Toast.LENGTH_LONG).show();

                                            }

                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }else{
                                Toast.makeText(getActivity(), "No Records to upload !", android.widget.Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e(">>>ERROR In EDIT",">>>"+e.toString());
                            throw e;
                        }

//                        Toast.makeText(getActivity(), "Order discarded successfully..!", Toast.LENGTH_SHORT).show();

//                            Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
//                            intnt.putExtra("outlet", outlet);
//                            startActivity(intnt);
//                            getActivity().finish();


                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);

                        dialog.dismiss();


                    }
                })
                .build();
        materialDialog.setCanceledOnTouchOutside(false);
        materialDialog.show();
    }

    private String currentTime(){
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(cal.getTime());
    }

    private String currentDate() {
        // TODO Auto-generated method stub
        //current date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);

    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateTaxDetails(String refNo) {

        ArrayList<OrderDetail> list = new OrderDetailController(getActivity()).getAllOrderDetailsForTaxUpdate(refNo);
        new PreSaleTaxRGController(getActivity()).UpdateSalesTaxRG(list, mSharedPref.getSelectedDebCode());
        new PreSaleTaxDTController(getActivity()).UpdateSalesTaxDT(list);
    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void mPauseinvoice() {

        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0) {
            Order hed = new OrderController(getActivity()).getAllActiveOrdHed();
            outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(hed.getFORDHED_DEB_CODE());
            Intent intnt = new Intent(getActivity(), ActivityHome.class);
            intnt.putExtra("outlet", outlet);
            startActivity(intnt);
            getActivity().finish();
        } else
            Toast.makeText(getActivity(), "Add items before pause ...!", Toast.LENGTH_LONG).show();
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_PRE_SUMMARY"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mRefreshData();
        }
    }
    /*******************************************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity){
            activity=(Activity) context;
            try {
                responseListener = (OrderResponseListener) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
            }
        }

    }
}
