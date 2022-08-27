package com.thallo.stage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.thallo.stage.components.Qr;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.databinding.FragmentHomeBinding;

import java.util.Calendar;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    BaseActivity baseActivity;
    Intent intentScan;
    Qr qr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
        qr=new Qr();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentHomeBinding.inflate(LayoutInflater.from(getContext()),container,false);
        Calendar calendar =Calendar.getInstance();
        if(5<=calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=8){binding.tips.setText("早安");}
        if(8<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=11){binding.tips.setText("上午好");}
        if(11<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=13){binding.tips.setText("午安");}
        if(13<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=19){binding.tips.setText("下午好");}
        if(19<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=22){binding.tips.setText("晚安");}
        if(22<calendar.get(Calendar.HOUR_OF_DAY)){binding.tips.setText("深夜,Good dream");}
        if(0<=calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<5){binding.tips.setText("深夜,Good dream");}


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
        bookmarkViewModel.findBookmarksWithShow(true).observe(getViewLifecycleOwner(), new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> list) {
                MyBaseAdapter myBaseAdapter=new MyBaseAdapter(getContext(),list,getActivity(),HomeFragment.this, baseActivity,bookmarkViewModel);
                binding.gridview.setAdapter(myBaseAdapter);
            }
        });





        // Inflate the layout for this fragment
        return binding.getRoot();
    }






}