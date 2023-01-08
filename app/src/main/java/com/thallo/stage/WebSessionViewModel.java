package com.thallo.stage;

import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.liulishuo.okdownload.OkDownload;
import com.thallo.stage.components.dialog.AlertDialog;
import com.thallo.stage.components.dialog.ContextMenuDialog;
import com.thallo.stage.components.filePicker.GetFile;
import com.thallo.stage.components.popup.IntentPopup;
import com.thallo.stage.download.DownloadUtils;
import com.thallo.stage.components.dialog.JsChoiceDialog;
import com.thallo.stage.database.history.History;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.interfaces.confirm;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.Autofill;
import org.mozilla.geckoview.GeckoDisplay;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.MediaSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;
import org.mozilla.geckoview.WebResponse;

import java.util.List;


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
    StatusBar statusBar;
    SharedPreferences prefs;
    public WebSessionViewModel(GeckoSession session, BaseActivity context) {
        this.session = session;
        mContext=context;
        webExtensionController = GeckoRuntime.getDefault(mContext).getWebExtensionController();
        geckoRuntimeSettings=GeckoRuntime.getDefault(mContext).getSettings();
        WebExtension.SessionController sessionController= session.getWebExtensionController();
        historyViewModel = new ViewModelProvider(context).get(HistoryViewModel.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        IntentPopup intentPopup=new IntentPopup(context);
        statusBar=new StatusBar(context);




        session.setContentDelegate(new GeckoSession.ContentDelegate() {
            @Override
            public void onShowDynamicToolbar(@NonNull GeckoSession geckoSession) {
                GeckoSession.ContentDelegate.super.onShowDynamicToolbar(geckoSession);
                Toast.makeText(context, "aa", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContextMenu(@NonNull GeckoSession session, int screenX, int screenY, @NonNull ContextElement element) {
                GeckoSession.ContentDelegate.super.onContextMenu(session, screenX, screenY, element);
                ContextMenuDialog contextMenuDialog=new ContextMenuDialog(context,element);
                if(element.type==element.TYPE_IMAGE|element.type==element.TYPE_VIDEO) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        long[] pattern = { 10L, 60L }; // An array of longs of times for which to turn the vibrator on or off.
                        vibrator.vibrate(pattern, -1); // The index into pattern at which to repeat, or -1 if you don't want to repeat.
                    }

                    contextMenuDialog.open();
                }

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
                    builder.setNeutralButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DownloadUtils downloadUtils=new DownloadUtils(context);
                            downloadUtils.startTask(address);


                        }
                    });
                    builder.setNegativeButton("复制链接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(address);
                            Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
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
                getFile.open(context,0);
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
                if (!mUrl.contains("about:blank"))
                {
                    History history=new History(mUrl,mTitle,1);
                    historyViewModel.insertWords(history);



                }

            }

            @Override
            public void onPageStart(@NonNull GeckoSession session, @NonNull String url) {
                mUrl=url;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (BaseActivity.binding.urlView!=null) BaseActivity.binding.geckoview.setDynamicToolbarMaxHeight(BaseActivity.spToInt);
                if(prefs.getBoolean("settingProtecting",false)) session.getSettings().setUseTrackingProtection(true);
                else session.getSettings().setUseTrackingProtection(false);
                if (BaseActivity.binding.urlView!=null)BaseActivity.binding.urlView.setTranslationY(0);
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

            @Override
            public void onLocationChange(@NonNull GeckoSession session, @Nullable String url, @NonNull List<GeckoSession.PermissionDelegate.ContentPermission> perms) {
                GeckoSession.NavigationDelegate.super.onLocationChange(session, url, perms);
            }

            @Nullable
            @Override
            public GeckoResult<AllowOrDeny> onLoadRequest(@NonNull GeckoSession session, @NonNull LoadRequest request) {
                url1=request.uri;
                Uri uri=Uri.parse(url1);
                if (uri.getScheme()!=null){
                if(!uri.getScheme().contains("https") && !uri.getScheme().contains("http") && !uri.getScheme().contains("about"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(context.getPackageManager())!=null)
                    {
                        intentPopup.show(intent);
                        Log.d("scheme1",uri.getScheme());

                    }

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


        session.setMediaSessionDelegate(new MediaSession.Delegate() {
            Boolean orientation;

            @Override
            public void onFullscreen(@NonNull GeckoSession session, @NonNull MediaSession mediaSession, boolean enabled, @Nullable MediaSession.ElementMetadata meta) {
                MediaSession.Delegate.super.onFullscreen(session, mediaSession, enabled, meta);


                BaseActivity.binding.urlView.setVisibility(View.GONE);
                if (enabled) {
                    if (meta.height>meta.width)
                    {
                        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    else context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    BaseActivity.binding.urlView.setVisibility(View.GONE);
                    statusBar.hideStatusBar();
                    BaseActivity.binding.geckoview.setDynamicToolbarMaxHeight(0);
                }
                else {
                    context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    BaseActivity.binding.urlView.setVisibility(View.VISIBLE);
                    statusBar.showStatusBar();
                }


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
                statusBar.setStatusBarColor(R.color.alpha);

                if (prefs.getInt("textC",-1)==1){
                    statusBar.setTextColor(true);
                }
                else if (prefs.getInt("textC",-1)==0){
                    statusBar.setTextColor(false);
                }
            }else {
                BaseActivity.binding.fragmentContainerLayout.setVisibility(View.GONE);
                BaseActivity.binding.geckoview.setVisibility(View.VISIBLE);
                BaseActivity.binding.tabButton.setIconTint(ColorStateList.valueOf(mContext.getColor(R.color.textcolor)));
                BaseActivity.binding.menu.setIconTint(ColorStateList.valueOf(mContext.getColor(R.color.textcolor)));
                BaseActivity.binding.tabSize2.setTextColor(mContext.getColor(R.color.textcolor));
                statusBar.showStatusBar();
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
