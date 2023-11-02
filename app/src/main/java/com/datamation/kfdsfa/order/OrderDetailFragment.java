package com.datamation.kfdsfa.order;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.adapter.PreOrderRecycleAdapter;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.controller.ItemController;
import com.datamation.kfdsfa.controller.ItemLocController;
import com.datamation.kfdsfa.controller.ItemPriceController;
import com.datamation.kfdsfa.controller.OrdFreeIssueController;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.helpers.OrderResponseListener;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.FreeIssue;
import com.datamation.kfdsfa.model.FreeItemDetails;
import com.datamation.kfdsfa.model.Item;
import com.datamation.kfdsfa.model.ItemFreeIssue;
import com.datamation.kfdsfa.model.OrdFreeIssue;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.model.PreProduct;
import com.datamation.kfdsfa.view.OrderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailFragment extends Fragment {

    private static final String TAG = "OrderDetailFragment";
    public View view;
    public SharedPref mSharedPref;
    int totPieces = 0;
    int seqno = 0;
    SweetAlertDialog pDialog;
    OrderResponseListener preSalesResponseListener;
    ArrayList<PreProduct> productList = null, selectedItemList = null;
    int clickCount = 0;
    OrderActivity mainActivity;
    private String RefNo, locCoe;
    private MyReceiver r;
    private Order tmpsoHed = null;
    RecyclerView lvProducts;
    SearchView search;
    MyReceiver rd;
    private FloatingActionButton next;
    OrderResponseListener responseListener;
    ArrayList<Item> loadlist;
    String genericItemName;

    //PreSalesResponseListener preSalesResponseListener;
    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.product_dialog_layout, container, false);
        //view = inflater.inflate(R.layout.sales_management_pre_sales_details_new, container, false);
        seqno = 0;
        totPieces = 0;
        mSharedPref = SharedPref.getInstance(getActivity());
//        mSharedPref = new SharedPref(getActivity());
        lvProducts = (RecyclerView) view.findViewById(R.id.lv_product_list);
        search = (SearchView) view.findViewById(R.id.et_search);
        next = (FloatingActionButton) view.findViewById(R.id.fab);

        lvProducts.setLongClickable(true);
        mainActivity = (OrderActivity) getActivity();
        RefNo = "" + mSharedPref.generateOrderId();
//        RefNo = mainActivity.selectedPreHed.getORDER_REFNO();
        tmpsoHed = new Order();
        new LoardingProductFromDB().execute();

        Log.v("Latitude>>>>>", mSharedPref.getGlobalVal("Latitude"));
        Log.v("Longi>>>>>", mSharedPref.getGlobalVal("Longitude"));
        mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
        mSharedPref.setDiscountClicked("0");
        clickCount = 0;

        Log.d("order_detail", "clicked_count" + clickCount);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (new OrderDetailController(getActivity()).tableHasRecords("" + mSharedPref.generateOrderId())) {
                    productList = new OrderDetailController(getActivity()).getAlreadyOrderedItems(query, new CustomerController(getActivity()).getCurrentLocCode(), "" + mSharedPref.generateOrderId());
                } else {
                    productList = new OrderDetailController(getActivity()).getAllItems(query, new CustomerController(getActivity()).getCurrentLocCode());
                }
                lvProducts.setAdapter(new PreOrderRecycleAdapter(getActivity(), productList));
                lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (new OrderDetailController(getActivity()).tableHasRecords("" + mSharedPref.generateOrderId())) {
                    productList = new OrderDetailController(getActivity()).getAlreadyOrderedItems(newText, new CustomerController(getActivity()).getCurrentLocCode(), "" + mSharedPref.generateOrderId());
                } else {
                    productList = new OrderDetailController(getActivity()).getAllItems(newText, new CustomerController(getActivity()).getCurrentLocCode());
                }
                lvProducts.setAdapter(new PreOrderRecycleAdapter(getActivity(), productList));
                lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                return true;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPref.setDiscountClicked("1");

                new OrderDetailController(getActivity()).DeleteZeroValueData(RefNo);
                new CalculateFree(mSharedPref.getSelectedDebCode()).execute();
            }
        });

        return view;
    }

    public void mToggleTextbox() {
        Log.d("Detail>>", ">>" + mSharedPref.getHeaderNextClicked());
        if (mSharedPref.getHeaderNextClicked() == "1") {

            new LoardingProductFromDB().execute();

        } else {
            preSalesResponseListener.moveBackToFragment(0);
            Toast.makeText(getActivity(), "Cannot proceed,Please click arrow button to save header details...", Toast.LENGTH_LONG).show();


        }
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rd);

        Log.d("order_detail", "clicked_count" + clickCount);

    }

    public void onResume() {
        super.onResume();
        rd = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rd, new IntentFilter("TAG_PRE_DETAILS"));
        Log.d("order_detail", "clicked_count" + clickCount);
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            mRefreshDataDet();
            OrderDetailFragment.this.mToggleTextbox();

            Log.d("order_detail", "clicked_count" + clickCount);
        }
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            preSalesResponseListener = (OrderResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onButtonPressed");
        }
    }
    public class LoardingProductFromDB extends AsyncTask<Object, Object, ArrayList<PreProduct>> {


        public LoardingProductFromDB() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Fetch Data Please Wait.");
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected ArrayList<PreProduct> doInBackground(Object... objects) {
            String loccode = new CustomerController(getActivity()).getCurrentLocCode();//prilcode, loccode hardcode for swadeshi

            if (new OrderDetailController(getActivity()).tableHasRecords("" + mSharedPref.generateOrderId())) {
                productList = new OrderDetailController(getActivity()).getAlreadyOrderedItems("", loccode, "" + mSharedPref.generateOrderId());
            } else {
                productList = new OrderDetailController(getActivity()).getAllItems("", loccode);
            }

            // productList = new OrderDetailController(getActivity()).getAllItems("",new CustomerController(getActivity()).getCurrentLocCode());//rashmi -2018-10-26
            return productList;
        }


        @Override
        protected void onPostExecute(ArrayList<PreProduct> products) {
            super.onPostExecute(products);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            //   productList = new OrderDetailController(getActivity()).getAllItems("",new CustomerController(getActivity()).getCurrentLocCode());
            lvProducts.setAdapter(new PreOrderRecycleAdapter(getActivity(), products));
            lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        }
    }

    public class CalculateFree extends AsyncTask<Object, Object, ArrayList<FreeItemDetails>> {
        CustomProgressDialog pdialog;
        private String debcode;

        public CalculateFree(String debcode) {
            this.pdialog = new CustomProgressDialog(getActivity());
            this.debcode = debcode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Calculating promotions.. Please Wait.");
            pdialog.show();

            //pDialog.show();
        }

        @Override
        protected ArrayList<FreeItemDetails> doInBackground(Object... objects) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pdialog.setMessage("Calculating Free...");
                }
            });
            new OrderDetailController(getActivity()).restFreeIssueData(RefNo);
            ArrayList<OrderDetail> dets = new OrderDetailController(getActivity()).getSAForFreeIssueCalc(RefNo);
            FreeIssue issue = new FreeIssue(getActivity());
            ArrayList<FreeItemDetails> list = issue.getFreeItemsBySalesItem(dets);
            try {

                for (FreeItemDetails freeItemDetails : list) {
                    int freeQty = freeItemDetails.getFreeQty();
                    String itemQOH = new ItemLocController(getActivity()).getProductQOH(freeItemDetails.getFreeIssueSelectedItem(), new CustomerController(getActivity()).getCurrentLocCode());
                    boolean validQOH = new OrderDetailController(getActivity()).getCheckQOH(RefNo, freeItemDetails.getFreeIssueSelectedItem(), freeQty, Integer.parseInt(itemQOH));
                    if (validQOH) {
                        updateFreeIssues(freeItemDetails);
                    } else {
                        Toast.makeText(getActivity(), "Not Enough Quantity On Hand For FREE ISSUES..!", Toast.LENGTH_LONG).show();

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("Exception", e.toString());
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pdialog.setMessage("Calculed Free...");
                }
            });
            return list;
        }


        @Override
        protected void onPostExecute(ArrayList<FreeItemDetails> products) {
            super.onPostExecute(products);

            if (pdialog.isShowing()) {
                pdialog.dismiss();
            }
            preSalesResponseListener.moveNextToFragment(2);
        }
    }

    private boolean updateFreeIssues(final FreeItemDetails itemDetails) {

        final ArrayList<ItemFreeIssue> itemFreeIssues;
        final String FIRefNo = itemDetails.getRefno();
        final ItemController itemsDS = new ItemController(getActivity());
        itemFreeIssues = itemsDS.getAllFreeItemNameByRefno(itemDetails.getRefno());

        for (ItemFreeIssue itemFreeIssue : itemFreeIssues) {

            seqno++;
            OrderDetail ordDet = new OrderDetail();
            OrderDetailController detDS = new OrderDetailController(getActivity());
            ArrayList<OrderDetail> ordList = new ArrayList<OrderDetail>();
            ItemPriceController priDS = new ItemPriceController(getActivity());

            ordDet.setFORDDET_ID("0");
            ordDet.setFORDDET_AMT("0.00");
            ordDet.setFORDDET_BAL_QTY("0");
            ordDet.setFORDDET_B_AMT("0.00");
            ordDet.setFORDDET_B_DIS_AMT("0.00");
            ordDet.setFORDDET_BP_DIS_AMT("0.00");

            double unitPrice = Double.parseDouble(priDS.getProductPriceByCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));

            ordDet.setFORDDET_B_SELL_PRICE(unitPrice + "");
            ordDet.setFORDDET_BT_TAX_AMT("0");
            ordDet.setFORDDET_BT_SELL_PRICE("0");
            ordDet.setFORDDET_CASE("0");
            ordDet.setFORDDET_CASE_QTY("0");
            ordDet.setFORDDET_DIS_AMT("0.00");
            ordDet.setFORDDET_DIS_PER("0.00");
            ordDet.setFORDDET_FREE_QTY(itemDetails.getFreeQty() + "");
            ordDet.setFORDDET_ITEM_CODE(itemFreeIssue.getItems().getFITEM_ITEM_CODE());
            ordDet.setFORDDET_P_DIS_AMT("0.00");
            ordDet.setFORDDET_PRIL_CODE(priDS.getPrilCodeByItemCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
            ordDet.setFORDDET_QTY(itemDetails.getFreeQty() + "");
            ordDet.setFORDDET_DIS_VAL_AMT("0.00");
            ordDet.setFORDDET_REA_CODE("");
            ordDet.setFORDDET_TYPE("FI");
            ordDet.setFORDDET_RECORD_ID("");
            ordDet.setFORDDET_REFNO("");
            if (mSharedPref.generateOrderId() == 0) {
                long time = System.currentTimeMillis();
                ordDet.setOrderId(time);
            } else {
                ordDet.setOrderId(mSharedPref.generateOrderId());
            }
            ordDet.setFORDDET_SELL_PRICE(unitPrice + "");
            ordDet.setFORDDET_SEQNO(seqno + "");
            ordDet.setFORDDET_TAX_AMT("0.00");
            ordDet.setFORDDET_TAX_COM_CODE("");
            ordDet.setFORDDET_TIMESTAMP_COLUMN("");
            ordDet.setFORDDET_T_SELL_PRICE("0");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            ordDet.setFORDDET_TXN_DATE(dateFormat.format(date));
            ordDet.setFORDDET_TXN_TYPE("21");
            ordDet.setFORDDET_IS_ACTIVE("1");
            ordDet.setFORDDET_ITEMNAME(new ItemController(getActivity()).getItemNameByCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
            ordDet.setFORDDET_PACKSIZE(new ItemController(getActivity()).getPackSizeByCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
            /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*OrdFreeIssue table update*-*-*-*-*-*-*-*-*-*-*-*-*-*/
            OrdFreeIssue ordFreeIssue = new OrdFreeIssue();
            ordFreeIssue.setOrdFreeIssue_ItemCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE());
            ordFreeIssue.setOrdFreeIssue_Qty(itemDetails.getFreeQty() + "");
            ordFreeIssue.setOrdFreeIssue_RefNo(FIRefNo);
            ordFreeIssue.setOrdFreeIssue_RefNo1(RefNo);
            ordFreeIssue.setOrdFreeIssue_TxnDate(dateFormat.format(date));
            new OrdFreeIssueController(getActivity()).UpdateOrderFreeIssue(ordFreeIssue);
            /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*/

            ordList.add(ordDet);

            if (detDS.createOrUpdateOrdDet(ordList) > 0) {

                //free calculate flag should be true
            }
        }
        return true;
    }

}
