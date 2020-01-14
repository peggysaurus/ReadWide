package com.example.readwide;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookResponse {

    @SerializedName("pagination")
    @Expose
    private String pagination;
    @SerializedName("identifiers")
    @Expose
    private Identifiers identifiers;
    @SerializedName("classifications")
    @Expose
    private Classifications classifications;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("lccn")
    @Expose
    private List<String> lccn = null;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("number_of_pages")
    @Expose
    private Integer numberOfPages;
    @SerializedName("created")
    @Expose
    private Created created;
    @SerializedName("lc_classifications")
    @Expose
    private List<String> lcClassifications = null;
    @SerializedName("publish_date")
    @Expose
    private String publishDate;
    @SerializedName("last_modified")
    @Expose
    private LastModified lastModified;
    @SerializedName("authors")
    @Expose
    private List<Author> authors = null;
    @SerializedName("latest_revision")
    @Expose
    private Integer latestRevision;
    @SerializedName("oclc_numbers")
    @Expose
    private List<String> oclcNumbers = null;
    @SerializedName("works")
    @Expose
    private List<Work> works = null;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("revision")
    @Expose
    private Integer revision;

    private List<Book> results;

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }

    public Identifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Identifiers identifiers) {
        this.identifiers = identifiers;
    }

    public Classifications getClassifications() {
        return classifications;
    }

    public void setClassifications(Classifications classifications) {
        this.classifications = classifications;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLccn() {
        return lccn;
    }

    public void setLccn(List<String> lccn) {
        this.lccn = lccn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Created getCreated() {
        return created;
    }

    public void setCreated(Created created) {
        this.created = created;
    }

    public List<String> getLcClassifications() {
        return lcClassifications;
    }

    public void setLcClassifications(List<String> lcClassifications) {
        this.lcClassifications = lcClassifications;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public LastModified getLastModified() {
        return lastModified;
    }

    public void setLastModified(LastModified lastModified) {
        this.lastModified = lastModified;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Integer getLatestRevision() {
        return latestRevision;
    }

    public void setLatestRevision(Integer latestRevision) {
        this.latestRevision = latestRevision;
    }

    public List<String> getOclcNumbers() {
        return oclcNumbers;
    }

    public void setOclcNumbers(List<String> oclcNumbers) {
        this.oclcNumbers = oclcNumbers;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public List<Book> getResults() {
        return results;
    }

    public void setResults(List<Book> results) {
        this.results = results;
    }

    private class Identifiers {
    }

    private class Classifications {
    }

    private class Created {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("value")
        @Expose
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private class Type {
        @SerializedName("key")
        @Expose
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    private class Work {

        @SerializedName("key")
        @Expose
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

    public class LastModified {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("value")
        @Expose
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}