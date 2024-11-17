package com.example.bottom_main;

import java.time.LocalTime;
import java.util.ArrayList;

class CalendarHourEvent
{
    LocalTime time;
    ArrayList<CalendarEvent> events;

    public CalendarHourEvent(LocalTime time, ArrayList<CalendarEvent> events)
    {
        this.time = time;
        this.events = events;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public ArrayList<CalendarEvent> getEvents()
    {
        return events;
    }

    public void setEvents(ArrayList<CalendarEvent> events)
    {
        this.events = events;
    }
}
