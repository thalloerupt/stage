package com.thallo.stage.database.bookmark;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
