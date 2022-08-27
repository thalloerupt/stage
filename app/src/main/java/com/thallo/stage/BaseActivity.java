package com.thallo.stage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;


import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.thallo.stage.components.Qr;
import com.thallo.stage.components.filePicker.FilePicker;
import com.thallo.stage.components.filePicker.PickUtils;
import com.thallo.stage.components.popup.InformationPopup;
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


public class BaseActivity extends AppCompatActivity  {
    public static String url;
    public static ActivityMainBinding binding;
    public static List<PageTab> tabList;
    private int currentIndex;
    private static GeckoRuntime sRuntime;
    public static WebExtensionController webExtensionController;
    private List<AddOns> addOnsList = new ArrayList<AddOns>();
    AddOns add;
    Boolean is;
    BottomSheetDialog dialog;
    BottomSheetBehavior behavior;
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
    PopUp popUp;
    SettingPopUp settingPopUp;
    Controller controller;
    TabDetails tabDetails;
    GeckoResult n;
    WebSessionViewModel webSessionViewModel;
    private List<PageTab> mTabList;
    Qr qr;
    public static Uri uri;
    public static FilePicker filePicker;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        View view=new View(this);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.background));
        window.setNavigationBarColor(getColor(R.color.background));
        setStatusBarColor();
        spToInt= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,64,getResources().getDisplayMetrics());
        tabList = new LinkedList<>();

        mSp = getPreferences(MODE_PRIVATE);
        mEditor = mSp.edit();
        popUp=new PopUp();
        settingPopUp= new SettingPopUp();
        controller= new Controller();
        tabDetails= new TabDetails();
        tabDetails.setThings(binding,tabList,spToInt);
        tabDetails.setCurrentIndex(currentIndex);
        ConstraintLayout constraintLayout = findViewById(R.id.toolLayout);
        behavior = BottomSheetBehavior.from(constraintLayout);
        qr=new Qr();
        filePicker=new FilePicker(this);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
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


        switch (prefs.getString("searchEngine","https://www.baidu.com/s?wd="))
        {
            case "https://www.baidu.com/s?wd=":
                binding.searchIcon.setImageResource(R.drawable.ic_baidu);
                break;
            case "https://www.google.com/search?q=":
                binding.searchIcon.setImageResource(R.drawable.ic_google);
                break;
            case "https://www.bing.com/search?q=":
                binding.searchIcon.setImageResource(R.drawable.ic_bing);
                break;
            case "https://www.sogou.com/web?query=":
                binding.searchIcon.setImageResource(R.drawable.ic_sogou);
                break;
        }

        binding.addressText2.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        binding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addressText2.setText("");
            }
        });

        binding.addressText2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(KeyEvent.KEYCODE_ENTER==i && keyEvent.getAction()==KeyEvent.ACTION_DOWN)
                {
                    if(Patterns.WEB_URL.matcher(binding.addressText2.getText().toString()).matches() || URLUtil.isValidUrl(binding.addressText2.getText().toString()))
                    {

                        binding.getSessionModel().getSession().loadUri(binding.addressText2.getText().toString());

                    }
                    else try {binding.getSessionModel().getSession().loadUri(prefs.getString("searchEngine","https://www.baidu.com/s?wd=")+binding.addressText2.getText().toString());
                    }catch (Exception e){newTab(prefs.getString("searchEngine","https://www.baidu.com/s?wd=")+binding.addressText2.getText().toString(),tabList.size());}
                    binding.editView.setVisibility(View.GONE);
                    binding.urlView.setVisibility(View.VISIBLE);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() { //弹出软键盘的代码
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }, 100);
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
                switch (prefs.getString("searchEngine","https://www.baidu.com/s?wd="))
                {
                    case "https://www.baidu.com/s?wd=":
                        binding.searchIcon.setImageResource(R.drawable.ic_baidu);
                        break;
                    case "https://www.google.com/search?q=":
                        binding.searchIcon.setImageResource(R.drawable.ic_google);
                        break;
                    case "https://www.bing.com/search?q=":
                        binding.searchIcon.setImageResource(R.drawable.ic_bing);
                        break;
                    case "https://www.sogou.com/web?query=":
                        binding.searchIcon.setImageResource(R.drawable.ic_sogou);
                        break;
                }
            }
        });

        binding.information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InformationPopup informationPopup=new InformationPopup(BaseActivity.this,binding.getSessionModel().getSession(),binding.getSessionModel());
                informationPopup.show();
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




            }
        });
        binding.menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingPopUp.setting(BaseActivity.this,webExtensionController,tabList,behavior,binding,spToInt,currentIndex);

            }
        });

        binding.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard(BaseActivity.this,binding.textView15.getText().toString());
            }
        });


       Intent intent=getIntent();
       if (intent.getDataString()!=null)
       {
           newTab(intent.getDataString(),tabList.size());
       }else {
           newTab("about:blank",tabList.size());

       }



        webExtensionController = GeckoRuntime.getDefault(BaseActivity.this).getWebExtensionController();
        controller.setWebExtensionController(webExtensionController);
        controller.setThing(this,tabDetails,binding.getSessionModel(),tabList,behavior,currentIndex);
        controller.Details();
        controller.promptDelegate(BaseActivity.this);
        binding.geckoview.setDynamicToolbarMaxHeight(spToInt);
        binding.geckoview.setAutofillEnabled(true);








    }
    public static void copyToClipboard(Context context, String content) {
        // 从 API11 开始 android 推荐使用 android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的 android.text.ClipboardManager，虽然提示 deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(content);
        Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
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


    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        int uiOption = window.getDecorView().getSystemUiVisibility();
        if (isDarkMode()) {
            //没有DARK_STATUS_BAR属性，通过位运算将LIGHT_STATUS_BAR属性去除
            window.getDecorView().setSystemUiVisibility(uiOption & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //这里是要注意的地方，如果需要补充新的FLAG，记得要带上之前的然后进行或运算
            window.getDecorView().setSystemUiVisibility(uiOption | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    public boolean isDarkMode() {
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }



    public void changStatusIconCollor(boolean setDark) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = getWindow().getDecorView();
            if(decorView != null){
                int vis = decorView.getSystemUiVisibility();
                if(setDark){
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else{
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            filePicker.setUri(Uri.parse("file://"+ PickUtils.getPath(this, data.getData())));
            return;
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
}

