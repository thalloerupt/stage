package com.thallo.stage.database.history;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "url_info")
    private String url;
    @ColumnInfo(name = "title_info")
    private String title;
    @ColumnInfo(name = "time_info")
    private int time;
    @ColumnInfo(name = "mix")
    private String mix;
    public History(String url, String title,int time) {
        this.url=url;
        this.title=title;
        this.time=time;
        this.mix=url+title;

    }

    public String getMix() {
        return mix;
    }

    public void setMix(String mix) {
        this.mix = mix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
