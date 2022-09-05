package com.thallo.stage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.thallo.stage.databinding.FragmentAboutBinding;


public class AboutFragment extends Fragment {
    FragmentAboutBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAboutBinding.inflate(inflater,container,false);
        binding.aboutTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.url="https://t.me/stage_browser_channel";
                Intent intent = new Intent(getContext(), BaseActivity.class);
                getContext().startActivity(intent);
            }
        });
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.url="https://static-6e7c68d2-83dd-40b0-9f09-0150b6b22138.bspapp.com/";
                Intent intent = new Intent(getContext(), BaseActivity.class);
                getContext().startActivity(intent);
            }
        });

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.url="https://static-6e7c68d2-83dd-40b0-9f09-0150b6b22138.bspapp.com/Stage%E9%9A%90%E7%A7%81%E6%94%BF%E7%AD%96.html";
                Intent intent = new Intent(getContext(), BaseActivity.class);
                getContext().startActivity(intent);
            }
        });

        binding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FragmentHolder.class);
                intent.putExtra("page","OS");
                getContext().startActivity(intent);
            }
        });



        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}