package com.datamation.kfdsfa.order;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.adapter.CustomerDebtAdapter;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.controller.ItemLocController;
import com.datamation.kfdsfa.controller.OrderController;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.controller.OutstandingController;
import com.datamation.kfdsfa.controller.PreProductController;
import com.datamation.kfdsfa.controller.RepDetailsController;
import com.datamation.kfdsfa.controller.RouteController;
import com.datamation.kfdsfa.controller.RouteDetController;
import com.datamation.kfdsfa.helpers.OrderResponseListener;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.Customer;
import com.datamation.kfdsfa.model.FddbNote;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.utils.UtilityContainer;
import com.datamation.kfdsfa.view.ActivityHome;
import com.datamation.kfdsfa.view.OrderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import com.bit.sfa.Settings.SharedPreferencesClass;

public class OrderHeaderFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    View view;
    private FloatingActionButton next;
    //public static EditText ordno, date, mNo, deldate, remarks;
    public String LOG_TAG = "OrderHeaderFragment";
    TextView lblCustomerName, outStandingAmt, lastBillAmt,lblPreRefno, deliveryDate;
    EditText  currnentDate,txtManual,txtRemakrs, txtRoute;
    MyReceiver r;
    public SharedPref pref;
    public SharedPref mSharedPref;
    OrderResponseListener preSalesResponseListener;
    OrderActivity activity;

    private Customer outlet;
    ImageButton img_bdate;
    Calendar Scalendar;
    int year, month , day;
    DatePickerDialog datePickerDialog;
    Spinner spPayment_Type,spSales_Rep;
    String formattedDate;
    String address = "No Address";
    String repcode;

    public OrderHeaderFragment() {
        // Required empty public constructor
    }

    public static OrderHeaderFragment newInstance() {
        OrderHeaderFragment fragment = new OrderHeaderFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_promo_sale_header, container, false);
        activity = (OrderActivity) getActivity();
        next = (FloatingActionButton) view.findViewById(R.id.fab);
        pref = SharedPref.getInstance(getActivity());
        mSharedPref =SharedPref.getInstance(getActivity());
        setHasOptionsMenu(true);
        final Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()); //change this
        formattedDate = simpleDateFormat.format(d);

        Scalendar = Calendar.getInstance();
        Scalendar.add(Calendar.DAY_OF_YEAR , 1);
        Date tomorrow = Scalendar.getTime();

        next = (FloatingActionButton) view.findViewById(R.id.fab);

        lblCustomerName = (TextView) view.findViewById(R.id.customerName);
        outStandingAmt = (TextView) view.findViewById(R.id.lbl_Inv_outstanding_amt);
        lastBillAmt = (TextView) view.findViewById(R.id.lbl_inv_lastbill);
        lblPreRefno = (TextView) view.findViewById(R.id.invoice_no);
        currnentDate = (EditText) view.findViewById(R.id.lbl_InvDate);
        txtManual = (EditText) view.findViewById(R.id.txt_InvManual);
        txtRemakrs = (EditText) view.findViewById(R.id.txt_InvRemarks);
        txtRoute = (EditText)view.findViewById(R.id.txt_route);
        img_bdate = (ImageButton)view.findViewById(R.id.imgbtn_DeliveryDate);
        deliveryDate = (TextView)view.findViewById(R.id.txt_deliveryDate);
        spPayment_Type = (Spinner)view.findViewById(R.id.paytype_spinner) ;
        spSales_Rep = (Spinner)view.findViewById(R.id.sales_rep_spinner) ;

        // lblCustomerName.setText(pref.getSelectedDebName());
        lblCustomerName.setText(new CustomerController(getActivity()).getCusNameByCode(pref.getSelectedDebCode()));
        // txtRoute.setText(new RouteDetController(getActivity()).getRouteCodeByDebCode(pref.getSelectedDebCode()));
        txtRoute.setText("");


        currnentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        deliveryDate.setText(simpleDateFormat.format(tomorrow));
        outStandingAmt.setText(String.format("%,.2f", new OutstandingController(getActivity()).getDebtorBalance(pref.getSelectedDebCode())));
        txtRemakrs.setEnabled(true);
        txtManual.setEnabled(true);
        mSharedPref.setHeaderNextClicked("0");
        //select Delivery Date
        year  = Scalendar.get(Calendar.YEAR);
        month = Scalendar.get(Calendar.MONTH);
        day   = Scalendar.get(Calendar.DAY_OF_MONTH);
        if(pref.generateOrderId() == 0) {
            long time = System.currentTimeMillis();
            pref.setOrderId(time);
            pref.setEditOrderId(time);
        }
        lblPreRefno.setText(""+pref.generateOrderId());

//        if (new OrderController(getActivity()).isAnyActiveOrderHed(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal))))
//        {
//            Order hed = new OrderController(getActivity()).getAllActiveOrdHed();
//
//            txtManual.setText(hed.getORDER_MANUREF());
//            txtRemakrs.setText(hed.getORDER_REMARKS());
        //  }

        /*Select Delivery Date*/
        img_bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = datePickerDialog.newInstance(OrderHeaderFragment.this,year,month,day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
                datePickerDialog.setTitle("");
                datePickerDialog.show(getActivity().getFragmentManager(),"DatePickerDialog");

            }
        });

        /*create payment Type*/
        List<String> listPayType  = new ArrayList<String>();

        listPayType.add("CASH");
        listPayType.add("CREDIT");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, listPayType);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPayment_Type.setAdapter(dataAdapter1);

        spPayment_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new SharedPref(getActivity()).setGlobalVal("KeyPayType" , spPayment_Type.getSelectedItem().toString());
                Log.v("PAYMENT TYPE", spPayment_Type.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> repList = new RepDetailsController(getActivity()).getAllSalesRep();
        repcode = new CustomerController(getActivity()).getCurrentRepCode();

        if(repList.isEmpty()){
            new SharedPref(getActivity()).setGlobalVal("KeySelectedRep" , repcode);
        }else {
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, repList);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSales_Rep.setAdapter(dataAdapter2);

            spSales_Rep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    new SharedPref(getActivity()).setGlobalVal("KeySelectedRep" , spSales_Rep.getSelectedItem().toString().split("-")[0].trim());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        outStandingAmt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.customer_debtor, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Customer outstanding...");
                alertDialogBuilder.setView(promptView);

                final ListView listView = (ListView) promptView.findViewById(R.id.lvCusDebt);
                ArrayList<FddbNote> list = new OutstandingController(getActivity()).getDebtInfo(SharedPref.getInstance(getActivity()).getSelectedDebCode());
                listView.setAdapter(new CustomerDebtAdapter(getActivity(), list));

                alertDialogBuilder.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPref.setHeaderNextClicked("1");
                if (lblCustomerName.getText().toString().equals(""))
                {
                    Log.d("<<<lblCustomerName<<<<", " " + lblCustomerName.getText().toString());
                    Log.d("<<<txtRoute<<<<", " " + txtRoute.getText().toString());
                    //   preSalesResponseListener.moveBackToFragment(0);
                    Toast.makeText(getActivity(), "Can not proceed without Customer...", Toast.LENGTH_LONG).show();
                    checkdate();
                }
                else
                {
                    SaveSalesHeader();


                }

            }
        });
//        if(mSharedPref.getHeaderNextClicked().equals("1")) {
//            Log.d("Header>>", ">>Headeroncreate");
//            OrderHeaderFragment.this.mRefreshHeader();
//        }
        return view;
    }
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mnu_exit, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.close:
                if(new OrderDetailController(getActivity()).isAnyActiveOrders() || !(pref.getEditOrderId() == 0)){

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
//                                    Log.d(">>>click discard"+pref.generateOrderId(), ">>>click discard"+pref.generateOrderId());
//                                    Log.d(">>>click discard"+pref.getEditOrderId(), ">>>click discard"+pref.getEditOrderId());
                                    int result = new OrderController(getActivity()).restData(""+pref.generateOrderId());
                                    if (result > 0) {
                                        new OrderDetailController(getActivity()).restData(""+pref.generateOrderId());
                                        pref.setDiscountClicked("0");
                                        pref.setHeaderNextClicked("0");
                                        pref.setOrderId(0);
                                        pref.setEditOrderId(0);
                                        new PreProductController(getActivity()).mClearTables();

                                    }
                                    pref.setGlobalVal("placeAnOrder", "");
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

//                    MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
//                            .content("You have active orders. Cannot back without complete.")
//                            .positiveText("OK")
//                            .positiveColor(getResources().getColor(R.color.material_alert_positive_button))
////                            .negativeText("No")
////                            .negativeColor(getResources().getColor(R.color.material_alert_negative_button))
//
//                            .callback(new MaterialDialog.ButtonCallback() {
//                                @Override
//                                public void onPositive(MaterialDialog dialog) {
//                                    dialog.dismiss();
//                                }
//
//                                @Override
//                                public void onNegative(MaterialDialog dialog) {
//                                    super.onNegative(dialog);
//                                }
//
//                                @Override
//                                public void onNeutral(MaterialDialog dialog) {
//                                    super.onNeutral(dialog);
//                                }
//                            })
//                            .build();
//                    materialDialog.setCancelable(false);
//                    materialDialog.setCanceledOnTouchOutside(false);
//                    materialDialog.show();
                }else{
                    MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                            .content("Do you want to back?")
                            .positiveText("Yes")
                            .positiveColor(getResources().getColor(R.color.material_alert_positive_button))
                            .negativeText("No")
                            .negativeColor(getResources().getColor(R.color.material_alert_negative_button))

                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    // Toast.makeText(getActivity(),"Not apply yet",Toast.LENGTH_LONG).show();
                                    Intent back = new Intent(getActivity(), ActivityHome.class);
                                    back.putExtra("outlet",new CustomerController(getActivity()).getSelectedCustomerByCode(SharedPref.getInstance(getActivity()).getSelectedDebCode()));
                                    startActivity(back);
                                    getActivity().finish();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                }

                                @Override
                                public void onNeutral(MaterialDialog dialog) {
                                    super.onNeutral(dialog);
                                }
                            })
                            .build();
                    materialDialog.setCancelable(false);
                    materialDialog.setCanceledOnTouchOutside(false);
                    materialDialog.show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public void SaveSalesHeader() {

        if (lblPreRefno.getText().length() > 0)
        {
            Order hed =new Order();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            String AppVersion = "";

            try{
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                AppVersion = pInfo.versionName;

            }catch (Exception e){
                e.printStackTrace();
            }
            String Loc_code = new CustomerController(getActivity()).getCurrentLocCode();
            String Cost_Code = new OrderController(getActivity()).getCostCodeByLocCode(Loc_code);

            hed.setFORDHED_REFNO("");
            hed.setOrderId(pref.generateOrderId());

            hed.setFORDHED_DEB_CODE(pref.getSelectedDebCode());
            hed.setFORDHED_ADD_DATE(df.format(new Date()));
            hed.setFORDHED_DELV_DATE(deliveryDate.getText().toString());
            //hed.setFORDHED_ROUTE_CODE(new RouteDetController(getActivity()).getRouteCodeByDebCode(pref.getSelectedDebCode()));
            hed.setFORDHED_MANU_REF("");
            hed.setFORDHED_REMARKS(txtRemakrs.getText().toString());
            //hed.setFORDHED_ADD_MACH(prefs_MACID.getString("MAC_Address", "No name defined").toString());
            hed.setFORDHED_ADD_MACH(pref.getMacAddress());
            hed.setFORDHED_ADD_USER("");
            hed.setFORDHED_APP_DATE("2020-09-30");
            hed.setFORDHED_APPSTS("1");
            hed.setFORDHED_APP_USER(pref.getSelectedDebCode());
            hed.setFORDHED_CUR_CODE("LKR");
            hed.setFORDHED_CUR_RATE("1.00");
            hed.setFORDHED_IS_ACTIVE("1");
            hed.setFORDHED_LOC_CODE("");
            hed.setFORDHED_COST_CODE(Cost_Code);
            hed.setFORDHED_RECORD_ID(AppVersion);
            hed.setFORDHED_START_TIME_SO(df.format(new Date()));
            hed.setFORDHED_REPCODE(pref.getGlobalVal("KeySelectedRep"));
            hed.setFORDHED_STATUS("NOT SYNCED");


            if (spPayment_Type.getSelectedItemPosition() == 0) {
                hed.setFORDHED_PAYMENT_TYPE("CA");
            }else if(spPayment_Type.getSelectedItemPosition() == 1){
                hed.setFORDHED_PAYMENT_TYPE("CH");
            }else {
                hed.setFORDHED_PAYMENT_TYPE("OTHERS");
            }


            ArrayList<Order> ordHedList=new ArrayList<Order>();
            OrderController ordHedDS =new OrderController(getActivity());
            ordHedList.add(hed);

            if (ordHedDS.createOrUpdateOrdHed(ordHedList)>0)
            {
                preSalesResponseListener.moveNextToFragment(1);
                Toast.makeText(getActivity(),"Order Header Saved...", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void mRefreshHeader() {
        Log.d("Header>>",">>"+mSharedPref.getHeaderNextClicked());

        ArrayList<OrderDetail> dets = new OrderDetailController(getActivity()).getSAForFreeIssueCalc(""+pref.generateOrderId());
        Log.d("Header>>",">>detsize"+dets.size());
        if(dets.size()>0&&mSharedPref.getHeaderNextClicked().equals("1")){
            preSalesResponseListener.moveBackToFragment(1);
        }


        currnentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        //  txtRoute.setText(new RouteDetController(getActivity()).getRouteCodeByDebCode(pref.getSelectedDebCode()));
        txtRoute.setText("");
        txtRemakrs.setEnabled(true);
        lblCustomerName.setText(new CustomerController(getActivity()).getCusNameByCode(pref.getSelectedDebCode()));
        lblPreRefno.setText(""+pref.generateOrderId());
        // lblPreRefno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));
        //deldate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
      //  SaveSalesHeader();
    }



    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);

        Log.d("Header>>",">>Headerpause");
    }

    public void onResume() {
        super.onResume();
        checkdate();
        r = new OrderHeaderFragment.MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_PRE_HEADER"));

        Log.d("Header>>",">>Headerresume");
//        ArrayList<OrderDetail> dets = new OrderDetailController(getActivity()).getSAForFreeIssueCalc(""+pref.generateOrderId());
//        Log.d("Header>>",">>detsizeresume"+dets.size());
//        if(dets.size()>0){
//            preSalesResponseListener.moveBackToFragment(1);
//        }
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());

        Date dateToday = null;
        Date date2 = null;

        try {

            dateToday = sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            date2 = sdf.parse(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date2.equals(dateToday) || date2.after(dateToday)) {
            deliveryDate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth)));
        }
        else {
            Toast.makeText(getActivity(),"Can't set previous date as a delivery date.Please enter valide date",Toast.LENGTH_LONG).show();
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            OrderHeaderFragment.this.mRefreshHeader();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity){
            activity=(Activity) context;
            try {
                preSalesResponseListener = (OrderResponseListener) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
            }
        }

    }
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public void checkdate() {
        int i = android.provider.Settings.Global.getInt(getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
        /* If option is selected */
        if (i > 0)
        {



            /* if not selected */
        } else {
            Toast.makeText(getActivity(), "Date is wrong..Please correct!!!", Toast.LENGTH_LONG).show();
            /* Show Date/time settings dialog */
            startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
        }

    }
}
