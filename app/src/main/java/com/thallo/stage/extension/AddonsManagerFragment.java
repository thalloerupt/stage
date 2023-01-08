package com.thallo.stage.extension;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thallo.stage.BaseActivity;
import com.thallo.stage.FragmentHolder;
import com.thallo.stage.R;
import com.thallo.stage.databinding.FragmentAddonsManagerBinding;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.WebExtension;
import org.mozilla.geckoview.WebExtensionController;

import java.util.List;

public class AddonsManagerFragment extends Fragment {
    FragmentAddonsManagerBinding binding;
    WebExtensionController webExtensionController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webExtensionController= GeckoRuntime.getDefault(getContext()).getWebExtensionController();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAddonsManagerBinding.inflate(inflater,container,false);
        binding.AddonsRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
        AddonsAdapter addonsAdapter=new AddonsAdapter();
        Log.d("dz",webExtensionController+"");
        webExtensionController.list().accept(new GeckoResult.Consumer<List<WebExtension>>() {
            @Override
            public void accept(@Nullable List<WebExtension> webExtensions) {
                addonsAdapter.AddonsAdapters(webExtensions,webExtensionController,getActivity());
                binding.AddonsRecyler.setAdapter(addonsAdapter);
                if (webExtensions.size()==0) binding.addonsLottie.setVisibility(View.VISIBLE);
                else binding.addonsLottie.setVisibility(View.GONE);

            }
        });
        binding.addonsLottie.loop(true);
        binding.addonsLottie.playAnimation();
        binding.toolbar.setTitle(R.string.pop_addons);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        binding.constraintLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.url="https://addons.mozilla.org/zh-CN/firefox/";
                Intent intent = new Intent(getContext(), BaseActivity.class);
                getContext().startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}