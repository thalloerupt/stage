package com.thallo.stage.components.popup;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.R;
import com.thallo.stage.databinding.PopupTabsBinding;
import com.thallo.stage.tab.PageTab;

import org.webrtc.EglBase10;

import java.util.List;

public class TabsPopup {
    PopupTabsBinding binding;
    List<PageTab> tabList;
    Context context;
    int index;
    MyBottomSheetDialog bottomSheetDialog;
    onAddNewTab onAddNewTab;
    public TabsPopup( Context context) {
        this.tabList = tabList;
        this.context = context;
        bottomSheetDialog = new MyBottomSheetDialog(context, R.style.BottomSheetDialog,2);
        binding=PopupTabsBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                binding.tabs.removeAllViews();
            }
        });

    }
    public void show(List<PageTab> tabList,int index){
        for (int i=0;i<index;i++)
        {
            binding.tabs.addView(tabList.get(i),i);

        }
        Log.d("标签",index+"");
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewTab.addNewTab();
                binding.tabs.addView(tabList.get(tabList.size()-1));

            }
        });
        binding.button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
    public void removeTab(int index){
        binding.tabs.removeViewAt(index);
    }


    public interface onAddNewTab{
        void addNewTab();
    }

    public void setOnAddNewTab(TabsPopup.onAddNewTab onAddNewTab) {
        this.onAddNewTab = onAddNewTab;
    }
}
