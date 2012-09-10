package com.vonhof.ms.dto;

import java.util.Date;

public class DownloadDTO {
    private String filename;
    private int size;
    private Date created = new Date();
    private float percent = 0;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
