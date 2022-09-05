package com.thallo.stage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SearchItem extends ArrayAdapter<SearchChoice> {
    public SearchItem(@NonNull Context context, int resource, @NonNull List<SearchChoice> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.se_select,parent,false);
        ImageView icon=view.findViewById(R.id.imageView16);
        TextView name=view.findViewById(R.id.textView10);
        View view1=view.findViewById(R.id.view);
        SearchChoice searchChoice=getItem(position);
        icon.setImageResource(searchChoice.getImageID());
        name.setText(searchChoice.getName());
        return view;
    }

}
