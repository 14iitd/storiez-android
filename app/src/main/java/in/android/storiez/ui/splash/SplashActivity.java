package in.android.storiez.ui.splash;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.android.storiez.base.BaseActivity;
import in.android.storiez.databinding.ActivitySplashBinding;
import in.android.storiez.ui.home.HomeActivity;
import in.android.storiez.ui.language.LanguageActivity;
import in.android.storiez.utils.API_Details;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.Constants;
import in.android.storiez.utils.StoriezApp;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public ViewBinding initViewBinding(LayoutInflater inflater) {
        return ActivitySplashBinding.inflate(inflater);
    }


    BasicUtils basicUtils;

    //ProgressBar pbDestination;
    Context context;// Create a request queue


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA

    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();


        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                Log.d(TAG, "onCreate: url we got from link "+uri.getLastPathSegment());
                Log.d(TAG, "onCreate: url we got from link string "+uri.toString());

                // Extract postId from the URI path
                String postId = uri.getLastPathSegment();
                // Use postId to navigate or perform actions in your app
                // Example: navigate to a specific post based on postId

                Intent homeIntent = new Intent(this, HomeActivity.class);
                homeIntent.putExtra(HomeActivity.EXTRAS_SHARED_POST_ID, postId);
                startActivity(homeIntent);
                finish();
            }
        } else {

            volleyGetLogin();
        }
        






    }

    private void init() {
        context = this;
        basicUtils = new BasicUtils(context);
    }

    private void volleyGetLogin() {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("voll eyGetLogin");
        //pbDestination.setVisibility(View.VISIBLE);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = ApiProcessing.Login.API_URL;
        Log.i(TAG, "volleyGetLogin : URL = " + url);
        details.setAPI_URL(url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //LoginItem loginItem = ApiProcessing.Login.parseResponse(response);
                            details.setResponse(response.toString());
                            //pbDestination.setVisibility(View.GONE);
//                            Log.i(TAG, "S_version response = " + loginItem.getDevice_id());
                            Log.i(TAG, "volleyGetCityStateName : Response = " + response);
                            Log.i(TAG, "volleyGetCityStateName : Response Length = " + response.length());

                            if (StoriezApp.getAppDataManager().isHindiSelection() || StoriezApp.getAppDataManager().isEnglishSelection()) {
                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this, LanguageActivity.class));
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        if (Constants.SUPER_USER)
//                            details.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                details.setErrorResponse(error.toString());
                if (Constants.SUPER_USER)
                    details.show();
                // pbDestination.setVisibility(View.GONE);
//                dialog.dismiss();
                Toast.makeText(SplashActivity.this, "Time Out", Toast.LENGTH_SHORT).show();
//                basicUtils.showCustomAlert("Timed Out!");
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
                Log.e(TAG, "volleyGetCityStateName : Error = " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("device_id", deviceId);
                return headers;
            }
        };
        requestQueue.add(request);

//        request.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3,
//                        0,  //Since Multiple bids are being placed if retry is hit as response is delayed but DB captures the bid
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //SApplication.getInstance().addToRequestQueue(request, "GetDestination");
    }

    @Override
    protected void onStart() {
        super.onStart();



    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        handleDynamicLink(intent);

        this.setIntent(intent);
        if (intent != null && intent.hasExtra("branch_force_new_session") && intent.getBooleanExtra("branch_force_new_session", false)) {
            Branch.sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(JSONObject referringParams, BranchError error) {
                    if (error != null) {
                        Log.d(TAG, "onInitFinished: branch tester " + error.getMessage());
                        Log.e("BranchSDK_Tester", error.getMessage());
                    } else if (referringParams != null) {
                        Log.i("BranchSDK_Tester", referringParams.toString());
                        Log.d(TAG, "onInitFinished: branch tester params " + referringParams.toString());
                    } else {
                        Log.d(TAG, "onInitFinished: branch tester null");
                    }
                }
            }).reInit();
        }
    }
}
