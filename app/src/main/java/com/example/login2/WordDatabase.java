package com.example.login2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Word.class},version = 5,exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {

    private static WordDatabase INSTANCE;
    static synchronized WordDatabase getDatabase(Context context){
        if (INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),WordDatabase.class,"Word_data").addMigrations(MIGRATION_4_5).build();
        }
        return  INSTANCE;
    }
    public abstract WordDao getWordDao();
    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word ADD COLUMN chinese_off INTEGER  NOT NULL DEFAULT 0");
        }
    };
    static final Migration MIGRATION3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE word_temp(id INTEGER PRIMARY KEY NOT NULL,engList_word TEXT,"+"chinese_meaning TEXT)");
            database.execSQL("INSERT INTO word_temp(id,engList_word,chinese_meaning)"+"SELECT id,engList_word,chinese_meaning FROM word");
            database.execSQL("DROP TABLE word");
            database.execSQL("ALTER TABLE word_temp RENAME to word");
        }
    };
}
