package com.thallo.stage.components.binding;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.thallo.stage.R;

import com.bumptech.glide.Glide;

import java.net.URI;

public class ImageLoaderUtils {

    @BindingAdapter(value = {"imageUrl"} ,requireAll = false)
    public static void loadImage(ImageView view, String url){
        if (url==null) return;

        URI uri=URI.create(url);
        String faviconUrl=uri.getScheme()+"://"+uri.getHost()+"/favicon.ico";
            Glide.with(view.getContext()).load(faviconUrl).placeholder(R.drawable.ic_internet)
                    .into(view);
    }

    @BindingAdapter(value = {"isSecure","pageUrl"} ,requireAll = false)
    public static void loadSecureImage(ImageView view, boolean isSecure,String url){
        Log.d("isSecure",isSecure+"");
        if (url==null) return;
        else if (url.indexOf("about:blank") == -1)
        {view.setVisibility(View.VISIBLE);Log.d("YES",url);}
        else if (url.indexOf("about:blank") != -1)
        {view.setVisibility(View.GONE);Log.d("YES",url);}
        if (isSecure) view.setImageResource(R.drawable.ic_lock);
        else view.setImageResource(R.drawable.ic_broken_lock);
    }

    @BindingAdapter(value = {"isProtecting"} ,requireAll = false)
    public static void loadProtectingImage(ImageView view, boolean isSecure){
        Log.d("isProtecting",isSecure+"");
        if (isSecure) view.setImageResource(R.drawable.ic_shield);
        else view.setImageResource(R.drawable.ic_broken_shield);

    }


}
