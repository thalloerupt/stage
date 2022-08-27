package com.thallo.stage.tab;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.thallo.stage.HomeFragment;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.WebSessionViewModel;
import com.thallo.stage.databinding.ActivityMainBinding;

import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoSessionSettings;

import java.util.List;

public class TabDetails  {
    int currentIndex;
    int mProcess;
    String mUrl;
    Bitmap pngBM;
    List<PageTab> tabList;
    ActivityMainBinding binding;
    GeckoSession.SessionState mSessionState;
    BaseActivity activity;
    int dp;


    public void newTabDetail(String url, int index, BaseActivity context, BottomSheetBehavior behavior){
        GeckoSession mSession= new GeckoSession();
        mSession.getSettings().setUserAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE);
        PageTab tab=new PageTab(context,new WebSessionViewModel(mSession,context));
        tab.setOnClickListener(v -> useTabDetail(tabList.indexOf(v), behavior,true));
        tab.getModel().setNewSessionHandler((session, uri) -> {
            Log.d("New",uri);
            newTabDetail(uri,tabList.indexOf(tab)+1,context,behavior);
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
                Log.d("scrollY",dp+"");
                if (i<=scrollY) {
                    i=scrollY;
                    if (scrollY-n>200) {
                        binding.toolLayout.setTranslationY(dp);
                        binding.geckoview.setDynamicToolbarMaxHeight(0);

                    }
                    else {
                        binding.toolLayout.setTranslationY(scrollY - n);
                        binding.geckoview.setDynamicToolbarMaxHeight(n-scrollY);

                    }


                }
                else if(i>=scrollY) {
                    binding.toolLayout.setTranslationY(0);
                    binding.geckoview.setDynamicToolbarMaxHeight(dp);

                    if (scrollY==0) i=0;
                }
                Log.d("scrd",n+"");

            }
        });

    }
    public void useTabDetail(int index,BottomSheetBehavior behavior,boolean collapsed){
        if(currentIndex!=index&&currentIndex<tabList.size()) tabList.get(currentIndex).getModel().inactive();
        currentIndex=index;
        binding.geckoview.releaseSession();
        tabList.get(index).getModel().active();
        binding.setSessionModel(tabList.get(index).getModel());
        binding.getSessionModel().getSession().setActive(true);
        binding.geckoview.setSession(binding.getSessionModel().getSession());
        if (collapsed) behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);



    }
    public void closeTabDetail(int index, BottomSheetBehavior behavior, BaseActivity context){
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


    public void setThings(ActivityMainBinding binding,List<PageTab> tabList,int dp) {
        this.binding = binding;
        this.tabList = tabList;
        this.dp=dp;

    }

    public List<PageTab> getTabList() {
        return tabList;
    }


    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
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
