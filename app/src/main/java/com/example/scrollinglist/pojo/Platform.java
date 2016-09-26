
package com.example.scrollinglist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Platform {

    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("downloadLink")
    @Expose
    private String downloadLink;
    @SerializedName("contentLink")
    @Expose
    private String contentLink;
    @SerializedName("authRequired")
    @Expose
    private String authRequired;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;

    /**
     * 
     * @return
     *     The platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 
     * @param platform
     *     The platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * 
     * @return
     *     The downloadLink
     */
    public String getDownloadLink() {
        return downloadLink;
    }

    /**
     * 
     * @param downloadLink
     *     The downloadLink
     */
    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    /**
     * 
     * @return
     *     The contentLink
     */
    public String getContentLink() {
        return contentLink;
    }

    /**
     * 
     * @param contentLink
     *     The contentLink
     */
    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    /**
     * 
     * @return
     *     The authRequired
     */
    public String getAuthRequired() {
        return authRequired;
    }

    /**
     * 
     * @param authRequired
     *     The authRequired
     */
    public void setAuthRequired(String authRequired) {
        this.authRequired = authRequired;
    }

    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
