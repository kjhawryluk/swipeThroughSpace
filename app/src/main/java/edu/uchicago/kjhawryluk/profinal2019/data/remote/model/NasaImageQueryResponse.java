
package edu.uchicago.kjhawryluk.profinal2019.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NasaImageQueryResponse {

    @SerializedName("collection")
    @Expose
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

}
