package com.thallo.stage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gyf.immersionbar.ImmersionBar;

import com.thallo.stage.components.filePicker.FilePicker;
import com.thallo.stage.components.filePicker.PickUtils;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.database.history.History;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.databinding.ActivityFragmentHolderBinding;
import com.thallo.stage.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FragmentHolder extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityFragmentHolderBinding binding;
    Intent intent;
    BookmarkViewModel bookmarkViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.background)
                .navigationBarColor(R.color.background)
                .autoStatusBarDarkModeEnable(true,0.2f)
                .init();
        bookmarkViewModel= new ViewModelProvider(this).get(BookmarkViewModel.class);
        binding = ActivityFragmentHolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent=getIntent();
        NavHostFragment navHostController= (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.fragmentContainerView.getId());
        NavController navController = navHostController.getNavController();
        switch (intent.getStringExtra("page"))
        {
            case "HISTORY":
                navController.navigate(R.id.historyFragment,null);
                break;
            case "BOOKMARK":
                navController.navigate(R.id.bookmarkFragment, null);
                break;
            case "DOWNLOAD":
                navController.navigate(R.id.downloadFragment, null);
                break;
            case "ADDONS":
                navController.navigate(R.id.addonsManagerFragment, null);

                break;
            case "ABOUT":
                navController.navigate(R.id.aboutFragment, null);
                break;
            case "OS":
                navController.navigate(R.id.OSFragment, null);
                break;
        }











    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 24&& resultCode == Activity.RESULT_OK) {
            if (resultData != null) {


                try {
                    File file=new File(PickUtils.getPath(this,resultData.getData()));
                    Document document  = Jsoup.parse(file);
                    Elements elements=document.getElementsByTag("a");
                    for (int i = 0; i < elements.size(); i++) {
                        Bookmark bookmark=new Bookmark(elements.get(i).attr("href"),elements.get(i).text(),"默认",false);
                        bookmarkViewModel.insertWords(bookmark);


                    }
                    Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();


                } catch (IOException e) {
                    Log.i("jsoup",e.getMessage());
                    Toast.makeText(this, "导入失败"+e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }
        if (requestCode == 27 && resultCode == Activity.RESULT_OK) {


            if (resultData != null) {
                bookmarkViewModel.getAllBookmarksLive().observe(this, new Observer<List<Bookmark>>() {
                    @Override
                    public void onChanged(List<Bookmark> bookmarks) {
                        String bookmark =null;
                        String html;
                        for (int i = 0; i < bookmarks.size(); i++) {
                            if (bookmark==null){
                                bookmark="<DT><A HREF=\"" +
                                        bookmarks.get(i).getUrl()+
                                        "\">" +
                                        bookmarks.get(i).getTitle() +
                                        "</A>\n";
                            }else {
                                bookmark=bookmark+"<DT><A HREF=\"" +
                                        bookmarks.get(i).getUrl()+
                                        "\">" +
                                        bookmarks.get(i).getTitle() +
                                        "</A>\n";
                            }



                        }
                        html="<!DOCTYPE NETSCAPE-Bookmark-file-1>\n" +
                                "<!-- This is an automatically generated file.\n" +
                                "     It will be read and overwritten.\n" +
                                "     DO NOT EDIT! -->\n" +
                                "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n" +
                                "<TITLE>Bookmarks</TITLE>\n" +
                                "<H1>Bookmarks</H1>\n" +
                                "<DL><p>\n" +
                                bookmark +
                                "</DL><p>";
                        try {
                            modifyDocument(resultData.getData(),html);
                            Toast.makeText(FragmentHolder.this, "导出成功", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(FragmentHolder.this, "导出失败", Toast.LENGTH_SHORT).show();


                        }
                    }
                });



            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public void modifyDocument(Uri uri,String string) throws IOException {
        Toast.makeText(this,PickUtils.getPath(this,uri), Toast.LENGTH_SHORT).show();
        final ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "w");
        final FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        final FileOutputStream fos = new FileOutputStream(fileDescriptor);
        fos.write((string).getBytes());
        fos.close();
        parcelFileDescriptor.close();
    }


}