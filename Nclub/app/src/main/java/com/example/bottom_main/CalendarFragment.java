package com.example.bottom_main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.bottom_main.CalendarUtils.daysInMonthArray;
import static com.example.bottom_main.CalendarUtils.monthYearFromDate;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 載入 Fragment 的佈局
        View view = inflater.inflate(R.layout.cal_fragment_calendar, container, false);

        // 初始化元件並設定按鈕事件
        initWidgets(view);

        // 設定選擇的日期為當前日期
        CalendarUtils.selectedDate = LocalDate.now();

        // 顯示當月的日期
        setMonthView();

        return view;
    }

    // 初始化元件
    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);

        // 綁定按鈕的點擊事件
        view.findViewById(R.id.previousMonthButton).setOnClickListener(v -> previousMonthAction());
        view.findViewById(R.id.nextMonthButton).setOnClickListener(v -> nextMonthAction());
        view.findViewById(R.id.weeklyButton).setOnClickListener(v -> weeklyAction());

    }


    // 設定當月的日期視圖
    private void setMonthView() {
        // 顯示月份和年份
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        // 獲取當月的所有日期
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        // 設定 RecyclerView 的佈局和適配器
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    // 上個月按鈕動作
    public void previousMonthAction() {
        // 選擇的日期減去一個月
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    // 下個月按鈕動作
    public void nextMonthAction() {
        // 選擇的日期加上一個月
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    // 點擊週視圖按鈕動作
    public void weeklyAction() {
        // 啟動 CalendarWeekViewActivity
        Intent intent = new Intent(requireContext(), CalendarWeekViewActivity.class);
        startActivity(intent);
    }

    // 日期項目點擊事件
    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            // 更新選擇的日期並顯示
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabVisibility(View.GONE); // 隱藏 FAB
        }
    }
}
