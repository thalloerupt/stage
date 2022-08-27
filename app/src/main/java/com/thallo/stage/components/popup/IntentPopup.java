package com.thallo.stage.components.popup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.R;
import com.thallo.stage.databinding.PopupInformationBinding;
import com.thallo.stage.databinding.PopupIntentBinding;
import com.thallo.stage.databinding.SettingMenuBinding;

public class IntentPopup {
    Context context;
    PopupIntentBinding binding;



    public IntentPopup(Context context){
        this.context=context;
    }
    public void show(Intent intent){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        binding=PopupIntentBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
        binding.IntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
    }


}
