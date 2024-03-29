package edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "QueryLog")
public class ImageQuery {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private long queryTimestamp;

    private String query;

    @NonNull
    public int getId() {
        return id;
    }

    public ImageQuery() {
    }

    public ImageQuery(long queryTimestamp, String query) {
        this.queryTimestamp = queryTimestamp;
        this.query = query;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public long getQueryTimestamp() {
        return queryTimestamp;
    }

    public void setQueryTimestamp(long queryTimestamp) {
        this.queryTimestamp = queryTimestamp;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
