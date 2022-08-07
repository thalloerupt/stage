package com.thallo.stage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thallo.stage.components.QR;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.databinding.FragmentHomeBinding;

import java.util.Calendar;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentHomeBinding.inflate(LayoutInflater.from(getContext()),container,false);
        Calendar calendar =Calendar.getInstance();
        if(3<=calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=8){binding.tips.setText("早安");}
        if(8<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=11){binding.tips.setText("上午好");}
        if(11<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=13){binding.tips.setText("午安");}
        if(13<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=19){binding.tips.setText("下午好");}
        if(19<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<=22){binding.tips.setText("晚安");}
        if(22<calendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.HOUR_OF_DAY)<3){binding.tips.setText("深夜,Good dream");}



        binding.HomeQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QR qr=new QR();

            }
        });
        HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}