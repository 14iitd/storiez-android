package in.android.storiez.ui.language;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import in.android.storiez.R;
import in.android.storiez.ui.home.HomeActivity;
import in.android.storiez.utils.StoriezApp;

public class LanguageActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String SELECTED_OPTION_KEY = "selectedOption";

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_screen);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        TextView hindiTxtView = findViewById(R.id.button_option1);
        TextView englishTxtView = findViewById(R.id.button_option2);
        ImageView forwardArrow = findViewById(R.id.forward_arrow);
        MaterialCardView hindiCard = findViewById(R.id.hindi_card);
        MaterialCardView englishCard = findViewById(R.id.english_card);

        forwardArrow.setOnClickListener(v -> {
            if (StoriezApp.getAppDataManager().isHindiSelection() || StoriezApp.getAppDataManager().isEnglishSelection()) {
                navigateToMainActivity();
            } else {
                Toast.makeText(this, "Please Select First", Toast.LENGTH_SHORT).show();
            }
        });

        hindiTxtView.setOnClickListener(v -> {
            saveSelectedOption(1);


            if (StoriezApp.getAppDataManager().isHindiSelection()) {
                hindiCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                hindiTxtView.setTextColor(getResources().getColor(android.R.color.black));
                hindiCard.setStrokeColor(getResources().getColor(android.R.color.black));
                StoriezApp.getAppDataManager().setHindiSelection(false);
            } else {
                hindiCard.setCardBackgroundColor(getResources().getColor(R.color.hindi_btn_bg));
                hindiTxtView.setTextColor(getResources().getColor(android.R.color.white));
                hindiCard.setStrokeColor(getResources().getColor(R.color.hindi_btn_bg));
                StoriezApp.getAppDataManager().setHindiSelection(true);
            }


//            navigateToMainActivity();
        });


        englishTxtView.setOnClickListener(v -> {
            saveSelectedOption(2);

            if (StoriezApp.getAppDataManager().isEnglishSelection()) {
                englishCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                englishCard.setStrokeColor(getResources().getColor(android.R.color.black));
                englishTxtView.setTextColor(getResources().getColor(android.R.color.black));
                StoriezApp.getAppDataManager().setEnglishSelection(false);
            } else {
                englishCard.setCardBackgroundColor(getResources().getColor(R.color.hindi_btn_bg));
                englishCard.setStrokeColor(getResources().getColor(R.color.hindi_btn_bg));
                englishTxtView.setTextColor(getResources().getColor(android.R.color.white));
                StoriezApp.getAppDataManager().setEnglishSelection(true);
            }
        });


//        hindiTxtView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedOption(1);
//                StoriezApp.getAppDataManager().setHindiSelection(true);
//                navigateToMainActivity();
//            }
//        });

//        englishTxtView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedOption(2);
//                navigateToMainActivity();
//                StoriezApp.getAppDataManager().setEnglishSelection(true);
//            }
//        });

        int savedOption = prefs.getInt(SELECTED_OPTION_KEY, 0);
        if (savedOption != 0) {
            // Highlight the previously selected option
            if (savedOption == 1) {
//                option1Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            } else if (savedOption == 2) {
//                option2Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }
        }
    }

    private void saveSelectedOption(int option) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SELECTED_OPTION_KEY, option);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity (NewScreenActivity)
    }
}