package com.thallo.stage.components.imageViewGlide;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.thallo.stage.BR;
import com.thallo.stage.R;

import com.bumptech.glide.Glide;

import java.net.URI;

public class ImageLoaderUtils {

    @BindingAdapter(value = {"imageUrl"} ,requireAll = false)
    public static void loadImage(ImageView view, String url){
        if (url==null) return;

        URI uri=URI.create(url);
        String faviconUrl=uri.getScheme()+"://"+uri.getHost()+"/favicon.ico";
            Glide.with(view.getContext()).load(faviconUrl).error(R.drawable.ic_internet)
                    .into(view);


    }


}
