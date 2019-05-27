package edu.uchicago.kjhawryluk.profinal2019.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.data.NasaImageRepository;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageQuery;
import io.reactivex.annotations.NonNull;

public class NasaImageViewModel extends AndroidViewModel {
    private NasaImageRepository mNasaImageRepository;
    private LiveData<List<ImageDetails>> mFavoriteImages;
    private LiveData<ImageQuery> mMostRecentQuery;
    private LiveData<List<ImageQuery>> mQueryHistory;
    private MutableLiveData<List<ImageDetails>> mQueriedImages;

    public NasaImageViewModel(@NonNull Application application){
        super(application);
        mNasaImageRepository = NasaImageRepository.getInstance(application);
        mMostRecentQuery = mNasaImageRepository.getMostRecentQuery();
        mQueryHistory = mNasaImageRepository.loadQueryHistory();
        mFavoriteImages = mNasaImageRepository.loadFavoriteImages();
        mQueriedImages = mNasaImageRepository.getQueriedImages();
    }

    public void queryNasaImages(String query){
        mNasaImageRepository.queryImages(query);
    }

    public LiveData<List<ImageDetails>> getFavoriteImages() {
        return mFavoriteImages;
    }

    public LiveData<ImageQuery> getMostRecentQuery() {
        return mMostRecentQuery;
    }

    public LiveData<List<ImageQuery>> getQueryHistory() {
        return mQueryHistory;
    }

    public MutableLiveData<List<ImageDetails>> getQueriedImages() {
        return mQueriedImages;
    }
}
