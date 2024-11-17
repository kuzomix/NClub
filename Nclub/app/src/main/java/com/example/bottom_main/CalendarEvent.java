package com.example.bottom_main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CalendarEvent
{
    // 靜態事件列表，用於存儲所有事件
    public static ArrayList<CalendarEvent> eventsList = new ArrayList<>();

    // 根據日期查找事件
    public static ArrayList<CalendarEvent> eventsForDate(LocalDate date)
    {
        ArrayList<CalendarEvent> events = new ArrayList<>();

        // 遍歷所有事件，將日期匹配的事件添加到結果列表中
        for(CalendarEvent event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    // 根據日期和時間查找事件
    public static ArrayList<CalendarEvent> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<CalendarEvent> events = new ArrayList<>();

        // 遍歷所有事件，將日期和小時匹配的事件添加到結果列表中
        for(CalendarEvent event : eventsList)
        {
            int eventHour = event.time.getHour();
            int cellHour = time.getHour();
            if(event.getDate().equals(date) && eventHour == cellHour)
                events.add(event);
        }

        return events;
    }

    // 事件的名稱
    private String name;
    // 事件的日期
    private LocalDate date;
    // 事件的時間
    private LocalTime time;

    // 構造函數，初始化事件的名稱、日期和時間
    public CalendarEvent(String name, LocalDate date, LocalTime time)
    {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    // 獲取事件的名稱
    public String getName()
    {
        return name;
    }

    // 設置事件的名稱
    public void setName(String name)
    {
        this.name = name;
    }

    // 獲取事件的日期
    public LocalDate getDate()
    {
        return date;
    }

    // 設置事件的日期
    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    // 獲取事件的時間
    public LocalTime getTime()
    {
        return time;
    }

    // 設置事件的時間
    public void setTime(LocalTime time)
    {
        this.time = time;
    }
}
