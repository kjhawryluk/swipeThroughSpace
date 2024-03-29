package edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageDetails;

@Dao
public interface ImageDetailsDao {
    @Query("SELECT * FROM SwipedImages WHERE isFavorite = 1 ORDER BY title")
    LiveData<List<ImageDetails>> loadFavoriteImages();

    @Query("SELECT * FROM SwipedImages WHERE isFavorite = 0 ORDER BY title")
    LiveData<List<ImageDetails>> loadDislikedImages();

    @Query("SELECT * FROM SwipedImages")
    List<ImageDetails> loadAllSwipedImages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveFavoriteImage(ImageDetails imageDetails);


}
