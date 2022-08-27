package com.thallo.stage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}