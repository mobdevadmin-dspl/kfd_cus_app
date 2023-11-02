package com.datamation.kfdsfa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.model.CusCredentials;
import com.datamation.kfdsfa.model.Debtor;
import com.datamation.kfdsfa.model.DebtorNew;
import com.datamation.kfdsfa.upload.UploadPassword;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.apimodel.ReadJsonList;
import com.datamation.kfdsfa.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    /*********modified by rashmi 2020-09-11********/
    EditText username, password;
    TextView txtver;
    SharedPref pref;
    NetworkFunctions networkFunctions;
    int tap;
    RelativeLayout loginlayout;
    private long timeInMillis;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        networkFunctions = new NetworkFunctions(this);
        pref = SharedPref.getInstance(this);
        username = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);
        Button login = (Button) findViewById(R.id.btnlogin);
        txtver = (TextView) findViewById(R.id.textVer);
        loginlayout = (RelativeLayout) findViewById(R.id.loginLayout);
        txtver.setText("Version " + getVersionCode());
        timeInMillis = System.currentTimeMillis();

        login.setOnClickListener(this);

        txtver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tap += 1;
                // StartTimer(3000);
                if (tap >= 7) {
                    // validateDialog();
                }
            }
        });


    }



    public void reCallActivity() {
        Intent mainActivity = new Intent(ActivityLogin.this, ActivityLogin.class);
        startActivity(mainActivity);
        finish();
    }

    public void StartTimer(int timeout) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tap = 0;
            }
        }, timeout);

    }

    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";

    }

    //********************** kaveesha - 10/12/2020 -************************************************
    public void LoginFailedMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("please check your User Id and Password ");
        alertDialogBuilder.setTitle("Login Failed");
        alertDialogBuilder.setIcon(R.drawable.info);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                reCallActivity();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin: {


                if (!(username.getText().toString().equalsIgnoreCase("")) && !(password.getText().toString().equalsIgnoreCase(""))) {
                    //temparary for datamation
                    Log.d(">>>", "Validation :: " + username.getText().toString());
                    Log.d(">>>", "Validation :: " + password.getText().toString());

                    if((username.getText().toString().equalsIgnoreCase("admin")) && (password.getText().toString().equalsIgnoreCase("dspl@123"))){
                        pref.setGlobalVal("UserType", "admin");
                        Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
                        startActivity(loginActivity);
                        finish();
                    }else {
                        if(NetworkUtil.isNetworkAvailable(ActivityLogin.this)){
                            new  Validate(username.getText().toString().trim(),password.getText().toString().trim()).execute();
                        }else{
                            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Log.d(">>>", "Validation :: " + username.getText().toString());
                    Log.d(">>>", "Validation :: " + password.getText().toString());
                    Toast.makeText(this, "Please fill the valid credentials", Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                }


            }
            break;

            default:
                break;
        }
    }



    public void SignUp() {
        final Dialog repDialog = new Dialog(this);
        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repDialog.setCancelable(false);
        repDialog.setCanceledOnTouchOutside(false);
        repDialog.setContentView(R.layout.sign_up);
        //initializations

        final TextView cusID = (TextView) repDialog.findViewById(R.id.cID);
        final EditText ExistingPw = (EditText) repDialog.findViewById(R.id.ePW);
        final EditText newPW = (EditText) repDialog.findViewById(R.id.nPW);
        final EditText conPW = (EditText) repDialog.findViewById(R.id.cPW);

        cusID.setText(pref.getLoginUser().getFDEBTOR_CUSID());
        ExistingPw.setText(pref.getLoginUser().getFDEBTOR_OTP());


        //close
        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPW.getText().toString().equalsIgnoreCase(conPW.getText().toString()) && !newPW.getText().toString().equals("") && !conPW.getText().toString().equals("")){
                    repDialog.dismiss();
                    pref.setSelectedDebCode(pref.getLoginUser().getFDEBTOR_CODE());
                    pref.setValidateStatus(true);
                    pref.setLoginStatus(true);
                    pref.storeNewOTP(newPW.getText().toString());
                    String NewPass = sha1Hash(newPW.getText().toString());

                    ArrayList<CusCredentials> credentialsList = new ArrayList<>();
                    CusCredentials credentials = new CusCredentials();
                    credentials.setUserid(pref.getLoginUser().getFDEBTOR_CODE());
                    credentials.setPassword(NewPass);
                    credentialsList.add(credentials);

//                    if(new CustomerController(ActivityLogin.this).updateNewPassword(pref.getLoginUser().getFDEBTOR_CODE(),NewPass)>0){
//                        Toast.makeText(getApplicationContext(), "Password updated", Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(getApplicationContext(), "Password not updated", Toast.LENGTH_LONG).show();
//                    }
                    if(NetworkUtil.isNetworkAvailable(ActivityLogin.this)){
                        //ArrayList<DebtorNew> passwordupload = new CustomerController(ActivityLogin.this).uploadCustomerPassword(pref.getLoginUser().getFDEBTOR_CODE());
                        new UploadPassword(ActivityLogin.this).execute(credentialsList);
                        Log.v(">>8>>", "UploadPreSales execute finish");

                    }else{
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();

                    }
//                    Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
//                    startActivity(loginActivity);
//                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Please check new password again", Toast.LENGTH_LONG).show();

                }


            }
        });
        repDialog.show();
    }


    private void showErrorText(String s) {
        Snackbar snackbar = Snackbar.make(loginlayout, R.string.txt_msg, Snackbar.LENGTH_LONG);
        View snackbarLayout = snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sync, 0, 0, 0);
        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.body_size));
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    String sha1Hash( String toHash )
    {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }




    private class Validate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String cusId, otp;

        public Validate(String uid, String pwd) {
            this.cusId = uid;
            this.otp = pwd;
            this.pdialog = new CustomProgressDialog(ActivityLogin.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Validating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            try {
                int recordCount = 0;
                int totalBytes = 0;
                String validateResponse = null;
                JSONObject validateJSON;
                try {

                    CustomerController customerController = new CustomerController(ActivityLogin.this);
                    customerController.deleteAll();

                    validateResponse = networkFunctions.getCustomer(cusId,otp);
                    Log.d("validateResponse", validateResponse);
                    validateJSON = new JSONObject(validateResponse);

                    if (validateJSON != null) {
                        pref = SharedPref.getInstance(ActivityLogin.this);
                        //dbHandler.clearTables();
                        // Login successful. Proceed to download other items

                        JSONArray repArray = validateJSON.getJSONArray("data");
                      //  if(repArray.length()>0) {
                            ArrayList<Debtor> UserList = new ArrayList<>();
                            for (int i = 0; i < repArray.length(); i++) {
                                JSONObject userJSON = repArray.getJSONObject(i);
                                pref.storeLoginUser(Debtor.parseDebtor(userJSON), otp);
//                            pref.setUserId(userJSON.getString("repCode").trim());
//                            pref.setUserPwd(userJSON.getString("password").trim());
//                            pref.setUserPrefix(userJSON.getString("repPreFix").trim());
                                UserList.add(Debtor.parseDebtor(userJSON));
                            }

                            customerController.InsertOrReplaceDebtor(UserList);


//                        if(UserList.size()>0){
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    pdialog.setMessage("Authenticated...");
//                                }
//                            });
//                        }else{
//                            Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
//                            reCallActivity();
//                        }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pdialog.setMessage("Authenticated...");
                                }
                            });

                            return true;
                        }else{
                            Toast.makeText(ActivityLogin.this, "No matching customer data", Toast.LENGTH_SHORT).show();
                            return false;
                        }
//                    } else {
//                        Toast.makeText(ActivityLogin.this, "Invalid response from server when getting customer data", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }

                } catch (IOException e) {
                    Log.e("networkFunctions ->", "IOException -> " + e.toString());
                    throw e;
                } catch (JSONException e) {
                    Log.e("networkFunctions ->", "JSONException -> " + e.toString());
                    throw e;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            pDialog.setMessage("Prefetching data..." + progress[0] + "/" + totalRecords);
//
//        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pdialog.isShowing())
                pdialog.cancel();
            if (result) {
                    String hashtest = sha1Hash(otp);

                try{
                    if(!pref.getLoginUser().getFDEBTOR_PASSWORD().equals("") || !pref.getLoginUser().getFDEBTOR_PASSWORD().equals(null)){

                            if (hashtest.equalsIgnoreCase(pref.getLoginUser().getFDEBTOR_PASSWORD()) && pref.getLoginUser().getFDEBTOR_FIRSTLOGIN().equals("0")) {
                                pref.setSelectedDebCode(pref.getLoginUser().getFDEBTOR_CODE());
                                pref.setValidateStatus(true);
                                pref.setLoginStatus(true);
                                Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
                                startActivity(loginActivity);
                                finish();
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                SignUp();
                            }
                    }else{
                        SignUp();
                    }

                }catch (NullPointerException ex){
                    LoginFailedMessage();
                }

            } else {
                LoginFailedMessage();
            }
        }
    }

//    private void Validate(String customerId, final String otp){//2020-03-19- by rashmi
//
//        try{
//            ApiInterface apiInterface = ApiCllient.getClient(ActivityLogin.this).create(ApiInterface.class);
//            Call<ReadJsonList> resultCall = apiInterface.getDebtorResult(customerId,otp);
//            resultCall.enqueue(new Callback<ReadJsonList>() {
//                @Override
//                public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
//                    CustomProgressDialog pdialog = new CustomProgressDialog(ActivityLogin.this);
//                    pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    pdialog.setMessage("Validating...");
//                    pdialog.show();
//                    // System.out.println("test responce 01 " + response.body().getDebtorResult().size());
//                    //  System.out.println(response.body().getInvDetResult().get(1));]
//                    if (response.body() != null) {
//                        ArrayList<Debtor> cusList = new ArrayList<Debtor>();
//                        if (response.body().getDebtorResult().size() > 0) {
//                            for (int i = 0; i < response.body().getDebtorResult().size(); i++) {
//                                cusList.add(response.body().getDebtorResult().get(i));
//                            }
//                            new CustomerController(ActivityLogin.this).InsertOrReplaceDebtor(cusList);
//                            networkFunctions.setUser(cusList.get(0));
//                            pref.storeLoginUser(cusList.get(0), otp);
//                            System.out.println("CUSTOMER List " + cusList.toString());
//                            if (cusList.size() > 0) {
//                                if (pdialog.isShowing()) {
//                                    pdialog.cancel();
//                                }
//                                String hashtest = sha1Hash(otp);
//                                if (hashtest.equalsIgnoreCase(pref.getLoginUser().getFDEBTOR_PASSWORD()) && pref.getLoginUser().getFDEBTOR_FIRSTLOGIN().equals("0")) {
//                                    pref.setSelectedDebCode(pref.getLoginUser().getFDEBTOR_CODE());
//                                    pref.setValidateStatus(true);
//                                    pref.setLoginStatus(true);
//                                    Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
//                                    startActivity(loginActivity);
//                                    finish();
//                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    SignUp();
//                                }
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Invalid Customer Id", Toast.LENGTH_LONG).show();
//                                reCallActivity();
//                            }
//                        } else {
//                            LoginFailedMessage();
//                        }
//                    }
//                }
//                @Override
//                public void onFailure(Call<ReadJsonList> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//        }catch (Exception e){
//            Log.d(">>>ERROR Validate",">>>"+e.toString());
//        }
//    }

}
