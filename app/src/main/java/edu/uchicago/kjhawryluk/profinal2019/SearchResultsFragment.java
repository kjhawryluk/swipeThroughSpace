package edu.uchicago.kjhawryluk.profinal2019;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.ListPreloader.*;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.List;
import java.util.Stack;

import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;


/**
 * Fragment to show image query results.
 */
public class SearchResultsFragment extends Fragment {

    private NasaImageViewModel mNasaImageViewModel;
    private RecyclerView mSearchResultsRecyclerView;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_results, container, false);
        mNasaImageViewModel = ViewModelProviders.of(this).get(NasaImageViewModel.class);
        mSearchResultsRecyclerView = root.findViewById(R.id.searchResultsRecyclerView);

        PreloadSizeProvider sizeProvider =
                new ViewPreloadSizeProvider();
        final NasaImageAdaptor adaptor = new NasaImageAdaptor(container.getContext(), mNasaImageViewModel);
        RecyclerViewPreloader<ImageDetails> preloader =
                new RecyclerViewPreloader<>(
                        Glide.with(this), adaptor, sizeProvider, 10 /*maxPreload*/);

        mSearchResultsRecyclerView.addOnScrollListener(preloader);

        // Initialize adaptor

        // Bind Recycler to adaptor
        mSearchResultsRecyclerView.setAdapter(adaptor);
        mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        // Listen for updates
        mNasaImageViewModel.getQueriedImages().observe(this, new Observer<Stack<ImageDetails>>() {
            @Override
            public void onChanged(@Nullable Stack<ImageDetails> imageDetails) {
                adaptor.setImageDetails(imageDetails);
            }
        });

        return root;
    }

}
