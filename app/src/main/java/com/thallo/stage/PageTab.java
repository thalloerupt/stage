package com.thallo.stage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.thallo.stage.databinding.TabBinding;

import java.net.MalformedURLException;
import java.net.URL;

public class PageTab extends RelativeLayout {
    TabBinding binding;

    public PageTab(Context context, WebSessionViewModel model){
        this(context);
        binding.setSession(model);





    }
    public PageTab(Context context) {
        this(context,(AttributeSet) null);
    }

    public PageTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);

    }

    private void initUI(Context context){
        binding=TabBinding.inflate(LayoutInflater.from(context),this,true);
    }
    public WebSessionViewModel getModel() {
        return binding.getSession();
    }

    public TabBinding getBinding() {
        return binding;
    }
}
