package in.abhishek.playchat.utils;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SApplication extends Application {
    public static final String TAG = SApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static SApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = SApplication.this;
    }
    public static synchronized SApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void setRetryPolicy(Request<T> req) {
        req.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3,
                        0,  //Since Multiple bids are being placed if retry is hit as response is delayed but DB captures the bid
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
