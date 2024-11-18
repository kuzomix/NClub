package com.example.bottom_main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendarEvent {
    private String title;
    private String startTimeTour;
    private String endTimeTour;


    // 用於行事曆的構造函數
    public CalendarEvent(String title, String startTimeTour, String endTimeTour) {
        this.title = title;
        this.startTimeTour = startTimeTour;
        this.endTimeTour = endTimeTour;
    }

    // Getter 和 Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTimeTour() {
        return startTimeTour;
    }

    public void setStartTimeTour(String startTimeTour) {
        this.startTimeTour = startTimeTour;
    }

    public String getEndTimeTour() {
        return endTimeTour;
    }

    public void setEndTimeTour(String endTimeTour) {
        this.endTimeTour = endTimeTour;
    }

    // 靜態方法：過濾指定日期的事件
    public static List<CalendarEvent> eventsForDate(LocalDate date, Map<LocalDate, List<Event>> eventsMap) {
        List<CalendarEvent> events = new ArrayList<>();
        List<Event> eventList = eventsMap.getOrDefault(date, new ArrayList<>());

        for (Event e : eventList) {
            // 確保顯示的 title 是正確的
            String displayTitle = e.getTitle() != null ? e.getTitle() : "未命名活動";
            events.add(new CalendarEvent(
                    "活動: " + displayTitle,
                    e.getStartTimeTour(),
                    e.getEndTimeTour()
            ));
        }

        return events;
    }

}

