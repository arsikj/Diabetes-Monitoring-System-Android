package mk.ukim.finki.nsi.dms.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mk.ukim.finki.nsi.dms.model.Measure;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dejan on 03.9.2017.
 */

public class HTTPRequestService {

    private static HTTPRequestService httpRequestService;
    private static OkHttpClient httpClient;

    private HTTPRequestService(){
        httpClient = new OkHttpClient();
    }

    public synchronized static HTTPRequestService getInstance(){
        if(httpRequestService == null) {
            return new HTTPRequestService();
        }
        return httpRequestService;
    }

    public OkHttpClient getHttpClient(){
        return httpClient;
    }

    public boolean signUp(String url, String name, String username, String password){
        boolean isSignedUp=false;

        RequestBody formBody = new FormBody.Builder()
                .add(Constants.NAME, name)
                .add(Constants.USERNAME, username)
                .add(Constants.PASSWORD,password)
                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        try {
            Response response = getInstance().getHttpClient().newCall(request).execute();
            if(response.isSuccessful()){
                isSignedUp=true;
            } else {
                isSignedUp=false;
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return isSignedUp;
    }

    public boolean login(String url, String username, String password, Context context){
        boolean isLoggedIn=false;

        RequestBody formBody = new FormBody.Builder()
                .add(Constants.USERNAME,username)
                .add(Constants.PASSWORD,password)
                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        try {
            Response response = getInstance().getHttpClient().newCall(request).execute();

            if(response.isSuccessful()){
                PreferencesManager.getInstance(context).addStringValue(Constants.USERNAME, username);
                PreferencesManager.getInstance(context).addStringValue(Constants.PASSWORD, password);

                isLoggedIn = true;
            } else {
                isLoggedIn = false;
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return isLoggedIn;
    }

    public List<Measure> getMeasures(String url){
        Request request = new Request.Builder().url(url).build();
        List<Measure> measures = new ArrayList<Measure>();
        try {
            Response response = HTTPRequestService.getInstance().getHttpClient().newCall(request).execute();
            String jsonData = response.body().string();
            JSONArray jsonArray = new JSONArray(jsonData);

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int level = jsonObject.getInt("level");
                Date dateAdded = new Date(jsonObject.getLong("dateAdded"));
                Measure measure = new Measure(level, dateAdded);

                measures.add(measure);
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return measures;
    }

    public Boolean addMeasure(String url, String username, String level){

        RequestBody formBody = new FormBody.Builder().add(Constants.USERNAME, username).add(Constants.LEVEL, level).build();

        Request request = new Request.Builder().url(url).post(formBody).build();
        boolean isSent = false;

        try {
            Response response = HTTPRequestService.getInstance().getHttpClient().newCall(request).execute();
            if(response.isSuccessful()){
                isSent = true;
            } else {
                isSent = false;
            }
            response.body().close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return isSent;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
