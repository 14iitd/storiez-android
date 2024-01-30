package in.abhishek.playchat.items;

import java.util.List;

public class CreateQuestionItem {
    String question;
    List<String> options;
    int answer_index;
    String game;
    long played_by;
    String difficulty;
    String category;
    String topic;
    int age;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getAnswer_index() {
        return answer_index;
    }

    public void setAnswer_index(int answer_index) {
        this.answer_index = answer_index;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public long getPlayed_by() {
        return played_by;
    }

    public void setPlayed_by(long played_by) {
        this.played_by = played_by;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
