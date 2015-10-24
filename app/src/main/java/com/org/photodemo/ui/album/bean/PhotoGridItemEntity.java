package com.org.photodemo.ui.album.bean;

/**
 * Created by GZK on 2015/10/22.
 */
public class PhotoGridItemEntity {
    private String path;
    private String time;
    private String id;
    private boolean selected;
    private int orientation;

    public PhotoGridItemEntity(String id, String time, int orientation, String path) {
        this.id = id;
        this.time = time;
        this.orientation = orientation;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
