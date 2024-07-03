package in.android.storiez.ui.createContent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.android.storiez.data.local.model.PostType;
import in.android.storiez.data.local.model.PostTypeUrls;
import in.android.storiez.utils.StoriezApp;

public class CreateContentViewModel extends ViewModel {


    private MutableLiveData<List<PostType>> postTypeUrlsMutableLiveData = new MutableLiveData<>();



    public LiveData<List<PostType>> getPostTypeUrlsObservable() {
        return postTypeUrlsMutableLiveData;
    }


    public void getCreationUrls() {

        StoriezApp.getAppDataManager().getCreationUrls(postTypeUrlsMutableLiveData);
    }
}
