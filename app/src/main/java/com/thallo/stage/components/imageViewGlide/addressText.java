package com.thallo.stage.components.imageViewGlide;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.thallo.stage.R;

import java.net.URI;

public class addressText {
    @BindingAdapter(value = {"titleToUrl","url"} ,requireAll = false)
    public static void titleToUrl(TextView view, String title,String url){
        if (title==null) return;
        if (url==null) return;

        if(title=="新标签页")
            view.setText("");
        else view.setText(url);



    }

}
