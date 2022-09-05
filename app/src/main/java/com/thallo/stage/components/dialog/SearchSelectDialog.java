package com.thallo.stage.components.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ListAdapter;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.R;
import com.thallo.stage.SearchChoice;
import com.thallo.stage.SearchItem;
import com.thallo.stage.databinding.DiaSearchSelectBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchSelectDialog extends AlertDialog {
    DiaSearchSelectBinding binding;
    public SearchSelectDialog(@NonNull Context context) {
        super(context);
        String[] datas=context.getResources().getStringArray(R.array.searchEngine_values);
        binding=DiaSearchSelectBinding.inflate(LayoutInflater.from(context));
        List<SearchChoice> searchChoices=new ArrayList<>();
        init(searchChoices,datas);
        SearchItem searchItem=new SearchItem(context,R.layout.se_select,searchChoices);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        binding.iconList.setAdapter(searchItem);
        binding.iconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        BaseActivity.binding.searchIcon.setImageResource(R.drawable.ic_baidu);
                        prefs.edit().putString("searchEngine", context.getString(R.string.baidu)).commit();
                        break;
                    case 1:
                        BaseActivity.binding.searchIcon.setImageResource(R.drawable.ic_google);
                        prefs.edit().putString("searchEngine", context.getString(R.string.google)).commit();

                        break;
                    case 2:
                        BaseActivity.binding.searchIcon.setImageResource(R.drawable.ic_bing);
                        prefs.edit().putString("searchEngine", context.getString(R.string.bing)).commit();

                        break;
                    case 3:
                        BaseActivity.binding.searchIcon.setImageResource(R.drawable.ic_sogou);
                        prefs.edit().putString("searchEngine", context.getString(R.string.sogou)).commit();

                        break;
                }
                dismiss();


            }
        });
        setView(binding.getRoot());
    }
    public void init(List<SearchChoice> searchChoices,String[] datas){
        SearchChoice a=new SearchChoice(R.drawable.ic_baidu,datas[0]);
        searchChoices.add(a);
        SearchChoice b=new SearchChoice(R.drawable.ic_google,datas[1]);
        searchChoices.add(b);
        SearchChoice c=new SearchChoice(R.drawable.ic_bing,datas[2]);
        searchChoices.add(c);
        SearchChoice d=new SearchChoice(R.drawable.ic_sogou,datas[3]);
        searchChoices.add(d);
    }
}
