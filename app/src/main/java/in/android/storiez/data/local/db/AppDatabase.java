package in.android.storiez.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.android.storiez.data.local.db.dao.TopicsCategoryDao;
import in.android.storiez.data.local.model.ContentTopic;


@Database(entities = {
        ContentTopic.class}, version = 1, exportSchema = true)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;


    public abstract TopicsCategoryDao contentTopicDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "storiez")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
