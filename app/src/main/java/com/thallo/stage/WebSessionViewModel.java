package com.thallo.stage;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.thallo.stage.interfaces.favicon;
import com.thallo.stage.tab.TabDetails;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoSessionSettings;
import org.mozilla.geckoview.Image;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;
import org.mozilla.geckoview.WebResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;


public class WebSessionViewModel extends BaseObservable {
    private final GeckoSession session;
    private String mUrl;
    private boolean canBack;
    private boolean canForward;
    private boolean active;
    int mProcess;
    private String mTitle;
    private NewSessionHandler newSessionHandler;
    private GeckoSession.SessionState mSessionState;
    private final Context mContext;
    private boolean mVisibility;
    private WebExtensionController webExtensionController;
    String mIcon;
    String url1;
    String favText;
    androidx.appcompat.app.AlertDialog alertDialog;
    String s="";
    GeckoResult n;
    Boolean showHome;
    private String faviconUrl;

    public WebSessionViewModel(GeckoSession session, Context context) {
        this.session = session;
        mContext=context;
        webExtensionController = GeckoRuntime.getDefault(mContext).getWebExtensionController();
        WebExtension.SessionController sessionController= session.getWebExtensionController();
        session.setContentDelegate(new GeckoSession.ContentDelegate() {
         /**   @Override
            public void onFirstContentfulPaint(@NonNull GeckoSession session) {
                geckoDisplay= session.acquireDisplay();

            }**/

            @Override
            public void onExternalResponse(@NonNull GeckoSession session, @NonNull WebResponse response) {
                String address = response.uri;
                if (address.endsWith("xpi"))
                {
                    String ADD =address;
                    Log.d("网址",ADD);

                    webExtensionController
                            .install(ADD)
                            .accept(webExtension -> Toast.makeText(mContext,webExtension.metaData.name+"安装成功",Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onTitleChange(@NonNull GeckoSession session, @Nullable String title) {
                webExtensionController.setTabActive(session,true);
                if (mUrl.indexOf("about:blank")!=-1)
                {mTitle = "新标签页";Log.d("YES",title);}
                else mTitle=title;
                notifyPropertyChanged(BR.title);
            }
        });

        /*
        session.setAutofillDelegate(new Autofill.Delegate() {
            @Override
            public void onAutofill(@NonNull GeckoSession session, int notification, @Nullable Autofill.Node node) {
                AutofillManager afm = context.getSystemService(AutofillManager.class);
                if(afm!=null){

                }
            }
        });*/

        session.setProgressDelegate(new GeckoSession.ProgressDelegate() {
            @Override
            public void onSessionStateChange(@NonNull GeckoSession session, @NonNull GeckoSession.SessionState sessionState) {
                mSessionState=sessionState;
            }
            @Override
            public void onPageStop(@NonNull GeckoSession session, boolean success) {


            }

            @Override
            public void onPageStart(@NonNull GeckoSession session, @NonNull String url) {
                mUrl=url;
                URI uri=URI.create(url);
                if(url.indexOf("about:blank")!=-1)
                {

                }
                Log.d("YES",url);

                notifyPropertyChanged(BR.url);

            }



            @Override
            public void onProgressChange(@NonNull GeckoSession session, int progress) {
                if (progress==100) mProcess=0;
                else mProcess=progress;
                notifyPropertyChanged(BR.process);



            }
        });
        session.setNavigationDelegate(new GeckoSession.NavigationDelegate() {
            @Nullable
            @Override
            public GeckoResult<AllowOrDeny> onLoadRequest(@NonNull GeckoSession session, @NonNull LoadRequest request) {
                url1=request.uri;

                return GeckoResult.allow();
            }

            @Nullable
            @Override
            public GeckoResult<GeckoSession> onNewSession(@NonNull GeckoSession session, @NonNull String uri) {
                if(newSessionHandler==null)
                    return null;

                return newSessionHandler.onNewSession(session, uri);
            }
            @Override
            public void onCanGoBack(@NonNull GeckoSession session, boolean canGoBack) {
                canBack=canGoBack;
                notifyPropertyChanged(BR.canBack);
            }
            @Override
            public void onCanGoForward(@NonNull GeckoSession session, boolean canGoForward) {
                canForward=canGoForward;
                notifyPropertyChanged(BR.canForward);
            }
        });







    }



    public GeckoSession.SessionState getmSessionState() {
        return mSessionState;
    }

    public String getIcon() {
        return mIcon=faviconUrl;
    }

    @Bindable
    public String getUrl(){
        return mUrl;
    }

    @Bindable
    public int getProcess() {
        return mProcess;
    }

    public boolean getVisibility() {
        return mVisibility;
    }

    @Bindable
    public boolean isCanBack(){
        return canBack;
    }
    @Bindable
    public boolean isCanForward() {
        return canForward;
    }
    @Bindable
    public String getTitle(){
        return mTitle;
    }
    @Bindable
    public boolean isActive() {
        return active;
    }

    @BindingAdapter("enabled")
    public static void setEnabled(ImageView v, boolean enabled){
        v.setEnabled(enabled);
    }
    public GeckoSession getSession() {
        return session;
    }

    public void inactive(){
        active=false;
        notifyPropertyChanged(BR.active);
    }
    public void active(GeckoSession.SessionState sessionState){
        if(!session.isOpen()){
            session.open(GeckoRuntime.getDefault(mContext));
            if(sessionState!=null)
                session.restoreState(sessionState);
        }
        active=true;
        notifyPropertyChanged(BR.active);
    }

    public void setNewSessionHandler(NewSessionHandler newSessionHandler) {
        this.newSessionHandler = newSessionHandler;
    }


    public void setFavicon(String faviconUrl,Context context,favicon fav) {
       }


    public interface NewSessionHandler{
        GeckoResult<GeckoSession> onNewSession(GeckoSession session,String uri);

    }

    public String getFaviconUrl() {
        return faviconUrl;
    }

    public String getUrl1() {
        return url1;
    }

    public Boolean getShowHome() {
        return showHome;
    }
}
