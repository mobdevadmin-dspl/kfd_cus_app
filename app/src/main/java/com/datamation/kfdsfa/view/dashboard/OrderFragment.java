package com.datamation.kfdsfa.view.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.controller.OrderController;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.model.OrderNew;
import com.datamation.kfdsfa.model.apimodel.Result;
import com.datamation.kfdsfa.utils.NetworkUtil;
import com.datamation.kfdsfa.view.OrderActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;

public class OrderFragment extends Fragment {

    View view;
    ExpandableListView expListView;
    private NumberFormat numberFormat = NumberFormat.getInstance();
    ExpandableListAdapter listVanAdapter;
    List<Order> listDataHeader;
    NetworkFunctions networkFunctions;
    HashMap<Order, List<OrderDetail>> listDataChild;
    String Order_Status;
    SearchView search;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.responsive_order, container, false);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        search = (SearchView) view.findViewById(R.id.search);
        networkFunctions = new NetworkFunctions(getActivity());
        mHandler = new Handler(Looper.getMainLooper());

        prepareListData("", "");


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    searchListData(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    searchListData(newText);
                }

                return true;
            }
        });

        return view;

    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Order> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<Order, List<OrderDetail>> _listDataChild;

        public ExpandableListAdapter(Context context, List<Order> listDataHeader,
                                     HashMap<Order, List<OrderDetail>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View grpview, ViewGroup parent) {

            final OrderDetail childText = (OrderDetail) getChild(groupPosition, childPosition);

            if (grpview == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                grpview = infalInflater.inflate(R.layout.list_items, null);
            }

            TextView txtListChild = (TextView) grpview.findViewById(R.id.itemcode);
            TextView txtListChild1 = (TextView) grpview.findViewById(R.id.qty);
            TextView txtListChild2 = (TextView) grpview.findViewById(R.id.amount);

            txtListChild.setText(" " + childText.getFORDDET_ITEMNAME());
            txtListChild1.setText("Qty - " + childText.getFORDDET_QTY());
            txtListChild2.setText("Amount - " + numberFormat.format(Double.parseDouble(childText.getFORDDET_AMT())));
            return grpview;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            final Order headerTitle = (Order) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.refno);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView tot = (TextView) convertView.findViewById(R.id.total);
            final TextView stats = (TextView) convertView.findViewById(R.id.status);
            TextView cnfrmstats = (TextView) convertView.findViewById(R.id.cnfrmstatus);
            ImageView type = (ImageView) convertView.findViewById(R.id.type);
            ImageView print = (ImageView) convertView.findViewById(R.id.print);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText("" + headerTitle.getOrderId());


            if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("DISPATCHED")) {
                stats.setText("INVOICED");
                stats.setTextColor(Color.WHITE);
                cnfrmstats.setText("Click to refresh");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_dispatch));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
            }
//            else if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("INVOICED ")) {
//                stats.setText("ISSUED");
//                cnfrmstats.setText("               ");
//                stats.setBackground(getResources().getDrawable(R.drawable.bg_issued));
//                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
//            }
            else if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("REJECTED")) {
                stats.setText("REJECTED");
                cnfrmstats.setVisibility(View.GONE);
                stats.setBackground(getResources().getDrawable(R.drawable.bg_rejected));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
            } else if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("APPROVED")) {
                stats.setText("PROCESSING");
                cnfrmstats.setText("Click to refresh");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_approval));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
            } else if (headerTitle.getFORDHED_IS_SYNCED().equals("1") && headerTitle.getFORDHED_IS_ACTIVE().equals("2")) {
                listVanAdapter.notifyDataSetChanged();
                stats.setText("SENT");
                cnfrmstats.setText("Click to refresh");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_sync));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            } else if (headerTitle.getFORDHED_IS_SYNCED().equals("1") && headerTitle.getFORDHED_IS_ACTIVE().equals("0")) {
                stats.setText("SENT");
                cnfrmstats.setText("Click to refresh");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_sync));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            } else if (headerTitle.getFORDHED_IS_SYNCED().equals("0") && headerTitle.getFORDHED_IS_ACTIVE().equals("0") && headerTitle.getFORDHED_STATUS().equals("SYNCED")) {
                stats.setText("SENT");
                cnfrmstats.setText("Click to refresh");
                new OrderController(getActivity()).updateIsSynced("" +headerTitle.getOrderId(), "1", "SYNCED","0");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_sync));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            } else if (headerTitle.getFORDHED_IS_SYNCED().equals("0") && headerTitle.getFORDHED_IS_ACTIVE().equals("2") && headerTitle.getFORDHED_STATUS().equals("SYNCED")) {
                stats.setText("SENT");
                cnfrmstats.setText("Click to refresh");
                new OrderController(getActivity()).updateIsSynced("" +headerTitle.getOrderId(), "1", "SYNCED","2");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_sync));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            } else if (headerTitle.getFORDHED_IS_SYNCED().equals("0") && headerTitle.getFORDHED_IS_ACTIVE().equals("0")) {
                stats.setText("NOT SEND");
                cnfrmstats.setText("Click to refresh");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            }  else {
                stats.setText("NOT SEND");
                cnfrmstats.setText("Click to refresh");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            }

            date.setText(headerTitle.getFORDHED_TXN_DATE());
            tot.setText(headerTitle.getFORDHED_TOTAL_AMT());
            type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (headerTitle.getFORDHED_IS_SYNCED().equals("0")) {
                        int result = new OrderController(getActivity()).restData("" + headerTitle.getOrderId());
                        if (result > 0) {
                            new OrderDetailController(getActivity()).restData("" + headerTitle.getOrderId());
                            new SharedPref(getActivity()).setOrderId(0);
                            prepareListData("", "");
                            Toast.makeText(getActivity(), "Order deleted successfully", Toast.LENGTH_LONG).show();
                            new SharedPref(getActivity()).setEditOrderId(0);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Cannot delete synced orders", Toast.LENGTH_LONG).show();
                    }
                }
            });
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!new OrderController(getActivity()).isAnyActiveOrders()){
                        if ((!IsSync(headerTitle)) && (!IsConfirmed(headerTitle))) {
                            new SharedPref(getActivity()).setOrderId(headerTitle.getOrderId());
                            new SharedPref(getActivity()).setEditOrderId(1);
                            //new OrderController(getActivity()).updateIsSynced(headerTitle.getOrderId(), "1");
                            Intent intent = new Intent(getActivity(), OrderActivity.class);
                            intent.putExtra("From", "edit");
                            startActivity(intent);


//                        startActivity(intent);
//                        getActivity().finish();

//                        FragmentManager fragmentManager = getFragmentManager ();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();
//
//                        OrderDetailFragment myfragment = new OrderDetailFragment();  //your fragment
//
//                        // work here to add, remove, etc
//                        fragmentTransaction.add (R.id.order, myfragment);
//                        fragmentTransaction.commit ();
                        } else {
                            new SharedPref(getActivity()).setOrderId(0);
                            new SharedPref(getActivity()).setEditOrderId(0);
                            Toast.makeText(getActivity(), "Cannot edit confirmed orders", Toast.LENGTH_LONG).show();
                        }
                    } else {
                    Toast.makeText(getActivity(), "You can't edit this order now.  Already have active order. If you want to edit this, please go back and discard the active order", Toast.LENGTH_LONG).show();
                }
                }
            });
            cnfrmstats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new OrderController(getActivity()).updateIsActive("" + headerTitle.getOrderId(), "2");
                    ArrayList<OrderNew> orders = new OrderController(getActivity()).getAllUnSyncOrdHedByOrderId("" +headerTitle.getOrderId());
                    // Log.d(">>>2", ">>>2 ");
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        if (stats.getText().toString().equals("NOT SEND")) {
                            try {
                                if (new OrderController(getActivity()).getAllUnSyncOrdHed().size() > 0) {
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
                                                                 //   new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1");
                                                                    new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED","0");

                                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                    ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                                                                }
                                                            });
                                                        }else{
                                                            c.setIsSync("0");
                                                          //  new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0");
                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED","1");

                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
//                                                           listVanAdapter.notifyDataSetChanged();
                                                            requestReupload(c.getRefNo());
                                                        }
                                                    }else {
                                                        Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                                        Log.d( ">>error response"+status,response.errorBody().toString()+">>"+c.getRefNo() );
                                                    }



//
//                                                    if (response != null && response.body() != null) {
//                                                        int status = response.code();
//                                                        Log.d(">>>response code", ">>>res " + status);
//                                                        Log.d(">>>response message", ">>>res " + response.message());
//                                                        Log.d(">>>response body", ">>>res " + response.body().toString());
//                                                        int resLength = response.body().toString().trim().length();
//                                                        String resmsg = "" + response.body().toString();
//                                                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null) && resmsg.substring(0, 3).equals("202")) {
////                                                        mHandler.post(new Runnable() {
////                                                            @Override
////                                                            public void run() {
//                                                            c.setIsSync("1");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1");
//
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
////                                                            listVanAdapter.notifyDataSetChanged();
//                                                            //       }
//                                                            //     });
//                                                        } else {
//                                                            c.setIsSync("0");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0");
//
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
////                                                           listVanAdapter.notifyDataSetChanged();
//                                                            requestReupload();
//                                                        }
//                                                    } else {
//                                                        Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
//                                                    }

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

                                } else {
                                    Toast.makeText(getActivity(), "No Records to upload !", android.widget.Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e(">>>ERROR In EDIT", ">>>" + e.toString());
                                throw e;
                            }
                        } else if (stats.getText().toString().equals("SENT")) {//pending ---> order is only sync
                            new OrderStatusDownload(headerTitle.getOrderId(), "SYNCED").execute();
                            listVanAdapter.notifyDataSetChanged();
                        } else if (stats.getText().toString().equals("PROCESSING")) {
                            new OrderStatusDownload(headerTitle.getOrderId(), "APPROVED").execute();
                            listVanAdapter.notifyDataSetChanged();
                        }
//                        else if (stats.getText().toString().equals("PROCESSING")) {
//                            new OrderStatusDownload(headerTitle.getOrderId(), "DISPATCHED").execute();
//                        }
                    } else {
                        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                    prepareListData("", "");
//                    listVanAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Refreshed order successfully", Toast.LENGTH_LONG).show();

                }
            });
            listVanAdapter.notifyDataSetChanged();
            return convertView;
        }

        public void requestReupload(String orderId) {
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
                            ArrayList<OrderNew> orders = new OrderController(getActivity()).getAllUnSyncOrdHedByOrderId(orderId);
                            try {
                                if (new OrderController(getActivity()).getAllUnSyncOrdHed().size() > 0) {
                                    for (final OrderNew c : orders) {
                                        try {
                                            JsonParser jsonParser = new JsonParser();
                                            String orderJson = new Gson().toJson(c);
                                            JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                                            JsonArray jsonArray = new JsonArray();
                                            jsonArray.add(objectFromString);
                                            String content_type = "application/json";

                                            try{

                                                FileWriter writer=new FileWriter(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ "KFD_OrderJson.txt");
                                                writer.write(objectFromString.toString());
                                                writer.close();
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                            ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
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
                                                                   // new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1");
                                                                    new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED","0");


                                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                    ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                                                                }
                                                            });
                                                        }else{
                                                            c.setIsSync("0");
                                                        //    new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0");
                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED","1");


                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                                                        }
                                                    }else {
                                                        Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                                        Log.d( ">>error response"+status,response.errorBody().toString()+">>"+c.getRefNo() );
                                                    }// this will tell you why your api doesnt work most of time

//                                          **************************************************** old code *********** 2023-02-23 - kaveesha *******************
//                                                    if (response != null && response.body() != null) {
//                                                        int status = response.code();
//                                                        Log.d(">>>response code", ">>>res " + status);
//                                                        Log.d(">>>response message", ">>>res " + response.message());
//                                                        Log.d(">>>response body", ">>>res " + response.body().toString());
//                                                        int resLength = response.body().toString().trim().length();
//                                                        String resmsg = "" + response.body().toString();
//                                                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null) && resmsg.substring(0, 3).equals("202")) {
////                                                        mHandler.post(new Runnable() {
////                                                            @Override
////                                                            public void run() {
//                                                            c.setIsSync("1");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1");
//
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
////                                                            listVanAdapter.notifyDataSetChanged();
//                                                            //       }
//                                                            //     });
//                                                        } else {
//                                                            c.setIsSync("0");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0");
//
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
////                                                           listVanAdapter.notifyDataSetChanged();
////                                                            requestReupload();
//                                                        }
//                                                    } else {
//                                                        Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
//                                                    }
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
                                } else {
                                    Toast.makeText(getActivity(), "No Records to upload !", android.widget.Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e(">>>ERROR In EDIT", ">>>" + e.toString());
                                throw e;
                            }
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

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            double grossTotal = 0;


            List<Order> searchingDetails = _listDataHeader;


            for (Order invoice : searchingDetails) {

                if (invoice != null) {
                    grossTotal += Double.parseDouble(invoice.getFORDHED_TOTAL_AMT());

                }
            }

            // total.setText(numberFormat.format(grossTotal));
        }
    }


    public void prepareListData(String from, String to) {
        //  if(from.equals("") || to.equals("")) {
        listDataHeader = new OrderController(getActivity()).getAllOrders();
//        }else{
//            listDataHeader = new OrderController(getActivity()).getOrdersByDate(from,to);
//        }

        if (listDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
            listDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listDataHeader) {
                listDataChild.put(free, new OrderDetailController(getActivity()).getAllOrderDets("" + free.getOrderId()));
            }

            listVanAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listVanAdapter);
        } else {
            listDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listDataHeader) {
                listDataChild.put(free, new OrderDetailController(getActivity()).getAllOrderDets("" + free.getOrderId()));
            }

            listVanAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listVanAdapter);
        }
    }


    public void searchListData(String key) {

        listDataHeader = new OrderController(getActivity()).getAllOrdersBySearch(key);

        if (listDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
            listDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listDataHeader) {
                listDataChild.put(free, new OrderDetailController(getActivity()).getAllOrderDetsBySearch("" + free.getOrderId(), key));
            }

            listVanAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listVanAdapter);
        } else {
            listDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listDataHeader) {
                listDataChild.put(free, new OrderDetailController(getActivity()).getAllOrderDets("" + free.getOrderId()));
            }

            listVanAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listVanAdapter);
        }
    }

    private boolean IsConfirmed(Order order) {

        boolean orderConfirmed = false;


        if (order.getOrderId() != 0) {

            orderConfirmed = new OrderController(getActivity()).isAnyConfirmOrderHed(order.getOrderId() + "");

        }

        return orderConfirmed;
    }

    private boolean IsSync(Order order) {

        boolean orderSync = false;


        if (order.getOrderId() != 0) {

            orderSync = new OrderController(getActivity()).isAnySyncOrderHed(order.getOrderId() + "");

        }

        return orderSync;
    }


    // order status download - kaveesha - 18/11-2020 ---------------------------------------------------
    private class OrderStatusDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private long orderNo;
        private String status;

        public OrderStatusDownload(long orderNo, String status) {
            this.orderNo = orderNo;
            this.status = status;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Order Status...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    OrderController orderController = new OrderController(getActivity());

                    /*****************Order Status *****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Order Status...");
                        }
                    });

                    String orderStatus = "";
                    try {
                        orderStatus = networkFunctions.getOrderStatus(orderNo, status);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing Order Status
                    try {
                        JSONObject OrderStatusJSON = new JSONObject(orderStatus);
                        JSONArray OrderStatusJSONArray = OrderStatusJSON.getJSONArray("data");
                        ArrayList<Order> OrderStatusList = new ArrayList<Order>();

                        for (int i = 0; i < OrderStatusJSONArray.length(); i++) {
                            OrderStatusList.add(Order.parseOrderStatus(OrderStatusJSONArray.getJSONObject(i)));
                        }
                        orderController.CreateOrUpdateOrderStatus(OrderStatusList);
                    } catch (JSONException | NumberFormatException e) {
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            getParentFragment().getFragmentManager().beginTransaction().detach(OrderFragment.this).attach(OrderFragment.this).commit();

            pdialog.setMessage("Finalizing Order Status Data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }
}
