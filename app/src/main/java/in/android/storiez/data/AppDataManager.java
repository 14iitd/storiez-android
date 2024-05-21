package in.android.storiez.data;

import android.content.Context;


import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.data.local.prefs.PrefManager;
import in.android.storiez.data.remote.api.ApiManager;


public class AppDataManager {

    private static final String TAG = AppDataManager.class.getSimpleName();
    private static AppDataManager instance;
    private ApiManager apiManager;
    private PrefManager prefManager;

    private AppDataManager(Context context) {
        apiManager = ApiManager.getInstance();
        prefManager = PrefManager.getInstance(context);
    }

    public static AppDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppDataManager(context);
        }
        return instance;
    }


    public void getTopicContents(MutableLiveData<List<ContentTopic>> contentTopicsMutableLiveData) {
        apiManager.getTopicContents(contentTopicsMutableLiveData);
    }


    public void setHindiSelection(boolean selected) {
     prefManager.setHindiLanguageSelection(selected);
    }

    public boolean isHindiSelection() {
        return prefManager.isHindiLanguageSelected();
    }

    public void setEnglishSelection(boolean selected) {
        prefManager.setEnglishLanguageSelection(selected);
    }

    public boolean isEnglishSelection() {
        return prefManager.isEnglishLanguageSelected();
    }

}
