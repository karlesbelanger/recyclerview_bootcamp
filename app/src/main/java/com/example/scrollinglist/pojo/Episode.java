
package com.example.scrollinglist.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Episode {

    @SerializedName("mgid")
    @Expose
    private String mgid;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("episodeNumber")
    @Expose
    private String episodeNumber;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("ratingType")
    @Expose
    private String ratingType;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("originalAirDate")
    @Expose
    private String originalAirDate;
    @SerializedName("images")
    @Expose
    private List<Image> images = new ArrayList<Image>();
    @SerializedName("people")
    @Expose
    private List<Person> people = new ArrayList<Person>();
    @SerializedName("season")
    @Expose
    private Season season;
    @SerializedName("show")
    @Expose
    private Show show;
    @SerializedName("platforms")
    @Expose
    private List<Platform> platforms = new ArrayList<Platform>();
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("franchise")
    @Expose
    private Franchise franchise;

    /**
     * 
     * @return
     *     The mgid
     */
    public String getMgid() {
        return mgid;
    }

    /**
     * 
     * @param mgid
     *     The mgid
     */
    public void setMgid(String mgid) {
        this.mgid = mgid;
    }

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

    /**
     * 
     * @return
     *     The episodeNumber
     */
    public String getEpisodeNumber() {
        return episodeNumber;
    }

    /**
     * 
     * @param episodeNumber
     *     The episodeNumber
     */
    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * 
     * @param shortDescription
     *     The shortDescription
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * 
     * @return
     *     The ratingType
     */
    public String getRatingType() {
        return ratingType;
    }

    /**
     * 
     * @param ratingType
     *     The ratingType
     */
    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }

    /**
     * 
     * @return
     *     The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * 
     * @param genre
     *     The genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * 
     * @return
     *     The originalAirDate
     */
    public String getOriginalAirDate() {
        return originalAirDate;
    }

    /**
     * 
     * @param originalAirDate
     *     The originalAirDate
     */
    public void setOriginalAirDate(String originalAirDate) {
        this.originalAirDate = originalAirDate;
    }

    /**
     * 
     * @return
     *     The images
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * 
     * @param images
     *     The images
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * 
     * @return
     *     The people
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * 
     * @param people
     *     The people
     */
    public void setPeople(List<Person> people) {
        this.people = people;
    }

    /**
     * 
     * @return
     *     The season
     */
    public Season getSeason() {
        return season;
    }

    /**
     * 
     * @param season
     *     The season
     */
    public void setSeason(Season season) {
        this.season = season;
    }

    /**
     * 
     * @return
     *     The show
     */
    public Show getShow() {
        return show;
    }

    /**
     * 
     * @param show
     *     The show
     */
    public void setShow(Show show) {
        this.show = show;
    }

    /**
     * 
     * @return
     *     The platforms
     */
    public List<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * 
     * @param platforms
     *     The platforms
     */
    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    /**
     * 
     * @return
     *     The links
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * 
     * @param links
     *     The links
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * 
     * @return
     *     The brand
     */
    public Brand getBrand() {
        return brand;
    }

    /**
     * 
     * @param brand
     *     The brand
     */
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    /**
     * 
     * @return
     *     The franchise
     */
    public Franchise getFranchise() {
        return franchise;
    }

    /**
     * 
     * @param franchise
     *     The franchise
     */
    public void setFranchise(Franchise franchise) {
        this.franchise = franchise;
    }

}
