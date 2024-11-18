// 路徑：com/example/bottom_main/Event.java
package com.example.bottom_main;

public class Event {
    private String id; // 活動的唯一 ID
    private String title;
    private String imageUrl;
    private String description;
    private String startDateTour;
    private String endDateTour;
    private String startTimeTour;
    private String endTimeTour;
    private String tourItemId;

    public Event() {
    }

    public Event(String id,String title, String imageUrl, String description) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // 獲取活動 ID 的方法
    public String getId() {
        return id;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
    public String getStartDateTour() {
        return startDateTour;
    }

    public void setStartDateTour(String startDateTour) {
        this.startDateTour = startDateTour;
    }

    public String getEndDateTour() {
        return endDateTour;
    }

    public void setEndDateTour(String endDateTour) {
        this.endDateTour = endDateTour;
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

    public String getTourItemId() {
        return tourItemId;
    }

    public void setTourItemId(String tourItemId) {
        this.tourItemId = tourItemId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
