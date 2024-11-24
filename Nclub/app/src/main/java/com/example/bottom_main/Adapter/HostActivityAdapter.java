package com.example.bottom_main.Adapter;

import com.example.bottom_main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HostActivityAdapter extends ArrayAdapter<String>{
    private Context context;
    private List<String> activityTitles;

    // 構造方法
    public HostActivityAdapter(Context context, List<String> activityTitles) {
        super(context, 0, activityTitles);  // 呼叫父類的構造方法
        this.context = context;
        this.activityTitles = activityTitles;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.host_activity, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.activityTitle);
        titleTextView.setText(activityTitles.get(position));

        return convertView;
    }
}
