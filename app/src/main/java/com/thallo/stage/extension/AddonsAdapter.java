package com.thallo.stage.extension;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thallo.stage.components.popup.AddonsInformationPopup;
import com.thallo.stage.databinding.AddonsManagerItemBinding;

import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class AddonsAdapter extends RecyclerView.Adapter<AddonsAdapter.AddonsAdapterHolder>{
    List<WebExtension> webExtensions;
    AddonsManagerItemBinding binding;
    WebExtensionController webExtensionController;
    Activity activity;
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
            binding.addonsManagerIcon.setImageBitmap(webExtensions.get(position).metaData.icon.getBitmap(64).poll(1000));
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
                AddonsInformationPopup addonsInformationPopup=new AddonsInformationPopup(activity, webExtensions.get(position),webExtensionController,AddonsAdapter.this,position,webExtensions);
                addonsInformationPopup.show();


            }
        });



    }

    public void AddonsAdapters(List<WebExtension> webExtensions, WebExtensionController webExtensionController, Activity activity) {
        this.webExtensions = webExtensions;
        this.webExtensionController = webExtensionController;
        this.activity = activity;
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


}
