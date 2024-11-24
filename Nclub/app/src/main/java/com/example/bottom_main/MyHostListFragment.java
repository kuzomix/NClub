package com.example.bottom_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom_main.Adapter.MyHostListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyHostListFragment extends Fragment {

    private String activityTitle;
    private RecyclerView recyclerView;
    private MyHostListAdapter adapter;
    private List<String> registeredUsers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_hostlist, container, false);

        // 接收活動標題
        if (getArguments() != null) {
            activityTitle = getArguments().getString("activityTitle");
        }

        // 假設報名的用戶列表
        registeredUsers = Arrays.asList("User 1", "User 2", "User 3");

        // 初始化 RecyclerView
        recyclerView = rootView.findViewById(R.id.hostRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 設置適配器
        adapter = new MyHostListAdapter(getContext(), registeredUsers, new MyHostListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 審核報名用戶
                String selectedUser = registeredUsers.get(position);
                reviewUser(selectedUser);
            }
        });

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void reviewUser(String user) {
        // 處理審核邏輯，例如顯示批准或拒絕的選項
        Toast.makeText(getContext(), "審核 " + user, Toast.LENGTH_SHORT).show();
    }
}
