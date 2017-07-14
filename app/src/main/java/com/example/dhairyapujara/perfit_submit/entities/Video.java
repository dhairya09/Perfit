package com.example.dhairyapujara.perfit_submit.entities;

/**
 * Created by Dhairya Pujara on 17-02-2017.
 */
import java.io.Serializable;

public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The title of the video. */
    public String title = "Untitled";




    /** The author of the video. */
    public String author = "Unknown";

    /** The description of the video. */
    public String description = "No Description";

    /** The video's streaming url. */
    public String url = "Untitled";

    /**
     * Create a video object with a streaming url.
     * @param url A url to stream from.
     */
    public Video(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }


}
