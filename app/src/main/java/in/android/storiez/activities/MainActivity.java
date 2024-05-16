package in.android.storiez.activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.android.storiez.R;
import in.android.storiez.databinding.ActivityMainBinding;
import in.android.storiez.adapter.QuestionAdapter;
import in.android.storiez.items.QuestionItem;
import in.android.storiez.utils.API_Details;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ViewPager2 videosViewPager;
    QuestionAdapter adapter;
    BasicUtils basicUtils;
    QuestionItem questionItem;
    ArrayList<QuestionItem> branches;
    ArrayList<String> branchNames;
    ProgressBar pbDestination;
    Context context;
    int userselect;
    boolean correct=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.llCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

        init();
    }
    private void init(){
        context = this;
        questionItem= new QuestionItem();
        basicUtils = new BasicUtils(context);
        initView();

        volleyGetQuestion();

    }
    private void initView() {

        videosViewPager = findViewById(R.id.MainViewPager);
        //videosViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        adapter = new QuestionAdapter(context); // Replace YourPagerAdapter with your actual adapter
        videosViewPager.setAdapter(adapter);
        videosViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Check if the user has reached the 5th item
                if (position == adapter.getItemCount() - 1) {
                    // Fetch more data from the API and add it to your adapter
                    volleyGetQuestion();
//                    fetchMoreData();
                }
            }
        });



        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volleyGetQuestion();
            }
        });
        binding.llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        binding.llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MoreActivity.class);
                startActivity(i);
            }
        });
        binding.llMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MultiPlayerActivity.class);
                startActivity(i);
            }
        });


    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_sheet_layout);

//        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
//        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout llMcq = dialog.findViewById(R.id.llMcq);
        LinearLayout llCard = dialog.findViewById(R.id.llCard);
        LinearLayout llPost = dialog.findViewById(R.id.llPost);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        llMcq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(MainActivity.this, CreateQuestionActivity.class);
                startActivity(i);
            }
        });

        llCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(MainActivity.this, CreateCardActivity.class);
                startActivity(i);
            }
        });

        llPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(MainActivity.this, TakePictureActivity.class);
                startActivity(i);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void volleyGetQuestion( ) {
        //SharedPreferences.Editor editor = prefs.edit();
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyGetQuestion");
        // pbDestination.setVisibility(View.VISIBLE);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = ApiProcessing.GetQuestion.API_URL;
        Log.i(TAG, "volleyGetQuestion : URL = " + url);
        details.setAPI_URL(url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "volleyGetQuestion : Response = " + response);
                        try {

                            branches = ApiProcessing.GetQuestion.parseResponse(response);
                            adapter.addData(branches);
                            //initView(questionItem);
//
//                            // Notify the adapter about the data change
//                            adapter.notifyDataSetChanged();
//                            if (branches.size() == 0) {
//                                basicUtils.showCustomAlert("No Question present");
//                                volleyGetQuestion();
//                            }
                            details.setResponse(response.toString());
                            if (Constants.SUPER_USER)
                                details.show();
                            //pbDestination.setVisibility(View.GONE);
//                            Log.i(TAG, "S_version response = " + loginItem.getDevice_id());
                           // Log.i(TAG, "volleyGetQuestion : Response = " + response);
                            Log.i(TAG, "volleyGetQuestion : Response Length = " + branches.size());

//                            Intent intent = new Intent(HomeFragment.this, MainActivity.class);
//                            startActivity(intent);
                            //finish();

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
                //pbDestination.setVisibility(View.GONE);
//                dialog.dismiss();
                basicUtils.showCustomAlert("Timed Out!");
                Log.e(TAG, "volleyGetQuestion : Error = " + error.toString());
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

    private void volleyQuestionResponse(String question_id, String gmae, String user_select, String correct) {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyGetQuestion");
        // pbDestination.setVisibility(View.VISIBLE);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = ApiProcessing.QuestionResponse.API_URL;
        JSONObject object = ApiProcessing.QuestionResponse.constructObject(question_id,gmae,user_select,correct);
        Log.i(TAG, "volleyQuestionResponse : URL = " + url);
        Log.i(TAG, "volleyQuestionResponse : OBJECT = " + object.toString());
        details.setAPI_URL(url);
        details.setObject(object.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        details.setResponse(response.toString());
                        if (Constants.SUPER_USER)
                            details.show();
                        Log.i(TAG, "volleyPostStringRequest : Response = " + response);
                        try {
                            //questionItem = ApiProcessing.GetQuestion.parseResponse(response);
                            details.setResponse(response.toString());
//                            initView(questionItem);
                            //pbDestination.setVisibility(View.GONE);
//                            Log.i(TAG, "S_version response = " + loginItem.getDevice_id());
                            // Log.i(TAG, "volleyGetQuestion : Response = " + response);
                            Log.i(TAG, "volleyQuestionResponse : Response Length = " + questionItem.getQuestion_text());

//                            Intent intent = new Intent(HomeFragment.this, MainActivity.class);
//                            startActivity(intent);
                            //finish();

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
                //pbDestination.setVisibility(View.GONE);
//                dialog.dismiss();
               // basicUtils.showCustomAlert("Timed Out!");
                Log.e(TAG, "volleyQuestionResponse : Error = " + error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("device_id", deviceId);
                headers.put("Content-Type","application/json");
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams()  {
//                Map<String, String> params = new HashMap<>();
//                params.put("question_id", question_id);
//                params.put("game", gmae);
//                params.put("user_select", user_select);
//                params.put("correct", correct);
//                return params;
//            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Handle item selection
        if (id == R.id.menu_item) {
            // Handle the menu item click (e.g., open a side drawer)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}