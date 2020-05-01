package com.jayare.jappanese.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jayare.jappanese.category.CategoryEnum;
import com.jayare.jappanese.definition.Definition;

@Database(entities = Definition.class, version = 1)
public abstract class DefinitionDatabase extends RoomDatabase {
    private static DefinitionDatabase instance;

    public abstract DefinitionDao definitionDao();

    //Singleton instance
    public static synchronized DefinitionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DefinitionDatabase.class, "definition_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DefinitionDao definitionDao;

        private PopulateDbAsyncTask(DefinitionDatabase db) {
            definitionDao = db.definitionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //definitionDao.insert(new Definition("Hi"," 今日は","こんにちは", CategoryEnum.VOCABULARY.getCategory()));
            definitionDao.insert(new Definition("Where is the toilet?", "トイレはどこですか", "", CategoryEnum.PHRASES.getCategory()));
            return null;
        }
    }
}
