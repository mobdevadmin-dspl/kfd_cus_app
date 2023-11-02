package com.datamation.kfdsfa.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.controller.FItenrDetController;
import com.datamation.kfdsfa.controller.FItenrHedController;
import com.datamation.kfdsfa.controller.FreeDebController;
import com.datamation.kfdsfa.controller.FreeDetController;
import com.datamation.kfdsfa.controller.FreeHedController;
import com.datamation.kfdsfa.controller.FreeItemController;
import com.datamation.kfdsfa.controller.FreeMslabController;
import com.datamation.kfdsfa.controller.OutstandingController;
import com.datamation.kfdsfa.controller.RouteController;
import com.datamation.kfdsfa.controller.RouteDetController;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.FddbNote;
import com.datamation.kfdsfa.model.FreeDeb;
import com.datamation.kfdsfa.model.FreeDet;
import com.datamation.kfdsfa.model.FreeHed;
import com.datamation.kfdsfa.model.FreeItem;
import com.datamation.kfdsfa.model.FreeMslab;
import com.datamation.kfdsfa.model.FreeSlab;
import com.datamation.kfdsfa.model.Item;
import com.datamation.kfdsfa.model.Route;
import com.datamation.kfdsfa.model.RouteDet;
import com.datamation.kfdsfa.settings.TaskTypeDownload;
import com.datamation.kfdsfa.utils.NetworkUtil;
import com.datamation.kfdsfa.utils.UtilityContainer;
import com.datamation.kfdsfa.view.ActivityHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragmentCategoryWiseDownload extends Fragment {

    private View view;
    private TextView downItems, downFree, downRoute, downOutstanding, downPrice, downStock, downOthers,downCustomer;
    NetworkFunctions networkFunctions;
    ApiInterface apiInterface;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_download, container, false);

        getActivity().setTitle("Category Wise Download");
        networkFunctions = new NetworkFunctions(getActivity());
        //initializations
        downItems = (TextView) view.findViewById(R.id.items_download);
        downFree = (TextView) view.findViewById(R.id.free_download);
        downRoute = (TextView) view.findViewById(R.id.route_download);
        downOutstanding = (TextView) view.findViewById(R.id.outstanding_download);
        downPrice = (TextView) view.findViewById(R.id.price_download);
        downStock = (TextView) view.findViewById(R.id.stock_download);
        downOthers = (TextView) view.findViewById(R.id.other_download);
        downCustomer = (TextView) view.findViewById(R.id.customer_download);

        downItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {
                            //new itemsDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInItemDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        downFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {

                           // new freeDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInItemDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {
                           // new routeDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInItemDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downOutstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {
                          //  new outstandingDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInOutstanding##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

                if (connectionStatus == true) {
                    if (isAllUploaded(getActivity())) {
                        try {
                        //    new CustomersDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInCustomer ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

                if (connectionStatus == true) {
                    if (isAllUploaded(getActivity())) {
                        try {
                           // new OtherDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInOtherDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });


        //DISABLED BACK NAVIGATION
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("", "keyCode: " + keyCode);
                ActivityHome.navigation.setVisibility(View.VISIBLE);

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getActivity(), "Back button disabled!", Toast.LENGTH_SHORT).show();
                    return true;
                } else if ((keyCode == KeyEvent.KEYCODE_HOME)) {

                    getActivity().finish();

                    return true;

                } else {
                    return false;
                }
            }
        });


        return view;
    }


    private boolean isAllUploaded(Context context) {
        Boolean allUpload = false;


        return allUpload;
    }

    //item download asynctask
    private class itemsDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;

        public itemsDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Items...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    /*****************Item *****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading item data...");
                        }
                    });

                    String item = "";
                    try {
                        item = networkFunctions.getItems(debcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing item
                    try {
                        JSONObject itemJSON = new JSONObject(item);
                        JSONArray itemJSONArray = itemJSON.getJSONArray("fItemsResult");
                        ArrayList<Item> itemList = new ArrayList<Item>();

                        for (int i = 0; i < itemJSONArray.length(); i++) {
                            itemList.add(Item.parseItem(itemJSONArray.getJSONObject(i)));
                        }
                //        itemController.InsertOrReplaceItems(itemList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end item **********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
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

            pdialog.setMessage("Finalizing itembundle data");
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

    //free download asynctask

    private class freeDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;

        public freeDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Free...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    FreeHedController freeHedController = new FreeHedController(getActivity());
                    freeHedController.deleteAll();
                    FreeDetController freedetController = new FreeDetController(getActivity());
                    freedetController.deleteAll();
                    FreeDebController freedebController = new FreeDebController(getActivity());
                    freedebController.deleteAll();
                    FreeItemController freeitemController = new FreeItemController(getActivity());
                    freeitemController.deleteAll();
                    FreeMslabController freeMslabController = new FreeMslabController(getActivity());
                    freeMslabController.deleteAll();

                    /*****************FreeHed**********************************************************************/
                    String freehed = "";
                    try {
                        freehed = networkFunctions.getFreeHed(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }


                    // Processing freehed
                    try {
                        JSONObject freeHedJSON = new JSONObject(freehed);
                        JSONArray freeHedJSONArray = freeHedJSON.getJSONArray("FfreehedResult");
                        ArrayList<FreeHed> freeHedList = new ArrayList<FreeHed>();

                        for (int i = 0; i < freeHedJSONArray.length(); i++) {
                            freeHedList.add(FreeHed.parseFreeHed(freeHedJSONArray.getJSONObject(i)));
                        }
                        freeHedController.createOrUpdateFreeHed(freeHedList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freeHed**********************************************************************/
                    /*****************Freedet**********************************************************************/
                    String freedet = "";
                    try {
                        freedet = networkFunctions.getFreeDet(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    try {
                        JSONObject freedetJSON = new JSONObject(freedet);
                        JSONArray freedetJSONArray = freedetJSON.getJSONArray("FfreedetResult");
                        ArrayList<FreeDet> freedetList = new ArrayList<FreeDet>();

                        for (int i = 0; i < freedetJSONArray.length(); i++) {
                            freedetList.add(FreeDet.parseFreeDet(freedetJSONArray.getJSONObject(i)));
                        }
                        freedetController.createOrUpdateFreeDet(freedetList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freedet**********************************************************************/
                    /*****************freedeb**********************************************************************/
                    String freedeb = "";
                    try {
                        freedeb = networkFunctions.getFreeDebs(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }



                    // Processing freedeb
                    try {
                        JSONObject freedebJSON = new JSONObject(freedeb);
                        JSONArray freedebJSONArray = freedebJSON.getJSONArray("FfreedebResult");
                        ArrayList<FreeDeb> freedebList = new ArrayList<FreeDeb>();

                        for (int i = 0; i < freedebJSONArray.length(); i++) {
                            freedebList.add(FreeDeb.parseFreeDeb(freedebJSONArray.getJSONObject(i)));
                        }
                        freedebController.createOrUpdateFreeDeb(freedebList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freedeb**********************************************************************/
                    /*****************FreeItem*********************************************************************/
                    String freeitem = "";
                    try {
                        freeitem = networkFunctions.getFreeItems();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing freeItem
                    try {
                        JSONObject freeitemJSON = new JSONObject(freeitem);
                        JSONArray freeitemJSONArray = freeitemJSON.getJSONArray("fFreeItemResult");
                        ArrayList<FreeItem> freeitemList = new ArrayList<FreeItem>();

                        for (int i = 0; i < freeitemJSONArray.length(); i++) {
                            freeitemList.add(FreeItem.parseFreeItem(freeitemJSONArray.getJSONObject(i)));
                        }
                        freeitemController.createOrUpdateFreeItem(freeitemList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freeItem**********************************************************************/

                    /*****************Freemslab - kaveesha -11-06-2020 *********************************************************************/
                    String freemslab = "";
                    try {
                        freemslab = networkFunctions.getFreeMslab();
                    } catch (IOException e) {

                        throw e;
                    }

                    // Processing Freemslab
                    try {
                        JSONObject freemslabJSON = new JSONObject(freemslab);
                        JSONArray freemslabJSONArray = freemslabJSON.getJSONArray("fFreeMslabResult");
                        ArrayList<FreeMslab> freeMslabsList = new ArrayList<FreeMslab>();

                        for (int i = 0; i < freemslabJSONArray.length(); i++) {
                            freeMslabsList.add(FreeMslab.parseFreeMslab(freemslabJSONArray.getJSONObject(i)));
                        }
                        freeMslabController.createOrUpdateFreeMslab(freeMslabsList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);
                        throw e;
                    }
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
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

            pdialog.setMessage("Finalizing free data");
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

    //route download asynctask
    private class routeDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;

        public routeDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading routes...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    RouteController routeController = new RouteController(getActivity());
                    routeController.deleteAll();
                    RouteDetController routedetController = new RouteDetController(getActivity());
                    routedetController.deleteAll();
                    FItenrHedController fItenrHedController = new FItenrHedController(getActivity());
                    fItenrHedController.deleteAll();
                    FItenrDetController fItenrDetController = new FItenrDetController(getActivity());
                    fItenrDetController.deleteAll();
                    /*****************route*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route data...");
                        }
                    });

                    String route = "";
                    try {
                        route = networkFunctions.getRoutes(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (routes)...");
                        }
                    });

                    // Processing route
                    try {
                        JSONObject routeJSON = new JSONObject(route);
                        JSONArray routeJSONArray = routeJSON.getJSONArray("fRouteResult");
                        ArrayList<Route> routeList = new ArrayList<Route>();

                        for (int i = 0; i < routeJSONArray.length(); i++) {
                            routeList.add(Route.parseRoute(routeJSONArray.getJSONObject(i)));
                        }
                        routeController.createOrUpdateFRoute(routeList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end route**********************************************************************/
                    /*****************Route det**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Last invoices downloaded\nDownloading route details...");
                        }
                    });

                    String routedet = "";
                    try {
                        routedet = networkFunctions.getRouteDets(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (routes)...");
                        }
                    });

                    // Processing route
                    try {
                        JSONObject routeJSON = new JSONObject(routedet);
                        JSONArray routeJSONArray = routeJSON.getJSONArray("fRouteDetResult");
                        ArrayList<RouteDet> routeList = new ArrayList<RouteDet>();

                        for (int i = 0; i < routeJSONArray.length(); i++) {
                            routeList.add(RouteDet.parseRoute(routeJSONArray.getJSONObject(i)));
                        }
                        routedetController.InsertOrReplaceRouteDet(routeList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end route det**********************************************************************/




                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
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

            pdialog.setMessage("Finalizing item data");
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

    //outstanding download asynctask
    private class outstandingDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;

        public outstandingDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading outstanding...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    OutstandingController outstandingController = new OutstandingController(getActivity());
                    outstandingController.deleteAll();
                    /*****************fDdbNoteWithCondition*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading outstanding data...");
                        }
                    });

                    /*****************fddbnote*****************************************************************************/

                    String fddbnote = "";
                    try {
                        fddbnote = networkFunctions.getFddbNotes(debcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (outstanding details)...");
                        }
                    });

                    // Processing fddbnote
                    try {
                        JSONObject fddbnoteJSON = new JSONObject(fddbnote);
                        JSONArray fddbnoteJSONArray = fddbnoteJSON.getJSONArray("fDdbNoteWithConditionResult");
                        ArrayList<FddbNote> fddbnoteList = new ArrayList<FddbNote>();

                        for (int i = 0; i < fddbnoteJSONArray.length(); i++) {
                            fddbnoteList.add(FddbNote.parseFddbnote(fddbnoteJSONArray.getJSONObject(i)));
                        }
                        outstandingController.createOrUpdateFDDbNote(fddbnoteList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

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
                    //errors.add("Please enter correct username and password");
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

            pdialog.setMessage("Finalizing item data");
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


    //other download - 11-06-2020
    private class OtherDownload extends AsyncTask<String, Integer, Boolean> {

        private String debcode;
        CustomProgressDialog pdialog;
        private Handler mHandler;
        private List<String> errors = new ArrayList<>();

        public OtherDownload(String debcode) {
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
        protected Boolean doInBackground(String... strings) {

            apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
            mHandler = new Handler(Looper.getMainLooper());

            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    /*****************company details**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data(Company details)...");
                        }
                    });

                    // Processing controls
                    try {
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Controllist, networkFunctions.getCompanyDetails(debcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    // ************************reasons**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data (reasons)...");
                        }
                    });
                    // Processing reasons

                    try {
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Reason, networkFunctions.getReasons());
                    } catch (IOException e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************banks**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading banks...");
                        }
                    });

                    // Processing banks
                    try {
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Bank, networkFunctions.getBanks());
                    } catch (IOException e) {
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
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing data");
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
