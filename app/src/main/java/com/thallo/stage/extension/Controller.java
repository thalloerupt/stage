package com.thallo.stage.extension;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.tab.PageTab;
import com.thallo.stage.components.dialog.PermissionDialog;
import com.thallo.stage.WebSessionViewModel;
import com.thallo.stage.components.popup.PopUp;
import com.thallo.stage.tab.TabDetails;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class Controller  {
    /**
     * 用于处理webExtension的相关委托
     * webExtension与GeckoRuntime绑定
     * 由于时间紧张以及对其他因素的考量，很多委托并未实现
     */
    WebExtensionController webExtensionController;
    PopUp popUp;
    TabDetails tabDetails;
    BaseActivity context;
    WebSessionViewModel webSessionViewModel;
    List<PageTab> tabList;
    BottomSheetBehavior behavior;
    int exAmount;
    int currentIndex;
    onNewTab onNewTab;
    onInstallNewTab onInstallNewTab;
    GeckoResult i;
    public void setWebExtensionController(WebExtensionController webExtensionController) {
        this.webExtensionController = webExtensionController;
    }

    public void promptDelegate(Activity activity) {
        tabDetails.setCurrentIndex(currentIndex);
        //处理webExtension的提示委托
        webExtensionController.setPromptDelegate(new  WebExtensionController.PromptDelegate() {
                @Nullable
                @Override
                public GeckoResult<AllowOrDeny> onOptionalPrompt(@NonNull WebExtension extension, @NonNull String[] permissions, @NonNull String[] origins) {
                    return GeckoResult.allow();
                }

                @Nullable
                @Override
                public GeckoResult<AllowOrDeny> onUpdatePrompt(@NonNull WebExtension currentlyInstalled, @NonNull WebExtension updatedExtension, @NonNull String[] newPermissions, @NonNull String[] newOrigins) {
                    return GeckoResult.allow();}
            //webExtension安装完成后调起
                @Nullable
                @Override
                public GeckoResult onInstallPrompt(@NonNull WebExtension extension) {
                    //webExtension对tab的委托
                    extension.setTabDelegate(new WebExtension.TabDelegate() {
                        /**webExtension新建标签时调起
                         *
                         * 实例化一个GeckoSession
                         * GeckoResult将会带回这个GeckoSession
                         * 随后session会带有newtab的url
                         *
                         */
                        @Nullable
                        @Override
                        public GeckoResult<GeckoSession> onNewTab(@NonNull WebExtension source, @NonNull WebExtension.CreateTabDetails createDetails) {
                            GeckoSession session= new GeckoSession();
                            onInstallNewTab.InstallNewTab(session);
                            return GeckoResult.fromValue(session);
                        }
                    });
                    PermissionDialog dlg = new PermissionDialog(activity,extension);

                    if(dlg.showDialog() == 1)
                    {

                        return GeckoResult.allow();

                    }
                    else return GeckoResult.deny();





                }

            });
        }

    public void Details(){
        popUp=new PopUp();
        //获取已安装webExtension的列表
        webExtensionController.list().accept(new GeckoResult.Consumer<List<WebExtension>>() {
            @Override
            public void accept(@Nullable List<WebExtension> webExtensions) {
                exAmount=webExtensions.size();
                for (int i=0;i<webExtensions.size();i++)
                {
                    //webExtension的行为委托
                    webExtensions.get(i).setActionDelegate(new WebExtension.ActionDelegate() {

                        @Override
                        public void onBrowserAction(@NonNull WebExtension extension, @Nullable GeckoSession session, @NonNull WebExtension.Action action) {
                            extension.setTabDelegate(new WebExtension.TabDelegate() {
                                @Nullable
                                @Override
                                public GeckoResult<GeckoSession> onNewTab(@NonNull WebExtension source, @NonNull WebExtension.CreateTabDetails createDetails) {
                                    GeckoSession session= new GeckoSession();
                                    onNewTab.newTab(session);
                                    return GeckoResult.fromValue(session);
                                }

                                @Override
                                public void onOpenOptionsPage(@NonNull WebExtension source) {
                                    WebExtension.TabDelegate.super.onOpenOptionsPage(source);
                                }
                            });
                        }

                        @Override
                        public void onPageAction(@NonNull WebExtension extension, @Nullable GeckoSession session, @NonNull WebExtension.Action action) {
                            WebExtension.ActionDelegate.super.onPageAction(extension, session, action);
                        }

                        @Nullable
                        @Override
                        public GeckoResult<GeckoSession> onTogglePopup(@NonNull WebExtension extension, @NonNull WebExtension.Action action) {
                            GeckoSession session= new GeckoSession();
                            popUp.popUp(extension,session, context);
                            return GeckoResult.fromValue(session);
                        }

                        @Nullable
                        @Override
                        public GeckoResult<GeckoSession> onOpenPopup(@NonNull WebExtension extension, @NonNull WebExtension.Action action) {
                            GeckoSession session= new GeckoSession();
                            popUp.popUp(extension,session,context);
                            return GeckoResult.fromValue(session);
                        }
                    });

                }

            }
        });

    }
    public void setThing(BaseActivity context, TabDetails tabDetails, WebSessionViewModel webSessionViewModel, List<PageTab> tabList,  int currentIndex){
        this.context=context;
        this.tabDetails=tabDetails;
        this.webSessionViewModel=webSessionViewModel;
        this.tabList=tabList;
        this.behavior=behavior;
        this.currentIndex=currentIndex;


    }

    public int getExAmount() {
        return exAmount;
    }
    public interface onNewTab{
        void newTab(GeckoSession session);
    }
    public interface onInstallNewTab{
        void InstallNewTab(GeckoSession session);
    }


    public void setOnNewTab(Controller.onNewTab onNewTab) {
        this.onNewTab = onNewTab;
    }

    public void setOnInstallNewTab(Controller.onInstallNewTab onInstallNewTab) {
        this.onInstallNewTab = onInstallNewTab;
    }
}
