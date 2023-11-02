package com.datamation.kfdsfa.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.SalRep;
import com.datamation.kfdsfa.model.apimodel.ReadJsonList;
import com.datamation.kfdsfa.utils.NetworkUtil;
import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.utils.GetMacAddress;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity{
/**modified by rashmi 2020-09-11 kfd online**/
    private ImageView logo;
    DatabaseHelper db;
    private SharedPref pref;
    private NetworkFunctions networkFunctions;
    PermissionManager permissionManager;
    private String TAG = "ActivitySplash";
    private TextView tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View v = layoutInflater.inflate(R.layout.activity_splash, null);
        setContentView(v);
        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);
        db=new DatabaseHelper(getApplicationContext());
        SQLiteDatabase SFA;
        SFA = db.getWritableDatabase();
        pref = SharedPref.getInstance(this);
        db.onUpgrade(SFA, 1, 2);

        logo = (ImageView) findViewById(R.id.logo);
        tryAgain = (TextView) findViewById(R.id.tryAgain);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        networkFunctions = new NetworkFunctions(this);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_up);
        logo.startAnimation(animation1);

        final boolean connectionStatus = NetworkUtil.isNetworkAvailable(ActivitySplash.this);

        GetMacAddress macAddress = new GetMacAddress();
        if (android.os.Build.VERSION.SDK_INT < 23) {
            pref.setMacAddress(macAddress.getMacAddress(getApplicationContext()).toString().replace(":", ""));
        } else {
            pref.setMacAddress(macAddress.getMacAddressNewApi(getApplicationContext()).toString().replace(":", ""));
        }

//        if(pref.getLoginUser()==null) {
//
//        }else{
            if(pref.isLoggedIn()){
                goToHome();
            }else {
                goToLogin();
            }
//        }

    }

    //without console database- implement 2020-02-28 by rashmi for hameedia
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions,grantResults);
    }
    public void reCallActivity(){
        Intent mainActivity = new Intent(ActivitySplash.this, ActivitySplash.class);
        startActivity(mainActivity);
        finish();
    }
    public void goToLogin(){
        Intent mainActivity = new Intent(ActivitySplash.this, ActivityLogin.class);
        startActivity(mainActivity);
        finish();
    }
    public void goToHome(){
        Intent mainActivity = new Intent(ActivitySplash.this, ActivityHome.class);
        startActivity(mainActivity);
        finish();
    }
}
