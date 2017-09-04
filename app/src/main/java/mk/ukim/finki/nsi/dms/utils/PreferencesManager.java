package mk.ukim.finki.nsi.dms.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dejan on 03.9.2017.
 */

public class PreferencesManager {

    private static final String PREF_NAME = "mk.ukim.finki.nsi.dms.PREF_NAME";
    private static final String KEY_VALUE = "mk.ukim.finki.nsi.dms.KEY_VALUE";

    private static PreferencesManager preferencesManager;
    private final SharedPreferences sharedPreferences;

    private PreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesManager getInstance(Context context){
        if(preferencesManager ==null){
            preferencesManager = new PreferencesManager(context);
        }
        return preferencesManager;
    }

    public void addStringValue(String key, String value){
        sharedPreferences.edit().putString(key,value).commit();
    }

    public void addIntegerValue(String key, int value){
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public String getStringValue(String key){
        return sharedPreferences.getString(key, null);
    }

    public int getIntegerValue(String key){
        return sharedPreferences.getInt(key, 0);
    }

    public void remove(String key){
        sharedPreferences.edit().remove(key).commit();
    }

    public boolean clear(){
        return sharedPreferences.edit().clear().commit();
    }
}
