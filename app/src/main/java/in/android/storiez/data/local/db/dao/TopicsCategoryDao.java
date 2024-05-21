package in.android.storiez.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import in.android.storiez.data.local.model.ContentTopic;

@Dao
public interface TopicsCategoryDao {

    @Insert
    void insert(ContentTopic topic);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ContentTopic> topics);



    @Query("SELECT * FROM content_topic ")
    List<ContentTopic> getTopics();

    @Query("DELETE FROM content_topic")
    void deleteAll();

    @Query("SELECT * FROM content_topic WHERE isSelected = 1")
    List<ContentTopic> getSelectedTopics();


    @Query("UPDATE content_topic SET isSelected = :value WHERE id = :id")
    void updateSelection(int id, boolean value);

}
