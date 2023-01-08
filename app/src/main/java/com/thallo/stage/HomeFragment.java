package com.thallo.stage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.thallo.stage.components.Qr;
import com.thallo.stage.components.filePicker.GetFile;
import com.thallo.stage.components.popup.DIYPopup;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.databinding.FragmentHomeBinding;

import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {
    public static FragmentHomeBinding binding;
    BaseActivity baseActivity;
    Intent intentScan;
    Qr qr;
    public static MyBaseAdapter myBaseAdapter;
    int darkMutedColor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
        qr=new Qr();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());


        binding=FragmentHomeBinding.inflate(LayoutInflater.from(getContext()),container,false);
        Calendar calendar =Calendar.getInstance();
        if(5<=calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=8){binding.tips.setText("早安");}
        if(8<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=11){binding.tips.setText("上午好");}
        if(11<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=13){binding.tips.setText("午安");}
        if(13<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=19){binding.tips.setText("下午好");}
        if(19<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=22){binding.tips.setText("晚安");}
        if(22<calendar.get(Calendar.HOUR_OF_DAY)){binding.tips.setText("深夜,Good dream");}
        if(0<=calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<5){binding.tips.setText("深夜,Good dream");}

        String bg=prefs.getString("bg",null);
        if(!prefs.getBoolean("bgStyle", true))
        {
            if (bg!=null) {
                Uri uri=Uri.parse(bg);
                binding.imageView22.setImageURI(uri);


            }
            int textColor=prefs.getInt("textC",-1);
            if (textColor==1){
                binding.tips.setTextColor(getContext().getColor(R.color.background_light));
                binding.HomeQr.setIconTint(ColorStateList.valueOf(getContext().getColor(R.color.background_light)));

            }
            else if (textColor==0){
                binding.tips.setTextColor(getContext().getColor(R.color.black));
                binding.HomeQr.setIconTint(ColorStateList.valueOf(getContext().getColor(R.color.black)));
            }
        }


        binding.HomeQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
                {
                    qr.show(baseActivity);


                }else getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA},1);


            }
        });
        HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        BookmarkViewModel bookmarkViewModel= new ViewModelProvider(this).get(BookmarkViewModel.class);
        if(BaseActivity.binding.urlView==null) binding.gridview.setNumColumns(4);
        else binding.gridview.setNumColumns(GridView.AUTO_FIT);
        bookmarkViewModel.findBookmarksWithShow(true).observe(getViewLifecycleOwner(), new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> list) {
                myBaseAdapter=new MyBaseAdapter(getContext(),list,getActivity(),HomeFragment.this, baseActivity,bookmarkViewModel,prefs.getInt("bgColor",0),prefs.getInt("textC",-1));
                binding.gridview.setAdapter(myBaseAdapter);
                myBaseAdapter.setOnclick(new MyBaseAdapter.onclick() {
                    @Override
                    public void click(String url) {
                        baseActivity.getWebSessionViewModel().getSession().loadUri(url);
                    }
                });

            }
        });

        binding.button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DIYPopup(getContext());
            }
        });









        // Inflate the layout for this fragment
        return binding.getRoot();
    }




}