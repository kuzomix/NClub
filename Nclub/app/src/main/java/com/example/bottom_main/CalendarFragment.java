package com.example.bottom_main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bottom_main.CalendarUtils.daysInMonthArray;
import static com.example.bottom_main.CalendarUtils.monthYearFromDate;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Map<LocalDate, List<Event>> eventsMap = new HashMap<>(); // 儲存日期對應的事件

    public void loadChatrooms() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://nclub-a408e-default-rtdb.firebaseio.com/");
        DatabaseReference eventsRef = database.getReference("chatroom");
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsMap.clear(); // 清空事件 Map
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        try {
                            // 解析日期，將事件加入對應日期
                            LocalDate eventDate = LocalDate.parse(event.getTitle()); // 假設 Firebase 存的是日期字串
                            eventsMap.computeIfAbsent(eventDate, k -> new ArrayList<>()).add(event);
                        } catch (Exception e) {
                            Log.e("Firebase", "Error parsing date: " + e.getMessage());
                        }
                    }
                }
                setMonthView(); // 更新行事曆顯示
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cal_fragment_calendar, container, false);

        initWidgets(view);

        CalendarUtils.selectedDate = LocalDate.now();
//        fetchEventsFromFirebase(); // 從 Firebase 抓取事件
        loadChatrooms(); // �� Firebase ��取事件
        setMonthView();

        return view;
    }

    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);

        view.findViewById(R.id.previousMonthButton).setOnClickListener(v -> previousMonthAction());
        view.findViewById(R.id.nextMonthButton).setOnClickListener(v -> nextMonthAction());
        view.findViewById(R.id.weeklyButton).setOnClickListener(v -> weeklyAction());
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        // 將事件資訊傳遞給 CalendarAdapter
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    public void weeklyAction() {
        Intent intent = new Intent(requireContext(), CalendarWeekViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabVisibility(View.GONE);
        }
    }


}