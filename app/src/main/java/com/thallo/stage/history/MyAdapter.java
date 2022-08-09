package com.thallo.stage.history;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thallo.stage.MainActivity;
import com.thallo.stage.R;
import com.thallo.stage.database.history.History;
import com.thallo.stage.database.history.HistoryViewModel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterHold> {
    List<History> allHistory = new ArrayList<>();
    TextView title,url;
    ImageView icon;
    HistoryViewModel historyViewModel;
    View close;


    @NonNull
    @Override
    public MyAdapterHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.history_item,parent,false);
        historyViewModel=new ViewModelProvider((FragmentActivity)parent.getContext()).get(HistoryViewModel.class);


        return new MyAdapterHold(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterHold holder, int position) {
        History history=allHistory.get(position);
        title.setText(history.getTitle());
        url.setText(history.getUrl());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyViewModel.deleteWords(history);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(), MainActivity.class);
                MainActivity.url=history.getUrl();
                holder.itemView.getContext().startActivity(intent);
                ((FragmentActivity) view.getContext()).finish();
            }
        });
        URI uri=URI.create(history.getUrl());
        String faviconUrl=uri.getScheme()+"://"+uri.getHost()+"/favicon.ico";
        Glide.with(holder.itemView.getContext()).load(faviconUrl).error(R.drawable.ic_internet)
                .into(icon);

    }

    @Override
    public int getItemCount() {
        return allHistory.size();
    }

    public void setAllHistory(List<History> allHistory) {
        this.allHistory = allHistory;
    }

    class MyAdapterHold extends RecyclerView.ViewHolder{
        public MyAdapterHold(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.historyTitle);
            url=itemView.findViewById(R.id.historyUrl);
            icon=itemView.findViewById(R.id.historyIcon);
            close=itemView.findViewById(R.id.historyDelete);

        }
    }
}
