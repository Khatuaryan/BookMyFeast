package com.example.bookmyfeast;

public class RecomRestModel {
    private final String name;
    private final String locality;
    private final int pictureResId;
    private final String rating;
    private final String highlights;
    private final String price;
    private final String address;
    private final String rating_text;
    private final Double lat;
    private final Double lon;
    private final String locVerb;


    public RecomRestModel(String name, String locality, int pictureResId, String rating, String highlights, String price, String address, String rating_text, Double lat, Double lon, String locVerb) {
        this.name = name;
        this.locality = locality;
        this.pictureResId = pictureResId;
        this.rating = rating;
        this.highlights = highlights;
        this.price = price;
        this.address = address;
        this.rating_text = rating_text;
        this.lat = lat;
        this.lon = lon;
        this.locVerb = locVerb;
    }

    public String getName() {
        return name;
    }

    public String getLocality() {
        return locality;
    }

    public int getPictureResId() {
        return pictureResId;
    }

    public String getRating() {
        return rating;
    }

    public String getHighlights() {
        return highlights;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }
    public String getRatingText() {
        return rating_text;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getLocVerb() {
        return locVerb;
    }
}