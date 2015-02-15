package com.example.suraj.androidgoogleimagesearch;

import java.io.Serializable;

/**
 * Created by sbharech on 2/10/2015.
 */
public class Filter implements Serializable {
    private String imageSize = null;
    private String imageType = null;
    private String color = null;
    private String siteFilter = null;

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSiteFilter() {
        return siteFilter;
    }

    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }
}
