package com.example.bottom_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.bottom_main.CalendarUtils.daysInWeekArray;
import static com.example.bottom_main.CalendarUtils.monthYearFromDate;

public class CalendarWeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Button previousWeekButton, nextWeekButton, newEventButton, dailyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal_avtivity_week_view);
        initWidgets();
        setWeekView();
        setButtonListeners(); // 設定按鈕的點擊事件
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);

        // 初始化按鈕
        previousWeekButton = findViewById(R.id.previousWeekButton);
        nextWeekButton = findViewById(R.id.nextWeekButton);
        //newEventButton = findViewById(R.id.newEventButton);
        dailyButton = findViewById(R.id.dailyButton);
    }

    private void setButtonListeners() {
        previousWeekButton.setOnClickListener(view -> previousWeekAction());
        nextWeekButton.setOnClickListener(view -> nextWeekAction());
//        newEventButton.setOnClickListener(view -> newEventAction());
        dailyButton.setOnClickListener(view -> dailyAction());
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    private void previousWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    private void nextWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<CalendarEvent> dailyEvents = CalendarEvent.eventsForDate(CalendarUtils.selectedDate);
        CalendarEventAdapter eventAdapter = new CalendarEventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

//    private void newEventAction() {
//        startActivity(new Intent(this, CalendarEventEditActivity.class));
//    }

    private void dailyAction() {
        startActivity(new Intent(this, CalendarDailyActivity.class));
    }
}
