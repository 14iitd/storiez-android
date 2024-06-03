package in.android.storiez.ui.home;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.android.storiez.R;
import in.android.storiez.activities.CreateCardActivity;
import in.android.storiez.activities.CreateQuestionActivity;
import in.android.storiez.activities.MainActivity;
import in.android.storiez.activities.MoreActivity;
import in.android.storiez.activities.MultiPlayerActivity;
import in.android.storiez.activities.ProfileActivity;
import in.android.storiez.activities.TakePictureActivity;
import in.android.storiez.adapter.QuestionAdapter;
import in.android.storiez.base.BaseActivity;
import in.android.storiez.databinding.ActivityHomeBinding;
import in.android.storiez.databinding.ActivityMainBinding;
import in.android.storiez.items.QuestionItem;
import in.android.storiez.ui.ProfileFragment;
import in.android.storiez.utils.API_Details;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.Constants;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    ViewPager2 videosViewPager;
    QuestionAdapter adapter;
    BasicUtils basicUtils;
    QuestionItem questionItem;
    ArrayList<QuestionItem> branches;
    ArrayList<String> branchNames;
    ProgressBar pbDestination;
    Context context;
    int userselect;

    @Override
    public ViewBinding initViewBinding(LayoutInflater inflater) {
        return ActivityHomeBinding.inflate(inflater);
    }


    private BottomNavigationView bottomNavigationView;
    private int selectedItemId = R.id.nav_home; // Default selected item

    HomeFragment homeFragment = new HomeFragment(this);


    boolean isOnHome = true;
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        setUpListeners();

    }

    private void setUpListeners() {


        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
                selectedItemId = item.getItemId(); // Storing the selected item ID
                Fragment selectedFragment = null;

                switch (selectedItemId) {
                    case R.id.nav_home:
                        selectedFragment = homeFragment;

                        if (isOnHome) {
                            homeFragment.scrollToTheTop();
                        }
                        isOnHome = true;
                        Log.d(TAG, "onNavigationItemSelected: home ");
                        break;

                    case R.id.nav_search:
                        selectedFragment = profileFragment;
                        isOnHome = false;
                        Log.d(TAG, "onNavigationItemSelected: search ");
                        break;

                    case R.id.nav_create:
                        selectedFragment = profileFragment;
                        isOnHome = false;
                        Log.d(TAG, "onNavigationItemSelected: Create ");
                        // Initialize your CommunitiesFragment
                        break;

                    case R.id.nav_workspaces:
                        selectedFragment = profileFragment;
                        isOnHome = false;
                        // Initialize NotificationFragment
                        break;

                    case R.id.nav_profile:
                        selectedFragment = profileFragment;
                        isOnHome = false;
                        // Initialize  MessageFragment
                        break;
                }

                // Performing fragment transaction
                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            }
        });
    }

}
