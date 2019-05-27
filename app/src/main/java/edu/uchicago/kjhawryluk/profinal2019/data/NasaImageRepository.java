package edu.uchicago.kjhawryluk.profinal2019.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.data.local.NasaImageDatabase;
import edu.uchicago.kjhawryluk.profinal2019.data.local.dao.ImageDetailsDao;
import edu.uchicago.kjhawryluk.profinal2019.data.local.dao.QueryDao;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageQuery;
import edu.uchicago.kjhawryluk.profinal2019.data.remote.ApiConstants;
import edu.uchicago.kjhawryluk.profinal2019.data.remote.NasaImageRestService;
import edu.uchicago.kjhawryluk.profinal2019.data.remote.model.Item;
import edu.uchicago.kjhawryluk.profinal2019.data.remote.model.NasaImageQueryResponse;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static edu.uchicago.kjhawryluk.profinal2019.data.remote.ApiConstants.MEDIA_TYPE;

public class NasaImageRepository {
    private static NasaImageRepository mInstance;
    private final ImageDetailsDao mImageDetailsDao;
    private final QueryDao mQueryDao;
    private NasaImageDatabase mNasaImageDatabase;
    private final NasaImageRestService mNasaImageRestService;
    public static final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<ImageDetails>> mQueriedImages;

    private NasaImageRepository(Application application) {
        this.mNasaImageDatabase = NasaImageDatabase.getDatabase(application);
        this.mImageDetailsDao = this.mNasaImageDatabase.mImageDetailsDao();
        this.mQueryDao = this.mNasaImageDatabase.mQueryDao();
        this.mNasaImageRestService = getNasaImageRestService();
        this.mQueriedImages = new MutableLiveData<>();
    }

    public static NasaImageRepository getInstance(Application application) {
        if (mInstance == null)
            mInstance = new NasaImageRepository(application);
        return mInstance;
    }

    private NasaImageRestService getNasaImageRestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.QUERY_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(NasaImageRestService.class);
    }

    /**
     * Initial image query on page 1.
     *
     * @param query
     */
    public void queryImages(final String query) {
        if (query == null || query.isEmpty())
            return;
        List<ImageDetails> prevPageImages = new ArrayList<>();
        int pageNum = 1;
        queryImages(query, pageNum, prevPageImages);
    }

    private void queryImages(final String query, final int pageNum, final List<ImageDetails> prevPageImages) {
        mNasaImageRestService.searchImages(query, pageNum, MEDIA_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NasaImageQueryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(NasaImageQueryResponse queryResponse) {
                        List<ImageDetails> imageDetailList = queryResponse.getAllImageDetails();
                        prevPageImages.addAll(imageDetailList);

                        // Update the image list even before all the pages have been processed.
                        if (mQueriedImages != null && prevPageImages != null)
                            mQueriedImages.setValue(prevPageImages);

                        int nextPageNum = queryResponse.getNextPageNum(pageNum);
                        if (nextPageNum > -1) {
                            queryImages(query, nextPageNum, prevPageImages);
                        } else {
                            // Update UI
                            new SaveQueryAsyncTask(mQueryDao).execute(query);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RESPONSE ERROR", e.getMessage());
                        // Update with partial results.
                        if (prevPageImages != null && prevPageImages.size() > 0) {
                            mQueriedImages.setValue(prevPageImages);
                            new SaveQueryAsyncTask(mQueryDao).execute(query);
                        }
                    }

                });
    }

    private static class SaveQueryAsyncTask extends AsyncTask<String, Void, Void> {

        private QueryDao mQueryDao;

        SaveQueryAsyncTask(QueryDao dao) {
            mQueryDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            long millis = System.currentTimeMillis();
            ImageQuery imageQuery = new ImageQuery(millis, params[0]);
            mQueryDao.saveQuery(imageQuery);
            return null;
        }
    }

    public LiveData<List<ImageDetails>> loadFavoriteImages() {
        return mImageDetailsDao.loadFavoriteImages();
    }

    public LiveData<List<ImageQuery>> loadQueryHistory() {
        return mQueryDao.loadQueryHistory();
    }

    public LiveData<ImageQuery> getMostRecentQuery() {
        return mQueryDao.getMostRecentQuery();
    }

    public MutableLiveData<List<ImageDetails>> getQueriedImages() {
        return mQueriedImages;
    }

    /**
     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     *
     * @return
     */
    public boolean isInternetAvailable(Application application) {
        ConnectivityManager cm =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
