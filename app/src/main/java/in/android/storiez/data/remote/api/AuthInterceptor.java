package in.android.storiez.data.remote.api;

import android.util.Log;


import java.io.IOException;


import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.StoriezApp;
import in.android.storiez.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class AuthInterceptor implements Interceptor {


    private static final String TAG = AuthInterceptor.class.getSimpleName();
    private String authToken;

    public AuthInterceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Log.d(TAG, "intercept: ");

        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "hey")
                .header("device_id", BasicUtils.getDeviceId(StoriezApp.getInstance()))
//                .header("device_id", "65845de0f3aac0d35e7d6172s")

                .header("user_id", BasicUtils.getDeviceId(StoriezApp.getInstance()))
//                .header("user_id", "12918hbwdh994")

                .header("Content-Type", "application/json")
                .build();

        Log.d(TAG, "intercept: auth interceptor device id and user id "+BasicUtils.getDeviceId(StoriezApp.getInstance())+" "+BasicUtils.getDeviceId(StoriezApp.getInstance())   );

        if (modifiedRequest.body() != null) {
            Buffer buffer = new Buffer();
            modifiedRequest.body().writeTo(buffer);
            String requestBody = buffer.readUtf8();
            Log.d(TAG, "intercept: Request Body: " + requestBody);
        }
        return chain.proceed(originalRequest);
    }


}
