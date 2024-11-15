package com.example.bottom_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatroomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> titles;

    public ChatroomAdapter(Context context, List<String> titles) {
        super(context, R.layout.list_item_chatroom, titles);
        this.context = context;
        this.titles = titles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_chatroom, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.chatListTitle); // 獲取正確的 TextView
        textView.setText(titles.get(position)); // 設置標題文本

        // 設置背景顏色
        int color = (position % 2 == 0) ?
                context.getResources().getColor(android.R.color.holo_blue_light) :
                context.getResources().getColor(android.R.color.holo_green_light);
        convertView.setBackgroundColor(color);

        return convertView;
    }

}
