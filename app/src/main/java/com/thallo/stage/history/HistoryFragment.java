package com.thallo.stage.history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thallo.stage.database.history.History;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.databinding.FragmentHistoryBinding;

import java.util.List;


public class HistoryFragment extends Fragment {
    HistoryViewModel historyViewModel;
    FragmentHistoryBinding binding;
    MyAdapter myAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHistoryBinding.inflate(inflater,container,false);
        binding.historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter=new MyAdapter();
        historyViewModel= new ViewModelProvider((FragmentActivity)getContext()).get(HistoryViewModel.class);
        historyViewModel.getAllHistoriesLive().observe((LifecycleOwner) this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                myAdapter.setAllHistory(histories);
                binding.historyRecycler.setItemViewCacheSize(histories.size());
                binding.historyRecycler.setAdapter(myAdapter);
                if (histories.size()==0) binding.lottie.setVisibility(View.VISIBLE);
                else binding.lottie.setVisibility(View.GONE);
            }
        });

        binding.lottie.loop(true);
        binding.lottie.playAnimation();




        // Inflate the layout for this fragment
        return binding.getRoot();
    }


}