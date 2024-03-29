
package edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.uchicago.kjhawryluk.swiptThroughSpace.data.typeconverters.ListConverter;
import edu.uchicago.kjhawryluk.swiptThroughSpace.data.remote.ApiConstants;

@Entity(tableName = "SwipedImages")
public class ImageDetails {

    @SerializedName("center")
    @Expose
    private String center;
    @SerializedName("description")
    @Expose
    private String description;
    @PrimaryKey
    @NonNull
    @SerializedName("nasa_id")
    @Expose
    private String nasaId;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("description_508")
    @Expose
    private String description508;
    @SerializedName("secondary_creator")
    @Expose
    private String secondaryCreator;
    @TypeConverters({ListConverter.class})
    @SerializedName("keywords")
    @Expose
    private List<String> keywords = null;

    @TypeConverters({ListConverter.class})
    @SerializedName("imageUris")
    @Expose
    private List<String> imageUris;

    private String metaUri;

    private boolean isFavorite;

    @Ignore
    private Uri uriToShow;

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNasaId() {
        return nasaId;
    }

    public void setNasaId(String nasaId) {
        this.nasaId = nasaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription508() {
        return description508;
    }

    public void setDescription508(String description508) {
        this.description508 = description508;
    }

    public String getSecondaryCreator() {
        return secondaryCreator;
    }

    public void setSecondaryCreator(String secondaryCreator) {
        this.secondaryCreator = secondaryCreator;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Uri getUri(){
        return getUri(ApiConstants.DEFAULT_SIZE);
    }

    public Uri getUri(String size){
        String uriString = String.format(ApiConstants.IMAGE_BASE_URI, nasaId, nasaId, size);
        return Uri.parse(uriString);
    }

    public void setImageUriToShow(){
        String[] imageSizeOptionsInOrder = {"small", "medium", "large", "orig"};
        if(imageUris != null)
            for(String imageSize : imageSizeOptionsInOrder){
                for(String uri : imageUris){
                    if(uri.contains(imageSize))
                    {
                        uriToShow = Uri.parse(uri);
                        return;
                    }
                }
            }
    }

    public List<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<String> imageUris) {
        this.imageUris = imageUris;
    }

    public Uri getUriToShow() {
        return uriToShow;
    }

    public void setUriToShow(Uri uriToShow) {
        this.uriToShow = uriToShow;
    }

    public String getMetaUri() {
        return metaUri;
    }

    public void setMetaUri(String metaUri) {
        this.metaUri = metaUri;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                return obj.equals(nasaId);
            } else if (obj instanceof ImageDetails) {
                ImageDetails objEntity = (ImageDetails) obj;
                return objEntity.getNasaId().equals(nasaId);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nasaId.hashCode();
    }
}
