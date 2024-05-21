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
import in.android.storiez.utils.StoriezApp;
import okhttp3.OkHttpClient;
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
//                .addInterceptor(new AuthInterceptor(null))
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

                                    if (StoriezApp.getAppDatabase().contentTopicDao().getTopics().size() > 0) {
                                        StoriezApp.getAppDatabase().contentTopicDao().deleteAll();
                                    }
                                    StoriezApp.getAppDatabase().contentTopicDao().insertAll(contentTopics);
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
}
