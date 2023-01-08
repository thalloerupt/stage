package com.thallo.stage.components.popup;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.R;
import com.thallo.stage.components.dialog.SearchSelectDialog;
import com.thallo.stage.databinding.PopupSearchBinding;
import com.thallo.stage.databinding.PopupTabsBinding;
import com.thallo.stage.tab.PageTab;

import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchPopup {
    PopupSearchBinding binding;
    Context context;
    MyBottomSheetDialog bottomSheetDialog;
    public onSearchListener onSearchListener;
    InputMethodManager imm ;
    SearchSelectDialog searchSelectDialog;
    SharedPreferences prefs;
    public SearchPopup(Activity context) {
        this.context = context;
        binding=PopupSearchBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog = new MyBottomSheetDialog(context, R.style.BottomSheetDialog,2);
        imm= (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.editTextTextPersonName3.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchSelectDialog=new SearchSelectDialog(context);
        prefs= PreferenceManager.getDefaultSharedPreferences(context);

        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.editTextTextPersonName3.setText("");
            }
        });
        binding.editTextTextPersonName3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(KeyEvent.KEYCODE_ENTER==i && keyEvent.getAction()==KeyEvent.ACTION_DOWN)
                {
                    onSearchListener.onSearch(binding.editTextTextPersonName3.getText().toString());
                }
                return false;
            }
        });
        binding.engineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchSelectDialog.show();

            }
        });

        searchSelectDialog.setOnSelect(new SearchSelectDialog.onSelect() {
            @Override
            public void select(int i) {
                switch (i){
                    case 0:
                        binding.engineIcon.setImageResource(R.drawable.ic_baidu);
                        break;
                    case 1:
                        binding.engineIcon.setImageResource(R.drawable.ic_google);
                        break;
                    case 2:
                        binding.engineIcon.setImageResource(R.drawable.ic_bing);


                        break;
                    case 3:
                        binding.engineIcon.setImageResource(R.drawable.ic_sogou);
                        break;
                }
            }
        });



        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
    }
    public interface onSearchListener{
        void onSearch(String value);
    }

    public void setOnSearchListener(SearchPopup.onSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public void show(){
        bottomSheetDialog.show();

        switch (prefs.getString("searchEngine", context.getString(R.string.baidu))){
            case "https://www.baidu.com/s?wd=":
                binding.engineIcon.setImageResource(R.drawable.ic_baidu);
                break;
            case "https://www.google.com/search?q=":
                binding.engineIcon.setImageResource(R.drawable.ic_google);
                break;
            case "https://www.bing.com/search?q=":
                binding.engineIcon.setImageResource(R.drawable.ic_bing);
                break;
            case "https://www.sogou.com/web?query=":
                binding.engineIcon.setImageResource(R.drawable.ic_sogou);
                break;
        }

        binding.editTextTextPersonName3.setFocusable(true);
        binding.editTextTextPersonName3.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { //弹出软键盘的代码
                imm.showSoftInput(binding.editTextTextPersonName3, InputMethodManager.SHOW_IMPLICIT);
                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 200);
        try {
            binding.textView4.setText(BaseActivity.binding.getSessionModel().getUrl());
            URI uri=URI.create(BaseActivity.binding.getSessionModel().getUrl());
            String faviconUrl=uri.getScheme()+"://"+uri.getHost()+"/favicon.ico";
            Glide.with(context).load(faviconUrl).placeholder(R.drawable.ic_internet)
                    .into(binding.imageView21);
        }catch (Exception e){

        }

        binding.button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(BaseActivity.binding.getSessionModel().getUrl());
                    Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                }catch (Exception e){}
            }
        });
        binding.button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    binding.editTextTextPersonName3.setText(BaseActivity.binding.getSessionModel().getUrl());
                    binding.editTextTextPersonName3.setSelection(BaseActivity.binding.getSessionModel().getUrl().length());
                }catch (Exception e){}
            }
        });
    }
    public void dismiss(){
        bottomSheetDialog.dismiss();
    }
}
