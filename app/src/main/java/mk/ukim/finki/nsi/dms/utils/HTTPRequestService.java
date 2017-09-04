package mk.ukim.finki.nsi.dms.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

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
