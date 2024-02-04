package in.abhishek.playchat.activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.abhishek.playchat.R;
import in.abhishek.playchat.utils.API_Details;
import in.abhishek.playchat.utils.ApiProcessing;
import in.abhishek.playchat.utils.BasicUtils;
import in.abhishek.playchat.utils.Constants;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    BasicUtils basicUtils;

    ProgressBar pbDestination;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //setContentView(R.layout.activity_fullscreen);
        //pbDestination = findViewById(R.id.pbDestination);
        init();

//        if (ActivityCompat.checkSelfPermission(FullscreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(FullscreenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(FullscreenActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            //viewFlipper.stopFlipping();
//            ActivityCompat.requestPermissions(
//                    FullscreenActivity.this,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        } else {
//            volleyGetLogin(pbDestination);
//        }
        volleyGetLogin();


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(FullscreenActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
//            }
//        }, 3000);


    }
    private void init(){
        context = this;
        basicUtils = new BasicUtils(context);
    }
    private void volleyGetLogin() {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyGetLogin");
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

                            Intent intent = new Intent(FullscreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

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
                pbDestination.setVisibility(View.GONE);
//                dialog.dismiss();
                basicUtils.showCustomAlert("Timed Out!");
                Log.e(TAG, "volleyGetCityStateName : Error = " + error.toString());
            }
        }){
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