package com.datamation.kfdsfa.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import com.datamation.kfdsfa.R;
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
import com.datamation.kfdsfa.controller.OutstandingController;
import com.datamation.kfdsfa.controller.PaymentController;
import com.datamation.kfdsfa.controller.PaymentDetailController;
import com.datamation.kfdsfa.controller.PaymentHeaderController;
import com.datamation.kfdsfa.controller.PushMsgHedDetController;
import com.datamation.kfdsfa.controller.ReasonController;
import com.datamation.kfdsfa.controller.RepDetailsController;
import com.datamation.kfdsfa.controller.RouteController;
import com.datamation.kfdsfa.controller.RouteDetController;
import com.datamation.kfdsfa.controller.TownController;
import com.datamation.kfdsfa.controller.TypeController;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.model.Area;
import com.datamation.kfdsfa.model.Bank;
import com.datamation.kfdsfa.model.Brand;
import com.datamation.kfdsfa.model.Control;
import com.datamation.kfdsfa.model.FddbNote;
import com.datamation.kfdsfa.model.FreeDeb;
import com.datamation.kfdsfa.model.FreeDet;
import com.datamation.kfdsfa.model.FreeHed;
import com.datamation.kfdsfa.model.FreeItem;
import com.datamation.kfdsfa.model.FreeMslab;
import com.datamation.kfdsfa.model.Group;
import com.datamation.kfdsfa.model.Item;
import com.datamation.kfdsfa.model.ItemLoc;
import com.datamation.kfdsfa.model.ItemPri;
import com.datamation.kfdsfa.model.Locations;
import com.datamation.kfdsfa.model.Payment;
import com.datamation.kfdsfa.model.PushMsgHedDet;
import com.datamation.kfdsfa.model.Reason;
import com.datamation.kfdsfa.model.ReceiptDet;
import com.datamation.kfdsfa.model.ReceiptHed;
import com.datamation.kfdsfa.model.RepDetail;
import com.datamation.kfdsfa.model.Route;
import com.datamation.kfdsfa.model.RouteDet;
import com.datamation.kfdsfa.model.Town;
import com.datamation.kfdsfa.model.Type;
import com.datamation.kfdsfa.settings.TaskTypeDownload;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.kfdsfa.helpers.SQLiteBackUp;
import com.datamation.kfdsfa.helpers.SQLiteRestore;
import com.datamation.kfdsfa.helpers.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * common functions
 */

public class UtilityContainer {


    //---------------------------------------------------------------------------------------------------------------------------------------------------

    public static void  showSnackBarError(View v, String message, Context context) {
        Snackbar snack = Snackbar.make(v, "" + message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        view.setBackgroundColor(Color.parseColor("#CB4335"));
        TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        snack.show();

    }

  /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public static void mLoadFragment(Fragment fragment, Context context) {

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(R.anim.enter, R.anim.exit_to_right);
        ft.replace(R.id.fragmentContainer, fragment, fragment.getClass().getSimpleName());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }
    protected static void sacaSnackbar (Context context, View view, String s)
    {

    }

    public static void ClearReturnSharedPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("returnKeyRoute");
        editor.remove("returnKeyCostCode");
        editor.remove("returnKeyLocCode");
        editor.remove("returnKeyTouRef");
        editor.remove("returnKeyAreaCode");
        editor.remove("returnKeyRouteCode");
        editor.remove("returnKeyTourPos");
        editor.remove("returnkeyCustomer");
        editor.remove("returnkeyReasonCode");
        editor.remove("returnKeyRepCode");
        editor.remove("returnKeyDriverCode");
        editor.remove("returnKeyHelperCode");
        editor.remove("returnKeyLorryCode");
        editor.remove("returnKeyReason");

        editor.commit();
    }
    public static void ClearReceiptSharedPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("ReckeyPayModePos");
        editor.remove("ReckeyPayMode");
        editor.remove("isHeaderComplete");
        editor.remove("ReckeyHeader");
        editor.remove("ReckeyRecAmt");
        editor.remove("ReckeyRemnant");
        editor.remove("ReckeyCHQNo");
        editor.remove("Rec_Start_Time");
        editor.commit();
    }
    public static void ClearCustomerSharedPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("selected_out_id");
        editor.remove("selected_out_name");
        editor.remove("selected_out_route_code");
        editor.remove("selected_pril_code");
        editor.commit();
    }
    public static void ClearDBName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("Dist_DB");
        editor.commit();
    }



    //-----------------------------------------------------------------------------------------------------------------------------------------------------

    public static void mPrinterDialogbox(final Context context) {

        SharedPref mSharedPref;
        mSharedPref = new SharedPref(context);

        View promptView = LayoutInflater.from(context).inflate(R.layout.settings_printer_layout, null);
        final EditText serverURL = (EditText) promptView.findViewById(R.id.et_mac_address);

        String printer_mac_shared_pref = "";
        printer_mac_shared_pref = new SharedPref(context).getGlobalVal("printer_mac_address");

        if(!TextUtils.isEmpty(printer_mac_shared_pref))
        {
            serverURL.setText(printer_mac_shared_pref);
            Toast.makeText(context, "MAC Address Already Exists", Toast.LENGTH_LONG).show();
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle("Printer MAC Address")
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button bOk = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button bClose = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);



                bOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (serverURL.length() > 0) {

                            if (validate(serverURL.getText().toString().toUpperCase())) {
                                //SharedPreferencesClass.setLocalSharedPreference(context, "printer_mac_address", serverURL.getText().toString().toUpperCase());
                                new SharedPref(context).setGlobalVal("printer_mac_address", serverURL.getText().toString().toUpperCase());
                                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(context, "Enter Valid MAC Address", Toast.LENGTH_LONG).show();
                            }
                        } else
                            Toast.makeText(context, "Type in the MAC Address", Toast.LENGTH_LONG).show();
                    }
                });

                bClose.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public static boolean validate(String mac) {
        Pattern p = Pattern.compile("^([a-fA-F0-9]{2}[:-]){5}[a-fA-F0-9]{2}$");
        Matcher m = p.matcher(mac);
        return m.find();
    }


    public static void mSQLiteDatabase(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.settings_sqlite_database_layout);
        dialog.setTitle("SQLite Backup/Restore");

        final Button b_backups = (Button) dialog.findViewById(R.id.b_backups);
        final Button b_restore = (Button) dialog.findViewById(R.id.b_restore);

        b_backups.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SQLiteBackUp backUp = new SQLiteBackUp(context);
                if (android.os.Build.VERSION.SDK_INT < 23) {
                    backUp.exportDB();
                } else {
                    backUp.exportDBNew();
                }

                dialog.dismiss();
            }
        });

        b_restore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mLoadFragment(new SQLiteRestore(), context);
//                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.main_container, new FragmentTools());
//                ft.addToBackStack(null);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public static void download(final Context context, TaskTypeDownload task , String jsonString) {
        NetworkFunctions networkFunctions = new NetworkFunctions(context);
        JSONObject jsonObject = null;
        int totalRecords = 0;

        try {
            jsonObject = new JSONObject(jsonString);

         } catch (JSONException e) {
            Log.e("JSON ERROR>>>>>",e.toString());
        }
     switch (task) {
         case Controllist: {

             try {
                 JSONArray jsonArray = jsonObject.getJSONArray("data");

                 ArrayList<Control> downloadedList = new ArrayList<Control>();
                 CompanyDetailsController companyController = new CompanyDetailsController(context);
                 for (int i = 0; i < jsonArray.length(); i++) {
                     downloadedList.add(Control.parseControlDetails(jsonArray.getJSONObject(i)));
                 }
                 companyController.createOrUpdateFControl(downloadedList);

             } catch (JSONException | NumberFormatException e) {

                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case Type:{

             try{
                 TypeController typeController = new TypeController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<Type> downloadedlist = new ArrayList<Type>();

                 for(int i = 0; i < jsonArray.length(); i++){
                     downloadedlist.add(Type.parseType(jsonArray.getJSONObject(i)));
                 }
                 typeController.CreateOrUpdateType(downloadedlist);
             }
             catch (JSONException | NumberFormatException e){
                 try {
                     throw e;
                 }catch (JSONException e1){
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case Groups: {

             try {
                 GroupController groupController = new GroupController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<Group> arrayList = new ArrayList<Group>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(Group.parseGroup(jsonArray.getJSONObject(i)));
                 }
                 groupController.CreateOrUpdateGroup(arrayList);

             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case Brand:{

             try{
                 BrandController brandController = new BrandController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<Brand> downloadedlist = new ArrayList<Brand>();

                 for(int i = 0; i < jsonArray.length(); i++){
                     downloadedlist.add(Brand.parseBrand(jsonArray.getJSONObject(i)));
                 }
                 brandController.CreateOrUpodateBrand(downloadedlist);
             }
             catch (JSONException | NumberFormatException e){
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case Items: {

             try {
                 ItemController itemController = new ItemController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<Item> downloadedList = new ArrayList<Item>();

                 for (int i = 0; i < jsonArray.length(); i++) {
                     downloadedList.add(Item.parseItem(jsonArray.getJSONObject(i)));
                 }
                 itemController.InsertOrReplaceItems(downloadedList);


             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case ItemLoc: {

             try {
                 ItemLocController itemLocController = new ItemLocController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<ItemLoc> downloadedList = new ArrayList<ItemLoc>();

                 for (int i = 0; i < jsonArray.length(); i++) {
                     downloadedList.add(ItemLoc.parseItemLocs(jsonArray.getJSONObject(i)));
                 }
                 itemLocController.InsertOrReplaceItemLoc(downloadedList);


             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case ItemPri: {

             try {
                 ItemPriceController itemPriceController = new ItemPriceController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<ItemPri> downloadedList = new ArrayList<ItemPri>();

                 for (int i = 0; i < jsonArray.length(); i++) {
                     downloadedList.add(ItemPri.parseItemPrices(jsonArray.getJSONObject(i)));
                 }
                 itemPriceController.InsertOrReplaceItemPri(downloadedList);


             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case Reason: {

             // Processing reasons
             try {
                 ReasonController reasonController = new ReasonController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");

                 ArrayList<Reason> arrayList = new ArrayList<Reason>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(Reason.parseReason(jsonArray.getJSONObject(i)));
                 }
                 Log.d("befor add reason tbl>>>", arrayList.toString());
                 reasonController.createOrUpdateReason(arrayList);

             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
            break;
            case Bank: {

                // Processing bank
                try {
                    BankController bankController = new BankController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    ArrayList<Bank> arrayList = new ArrayList<Bank>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Bank.parseBank(jsonArray.getJSONObject(i)));
                    }
                    bankController.createOrUpdateBank(arrayList);

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>",e.toString());
                    }
                }
            }
         break;
         case Route:{
             try {
                 RouteController routeController = new RouteController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");

                 ArrayList<Route> arrayList = new ArrayList<Route>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(Route.parseRoute(jsonArray.getJSONObject(i)));
                 }
                 routeController.createOrUpdateFRoute(arrayList);

             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
     }
        break;
            case RouteDet: {

                try {
                    RouteDetController routeDetController = new RouteDetController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<RouteDet> arrayList = new ArrayList<RouteDet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(RouteDet.parseRoute(jsonArray.getJSONObject(i)));
                    }
                    routeDetController.InsertOrReplaceRouteDet(arrayList);

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
         case Area: {

             try {
                 AreaController areaController = new AreaController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<Area> arrayList = new ArrayList<Area>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(Area.parseArea(jsonArray.getJSONObject(i)));
                 }
                 areaController.CreateOrUpdateArea(arrayList);

             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case Locations: {

             try {
                 LocationsController locationsController = new LocationsController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<Locations> arrayList = new ArrayList<Locations>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(Locations.parseLocs(jsonArray.getJSONObject(i)));
                 }
                 locationsController.createOrUpdateFLocations(arrayList);

             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
         case Towns: {

             try {
                 TownController townController = new TownController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<Town> arrayList = new ArrayList<Town>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(Town.parseTown(jsonArray.getJSONObject(i)));
                 }
                 townController.createOrUpdateFTown(arrayList);

             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
            break;
            case Freemslab:{

                try {
                    FreeMslabController freemSlabController = new FreeMslabController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    ArrayList<FreeMslab> arrayList = new ArrayList<FreeMslab>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeMslab.parseFreeMslab(jsonArray.getJSONObject(i)));
                    }
                    freemSlabController.createOrUpdateFreeMslab(arrayList);

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>",e.toString());
                    }
                }
            }
            break;
            case Freehed:{

                try {
                    FreeHedController freeHedController = new FreeHedController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    ArrayList<FreeHed> arrayList = new ArrayList<FreeHed>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeHed.parseFreeHed(jsonArray.getJSONObject(i)));
                    }
                    freeHedController.createOrUpdateFreeHed(arrayList);

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>",e.toString());
                    }
                }
            }
            break;
            case Freedet:{

                try {
                    FreeDetController freeDetController = new FreeDetController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    ArrayList<FreeDet> arrayList = new ArrayList<FreeDet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeDet.parseFreeDet(jsonArray.getJSONObject(i)));
                    }
                    freeDetController.createOrUpdateFreeDet(arrayList);

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>",e.toString());
                    }
                }
            }
            break;
            case Freedeb:{

                try {
                    FreeDebController freeDebController = new FreeDebController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    ArrayList<FreeDeb> arrayList = new ArrayList<FreeDeb>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeDeb.parseFreeDeb(jsonArray.getJSONObject(i)));
                    }
                    freeDebController.createOrUpdateFreeDeb(arrayList);

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>",e.toString());
                    }
                }
            }
            break;
            case Freeitem: {

            try {
                FreeItemController freeItemController = new FreeItemController(context);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                ArrayList<FreeItem> arrayList = new ArrayList<FreeItem>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(FreeItem.parseFreeItem(jsonArray.getJSONObject(i)));
                }
                freeItemController.createOrUpdateFreeItem(arrayList);


            } catch (JSONException | NumberFormatException e) {
                try {
                    throw e;
                } catch (JSONException e1) {
                    Log.e("JSON ERROR>>>>>", e.toString());
                }
            }
        }
            break;
            case fddbnote: {

            try {
                OutstandingController outstandingController = new OutstandingController(context);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                ArrayList<FddbNote> arrayList = new ArrayList<FddbNote>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(FddbNote.parseFddbnote(jsonArray.getJSONObject(i)));
                }
                outstandingController.createOrUpdateFDDbNote(arrayList);

            } catch (JSONException | NumberFormatException e) {
                try {
                    throw e;
                } catch (JSONException e1) {
                    Log.e("JSON ERROR>>>>>", e.toString());
                }
            }
        }
            break;
         case PushMsgHedDet: {

            try {
                PushMsgHedDetController pushMsgHedDetController = new PushMsgHedDetController(context);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                ArrayList<PushMsgHedDet> arrayList = new ArrayList<PushMsgHedDet>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(PushMsgHedDet.parsePMsg(jsonArray.getJSONObject(i)));
                }
                pushMsgHedDetController.createOrUpdatePMsgHedDet(arrayList);
            } catch (JSONException | NumberFormatException e) {
                try {
                    throw e;
                } catch (JSONException e1) {
                    Log.e("JSON ERROR>>>>>", e.toString());
                }
            }
        }
             break;
             case CusPRecHed: {

            try {
                PaymentHeaderController cusPRecHedController = new PaymentHeaderController(context);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                ArrayList<ReceiptHed> arrayList = new ArrayList<ReceiptHed>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(ReceiptHed.parseRecHed(jsonArray.getJSONObject(i)));

                }
                cusPRecHedController.createOrUpdateCusPRecHed(arrayList);
            } catch (JSONException | NumberFormatException e) {
                try {
                    throw e;
                } catch (JSONException e1) {
                    Log.e("JSON ERROR>>>>>", e.toString());
                }
            }
        }
         case CusPRecDet: {

             try {
                 PaymentDetailController cusPRecDetController = new PaymentDetailController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");

                 ArrayList<ReceiptDet> arrayList = new ArrayList<ReceiptDet>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(ReceiptDet.parseRecDet(jsonArray.getJSONObject(i)));

                 }
                 cusPRecDetController.createOrUpdateCusPRecDet(arrayList);

             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
             break;
             case Payments: {

                 try {
                     PaymentController paymentController = new PaymentController(context);
                     JSONArray jsonArray = jsonObject.getJSONArray("data");

                     ArrayList<Payment> arrayList = new ArrayList<Payment>();
                     for (int i = 0; i < jsonArray.length(); i++) {
                         arrayList.add(Payment.parsePayment(jsonArray.getJSONObject(i)));

                     }
                     paymentController.CreateOrUpdatePayments(arrayList);
                 } catch (JSONException | NumberFormatException e) {
                     try {
                         throw e;
                     } catch (JSONException e1) {
                         Log.e("JSON ERROR>>>>>", e.toString());
                     }
                 }
             }
             break;
         case RepDetails: {

             try {
                 RepDetailsController repDetailsController = new RepDetailsController(context);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");

                 ArrayList<RepDetail> arrayList = new ArrayList<RepDetail>();
                 for (int i = 0; i < jsonArray.length(); i++) {
                     arrayList.add(RepDetail.parseRepDetail(jsonArray.getJSONObject(i)));

                 }
                 repDetailsController.CreateOrUpdateRepDetails(arrayList);
             } catch (JSONException | NumberFormatException e) {
                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }

                Thread thread = new Thread() {
                    public void run() {
                        Looper.prepare();//Call looper.prepare()

                        Handler mHandler = new Handler() {
                            public void handleMessage(Message msg) {
                                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                            }
                        };

                        Looper.loop();
                    }
                };
                thread.start();
            }
             break;
             default:
                 break;
         }
    }

}
