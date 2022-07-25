package com.thallo.stage.tab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.thallo.stage.BR;
import com.thallo.stage.HomeFragment;
import com.thallo.stage.MainActivity;
import com.thallo.stage.PageTab;
import com.thallo.stage.R;
import com.thallo.stage.WebSessionViewModel;
import com.thallo.stage.databinding.ActivityMainBinding;
import com.thallo.stage.databinding.TabBinding;
import com.thallo.stage.interfaces.favicon;

import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoSessionSettings;
import org.mozilla.geckoview.WebExtensionController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TabDetails  {
    int currentIndex;
    int mProcess;
    String mUrl;
    Bitmap pngBM;
    List<PageTab> tabList;
    ActivityMainBinding binding;
    GeckoSession.SessionState mSessionState;
    MainActivity activity;
    int dp;
    HomeFragment homeFragment;
    FragmentManager fm;

    public void newTabDetail(String url, int index, Context context, BottomSheetBehavior behavior){
        GeckoSession mSession= new GeckoSession();
        mSession.getSettings().setUserAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE);
        PageTab tab=new PageTab(context,new WebSessionViewModel(mSession,context));
        tab.setOnClickListener(v -> useTabDetail(tabList.indexOf(v), behavior,true));
        tab.getModel().setNewSessionHandler((session, uri) -> {

            newTabDetail(url,tabList.indexOf(tab)+1,context,behavior);
            return null;
        });
        tab.getBinding().close.setOnClickListener(v->closeTabDetail(tabList.indexOf(tab),behavior,context));
        tabList.add(index,tab);
        useTabDetail(index,behavior,true);
        currentIndex=index;
        binding.tabs.addView(tabList.get(index),index);
        mSession.loadUri(url);
        binding.tabSize2.setText(tabList.size()+"");
        binding.getSessionModel().getSession().setScrollDelegate(new GeckoSession.ScrollDelegate() {
            int i=0,n=0,m=0;
            @Override
            public void onScrollChanged(@NonNull GeckoSession session, int scrollX, int scrollY) {
                GeckoSession.ScrollDelegate.super.onScrollChanged(session, scrollX, scrollY);
                if (i<=scrollY) {
                    n = i = scrollY;
                    binding.toolLayout.setTranslationY(dp);
                }
                else if(i>=scrollY) {
                    binding.toolLayout.setTranslationY(0);
                    if (scrollY==0) i=0;
                }
                Log.d("scrd",i+"");

            }
        });

    }
    public void useTabDetail(int index,BottomSheetBehavior behavior,boolean collapsed){
        if(currentIndex!=index&&currentIndex<tabList.size())
            tabList.get(currentIndex).getModel().inactive();
        currentIndex=index;
        binding.geckoview.releaseSession();
        tabList.get(index).getModel().active(mSessionState);
        binding.setSessionModel(tabList.get(index).getModel());
        binding.getSessionModel().getSession().setActive(true);
        binding.geckoview.setSession(binding.getSessionModel().getSession());
        if (collapsed) behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if(binding.getSessionModel().getTitle()=="新标签页")
        {
            fm.beginTransaction().show(homeFragment).commit();
        }else fm.beginTransaction().hide(homeFragment).commit();
    }
    public void closeTabDetail(int index,BottomSheetBehavior behavior,Context context){
        binding.tabSize2.setText(tabList.size()-1+"");
        tabList.get(currentIndex).getModel().inactive();
        binding.geckoview.releaseSession();
        tabList.get(index).getModel().inactive();
        binding.tabs.removeViewAt(index);
        tabList.remove(index);
        if(tabList.size()>0){
            useTabDetail(Math.max(index-1,0),behavior,false);
            return;
        }else if(tabList.size()==0){newTabDetail("",0,context,behavior);return;}

    }


    public void setThings(ActivityMainBinding binding,List<PageTab> tabList,int dp,FragmentManager fm,HomeFragment homeFragment) {
        this.binding = binding;
        this.tabList = tabList;
        this.dp=dp;
        this.homeFragment=homeFragment;
        this.fm=fm;
    }

    public List<PageTab> getTabList() {
        return tabList;
    }





    public int getmProcess() {
        return mProcess;
    }

    public String getmUrl() {
        return mUrl;
    }

    public Bitmap getPngBM() {
        return pngBM;
    }


}
