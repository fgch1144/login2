package com.example.login2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWord(Word...words);
    @Update
    void updateWord(Word... words);
    @Delete
    void deleteWord(Word... words);
    @Query("DELETE FROM WORD")
    void deleteAllWord();
    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    LiveData<List<Word>> selectAllWord();
    @Query("SELECT * FROM WORD WHERE engList_word LIKE :pattern ORDER BY ID DESC")
    LiveData<List<Word>> findWorMenu(String pattern);
}
