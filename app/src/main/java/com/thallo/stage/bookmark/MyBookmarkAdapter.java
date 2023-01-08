package com.thallo.stage.bookmark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.R;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyBookmarkAdapter extends RecyclerView.Adapter<com.thallo.stage.bookmark.MyBookmarkAdapter.MyAdapterHold> {
    List<Bookmark> allBookmark = new ArrayList<>();
    TextView title,url;
    ImageView icon;
    BookmarkViewModel bookmarkViewModel;
    View close;


    @NonNull
    @Override
    public com.thallo.stage.bookmark.MyBookmarkAdapter.MyAdapterHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.history_item,parent,false);
        bookmarkViewModel=new ViewModelProvider((FragmentActivity)parent.getContext()).get(BookmarkViewModel.class);


        return new com.thallo.stage.bookmark.MyBookmarkAdapter.MyAdapterHold(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.thallo.stage.bookmark.MyBookmarkAdapter.MyAdapterHold holder, @SuppressLint("RecyclerView") int position) {
        Bookmark bookmark=allBookmark.get(position);
        title.setText(bookmark.getTitle());
        url.setText(bookmark.getUrl());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkViewModel.deleteWords(bookmark);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.url=bookmark.getUrl();
                ((FragmentActivity) view.getContext()).finish();
            }
        });

        try {
            URI uri=URI.create(bookmark.getUrl());
            String faviconUrl=uri.getScheme()+"://"+uri.getHost()+"/favicon.ico";
            Glide.with(holder.itemView.getContext()).load(faviconUrl).placeholder(R.drawable.ic_internet)
                    .into(icon);
        }catch (Exception e){

        }



    }

    @Override
    public int getItemCount() {
        return allBookmark.size();
    }

    public void setAllBookmark(List<Bookmark> allBookmark) {
        this.allBookmark = allBookmark;

    }

    class MyAdapterHold extends RecyclerView.ViewHolder{
        public MyAdapterHold(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.historyTitle);
            url=itemView.findViewById(R.id.historyUrl);
            icon=itemView.findViewById(R.id.historyIcon);
            close=itemView.findViewById(R.id.historyDelete);

        }
    }




}