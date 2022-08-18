package com.thallo.stage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.thallo.stage.components.Qr;
import com.thallo.stage.components.popup.PopUp;
import com.thallo.stage.components.popup.SettingPopUp;
import com.thallo.stage.databinding.ActivityMainBinding;
import com.thallo.stage.extension.AddOns;
import com.thallo.stage.extension.Controller;
import com.thallo.stage.tab.PageTab;
import com.thallo.stage.tab.TabDetails;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mozilla.components.feature.qr.QrFragment;

public class BaseActivity extends AppCompatActivity  {
    public static String url;
    private ActivityMainBinding binding;
    private List<PageTab> tabList;
    private int currentIndex;
    private static GeckoRuntime sRuntime;
    public static WebExtensionController webExtensionController;
    private List<AddOns> addOnsList = new ArrayList<AddOns>();
    AddOns add;
    Boolean is;
    BottomSheetDialog dialog;
    BottomSheetBehavior behavior;
    HomeFragment homeFragment;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    View dialogView;
    LinearLayout linearLayout2;
    public int spToInt;
    Bitmap extensionIcon;
    Bitmap pngBM;
    androidx.appcompat.app.AlertDialog alertDialog;
    String s="";
    GeckoSession.SessionState mSessionState;
    WebExtension.SessionController sessionController;
    PopUp popUp;
    SettingPopUp settingPopUp;
    Controller controller;
    TabDetails tabDetails;
    GeckoResult n;
    WebSessionViewModel webSessionViewModel;
    private List<PageTab> mTabList;
    QrFragment qrFragment=new QrFragment();
    Qr qr;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        View view=new View(this);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.background)
                .autoStatusBarDarkModeEnable(true,0.2f)
                .init();
        spToInt= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,64,getResources().getDisplayMetrics());
        tabList = new LinkedList<>();
        homeFragment= new HomeFragment();
        mSp = getPreferences(MODE_PRIVATE);
        mEditor = mSp.edit();
        popUp=new PopUp();
        settingPopUp= new SettingPopUp();
        controller= new Controller();
        tabDetails= new TabDetails();
        tabDetails.setThings(binding,tabList,spToInt,getSupportFragmentManager(),homeFragment);
        tabDetails.setCurrentIndex(currentIndex);
        ConstraintLayout constraintLayout = findViewById(R.id.toolLayout);
        behavior = BottomSheetBehavior.from(constraintLayout);
        qr=new Qr();




        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState==BottomSheetBehavior.STATE_EXPANDED)
                {
                    behavior.setDraggable(false);

                }
                else{
                    behavior.setDraggable(true);

                }
                //这里是bottomSheet 状态的改变
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }

        });

        getSupportFragmentManager().beginTransaction().replace(binding.MainView.getId(), homeFragment,"home").commit();

        binding.addressText2.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        binding.addressText2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(KeyEvent.KEYCODE_ENTER==i && keyEvent.getAction()==KeyEvent.ACTION_DOWN)
                {
                    if(Patterns.WEB_URL.matcher(binding.addressText2.getText().toString()).matches() || URLUtil.isValidUrl(binding.addressText2.getText().toString()))
                    {

                        binding.getSessionModel().getSession().loadUri(binding.addressText2.getText().toString());

                    }
                    else try {binding.getSessionModel().getSession().loadUri("https://www.baidu.com/s?wd="+binding.addressText2.getText().toString());
                    }catch (Exception e){newTab("https://www.baidu.com/s?wd="+binding.addressText2.getText().toString(),tabList.size());}
                    getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                    binding.editView.setVisibility(View.GONE);
                    binding.urlView.setVisibility(View.VISIBLE);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }
                return false;
            }
        });

        binding.textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                binding.editView.setVisibility(View.VISIBLE);
                binding.addressText2.requestFocus();
                binding.addressText2.setSelectAllOnFocus(true);
                binding.addressText2.selectAll();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() { //弹出软键盘的代码
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(binding.addressText2, InputMethodManager.RESULT_SHOWN);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }, 200);
                binding.urlView.setVisibility(View.GONE);
            }
        });








        binding.tabButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


            }
        });
       // binding.homeButton.setImageDrawable(binding.geckoview.get);
        binding.add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               newTab("about:blank",tabList.size());
               binding.urlView.setVisibility(View.VISIBLE);
               binding.editView.setVisibility(View.GONE);
           }
       });
        binding.homeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getSessionModel().getSession().loadUri("about:blank");
                getSupportFragmentManager().beginTransaction().show(homeFragment).commit();


            }
        });
        binding.menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingPopUp.setting(BaseActivity.this,webExtensionController,tabList,behavior,binding,spToInt,homeFragment,getSupportFragmentManager(),currentIndex);

            }
        });

        //newTab("https://www.csdn.net/",0);

        newTab("about:blank",tabList.size());
        webExtensionController = GeckoRuntime.getDefault(BaseActivity.this).getWebExtensionController();
        controller.setWebExtensionController(webExtensionController);
        controller.setThing(this,tabDetails,binding.getSessionModel(),tabList,behavior,homeFragment,getSupportFragmentManager(),currentIndex);
        controller.Details();
        controller.promptDelegate(BaseActivity.this);
        binding.geckoview.setDynamicToolbarMaxHeight(spToInt);




















    }


    @Override
    public void onBackPressed() {
            if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                binding.urlView.setVisibility(View.VISIBLE);
                binding.editView.setVisibility(View.GONE);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return;
            }
            else{
                if(binding.getSessionModel().getUrl().indexOf("about:blank") == -1) {
                    if (binding.getSessionModel().isCanBack()) {
                        binding.getSessionModel().getSession().goBack();
                        return;
                    } else if (tabList.size() != 1) {
                        tabDetails.closeTabDetail(tabList.size() - 1, behavior, BaseActivity.this);
                    } else if (tabList.size() == 1) {
                        binding.getSessionModel().getSession().loadUri("about:blank");
                    }
                }else  super.onBackPressed();
            }

    }



    public void newTab(String url,int index){
        tabDetails.newTabDetail(url,index,this,behavior);
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

    }

    @Override
    protected void onStop() {
        super.onStop();
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
}

