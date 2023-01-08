package com.thallo.stage.components.popup;

import static com.thallo.stage.R.color.background;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.HomeFragment;
import com.thallo.stage.R;
import com.thallo.stage.StatusBar;
import com.thallo.stage.components.filePicker.GetFile;
import com.thallo.stage.databinding.PopupDiyBinding;

public class DIYPopup {
    PopupDiyBinding binding;
    BottomSheetDialog bottomSheetDialog;
    public DIYPopup(Context context) {
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        binding=PopupDiyBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(binding.getRoot());
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        Glide.with(context).load(pref.getString("bg",null)).circleCrop().into(binding.imageView25);
        binding.imageView25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putBoolean("bgStyle",false).commit();

                GetFile getFile=new GetFile();
                getFile.open((Activity) context,1);
            }
        });
        binding.bgLig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putInt("textC",1).commit();
                HomeFragment.binding.tips.setTextColor(context.getColor(R.color.background_light));
                HomeFragment.binding.HomeQr.setIconTint(ColorStateList.valueOf(context.getColor(R.color.background_light)));
                new StatusBar((Activity) context).setTextColor(true);

            }
        });
        binding.bgBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putInt("textC",0).commit();
                HomeFragment.binding.tips.setTextColor(context.getColor(R.color.black));
                HomeFragment.binding.HomeQr.setIconTint(ColorStateList.valueOf(context.getColor(R.color.black)));
                new StatusBar((Activity) context).setTextColor(false);

            }
        });
        binding.bgDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putBoolean("bgStyle",true).commit();
                HomeFragment.binding.imageView22.setImageResource(background);
            }
        });
        bottomSheetDialog.show();
    }
}
