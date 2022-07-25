package com.thallo.stage.extension;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.thallo.stage.HomeFragment;
import com.thallo.stage.MainActivity;
import com.thallo.stage.PageTab;
import com.thallo.stage.WebSessionViewModel;
import com.thallo.stage.components.PopUp;
import com.thallo.stage.tab.TabDetails;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class Controller {
    WebExtensionController webExtensionController;
    PopUp popUp;
    TabDetails tabDetails;
    Context context;
    WebSessionViewModel webSessionViewModel;
    List<PageTab> tabList;
    BottomSheetBehavior behavior;
    HomeFragment homeFragment;
    FragmentManager fm;
    int exAmount;
    public void setWebExtensionController(WebExtensionController webExtensionController) {
        this.webExtensionController = webExtensionController;
    }

    public void promptDelegate() {
            webExtensionController.setPromptDelegate(new WebExtensionController.PromptDelegate() {
                @Nullable
                @Override
                public GeckoResult<AllowOrDeny> onOptionalPrompt(@NonNull WebExtension extension, @NonNull String[] permissions, @NonNull String[] origins) {

                    return GeckoResult.allow();
                }

                @Nullable
                @Override
                public GeckoResult<AllowOrDeny> onUpdatePrompt(@NonNull WebExtension currentlyInstalled, @NonNull WebExtension updatedExtension, @NonNull String[] newPermissions, @NonNull String[] newOrigins) {
                    return GeckoResult.allow(); }

                @Nullable
                @Override
                public GeckoResult onInstallPrompt(@NonNull WebExtension extension) {
                    extension.setTabDelegate(new WebExtension.TabDelegate() {
                        @Nullable
                        @Override
                        public GeckoResult<GeckoSession> onNewTab(@NonNull WebExtension source, @NonNull WebExtension.CreateTabDetails createDetails) {
                            GeckoSession session= new GeckoSession();
                            tabDetails.newTabDetail(createDetails.url,tabList.size(),context,behavior);
                            fm.beginTransaction().hide(homeFragment).commit();
                            return GeckoResult.fromValue(session);
                        }
                    });
                    return GeckoResult.allow();
                }

            });
        }

    public void Details(){
        popUp=new PopUp();
        webExtensionController.list().accept(new GeckoResult.Consumer<List<WebExtension>>() {
            @Override
            public void accept(@Nullable List<WebExtension> webExtensions) {
                exAmount=webExtensions.size();
                for (int i=0;i<webExtensions.size();i++)
                {
                    webExtensions.get(i).setActionDelegate(new WebExtension.ActionDelegate() {
                        @Override
                        public void onBrowserAction(@NonNull WebExtension extension, @Nullable GeckoSession session, @NonNull WebExtension.Action action) {
                            extension.setTabDelegate(new WebExtension.TabDelegate() {
                                @Nullable
                                @Override
                                public GeckoResult<GeckoSession> onNewTab(@NonNull WebExtension source, @NonNull WebExtension.CreateTabDetails createDetails) {
                                    GeckoSession session= new GeckoSession();
                                    tabDetails.newTabDetail(createDetails.url,tabList.size(),context,behavior);
                                    fm.beginTransaction().hide(homeFragment).commit();
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
    public void setThing(Context context, TabDetails tabDetails, WebSessionViewModel webSessionViewModel, List<PageTab> tabList, BottomSheetBehavior behavior, HomeFragment homeFragment, FragmentManager fm){
        this.context=context;
        this.tabDetails=tabDetails;
        this.webSessionViewModel=webSessionViewModel;
        this.tabList=tabList;
        this.behavior=behavior;
        this.homeFragment=homeFragment;
        this.fm=fm;


    }

    public int getExAmount() {
        return exAmount;
    }
}
