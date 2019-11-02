
package edu.uchicago.kjhawryluk.swiptThroughSpace.data.remote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageDetails;

public class Item {

    @SerializedName("data")
    @Expose
    private List<ImageDetails> data = null;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;

    public List<ImageDetails> getData() {
        return data;
    }

    public void setData(List<ImageDetails> data) {
        this.data = data;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
