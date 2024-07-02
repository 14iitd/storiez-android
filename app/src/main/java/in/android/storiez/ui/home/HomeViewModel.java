package in.android.storiez.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.data.remote.UserComments;
import in.android.storiez.data.remote.model.PostData;
import in.android.storiez.items.PostMetaInfo;
import in.android.storiez.utils.StoriezApp;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<ContentTopic>> contentTopicsMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLikedSuccess = new MutableLiveData<>();

    private MutableLiveData<Boolean> removeLikeSuccess = new MutableLiveData<>();

    private MutableLiveData<List<UserComments>> userCommentsMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<List<PostData>> postsMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<UserComments> userCommentMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<PostMetaInfo> postMetaInfoMutableLiveData = new MutableLiveData<>();

    public LiveData<PostMetaInfo> getPostMetaInfoObservable() {
        return postMetaInfoMutableLiveData;
    }

    public LiveData<UserComments> getUserAddCommentObservable() {
        return userCommentMutableLiveData;
    }

    public LiveData<List<PostData>> getPostsObservable() {
        return postsMutableLiveData;
    }


    public LiveData<List<UserComments>> getUserCommentsObservable() {
        return userCommentsMutableLiveData;
    }

    public LiveData<Boolean> getRemoveSuccessObservable() {
        return removeLikeSuccess;
    }

    public LiveData<Boolean> getIsLikedSuccessObservable() {
        return isLikedSuccess;
    }

    public LiveData<List<ContentTopic>> getContentTopicsObservable() {
        return contentTopicsMutableLiveData;

    }

    public void getTopicContents() {
        StoriezApp.getAppDataManager().getTopicContents(contentTopicsMutableLiveData);
    }


    public void likePost(String postId) {
        StoriezApp.getAppDataManager().likePost(postId, isLikedSuccess);
    }

    public void removeLike(String id) {
        StoriezApp.getAppDataManager().removeLike(id, removeLikeSuccess);
    }

    public void getPostComments(String postId) {
        StoriezApp.getAppDataManager().getPostComments(postId, userCommentsMutableLiveData);
    }

    public void getPosts() {
        StoriezApp.getAppDataManager().getPosts(postsMutableLiveData);
    }

    public void addComment(String postId, UserComments commentObj) {
    StoriezApp.getAppDataManager().addComment(postId, commentObj, userCommentMutableLiveData);
    }

    public void getPostMetaInfo(String postId) {
        StoriezApp.getAppDataManager().getPostMetaInfo(postId, postMetaInfoMutableLiveData);
    }
}
