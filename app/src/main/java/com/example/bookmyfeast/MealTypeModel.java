package com.example.bookmyfeast;

public class MealTypeModel {
    private final int imageResId;
    private final String label;

    public MealTypeModel(int imageResId, String label) {
        this.imageResId = imageResId;
        this.label = label;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getLabel() {
        return label;
    }
}