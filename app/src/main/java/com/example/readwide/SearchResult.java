package com.example.readwide;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("start")
    @Expose
    private Integer start;
    @SerializedName("numFound")
    @Expose
    private Integer numFound;
    @SerializedName("docs")
    @Expose
    private List<Book> books = null;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getNumFound() {
        return numFound;
    }

    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setDocs(List<Book> books) {
        this.books = books;
    }

}