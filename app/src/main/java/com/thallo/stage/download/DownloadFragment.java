package com.thallo.stage.download;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thallo.stage.R;
import com.thallo.stage.database.download.Download;
import com.thallo.stage.database.download.DownloadViewModel;
import com.thallo.stage.databinding.FragmentDownloadBinding;

import java.util.List;


public class DownloadFragment extends Fragment {
    FragmentDownloadBinding binding;
    DownloadViewModel downloadViewModel;
    DownloadAdapter downloadAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDownloadBinding.inflate(inflater,container,false);
        binding.downloadRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.toolbar.setTitle(R.string.pop_download);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        downloadViewModel=new ViewModelProvider((ViewModelStoreOwner) getContext()).get(DownloadViewModel.class);
        downloadAdapter= new DownloadAdapter();
        downloadAdapter.setDownloadViewModel(downloadViewModel);
        downloadViewModel.getAllDownloadsLive().observe(getViewLifecycleOwner(), new Observer<List<Download>>() {
            @Override
            public void onChanged(List<Download> downloads) {

                downloadAdapter.setAllDownload(downloads);
                binding.downloadRecycler.setAdapter(downloadAdapter);
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}