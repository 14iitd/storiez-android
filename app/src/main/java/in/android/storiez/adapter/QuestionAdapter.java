package in.android.storiez.adapter;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.android.storiez.R;
import in.android.storiez.items.QuestionItem;
import in.android.storiez.items.RepliesItem;
import in.android.storiez.utils.API_Details;
import in.android.storiez.utils.ApiProcessing;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.Constants;
import in.android.storiez.utils.StoriezApp;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>{
    private List<QuestionItem> questionItems;
    static CommentAdapter adapter;
    static ArrayList<RepliesItem> areaList;
    static RecyclerView lvComments;

    static Context context;
    static BasicUtils basicUtils;
    public QuestionAdapter(Context context)
    {
        questionItems =new ArrayList<>();
        areaList = new ArrayList<>();
        this.context=context;
        basicUtils = new BasicUtils(StoriezApp.getInstance());
    }

    public void addData(List<QuestionItem> newData) {
        // Add the new data to the existing dataset
//        questionItems =new ArrayList<>();
        questionItems.addAll(newData);
        notifyDataSetChanged();
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
        holder.setDataView(questionItems.get(position));

    }

    @Override
    public int getItemCount()
    {
        return questionItems.size();
    }

    static class QuestionHolder extends RecyclerView.ViewHolder {

        TextView question, question1,q1,q2,q3,q4,played,accuracy;
        WebView webView;
        ImageView imageView,replies;
        private boolean isFront = true;
        LinearLayout ll,ll1,lltap;
        RelativeLayout rl;
        int userselect;
        boolean correct=false;

        public QuestionHolder(@NonNull View itemView)
        {
            super(itemView);
            //context=this;

            // getting all this from the
            // java file we created in above steps
            question = itemView.findViewById(R.id.question);
            question1 = itemView.findViewById(R.id.question1);
            webView = itemView.findViewById(R.id.webView);
            imageView = itemView.findViewById(R.id.image);
            replies = itemView.findViewById(R.id.replies);
            q1 = itemView.findViewById(R.id.q1);
            q2 = itemView.findViewById(R.id.q2);
            q3 = itemView.findViewById(R.id.q3);
            q4 = itemView.findViewById(R.id.q4);
            played = itemView.findViewById(R.id.played);
            accuracy = itemView.findViewById(R.id.accuracy);
            ll = itemView.findViewById(R.id.ll);
            ll1 = itemView.findViewById(R.id.ll1);
            lltap = itemView.findViewById(R.id.tap);
            rl = itemView.findViewById(R.id.rl);
        }

        void setDataView(QuestionItem questionItem)
        {

            if (Objects.equals(questionItem.getType(), "post")){
                ll1.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                question.setVisibility(View.VISIBLE);
                question1.setVisibility(View.GONE);
                lltap.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                if (Objects.equals(questionItem.getImage_url(), "")){
                    Picasso.get().load("https://static.vecteezy.com/system/resources/previews/004/141/669/original/no-photo-or-blank-image-icon-loading-images-or-missing-image-mark-image-not-available-or-image-coming-soon-sign-simple-nature-silhouette-in-frame-isolated-illustration-vector.jpg").into(imageView);
                } else {
                    Picasso.get().load(questionItem.getImage_url()).into(imageView);
                }
                volleyGetComments(questionItem.getId());
                replies.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomDialog(questionItem.getId());
                    }
                });

            }
            if(Objects.equals(questionItem.getType(), "html5")){

                ll1.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
                question.setVisibility(View.GONE);
                question1.setVisibility(View.GONE);
                lltap.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        //progressBar.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }
                });
            webView.loadUrl(questionItem.getSource());
            webView.getSettings().setJavaScriptEnabled(true);

            }
            if (Objects.equals(questionItem.getType(), "flashcard")){
                ll1.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                question.setVisibility(View.GONE);
                question1.setVisibility(View.VISIBLE);
                question1.setText(questionItem.getQuestion_text());
                lltap.setVisibility(View.VISIBLE);
                //basicUtils.showLongCustomAlert("Tap the Screen to know the Answer!");
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(question, "scaleX", 1f, 0f);
                        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(question, "scaleX", 0f, 1f);
                        oa1.setInterpolator(new DecelerateInterpolator());
                        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                        if (isFront) {
                            oa1.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    question1.setText(questionItem.getAnswer());
                                    lltap.setVisibility(View.GONE);
                                    oa2.start();
                                }
                            });
                            oa1.start();isFront = false;
                            //ll.setBackgroundColor(Color.GREEN);
                            ll.setBackgroundColor(Color.parseColor("#"+questionItem.getColor()));
                        }else {
                            oa1.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    question1.setText(questionItem.getQuestion_text());
                                    oa2.start();
                                }
                            });
                            oa1.start();
                            isFront = true;
                            ll.setBackgroundColor(Color.parseColor("#"+questionItem.getColor()));
                        }

                    }
                });
            }
            if (Objects.equals(questionItem.getType(), "mcq")){
                rl.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                question1.setVisibility(View.GONE);
                lltap.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
                question.setVisibility(View.VISIBLE);
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
                q1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userselect =0;
                        if (userselect==questionItem.getAnswer_index()){
                            correct=true;
                        }else {
                            correct=false;
                        }
                        volleyQuestionResponse(questionItem.getId(),questionItem.getType(),String.valueOf(userselect),String.valueOf(correct));
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
                        volleyQuestionResponse(questionItem.getId(),questionItem.getType(),String.valueOf(userselect),String.valueOf(correct));
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
                        volleyQuestionResponse(questionItem.getId(),questionItem.getType(),String.valueOf(userselect),String.valueOf(correct));
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
                        volleyQuestionResponse(questionItem.getId(),questionItem.getType(),String.valueOf(userselect),String.valueOf(correct));
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
            float scale = itemView.getContext().getResources().getDisplayMetrics().density;
            question.setCameraDistance(8000 * scale);
            question.setText(questionItem.getQuestion_text());
            accuracy.setText("Accuracy "+questionItem.getAccuracy()+"%");
            played.setText("Played "+questionItem.getPlayed());
        ll.setBackgroundColor(Color.parseColor("#"+questionItem.getColor()));
        //binding.ll2.setBackgroundColor(Color.parseColor("#"+questionItem1.getColor()));

        }

    }

    private static void showBottomDialog(String id) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comment_sheet_layout);

//        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
//        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        EditText comment = dialog.findViewById(R.id.commenttext);
        ImageView send = dialog.findViewById(R.id.send);
        ProgressBar progressBar = dialog.findViewById(R.id.pb);
        lvComments= dialog.findViewById(R.id.replies_recycler_view);
        lvComments.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommentAdapter(context, areaList);
        //Collections.sort(areaList, RepliesItem.SORT_BY_ALPHABET);
        Collections.reverse(areaList);
        lvComments.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text= comment.getText().toString().trim();
                if (!text.isEmpty()){
                    volleyPostComment(text,id,progressBar);
                    volleyGetComments(id);
                    Toast.makeText(StoriezApp.getInstance(), "Done", Toast.LENGTH_SHORT).show();
//                    Commented  by KK
//                    basicUtils.showCustomAlert("Done");
                   comment.setText("");
                } else {
                    //                    Commented  by KK
//                    basicUtils.showCustomAlert("Please Enter text!");
                    Toast.makeText(StoriezApp.getInstance(), "Please Enter Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

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

    private static void volleyPostComment(String text, String id,ProgressBar progressBar) {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyPostComment");
        progressBar.setVisibility(View.VISIBLE);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = Constants.API_POST_COMMENT+id+"/replies";
        JSONObject object = ApiProcessing.PostComment.constructObject(text);
        Log.i(TAG, "volleyPostComment : URL = " + url);
        Log.i(TAG, "volleyPostComment : OBJECT = " + object.toString());
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
                        Log.i(TAG, "volleyPostComment : Response = " + response);
                        try {
                            //questionItem = ApiProcessing.GetQuestion.parseResponse(response);
                            details.setResponse(response.toString());
//                            if (response.toString().equals("true")){
//                            }
//                            initView(questionItem);
                            progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
//                dialog.dismiss();
                // basicUtils.showCustomAlert("Timed Out!");
                Log.e(TAG, "volleyPostComment : Error = " + error.toString());
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
    }
    private static void volleyGetComments(String id) {
        final API_Details details = new API_Details(context);
        details.setAPI_Name("volleyGetQuestion");
        //basicUtils.showSmallProgressDialog(false);
        String deviceId = BasicUtils.getDeviceId(context);
        String url = Constants.API_POST_COMMENT+id+"/replies";
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

                            areaList = ApiProcessing.PostComment.parseResponse(response);
                            adapter.addData(areaList);
                            adapter = new CommentAdapter(context, areaList);
                            Collections.reverse(areaList);
                            lvComments.setAdapter(adapter);
                            details.setResponseLength(areaList.size());
                            //adapter.notifyDataSetChanged();
                            //initView(questionItem);
//
                            // Notify the adapter about the data change
                            //adapter.notifyDataSetChanged();
//                            if (areaList.size() == 0) {
//                                basicUtils.showCustomAlert("No Comments present");
//                                volleyGetComments(id);
//                            }
                            details.setResponse(response.toString());
                            if (Constants.SUPER_USER)
                                details.show();
                            //pbDestination.setVisibility(View.GONE);
//                            Log.i(TAG, "S_version response = " + loginItem.getDevice_id());
                            // Log.i(TAG, "volleyGetQuestion : Response = " + response);
 //                           Log.i(TAG, "volleyGetQuestion : Response Length = " + branches.size());

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
}
