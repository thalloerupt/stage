package com.thallo.stage;

public class SearchChoice {
    private int imageID;
    private String name;

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchChoice(int imageID, String name) {
        this.imageID = imageID;
        this.name = name;
    }
}
