package com.thallo.stage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.thallo.stage.databinding.FragmentOSBinding;


public class OSFragment extends Fragment {
    FragmentOSBinding binding;
    private String[] datas = {"GeckoView - Mozilla","AndrodX - Google","Glide - bumptech","glide-transformations - wasabeef","lottie - airbnb","immersionbar - geyifeng","bga-qrcode-zxing - bingoogolapple"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentOSBinding.inflate(inflater,container,false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,datas);
        Intent intent = new Intent(getContext(), BaseActivity.class);
        binding.lisview1.setAdapter(adapter);
        binding.lisview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        BaseActivity.url="https://github.com/mozilla/geckoview";
                        getContext().startActivity(intent);
                        break;
                    case 1:
                        BaseActivity.url="https://github.com/androidx/androidx";
                        getContext().startActivity(intent);
                        break;
                    case 2:
                        BaseActivity.url="https://github.com/bumptech/glide";
                        getContext().startActivity(intent);
                        break;
                    case 3:
                        BaseActivity.url="https://github.com/wasabeef/glide-transformations";
                        getContext().startActivity(intent);
                        break;
                    case 4:
                        BaseActivity.url="https://github.com/airbnb/lottie-android";
                        getContext().startActivity(intent);
                        break;
                    case 5:
                        BaseActivity.url="https://github.com/gyf-dev/ImmersionBar";
                        getContext().startActivity(intent);
                        break;
                    case 6:
                        BaseActivity.url="https://github.com/bingoogolapple/BGAQRCode-Android";
                        getContext().startActivity(intent);
                        break;
                }
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}