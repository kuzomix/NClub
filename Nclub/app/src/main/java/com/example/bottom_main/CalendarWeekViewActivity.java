package com.example.bottom_main;

import static com.example.bottom_main.CalendarUtils.daysInWeekArray;
import static com.example.bottom_main.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarWeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Button previousWeekButton, nextWeekButton;
    private Map<LocalDate, List<Event>> eventsMap = new HashMap<>(); // 存儲每日期的事件
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); // 日期格式
    private ArrayList<String> chatroomTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal_avtivity_week_view);

        // 初始化 UI
        initWidgets();
        setWeekView();

        loadChatrooms();
        setButtonListeners();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        previousWeekButton = findViewById(R.id.previousWeekButton);
        nextWeekButton = findViewById(R.id.nextWeekButton);
    }

    private void setButtonListeners() {
        previousWeekButton.setOnClickListener(view -> previousWeekAction());
        nextWeekButton.setOnClickListener(view -> nextWeekAction());
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // 更新當前選中日期的事件
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
        List<CalendarEvent> dailyEvents = CalendarEvent.eventsForDate(CalendarUtils.selectedDate, eventsMap);
        CalendarEventAdapter eventAdapter = new CalendarEventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }
    public void loadChatrooms() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://nclub-a408e-default-rtdb.firebaseio.com/");
        DatabaseReference chatroomsRef = database.getReference("chatrooms");
        DatabaseReference itemsRef = database.getReference("Items");

        chatroomTitles = new ArrayList<>();
        Map<String, String> tourItemIdToTitleMap = new HashMap<>(); // 存儲 tourItemId -> title 的對應關係

        chatroomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsMap.clear(); // 清空事件 Map
                chatroomTitles.clear(); // 清空標題清單

                for (DataSnapshot chatroomSnapshot : snapshot.getChildren()) {
                    // 檢查 members 節點
                    DataSnapshot membersSnapshot = chatroomSnapshot.child("members");
                    boolean hasTrueUser = false;

                    for (DataSnapshot member : membersSnapshot.getChildren()) {
                        Boolean isTrue = member.getValue(Boolean.class);
                        if (Boolean.TRUE.equals(isTrue)) {
                            hasTrueUser = true;
                            break;
                        }
                    }

                    // 如果有至少一個 user 為 true，繼續抓取其他資料
                    if (hasTrueUser) {
                        String startDateTour = chatroomSnapshot.child("startDateTour").getValue(String.class);
                        String startTimeTour = chatroomSnapshot.child("startTimeTour").getValue(String.class);
                        String tourItemId = chatroomSnapshot.child("tourItemId").getValue(String.class);

                        if (startDateTour != null && startTimeTour != null && tourItemId != null) {
                            try {
                                LocalDate eventDate = LocalDate.parse(startDateTour, formatter);
                                Event event = new Event();
                                event.setStartDateTour(startDateTour);
                                event.setStartTimeTour(startTimeTour);

                                // 查詢 title 並更新事件
                                itemsRef.child(tourItemId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                                        String title = itemSnapshot.child("title").getValue(String.class);
                                        if (title != null) {
                                            event.setTitle(title);

                                            // 更新事件資料
                                            eventsMap.computeIfAbsent(eventDate, k -> new ArrayList<>()).add(event);
                                            chatroomTitles.add(title);
                                            setWeekView(); // 更新 UI
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Firebase", "Error loading title: " + error.getMessage());
                                    }
                                });
                            } catch (Exception e) {
                                Log.e("CalendarWeekView", "日期解析失敗: " + startDateTour, e);
                            }
                        } else {
                            Log.e("Firebase", "資料缺失: startDateTour 或 tourItemId 為 null");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }

}

