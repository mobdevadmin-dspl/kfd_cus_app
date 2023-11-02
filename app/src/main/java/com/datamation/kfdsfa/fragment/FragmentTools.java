package com.datamation.kfdsfa.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.api.TaskTypeUpload;
import com.datamation.kfdsfa.controller.AreaController;
import com.datamation.kfdsfa.controller.BankController;
import com.datamation.kfdsfa.controller.BrandController;
import com.datamation.kfdsfa.controller.CompanyDetailsController;
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
import com.datamation.kfdsfa.controller.PaymentDetailController;
import com.datamation.kfdsfa.controller.PaymentHeaderController;
import com.datamation.kfdsfa.controller.PushMsgHedDetController;
import com.datamation.kfdsfa.controller.ReasonController;
import com.datamation.kfdsfa.controller.RouteController;
import com.datamation.kfdsfa.controller.RouteDetController;
import com.datamation.kfdsfa.controller.TownController;
import com.datamation.kfdsfa.controller.TypeController;
import com.datamation.kfdsfa.helpers.SQLiteBackUp;
import com.datamation.kfdsfa.helpers.SQLiteRestore;
import com.datamation.kfdsfa.model.Control;
import com.datamation.kfdsfa.model.Order;
import com.datamation.kfdsfa.model.OrderNew;
import com.datamation.kfdsfa.settings.TaskTypeDownload;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.dialog.StockInquiryDialog;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.helpers.UploadTaskListener;
import com.datamation.kfdsfa.model.SalRep;
import com.datamation.kfdsfa.model.User;
import com.datamation.kfdsfa.model.apimodel.ReadJsonList;
import com.datamation.kfdsfa.upload.UploadPreSales;
import com.datamation.kfdsfa.utils.NetworkUtil;
import com.datamation.kfdsfa.utils.UtilityContainer;
import com.datamation.kfdsfa.view.QuestionsActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***@Auther - rashmi**/

public class FragmentTools extends Fragment implements View.OnClickListener, UploadTaskListener {

    private Context context = getActivity();
    User loggedUser;
    View view;
    int count = 0;
    Animation animScale;
    ImageView imgSync, imgUpload, imgPrinter, imgDatabase, imgStockDown, imgStockInq, imgSalesRep, imgTour, imgDayExp, imgImage, imgVideo;
    NetworkFunctions networkFunctions;
    SharedPref pref;
    List<String> resultList;
    LinearLayout layoutTool;
    private long timeInMillis;
    private Handler mHandler;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    ApiInterface apiInterface;
    ArrayList<Control> downloadList = new ArrayList<>();

    boolean isAnyActiveImages = false;
    boolean isAnyActiveVideos = false;
    boolean isImageFitToScreen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_management_tools, container, false);
        pref = SharedPref.getInstance(getActivity());

        animScale = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale);
        layoutTool = (LinearLayout) view.findViewById(R.id.layoutTool);
        imgTour = (ImageView) view.findViewById(R.id.imgTourInfo);
        imgStockInq = (ImageView) view.findViewById(R.id.imgStockInquiry);
        imgSync = (ImageView) view.findViewById(R.id.imgSync);
        imgUpload = (ImageView) view.findViewById(R.id.imgUpload);
        imgStockDown = (ImageView) view.findViewById(R.id.imgDownload);
        imgPrinter = (ImageView) view.findViewById(R.id.imgPrinter);
        imgDatabase = (ImageView) view.findViewById(R.id.imgSqlite);
        imgSalesRep = (ImageView) view.findViewById(R.id.imgSalrep);
        imgDayExp = (ImageView) view.findViewById(R.id.imgDayExp);
        imgImage = (ImageView) view.findViewById(R.id.imgImage);
        imgVideo = (ImageView) view.findViewById(R.id.imgVideo);
        mHandler = new Handler(Looper.getMainLooper());

//        isAnyActiveImages = new InvDetController(getActivity()).isAnyActiveOrders();
//        isAnyActiveVideos = new ReceiptDetController(getActivity()).isAnyActiveReceipt();
//
//        if (isAnyActiveImages) {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_image));
//        } else {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_image));
//        }
//
//        if (isAnyActiveVideos) {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video));
//        } else {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video));
//        }
        networkFunctions = new NetworkFunctions(getActivity());
        imgTour.setOnClickListener(this);
        imgStockInq.setOnClickListener(this);
        imgSync.setOnClickListener(this);
        imgUpload.setOnClickListener(this);
        imgStockDown.setOnClickListener(this);
        imgPrinter.setOnClickListener(this);
        imgDatabase.setOnClickListener(this);
        imgSalesRep.setOnClickListener(this);
        imgDayExp.setOnClickListener(this);
        imgImage.setOnClickListener(this);
        imgVideo.setOnClickListener(this);
        resultList = new ArrayList<>();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        timeInMillis = System.currentTimeMillis();

        Log.d("FRAGMENT_TOOL", "IMAGE_FLAG: " + pref.getImageFlag());


//        if (fmc.getAllMediaforCheckIfIsExist("IMG") > 0) {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_img_notification));
//        } else {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_image));
 //       }

//        if (fmc.getAllMediaforCheckIfIsExist("VDO") > 0) {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video_notification));
//        } else {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video));
//        }

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgTourInfo:
                imgTour.startAnimation(animScale);
                //UtilityContainer.mLoadFragment(new FragmemtFAQ(), getActivity());
                break;

            case R.id.imgStockInquiry:
                imgStockInq.startAnimation(animScale);//
                new StockInquiryDialog(getActivity());
                break;

            case R.id.imgSync:
                imgSync.startAnimation(animScale);
                Log.d("Validate Secondary Sync", ">>Mac>> " + pref.getMacAddress().trim() + " >>URL>> " + pref.getBaseURL() + " >>DB>> " + pref.getDistDB());
                try {
                    if(NetworkUtil.isNetworkAvailable(getActivity())) {
                        SharedPref sharedPref = SharedPref.getInstance(context);
                       new secondarySync(sharedPref.getSelectedDebCode()).execute();
                      //  new Validate(pref.getMacAddress().trim(), pref.getBaseURL(), pref.getDistDB()).execute();
                    }else{
                        Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Log.e(">>>> Secondary Sync",e.toString());
                }
                break;

            case R.id.imgUpload:
                imgUpload.startAnimation(animScale);
                syncDialog(getActivity());
                break;

            case R.id.imgDownload:
                imgStockDown.startAnimation(animScale);
            //    dataSyncDialog(getActivity());
                break;

            case R.id.imgPrinter:
                imgPrinter.startAnimation(animScale);
                UtilityContainer.mPrinterDialogbox(getActivity());
                break;

            case R.id.imgSqlite:
                imgDatabase.startAnimation(animScale);
                UtilityContainer.mSQLiteDatabase(getActivity());
                break;

            case R.id.imgSalrep:
                imgSalesRep.startAnimation(animScale);
                ViewRepProfile();
                break;

            case R.id.imgDayExp:
                imgDayExp.startAnimation(animScale);
                break;

            case R.id.imgImage:
                imgImage.startAnimation(animScale);
               // imgUrlList = fmc.getAllMediafromDb("IMG");
               // ViewImageList();
                break;

            case R.id.imgVideo:
               // imgVideo.startAnimation(animScale);
               // vdoUrlList = fmc.getAllMediafromDb("VDO");
                UtilityContainer.mPrinterDialogbox(getActivity());
                //ViewVideoList();
                break;

        }

    }


    public void ViewRepProfile() {}

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    private void syncDialog(final Context context) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Are you sure, Do you want to Upload Data?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        boolean connectionStatus = NetworkUtil.isNetworkAvailable(context);
                        if (connectionStatus == true) {

                            ArrayList<OrderNew> orderList = new OrderController(getActivity()).getAllUnSyncOrdHed();

                            if (orderList.size() <= 0 )
                            {
                                Toast.makeText(getActivity(), "No Records to upload !", Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    //new UploadPreSales(getActivity(), FragmentTools.this, TaskTypeUpload.UPLOAD_ORDER).execute(orderList);
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

    public void mDevelopingMessage(String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.info);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void syncMasterDataDialog(final Context context) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Are you sure, Do you want to Sync Master Data?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                        if (connectionStatus == true) {
                            if (isAllUploaded()) {
                                dialog.dismiss();
                                try {
                                    // new secondarySync(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                                    SharedPref.getInstance(getActivity()).setGlobalVal("SyncDate", dateFormat.format(new Date(timeInMillis)));
                                } catch (Exception e) {
                                    Log.e("## ErrorIn2ndSync ##", e.toString());
                                }
                            } else {
                                Toast.makeText(context, "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
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

    private boolean isAllUploaded() {
        Boolean allUpload = false;



        return allUpload;
    }

    @Override
    public void onTaskCompleted(TaskTypeUpload taskType, List<String> list) {}

    @Override
    public void onTaskCompleted(List<String> list) {

    }



    //**********************secondary sysnc start***********************************************/
     private class secondarySync extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String debcode;
        private List<String> errors = new ArrayList<>();
        private Handler mHandler;

        public secondarySync(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
            mHandler = new Handler(Looper.getMainLooper());
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            mHandler = new Handler(Looper.getMainLooper());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }
        @Override
        protected Boolean doInBackground(String... arg0) {
            apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
            mHandler = new Handler(Looper.getMainLooper());
            try {
              if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    new CompanyDetailsController(getActivity()).deleteAll();

/*****************company details**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data(Company details)...");
                        }
                    });
                  // Processing controls
                  try{
                      UtilityContainer.download(getActivity(),TaskTypeDownload.Controllist, networkFunctions.getCompanyDetails(debcode));
                  } catch (Exception e) {
                      errors.add(e.toString());
                      throw e;
                  }

                  /***************** Types - kaveesha - 2020/10/06 *************************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Types....");
                      }
                  });

                  // Processing Types
                  try{
                      TypeController typeController = new TypeController(getActivity());
                      typeController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.Type, networkFunctions.getTypes());
                  } catch (Exception e) {
                      errors.add(e.toString());
                      throw e;
                  }


                    /*****************Items*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (item details)...");
                        }
                    });

                    // Processing item
                    try{
                       ItemController itemController = new ItemController(getActivity());
                        itemController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Items, networkFunctions.getItems(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                  /*****************ItemLoc*****************************************************************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Processing downloaded data (item loc details)...");
                      }
                  });

                  // Processing item loc
                  try{
                      ItemLocController itemLocController = new ItemLocController(getActivity());
                      itemLocController.deleteAllItemLoc();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.ItemLoc, networkFunctions.getItemLocations(debcode));
                  } catch (Exception e) {
                      errors.add(e.toString());
                      throw e;
                  }

                  /*****************reasons**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Items downloaded\nDownloading reasons...");
                        }
                    });
                    // Processing reasons

                   try {
                       ReasonController reasonController = new ReasonController(getActivity());
                       reasonController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Reason, networkFunctions.getReasons());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************ItemPri**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Items downloaded\nDownloading Item Pri...");
                        }
                    });
                    // Processing Item Pri

                    try {
                        ItemPriceController itemPriceController = new ItemPriceController(getActivity());
                        itemPriceController.deleteAllItemPri();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.ItemPri, networkFunctions.getItemPrices(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading banks...");
                        }
                    });
                    /*****************banks**********************************************************************/

                    try {
                        BankController bankController = new BankController(getActivity());
                        bankController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Bank, networkFunctions.getBanks());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Route**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route details...");
                        }
                    });
                    // Processing route

                    try {
                        RouteController routeController = new RouteController(getActivity());
                        routeController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Route, networkFunctions.getRoutes(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************last 3 invoice heds**********************************************************************/

//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            pdialog.setMessage("Processing downloaded data (last invoices)...");
//                        }
//                    });
//                    // Processing lastinvoiceheds
//                    try {
//                        Call<ReadJsonList> resultCall = apiInterface.getLastThreeInvHedResult(pref.getDistDB(),debcode);
//                        resultCall.enqueue(new Callback<ReadJsonList>() {
//                            @Override
//                            public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
//                                FInvhedL3Controller invoiceHedController = new FInvhedL3Controller(getActivity());
//                                invoiceHedController.deleteAll();
//                                ArrayList<FInvhedL3> invoiceHedList = new ArrayList<FInvhedL3>();
//                                if(response.body() != null) {
//
//                                    for (int i = 0; i < response.body().getLastThreeInvHedResult().size(); i++) {
//                                        invoiceHedList.add(response.body().getLastThreeInvHedResult().get(i));
//                                    }
//                                    invoiceHedController.createOrUpdateFinvHedL3(invoiceHedList);
//                                }else{
//                                    errors.add("LastThreeInvHed response is null");
//                                }
//                            }
//                            @Override
//                            public void onFailure(Call<ReadJsonList> call, Throwable t) {
//                                errors.add(t.toString());
//                            }
//                        });
//                    } catch (Exception e) {
//                        errors.add(e.toString());
//                        throw e;
//                    }
                    /*****************end lastinvoiceheds**********************************************************************/
                    /*****************last 3 invoice dets**********************************************************************/
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            pdialog.setMessage("Processing downloaded data (invoices)...");
//                        }
//                    });
//                    // Processing lastinvoiceheds
//                    try {
//                        Call<ReadJsonList> resultCall = apiInterface.getLastThreeInvDetResult(pref.getDistDB(),debcode);
//                        resultCall.enqueue(new Callback<ReadJsonList>() {
//                            @Override
//                            public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
//                                FinvDetL3Controller invoiceDetController = new FinvDetL3Controller(getActivity());
//                                invoiceDetController.deleteAll();
//                                ArrayList<FinvDetL3> invoiceDetList = new ArrayList<FinvDetL3>();
//                                if(response.body() != null) {
//                                    for (int i = 0; i < response.body().getLastThreeInvDetResult().size(); i++) {
//                                        invoiceDetList.add(response.body().getLastThreeInvDetResult().get(i));
//                                    }
//                                    invoiceDetController.createOrUpdateFinvDetL3(invoiceDetList);
//                                }else{
//                                    errors.add("LastThreeInvDet response is null");
//                                }
//                            }
//                            @Override
//                            public void onFailure(Call<ReadJsonList> call, Throwable t) {
//                                errors.add(t.toString());
//                            }
//                        });
//                    } catch (Exception e) {
//                        errors.add(e.toString());
//
//                        throw e;
//                    }

                    /*****************Route det**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route details...");
                        }
                    });

                    // Processing route
                    try {
                        RouteDetController routeDetController = new RouteDetController(getActivity());
                        routeDetController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.RouteDet, networkFunctions.getRouteDets(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                  /***************** Area - kaveesha - 2020/10/05 ****************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Area Details...");
                      }
                  });

                  // Processing area
                  try {
                      AreaController areaController = new AreaController(getActivity());
                      areaController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.Area, networkFunctions.getArea());
                  } catch (IOException e) {
                      e.printStackTrace();
                      errors.add(e.toString());
                      throw e;
                  }

                  /***************** Locations - kaveesha - 2020/10/05 *****************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Locations Details...");
                      }
                  });

                  // Processing locations
                  try {
                      LocationsController locationsController = new LocationsController(getActivity());
                      locationsController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.Locations, networkFunctions.getLocations(debcode));
                  } catch (IOException e) {
                      e.printStackTrace();
                      errors.add(e.toString());
                      throw e;
                  }


                  /*****************Towns - kaveesha - 2020/10/05 *******************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Towns...");
                      }
                  });

                  // Processing towns
                  try {
                      TownController townController = new TownController(getActivity());
                      townController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.Towns, networkFunctions.getTowns());
                  } catch (IOException e) {
                      e.printStackTrace();
                      errors.add(e.toString());
                      throw e;
                  }

                  /***************** Groups - kaveesha - 2020/10/05 *****************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Groups...");
                      }
                  });

                  // Processing group
                  try {
                      GroupController groupController = new GroupController(getActivity());
                      groupController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.Groups, networkFunctions.getGroups());
                  } catch (IOException e) {
                      e.printStackTrace();
                      errors.add(e.toString());
                      throw e;
                  }

                  /***************** Brands - kaveesha - 2020/10/06 *****************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Brands...");
                      }
                  });

                  // Processing brands
                  try {
                      BrandController brandController = new BrandController(getActivity());
                      brandController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.Brand, networkFunctions.getBrands());
                  } catch (IOException e) {
                      e.printStackTrace();
                      errors.add(e.toString());
                      throw e;
                  }


//                    /*****************Freeslab**********************************************************************/
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            pdialog.setMessage("Processing downloaded data (free)...");
//                        }
//                    });
//                    // Processing freeslab
//                    try {
//
//                        FreeSlabController freeSlabController = new FreeSlabController(getActivity());
//                        freeSlabController.deleteAll();
//                        UtilityContainer.download(getActivity(),TaskTypeDownload.Freeslab, networkFunctions.getFreeSlab());
//                    } catch (Exception e) {
//                        errors.add(e.toString());
//                        throw e;
//                    }

                    /*****************freeMslab**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });
                    // Processing freeMslab
                    try {

                        FreeMslabController freemSlabController = new FreeMslabController(getActivity());
                        freemSlabController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Freemslab, networkFunctions.getFreeMslab());
                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************FreeHed**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });
                    // Processing freehed
                    try {

                        FreeHedController freeHedController = new FreeHedController(getActivity());
                        freeHedController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Freehed, networkFunctions.getFreeHed(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************Freedet**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });

                    // Processing freedet
                    try {

                        FreeDetController freeDetController = new FreeDetController(getActivity());
                        freeDetController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Freedet, networkFunctions.getFreeDet(debcode));

                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************freedeb**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });

                    // Processing freedeb
                    try {

                        FreeDebController freeDebController = new FreeDebController(getActivity());
                        freeDebController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Freedeb, networkFunctions.getFreeDebs(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************freeItem**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });


                    // Processing freeItem
                    try {

                        FreeItemController freeItemController = new FreeItemController(getActivity());
                        freeItemController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.Freeitem, networkFunctions.getFreeItems());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Outsatnding...");
                        }
                    });

                    // Processing fddbnote
                    try {
                        OutstandingController outstandingController = new OutstandingController(getActivity());
                        outstandingController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.fddbnote, networkFunctions.getFddbNotes(debcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Push Msg HedDet  - kaveesha - 16-09-2020**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Push Msg Hed Det....");
                        }
                    });
                    // Processing Push Msg HedDet

                    try {
                        PushMsgHedDetController pushMsgHedDetController = new PushMsgHedDetController(getActivity());
                        pushMsgHedDetController.deleteAll();
                        UtilityContainer.download(getActivity(),TaskTypeDownload.PushMsgHedDet, networkFunctions.getPushMsgHedDet(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                  /*****************CusPRecHed  - kaveesha - 22-09-2020**********************************************************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Cusp RecHed ....");
                      }
                  });
                  // Processing Cusp RecHed

                  try {
                      PaymentHeaderController paymentHeaderController = new PaymentHeaderController(getActivity());
                      paymentHeaderController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.CusPRecHed, networkFunctions.getCusPRecHed(debcode));
                  } catch (Exception e) {
                      errors.add(e.toString());
                      throw e;
                  }

                  /*****************CusPRecDet  - kaveesha - 22-09-2020**********************************************************************/
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          pdialog.setMessage("Downloading Cusp RecDet ....");
                      }
                  });
                  // Processing Cusp RecDet

                  try {
                      PaymentDetailController paymentDetailController = new PaymentDetailController(getActivity());
                      paymentDetailController.deleteAll();
                      UtilityContainer.download(getActivity(),TaskTypeDownload.CusPRecDet, networkFunctions.getCusPRecDet(debcode));
                  } catch (Exception e) {
                      errors.add(e.toString());
                      throw e;
                  }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Completed...");
                        }
                    });

                    /*****************end sync**********************************************************************/
                return true;
                } else {
                    errors.add("SharedPref.getInstance(getActivity()).getLoginUser() = null OR !SharedPref.getInstance(getActivity()).isLoggedIn()");
                    Log.d("ERROR>>>>>", "Login USer" + SharedPref.getInstance(getActivity()).getLoginUser().toString() + " IS LoggedIn --> " + SharedPref.getInstance(getActivity()).isLoggedIn());
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
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
                showErrorText("Successfully Synchronized");
            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
                StringBuilder sb = new StringBuilder();
                if (errors.size() == 1) {
                    sb.append(errors.get(0));
                    showErrorText(sb.toString());
                } else if(errors.size() == 0) {
                    sb.append("Following errors occurred");
                    for (String error : errors) {
                        sb.append("\n - ").append(error);
                        showErrorText(sb.toString());
                    }
                }
                //showErrorText(sb.toString());
            }

        }
    }

    private void showErrorText(String s) {
        Toast.makeText(getActivity(), "" + s, Toast.LENGTH_LONG).show();

    }

    /////////////***********************secondory sync finish***********************************/
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public void mUploadResult(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Upload Summary");

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private class Validate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String macId, url, db;

        public Validate(String macId, String url, String db) {
            this.macId = macId;
            this.url = url;
            this.db = db;
            this.pdialog = new CustomProgressDialog(getActivity());
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

                try {
                    ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                    Call<ReadJsonList> resultCall = apiInterface.getSalRepResult(pref.getDistDB(),macId);
                    resultCall.enqueue(new Callback<ReadJsonList>() {
                        @Override
                        public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
                            ArrayList<SalRep> repList = new ArrayList<SalRep>();
                            for (int i = 0; i < response.body().getSalRepResult().size(); i++) {
                                repList.add(response.body().getSalRepResult().get(i));
                            }

                            if(repList.size()>0){
//                                networkFunctions.setUser(repList.get(0));
//                                pref.storeLoginUser(repList.get(0));
                            }

                        }

                        @Override
                        public void onFailure(Call<ReadJsonList> call, Throwable t) {
                            Log.d(">>>Error in failure",t.toString());
                        }
                    });

                        pref = SharedPref.getInstance(getActivity());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pdialog.setMessage("Authenticated...");
                            }
                        });

                        return true;

                } catch (Exception e) {
                    Log.e("networkFunctions ->", "IOException -> " + e.toString());
                    throw e;
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pdialog.isShowing())
                pdialog.cancel();
            // pdialog.cancel();
            if (result) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                syncMasterDataDialog(getActivity());
            } else {
                Toast.makeText(getActivity(), "Invalid Mac Id", Toast.LENGTH_LONG).show();
            }
        }
    }

}
