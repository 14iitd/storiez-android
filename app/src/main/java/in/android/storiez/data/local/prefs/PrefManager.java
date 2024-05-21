package in.android.storiez.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class PrefManager {

    private static final String PREF_APP_DB = "pref_app_db";

    private static final String PREF_IS_HINDI_LANGUAGE_SELECTED = "pref_is_hindi_language_selected";
    private static final String PREF_IS_ENGLISH_LANGUAGE_SELECTED = "pref_is_english_language_selected";

    private SharedPreferences pref;
    private Gson gson;


    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_APP_DB, Context.MODE_PRIVATE);
        gson = new Gson();
    }


    public static PrefManager getInstance(Context context) {
        return new PrefManager(context);
    }





    public void setHindiLanguageSelection(boolean selected) {
        pref.edit().putBoolean(PREF_IS_HINDI_LANGUAGE_SELECTED, selected).apply();
    }

    public boolean isHindiLanguageSelected() {
        return pref.getBoolean(PREF_IS_HINDI_LANGUAGE_SELECTED, false);
    }

    public void setEnglishLanguageSelection(boolean selected) {
        pref.edit().putBoolean(PREF_IS_ENGLISH_LANGUAGE_SELECTED, selected).apply();
    }

    public boolean isEnglishLanguageSelected() {
        return pref.getBoolean(PREF_IS_ENGLISH_LANGUAGE_SELECTED, false);
    }

}




