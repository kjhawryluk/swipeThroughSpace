
package edu.uchicago.kjhawryluk.swiptThroughSpace.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Stack;

import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageDetails;

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

    public Stack<ImageDetails> getAllImageDetails() {
        Stack<ImageDetails> imageDetailsList = new Stack<>();
        for (Item item : collection.getItems()) {
            List<ImageDetails> data = item.getData();
            if (data != null && data.size() > 0) {
                ImageDetails imageDetails = item.getData().get(0);
                imageDetails.setMetaUri(item.getHref());
                imageDetailsList.add(imageDetails);
            }
        }
        return imageDetailsList;
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
