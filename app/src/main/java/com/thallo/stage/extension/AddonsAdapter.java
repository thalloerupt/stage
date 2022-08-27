package com.thallo.stage.extension;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thallo.stage.R;
import com.thallo.stage.components.popup.AddonsInformationPopup;
import com.thallo.stage.databinding.AddonsManagerItemBinding;
import com.thallo.stage.extension.AddOns;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class AddonsAdapter extends RecyclerView.Adapter<AddonsAdapter.AddonsAdapterHolder>{
    List<WebExtension> webExtensions;
    AddonsManagerItemBinding binding;
    WebExtensionController webExtensionController;
    @NonNull
    @Override
    public AddonsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=AddonsManagerItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AddonsAdapterHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AddonsAdapterHolder holder, @SuppressLint("RecyclerView") int position) {
        binding.addonsManagerTitle.setText(webExtensions.get(position).metaData.name);
        binding.addonsManagerDes.setText(webExtensions.get(position).metaData.description);
        binding.addonsManagerSwitch.setChecked(webExtensions.get(position).metaData.enabled);
        try {
            binding.addonsManagerIcon.setImageBitmap(webExtensions.get(position).metaData.icon.getBitmap(64).poll(500));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        binding.addonsManagerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) webExtensionController.enable(webExtensions.get(position), WebExtensionController.EnableSource.USER);
                else webExtensionController.disable(webExtensions.get(position), WebExtensionController.EnableSource.USER);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddonsInformationPopup addonsInformationPopup=new AddonsInformationPopup(view.getContext(), webExtensions.get(position),webExtensionController,AddonsAdapter.this,position,webExtensions);
                addonsInformationPopup.show();


            }
        });



    }

    @Override
    public int getItemCount() {
        return webExtensions.size();
    }

    class AddonsAdapterHolder extends RecyclerView.ViewHolder{
        public AddonsAdapterHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    public void setWebExtensionController(WebExtensionController webExtensionController) {
        this.webExtensionController = webExtensionController;
    }

    public void setWebExtensions(List<WebExtension> webExtensions) {
        this.webExtensions = webExtensions;
    }
}
