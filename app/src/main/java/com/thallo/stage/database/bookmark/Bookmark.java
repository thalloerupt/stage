package com.thallo.stage.database.bookmark;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "url_info")
    private String url;
    @ColumnInfo(name = "title_info")
    private String title;
    @ColumnInfo(name = "file_name")
    private String file;
    @ColumnInfo(name = "show_info")
    private Boolean show;

    public Bookmark(String url, String title,String file,Boolean show) {
        this.url=url;
        this.title=title;
        this.file=file;
        this.show=show;

    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
