package com.thallo.stage.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.R;
import com.thallo.stage.databinding.DiaAgreementBinding;

public class AgreementDialog extends MaterialAlertDialogBuilder {
    DiaAgreementBinding binding;
    public AgreementDialog(@NonNull Context context, SharedPreferences.Editor mEditor) {
        super(context);
        binding=DiaAgreementBinding.inflate(LayoutInflater.from(context));
        setView(binding.getRoot());
        binding.agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebDialog webDialog=new WebDialog(context,true);
                webDialog.show();
            }
        });
        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebDialog webDialog=new WebDialog(context,false);
                webDialog.show();
            }
        });
        setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mEditor.putBoolean("first",true).commit();
            }
        });
        setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Activity context1 = (Activity) context;
                context1.finish();


            }
        });
    }

}
