// 路徑：com/example/bottom_main/Event.java
package com.example.bottom_main;

public class Event {
    private String id; // 活動的唯一 ID
    private String title;
    private String imageUrl;
    private String description;
    private  String startTimeTour;

    private  String endTimeTour;
    private String userId;

    private String members;

    public Event(String id,String title, String imageUrl, String description, String userId) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.userId = userId;
    }
    public Event() {
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
    @SuppressWarnings("unused")

    public String getDescription() {
        return description;
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
    @SuppressWarnings("unused")

    public void setEndTimeTour(String endTimeTour) {
        this.endTimeTour = endTimeTour;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @SuppressWarnings("unused")

    public CharSequence getStartDateTour() {
        return null;
    }

    public boolean getUserId() {
        return false;
    }
    @SuppressWarnings("unused")

    public void setStartDateTour(String startDateTour) {
    }

}
