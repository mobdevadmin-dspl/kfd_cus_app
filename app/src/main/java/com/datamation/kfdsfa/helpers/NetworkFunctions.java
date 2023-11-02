package com.datamation.kfdsfa.helpers;

import android.content.Context;
import android.util.Log;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.controller.DashboardController;
import com.datamation.kfdsfa.model.CustomNameValuePair;
import com.datamation.kfdsfa.model.Debtor;
import com.datamation.kfdsfa.model.SalRep;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * /***@Auther - rashmi
 * Created by Rashmi on 1/11/2018.
 * Handles network based functions.
 */
public class NetworkFunctions {

    private final String LOG_TAG = NetworkFunctions.class.getSimpleName();

    private final SharedPref pref;

    private Context context;

    /**
     * The base URL to POST/GET the parameters to. The function names will be appended to this
     */
    private String baseURL;

    private Debtor user;
    private String dbname;

    public NetworkFunctions(Context contextt) {
        this.context = contextt;
        pref = SharedPref.getInstance(context);
        String domain = pref.getBaseURL();
        Log.d("baseURL>>>>>>>>>", domain);
        baseURL = domain + context.getResources().getString(R.string.connection_string);

        // Log.d(LOG_TAG, "testing : " + baseURL + "login" + restOfURL);
        user = pref.getLoginUser();
    }


    public void setUser(Debtor user) {
        this.user = user;
    }
//with console database - commented by rashmi - 2020-02-28
//    public String validate(Context context, String macId, String url, String db) throws IOException {
//
//        List<CustomNameValuePair> params = new ArrayList<>();
//
//        Log.d(LOG_TAG, "Validating : " + url + context.getResources().getString(R.string.connection_string) + "fSalRep" + restOfURL + "/" + macId);
//
//        return getFromServer(url + context.getResources().getString(R.string.connection_string) + "fSalRep/mobile123/" + db + "/" + macId, params);
//    }
//without console database -  by rashmi - 2020-02-28
    public String validate(Context context,String macId, String url) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();
        Log.d(LOG_TAG, "Validating : " + url + context.getResources().getString(R.string.connection_string) + "fSalRep" + "/" + macId);

        return getFromServer(url + context.getResources().getString(R.string.connection_string) + "fSalRep" + "/" + macId);

    }
    /**
     * This function will POST repCode will return a the response JSON
     * from the server.
     *
     * @param repCode The string of the logged user's code
     * @return The response as a String
     * @throws IOException Throws if unable to reach the server
     */

    public String getCompanyDetails(String repCode) throws IOException {

        Log.d(LOG_TAG, "Getting company details : " + baseURL + "fControl");

        return getFromServer(baseURL + "fControl");

    }
//    public String getCompanyDetails(String repCode) throws IOException {
//
//        List<CustomNameValuePair> params = new ArrayList<>();
//
//        Log.d(LOG_TAG, "Getting company details : " + baseURL + "fControl" + restOfURL + params);
//
//        return getFromServer(baseURL + "fControl" + restOfURL, params);
//
//    }

    public String getCustomer(String cusId,String otp) throws IOException {

        Log.d(LOG_TAG, "Getting customer : " + baseURL + "fDebtor" + "/" + cusId +  "/" + otp);

        return getFromServer(baseURL + "fDebtor" +  "/" + cusId + "/" + otp);

    }

    //---------------------------kaveesha--------06/10/2020-----------------------------To get Types---------------------
    public String getTypes() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fType : " + baseURL + "fType" );

        return getFromServer(baseURL + "fType" );

    }
    public String isConfirmed(String orderId) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting order confirmation : " + baseURL + "IsOrderConfirmed" );

        return getFromServer(baseURL + "IsOrderConfirmed" +  "/" + orderId);

    }

    public String getItemLocations(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting itemlocs : " + baseURL + "fItemLoc" +  "/" + debcode);

        return getFromServer(baseURL + "fItemLoc" + "/" + debcode);

    }

    public String getItemPrices(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fItemPri : " + baseURL + "fItemPri" +  "/" + debcode );

        return getFromServer(baseURL + "fItemPri" + "/" + debcode);

    }

    public String getItems(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fItems : " + baseURL + "fItem" +  "/" + debcode );

        //return getFromServer(baseURL + "Fitems" + restOfURL + "/" + debcode, params);
        return getFromServer(baseURL + "fItem" +  "/" + debcode);

    }

    public String getLocations(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fLocations : " + baseURL + "fLocation"  );

        return getFromServer(baseURL + "fLocation");

    }


    public String getReferences(String repCode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting FCompanyBranch : " + baseURL + "FCompanyBranch" + "/" + repCode );

        return getFromServer(baseURL + "FCompanyBranch"  + repCode);
    }

    public String getReferenceSettings() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fCompanySetting : " + baseURL + "fCompanySetting" );

        return getFromServer(baseURL + "fCompanySetting" );
    }

    public String getReasons() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting freason  : " + baseURL + "fReason" );

        return getFromServer(baseURL + "fReason" );
    }

    public String getFreeSlab() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fFreeslab   : " + baseURL + "fFreeslab  " );

        return getFromServer(baseURL + "fFreeslab" );
    }

    public String getFreeDet(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting Ffreedet : " + baseURL + "fFreeDet" + "/" + debcode );

        return getFromServer(baseURL + "fFreeDet" + "/" + debcode);
    }

    public String getFreeDebs(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting Ffreedeb : " + baseURL + "fFreeDeb" + "/" + debcode );

        return getFromServer(baseURL + "fFreeDeb" + "/" + debcode);
    }

    public String getFreeItems() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting ffreeitem : " + baseURL + "fFreeItem" );

        return getFromServer(baseURL + "fFreeItem" );
    }

    //--------------kaveesha-----------2020/10/05--------------To get Groups--------------------------------------------------------------
    public String getGroups() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fGroup : " + baseURL + "fGroup" );

        return getFromServer(baseURL + "fGroup");
    }

    //--------------kaveesha-----------2020/10/05--------------To get Brands--------------------------------------------------------------
    public String getBrands() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fbrand : " + baseURL + "fBrand" );

        return getFromServer(baseURL + "fBrand");
    }

    public String getBanks() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fbank : " + baseURL + "fBank");

        return getFromServer(baseURL + "fBank");
    }

    public String getFreeMslab() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting ffreemslab : " + baseURL + "fFreeMsLab" );

        return getFromServer(baseURL + "fFreeMsLab");
    }

    public String getFreeHed(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting FreeHed : " + baseURL + "fFreeHed" + "/" + debcode );

        return getFromServer(baseURL + "fFreeHed" + "/" + debcode);
    }

    public String getRoutes(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting froute : " + baseURL + "fRoute");

        return getFromServer(baseURL + "fRoute" );
    }

    public String getRouteDets(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting froutedet : " + baseURL + "fRouteDet" );

        return getFromServer(baseURL + "fRouteDet" );
    }

    public String getTowns() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting ftown : " + baseURL + "fTown" );

        return getFromServer(baseURL + "fTown" );
    }


    public String getFddbNotes(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fDdbNoteWithCondition : " + baseURL + "FDdbNote" +  "/" + debcode );

        return getFromServer(baseURL + "FDdbNote" +  "/" + debcode);
    }

   //-------------------------------------Kaveesha--------------2020-10-05---------To Get Area-----------------------------------------
    public String getArea() throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting fArea : " + baseURL + "fArea" );

        return getFromServer(baseURL + "fArea" );
    }

    //----------------------------------kaveesha------------2020-09-16---------To get Push Msg -----------------------------------------
    public String getPushMsgHedDet(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting PushMsgHedDet : " + baseURL + "fPushMsgHedDet" +  "/" + debcode );

        return getFromServer(baseURL + "fPushMsgHedDet" + "/" + debcode);
    }

    //----------------------------------kaveesha------------2020-09-22--------------------------------------------------
    public String getCusPRecDet(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting FCUSPRecDet : " + baseURL + "fCusPRecDet" + "/" + debcode);

        return getFromServer(baseURL + "fCusPRecDet" +  "/" + debcode);
    }

    public String getCusPRecHed(String debcode) throws IOException {//kaveeesha ---------22-09-2020------------------

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting FCUSPRecDet : " + baseURL + "fCusPRecHed" + "/" + debcode );

        return getFromServer(baseURL + "fCusPRecHed" + "/" + debcode);
    }

    //----------------------------------kaveesha------------2020-11-10--------------------------------------------------
    public String getPayments(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting CusPayments : " + baseURL + "FCusPayment" + "/" + debcode );

        return getFromServer(baseURL + "FCusPayment" +  "/" + debcode);
    }

    //----------------------------------kaveesha------------2020-11-18--------------------------------------------------
    public String getOrderStatus(long orderNo , String status) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting CheckOrderStatus : " + baseURL + "CheckOrderStatus" +  "/" + orderNo + "/" + status );

        return getFromServer(baseURL + "CheckOrderStatus" + "/" + orderNo + "/" + status);
    }

    //----------------------------------kaveesha------------2020-12-16--------------------------------------------------
    public String getRepDetails(String debcode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting getDebtorReps : " + baseURL + "FDebtorRep" + "/" + debcode );

        return getFromServer(baseURL + "FDebtorRep" + "/" + debcode);
    }



    public String getLastThreeInvHed(String repCode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting RepLastThreeInvHed : " + baseURL + "RepLastThreeInvHed" +  "/" + repCode );

        return getFromServer(baseURL + "RepLastThreeInvHed" + "/" + repCode);
    }

    public String getLastThreeInvDet(String repCode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();

        Log.d(LOG_TAG, "Getting RepLastThreeInvDet : " + baseURL + "RepLastThreeInvDet" + "/" + repCode );

        return getFromServer(baseURL + "RepLastThreeInvDet" + "/" + repCode);
    }

    public String syncInvoice() {
        return baseURL + "insertFInvHed";
    }
    public String syncDeletedInvoice() {
        return baseURL + "insertDeletedInvoices";
    }
    public String syncReceipt() {
        return baseURL + "insertFrecHed";
    }

    public String syncOrder() {
        return baseURL + "insertFOrdHed";
    }

    public String syncSalesReturn() {
        return baseURL;
    }

    public String syncNonProductive() {
        return baseURL + "insertFDaynPrdHed";
    }

    public String syncNewCustomers() {
        return baseURL + "insertCustomer";
    }

    public String syncDebtor() {
        return baseURL + "updateDebtorCordinates";
    }

    public String syncDebtorImgUpd() {
        return baseURL + "updateDebtorImageURL";
    }

    public String syncEditedDebtors() {
        return baseURL + "updateEditedDebtors";
    }

    public String syncAttendance() {
        return baseURL + "insertTourInfo";
    }

    public String syncFirebasetoken() {
        return baseURL + "updateFirebaseTokenID";
    }

    public String syncEmailUpdatedSalrep() {
        return baseURL + "updateEmailUpdatedSalRep";
    }

    public String syncDayExp() {
        return baseURL + "insertDayExpense";
    }

    public String fetchOrderDetails(long invoiceId) throws IOException {
        List<CustomNameValuePair> params = new ArrayList<>();
        //params.add(new CustomNameValuePair("position_id", String.valueOf(user.getLocationId())));
        params.add(new CustomNameValuePair("sales_order_code", String.valueOf(invoiceId)));

        Log.d(LOG_TAG, "Fetching order details");

        return postToServer(baseURL + "get_invoice_details", params);
    }

    /**
     * This function POSTs params to server and gets the response.
     *
     * @param url    The URL to POST to
     * @param params The list of {@link CustomNameValuePair} of params to POST
     * @return The response from the server as a String
     * @throws IOException Throws if unable to connect to the server
     */
    private String postToServer(String url, List<CustomNameValuePair> params) throws IOException {

        String response = "";

        URL postURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) postURL.openConnection();
        con.setConnectTimeout(60 * 1000);
        con.setReadTimeout(30 * 1000);
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(generatePOSTParams(params));
        writer.flush();
        writer.close();
        os.close();

        con.connect();

        int status = con.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                response = sb.toString();
                Log.d(LOG_TAG, "Server Response : \n" + response);
        }

        return response;
    }
//    private String postToServer(String url, List<CustomNameValuePair> params) throws IOException {
//
//        String response = "";
//
//        URL postURL = new URL(url);
//        HttpsURLConnection con = (HttpsURLConnection) postURL.openConnection();
//        // Create the SSL connection
//        SSLContext sc = null;
//        try {
//            sc = SSLContext.getInstance("TLS");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            sc.init(null, null, new java.security.SecureRandom());
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        con.setSSLSocketFactory(sc.getSocketFactory());
//
//        // Use this if you need SSL authentication
//       // String userpass = user + ":" + password;
//       // String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
//       // con.setRequestProperty("Authorization", basicAuth);
//        con.setConnectTimeout(60 * 1000);
//        con.setReadTimeout(30 * 1000);
//        con.setRequestMethod("POST");
//        con.setDoInput(true);
//        con.setDoOutput(true);
//
//        OutputStream os = con.getOutputStream();
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//        writer.write(generatePOSTParams(params));
//        writer.flush();
//        writer.close();
//        os.close();
//
//        con.connect();
//
//        int status = con.getResponseCode();
//        switch (status) {
//            case 200:
//            case 201:
//                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line).append("\n");
//                }
//                br.close();
//
//                response = sb.toString();
//                Log.d(LOG_TAG, "Server Response : \n" + response);
//        }
//
//        return response;
//    }
//

    /**
     * This function GETs params to server and returns the response.
     *
     * @param url    The URL to GET from
     * @param params The List<CustomNameValuePair></> of params to encode as GET parameters
     * @return The response string from the server
     * @throws IOException Throws if unable to connect to the server
     */
    private String getFromServer(String url) throws IOException {

        String response = "";


        URL postURL = new URL(url);
//        Log.d(LOG_TAG, postURL.toString());
        HttpURLConnection con = (HttpURLConnection) postURL.openConnection();
        con.setConnectTimeout(60 * 1000);
        con.setReadTimeout(30 * 1000);
        con.setRequestMethod("GET");
        con.setDoInput(true);
//        con.setDoOutput(true);

        con.connect();

        int status = con.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                response = sb.toString();
                Log.d(LOG_TAG, "Server Response : \n" + response);
        }

        return response;
    }

    /**
     * This function will return the params as a queried String to POST to the server
     *
     * @param params The parameters to be POSTed
     * @return The formatted String
     * @throws UnsupportedEncodingException
     */
    private String generatePOSTParams(List<CustomNameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (CustomNameValuePair pair : params) {
            if (pair != null) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
        }

        Log.d(LOG_TAG, "Server REQUEST : " + result.toString());
        Log.d(LOG_TAG, "Upload size : " + result.toString().getBytes().length + " bytes");

        return result.toString();
    }

    /**
     * This function will return the params as a queried String to GET from the server
     *
     * @param params The parameters to encode as GET params
     * @return The formatted String
     */
    private String generateGETParams(List<CustomNameValuePair> params) {

        StringBuilder result = new StringBuilder().append("");
        boolean first = true;

        if (params != null) {
            for (CustomNameValuePair pair : params) {
                if (pair != null) {
                    if (first) {
                        first = false;
                        result.append("?");
                    } else
                        result.append("&");

                    result.append(pair.getName());
                    result.append("=");
                    result.append(pair.getValue());
                }
            }
        }

        Log.d(LOG_TAG, "Upload size : " + result.toString().getBytes().length + " bytes");

        return result.toString();
    }

    public static boolean mHttpManager(String url, String sJsonObject) throws Exception {
        Log.v(url + "## Json ##", sJsonObject);
        HttpPost requestfDam = new HttpPost(url);
        StringEntity entityfDam = new StringEntity(sJsonObject, "UTF-8");
        entityfDam.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        entityfDam.setContentType("application/json");
        requestfDam.setEntity(entityfDam);
        DefaultHttpClient httpClientfDamRec = new DefaultHttpClient();

        HttpResponse responsefDamRec = httpClientfDamRec.execute(requestfDam);
        HttpEntity entityfDamEntity = responsefDamRec.getEntity();
        InputStream is = entityfDamEntity.getContent();
        try {
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                is.close();
                String result = sb.toString();
                String result_fDamRec = result.replace("\"", "");
                Log.e("response", "connect:" + result_fDamRec);
                if (result_fDamRec.trim().equals("200"))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean mHttpManagerInvoice(String url, String sJsonObject) throws Exception {
        Log.v(url + "## Json ##", sJsonObject);
        HttpPost requestfDam = new HttpPost(url);
        StringEntity entityfDam = new StringEntity(sJsonObject, "UTF-8");
        entityfDam.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        entityfDam.setContentType("application/json");
        requestfDam.setEntity(entityfDam);
        DefaultHttpClient httpClientfDamRec = new DefaultHttpClient();

        HttpResponse responsefDamRec = httpClientfDamRec.execute(requestfDam);
        HttpEntity entityfDamEntity = responsefDamRec.getEntity();
        InputStream is = entityfDamEntity.getContent();
        try {
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                is.close();
                String result = sb.toString();
                String result_fDamRec = result.replace("\"", "");
                Log.e("response", "connect:" + result_fDamRec);
                if (result_fDamRec.trim().equals("202"))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
