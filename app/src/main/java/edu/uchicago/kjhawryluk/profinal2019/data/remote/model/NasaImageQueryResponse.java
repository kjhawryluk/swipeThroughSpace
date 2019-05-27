
package edu.uchicago.kjhawryluk.profinal2019.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;

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

    public List<ImageDetails> getAllImageDetails() {
        ArrayList<ImageDetails> imageDetails = new ArrayList<>();
        for (Item item : collection.getItems()) {
            List<ImageDetails> data = item.getData();
            if (data != null && data.size() > 0) {
                imageDetails.add(item.getData().get(0));
            }
        }
        return imageDetails;
    }

    public int getNextPageNum(int currentPageNum) {
        if (collection != null && collection.getLinks() != null) {
            for (Link link : collection.getLinks()) {
                if (link.getRel().equals("next"))
                    return currentPageNum + 1;
            }
        }
        return -1;
    }
}
