package com.datamation.kfdsfa.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.api.TaskTypeUpload;
import com.datamation.kfdsfa.controller.AreaController;
import com.datamation.kfdsfa.controller.BankController;
import com.datamation.kfdsfa.controller.BrandController;
import com.datamation.kfdsfa.controller.CompanyDetailsController;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.controller.FreeDebController;
import com.datamation.kfdsfa.controller.FreeDetController;
import com.datamation.kfdsfa.controller.FreeHedController;
import com.datamation.kfdsfa.controller.FreeItemController;
import com.datamation.kfdsfa.controller.FreeMslabController;
import com.datamation.kfdsfa.controller.GroupController;
import com.datamation.kfdsfa.controller.ItemController;
import com.datamation.kfdsfa.controller.ItemLocController;
import com.datamation.kfdsfa.controller.ItemPriceController;
import com.datamation.kfdsfa.controller.LocationsController;
import com.datamation.kfdsfa.controller.OrderController;
import com.datamation.kfdsfa.controller.OutstandingController;
import com.datamation.kfdsfa.controller.PaymentController;
import com.datamation.kfdsfa.controller.PaymentDetailController;
import com.datamation.kfdsfa.controller.PaymentHeaderController;
import com.datamation.kfdsfa.controller.PushMsgHedDetController;
import com.datamation.kfdsfa.controller.ReasonController;
import com.datamation.kfdsfa.controller.RepDetailsController;
import com.datamation.kfdsfa.controller.RouteDetController;
import com.datamation.kfdsfa.controller.TownController;
import com.datamation.kfdsfa.controller.TypeController;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.fragment.FragmentCategoryWiseDownload;
import com.datamation.kfdsfa.model.Debtor;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderNew;
import com.datamation.kfdsfa.model.apimodel.ReadJsonList;
import com.datamation.kfdsfa.settings.TaskTypeDownload;
import com.datamation.kfdsfa.upload.UploadPreSales;
import com.datamation.kfdsfa.view.dashboard.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.datamation.sfa.controller.ItemController;
import com.datamation.kfdsfa.fragment.FragmentHome;
import com.datamation.kfdsfa.fragment.FragmentTools;
import com.datamation.kfdsfa.helpers.NetworkFunctions;

import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.Customer;
import com.datamation.kfdsfa.controller.RouteController;
//import com.datamation.sfa.presale.OrderMainFragment;
import com.datamation.kfdsfa.R;

import com.datamation.kfdsfa.settings.UserSessionManager;
import com.datamation.kfdsfa.utils.NetworkUtil;
import com.datamation.kfdsfa.utils.UtilityContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity {

    public static final String SETTINGS = "SETTINGS";
    public Customer selectedDebtor = null;
    public int cusPosition = 0;
    private Context context = this;
    public String TAG = "ActivityHome.class";
    NetworkFunctions networkFunctions;
    List<String> resultList;
    SharedPref pref;
    Debtor loggedUser;
    private long timeInMillis;
    String currentVersion = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    ApiInterface apiInterface;
    int New = 0;
    int Current;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(0);
                    return true;
                case R.id.navigation_sales:
                    SharedPref sharedPref = SharedPref.getInstance(context);
                    sharedPref.setGlobalVal("placeAnOrder", "1");
                    if (sharedPref.getGlobalVal("SyncDate").equalsIgnoreCase(dateFormat.format(new Date(timeInMillis)))) {
                        Log.d("Test SecondarySync", "Secondary sync done");
                        if(NetworkUtil.isNetworkAvailable(ActivityHome.this)) {
                         //   CheckServerAvailable(pref.getLoginUser().getFDEBTOR_CUSID(),pref.getLoginUser().getFDEBTOR_OTP());

                            Log.d(">>new","New"+New);
                            Log.d(">>current","current"+Current);

                            if(New > Current){
                                Toast.makeText(ActivityHome.this,"Please update your application before place an order... ",Toast.LENGTH_LONG).show();

                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }else {
                                Intent intent = new Intent(ActivityHome.this, OrderActivity.class);
                                intent.putExtra("From","new");
                                startActivity(intent);
                            }

                        }else {
                            Toast.makeText(ActivityHome.this,"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            if(NetworkUtil.isNetworkAvailable(ActivityHome.this)){
                                new CheckServerAvailable(pref.getLoginUser().getFDEBTOR_CUSID(),pref.getLoginUser().getFDEBTOR_OTP()).execute();
                               // CheckServerAvailable(pref.getLoginUser().getFDEBTOR_CUSID(),pref.getLoginUser().getFDEBTOR_OTP());
                            }else{
                                Toast.makeText(ActivityHome.this,"No internet connection",Toast.LENGTH_LONG).show();
                            }
                        }catch(Exception e){
                            Log.e(">>>> Secondary Sync",e.toString());
                        }

                    }

                    return true;
                case R.id.navigation_downloads:
                    dataSyncDialog(ActivityHome.this);
                 //   new secondarySync(pref.getSelectedDebCode()).execute();
                    return true;
                case R.id.navigation_tools:
                    if (pref.getGlobalVal("UserType").equalsIgnoreCase("admin")){
                        //UtilityContainer.mLoadFragment(new FragmentTools(), ActivityHome.this);
                        validateSettings(ActivityHome.this);
                }else {
                        Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                  //  validateSettings(ActivityHome.this);
//                    UtilityContainer.mLoadFragment(new FragmentTools_new(), ActivityHome.this);
                    return true;
                case R.id.navigation_logout:
                    Logout();
                    return true;
            }
            return false;
        }
    };

    public static BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = SharedPref.getInstance(this);
        networkFunctions = new NetworkFunctions(this);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        resultList = new ArrayList<>();
        loggedUser = pref.getLoginUser();
        currentVersion = getVersionCode();
        timeInMillis = System.currentTimeMillis();
        //set home frgament
        changeFragment(0);
        if(NetworkUtil.isNetworkAvailable(context)) {
              new checkVersion().execute();
        }else{
            Toast.makeText(context, "No internet connection for validate app version", Toast.LENGTH_LONG).show();
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        pref.setImageFlag("1");


    }


    @Override
    public void onBackPressed() {
        //nothing (disable backbutton)
    }
    public void validateSettings(final Context context)
    {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.settings_sqlite_password_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.et_password);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Go",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String user_text = (userInput.getText()).toString();

                                /** CHECK FOR USER'S INPUT **/
                                if (user_text.equals("admin@kfd"))
                                {
                                    Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
                                    UtilityContainer.mLoadFragment(new FragmentTools(), ActivityHome.this);

                                }
                                else{
                                    Log.d(user_text,"string is empty");
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            validateSettings(context);
                                        }
                                    });
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }

                        }

                );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";

    }
    public class checkVersion extends AsyncTask<String, String, String> {

        private SweetAlertDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new SweetAlertDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String version = "0";
            try {

                URL json = new URL("" + pref.getBaseURL()+getResources().getString(R.string.connection_string) +"fControl");
               // URL json = new URL("" + pref.getBaseURL()+getResources().getString(R.string.connection_string) +"fControl/mobile123/" + pref.getDistDB());
                URLConnection jc = json.openConnection();

                BufferedReader readerfdblist = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = readerfdblist.readLine();
                JSONObject jsonResponse = new JSONObject(line);
                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                JSONObject jObject = (JSONObject) jsonArray.get(0);
                version = jObject.getString("appVersion");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return version;
        }

        protected void onPostExecute(String newVersion) {
            super.onPostExecute(newVersion);
            if(dialog.isShowing())
                dialog.dismiss();

            if (newVersion != null && !newVersion.isEmpty()) {

                if(!newVersion.trim().equals("")) {
                    New = Integer.parseInt(newVersion.trim().replace(".", ""));
                }

                 Current = Integer.parseInt(currentVersion.replace(".", ""));
                Log.v("New Version", ">>>>>>"+New);

                Log.v("old Version", ">>>>>>"+Current);
                //163>162
                if (New > Current) {
                    //show dialog
                    Log.v("UPDATE AVAILABLESSSS", "USSPDATE");
                    // Create custom dialog object

                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("New Update Available.")
                            .setContentText("Vesrion : " + newVersion)
                            .setConfirmText("Yes,Update!")
                            .setCancelText("No,cancel!")
                            .showCancelButton(false)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    //Loading();
                                    sDialog.dismiss();

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

//                                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.datamation.dss"));
//                                    startActivity(intent);
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            })

                            .show();

                } else {
                    Toast.makeText(context, "Your application is up to date", Toast.LENGTH_LONG).show();

                }

            } else
            {
                Toast.makeText(context, "Invalid response from server when check version", Toast.LENGTH_LONG).show();

            }


        }


    }
    public void Logout() {

        final Dialog Ldialog = new Dialog(ActivityHome.this);
        Ldialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Ldialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Ldialog.setContentView(R.layout.logout);


        //logout
        Ldialog.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSessionManager sessionManager = new UserSessionManager(context);
                sessionManager.Logout();
                finish();
                pref.setLoginStatus(false);
                pref.clearPref();
            }
        });
        Ldialog.show();
    }

    public void viewRouteInfo() {
        final Dialog repDialog = new Dialog(context);
        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repDialog.setCancelable(false);
        repDialog.setCanceledOnTouchOutside(false);
        repDialog.setContentView(R.layout.rep_route_profile);

        //initializations
        RouteController routeDS = new RouteController(context);
        //  String routes = routeDS.getRouteNameByCode(loggedUser.getRoute());

        TextView routeName = (TextView) repDialog.findViewById(R.id.routeName);
        // routeName.setText(routes);
        TextView routeCode = (TextView) repDialog.findViewById(R.id.routeCode);
        //   routeCode.setText(loggedUser.getRoute());


        //close
        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repDialog.dismiss();
            }
        });

        repDialog.show();
    }



    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /**
     * To load fragments for sample
     *
     * @param position menu index
     */
    private void changeFragment(int position) {


        if (position == 0) {
            //   newFragment = new FragHome();
            Log.d(">>>>>>", "position0");
            UtilityContainer.mLoadFragment(new FragmentHome(), ActivityHome.this);
        } else if (position == 1) {
            Log.d(">>>>>>", "position1");
        }
    }

    public void bottomNav(Boolean cmd) {

        if (cmd == true) {

            navigation.setVisibility(View.GONE);
        } else {
            navigation.setVisibility(View.VISIBLE);
        }
    }

    public void dataSyncDialog(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.settings_selection_layout);
        dialog.setTitle("Download/Upload");

        final Button t_download = (Button) dialog.findViewById(R.id.t_download);
        final Button  t_upload = (Button) dialog.findViewById(R.id.t_upload);

        t_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new secondarySync(pref.getSelectedDebCode()).execute();
            }
        });

        t_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                syncDialog(context);
            }
        });

        dialog.show();
    }

    private void syncDialog(final Context context) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(ActivityHome.this)
                .content("Are you sure, Do you want to Upload Data?")
                .positiveColor(ContextCompat.getColor(ActivityHome.this, R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(ActivityHome.this, R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        boolean connectionStatus = NetworkUtil.isNetworkAvailable(context);
                        if (connectionStatus == true) {

                            ArrayList<OrderNew> orderList = new OrderController(ActivityHome.this).getAllUnSyncOrdHed();

                            if (orderList.size() <= 0 )
                            {
                                Toast.makeText(ActivityHome.this, "No Records to upload !", Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    new UploadPreSales(ActivityHome.this,  TaskTypeUpload.UPLOAD_ORDER).execute(orderList);
                                    Log.v(">>8>>", "UploadPreSales execute finish");
                                } catch (Exception e) {
                                    Log.e("***", "onPositive: ", e);
                                }
                            }
                        } else
                            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
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

    public class secondarySync extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String debcode ,orderNo ,status;
        private List<String> errors = new ArrayList<>();
        private Handler mHandler;

        public secondarySync(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(ActivityHome.this);
            mHandler = new Handler(Looper.getMainLooper());
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(ActivityHome.this);
            mHandler = new Handler(Looper.getMainLooper());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }
        @Override
        protected Boolean doInBackground(String... arg0) {
            apiInterface = ApiCllient.getClient(ActivityHome.this).create(ApiInterface.class);
            mHandler = new Handler(Looper.getMainLooper());
            try {
                if (SharedPref.getInstance(ActivityHome.this).getLoginUser() != null && SharedPref.getInstance(ActivityHome.this).isLoggedIn()) {
                    new CompanyDetailsController(ActivityHome.this).deleteAll();

/*****************company details**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Company Details...");
                        }
                    });
                    // Processing controls
                    try{
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Controllist, networkFunctions.getCompanyDetails(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    /***************** Types - kaveesha - 2020/10/06 *************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Types....");
                        }
                    });

                    // Processing Types
                    try{
                        TypeController typeController = new TypeController(ActivityHome.this);
                        typeController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Type, networkFunctions.getTypes());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    /*****************Items*****************************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Item Details...");
                        }
                    });

                    // Processing item
                    try{
                        ItemController itemController = new ItemController(ActivityHome.this);
                        itemController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Items, networkFunctions.getItems(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************ItemLoc*****************************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Item Loc Details...");
                        }
                    });

                    // Processing item loc
                    try{
                        ItemLocController itemLocController = new ItemLocController(ActivityHome.this);
                        itemLocController.deleteAllItemLoc();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.ItemLoc, networkFunctions.getItemLocations(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************reasons**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Reasons...");
                        }
                    });
                    // Processing reasons

                    try {
                        ReasonController reasonController = new ReasonController(ActivityHome.this);
                        reasonController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Reason, networkFunctions.getReasons());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************ItemPri**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Item Pri Details...");
                        }
                    });
                    // Processing Item Pri

                    try {
                        ItemPriceController itemPriceController = new ItemPriceController(ActivityHome.this);
                        itemPriceController.deleteAllItemPri();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.ItemPri, networkFunctions.getItemPrices(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Banks...");
                        }
                    });
                    /*****************banks**********************************************************************/

                    try {
                        BankController bankController = new BankController(ActivityHome.this);
                        bankController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Bank, networkFunctions.getBanks());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Route**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Routes...");
                        }
                    });
                    // Processing route

                    try {
                        RouteController routeController = new RouteController(ActivityHome.this);
                        routeController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Route, networkFunctions.getRoutes(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Route det**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Route Det...");
                        }
                    });

                    // Processing route
                    try {
                        RouteDetController routeDetController = new RouteDetController(ActivityHome.this);
                        routeDetController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.RouteDet, networkFunctions.getRouteDets(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Area - kaveesha - 2020/10/05 ****************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Area Details...");
                        }
                    });

                    // Processing area
                    try {
                        AreaController areaController = new AreaController(ActivityHome.this);
                        areaController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Area, networkFunctions.getArea());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Locations - kaveesha - 2020/10/05 *****************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Locations...");
                        }
                    });

                    // Processing locations
                    try {
                        LocationsController locationsController = new LocationsController(ActivityHome.this);
                        locationsController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Locations, networkFunctions.getLocations(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }


                    /*****************Towns - kaveesha - 2020/10/05 *******************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Towns...");
                        }
                    });

                    // Processing towns
                    try {
                        TownController townController = new TownController(ActivityHome.this);
                        townController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Towns, networkFunctions.getTowns());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Groups - kaveesha - 2020/10/05 *****************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Groups...");
                        }
                    });

                    // Processing group
                    try {
                        GroupController groupController = new GroupController(ActivityHome.this);
                        groupController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Groups, networkFunctions.getGroups());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Brands - kaveesha - 2020/10/06 *****************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Brands...");
                        }
                    });

                    // Processing brands
                    try {
                        BrandController brandController = new BrandController(ActivityHome.this);
                        brandController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Brand, networkFunctions.getBrands());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************freeMslab**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Free...");
                        }
                    });
                    // Processing freeMslab
                    try {

                        FreeMslabController freemSlabController = new FreeMslabController(ActivityHome.this);
                        freemSlabController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Freemslab, networkFunctions.getFreeMslab());
                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************FreeHed**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Free...");
                        }
                    });
                    // Processing freehed
                    try {

                        FreeHedController freeHedController = new FreeHedController(ActivityHome.this);
                        freeHedController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Freehed, networkFunctions.getFreeHed(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************Freedet**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Free...");
                        }
                    });

                    // Processing freedet
                    try {

                        FreeDetController freeDetController = new FreeDetController(ActivityHome.this);
                        freeDetController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Freedet, networkFunctions.getFreeDet(debcode));

                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************freedeb**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Free...");
                        }
                    });

                    // Processing freedeb
                    try {

                        FreeDebController freeDebController = new FreeDebController(ActivityHome.this);
                        freeDebController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Freedeb, networkFunctions.getFreeDebs(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************freeItem**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Free...");
                        }
                    });


                    // Processing freeItem
                    try {

                        FreeItemController freeItemController = new FreeItemController(ActivityHome.this);
                        freeItemController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Freeitem, networkFunctions.getFreeItems());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Outsatnding...");
                        }
                    });

                    // Processing fddbnote
                    try {
                        OutstandingController outstandingController = new OutstandingController(ActivityHome.this);
                        outstandingController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.fddbnote, networkFunctions.getFddbNotes(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Push Msg HedDet  - kaveesha - 16-09-2020**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Push Msg Hed Det....");
                        }
                    });
                    // Processing Push Msg HedDet

                    try {
                        PushMsgHedDetController pushMsgHedDetController = new PushMsgHedDetController(ActivityHome.this);
                        pushMsgHedDetController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.PushMsgHedDet, networkFunctions.getPushMsgHedDet(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************CusPRecHed  - kaveesha - 22-09-2020**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading RecHed....");
                        }
                    });
                    // Processing Cusp RecHed

                    try {
                        PaymentHeaderController paymentHeaderController = new PaymentHeaderController(ActivityHome.this);
                        paymentHeaderController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.CusPRecHed, networkFunctions.getCusPRecHed(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************CusPRecDet  - kaveesha - 22-09-2020**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading RecDet ....");
                        }
                    });
                    // Processing Cusp RecDet

                    try {
                        PaymentDetailController paymentDetailController = new PaymentDetailController(ActivityHome.this);
                        paymentDetailController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.CusPRecDet, networkFunctions.getCusPRecDet(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Rep Details  - kaveesha - 16-12-2020**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Rep Details ....");
                        }
                    });
                    // Processing Rep Details

                    try {
                        RepDetailsController repDetailsController = new RepDetailsController(ActivityHome.this);
                        repDetailsController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.RepDetails, networkFunctions.getRepDetails(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    /*****************Payments  - kaveesha - 10-11-2020**********************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Payments ....");
                        }
                    });
                    // Processing Payments

                    try {
                        PaymentController paymentController = new PaymentController(ActivityHome.this);
                        paymentController.deleteAll();
                        UtilityContainer.download(ActivityHome.this,TaskTypeDownload.Payments, networkFunctions.getPayments(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Completed...");
                        }
                    });

                    /*****************end sync**********************************************************************/
                    return true;
                } else {
                    errors.add("SharedPref.getInstance(ActivityHome.this).getLoginUser() = null OR !SharedPref.getInstance(ActivityHome.this).isLoggedIn()");
                    Log.d("ERROR>>>>>", "Login USer" + SharedPref.getInstance(ActivityHome.this).getLoginUser().toString() + " IS LoggedIn --> " + SharedPref.getInstance(ActivityHome.this).isLoggedIn());
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                errors.add(e.toString());

                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing())
                    pdialog.dismiss();

                Toast.makeText(context, "Download Successfully.....", Toast.LENGTH_LONG).show();

                if(pref.getGlobalVal("placeAnOrder").equals("1"))
                {
                    pref.setOrderId(0);
                    pref.setEditOrderId(0);
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    intent.putExtra("From","new");
                    startActivity(intent);
                    finish();
                }
//                  int i = 1;
//                    for (Control c : new CompanyDetailsController(ActivityHome.this).getAllDownload()) {
//                        downloadList.add(c);
//                        i++;
//                    }
//
//                    if(downloadList.size()>0) {
//                        mDownloadResult(downloadList);
//                    }
//
//                showErrorText("Successfully Synchronized");
            } else {
                if (pdialog.isShowing())
                    pdialog.dismiss();

//                    int i = 1;
//                    for (Control c : new CompanyDetailsController(ActivityHome.this).getAllDownload()) {
//                        downloadList.add(c);
//                        i++;
//                    }
//
//                    if(downloadList.size()>0) {
//                        mDownloadResult(downloadList);
//                    }
//                }
//                StringBuilder sb = new StringBuilder();
//                if (errors.size() == 1) {
//                    sb.append(errors.get(0));
//                    showErrorText(sb.toString());
//                } else if(errors.size() == 0) {
//                    sb.append("Following errors occurred");
//                    for (String error : errors) {
//                        sb.append("\n - ").append(error);
//                        showErrorText(sb.toString());
//                    }
//
            }

        }
    }

    //---------------kaveesha - 11/04/2020----------------------------------------
    public void ServerAvailability() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Request Server Connection !");
        alertDialogBuilder.setMessage("Server is unavailable .Please check your server connection");
        alertDialogBuilder.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    private class CheckServerAvailable extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String cusId, otp;

        public CheckServerAvailable(String uid, String pwd) {
            this.cusId = uid;
            this.otp = pwd;
            this.pdialog = new CustomProgressDialog(ActivityHome.this);
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

                    CustomerController customerController = new CustomerController(ActivityHome.this);
                    customerController.deleteAll();

                    validateResponse = networkFunctions.getCustomer(cusId,otp);
                    Log.d("validateResponse", validateResponse);
                    validateJSON = new JSONObject(validateResponse);

                    if (validateJSON != null) {
                        pref = SharedPref.getInstance(ActivityHome.this);

                        JSONArray repArray = validateJSON.getJSONArray("data");
                        ArrayList<Debtor> UserList = new ArrayList<>();
                        for (int i = 0; i < repArray.length(); i++) {
                            JSONObject userJSON = repArray.getJSONObject(i);
                            pref.storeLoginUser(Debtor.parseDebtor(userJSON),otp);
                            UserList.add(Debtor.parseDebtor(userJSON));
                        }

                        customerController.InsertOrReplaceDebtor(UserList);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pdialog.setMessage("Authenticated...");
                            }
                        });

                        return true;
                    } else {
                        Toast.makeText(ActivityHome.this, "Invalid response from server when getting customer data", Toast.LENGTH_SHORT).show();
                        return false;
                    }

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
                if (result) {

                    pref.setServerConnectionStatus(true);
                    pref.setGlobalVal("SyncDate", dateFormat.format(new Date(timeInMillis)));
                    new secondarySync(pref.getSelectedDebCode()).execute();
                } else {
                    pref.setServerConnectionStatus(false);
                    ServerAvailability();
                }
            } else {
                pref.setServerConnectionStatus(false);
                ServerAvailability();
            }
        }
    }

//    public void CheckServerAvailable(String customerId, final String otp){
//
//        try{
//            ApiInterface apiInterface = ApiCllient.getClient(ActivityHome.this).create(ApiInterface.class);
//            Call<ReadJsonList> resultCall = apiInterface.getDebtorResult(customerId,otp);
//            resultCall.enqueue(new Callback<ReadJsonList>() {
//                @Override
//                public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
//                    CustomProgressDialog pdialog = new CustomProgressDialog(ActivityHome.this);
//                    pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    pdialog.setMessage("Validating...");
//                    pdialog.show();
//                    if(response.body()!= null) {
//                        ArrayList<Debtor> cusList = new ArrayList<Debtor>();
//                        for (int i = 0; i < response.body().getDebtorResult().size(); i++) {
//                            cusList.add(response.body().getDebtorResult().get(i));
//                        }
//
//                        if (cusList.size() > 0) {
//                            pref.setServerConnectionStatus(true);
//                            pref.setGlobalVal("SyncDate", dateFormat.format(new Date(timeInMillis)));
//                            new secondarySync(pref.getSelectedDebCode()).execute();
//                            if (pdialog.isShowing()) {
//                                pdialog.cancel();
//                            }
//
//                        } else {
//                            pref.setServerConnectionStatus(false);
//                            ServerAvailability();
//                        }
//                    }else{
//                        pref.setServerConnectionStatus(false);
//                        ServerAvailability();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ReadJsonList> call, Throwable t) {
//                    t.printStackTrace();
//                    pref.setServerConnectionStatus(false);
//                    ServerAvailability();
//                }
//            });
//        }catch (Exception e){
//            Log.d(">>>ERROR Connection",">>>"+e.toString());
//            pref.setServerConnectionStatus(false);
//        }
//    }


}
