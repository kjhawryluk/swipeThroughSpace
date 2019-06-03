package edu.uchicago.kjhawryluk.profinal2019.util;



import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Taken from labMyPrefs2019
 * This class simply provides some stock functions for access to SharedPreferences to abbreviate
 * calls throughout the application. Most of the functions here are fairly self explanatory and
 * boilerplate SharedPreferences wrapping.
 */

public class PrefsMgr {

    private static SharedPreferences sSharedPreferences;
    // for filtering how to display the results
    public static final String TYPE = "type";
    // Store sort preferences
    public static final String SORT = "sort";

    // string for boolean storing whether or not first ever launch of the app
    public static final String FIRST_RUN = "first_run";
    // stirng for boolean storing for whether or not the splash has been loaded
    // (e.g. shouldn't be called when resumed from background
    public static final String SPLASH_LOADED = "first_run";

    public static final String VERSION_KEY = "VERSION_KEY";


    public static void setString(Context context, String key, String value) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getString(Context context, String key, String defaultString) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sSharedPreferences.getString(key, defaultString );

    }
    public static String getString(Context context, String key) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sSharedPreferences.getString(key, null );

    }

    public static void setInt(Context context, String key, int value) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public static int getInt(Context context, String key, int defaultValue) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sSharedPreferences.getInt(key, defaultValue);

    }


    public static void setBoolean(Context context, String key, boolean bVal) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putBoolean(key, bVal);
        editor.commit();

    }

    public static boolean getBoolean(Context context, String key, boolean bDefault) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sSharedPreferences.getBoolean(key, bDefault);

    }

    public static void setBooleanArray(Context context, String key, boolean[] array) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sSharedPreferences.edit();


        for (int nC = 0; nC < array.length; nC++) {
            editor.putBoolean(key + nC, array[nC]);
        }

        editor.commit();
    }

    public static boolean[] getBooleanArray(Context context, String key, int size) {
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        boolean[] array = new boolean[size];
        for (int nC = 0; nC < array.length; nC++) {
            array[nC] = sSharedPreferences.getBoolean(key + nC, false);
        }
        return array;

    }


}