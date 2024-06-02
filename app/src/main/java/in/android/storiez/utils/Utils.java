package in.android.storiez.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.android.storiez.data.local.model.ContentTopic;

public class Utils {
    
    private static final String TAG = "Utils";
    public static List<ContentTopic> preProcessTopics(List<ContentTopic> topics) {

        List<ContentTopic> previousTopics = StoriezApp.getAppDatabase().contentTopicDao().getTopics();

        if (previousTopics.size() > 0) {
            for (ContentTopic topic : topics) {
                for (ContentTopic previousTopic : previousTopics) {
                    Log.d(TAG, "preProcessTopics: comparing values "+topic.getCategory()+" "+previousTopic.getCategory()+"  "+previousTopic.isSelected());
                    if (topic.getCategory().equals(previousTopic.getCategory()) &&
                            previousTopic.isSelected()) {
                        topic.setSelected(true);
                        Log.d(TAG, "preProcessTopics: setting values for  "+topic.getCategory());
                    } else {
                        Log.d(TAG, "preProcessTopics: setting values for  "+previousTopic.getCategory()+" "+previousTopic.isSelected());
                    }
                }
            }
            Log.d(TAG, "preProcessTopics: if ");
            return topics;

        } else {
            Log.d(TAG, "preProcessTopics: eslse ");
            return topics;
        }
    }
}
