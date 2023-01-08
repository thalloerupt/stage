package com.thallo.stage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.preference.PreferenceManager;


import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.components.Qr;
import com.thallo.stage.components.dialog.AgreementDialog;
import com.thallo.stage.components.filePicker.FilePicker;
import com.thallo.stage.components.filePicker.PickUtils;
import com.thallo.stage.components.popup.InformationPopup;
import com.thallo.stage.components.popup.PopUp;
import com.thallo.stage.components.popup.SearchPopup;
import com.thallo.stage.components.popup.SettingPopUp;
import com.thallo.stage.components.popup.TabsPopup;
import com.thallo.stage.databinding.ActivityMainBinding;
import com.thallo.stage.extension.AddOns;
import com.thallo.stage.extension.Controller;
import com.thallo.stage.tab.PageTab;
import com.thallo.stage.tab.TabDetails;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BaseActivity extends AppCompatActivity  {
    /**
     * author:  thallo
     * email:   l694630313@Gmail.com
     */
    public static String url;
    public static ActivityMainBinding binding;
    //tablist，标签列表
    public static List<PageTab> tabList;
    private int currentIndex;
    private static GeckoRuntime sRuntime;
    //WebExtensionCotroller
    // 详见 https://mozilla.github.io/geckoview/javadoc/mozilla-central/org/mozilla/geckoview/WebExtensionController.html
    public static WebExtensionController webExtensionController;
    //附加组件列表
    private List<AddOns> addOnsList = new ArrayList<AddOns>();
    AddOns add;
    Boolean is;
    BottomSheetDialog dialog;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    View dialogView;
    LinearLayout linearLayout2;
    public static int spToInt;
    Bitmap extensionIcon;
    Bitmap pngBM;
    androidx.appcompat.app.AlertDialog alertDialog;
    String s="";
    GeckoSession.SessionState mSessionState;
    WebExtension.SessionController sessionController;
    //附加组件popup
    PopUp popUp;
    //弹出菜单
    SettingPopUp settingPopUp;
    //WebExtensionController，用于管理附加组件的委托
    Controller controller;
    //管理tab相关事件
    TabDetails tabDetails;
    GeckoResult n;
    WebSessionViewModel webSessionViewModel;
    private List<PageTab> mTabList;
    Qr qr;
    public static Uri uri;
    public static FilePicker filePicker;
    SharedPreferences prefs;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        spToInt= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,64,getResources().getDisplayMetrics());
        //tablist
        tabList = new LinkedList<>();
        StatusBar statusBar=new StatusBar(this);
        if (BaseActivity.binding.urlView!=null)statusBar.setStatusBarColor(R.color.alpha);
        else statusBar.showStatusBar();
        TabsPopup tabsPopup=new TabsPopup(BaseActivity.this);
        SearchPopup searchPopup=new SearchPopup(this);
        InformationPopup informationPopup=new InformationPopup(this);
        mSp = getPreferences(MODE_PRIVATE);
        mEditor = mSp.edit();
        popUp=new PopUp();
        settingPopUp= new SettingPopUp();
        controller= new Controller();
        tabDetails= new TabDetails(tabList,spToInt,binding);
        tabDetails.setCurrentIndex(currentIndex);
        qr=new Qr();
        filePicker=new FilePicker(this);
        if(!mSp.getBoolean("first",false))
        {
            AgreementDialog agreementDialog=new AgreementDialog(this,mEditor);
            agreementDialog.show();
        }



         prefs= PreferenceManager.getDefaultSharedPreferences(this);






       Intent intent=getIntent();
       if (intent.getDataString()!=null)
       {
           newTab(intent.getDataString(),tabList.size());
       }else {
           newTab("about:blank",tabList.size());

       }



        webExtensionController = GeckoRuntime.getDefault(BaseActivity.this).getWebExtensionController();
        controller.setWebExtensionController(webExtensionController);
        controller.setThing(this,tabDetails,binding.getSessionModel(),tabList,currentIndex);
        controller.Details();
        controller.promptDelegate(BaseActivity.this);
        binding.geckoview.setAutofillEnabled(true);
        if(binding.menu!=null)
        {binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingPopUp.setting(BaseActivity.this,webExtensionController,tabList,binding,spToInt,currentIndex);
            }
        });}


        binding.tabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabsPopup.show(tabDetails.getTabList(),tabDetails.getTabList().size());
            }
        });
        binding.searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPopup.show();
            }
        });
        binding.information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informationPopup.show(binding.getSessionModel());
            }
        });


        searchPopup.setOnSearchListener(new SearchPopup.onSearchListener() {
            @Override
            public void onSearch(String value) {
                if(Patterns.WEB_URL.matcher(value).matches() || URLUtil.isValidUrl(value))
                {

                    binding.getSessionModel().getSession().loadUri(value);

                }
                else try {binding.getSessionModel().getSession().loadUri(prefs.getString("searchEngine","https://www.baidu.com/s?wd=")+value);
                }catch (Exception e){newTab(prefs.getString("searchEngine","https://www.baidu.com/s?wd=")+value,tabList.size());}
                searchPopup.dismiss();



            }
        });


        tabDetails.setOnCloseListener(new TabDetails.onCloseListener() {
            @Override
            public void onClose(int index) {
                tabsPopup.removeTab(index);
            }
        });


        controller.setOnNewTab(new Controller.onNewTab() {
            @Override
            public void newTab(GeckoSession session) {
                tabDetails.newTabDetail(null,session,tabList.size(),BaseActivity.this);

            }
        });
        controller.setOnInstallNewTab(new Controller.onInstallNewTab() {
            @Override
            public void InstallNewTab(GeckoSession session) {
                tabDetails.newTabDetail(null,session,tabList.size(),BaseActivity.this);

            }
        });

        settingPopUp.setOnNewTab(new SettingPopUp.onNewTab() {
            @Override
            public void newTab(GeckoSession session) {
                tabDetails.newTabDetail(null,session,tabList.size(),BaseActivity.this);

            }
        });

        tabsPopup.setOnAddNewTab(new TabsPopup.onAddNewTab() {
            @Override
            public void addNewTab() {
                newTab("about:blank",tabList.size());
            }
        });










    }
    public static void copyToClipboard(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(content);
        Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        if(!binding.getSessionModel().getUrl().contains("about:blank")) {
            if (binding.getSessionModel().isCanBack()) {
                binding.getSessionModel().getSession().goBack();
                return;
            } else if (tabList.size() != 1) {
                tabDetails.closeTabDetail(tabList.size() - 1, BaseActivity.this);
            } else if (tabList.size() == 1) {
                binding.getSessionModel().getSession().loadUri("about:blank");
                binding.getSessionModel().getSession().purgeHistory();
            }
        }else  super.onBackPressed();


    }



    public void newTab(String url,int index){
        tabDetails.newTabDetail(url,null,index,this);
        Log.d("tablist",tabDetails.getTabList().size()+"");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (url!=null)
        {
            newTab(url,tabList.size());
            url=null;
        }
        binding.getSessionModel().getSession().setActive(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            filePicker.setUri(Uri.parse("file://"+ PickUtils.getPath(this, data.getData())));

        }
        if (requestCode == 1) {
            prefs.edit().putString("bg","file:"+ PickUtils.getPath(this, data.getData())).commit();
            if ("file:"+ PickUtils.getPath(this, data.getData())!=null) {
                Uri uri=Uri.parse("file:"+ PickUtils.getPath(this, data.getData()));
                HomeFragment.binding.imageView22.setImageURI(uri);
                Glide.with(this).asBitmap().load("file:"+ PickUtils.getPath(this, data.getData())).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int darkMutedColor = palette.getMutedColor(Color.GREEN);
                                prefs.edit().putInt("bgColor",darkMutedColor).commit();
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            qr.show(this);

        } else {
            Toast.makeText(this,"请先授权",Toast.LENGTH_LONG).show();
        }

    }

    public WebSessionViewModel getWebSessionViewModel() {

        return binding.getSessionModel();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String IntentUrl=intent.getDataString();
        if (IntentUrl!=null)
            newTab(intent.getDataString(),tabList.size());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

