package com.example.music2.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class mydata {
    @JsonProperty("data")
    private List<APITrack> data;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("next")
    private String next;

    // Explicit getters
    public List<APITrack> getData() {
        return data;
    }

    public Integer getTotal() {
        return total;
    }

    public String getNext() {
        return next;
    }

    // Explicit setters
    public void setData(List<APITrack> data) {
        this.data = data;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setNext(String next) {
        this.next = next;
    }
}