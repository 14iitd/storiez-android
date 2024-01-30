package in.abhishek.playchat.adapter;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.graphics.Color;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.abhishek.playchat.R;
import in.abhishek.playchat.items.QuestionItem;
import in.abhishek.playchat.utils.API_Details;
import in.abhishek.playchat.utils.ApiProcessing;
import in.abhishek.playchat.utils.BasicUtils;
import in.abhishek.playchat.utils.Constants;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>{
    private List<QuestionItem> questionItems;

    static Context context;
    public QuestionAdapter(Context context,List<QuestionItem> questionItems)
    {
        this.questionItems = questionItems;
        this.context=context;
    }

    public void addData(List<QuestionItem> newData) {
        // Add the new data to the existing dataset
        questionItems.addAll(newData);
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new QuestionHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        // the layout that
                        // we created above
                        R.layout.question_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position)
    {
        holder.setDataVideo(questionItems.get(position));
    }

    @Override
    public int getItemCount()
    {
        return questionItems.size();
    }

    static class QuestionHolder extends RecyclerView.ViewHolder {

        TextView question, q1,q2,q3,q4,played,accuracy;
        LinearLayout ll;
        int userselect;
        boolean correct=false;

        public QuestionHolder(@NonNull View itemView)
        {
            super(itemView);
            //context=this;

            // getting all this from the
            // java file we created in above steps
            question = itemView.findViewById(R.id.question);
            q1 = itemView.findViewById(R.id.q1);
            q2 = itemView.findViewById(R.id.q2);
            q3 = itemView.findViewById(R.id.q3);
            q4 = itemView.findViewById(R.id.q4);
            played = itemView.findViewById(R.id.played);
            accuracy = itemView.findViewById(R.id.accuracy);
            ll = itemView.findViewById(R.id.ll
            );
        }

        void setDataVideo(QuestionItem questionItem)
        {
            question.setText(questionItem.getQuestion_text());
            accuracy.setText("Accuracy "+questionItem.getAccuracy()+"%");
            played.setText("Played "+questionItem.getPlayed());

            String qu1 = questionItem.getOptions().opt(0).toString();
            String qu2 = questionItem.getOptions().opt(1).toString();
            String qu3 = questionItem.getOptions().opt(2).toString();
            String qu4 = questionItem.getOptions().opt(3).toString();
            q1.setText(qu1);
            q2.setText(qu2);
            q3.setText(qu3);
            q4.setText(qu4);
                    q1.setBackgroundResource(R.drawable.bg_question);
        q2.setBackgroundResource(R.drawable.bg_question);
        q3.setBackgroundResource(R.drawable.bg_question);
        q4.setBackgroundResource(R.drawable.bg_question);
        String ans = questionItem.getCorrect_answer();


        ll.setBackgroundColor(Color.parseColor("#"+questionItem.getColor()));
        //binding.ll2.setBackgroundColor(Color.parseColor("#"+questionItem1.getColor()));

        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userselect =0;
                if (userselect==questionItem.getAnswer_index()){
                    correct=true;
                }else {
                    correct=false;
                }
                volleyQuestionResponse(questionItem.getId(),questionItem.getGame(),String.valueOf(userselect),String.valueOf(correct));
                q2.setClickable(false);
                q3.setClickable(false);
                q4.setClickable(false);
                if(Objects.equals(ans, qu1)){
                    q1.setBackgroundResource(R.drawable.bg_correct);
                    //basicUtils.showCustomAlert("Right Answer!");
                }else {
                    //basicUtils.showCustomAlert("Wrong Answer!");
                    q1.setBackgroundResource(R.drawable.bg_incorrect);
                    if(Objects.equals(ans, qu2)){
                        q2.setBackgroundResource(R.drawable.bg_correct);
                    }else if(Objects.equals(ans, qu3)){
                        q3.setBackgroundResource(R.drawable.bg_correct);
                    }else {
                        q4.setBackgroundResource(R.drawable.bg_correct);
                    }
                }
            }
        });

        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userselect=1;
                if (userselect==questionItem.getAnswer_index()){
                    correct=true;
                }else {
                    correct=false;
                }
                volleyQuestionResponse(questionItem.getId(),questionItem.getGame(),String.valueOf(userselect),String.valueOf(correct));
                q1.setClickable(false);
                q3.setClickable(false);
                q4.setClickable(false);
                if(Objects.equals(ans, qu2)){
                    q2.setBackgroundResource(R.drawable.bg_correct);
                    //basicUtils.showCustomAlert("Right Answer!");
                }else {
                    //basicUtils.showCustomAlert("Wrong Answer!");
                    q2.setBackgroundResource(R.drawable.bg_incorrect);
                    if(ans==qu1){
                        q1.setBackgroundResource(R.drawable.bg_correct);
                    }else if(ans==qu3){
                        q3.setBackgroundResource(R.drawable.bg_correct);
                    }else {
                        q4.setBackgroundResource(R.drawable.bg_correct);
                    }
                }
            }
        });

        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userselect=2;
                if (userselect==questionItem.getAnswer_index()){
                    correct=true;
                }else {
                    correct=false;
                }
                volleyQuestionResponse(questionItem.getId(),questionItem.getGame(),String.valueOf(userselect),String.valueOf(correct));
                q2.setClickable(false);
                q1.setClickable(false);
                q4.setClickable(false);
                if(Objects.equals(ans, qu3)){
                    q3.setBackgroundResource(R.drawable.bg_correct);
                    //basicUtils.showCustomAlert("Right Answer!");
                }else {
                   // basicUtils.showCustomAlert("Wrong Answer!");
                    q3.setBackgroundResource(R.drawable.bg_incorrect);
                    if(Objects.equals(ans, qu1)){
                        q1.setBackgroundResource(R.drawable.bg_correct);
                    }else if(Objects.equals(ans, qu2)){
                        q2.setBackgroundResource(R.drawable.bg_correct);
                    }else {
                        q4.setBackgroundResource(R.drawable.bg_correct);
                    }
                }
            }
        });

        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userselect=3;
                if (userselect==questionItem.getAnswer_index()){
                    correct=true;
                }else {
                    correct=false;
                }
                volleyQuestionResponse(questionItem.getId(),questionItem.getGame(),String.valueOf(userselect),String.valueOf(correct));
                q2.setClickable(false);
                q3.setClickable(false);
                q1.setClickable(false);
                if(Objects.equals(ans, qu4)){
                    q4.setBackgroundResource(R.drawable.bg_correct);
                    //basicUtils.showCustomAlert("Right Answer!");
                }else {
                    //basicUtils.showCustomAlert("Wrong Answer!");
                    q4.setBackgroundResource(R.drawable.bg_incorrect);
                    if(Objects.equals(ans, qu2)){
                        q2.setBackgroundResource(R.drawable.bg_correct);
                    }else if(Objects.equals(ans, qu3)){
                        q3.setBackgroundResource(R.drawable.bg_correct);
                    }else {
                        q1.setBackgroundResource(R.drawable.bg_correct);
                    }
                }
            }
        });
        }

    }
    private static void volleyQuestionResponse(String question_id, String gmae, String user_select, String correct) {
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
                            //Log.i(TAG, "volleyQuestionResponse : Response Length = " + questionItem.getQuestion_text());

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
