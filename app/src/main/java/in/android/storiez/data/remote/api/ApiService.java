package in.android.storiez.data.remote.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @GET("news/catgs")
    Call<ResponseBody> getTopicContents();


//    https://playchat.live/like/api/v1/post/65e098f7d436660c0c0ed6b6' \


//    @POST("like/api/v1/post/{postId}")
//    Call<ResponseBody> likePost(@Path("post_id") String postId);


    @POST
    Call<ResponseBody> likePost(@Url String url, @Body RequestBody body);

    @POST
    Call<ResponseBody> removeLike(@Url String url, @Body RequestBody body);

    @GET
    Call<ResponseBody> getPostComments(@Url String url);


    @GET
    Call<ResponseBody> getPosts(@Url String url);

    @POST
    Call<ResponseBody> addComment(@Url String url, @Body RequestBody body);


    @POST
    Call<ResponseBody> getPostMetaInfo(@Url String url, @Body RequestBody body);



//    @POST
//    Call<ResponseBody> likePost(@Url String url);

}

