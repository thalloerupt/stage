package com.thallo.stage.database.bookmark;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkRepository;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkDao;
import com.thallo.stage.database.history.History;

import java.util.List;

public class BookmarkRepository {
    BookmarkDao bookmarkDao;
    LiveData<List<Bookmark>> allBookmarkLive;
    BookmarkRepository(Context context)
    {
        com.thallo.stage.database.StageData stageData = com.thallo.stage.database.StageData.getDatabase(context.getApplicationContext());
        bookmarkDao = stageData.getBookmarkDao();
        allBookmarkLive = bookmarkDao.getAllBookmarksLive();
    }

    public LiveData<List<Bookmark>> getAllBookmarkLive() {
        return allBookmarkLive;
    }

    void insertBookmark(Bookmark... bookmarks) {
        new BookmarkRepository.InsertAsyncTask(bookmarkDao).execute(bookmarks);
    }

    void updateBookmark(Bookmark... bookmarks) {
        new BookmarkRepository.UpdateAsyncTask(bookmarkDao).execute(bookmarks);
    }

    void deleteBookmark(Bookmark... bookmarks) {
        new BookmarkRepository.DeleteAsyncTask(bookmarkDao).execute(bookmarks);
    }

    void deleteAllbookmarks() {
        new BookmarkRepository.DeleteAllAsyncTask(bookmarkDao).execute();
    }


    LiveData<List<Bookmark>> findBookmarksWithPattern(String pattern){
        return bookmarkDao.findBookmarksWithPattern("%"+pattern+"%");
    }
    LiveData<List<Bookmark>> findWordsWithTitle(String pattern){
        return bookmarkDao.findBookmarksWithTitle(pattern);
    }
    LiveData<List<Bookmark>> findWordsWithShow(Boolean pattern){
        return bookmarkDao.findBookmarksWithShow(pattern);
    }




    static class InsertAsyncTask extends AsyncTask<Bookmark, Void, Void> {
        private BookmarkDao bookmarkDao;

        InsertAsyncTask(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmarks) {
            bookmarkDao.insertBookmark(bookmarks);
            return null;
        }

    }

    static class UpdateAsyncTask extends AsyncTask<Bookmark, Void, Void> {
        private BookmarkDao bookmarkDao;

        UpdateAsyncTask(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmarks) {
            bookmarkDao.updateBookmark(bookmarks);
            return null;
        }

    }

    static class DeleteAsyncTask extends AsyncTask<Bookmark, Void, Void> {
        private BookmarkDao bookmarkDao;

        DeleteAsyncTask(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmarks) {
            bookmarkDao.deleteBookmark(bookmarks);
            return null;
        }

    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookmarkDao bookmarkDao;

        DeleteAllAsyncTask(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bookmarkDao.deleteAllBookmark();
            return null;
        }

    }
    
}
