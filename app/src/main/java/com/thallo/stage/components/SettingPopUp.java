package com.thallo.stage.components;

import android.content.Context;
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
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.HomeFragment;
import com.thallo.stage.MainActivity;
import com.thallo.stage.PageTab;
import com.thallo.stage.R;
import com.thallo.stage.Setting;
import com.thallo.stage.WebSessionViewModel;
import com.thallo.stage.databinding.ActivityMainBinding;
import com.thallo.stage.databinding.SettingMenuBinding;
import com.thallo.stage.tab.TabDetails;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class SettingPopUp {
    SettingMenuBinding mBinding;


    public void setting (Context context, WebExtensionController webExtensionController, WebSessionViewModel webSessionViewModel, SharedPreferences mSp, List<PageTab> tabList, BottomSheetBehavior behavior, ActivityMainBinding binding, int dp, HomeFragment homeFragment, FragmentManager fm){
        ImageView reload,setting,desktopMode;
        View dialogView;
        LinearLayout linearLayout2;
        PopUp popUp;
        SharedPreferences.Editor mEditor;
        TabDetails tabDetails;
        TextView popText;
        View addonsLayout;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
        mBinding=SettingMenuBinding.inflate(LayoutInflater.from(context));

        popUp=new PopUp();
        mEditor = mSp.edit();
        tabDetails=new TabDetails();
        tabDetails.setThings(binding,tabList,dp,fm,homeFragment);
        mBinding.popTitle.setText(webSessionViewModel.getTitle());

        webExtensionController.list().accept(new GeckoResult.Consumer<List<WebExtension>>() {
            @Override
            public void accept(@Nullable List<WebExtension> webExtensions) {
                if (webExtensions.size()==0) mBinding.addonsLayout.setVisibility(View.GONE);
                else mBinding.addonsLayout.setVisibility(View.VISIBLE);

                for (int i=0;i<webExtensions.size();i++)
                {
                    View iconView= LayoutInflater.from(context).inflate(R.layout.addons_icons,null );
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
                                    tabDetails.newTabDetail(createDetails.url,tabList.size(),context,behavior);
                                    fm.beginTransaction().hide(homeFragment).commit();
                                    return GeckoResult.fromValue(session);
                                }
                            });
                            badgeCard.setCardBackgroundColor(action.badgeBackgroundColor);
                            if (action.badgeText!=null)
                                badeText.setText(action.badgeText);
                            Log.d("badgeText",action.badgeText);






                            action.icon.getBitmap(72).accept(new GeckoResult.Consumer<Bitmap>() {
                                @Override
                                public void accept(@Nullable Bitmap bitmap) {

                                    imageView.setImageBitmap(bitmap);

                                }
                            });


                        }
                    });




                    mBinding.addonsIcon.addView(iconView);
                }
            }
        });






        mBinding.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webSessionViewModel.getSession().reload();
                bottomSheetDialog.dismiss();
            }
        });

        mBinding.menuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabDetails.newTabDetail("https://addons.mozilla.org/zh-CN/firefox/",tabList.size(),context,behavior);
                bottomSheetDialog.dismiss();
                fm.beginTransaction().hide(homeFragment).commit();
            }
        });

        mBinding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Setting.class);
                context.startActivity(intent);
            }
        });

        if(!mSp.getBoolean("mode", false))
        {
            mBinding.desktop.setImageResource(R.drawable.ic_desk);

        }else if (mSp.getBoolean("mode",false)){mBinding.desktop.setImageResource(R.drawable.ic_desktop_on);}
        mBinding.desktop.setOnClickListener(new View.OnClickListener() {
            int i;
            @Override
            public void onClick(View view) {
                if (i==0) {mEditor.putBoolean("mode",true).commit();i=1;mBinding.desktop.setImageResource(R.drawable.ic_desktop_on);}
                else {mEditor.putBoolean("mode",false).commit();i=0;mBinding.desktop.setImageResource(R.drawable.ic_desk);}

            }
        });


        bottomSheetDialog.setContentView(mBinding.getRoot());
        bottomSheetDialog.show();





    }

}
