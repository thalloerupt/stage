package com.thallo.stage.components.binding;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class AddressText {
    @BindingAdapter(value = {"titleToUrl","url"} ,requireAll = false)
    public static void titleToUrl(TextView view, String title,String url){
        if (title==null) return;
        if (url==null) return;

        if(title=="新标签页")
            view.setText("");
        else view.setText(url);

    }

}
