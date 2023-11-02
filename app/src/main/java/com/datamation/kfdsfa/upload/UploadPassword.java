package com.datamation.kfdsfa.upload;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.model.CusCredentials;
import com.datamation.kfdsfa.model.DebtorNew;
import com.datamation.kfdsfa.model.apimodel.Result;
import com.datamation.kfdsfa.view.ActivityHome;
import com.datamation.kfdsfa.view.ActivityLogin;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPassword extends AsyncTask<ArrayList<CusCredentials>, Integer, ArrayList<CusCredentials>> {



    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private Handler mHandler;
    ProgressDialog dialog;
    List<String> resultListPreSale;
    NetworkFunctions networkFunctions;
    int totalRecords;

    public UploadPassword(Context context) {
        resultListPreSale = new ArrayList<>();
        this.context = context;
        mHandler = new Handler(Looper.getMainLooper());
        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Uploading password records");
        dialog.show();
    }

    @Override
    protected ArrayList<CusCredentials> doInBackground(ArrayList<CusCredentials>... params) {
        int recordCount = 0;
        publishProgress(recordCount);
        networkFunctions = new NetworkFunctions(context);
        final ArrayList<CusCredentials> RCSList = params[0];
        totalRecords = RCSList.size();
        for (final CusCredentials c : RCSList)
        {
            try {
                String content_type = "application/json";
                ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
                JsonParser jsonParser = new JsonParser();
                String orderJson = new Gson().toJson(c);
                JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(objectFromString);

                Call<Result> resultCall = apiInterface.uploadCusNewPassword(objectFromString, content_type);
                resultCall.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {

                        int status = response.code();

                        if(response.isSuccessful()){
                            response.body(); // have your all data
                            boolean result =response.body().isResponse();
                            Log.d( ">>response"+status,result+">>"+c.getUserid() );
                            if(result){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        c.setIsSync("1");
                                        addRefNoResults(c.getUserid() + " --> Success\n", RCSList.size(),"Success");
                                        new CustomerController(context).updateIsSynced(c.getUserid(), "1");
                                    }
                                });
                            }else{
                                Log.d(">>response" + status, "" + c.getUserid());
                                c.setIsSync("0");
                                new CustomerController(context).updateIsSynced(c.getUserid(), "0");
                                addRefNoResults(c.getIsSync() + " --> Failed\n", RCSList.size(),"Failed");
                            }
                        }else {
                            Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            Log.d( ">>error response"+status,response.errorBody().toString()+">>"+c.getUserid() );
                        }// this will tell you why your api doesnt work most of time



//
//                        if (response != null && response.body() != null) {
//                            int status = response.code();
//                            Log.d(">>>response code", ">>>res " + status);
//                            Log.d(">>>response message", ">>>res " + response.message());
//                            Log.d(">>>response body", ">>>res " + response.body().toString());
//                            int resLength = response.body().toString().trim().length();
//                            String resmsg = "" + response.body().toString();
//                            if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
//                                mHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // resultListNonProduct.add(np.getNONPRDHED_REFNO()+ "--->SUCCESS");
//                                        //    addRefNoResults_Non(np.getNONPRDHED_REFNO() + " --> Success\n",RCSList.size());
//                                        //  Log.d( ">>response"+status,""+c.getORDER_REFNO() );
//                                        c.setIsSync("1");
//                                        addRefNoResults(c.getDebCode() + " --> Success\n", RCSList.size());
//                                        // new OrderController(context).updateIsSynced(c);
//                                        new CustomerController(context).updateIsSynced(c.getDebCode(), "1");
//                                        //  Toast.makeText(context,np.getNONPRDHED_REFNO()+"-Non-productive uploded Successfully" , Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                //addRefNoResults(c.getORDER_REFNO() +" --> Success\n",RCSList.size());
//
//                                //  Toast.makeText(context, c.getORDER_REFNO()+" - Order uploded Successfully", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.d(">>response" + status, "" + c.getDebCode());
//                                c.setIsSync("0");
//                                new CustomerController(context).updateIsSynced(c.getDebCode(), "0");
//                                addRefNoResults(c.getIsSync() + " --> Failed\n", RCSList.size());
//                                //   Toast.makeText(context, c.getORDER_REFNO()+" - Order uplod Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }else{
//                            Toast.makeText(context, " Invalid response when signup details upload", Toast.LENGTH_SHORT).show();
//                        }

                        }

                        @Override
                        public void onFailure (Call <Result> call, Throwable t){
                            Toast.makeText(context, "Error response " + t.toString(), Toast.LENGTH_SHORT).show();
                        }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            ++recordCount;
            publishProgress(recordCount);
        }

        return RCSList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialog.setMessage("Uploading.. PreSale Record " + values[0] + "/" + totalRecords);
    }

    @Override
    protected void onPostExecute(ArrayList<CusCredentials> RCSList) {

        super.onPostExecute(RCSList);
        dialog.dismiss();

    }
    private void addRefNoResults(String ref, int count, String status) {
        resultListPreSale.add(ref);
        if(count == resultListPreSale.size()) {
            mUploadResult(resultListPreSale,status);
        }
    }

    public void mUploadResult(List<String> messages,String mStatus) {
        String msg = "";
        for (String s : messages) {
            msg += s;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle("Upload Password Summary");

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
                if(mStatus.equals("Success")){
                    Intent homeActivity = new Intent(context, ActivityHome.class);
                    context.startActivity(homeActivity);
                    Toast.makeText(context,"New password updated...",Toast.LENGTH_LONG).show();
                }else {
                    Intent loginActivity = new Intent(context, ActivityLogin.class);
                    context.startActivity(loginActivity);
                    Toast.makeText(context,"New password update failed. Please try again...",Toast.LENGTH_LONG).show();
                }
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

}
