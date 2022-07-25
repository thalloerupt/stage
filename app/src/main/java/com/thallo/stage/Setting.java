package com.thallo.stage;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.thallo.stage.databinding.SettingBinding;

public class Setting extends AppCompatActivity {
    SettingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ImmersionBar.with(this)
                //.hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                //.navigationBarColor(R.color.white)
                .init();
        getSupportFragmentManager().beginTransaction().replace(binding.settingCon.getId(), new SettingsFragment()).commit();
        binding.toolbar.setNavigationIcon(R.drawable.ic_left);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();       //返回
            }
        });

    }
}
