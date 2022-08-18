package com.thallo.stage.database.download;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Download {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "id_info")
    private long ids;
    public Download(long ids) {
        this.ids=ids;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getIds() {
        return ids;
    }


}
