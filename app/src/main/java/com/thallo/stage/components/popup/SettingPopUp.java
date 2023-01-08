package com.thallo.stage.components.popup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.FragmentHolder;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.components.dialog.BookmarkDialog;
import com.thallo.stage.extension.Controller;
import com.thallo.stage.tab.PageTab;
import com.thallo.stage.R;
import com.thallo.stage.Setting;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.databinding.ActivityMainBinding;
import com.thallo.stage.databinding.SettingMenuBinding;
import com.thallo.stage.tab.TabDetails;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoSessionSettings;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class SettingPopUp {
    SettingMenuBinding mBinding;
    onNewTab onNewTab;
    onInstallNewTab onInstallNewTab;

    public void setting (BaseActivity context, WebExtensionController webExtensionController, List<PageTab> tabList, ActivityMainBinding binding, int dp, int currentIndex){
        ImageView reload,setting,desktopMode;
        View dialogView;
        LinearLayout linearLayout2;
        PopUp popUp;
        SharedPreferences.Editor mEditor;
        TabDetails tabDetails;
        TextView popText;
        View addonsLayout;
        BookmarkViewModel bookmarkViewModel;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        mBinding=SettingMenuBinding.inflate(LayoutInflater.from(context));

        popUp=new PopUp();
        tabDetails=new TabDetails(tabList,dp,binding);
        tabDetails.setCurrentIndex(currentIndex);
        mBinding.popTitle.setText(binding.getSessionModel().getTitle());
        bookmarkViewModel=new ViewModelProvider((ViewModelStoreOwner) context).get(BookmarkViewModel.class);

        webExtensionController.list().accept(new GeckoResult.Consumer<List<WebExtension>>() {
            @Override
            public void accept(@Nullable List<WebExtension> webExtensions) {
                if (webExtensions.size()==0) mBinding.addonsLayout.setVisibility(View.GONE);
                else mBinding.addonsLayout.setVisibility(View.VISIBLE);

                for (int i=0;i<webExtensions.size();i++)
                {
                    View iconView= LayoutInflater.from(context).inflate(R.layout.addons_icons,null);
                    ImageView imageView = iconView.findViewById(R.id.imageView2);
                    View badgeLayout=iconView.findViewById(R.id.badgeLayout);
                    CardView badgeCard=iconView.findViewById(R.id.badgeCard);
                    TextView badeText=iconView.findViewById(R.id.badgeText);



                    webExtensions.get(i).setActionDelegate(new WebExtension.ActionDelegate() {
                        @Nullable
                        @Override
                        public GeckoResult<GeckoSession> onTogglePopup(@NonNull WebExtension extension, @NonNull WebExtension.Action action) {
                            GeckoSession session= new GeckoSession();
                            popUp.popUp(extension,session,context);

                            return GeckoResult.fromValue(session);                        }

                        @Nullable
                        @Override
                        public GeckoResult<GeckoSession> onOpenPopup(@NonNull WebExtension extension, @NonNull WebExtension.Action action) {
                            GeckoSession session= new GeckoSession();
                            popUp.popUp(extension,session,context);
                            return GeckoResult.fromValue(session);                        }


                        @Override
                        public void onBrowserAction(@NonNull WebExtension extension, @Nullable GeckoSession session, @NonNull WebExtension.Action action) {
                            iconView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    action.click();
                                    bottomSheetDialog.dismiss();


                                }
                            });
                            extension.setTabDelegate(new WebExtension.TabDelegate() {
                                @Nullable
                                @Override
                                public GeckoResult<GeckoSession> onNewTab(@NonNull WebExtension source, @NonNull WebExtension.CreateTabDetails createDetails) {
                                    GeckoSession session= new GeckoSession();
                                    onNewTab.newTab(session);
                                    return GeckoResult.fromValue(session);
                                }
                            });
                            badgeCard.setCardBackgroundColor(action.badgeBackgroundColor);
                            if (action.badgeText!=null)
                                badeText.setText(action.badgeText);
                            Log.d("badgeText",action.badgeText);
                            try {
                                Bitmap bitmap=action.icon.getBitmap(72).poll(500);
                                if (bitmap!=null){
                                    imageView.setImageBitmap(bitmap);
                                    mBinding.addonsIcon.addView(iconView);
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }


                        }
                    });

                }
            }
        });


        mBinding.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FragmentHolder.class);
                intent.putExtra("page","HISTORY");
                context.startActivity(intent);
                bottomSheetDialog.dismiss();

            }
        });

        mBinding.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookmarkDialog bookmarkDialog=new BookmarkDialog(context,binding.getSessionModel().getTitle(),binding.getSessionModel().getUrl(),bookmarkViewModel);
                bookmarkDialog.open();
                bottomSheetDialog.dismiss();
            }
        });

        mBinding.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FragmentHolder.class);
                intent.putExtra("page","BOOKMARK");
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        mBinding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FragmentHolder.class);
                intent.putExtra("page","DOWNLOAD");
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        mBinding.addons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FragmentHolder.class);
                intent.putExtra("page","ADDONS");
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        mBinding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.getSessionModel().isCanForward())
                    binding.getSessionModel().getSession().goForward();
                bottomSheetDialog.dismiss();
            }
        });
        bookmarkViewModel.findBookmarksWithPattern(binding.getSessionModel().getUrl()).observe(context, new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> list) {
                if (list.size()!=0)mBinding.star.setImageResource(R.drawable.ic_round_star);
                else mBinding.star.setImageResource(R.drawable.ic_star);
            }
        });







        mBinding.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getSessionModel().getSession().reload();
                bottomSheetDialog.dismiss();
            }
        });

        mBinding.menuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabDetails.newTabDetail("https://addons.mozilla.org/zh-CN/firefox/",null,tabList.size(),context);
                bottomSheetDialog.dismiss();

            }
        });

        mBinding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Setting.class);
                context.startActivity(intent);
                bottomSheetDialog.dismiss();

            }
        });
        int i=binding.getSessionModel().getSession().getSettings().getUserAgentMode();
        mBinding.desktop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (i==GeckoSessionSettings.USER_AGENT_MODE_DESKTOP)
                {
                    binding.getSessionModel().getSession().getSettings().setUserAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE);
                    binding.getSessionModel().getSession().reload();
                }else if(i==GeckoSessionSettings.USER_AGENT_MODE_MOBILE)
                {
                    binding.getSessionModel().getSession().getSettings().setUserAgentMode(GeckoSessionSettings.USER_AGENT_MODE_DESKTOP);
                    binding.getSessionModel().getSession().reload();

                }

                bottomSheetDialog.dismiss();

            }

        });
        if (i==GeckoSessionSettings.USER_AGENT_MODE_DESKTOP)
        {
            mBinding.desktopBg.setVisibility(View.VISIBLE);
        }else if(i==GeckoSessionSettings.USER_AGENT_MODE_MOBILE)
        {
            mBinding.desktopBg.setVisibility(View.GONE);
        }


        bottomSheetDialog.setContentView(mBinding.getRoot());
        bottomSheetDialog.show();





    }
    public interface onNewTab{
        void newTab(GeckoSession session);
    }
    public interface onInstallNewTab{
        void InstallNewTab(GeckoSession session);
    }

    public void setOnNewTab(SettingPopUp.onNewTab onNewTab) {
        this.onNewTab = onNewTab;
    }

    public void setOnInstallNewTab(SettingPopUp.onInstallNewTab onInstallNewTab) {
        this.onInstallNewTab = onInstallNewTab;
    }
}
