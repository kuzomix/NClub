package com.example.bottom_main;

import static com.example.bottom_main.CalendarUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarDailyActivity extends AppCompatActivity
{
    private TextView monthDayText; // 顯示當前日期的文字視圖
    private TextView dayOfWeekTV; // 顯示星期的文字視圖
    private ListView hourListView; // 用於顯示小時事件的列表視圖

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal_activity_daily);
        initWidgets(); // 初始化視圖組件
    }

    // 初始化視圖組件
    private void initWidgets()
    {
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setDayView(); // 設置每日視圖
    }

    // 設置每日視圖，更新顯示日期和星期
    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter(); // 設置小時適配器
    }

    // 設置小時適配器，將事件數據綁定到小時列表視圖
    private void setHourAdapter()
    {
        CalendarHourAdapter hourAdapter = new CalendarHourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    // 生成小時事件列表，每小時一個事件
    private ArrayList<CalendarHourEvent> hourEventList()
    {
        ArrayList<CalendarHourEvent> list = new ArrayList<>();

        for(int hour = 0; hour < 24; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<CalendarEvent> events = CalendarEvent.eventsForDateAndTime(selectedDate, time);
            CalendarHourEvent hourEvent = new CalendarHourEvent(time, events);
            list.add(hourEvent);
        }

        return list;
    }

    // 前一天按鈕的點擊事件處理
    public void previousDayAction(View view)
    {
        selectedDate = selectedDate.minusDays(1);
        setDayView(); // 更新每日視圖
    }

    // 後一天按鈕的點擊事件處理
    public void nextDayAction(View view)
    {
        selectedDate = selectedDate.plusDays(1);
        setDayView(); // 更新每日視圖
    }

//    // 新事件按鈕的點擊事件處理，打開新事件編輯界面
//    public void newEventAction(View view)
//    {
//        startActivity(new Intent(this, CalendarEventEditActivity.class));
//    }
}
