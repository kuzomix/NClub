package com.example.bottom_main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {
    public static LocalDate selectedDate; // 选中的日期
    private static LocalDate date;

    // 格式化日期为 "dd MMMM yyyy" 格式的字符串
    public static String formattedDate(LocalDate date) {
        CalendarUtils.date = date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return date.format(formatter);
    }

    // 格式化时间为 "hh:mm:ss a" 格式的字符串（输入为字符串类型）
    public static String formattedTime(String time) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm"); // 假设输入为24小时制时间字符串
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a"); // 输出格式为12小时制
        LocalTime localTime = LocalTime.parse(time, inputFormatter); // 将字符串解析为 LocalTime
        return localTime.format(outputFormatter); // 格式化为目标格式
    }

    // 格式化时间为 "HH:mm" 格式的字符串
    public static String formattedShortTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    // 格式化日期为 "MMMM yyyy" 格式的字符串
    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    // 格式化日期为 "MMMM d" 格式的字符串
    public static String monthDayFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d");
        return date.format(formatter);
    }

    // 生成当前月份视图的日期列表，包括前后月份的部分日期
    public static ArrayList<LocalDate> daysInMonthArray() {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate prevMonth = selectedDate.minusMonths(1);
        LocalDate nextMonth = selectedDate.plusMonths(1);

        YearMonth prevYearMonth = YearMonth.from(prevMonth);
        int prevDaysInMonth = prevYearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        // 填充月视图的日期，包括前后月份的部分日期，以便月视图显示完整的6行
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek)
                daysInMonthArray.add(LocalDate.of(prevMonth.getYear(), prevMonth.getMonth(), prevDaysInMonth + i - dayOfWeek));
            else if (i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), i - dayOfWeek - daysInMonth));
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
        }
        return daysInMonthArray;
    }

    // 生成当前周视图的日期列表
    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        // 填充周视图的日期，从周日开始
        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    // 返回给定日期所在周的周日
    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        // 从当前日期向前推，找到最近的周日
        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null; // 理论上不会执行到这里
    }
}
