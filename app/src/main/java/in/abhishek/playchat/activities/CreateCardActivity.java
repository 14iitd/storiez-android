package in.abhishek.playchat.activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.abhishek.playchat.R;
import in.abhishek.playchat.databinding.ActivityCreateCardBinding;
import in.abhishek.playchat.databinding.ActivityCreateQuestionBinding;
import in.abhishek.playchat.items.CreateQuestionItem;
import in.abhishek.playchat.utils.API_Details;
import in.abhishek.playchat.utils.ApiProcessing;
import in.abhishek.playchat.utils.BasicUtils;
import in.abhishek.playchat.utils.Constants;

public class CreateCardActivity extends AppCompatActivity {
    private ActivityCreateCardBinding binding;
    BasicUtils basicUtils;
    Context context;

    String questions,answers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityCreateCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }
    private void init(){
        context = this;

        basicUtils = new BasicUtils(context);
        // volleyGetQuestion();
        initView();
    }

    private void initView() {
        EditText question = binding.question;
        EditText answer = binding.answer;
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicUtils.showCustomAlert("You Should Filed the both text!");
            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions = question.getText().toString().trim();
                answers = answer.getText().toString().trim();
                if(!questions.isEmpty() && !answers.isEmpty()){
                    volleyCreateCard(questions,answers);
                } else {
                    basicUtils.showCustomAlert("pleases Enter question and answer");
                }
            }
        });
    }
    private void volleyCreateCard(String questions, String answers) {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyCreateCard");
        // pbDestination.setVisibility(View.VISIBLE);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = ApiProcessing.CreateCard.API_URL;
        JSONObject object = ApiProcessing.CreateCard.constructObject(questions,answers);
        Log.i(TAG, "volleyCreateCard : URL = " + url);
        Log.i(TAG, "volleyCreateCard : OBJECT = " + object.toString());
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
                        Log.i(TAG, "volleyCreateCard : Response = " + response);
                        try {
                            //questionItem = ApiProcessing.GetQuestion.parseResponse(response);
                            details.setResponse(response.toString());
                            basicUtils.showCustomAlert("Question Successfully Created!");
//                            initView(questionItem);
                            //pbDestination.setVisibility(View.GONE);
//                            Log.i(TAG, "S_version response = " + loginItem.getDevice_id());
                            // Log.i(TAG, "volleyGetQuestion : Response = " + response);
                            // Log.i(TAG, "volleyQuestionResponse : Response Length = " + questionItem.getQuestion_text());

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
                Log.e(TAG, "volleyCreateCardResponse : Error = " + error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("device_id", deviceId);
                headers.put("Content-Type","application/json");
                return headers;
            }

        };
        requestQueue.add(request);

    }
}