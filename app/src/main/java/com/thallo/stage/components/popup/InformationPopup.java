package com.thallo.stage.components.popup;

import android.content.Context;
import android.os.PerformanceHintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.R;
import com.thallo.stage.WebSessionViewModel;
import com.thallo.stage.databinding.PopupInformationBinding;
import com.thallo.stage.databinding.SettingMenuBinding;
import com.thallo.stage.extension.Controller;

import org.mozilla.geckoview.GeckoSession;

import java.net.URI;

public class InformationPopup {
    Context context;
    BottomSheetDialog bottomSheetDialog;
    PopupInformationBinding binding;
    public InformationPopup(Context context, GeckoSession session, WebSessionViewModel webSessionViewModel){
        this.context=context;
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        binding= PopupInformationBinding.inflate(LayoutInflater.from(context));
        URI uri=URI.create(webSessionViewModel.getUrl());

        if (session.getSettings().getUseTrackingProtection()) {
            binding.informationProtection.setImageResource(R.drawable.ic_shield);
            binding.informationProtectionText.setText(context.getString(R.string.information_protection));
        }
        else {
            binding.informationProtection.setImageResource(R.drawable.ic_broken_shield);
            binding.informationProtectionText.setText(context.getString(R.string.information_not_protection));
        }

        if (webSessionViewModel.isSecure()) {
            binding.informationSecure.setImageResource(R.drawable.ic_lock);
            binding.informationSecureText.setText(context.getString(R.string.information_secure));
        }
        else {
            binding.informationSecure.setImageResource(R.drawable.ic_broken_lock);
            binding.informationSecureText.setText(context.getString(R.string.information_not_secure));

        }
        binding.informationUrl.setText(uri.getHost());
        String faviconUrl=uri.getScheme()+"://"+uri.getHost()+"/favicon.ico";
        Glide.with(context).load(faviconUrl).placeholder(R.drawable.ic_internet)
                .into(binding.informationIcon);
    }


    public void show(){
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
    }
}
