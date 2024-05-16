package in.android.storiez.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import in.android.storiez.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class LanguageActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String SELECTED_OPTION_KEY = "selectedOption";

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_screen);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Button option1Button = findViewById(R.id.button_option1);
        Button option2Button = findViewById(R.id.button_option2);

        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedOption(1);
                navigateToMainActivity();
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedOption(2);
                navigateToMainActivity();
            }
        });

        int savedOption = prefs.getInt(SELECTED_OPTION_KEY, 0);
        if (savedOption != 0) {
            // Highlight the previously selected option
            if (savedOption == 1) {
                option1Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            } else if (savedOption == 2) {
                option2Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }
        }
    }

    private void saveSelectedOption(int option) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SELECTED_OPTION_KEY, option);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity (NewScreenActivity)
    }
}