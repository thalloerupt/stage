package com.thallo.stage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.components.dialog.AlertDialog;
import com.thallo.stage.components.filePicker.GetFile;
import com.thallo.stage.components.popup.IntentPopup;
import com.thallo.stage.download.DownloadUtils;
import com.thallo.stage.components.dialog.JsChoiceDialog;
import com.thallo.stage.database.history.History;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.interfaces.confirm;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.Autofill;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;
import org.mozilla.geckoview.WebResponse;


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
    HistoryViewModel historyViewModel;
    Boolean isProtecting;
    GeckoRuntimeSettings geckoRuntimeSettings;
    public WebSessionViewModel(GeckoSession session, BaseActivity context) {
        this.session = session;
        mContext=context;
        webExtensionController = GeckoRuntime.getDefault(mContext).getWebExtensionController();
        geckoRuntimeSettings=GeckoRuntime.getDefault(mContext).getSettings();
        WebExtension.SessionController sessionController= session.getWebExtensionController();
        historyViewModel = new ViewModelProvider(context).get(HistoryViewModel.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        IntentPopup intentPopup=new IntentPopup(context);



        session.setContentDelegate(new GeckoSession.ContentDelegate() {


               @Override
            public void onFirstContentfulPaint(@NonNull GeckoSession session) {

               }

            @Override
            public void onExternalResponse(@NonNull GeckoSession session, @NonNull WebResponse response) {
                String address = response.uri;
                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(context);
                if (address.endsWith("xpi"))
                {
                    String ADD =address;
                    Log.d("网址",ADD);

                    webExtensionController
                            .install(ADD)
                            .accept(webExtension -> Toast.makeText(mContext,webExtension.metaData.name+"安装成功",Toast.LENGTH_LONG).show());
                }else {
                    builder.setTitle("确定下载？");
                    builder.setMessage(address);
                    builder.setNeutralButton("第三方下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(Uri.parse(address),"*/*");
                            context.startActivity(intent);
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DownloadUtils downloadUtils=new DownloadUtils(context,1);
                            downloadUtils.open(address);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
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
            public GeckoResult<PromptResponse> onFilePrompt(@NonNull GeckoSession session, @NonNull FilePrompt prompt) {
                GetFile getFile=new GetFile();
                getFile.open(context);
                return GeckoResult.fromValue(prompt.confirm(context,getFile.getUri()));
            }

            @Nullable
            @Override
            public GeckoResult<PromptResponse> onChoicePrompt(@NonNull GeckoSession session, @NonNull ChoicePrompt prompt) {
                //prompt.
                JsChoiceDialog jsChoiceDialog=new JsChoiceDialog(context,prompt);
                jsChoiceDialog.showDialog();
                return GeckoResult.fromValue(prompt.confirm(jsChoiceDialog.getDialogResult()+""));
            }

            @Nullable
            @Override
            public GeckoResult<PromptResponse> onAlertPrompt(@NonNull GeckoSession session, @NonNull AlertPrompt prompt) {
                AlertDialog alertDialog=new AlertDialog(context,prompt);
                alertDialog.showDialog();
                return GeckoResult.fromValue(alertDialog.getDialogResult());
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
                isProtecting=session.getSettings().getUseTrackingProtection();
                isSecure=securityInfo.isSecure;
                notifyPropertyChanged(BR.secure);

            }

            @Override
            public void onSessionStateChange(@NonNull GeckoSession session, @NonNull GeckoSession.SessionState sessionState) {
                mSessionState=sessionState;
            }
            @Override
            public void onPageStop(@NonNull GeckoSession session, boolean success) {
                if (mUrl.indexOf("about:blank")==-1)
                {
                    History history=new History(mUrl,mTitle,1);
                    historyViewModel.insertWords(history);


                }

            }

            @Override
            public void onPageStart(@NonNull GeckoSession session, @NonNull String url) {
                mUrl=url;


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if(prefs.getBoolean("settingProtecting",false)) session.getSettings().setUseTrackingProtection(true);
                else session.getSettings().setUseTrackingProtection(false);
                BaseActivity.binding.toolLayout.setTranslationY(0);
                BaseActivity.binding.geckoview.setDynamicToolbarMaxHeight(BaseActivity.spToInt);


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
                Uri uri=Uri.parse(url1);
                if (uri!=null){
                if(uri.getScheme().indexOf("https")==-1&&uri.getScheme().indexOf("http")==-1&&uri.getScheme().indexOf("about")==-1)
                {
                    Log.d("scheme1",uri.getScheme());
                    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(context.getPackageManager())!=null)
                        intentPopup.show(intent);

                }}


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


        session.setHistoryDelegate(new GeckoSession.HistoryDelegate() {
            @Nullable
            @Override
            public GeckoResult<Boolean> onVisited(@NonNull GeckoSession session, @NonNull String url, @Nullable String lastVisitedURL, int flags) {
                return GeckoResult.fromValue(false);
            }

            @Override
            public void onHistoryStateChange(@NonNull GeckoSession session, @NonNull HistoryList historyList) {

            }
        });


        session.setAutofillDelegate(new Autofill.Delegate() {
            @Override
            public void onNodeAdd(@NonNull GeckoSession session, @NonNull Autofill.Node node, @NonNull Autofill.NodeData data) {


                Autofill.Delegate.super.onNodeAdd(session, node, data);
            }
        });




        geckoRuntimeSettings.setInputAutoZoomEnabled(true);
        geckoRuntimeSettings.setLoginAutofillEnabled(true);
        geckoRuntimeSettings.setAboutConfigEnabled(true);

        //强制手势缩放
        if(prefs.getBoolean("setting_scalable",false)) geckoRuntimeSettings.setForceUserScalableEnabled(true);
        else geckoRuntimeSettings.setForceUserScalableEnabled(false);
        //自动调整字体大小
        if(prefs.getBoolean("setting_FontSize",true)) geckoRuntimeSettings.setAutomaticFontSizeAdjustment(true);
        else geckoRuntimeSettings.setAutomaticFontSizeAdjustment(true);










    }




    public GeckoSession.SessionState getmSessionState() {
        return mSessionState;
    }
    @Bindable
    public boolean isSecure() {
        return isSecure;
    }

    public Boolean getProtecting() {
        return isProtecting;
    }

    public String getIcon() {
        return mIcon=faviconUrl;
    }

    @Bindable
    public String getUrl(){
        if(mUrl!=null){
            if(mUrl.indexOf("about:blank")!=-1)
            {
                BaseActivity.binding.fragmentContainerLayout.setVisibility(View.VISIBLE);
                BaseActivity.binding.geckoview.setVisibility(View.GONE);
            }else {
                BaseActivity.binding.fragmentContainerLayout.setVisibility(View.GONE);
                BaseActivity.binding.geckoview.setVisibility(View.VISIBLE);
            }
        }
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
    public void active(){
        if(!session.isOpen()){
            session.open(GeckoRuntime.getDefault(mContext));
            if(mSessionState!=null)
                session.restoreState(mSessionState);
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
