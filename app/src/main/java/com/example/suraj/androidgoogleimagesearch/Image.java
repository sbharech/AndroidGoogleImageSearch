package com.example.suraj.androidgoogleimagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by suraj on 08/02/15.
 */
public class Image implements Serializable {
    private int height;
    private int width;
    private String url;
    private String thumbnail;
    private String title;
    private String contextUrl;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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

    public static ArrayList<Image> populateImagesFromJSON(JSONArray imageJSONArray) {
        ArrayList<Image> results = new ArrayList<Image>();

        for (int i = 0; i < imageJSONArray.length(); ++i) {
            try {
                JSONObject imageObject = (JSONObject)imageJSONArray.get(i);
                Image image = new Image();
                image.setUrl(imageObject.getString("url"));
                image.setTitle(imageObject.getString("titleNoFormatting"));
                image.setHeight(imageObject.getInt("height"));
                image.setWidth(imageObject.getInt("width"));
                image.setThumbnail(imageObject.getString("tbUrl"));
                image.setContextUrl(imageObject.getString("originalContextUrl"));
                results.add(image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContextUrl() {
        return contextUrl;
    }

    public void setContextUrl(String contextUrl) {
        this.contextUrl = contextUrl;
    }
}
