package in.android.storiez.data;

import android.content.Context;


import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.data.local.prefs.PrefManager;
import in.android.storiez.data.remote.UserComments;
import in.android.storiez.data.remote.api.ApiManager;
import in.android.storiez.data.remote.model.PostData;
import in.android.storiez.items.PostMetaInfo;


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

    public void likePost(String postId, MutableLiveData<Boolean> isLikedSuccess) {
        apiManager.likePost(postId, isLikedSuccess);
    }

    public void removeLike(String id, MutableLiveData<Boolean> removeLikeSuccess) {
        apiManager.removeLike(id, removeLikeSuccess);
    }

    public void getPostComments(String postId, MutableLiveData<List<UserComments>> userCommentsMutableLiveData) {
        apiManager.getPostComments(postId, userCommentsMutableLiveData);
    }

    public void getPosts(MutableLiveData<List<PostData>> postsMutableLiveData) {

        apiManager.getPosts(postsMutableLiveData);
    }

    public void addComment(String postId, UserComments commentObj, MutableLiveData<UserComments> userCommentMutableLiveData) {
        apiManager.addComment(postId, commentObj, userCommentMutableLiveData);
    }

    public void getPostMetaInfo(String postId, MutableLiveData<PostMetaInfo> postMetaInfoMutableLiveData) {
    apiManager.getPostMetaInfo(postId, postMetaInfoMutableLiveData);
    }
}
