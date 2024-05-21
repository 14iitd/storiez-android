package in.android.storiez.utils;

import android.app.Application;
import android.util.Log;


import in.android.storiez.data.AppDataManager;
import in.android.storiez.data.local.db.AppDatabase;
import in.android.storiez.data.local.prefs.PrefManager;

public class StoriezApp extends Application {

    private static final String TAG = StoriezApp.class.getSimpleName();
    private static StoriezApp instance = null;
    private static AppDatabase appDatabase;
    private static AppDataManager appDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = AppDatabase.getInstance(instance);
        appDataManager = AppDataManager.getInstance(instance);
        Log.d(TAG, "onCreate: app data manager "+appDataManager);
        Log.d(TAG, "onCreate: app data manager "+appDatabase);
    }

    public static StoriezApp getInstance() {
        return instance;
    }

    public PrefManager getPrefHelper() {
        return PrefManager.getInstance(instance);
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static AppDataManager getAppDataManager() {
        return appDataManager;
    }



}
