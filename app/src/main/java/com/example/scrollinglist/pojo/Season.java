
package com.example.scrollinglist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("seasonNumber")
    @Expose
    private String seasonNumber;
    @SerializedName("title")
    @Expose
    private String title;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The seasonNumber
     */
    public String getSeasonNumber() {
        return seasonNumber;
    }

    /**
     * 
     * @param seasonNumber
     *     The seasonNumber
     */
    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
