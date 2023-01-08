package com.thallo.stage;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StatusBar {
    private Activity activity;
    View view;
    //初始化activity
    public StatusBar(Activity activity){
        this.activity = activity;
        view= activity.getWindow().getDecorView();
    }

    //将状态栏设置为传入的color
    public void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= 21) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
            setTextColor(isDarkMode());
        }
    }

    //隐藏状态栏
    public void hideStatusBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    public void showStatusBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarColor();


        }
    }

    private void setStatusBarColor() {
        Window window = activity.getWindow();
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
        int mode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }

    //设置状态栏字体颜色
    public void setTextColor(boolean isDarkBackground){
        View decor = activity.getWindow().getDecorView();
        if (isDarkBackground) {
            //黑暗背景字体浅色
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            //高亮背景字体深色
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}

