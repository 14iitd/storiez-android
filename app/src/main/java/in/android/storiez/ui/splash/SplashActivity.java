package in.android.storiez.ui.splash;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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


        volleyGetLogin();
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
                basicUtils.showCustomAlert("Timed Out!");
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
}
