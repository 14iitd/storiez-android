package in.abhishek.playchat.utils;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.abhishek.playchat.items.LoginItem;
import in.abhishek.playchat.items.QuestionItem;

public class ApiProcessing {

    public static class Login {

        public static final String API_URL = Constants.API_LOGIN;


        public static LoginItem parseResponse(JSONObject response) throws Exception {
            LoginItem vItem = new LoginItem();

            JSONObject jo = (JSONObject) response.get(String.valueOf(0));
            vItem.set_id(jo.getString("_id"));
            vItem.setDevice_id(jo.getString("device_id"));
            vItem.setId(jo.getString("id"));
            return vItem;
        }
    }

    public static class GetQuestion {

        public static final String API_URL = Constants.API_GET_QUESTIONS;


        public static ArrayList<QuestionItem> parseResponse(JSONArray response) throws Exception {
            //QuestionItem vItem = new QuestionItem();
            ArrayList<QuestionItem> branchList = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject jo = (JSONObject) response.get(i);
                QuestionItem vItem = new QuestionItem();
                vItem.setDevice_id(jo.getString("device_id"));
                vItem.setQuestion_text(jo.getString("question_text"));
                vItem.setGame(jo.getString("game"));
                if(jo.getString("game").equals("mcq")){
                    vItem.setOptions((jo.getJSONArray("options")));
                    vItem.setCorrect_answer(jo.getString("correct_answer"));
                    vItem.setAnswer_index(jo.getInt("answer_index"));
                    vItem.setQuiz_name(jo.getString("quiz_name"));
                }
                if(jo.getString("game").equals("flashcard")){
                    vItem.setAnswer(jo.getString("answer"));
                }
                vItem.setCategory(jo.getString("category"));
                vItem.setAge(jo.getString("age"));
                vItem.setDifficulty(jo.getString("difficulty"));
                vItem.setTopic(jo.getString("topic"));
                vItem.setId(jo.getString("id"));
                vItem.setColor(jo.getString("color"));
                vItem.setAccuracy(jo.getString("accuracy"));
                vItem.setPlayed(jo.getString("played"));
                Log.d(TAG, "parseResponse: " + vItem.toString());
                branchList.add(vItem);
            }
            Log.d(TAG, "parseResponse: " + branchList.toString());
            return branchList;
        }
    }

    public static class QuestionResponse {

        public static final String API_URL = Constants.API_POST_QUESTIONS_RESPONSE;

        public static JSONObject constructObject(String question_id, String gmae, String user_select, String correct) {
            JSONObject object = new JSONObject();
            try {
                object.put("question_id", question_id);
                object.put("game", gmae);
                object.put("user_select", user_select);
                object.put("correct", correct);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }
    }

    public static class CreateQuestion {


        public static final String API_URL = Constants.API_POST_CREATE_QUESTIONS;

        public static JSONObject constructObject(String question, JSONArray options, int answer_index) {
            JSONObject object = new JSONObject();
            try {
                object.put("question", question);
                object.put("options", options);
                object.put("answer_index", answer_index);
                object.put("game", "mcq");
                object.put("played_by", "");
                object.put("difficulty", "");
                object.put("category", "");
                object.put("topic", "");
                object.put("age", "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }
    }


}
