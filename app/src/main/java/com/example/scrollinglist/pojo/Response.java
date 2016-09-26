
package com.example.scrollinglist.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes = new ArrayList<Episode>();
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("page_count")
    @Expose
    private Integer pageCount;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("nextUrl")
    @Expose
    private String nextUrl;

    /**
     * 
     * @return
     *     The episodes
     */
    public List<Episode> getEpisodes() {
        return episodes;
    }

    /**
     * 
     * @param episodes
     *     The episodes
     */
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    /**
     * 
     * @return
     *     The offset
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * 
     * @param offset
     *     The offset
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * 
     * @return
     *     The limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 
     * @param limit
     *     The limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * 
     * @return
     *     The pageCount
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * 
     * @param pageCount
     *     The page_count
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 
     * @return
     *     The totalCount
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 
     * @param totalCount
     *     The total_count
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 
     * @return
     *     The nextUrl
     */
    public String getNextUrl() {
        return nextUrl;
    }

    /**
     * 
     * @param nextUrl
     *     The nextUrl
     */
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

}
