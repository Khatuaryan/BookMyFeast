// PopCuisineModel.java
package com.example.bookmyfeast;

public class PopCuisineModel {
    private final int imageResourceId;
    private final String name;

    public PopCuisineModel(int imageResourceId, String name) {
        this.imageResourceId = imageResourceId;
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }
}