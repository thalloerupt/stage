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
import com.thallo.stage.tab.TabDetails;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class SettingPopUp {


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
        dialogView= LayoutInflater.from(context).inflate(R.layout.setting_menu,null );
        TextView title=dialogView.findViewById(R.id.popTitle);
        reload = dialogView.findViewById(R.id.reload);
        setting = dialogView.findViewById(R.id.setting);
        popText= dialogView.findViewById(R.id.popTitle);
        desktopMode = dialogView.findViewById(R.id.desktop);
        linearLayout2= dialogView.findViewById(R.id.addonsIcon);
        addonsLayout=dialogView.findViewById(R.id.addonsLayout);
        popUp=new PopUp();
        mEditor = mSp.edit();
        tabDetails=new TabDetails();
        tabDetails.setThings(binding,tabList,dp,null,null);
        popText.setText(webSessionViewModel.getTitle());

        webExtensionController.list().accept(new GeckoResult.Consumer<List<WebExtension>>() {
            @Override
            public void accept(@Nullable List<WebExtension> webExtensions) {
                if (webExtensions.size()==0) addonsLayout.setVisibility(View.GONE);
                else addonsLayout.setVisibility(View.VISIBLE);

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




                    linearLayout2.addView(iconView);
                }
            }
        });






        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webSessionViewModel.getSession().reload();
                bottomSheetDialog.dismiss();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Setting.class);
                context.startActivity(intent);
            }
        });

        if(!mSp.getBoolean("mode", false))
        {
            desktopMode.setImageResource(R.drawable.ic_desk);

        }else if (mSp.getBoolean("mode",false)){desktopMode.setImageResource(R.drawable.ic_desktop_on);}
        desktopMode.setOnClickListener(new View.OnClickListener() {
            int i;
            @Override
            public void onClick(View view) {
                if (i==0) {mEditor.putBoolean("mode",true).commit();i=1;desktopMode.setImageResource(R.drawable.ic_desktop_on);}
                else {mEditor.putBoolean("mode",false).commit();i=0;desktopMode.setImageResource(R.drawable.ic_desk);}

            }
        });


        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();





    }

}
