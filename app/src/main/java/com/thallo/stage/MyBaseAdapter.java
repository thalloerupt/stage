package com.thallo.stage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.databinding.HomeIconBinding;

import java.net.URI;
import java.util.List;

public class MyBaseAdapter extends BaseAdapter {
    private List<Bookmark> list;
    private Context context;
    private HomeIconBinding binding;
    private FragmentActivity activity;
    private HomeFragment homeFragment;
    private BaseActivity baseActivity;
    private BookmarkViewModel bookmarkViewModel;
    public onclick onclick;
    int darkMutedColor= -16776961;
    int textColor;
    public MyBaseAdapter(Context context, List<Bookmark> list, FragmentActivity activity, HomeFragment homeFragment, BaseActivity baseActivity, BookmarkViewModel bookmarkViewModel,int darkMutedColor,int textColor){
        this.context=context;
        this.list=list;
        this.activity=activity;
        this.homeFragment=homeFragment;
        this.baseActivity = baseActivity;
        this.bookmarkViewModel=bookmarkViewModel;
        this.darkMutedColor=darkMutedColor;
        this.textColor=textColor;



    }




    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        binding=HomeIconBinding.inflate(LayoutInflater.from(context),viewGroup,false);
        Bookmark bookmark=list.get(i);
        binding.IconText.setText(bookmark.getTitle());
        URI uri=URI.create(bookmark.getUrl());
        String faviconUrl=uri.getScheme()+"://"+uri.getHost()+"/favicon.ico";
        Glide.with(context).load(faviconUrl).placeholder(R.drawable.ic_internet).circleCrop()
                .into(binding.IconImage);
        binding.IconLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                bookmark.setShow(false);
                bookmarkViewModel.updateWords(bookmark);
                return false;
            }
        });
        binding.IconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //baseActivity.getWebSessionViewModel().getSession().loadUri(bookmark.getUrl());
                onclick.click(bookmark.getUrl());
                //Toast.makeText(viewGroup.getContext(), bookmark.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
        if (textColor==1) binding.IconText.setTextColor(context.getColor(R.color.background_light));
        else if (textColor==0)binding.IconText.setTextColor(context.getColor(R.color.black));
        if (darkMutedColor!=0) binding.IconBg.setCardBackgroundColor(darkMutedColor);
        return binding.getRoot();
    }

    public interface onclick{
        void click(String url);
    }

    public void setOnclick(MyBaseAdapter.onclick onclick) {
        this.onclick = onclick;
    }
}
