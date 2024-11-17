package com.example.bottom_main.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;          // 標題
    private String address;        // 地址
    private String description;    // 描述
    private String pic;            // 圖片 URL
    private int bed;               // 床位數
    private String category;       // 類別
    private long changeDate;       // 修改日期
    private long createDate;       // 創建日期
    private boolean popularFlag;    // 熱門標誌
    private boolean recommendFlag;  // 推薦標誌
    private ArrayList<String> tags; // 標籤
    private int price;             // 價格
    private double score;          // 評分

    // Getter 和 Setter 方法
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(long changeDate) {
        this.changeDate = changeDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public boolean isPopularFlag() {
        return popularFlag;
    }

    public void setPopularFlag(boolean popularFlag) {
        this.popularFlag = popularFlag;
    }

    public boolean isRecommendFlag() {
        return recommendFlag;
    }

    public void setRecommendFlag(boolean recommendFlag) {
        this.recommendFlag = recommendFlag;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getPrice() {
        return price; // Return the price
    }

    public void setPrice(int price) {
        this.price = price; // Set the price
    }

    public double getScore() {
        return score; // Return the score
    }

    public void setScore(double score) {
        this.score = score; // Set the score
    }

    public ItemDomain() {
        // 預設建構子
    }
}
