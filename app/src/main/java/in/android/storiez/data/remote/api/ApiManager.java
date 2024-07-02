package in.android.storiez.data.remote.api;
//
//import android.util.Log;
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//
//public class ApiManager {
//
//
//    private static final String TAG = "ApiManager";
//
//    private static Retrofit retrofit, retrofit2;
//
//
//
//
//    public static Retrofit getRetrofitInstance() {
//        Log.d(TAG, "getRetrofitInstance: " + BASE_URL);
//        if (retrofit == null) {
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .callTimeout(100, TimeUnit.SECONDS)
//                    .connectTimeout(100, TimeUnit.SECONDS)
//                    .readTimeout(100, TimeUnit.SECONDS)
//                    .build();
//            retrofit = new retrofit2.Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//}


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.data.remote.UserComments;
import in.android.storiez.data.remote.model.PostData;
import in.android.storiez.items.PostMetaInfo;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.StoriezApp;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    //
    private static final String TAG = ApiManager.class.getSimpleName();

//    private static final String BASE_URL = BuildConfig.SERVER_URL;

    //    private static final String BASE_URL = BuildConfig.SERVER_URL;
    private static final String BASE_URL = "https://playchat.live/";

    private static ApiManager instance;
    private ApiService apiService;
    private Retrofit retrofit;

    private Call<ResponseBody> call;

    private Call<ResponseBody> apiCall;

    private ApiManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor(null))
                .followRedirects(true)
                .followSslRedirects(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }


    public void getTopicContents(MutableLiveData<List<ContentTopic>> contentTopicsMutableLiveData) {

        apiService.getTopicContents()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String jsonString = response.body().string();
                                JSONObject jsonObject = new JSONObject(jsonString);

                                // Check if the response contains an array named "data"
                                if (jsonObject.has("data")) {
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    Type listType = new TypeToken<List<ContentTopic>>() {
                                    }.getType();
                                    List<ContentTopic> contentTopics = new Gson().fromJson(dataArray.toString(), listType);

                                    Log.d(TAG, "onResponse: we got some data " + contentTopics);

//                                    if (StoriezApp.getAppDatabase().contentTopicDao().getTopics().size() > 0) {
//                                        StoriezApp.getAppDatabase().contentTopicDao().deleteAll();
//                                    }
//                                    StoriezApp.getAppDatabase().contentTopicDao().insertAll(contentTopics);
                                    contentTopicsMutableLiveData.postValue(contentTopics);
                                } else {
                                    Log.d(TAG, "onResponse: Missing 'data' array in the response");
                                    contentTopicsMutableLiveData.postValue(null);
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "onResponse: exception " + e.getMessage());
                                e.printStackTrace();
                                contentTopicsMutableLiveData.postValue(null);
                            }
                        } else {
                            Log.d(TAG, "onResponse: we got some error ");
                            contentTopicsMutableLiveData.postValue(null);
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        contentTopicsMutableLiveData.postValue(null);
                    }
                });
    }

//    public void likePost(String postId, MutableLiveData<Boolean> isLikedSuccess) {
//
//        String url = "https://playchat.live/like/api/v1/"+postId;
//        Log.d(TAG, "likePost: url of post api "+url);
//        apiService.likePost(url)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            isLikedSuccess.postValue(true);
//                            Log.d(TAG, "onResponse: liked a post successfully ");
//                        } else {
//                            isLikedSuccess.postValue(false);
//                            Log.d(TAG, "onResponse: failed to like a post "+response.message());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        isLikedSuccess.postValue(false);
//                        Log.d(TAG, "onFailure: like post api "+t);
//                    }
//                });
//
//
//    }


    public void likePost(String postId, MutableLiveData<Boolean> isLikedSuccess) {
        String url = "https://playchat.live/like/api/v1/post/" + postId;
        Log.d(TAG, "likePost: url of post api " + url);

        // Create the request body
        String jsonBody = "{\"user_id\":\"21121qwqw\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        // Make the API call
        apiService.likePost(url, body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            isLikedSuccess.postValue(true);
                            Log.d(TAG, "onResponse: liked a post successfully");
                        } else {
                            isLikedSuccess.postValue(false);
                            Log.d(TAG, "onResponse: failed to like a post " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        isLikedSuccess.postValue(false);
                        Log.d(TAG, "onFailure: like post api " + t);
                    }
                });
    }

    public void removeLike(String id, MutableLiveData<Boolean> removeLikeSuccess) {
        String url = "https://playchat.live/dislike/api/v1/post/" + id;

        String jsonBody = "{\"user_id\":\"21121qwqw\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        apiService.removeLike(url, body)

                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            removeLikeSuccess.postValue(true);
                            Log.d(TAG, "onResponse: removed like successfully");
                        } else {
                            removeLikeSuccess.postValue(false);
                            Log.d(TAG, "onResponse: failed to remove like " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        removeLikeSuccess.postValue(false);
                        Log.d(TAG, "onFailure: remove like api " + t);
                    }
                });

    }

    public void getPostComments(String postId, MutableLiveData<List<UserComments>> userCommentsMutableLiveData) {

//        String url = "http://playchat.live/post/" + postId + "/replies";

        String url = "http://playchat.live/post/65b64f3350d507328fafa2ee/replies";


        apiService.getPostComments(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String jsonResponse = response.body().string();
                                Gson gson = new Gson();
                                Type commentListType = new TypeToken<List<UserComments>>() {
                                }.getType();
                                List<UserComments> commentList = gson.fromJson(jsonResponse, commentListType);

                                // Assuming UserComments has a method to set the list of comments
                                Log.d(TAG, "onResponse: we got some comments which are as follows " + commentList.size());
                                Log.d(TAG, "onResponse: we got some comments which are as follows " + commentList.toString());


                                userCommentsMutableLiveData.postValue(commentList);
                            } catch (IOException e) {
                                e.printStackTrace();
                                userCommentsMutableLiveData.postValue(null);
                                Log.d(TAG, "onResponse: failed to fet comments " + e);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure: in getting comments " + t);
                    }
                });

    }


    public void getPosts(MutableLiveData<List<PostData>> postsMutableLiveData) {

        Log.d(TAG, "getPosts: hitting api ");
        String baseUrl = ApiProcessing.GetQuestion.API_URL;


        String languages = "";


        if ((StoriezApp.getAppDataManager().isHindiSelection()) && (StoriezApp.getAppDataManager().isEnglishSelection())) {
            languages = "HINDI,ENGLISH";
        } else if (StoriezApp.getAppDataManager().isHindiSelection()) {
            languages = "HINDI";
        } else if (StoriezApp.getAppDataManager().isEnglishSelection()) {
            languages = "ENGLISH";
        } else {
            languages = null;
        }

        List<ContentTopic> topics = StoriezApp.getAppDatabase().contentTopicDao().getSelectedTopics();

        StringBuilder categoriesBuilder = new StringBuilder();

        for (int i = 0; i < topics.size(); i++) {
            ContentTopic topic = topics.get(i);

            categoriesBuilder.append(topic.getCategory());
            if (i < topics.size() - 1) {
                categoriesBuilder.append(",");
            }
        }

        String categoriesString = categoriesBuilder.toString();


//        String url = builder.build().toString();

        String queryString = "langs=" + languages + "&loc=" + StoriezApp.getInstance().getResources().getConfiguration().locale.getCountry() +
                "&cats=" + categoriesString;
        String url = baseUrl + "?" + queryString;


        apiService.getPosts(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                Log.d(TAG, "onResponse: successfully got the psot data ");
                                String jsonResponse = response.body().string();
                                Gson gson = new Gson();
                                Type postDataListType = new TypeToken<List<PostData>>() {
                                }.getType();
                                List<PostData> postDataList = gson.fromJson(jsonResponse, postDataListType);

                                postsMutableLiveData.postValue(postDataList);
                            } catch (IOException e) {
                                e.printStackTrace();
                                postsMutableLiveData.postValue(null);
                                Log.d(TAG, "onResponse: failed to get post data " + e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        Log.d(TAG, "onFailure: getting post data " + t);
                        postsMutableLiveData.postValue(null);
                    }
                });
    }

    public void addComment(String postId, UserComments commentObj, MutableLiveData<UserComments> userCommentMutableLiveData) {

        String url = "http://playchat.live/post/" + postId + "/replies";

        // Create the JSON body
        String jsonBody = "{\"text\":\"" + commentObj.getText() + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        apiService.addComment(url
                        , body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            userCommentMutableLiveData.postValue(commentObj);
                            Log.d(TAG, "onResponse: added comment successfully");
                        } else {
                            userCommentMutableLiveData.postValue(null);
                            Log.d(TAG, "onResponse: failed to add comment " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        userCommentMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: add comment api " + t);
                    }
                });

    }

    public void getPostMetaInfo(String postId, MutableLiveData<PostMetaInfo> postMetaInfoMutableLiveData) {

        String url = "https://playchat.live/haslike/api/v1/post/" + postId;

        String jsonBody = "{\"user_id\":\"21121qwqw\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        apiService.getPostMetaInfo(url, body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String responseBody = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseBody);

                                PostMetaInfo postMetaInfo = new PostMetaInfo();
                                postMetaInfo.setLiked(jsonObject.getBoolean("liked"));
                                postMetaInfo.setLikesCount(jsonObject.getInt("total_likes"));
                                postMetaInfo.setCommentsCount(jsonObject.getInt("total_comments"));
                                postMetaInfo.setShareCount(jsonObject.getInt("total_share"));
                                postMetaInfo.setPostId(postId);

                                postMetaInfoMutableLiveData.postValue(postMetaInfo);
                                Log.d(TAG, "onResponse: we got post meta info of " + postId);
                            } catch (Exception e) {
                                postMetaInfoMutableLiveData.postValue(null);
                                Log.e(TAG, "onResponse: failed to parse post meta info", e);
                            }
                        } else {
                            postMetaInfoMutableLiveData.postValue(null);
                            Log.d(TAG, "onResponse: failed to get post meta info ");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        postMetaInfoMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: in getting post meta info " + t);
                    }
                });
    }
}
