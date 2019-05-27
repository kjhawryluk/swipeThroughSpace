package edu.uchicago.kjhawryluk.profinal2019.data.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import edu.uchicago.kjhawryluk.profinal2019.data.local.dao.ImageDetailsDao;
import edu.uchicago.kjhawryluk.profinal2019.data.local.dao.QueryDao;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageQuery;
import edu.uchicago.kjhawryluk.profinal2019.data.typeconverters.ListConverter;

@Database(entities = {ImageDetails.class, ImageQuery.class},version = 1)
@TypeConverters({ListConverter.class})
public abstract class NasaImageDatabase extends RoomDatabase{
    public abstract ImageDetailsDao mImageDetailsDao();
    public abstract QueryDao mQueryDao();
    private static volatile NasaImageDatabase INSTANCE;

    public static NasaImageDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NasaImageDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NasaImageDatabase.class, "nasa_images_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static NasaImageDatabase.Callback sCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ImageDetailsDao mImageDetailsDao;
        private final  QueryDao mQueryDao;

        PopulateDbAsync(NasaImageDatabase db) {
            mImageDetailsDao = db.mImageDetailsDao();
            mQueryDao = db.mQueryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Load json into database to have some offline data.
            return null;
        }
    }

}
