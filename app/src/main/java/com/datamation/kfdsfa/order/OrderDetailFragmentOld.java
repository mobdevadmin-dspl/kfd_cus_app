package com.datamation.kfdsfa.order;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.adapter.OrderDetailsAdapter;
import com.datamation.kfdsfa.adapter.OrderFreeItemAdapter;
import com.datamation.kfdsfa.adapter.PreOrderAdapter;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.controller.ItemController;
import com.datamation.kfdsfa.controller.ItemLocController;
import com.datamation.kfdsfa.controller.ItemPriceController;
import com.datamation.kfdsfa.controller.OrdFreeIssueController;
import com.datamation.kfdsfa.controller.OrderController;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.controller.PreProductController;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.helpers.OrderResponseListener;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.Customer;
import com.datamation.kfdsfa.model.FreeIssue;
import com.datamation.kfdsfa.model.FreeItemDetails;
import com.datamation.kfdsfa.model.ItemFreeIssue;
import com.datamation.kfdsfa.model.OrdFreeIssue;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.model.PreProduct;
import com.datamation.kfdsfa.utils.GPSTracker;
import com.datamation.kfdsfa.view.OrderActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailFragmentOld extends Fragment {

    private static final String TAG = "OrderDetailFragment";
    public View view;
    public SharedPref mSharedPref;
    int totPieces = 0;
    int seqno = 0;
    ListView lv_order_det, lvFree;
    ImageButton ibtProduct;
    Button ibtDiscount;
    SweetAlertDialog pDialog;
    OrderResponseListener preSalesResponseListener;
    ArrayList<PreProduct> productList = null, selectedItemList = null;
    int clickCount = 0;
    OrderActivity mainActivity;
    ArrayList<OrderDetail> orderList;
    String address = "No Address";
    double latitude = 0.0;
    double longitude = 0.0;
    GPSTracker gpsTracker;
    String isQohZeroAllow = "0", qohStatus = "1";//qohStatus = 1 (show qoh > 0) else  all items check only isQohZeroAllow = 1 (2019-12-20 rashmi)
    private  String RefNo, locCoe;
    private  MyReceiver r;
    private Order tmpsoHed=null;  //from re oder creation
    private double totAmt = 0.0;
    private Customer debtor;
    int listcnt = 0;
    int listfreecnt = 0;
    int QOHEnough = 0;

    //PreSalesResponseListener preSalesResponseListener;
    public OrderDetailFragmentOld() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.pre_sale_details_new_responsive_layout, container, false);
        //view = inflater.inflate(R.layout.sales_management_pre_sales_details_new, container, false);
        seqno = 0;
        totPieces = 0;
        mSharedPref =SharedPref.getInstance(getActivity());
        lv_order_det = (ListView) view.findViewById(R.id.lvProducts_pre);
        lvFree = (ListView) view.findViewById(R.id.lvFreeIssue_Inv);
        ibtDiscount = (Button) view.findViewById(R.id.ibtDisc);
        ibtProduct = (ImageButton) view.findViewById(R.id.ibtProduct);
        mainActivity = (OrderActivity) getActivity();
        RefNo = "A0001OR/0001";
//        RefNo = mainActivity.selectedPreHed.getORDER_REFNO();
        tmpsoHed = new Order();
        showData();
        gpsTracker = new GPSTracker(getActivity());
        Log.v("Latitude>>>>>",mSharedPref.getGlobalVal("Latitude"));
        Log.v("Longi>>>>>",mSharedPref.getGlobalVal("Longitude"));
        mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
        mSharedPref.setDiscountClicked("0");
        clickCount = 0;

        Log.d("order_detail", "clicked_count" + clickCount);



        ibtProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(new OrderController(getActivity()).IsSavedHeader(RefNo)>0){
                    mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
                    mSharedPref.setDiscountClicked("0");
                    clickCount = 0;

                        new LoardingProductFromDB().execute();

                }else{
                    preSalesResponseListener.moveBackToFragment(0);
                    Toast.makeText(getActivity(), "Cannot proceed,Please click arrow button to save header details...", Toast.LENGTH_LONG).show();
                }

            }
        });

        ibtDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPref.setDiscountClicked("1");
                mSharedPref.setGlobalVal("preKeyIsFreeClicked", ""+clickCount);
                if(clickCount == 0) {

                    new CalculateFree(mSharedPref.getSelectedDebCode()).execute();
                   // new CalculateDiscounts(mSharedPref.getSelectedDebCode()).execute();
                    clickCount++;
                }else{
                    Toast.makeText(getActivity(),"Already clicked",Toast.LENGTH_LONG).show();
                    Log.v("Freeclick Count", mSharedPref.getGlobalVal("preKeyIsFreeClicked"));
                }

            }
        });

        lv_order_det.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mSharedPref.setDiscountClicked("0");
                clickCount = 0;
                mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
                new OrderDetailController(getActivity()).restFreeIssueData(RefNo);
                newDeleteOrderDialog(position);
                return true;
            }
        });


        return view;
    }

    public void mToggleTextbox()
    {
        gpsTracker = new GPSTracker(getActivity());
        Log.v("Latitude>>>>>",mSharedPref.getGlobalVal("Latitude"));
        Log.v("Longi>>>>>",mSharedPref.getGlobalVal("Longitude"));
        showData();


    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);

        Log.d("order_detail", "clicked_count" + clickCount);

    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_DETAILS"));
        Log.d("order_detail", "clicked_count" + clickCount);
    }

    public void showData() {
        try
        {
            lv_order_det.setAdapter(null);
            orderList = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
            lv_order_det.setAdapter(new OrderDetailsAdapter(getActivity(), orderList, mSharedPref.getSelectedDebCode()));//2019-07-07 till error free

            lvFree.setAdapter(null);
            ArrayList<OrderDetail> freeList=new OrderDetailController(getActivity()).getAllFreeIssue(RefNo);
            lvFree.setAdapter(new OrderFreeItemAdapter(getActivity(), freeList));

            Log.d("order_detail", "clicked_count" + clickCount);


        } catch (NullPointerException e) {
            Log.v("SA Error", e.toString());
        }
    }

    public void ProductDialogBox() {

        final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.product_dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final ListView lvProducts = (ListView) promptView.findViewById(R.id.lv_product_list);
        final SearchView search = (SearchView) promptView.findViewById(R.id.et_search);


        lvProducts.clearTextFilter();
        productList.clear();
        productList = new PreProductController(getActivity()).getAllItems("");
        lvProducts.setAdapter(new PreOrderAdapter(getActivity(), productList));

        alertDialogBuilder.setCancelable(false).setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                selectedItemList = new PreProductController(getActivity()).getSelectedItems();
//2019-10-18 rashmi
                    updateOrderDet(selectedItemList);

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                productList = new PreProductController(getActivity()).getAllItems(query);//Rashmi 2018-10-26
                lvProducts.setAdapter(new PreOrderAdapter(getActivity(), productList));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                productList.clear();
                productList = new PreProductController(getActivity()).getAllItems(newText);//rashmi-2018-10-26
                lvProducts.setAdapter(new PreOrderAdapter(getActivity(), productList));
                return true;
            }
        });
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void updateOrderDet(final ArrayList<PreProduct> list) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
//                pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
//                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                pDialog.setTitleText("Updating products...");
//                pDialog.setCancelable(false);
//                pDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                int i = 0;
                new OrderDetailController(getActivity()).deleteRecords(RefNo);//commented by rashmi because check duplicate issue
                ArrayList<OrderDetail> toSaveOrderDetails = mUpdatePrsSales(list,RefNo);

                if (new OrderDetailController(getActivity()).createOrUpdateOrdDet(toSaveOrderDetails)>0)
                {
                    Log.d("ORDER_DETAILS", "Order det saved successfully...");
                }
                else
                {
                    Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                if(pDialog.isShowing()){
//                    pDialog.dismiss();
//                }

                showData();
            }

        }.execute();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            preSalesResponseListener = (OrderResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
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

            if (new PreProductController(getActivity()).tableHasRecords()) {
                productList = new PreProductController(getActivity()).getAllItems("");
                } else {
                String loccode = new CustomerController(getActivity()).getCurrentLocCode();
                new PreProductController(getActivity()).insertIntoProductAsBulkForPre(loccode);//prilcode, loccode hardcode for swadeshi
                productList = new PreProductController(getActivity()).getAllItems("");

                if(tmpsoHed!=null) {

                    ArrayList<OrderDetail> orderDetailArrayList = tmpsoHed.getOrdDet();
                    if (orderDetailArrayList != null) {
                        for (int i = 0; i < orderDetailArrayList.size(); i++) {
                            String tmpItemcode = orderDetailArrayList.get(i).getFORDDET_ITEM_CODE();
                            String tmpItemname = orderDetailArrayList.get(i).getFORDDET_ITEMNAME();
                            String tmpprice = orderDetailArrayList.get(i).getFORDDET_SELL_PRICE();
                            String tmpQty = orderDetailArrayList.get(i).getFORDDET_QTY();
                            String tmprefno = orderDetailArrayList.get(i).getFORDDET_REFNO();

                            //Update Qty in  fProducts_pre table
                            int count = new PreProductController(getActivity()).updateQuantities(tmpItemcode,tmpItemname,tmpprice, tmpQty,tmprefno);
                            if (count > 0) {
                                Log.d("InsertOrUpdate", "success");
                            } else {
                                Log.d("InsertOrUpdate", "Failed");
                            }

                        }
                    }
                }
                //----------------------------------------------------------------------------
            }
            productList = new PreProductController(getActivity()).getAllItems("");//rashmi -2018-10-26
            return productList;
        }


        @Override
        protected void onPostExecute(ArrayList<PreProduct> products) {
            super.onPostExecute(products);

            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            ProductDialogBox();
        }
    }
    public ArrayList<OrderDetail> mUpdatePrsSales(ArrayList<PreProduct> list,String refno)
    {

        ArrayList<OrderDetail> SOList = new ArrayList<OrderDetail>();

        String UnitPrice = "";
        double amt = 0.0;
        totPieces = 0;
        for (PreProduct tempOrder : list) {
                OrderDetail ordDet = new OrderDetail();
                    totPieces = Integer.parseInt(tempOrder.getPREPRODUCT_QTY());
                    amt = totPieces * Double.parseDouble(tempOrder.getPREPRODUCT_PRICE());
                    ordDet.setFORDDET_AMT(String.format("%.2f", amt));
                    ordDet.setFORDDET_BAL_QTY(tempOrder.getPREPRODUCT_QTY() + "");
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
                    ordDet.setFORDDET_PRIL_CODE(new ItemPriceController(getActivity()).getPrilCodeByItemCode(tempOrder.getPREPRODUCT_ITEMCODE()));
                    ordDet.setFORDDET_QTY(tempOrder.getPREPRODUCT_QTY() + "");
                    ordDet.setFORDDET_DIS_VAL_AMT("0.00");
                    ordDet.setFORDDET_PICE_QTY(tempOrder.getPREPRODUCT_QTY() + "");
                    ordDet.setFORDDET_REA_CODE("");
                    ordDet.setFORDDET_TYPE("SA");
                    ordDet.setFORDDET_RECORD_ID("");
                    ordDet.setFORDDET_REFNO(refno);
                    ordDet.setFORDDET_SELL_PRICE(tempOrder.getPREPRODUCT_PRICE());
                    ordDet.setFORDDET_SEQNO(seqno + "");
                    ordDet.setFORDDET_TAX_AMT("0.00");
                    ordDet.setFORDDET_TAX_COM_CODE(new ItemController(getActivity()).getItemTaxComCode(tempOrder.getPREPRODUCT_ITEMCODE()));
                    ordDet.setFORDDET_TIMESTAMP_COLUMN("");
                    ordDet.setFORDDET_T_SELL_PRICE(tempOrder.getPREPRODUCT_PRICE());
                    ordDet.setFORDDET_TXN_DATE(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));
                    ordDet.setFORDDET_IS_ACTIVE("1");
                    ordDet.setFORDDET_ITEMNAME(new ItemController(getActivity()).getItemNameByCode(tempOrder.getPREPRODUCT_ITEMCODE()));
                    ordDet.setFORDDET_PACKSIZE(new ItemController(getActivity()).getPackSizeByCode(tempOrder.getPREPRODUCT_ITEMCODE()));

                SOList.add(ordDet);

        }

        return SOList;
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
                        String itemQOH = new ItemLocController(getActivity()).getProductQOH(freeItemDetails.getFreeIssueSelectedItem(),new CustomerController(getActivity()).getCurrentLocCode());
                        boolean validQOH = new OrderDetailController(getActivity()).getCheckQOH(RefNo, freeItemDetails.getFreeIssueSelectedItem(), freeQty,Integer.parseInt(itemQOH));
                        if (validQOH){
                            updateFreeIssues(freeItemDetails);
                            showData();
                        }else{
                            showData();
                            android.widget.Toast.makeText(getActivity(), "Not Enough Quantity On Hand For FREE ISSUES..!", android.widget.Toast.LENGTH_LONG).show();

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

            if(pdialog.isShowing()){
                pdialog.dismiss();
            }

            showData();

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
            ordDet.setFORDDET_REFNO(RefNo);
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
                showData();
            }
        }
        return true;
    }
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            OrderDetailFragmentOld.this.mToggleTextbox();

            Log.d("order_detail", "clicked_count" + clickCount);
        }
    }
}
