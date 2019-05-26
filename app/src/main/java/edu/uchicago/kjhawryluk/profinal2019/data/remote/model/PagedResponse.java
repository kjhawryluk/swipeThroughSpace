package edu.uchicago.kjhawryluk.profinal2019.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class PagedResponse {
    public String BASE_PATH;

    @SerializedName("next")
    @Expose
    protected String next;

    public int getNextPageNum() {
        if (next == null)
            return -1;
        String pageNumString = next.replace(BASE_PATH, "");
        try {
            return Integer.parseInt(pageNumString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
