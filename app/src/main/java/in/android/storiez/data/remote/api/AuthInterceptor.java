package in.android.storiez.data.remote.api;

import android.util.Log;


import java.io.IOException;


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

//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            FirebaseAuth.getInstance().getCurrentUser()
//                    .getIdToken(false).addOnSuccessListener(result -> {
//                        SocialMediaApp.getAppDataManager().setFirebaseAuthToken(result.getToken());
//                        Log.d(TAG, "init: firebase auth token: " + result.getToken());
//                    }).addOnFailureListener(e -> {
//                        SocialMediaApp.getAppDataManager().setFirebaseAuthToken(null);
//                    });
//            authToken = SocialMediaApp.getAppDataManager().getFirebaseAuthToken();
//            if (authToken == null) {
//                Log.d(TAG, "intercept: auth token null");
//                return chain.proceed(originalRequest);
//            }
//            Request modifiedRequest = originalRequest.newBuilder()
//                    .header("Authorization", authToken)
//                    .build();
//            Log.d(TAG, "intercept: token attached to header");
//            Log.d(TAG, "intercept: auth token " + authToken);
//            //Log.d(TAG, "intercept: body " + modifiedRequest.body().toString());
//            if (modifiedRequest.body() != null) {
//                Buffer buffer = new Buffer();
//                modifiedRequest.body().writeTo(buffer);
//                String requestBody = buffer.readUtf8();
//                Log.d(TAG, "intercept: Request Body: " + requestBody);
//            }
//            return chain.proceed(modifiedRequest);
//        }
        return chain.proceed(originalRequest);
    }


}
