package edu.uchicago.kjhawryluk.profinal2019.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.media.Image;

import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageQuery;

@Dao
public interface ImageDetailsDao {
    @Query("SELECT * FROM SwipedImages ORDER BY title")
    LiveData<List<ImageDetails>> loadFavoriteImages();

    @Query("SELECT nasaId FROM SwipedImages")
    LiveData<List<String>> loadSwipedImageIds();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveFavoriteImage(ImageDetails imageDetails);

    @Delete
    void deleteFavoriteImage(ImageDetails imageDetails);

}
