package com.thallo.stage;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.thallo.stage.dialog.ContentPermissionDialog;
import com.thallo.stage.interfaces.confirm;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;
import org.mozilla.geckoview.WebResponse;

import java.net.URI;


public class WebSessionViewModel extends BaseObservable  {
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
    boolean isSecure;
    int i;
    public WebSessionViewModel(GeckoSession session, Context context) {
        this.session = session;
        mContext=context;
        webExtensionController = GeckoRuntime.getDefault(mContext).getWebExtensionController();
        WebExtension.SessionController sessionController= session.getWebExtensionController();
        session.setContentDelegate(new GeckoSession.ContentDelegate() {
            @Override
            public void onShowDynamicToolbar(@NonNull GeckoSession geckoSession) {
                GeckoSession.ContentDelegate.super.onShowDynamicToolbar(geckoSession);
            }

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

        session.setPromptDelegate(new GeckoSession.PromptDelegate() {
            @Nullable
            @Override
            public GeckoResult<PromptResponse> onAlertPrompt(@NonNull GeckoSession session, @NonNull AlertPrompt prompt) {
                return GeckoSession.PromptDelegate.super.onAlertPrompt(session, prompt);
            }
        });

        session.setPermissionDelegate(new GeckoSession.PermissionDelegate() {
            @Nullable
            @Override
            public GeckoResult<Integer> onContentPermissionRequest(@NonNull GeckoSession session, @NonNull ContentPermission perm) {


                return GeckoResult.fromValue(i);
            }
        });

        session.setProgressDelegate(new GeckoSession.ProgressDelegate() {

            @Override
            public void onSecurityChange(@NonNull GeckoSession session, @NonNull SecurityInformation securityInfo) {
                GeckoSession.ProgressDelegate.super.onSecurityChange(session, securityInfo);
                isSecure=securityInfo.isSecure;
                notifyPropertyChanged(BR.secure);

            }

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
    @Bindable
    public boolean isSecure() {
        return isSecure;
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


    public void setFavicon(String faviconUrl, Context context, confirm fav) {
       }

    /*@Override
    public int getConfirm(int i) {
        this.i=i;
        Log.d("Permission", String.valueOf(i));
        return i;
    }*/


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
