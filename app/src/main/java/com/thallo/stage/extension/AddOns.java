package com.thallo.stage.extension;

import android.graphics.Bitmap;
import android.media.Image;

public class AddOns {
    private String name;
    private Bitmap imageId;

    public AddOns(String name, Bitmap imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImageId() {
        return imageId;
    }
}
