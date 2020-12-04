package me.jessyan.art.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public class TinyPref {
    private static TinyPref INSTANCE;
    private SharedPreferences preferences;

    private TinyPref() {
    }

    /**
     * for testcase use, set mock context
     *
     * @param context
     */
    private TinyPref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static TinyPref getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TinyPref();
        }
        return INSTANCE;
    }

    /**
     * init application context
     *
     * @param appContext
     */
    public void init(Context appContext) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        }
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0l);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double defValue) {
        String number = getString(key);
        try {
            double value = Double.parseDouble(number);
            return value;
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putDouble(String key, double value) {
        putString(key, String.valueOf(value));
    }

    public void putString(String key, String value) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putList(String key, List<String> array) {

        SharedPreferences.Editor editor = preferences.edit();
        String[] mystringlist = array.toArray(new String[array.size()]);
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        editor.putString(key, TextUtils.join("‚‗‚", mystringlist));
        editor.apply();
    }

    public ArrayList<String> getList(String key) {
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        String[] mylist = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> gottenlist = new ArrayList<String>(Arrays.asList(mylist));
        return gottenlist;
    }

    public void putListInt(String key, List<Integer> marray) {
        SharedPreferences.Editor editor = preferences.edit();
        Integer[] mystringlist = marray.toArray(new Integer[marray.size()]);
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        editor.putString(key, TextUtils.join("‚‗‚", mystringlist));
        editor.apply();
    }

    public ArrayList<Integer> getListInt(String key) {
        // the comma like character used below is not a comma it is the SINGLE
        // LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
        // seprating the items in the list
        String[] mylist = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> gottenlist = new ArrayList<String>(Arrays.asList(mylist));
        ArrayList<Integer> gottenlist2 = new ArrayList<Integer>();
        for (int i = 0; i < gottenlist.size(); i++) {
            gottenlist2.add(Integer.parseInt(gottenlist.get(i)));
        }

        return gottenlist2;
    }

    public void putListBoolean(String key, ArrayList<Boolean> marray) {
        ArrayList<String> origList = new ArrayList<String>();
        for (Boolean b : marray) {
            if (b == true) {
                origList.add("true");
            } else {
                origList.add("false");
            }
        }
        putList(key, origList);
    }

    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> origList = getList(key);
        ArrayList<Boolean> mBools = new ArrayList<Boolean>();
        for (String b : origList) {
            if (b.equals("true")) {
                mBools.add(true);
            } else {
                mBools.add(false);
            }
        }
        return mBools;
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0f);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * Mark in the editor to remove all values from the preferences. Once commit is called, the only remaining preferences will be any that you have
     * defined in this editor.Note that when committing back to the preferences, the clear is done first, regardless of whether you called clear
     * before or after put methods on this editor.
     */
    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
