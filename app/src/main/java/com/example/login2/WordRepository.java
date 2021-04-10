package com.example.login2;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private final LiveData<List<Word>> allWordLive;
    WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        allWordLive = wordDao.selectAllWord();
    }

    public LiveData<List<Word>> getAllWordLive() {
        return allWordLive;
    }

    LiveData<List<Word>> findWorMenu(String pattern) {
        return wordDao.findWorMenu("%" + pattern + "%");
    }

    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private final WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWord(words);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word, Void, Void> {
        private final WordDao wordDao;

        public UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWord(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private final WordDao wordDao;

        public DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWord(words);
            return null;
        }
    }

    void insertWords(Word... words) {
        new InsertAsyncTask(wordDao).execute(words);
    }

    void updateWords(Word... words) {
        new UpdateAsyncTask(wordDao).execute(words);
    }

    void deleteWords(Word... words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }
    void deleteAllWords() {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WordDao wordDao;

        public DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWord();
            return null;
        }
    }
}
