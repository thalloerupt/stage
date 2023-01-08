package com.thallo.stage.tab;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.thallo.stage.HomeFragment;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.WebSessionViewModel;
import com.thallo.stage.databinding.ActivityMainBinding;
import com.thallo.stage.tab.PageTab;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoSessionSettings;

import java.util.List;

public class TabDetails  {
    int currentIndex;
    int mProcess;
    String mUrl;
    Bitmap pngBM;
    List<PageTab> tabList;
    GeckoSession.SessionState mSessionState;
    BaseActivity activity;
    int dp;
    GeckoSession mSession;
    ActivityMainBinding binding;
    onCloseListener onCloseListener;
    public void newTabDetail(String url, @Nullable GeckoSession session, int index, BaseActivity context){
        if (session!=null)mSession=session;
        else {mSession= new GeckoSession();mSession.open(GeckoRuntime.getDefault(context));mSession.loadUri(url);}
        if (binding.urlView==null) mSession.getSettings().setUserAgentMode(GeckoSessionSettings.USER_AGENT_MODE_DESKTOP);
        else mSession.getSettings().setUserAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE);
        PageTab tab=new PageTab(context,new WebSessionViewModel(mSession,context));
        tab.setOnClickListener(v -> useTabDetail(tabList.indexOf(v),true,false));
        tab.getModel().setNewSessionHandler(new WebSessionViewModel.NewSessionHandler() {
            @Override
            public GeckoResult<GeckoSession> onNewSession(GeckoSession session, String uri) {
                GeckoSession session1=new GeckoSession();
                newTabDetail(uri,session1,tabList.indexOf(tab)+1,context);
                return GeckoResult.fromValue(session1);
            }
        });
        tab.getBinding().close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseListener.onClose(tabList.indexOf(tab));
                closeTabDetail(tabList.indexOf(tab),context);

            }
        });

        tabList.add(index,tab);
        if (session!=null) useTabDetail(index,true,true);
        else useTabDetail(index,true,false);
        currentIndex=index;
        binding.tabSize2.setText(tabList.size()+"");
        binding.getSessionModel().getSession().setScrollDelegate(new GeckoSession.ScrollDelegate() {
            int i=0,n=0,m=0;
            @Override
            public void onScrollChanged(@NonNull GeckoSession session, int scrollX, int scrollY) {
                GeckoSession.ScrollDelegate.super.onScrollChanged(session, scrollX, scrollY);
                Log.d("scrollY",dp+"");
                scrollY=scrollY*2;
                if (i<=scrollY) {
                    i=scrollY;
                    if (scrollY-n>200) {
                        if (binding.urlView!=null)binding.urlView.setTranslationY(dp);
                        if (binding.urlView!=null)binding.geckoview.setDynamicToolbarMaxHeight(0);

                    }
                    else {
                        if (binding.urlView!=null)binding.urlView.setTranslationY(scrollY - n);
                        if (binding.urlView!=null)binding.geckoview.setDynamicToolbarMaxHeight(n-scrollY);

                    }


                }
                else if(i>=scrollY) {
                    if (binding.urlView!=null)binding.urlView.setTranslationY(0);
                    if (binding.urlView!=null) binding.geckoview.setDynamicToolbarMaxHeight(dp);

                    if (scrollY==0) i=0;
                }
                Log.d("scrd",n+"");

            }
        });

    }
    public void useTabDetail(int index,boolean collapsed,boolean noNull){
        for (int i=0;i<tabList.size();i++)
        {
            if(i!=index){
                tabList.get(i).getModel().inactive();

            }
        }
        currentIndex=index;
        binding.geckoview.releaseSession();
        if (!noNull) tabList.get(index).getModel().active();
        binding.setSessionModel(tabList.get(index).getModel());
        binding.geckoview.setSession(binding.getSessionModel().getSession());



    }
    public void closeTabDetail(int index, BaseActivity context){
        binding.tabSize2.setText(tabList.size()-1+"");
        tabList.get(index).getModel().inactive();
        binding.geckoview.releaseSession();
        tabList.get(index).getModel().inactive();
        tabList.remove(index);
        if(tabList.size()>0){
            useTabDetail(Math.max(index-1,0),false,false);
            return;
        }else if(tabList.size()==0){newTabDetail("",null,0,context);return;}

    }


    public TabDetails(List<PageTab> tabList, int dp,ActivityMainBinding binding) {
        this.tabList = tabList;
        this.dp = dp;
        this.binding=binding;
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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setOnCloseListener(TabDetails.onCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public interface onCloseListener{
        void onClose(int index);
    }


}
