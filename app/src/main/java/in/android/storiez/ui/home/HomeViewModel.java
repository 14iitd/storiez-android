package in.android.storiez.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.utils.StoriezApp;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<ContentTopic>> contentTopicsMutableLiveData = new MutableLiveData<>();

    public  LiveData<List<ContentTopic>> getContentTopicsObservable() {
        return contentTopicsMutableLiveData;

    }

    public void getTopicContents(){
        StoriezApp.getAppDataManager().getTopicContents(contentTopicsMutableLiveData);
    }

}
