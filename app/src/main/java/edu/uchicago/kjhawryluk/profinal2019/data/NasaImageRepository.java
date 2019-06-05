package edu.uchicago.kjhawryluk.profinal2019.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import java.util.List;
import java.util.Stack;
import edu.uchicago.kjhawryluk.profinal2019.data.local.NasaImageDatabase;
import edu.uchicago.kjhawryluk.profinal2019.data.local.dao.ImageDetailsDao;
import edu.uchicago.kjhawryluk.profinal2019.data.local.dao.QueryDao;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageQuery;
import edu.uchicago.kjhawryluk.profinal2019.data.remote.ApiConstants;
import edu.uchicago.kjhawryluk.profinal2019.data.remote.NasaImageRestService;
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
    public MutableLiveData<Stack<ImageDetails>> mQueriedImages;
    private MutableLiveData<ImageDetails> mTopImageOfStack;

    private NasaImageRepository(Application application) {
        this.mNasaImageDatabase = NasaImageDatabase.getDatabase(application);
        this.mImageDetailsDao = this.mNasaImageDatabase.mImageDetailsDao();
        this.mQueryDao = this.mNasaImageDatabase.mQueryDao();
        this.mNasaImageRestService = getNasaImageRestService();
        this.mQueriedImages = new MutableLiveData<>();
        this.mTopImageOfStack = new MutableLiveData<>();
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
        mQueriedImages.setValue(new Stack<ImageDetails>());
        Stack<ImageDetails> prevPageImages = new Stack<>();
        int pageNum = 1;
        queryImages(query, pageNum, prevPageImages);
    }

    /**
     * Calls api, gets results, filters out previously swiped images, and updates the mutable
     * live data to show the results.
     *
     * Note: Due to a bug in the emulator, when you type in a query using your computer's
     * keyboard, it will process the query twice. If you do it on your phone or with the
     * soft keyboard, you will not face this issue. This largely just affects the
     * query history.
     *
     * @param query
     * @param pageNum
     * @param prevPageImages
     */
    private void queryImages(final String query, final int pageNum, final Stack<ImageDetails> prevPageImages) {
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
                        // Send to background thread to deal with filtering responses.
                        new UpdateImagesFromResponseAsyncTask(
                                queryResponse,
                                prevPageImages,
                                pageNum,
                                query).execute();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RESPONSE ERROR", e.getMessage());
                    }

                });
    }

    /**
     * This takes a response and filters out the already swiped images.
     * If it's the first page, it updates the mutable data to show the results.
     * If it's not, it keeps grabbing the rest of the results and updates the stack
     * at the end.
     */
    private class UpdateImagesFromResponseAsyncTask extends AsyncTask<Void, Void, Stack<ImageDetails>> {

        private NasaImageQueryResponse mNasaImageQueryResponse;
        private Stack<ImageDetails> mPrevPageImages;
        private int mPageNum;
        private String mQuery;

        UpdateImagesFromResponseAsyncTask(
                                          NasaImageQueryResponse queryResponse,
                                          Stack<ImageDetails> prevPageImages,
                                          int pageNum, String query) {
            mNasaImageQueryResponse = queryResponse;
            mPrevPageImages = prevPageImages;
            mPageNum = pageNum;
            mQuery = query;
        }

        @Override
        protected Stack<ImageDetails> doInBackground(Void... voids) {
            return getImageDetailsFromResponse();
        }


        private Stack<ImageDetails> getImageDetailsFromResponse() {
            Stack<ImageDetails> imageDetailList = mNasaImageQueryResponse.getAllImageDetails();

            // Add only the images that haven't been swiped.
            mPrevPageImages.addAll(filterSwipedImages(imageDetailList));
            return imageDetailList;

        }

        @Override
        protected void onPostExecute(Stack<ImageDetails> imageDetails) {
            super.onPostExecute(imageDetails);

            int nextPageNum = mNasaImageQueryResponse.getNextPageNum(mPageNum);

            // Set the list and pop the first image upon the first results
            if (mPageNum == 1) {
                // Update UI
                mQueriedImages.setValue(mPrevPageImages);
                loadNextImage();
                mPrevPageImages.empty();
                new SaveQueryAsyncTask(mQueryDao).execute(mQuery);
            }

            // Exit if no images to return.
            if (mPrevPageImages.empty())
                return;

            // Get the rest of the images and set update the stack at the end
            // don't pop from it though because that already happened.
            if (nextPageNum > -1) {
                queryImages(mQuery, nextPageNum, mPrevPageImages);
            } else {
                // Update UI
                mPrevPageImages.addAll(mQueriedImages.getValue());
                mQueriedImages.setValue(mPrevPageImages);
            }
        }
    }

    /**
     * Filters a stack of images to not include previously swiped images.
     * This is called off the UI thread.
     *
     * @param pulledImages
     * @return
     */
    private Stack<ImageDetails> filterSwipedImages(Stack<ImageDetails> pulledImages) {
        List<ImageDetails> swipedImages = loadAllSwipedImages();

        //First query so nothing to filter.
        if (swipedImages == null)
            return pulledImages;

        Stack<ImageDetails> newImages = new Stack<>();
        for (ImageDetails pulledImage : pulledImages) {
            if (!swipedImages.contains(pulledImage)) {
                newImages.add(pulledImage);
            }
        }
        return newImages;
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

    private static class DeleteQueryAsyncTask extends AsyncTask<ImageQuery, Void, Void> {

        private QueryDao mQueryDao;

        DeleteQueryAsyncTask(QueryDao dao) {
            mQueryDao = dao;
        }

        @Override
        protected Void doInBackground(final ImageQuery... imageQueries) {
            mQueryDao.deleteQuery(imageQueries[0]);
            return null;
        }
    }

    private static class SaveFavoriteAsyncTask extends AsyncTask<ImageDetails, Void, Void> {

        private ImageDetailsDao mFavoriteDao;

        SaveFavoriteAsyncTask(ImageDetailsDao dao) {
            mFavoriteDao = dao;
        }

        @Override
        protected Void doInBackground(final ImageDetails... params) {
            ImageDetails imageDetails = params[0];
            mFavoriteDao.saveFavoriteImage(imageDetails);
            return null;
        }
    }

    public void deleteQuery(ImageQuery imageQuery) {
        new DeleteQueryAsyncTask(mQueryDao).execute(imageQuery);
    }

    public LiveData<List<ImageDetails>> loadFavoriteImages() {
        return mImageDetailsDao.loadFavoriteImages();
    }

    public LiveData<List<ImageDetails>> loadDislikedImages() {
        return mImageDetailsDao.loadDislikedImages();
    }

    public List<ImageDetails> loadAllSwipedImages() {
        return mImageDetailsDao.loadAllSwipedImages();
    }

    public LiveData<List<ImageQuery>> loadQueryHistory() {
        return mQueryDao.loadQueryHistory();
    }

    public LiveData<ImageQuery> getMostRecentQuery() {
        return mQueryDao.getMostRecentQuery();
    }

    public MutableLiveData<Stack<ImageDetails>> getQueriedImages() {
        return mQueriedImages;
    }

    public MutableLiveData<ImageDetails> getTopImageOfStack() {
        return mTopImageOfStack;
    }

    /**
     * Set top image from queried images.
     *
     * @return
     */
    public void popImage(boolean isFavorite) {
        saveImageDetails(isFavorite);
        loadNextImage();
    }

    private void saveImageDetails(boolean isFavorite) {
        ImageDetails imageToSave = mTopImageOfStack.getValue();
        saveImageDetails(isFavorite, imageToSave);
    }

    public void saveImageDetails(boolean isFavorite, ImageDetails imageToSave) {
        if (isFavorite) {
            imageToSave.setFavorite(true);
        } else {
            imageToSave.setFavorite(false);
        }
        new SaveFavoriteAsyncTask(mImageDetailsDao).execute(imageToSave);
    }

    private void loadNextImage() {
        Stack<ImageDetails> queriedImages = mQueriedImages.getValue();
        if (!queriedImages.empty()) {
            mTopImageOfStack.setValue(queriedImages.pop());
            mQueriedImages.setValue(queriedImages);
        } else {
            mTopImageOfStack.setValue(null);
            mQueriedImages.setValue(new Stack<>());
        }
    }

    /**
     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     * @return
     */
    public static boolean isInternetAvailable(Application application) {
        ConnectivityManager cm =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
