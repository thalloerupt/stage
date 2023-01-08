package com.thallo.stage.bookmark;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thallo.stage.R;
import com.thallo.stage.components.filePicker.GetFile;
import com.thallo.stage.history.MyAdapter;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.databinding.FragmentBookmarkBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


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
        bookmarkViewModel= new ViewModelProvider((FragmentActivity)getContext()).get(BookmarkViewModel.class);
        binding.bookmarkRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.toolbar.inflateMenu(R.menu.bookmark_menu);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.importb:
                        openFile();
                        break;
                    case R.id.exportb:
                        createFile();
                        break;
                    case R.id.deleteAb:
                        bookmarkViewModel.deleteAllWords();
                        break;
                }
                return false;
            }
        });
        binding.toolbar.setTitle(R.string.pop_star);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });



        myAdapter=new MyBookmarkAdapter();

        bookmarkViewModel.getAllBookmarksLive().observe((LifecycleOwner)this,bookmarks->{
            myAdapter.setAllBookmark(bookmarks);
            binding.bookmarkRecycler.setAdapter(myAdapter);

            binding.bookmarkRecycler.setItemViewCacheSize(bookmarks.size());

            if (bookmarks.size()==0) binding.bookmarkLottie.setVisibility(View.VISIBLE);
            else binding.bookmarkLottie.setVisibility(View.GONE);
        });


        binding.bookmarkLottie.loop(true);
        binding.bookmarkLottie.playAnimation();


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT) ;
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/html")  ;

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads


        getActivity().startActivityForResult(intent,24);
    }
    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT) ;
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/html")  ;
        intent.putExtra(Intent.EXTRA_TITLE, "Stage书签"+_GetDate()+".html");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads


        getActivity().startActivityForResult(intent,27);
    }
    private String _GetDate(){
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar c = Calendar.getInstance(tz);
        return "_"+c.get(Calendar.YEAR)+c.get(Calendar.MONTH)+c.get(Calendar.DAY_OF_MONTH);
    }


}