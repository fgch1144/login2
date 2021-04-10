package com.example.login2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private final WordRepository wordRepository;
    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }
    public LiveData<List<Word>> getAllWordList() {
        return wordRepository.getAllWordLive();
    }
    LiveData<List<Word>> findWorMenu(String pattern){
        return wordRepository.findWorMenu(pattern);
    }

    void insertWords(Word...words){
        wordRepository.insertWords(words);
    }
    void updateWords(Word...words){
        wordRepository.updateWords(words);
    }
    void deleteWords(Word...words){
        wordRepository.deleteWords(words);
    }
    void deleteAllWords(){
        wordRepository.deleteAllWords();
    }
}
