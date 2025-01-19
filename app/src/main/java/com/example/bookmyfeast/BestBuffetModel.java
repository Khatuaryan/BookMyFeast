package com.example.bookmyfeast;

public class BestBuffetModel {
    private final int imageResourceId;
    private final String name;
    private final String loc;
    private final String price;

    public BestBuffetModel(int imageResourceId, String name, String loc, String price) {
        this.imageResourceId = imageResourceId;
        this.name = name;
        this.loc = loc;
        this.price = price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }

    public String getLoc(){
        return loc;
    }

    public String getPrice(){
        return price;
    }
}