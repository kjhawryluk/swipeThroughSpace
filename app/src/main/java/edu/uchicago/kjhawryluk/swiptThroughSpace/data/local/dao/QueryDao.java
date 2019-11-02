package edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageQuery;

@Dao
public interface QueryDao {
    @Query("SELECT * FROM QueryLog ORDER BY queryTimestamp desc limit 10")
    LiveData<List<ImageQuery>> loadQueryHistory();

    @Query("SELECT * FROM QueryLog ORDER BY queryTimestamp desc limit 1")
    LiveData<ImageQuery> getMostRecentQuery();

    @Insert
    Long saveQuery(ImageQuery imageQuery);

    @Delete
    void deleteQuery(ImageQuery imageQuery);

}
