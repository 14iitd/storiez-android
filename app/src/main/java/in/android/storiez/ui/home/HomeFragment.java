package in.android.storiez.ui.home;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
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

import in.android.storiez.R;
import in.android.storiez.activities.CreateCardActivity;
import in.android.storiez.activities.CreateQuestionActivity;
import in.android.storiez.activities.MoreActivity;
import in.android.storiez.activities.MultiPlayerActivity;
import in.android.storiez.activities.ProfileActivity;
import in.android.storiez.activities.TakePictureActivity;
import in.android.storiez.adapter.QuestionAdapter;
import in.android.storiez.base.BaseFragment;
import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.databinding.ActivityMainBinding;
import in.android.storiez.databinding.FragmentHomeMainBinding;
import in.android.storiez.items.QuestionItem;
import in.android.storiez.utils.API_Details;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.Constants;
import in.android.storiez.utils.StoriezApp;

public class HomeFragment extends BaseFragment<FragmentHomeMainBinding> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public ViewBinding initViewBinding(LayoutInflater inflater, ViewGroup parent) {
        return FragmentHomeMainBinding.inflate(inflater, parent, false);
    }


    private static final String TAG = HomeFragment.class.getSimpleName();
    ViewPager2 videosViewPager;
    QuestionAdapter adapter;
    BasicUtils basicUtils;
    QuestionItem questionItem;
    ArrayList<QuestionItem> branches;
    ArrayList<String> branchNames;
    ProgressBar pbDestination;
    Context context;
    int userselect;
    boolean correct = false;

    TopicsAdapter topicsAdapter;

    HomeViewModel viewModel;

    List<ContentTopic> topics = new ArrayList<>();

    boolean isDataChanged = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Log.d(TAG, "onViewCreated: home fragment");

        adapter = new QuestionAdapter(context); // Replace YourPagerAdapter with your actual adapter
        binding.MainViewPager.setAdapter(adapter);
        binding.MainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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

        init();

        topicsAdapter = new TopicsAdapter(topics);
        binding.topicRecyclerView.setAdapter(topicsAdapter);


        viewModel.getContentTopicsObservable().observe(getViewLifecycleOwner(), topics -> {
            if (topics != null) {
                Log.d(TAG, "onViewCreated: got some data ");
                topicsAdapter.updateTopics(topics);

            }
        });

        if (StoriezApp.getAppDatabase().contentTopicDao().getTopics().size() > 0) {
            topics = StoriezApp.getAppDatabase().contentTopicDao().getTopics();

            topicsAdapter.updateTopics(topics);
        } else {
            viewModel.getTopicContents();
        }

        topicsAdapter.setOnBookmarkedClicked(() -> {
            isDataChanged = true;
        });


        binding.menuIcon.setOnClickListener(v -> {
            if (!binding.drawerLayout.isDrawerOpen(binding.topicsConstraintLayout)) {
                openDrawer();
            } else {
                closeDrawer();
            }
        });

        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Slide the main content along with the drawer
                float drawerWidth = 280;
                binding.postsConstraintLayout.setTranslationX(slideOffset * drawerWidth);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // No need to handle
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d(TAG, "onDrawerClosed: ");
                volleyGetQuestion();
                // No need to handle
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // No need to handle
            }
        });

        setUpListeners();
        languageCardSelectionSetup();

    }

    private void languageCardSelectionSetup() {
        if (StoriezApp.getAppDataManager().isEnglishSelection()) {
            binding.englishCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));
            binding.englishCard.setStrokeColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));
            binding.englishTxt.setTextColor(requireContext().getResources().getColor(R.color.white));
        } else {
            binding.englishCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.white));
            binding.englishCard.setStrokeColor(requireContext().getResources().getColor(R.color.black));
            binding.englishTxt.setTextColor(requireContext().getResources().getColor(R.color.black));
        }


        if (StoriezApp.getAppDataManager().isHindiSelection()) {
            binding.hindiCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));
            binding.hindiTxt.setTextColor(requireContext().getResources().getColor(R.color.white));
            binding.hindiCard.setStrokeColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));
        } else {
            binding.hindiCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.white));
            binding.hindiTxt.setTextColor(requireContext().getResources().getColor(R.color.black));
            binding.hindiCard.setStrokeColor(requireContext().getResources().getColor(R.color.black));
        }
    }

    private void setUpListeners() {

        binding.hindiCard.setOnClickListener(v -> {
            isDataChanged = true;

            if (!StoriezApp.getAppDataManager().isHindiSelection()) {
                binding.hindiCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));
                binding.hindiTxt.setTextColor(requireContext().getResources().getColor(R.color.white));
                binding.hindiCard.setStrokeColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));

                StoriezApp.getAppDataManager().setHindiSelection(true);
            } else {
                binding.hindiCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.white));
                binding.hindiTxt.setTextColor(requireContext().getResources().getColor(R.color.black));
                binding.hindiCard.setStrokeColor(requireContext().getResources().getColor(R.color.black));
                StoriezApp.getAppDataManager().setHindiSelection(false);
            }

        });

        binding.englishCard.setOnClickListener(v -> {
            isDataChanged = true;
            if (!StoriezApp.getAppDataManager().isEnglishSelection()) {
                binding.englishCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));
                binding.englishCard.setStrokeColor(requireContext().getResources().getColor(R.color.hindi_btn_bg));
                binding.englishTxt.setTextColor(requireContext().getResources().getColor(R.color.white));

                StoriezApp.getAppDataManager().setEnglishSelection(true);
            } else {
                binding.englishCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.white));
                binding.englishCard.setStrokeColor(requireContext().getResources().getColor(R.color.black));
                binding.englishTxt.setTextColor(requireContext().getResources().getColor(R.color.black));
                StoriezApp.getAppDataManager().setEnglishSelection(false);
            }

        });
    }

    private void openDrawer() {
        binding.drawerLayout.openDrawer(binding.topicsConstraintLayout);
    }

    private void closeDrawer() {
        binding.drawerLayout.closeDrawer(binding.topicsConstraintLayout);
    }

    private void init() {
        context = requireContext();
        questionItem = new QuestionItem();
        basicUtils = new BasicUtils(context);
        initView();

        volleyGetQuestion();

    }

    private void initView() {

//        videosViewPager = findViewById(R.id.MainViewPager);
        //videosViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


//        binding.home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                volleyGetQuestion();
//            }
//        });
//        binding.llProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
//                startActivity(i);
//            }
//        });
//        binding.llMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(HomeActivity.this, MoreActivity.class);
//                startActivity(i);
//            }
//        });
//        binding.llMultiplayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(HomeActivity.this, MultiPlayerActivity.class);
//                startActivity(i);
//            }
//        });
//

    }



    public void scrollToTheTop() {

        if (binding != null) {
            if (binding.MainViewPager != null) {
                volleyGetQuestion();
                binding.MainViewPager.setCurrentItem(0, true);
            }
        }
    }

    public void volleyGetQuestion() {

        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyGetQuestion");

        // Construct URL with query parameters
        String deviceId = BasicUtils.getDeviceId(context);
        String baseUrl = ApiProcessing.GetQuestion.API_URL;


        String languages = "";


        if ((StoriezApp.getAppDataManager().isHindiSelection()) && (StoriezApp.getAppDataManager().isEnglishSelection())) {
            languages = "HINDI,ENGLISH";
        } else if (StoriezApp.getAppDataManager().isHindiSelection()) {
            languages = "HINDI";
        } else if (StoriezApp.getAppDataManager().isEnglishSelection()) {
            languages = "ENGLISH";
        } else {
            languages = null;
        }

        List<ContentTopic> topics = StoriezApp.getAppDatabase().contentTopicDao().getSelectedTopics();

        StringBuilder categoriesBuilder = new StringBuilder();

        for (int i = 0; i < topics.size(); i++) {
            ContentTopic topic = topics.get(i);

            categoriesBuilder.append(topic.getCategory());
            if (i < topics.size() - 1) {
                categoriesBuilder.append(",");
            }
        }

        String categoriesString = categoriesBuilder.toString();


//        String url = builder.build().toString();

        String queryString = "langs=" + languages + "&loc=" + requireContext().getResources().getConfiguration().locale.getCountry() +
                "&cats=" + categoriesString;
        String url = baseUrl + "?" + queryString;


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

    private void volleyQuestionResponse(String question_id, String gmae, String user_select, String correct) {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyGetQuestion");
        // pbDestination.setVisibility(View.VISIBLE);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = ApiProcessing.QuestionResponse.API_URL;
        JSONObject object = ApiProcessing.QuestionResponse.constructObject(question_id, gmae, user_select, correct);
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("device_id", deviceId);
                headers.put("Content-Type", "application/json");
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        requireActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

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
