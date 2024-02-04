package in.abhishek.playchat.activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
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
import java.util.List;
import java.util.Map;

import in.abhishek.playchat.R;
import in.abhishek.playchat.adapter.QuestionAdapter;
import in.abhishek.playchat.databinding.ActivityMainBinding;
import in.abhishek.playchat.items.QuestionItem;
import in.abhishek.playchat.utils.API_Details;
import in.abhishek.playchat.utils.ApiProcessing;
import in.abhishek.playchat.utils.BasicUtils;
import in.abhishek.playchat.utils.Constants;

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
                Intent i = new Intent(MainActivity.this, CreateQuestionActivity.class);
                startActivity(i);
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

//        final TextView question = binding.question;
//        final TextView q1 = binding.q1;
//        final TextView q2 = binding.q2;
//        final TextView q3 = binding.q3;
//        final TextView q4 = binding.q4;
//        final TextView accuracy = binding.accuracy;
//        final TextView played = binding.played;
        videosViewPager = findViewById(R.id.MainViewPager);
        videosViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating array list
//        List<QuestionItem> videoActList = new ArrayList<>();

        //videosViewPager.setAdapter(new QuestionAdapter(context,branches));
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
//        videosViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                // Not needed for infinite scrolling
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                // Check if the user has reached the last item
//                if (position == adapter.getItemCount() - 1) {
//                    // Load more data from the API
//                    volleyGetQuestion();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                // Not needed for infinite scrolling
//            }
//        });



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
    private void fetchMoreData() {
        // Make your API call here and add the new data to your adapter
        // For example, you can use Retrofit, AsyncTask, or any other networking library
        // After fetching data, add it to your adapter and notify the adapter about the data change
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                volleyGetQuestion();
                adapter.addData(branches);

                // Notify the adapter about the data change
                adapter.notifyDataSetChanged();
            }
        });
//        new AsyncTask<Void, Void, List<QuestionItem>>() {
//            @Override
//            protected List<QuestionItem> doInBackground(Void... voids) {
//                // Make your API call and return the new data
//                return volleyGetQuestion();
//            }
//
//            @Override
//            protected void onPostExecute(List<QuestionItem> newData) {
//                // Add the new data to your adapter
//                if (newData != null) {
//                    // Add the new data to the adapter
//                    adapter.addData(newData);
//
//                    // Notify the adapter about the data change
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }.execute();
    }

//    private void onInfinitePageChangeCallback(int listSize) {
//        binding.MainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//                if (state == ViewPager2.SCROLL_STATE_IDLE) {
//                    switch (binding.MainViewPager.getCurrentItem()) {
//                        case (listSize - 1):
//                            volleyGetQuestion();
//                            break;
//                        case 0:
//                            binding.MainViewPager.setCurrentItem(listSize - 2, false);
//                            break;
//                    }
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                if (position != 0 && position != listSize - 1) {
//                    // pageIndicatorView.setSelected(position-1)
//                }
//            }
//        });
//    }



    private void volleyGetQuestion( ) {
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


}