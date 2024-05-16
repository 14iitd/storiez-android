package in.android.storiez.activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import in.android.storiez.R;
import in.android.storiez.databinding.ActivityCreateQuestionBinding;
import in.android.storiez.databinding.DialogQuestionBinding;
import in.android.storiez.items.CreateQuestionItem;
import in.android.storiez.utils.API_Details;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.Constants;

public class CreateQuestionActivity extends AppCompatActivity {
    private ActivityCreateQuestionBinding binding;
    BasicUtils basicUtils;
    CreateQuestionItem createQuestionItem;
    int user_select=-1;
    ProgressBar pbDestination;
    Context context;

    String questions,op1,op2,op3,op4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }
    private void init(){
        context = this;
        createQuestionItem= new CreateQuestionItem();
        basicUtils = new BasicUtils(context);
       // volleyGetQuestion();
        initView();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        EditText question = binding.question;
        EditText q1 = binding.q1;
        EditText q2 = binding.q2;
        EditText q3 = binding.q3;
        EditText q4 = binding.q4;
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.cb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                user_select=0;
                uncheckOtherCheckboxes(binding.cb1, binding.cb2, binding.cb3, binding.cb4);
            }
        });

        binding.cb2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                user_select=1;
                uncheckOtherCheckboxes(binding.cb2, binding.cb1, binding.cb3, binding.cb4);
            }
        });

        binding.cb3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                user_select=2;
                uncheckOtherCheckboxes(binding.cb3, binding.cb1, binding.cb2, binding.cb4);
            }
        });

        binding.cb4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                user_select=3;
                uncheckOtherCheckboxes(binding.cb4, binding.cb1, binding.cb2, binding.cb3);
            }
        });
        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicUtils.showCustomAlert("Select Correct Answer!");
            }
        });





        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 questions = question.getText().toString().trim();
                 op1 = q1.getText().toString().trim();
                 op2 = q2.getText().toString().trim();
                 op3 = q3.getText().toString().trim();
                 op4 = q4.getText().toString().trim();
                JSONArray options = new JSONArray();
                options.put(op1);
                options.put(op2);
                options.put(op3);
                options.put(op4);
                if(!questions.isEmpty() && !op1.isEmpty()&& !op2.isEmpty()&& !op3.isEmpty()&& !op4.isEmpty()&& user_select!=-1){
                    volleyCreateQuestion(questions,options,user_select);
                } else {
                    basicUtils.showCustomAlert("pleases select all the from!");
                }
            }
        });

        q1.setBackgroundResource(R.drawable.bg_question);
        q2.setBackgroundResource(R.drawable.bg_question);
        q3.setBackgroundResource(R.drawable.bg_question);
        q4.setBackgroundResource(R.drawable.bg_question);


    }
    private void uncheckOtherCheckboxes(CheckBox currentCheckbox, CheckBox... checkboxes) {
        for (CheckBox checkBox : checkboxes) {
            if (checkBox != currentCheckbox) {
                checkBox.setChecked(false);
            }
        }
    }

    private void volleyCreateQuestion(String question_id, JSONArray optios, int user_select) {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyCreateQuestion");
        // pbDestination.setVisibility(View.VISIBLE);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = ApiProcessing.CreateQuestion.API_URL;
        JSONObject object = ApiProcessing.CreateQuestion.constructObject(question_id,optios,user_select);
        Log.i(TAG, "volleyCreateQuestion : URL = " + url);
        Log.i(TAG, "volleyCreateQuestion : OBJECT = " + object.toString());
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

        };
        requestQueue.add(request);

    }


    private void dialogEnterDetails(String str,TextView textView) {
        final Dialog dialog = new Dialog(context);
        final DialogQuestionBinding b = DialogQuestionBinding.inflate(getLayoutInflater());
        basicUtils.setDialogAttributes(dialog, false, false, b.getRoot(), 26, 27);

        b.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1 = textView.getText().toString().trim();
            }
        });

        b.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    }