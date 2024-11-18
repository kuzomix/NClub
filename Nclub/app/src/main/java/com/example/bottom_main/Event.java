// 路徑：com/example/bottom_main/Event.java
package com.example.bottom_main;

public class Event {
    private String id; // 活動的唯一 ID
    private String title;
    private String imageUrl;
    private String description;
    private String userId;

    public Event(String id,String title, String imageUrl, String description, String userId) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

}
