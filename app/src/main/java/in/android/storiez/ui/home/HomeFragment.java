package in.android.storiez.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.android.storiez.R;
import in.android.storiez.adapter.QuestionAdapter;
import in.android.storiez.base.BaseFragment;
import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.databinding.FragmentHomeMainBinding;
import in.android.storiez.items.QuestionItem;
import in.android.storiez.ui.home.post.CommentsBottomSheet;
import in.android.storiez.utils.API_Details;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.Constants;
import in.android.storiez.utils.StoriezApp;
import in.android.storiez.utils.Utils;

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


    int likeDislikeItemPos = -1;

    int currentPostItemPos = -1;

    String currentPostId = null;

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
                    Log.d(TAG, "onPageSelected: requesting volley ");
//                    fetchMoreData();
                }


                if (adapter != null) {
                    QuestionItem item = adapter.getPostItemByPos(position);
                    currentPostItemPos = position;
                    currentPostId = item.getId();
                    viewModel.getPostMetaInfo(currentPostId);

                    Log.d(TAG, "onPageSelected: post item data " + item);
                }
            }
        });

        init();

//        viewModel.getPostsObservable().observe(getViewLifecycleOwner(), posts -> {
//
//            if (posts != null) {
//                Log.d(TAG, "onViewCreated: we got some post data " + posts);
//            } else {
//                Log.d(TAG, "onViewCreated: we got no post data ");
//            }
//
//        });
//
//
//        viewModel.getPosts();

        topicsAdapter = new TopicsAdapter(topics);
        binding.topicRecyclerView.setAdapter(topicsAdapter);


        viewModel.getPostMetaInfoObservable().observe(getViewLifecycleOwner(), postMetaInfo -> {

            if (postMetaInfo != null) {

                if (currentPostItemPos != -1) {
                    if (currentPostId != null && currentPostId.equals(postMetaInfo.getPostId())) {
                        if (adapter != null) {
                            adapter.updatePostMetaInfo(currentPostItemPos, postMetaInfo);
                            currentPostId = null;
                            currentPostItemPos = -1;
                        } else {
                            currentPostId = null;
                            currentPostItemPos = -1;
                        }
                    }
                }
            }

        });


        viewModel.getContentTopicsObservable().observe(getViewLifecycleOwner(), topics -> {
            if (topics != null) {
                Log.d(TAG, "onViewCreated: got some data ");
                topicsAdapter.updateTopics(Utils.preProcessTopics(topics));

            }
        });

        viewModel.getIsLikedSuccessObservable().observe(getViewLifecycleOwner(), isLiked -> {
            if (isLiked) {
                Log.d(TAG, "onViewCreated: like a post successfully ");
                Toast.makeText(requireContext(), "Liked Successfully", Toast.LENGTH_SHORT).show();

                if (likeDislikeItemPos != -1) {
                    if (adapter != null) {
                        adapter.likePost(likeDislikeItemPos);
                        likeDislikeItemPos = -1;
                    } else {
                        likeDislikeItemPos = -1;
                    }
                }
            } else {
                likeDislikeItemPos = -1;
                Toast.makeText(requireContext(), "Retry", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onViewCreated: failed to Like post ");
            }
        });

        viewModel.getRemoveSuccessObservable().observe(getViewLifecycleOwner(), isRemoved -> {

            if (isRemoved) {
                Log.d(TAG, "onViewCreated: remove a post successfully ");
                Toast.makeText(requireContext(), "Removed Successfully", Toast.LENGTH_SHORT).show();

                if (likeDislikeItemPos != -1) {
                    if (adapter != null) {
                        adapter.removeLikeFromPost(likeDislikeItemPos);
                        likeDislikeItemPos = -1;
                    } else {
                        likeDislikeItemPos = -1;
                    }
                }
            } else {
                likeDislikeItemPos = -1;
                Toast.makeText(requireContext(), "Retry", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onViewCreated: failed to remove post ");
            }
        });

        viewModel.getTopicContents();

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
                if (isDataChanged) {
                    volleyGetQuestion();
                    isDataChanged = false;
                }
                // No need to handle
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // No need to handle
            }
        });

        setUpListeners();
        languageCardSelectionSetup();


        setUpMethodListeners();


    }

    private void setUpMethodListeners() {


        adapter.setOnCommentPostClicked(question -> {

            showCommentBottomSheet(question);
        });

        adapter.setOnLikePostClicked((question, pos) -> {
            if (question.isPostLiked()) {
                viewModel.removeLike(question.getId());
            } else {
                viewModel.likePost(question.getId());
            }
            likeDislikeItemPos = pos;
        });

        adapter.setOnSharePostClicked(question -> {

        });

    }

    private void showCommentBottomSheet(QuestionItem question) {
        CommentsBottomSheet bottomSheetDialog = CommentsBottomSheet.newInstance(question.getId());
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show(requireActivity().getSupportFragmentManager(), bottomSheetDialog.getTag());

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

        volleyGetQuestion();


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

                            details.setResponse(response.toString());
                            if (Constants.SUPER_USER)
                                details.show();
                            Log.i(TAG, "volleyGetQuestion : Response Length = " + branches.size());


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                details.setErrorResponse(error.toString());
                if (Constants.SUPER_USER)
                    details.show();

                Toast.makeText(context, "Time Out!", Toast.LENGTH_SHORT).show();
//                basicUtils.showCustomAlert("Timed Out!");
                Log.e(TAG, "volleyGetQuestion : Error = " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("device_id", deviceId);
                headers.put("user_id", deviceId);
                return headers;
            }
        };
        requestQueue.add(request);


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
