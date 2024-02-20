package in.abhishek.playchat.items;

import org.json.JSONArray;

public class QuestionItem {
    String device_id;
    String question_text;
    String game;
    JSONArray options;
    String correct_answer;
    int answer_index;
    String category;
    String age;
    String difficulty;
    String quiz_name;
    String topic;
    String id;
    String color;
    String accuracy;
    String played;
    String answer;
    String source;
    String image_url;
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public JSONArray getOptions() {
        return options;
    }

    public void setOptions(JSONArray options) {
        this.options = options;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public int getAnswer_index() {
        return answer_index;
    }

    public void setAnswer_index(int answer_index) {
        this.answer_index = answer_index;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    @Override
    public String toString() {
        return "QuestionItem{" +
                "device_id='" + device_id + '\'' +
                ", question_text='" + question_text + '\'' +
                ", game='" + game + '\'' +
                ", options=" + options +
                ", correct_answer='" + correct_answer + '\'' +
                ", answer_index='" + answer_index + '\'' +
                ", category='" + category + '\'' +
                ", age='" + age + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", quiz_name='" + quiz_name + '\'' +
                ", topic='" + topic + '\'' +
                ", id='" + id + '\'' +
                ", color='" + color + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", played='" + played + '\'' +
                '}';
    }
}
