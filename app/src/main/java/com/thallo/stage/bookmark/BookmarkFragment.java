package com.thallo.stage.bookmark;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thallo.stage.history.MyAdapter;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.databinding.FragmentBookmarkBinding;

import java.util.List;


public class BookmarkFragment extends Fragment {
    FragmentBookmarkBinding binding;
    MyBookmarkAdapter myAdapter;
    BookmarkViewModel bookmarkViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentBookmarkBinding.inflate(inflater,container,false);
        binding.bookmarkRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter=new MyBookmarkAdapter();
        bookmarkViewModel= new ViewModelProvider((FragmentActivity)getContext()).get(BookmarkViewModel.class);
        bookmarkViewModel.getAllBookmarksLive().observe((LifecycleOwner) this, new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> bookmarks) {

                myAdapter.setAllBookmark(bookmarks);
                binding.bookmarkRecycler.setItemViewCacheSize(bookmarks.size());
                binding.bookmarkRecycler.setAdapter(myAdapter);
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}