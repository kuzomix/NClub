package com.example.bottom_main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.bottom_main.R;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private String[] data;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView listName = convertView.findViewById(R.id.listName);
        TextView listTime = convertView.findViewById(R.id.listTime);

        listName.setText(data[position]);
        listTime.setText("12:00 PM");

        return convertView;
    }
}