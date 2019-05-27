package edu.uchicago.kjhawryluk.profinal2019;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {

    private NasaImageViewModel mNasaImageViewModel;
    private RecyclerView mSearchResultsRecyclerView;

    public SearchResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_results, container, false);
        mNasaImageViewModel = ViewModelProviders.of(this).get(NasaImageViewModel.class);
        mSearchResultsRecyclerView = root.findViewById(R.id.searchResultsRecyclerView);

        // Initialize adaptor

        // Bind Recycler to adaptor


        return root;
    }

}
