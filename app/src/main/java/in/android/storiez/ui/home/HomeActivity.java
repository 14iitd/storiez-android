package in.android.storiez.ui.home;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import in.android.storiez.R;
import in.android.storiez.base.BaseActivity;
import in.android.storiez.databinding.ActivityHomeBinding;
import in.android.storiez.items.QuestionItem;
import in.android.storiez.ui.ProfileFragment;
import in.android.storiez.ui.createContent.CreateContentActivity;
import in.android.storiez.ui.createContent.CreateContentBottomSheet;
import in.android.storiez.ui.createContent.CreateContentFragment;
import in.android.storiez.utils.BasicUtils;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    public static final String EXTRAS_SHARED_POST_ID =  "extras_shared_post_id";

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

    HomeFragment homeFragment = new HomeFragment();

    boolean isOnHome = true;
    ProfileFragment profileFragment = new ProfileFragment();
    CreateContentFragment createContentFragment = new CreateContentFragment();

    CreateContentBottomSheet createContentBottomSheet;

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

//                    case R.id.nav_search:
//                        selectedFragment = profileFragment;
//                        isOnHome = false;
//                        Log.d(TAG, "onNavigationItemSelected: search ");
//                        break;

                    case R.id.nav_create:
                        showCreateContentBottomSheet();
                        isOnHome = false;
                        Log.d(TAG, "onNavigationItemSelected: Create ");
                        return false;

//                    case R.id.nav_workspaces:
//                        selectedFragment = profileFragment;
//                        isOnHome = false;
//                        // Initialize NotificationFragment
//                        break;

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

    private void redirectToCreateContentActivity() {
        startActivity(new Intent(HomeActivity.this, CreateContentActivity.class));
    }

    private void showCreateContentBottomSheet() {
        CreateContentBottomSheet createContentBottomSheet = CreateContentBottomSheet.newInstance();
        createContentBottomSheet.setCancelable(true);
        createContentBottomSheet.show(getSupportFragmentManager(), createContentBottomSheet.getTag());
    }
}
