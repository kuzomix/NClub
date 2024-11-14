package com.example.bottom_main;

public class Message {
    private String sender;
    private String senderId;
    private String text;
    private String timestamp;
    private String userId; // 新增 userId

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String sender, String senderId, String text, String userId) {
        this.sender = sender;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = String.valueOf(System.currentTimeMillis()); // 獲取當前時間戳
        this.userId = userId; // 設置 userId
    }

    public String getSender() {
        return sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp; // 獲取時間戳的方法
    }

    public String getUserId() {
        return userId; // 獲取 userId 的方法
    }
}


