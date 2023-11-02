package com.datamation.kfdsfa.api;

import android.content.Context;
import android.util.Log;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.helpers.SharedPref;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rashmi on 7/15/2019.
 */

public class ApiCllient {
    private final String LOG_TAG = ApiCllient.class.getSimpleName();
    private static String baseURL;
    private static SharedPref pref;
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context contextt) {

        //add timouts 2020-03-19 becz sockettimeoutexception by rashmi
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                // .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(4, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES);

        pref = SharedPref.getInstance(contextt);
        String domain = pref.getBaseURL();
        Log.d("baseURL>>>>>>>>>", domain);
        baseURL = domain + contextt.getResources().getString(R.string.connection_string);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create());
        builder.client(httpClient.build());

        if(retrofit == null){
            retrofit = builder.build();
        }
        return retrofit;
    }
}
