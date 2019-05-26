
package edu.uchicago.kjhawryluk.profinal2019.data.remote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collection {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("href")
    @Expose
    private String href;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
