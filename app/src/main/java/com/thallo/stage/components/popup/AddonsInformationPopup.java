package com.thallo.stage.components.popup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.R;
import com.thallo.stage.databinding.PopupInformationAddonsBinding;
import com.thallo.stage.databinding.PopupInformationBinding;
import com.thallo.stage.extension.AddonsAdapter;

import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class AddonsInformationPopup {
    Context context;
    WebExtension webExtension;
    WebExtensionController webExtensionController;
    AddonsAdapter addonsAdapter;
    BottomSheetDialog bottomSheetDialog;
    PopupInformationAddonsBinding binding;
    int position;
    List<WebExtension> webExtensions;
    public AddonsInformationPopup(Activity context, WebExtension webExtension, WebExtensionController webExtensionController, AddonsAdapter addonsAdapter, int position, List<WebExtension> webExtensions){
        this.context=context;
        this.webExtension=webExtension;
        this.webExtensionController=webExtensionController;
        this.addonsAdapter=addonsAdapter;
        this.position=position;
        this.webExtensions=webExtensions;
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        binding= PopupInformationAddonsBinding.inflate(LayoutInflater.from(context));
        binding.popupInformationAddonsCreator.setText(webExtension.metaData.creatorName);
        binding.popupInformationAddonsDes.setText(webExtension.metaData.description);
        binding.popupInformationAddonsVersion.setText(webExtension.metaData.version);
        binding.popupInformationAddonsName.setText(webExtension.metaData.name);
        try {
            binding.popupInformationAddonsIcon.setImageBitmap(webExtension.metaData.icon.getBitmap(72).poll(500));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        binding.popupInformationAddonsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webExtensionController.uninstall(webExtension);
                bottomSheetDialog.dismiss();
                webExtensions.remove(position);
                addonsAdapter.notifyItemRemoved(position);
                addonsAdapter.notifyItemRangeChanged(position, webExtensions.size());
            }
        });
        if (webExtension.metaData.optionsPageUrl == null)
            binding.popupInformationAddonsSetting.setVisibility(View.GONE);

        binding.popupInformationAddonsSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.url=webExtension.metaData.optionsPageUrl;
                context.startActivity(new Intent(context,BaseActivity.class));
                context.finish();
            }
        });
    }
    public void show(){
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
    }

}
