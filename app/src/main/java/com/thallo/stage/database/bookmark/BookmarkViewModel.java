package com.thallo.stage.database.bookmark;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.thallo.stage.database.bookmark.Bookmark;

import java.util.List;

public class BookmarkViewModel extends AndroidViewModel {
    BookmarkRepository bookmarkRepository;
    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        bookmarkRepository=new BookmarkRepository(application);
    }

    public LiveData<List<Bookmark>> getAllBookmarksLive() {
        return bookmarkRepository.getAllBookmarkLive();
    }
    LiveData<List<Bookmark>>findBookmarksWithPattern(String pattern){
        return bookmarkRepository.findBookmarksWithPattern(pattern);
    }

    LiveData<List<Bookmark>>findBookmarksWithTitle(String pattern){
        return bookmarkRepository.findWordsWithTitle(pattern);
    }
    public void insertWords(com.thallo.stage.database.bookmark.Bookmark... bookmarks) {
        bookmarkRepository.insertBookmark(bookmarks);
    }
    public void updateWords(com.thallo.stage.database.bookmark.Bookmark... bookmarks) {
        bookmarkRepository.updateBookmark(bookmarks);
    }
    public void deleteWords(com.thallo.stage.database.bookmark.Bookmark... bookmarks) {
        bookmarkRepository.deleteBookmark(bookmarks);
    }
    public void deleteAllWords() {
        bookmarkRepository.deleteAllbookmarks();
    }
}
