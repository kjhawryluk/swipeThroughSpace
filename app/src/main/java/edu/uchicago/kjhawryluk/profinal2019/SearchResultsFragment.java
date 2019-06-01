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
import android.widget.Toast;

import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageTextAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;


/**
 * Fragment to show image query results.
 */
public class SearchResultsFragment extends Fragment {

    private NasaImageViewModel mNasaImageViewModel;
    private RecyclerView mSearchResultsRecyclerView;
    private RecyclerView mSpaceImageTextRecyclerView;

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
        mSearchResultsRecyclerView = root.findViewById(R.id.spaceImageRecyclerView);
        mSpaceImageTextRecyclerView = root.findViewById(R.id.spaceImageTextRecyclerView);

        final NasaImageAdaptor imageAdaptor = new NasaImageAdaptor(container.getContext(), mNasaImageViewModel);
        final NasaImageTextAdaptor textAdaptor = new NasaImageTextAdaptor(container.getContext());
        // Initialize adaptor

        // Bind Recycler to adaptor
        mSearchResultsRecyclerView.setAdapter(imageAdaptor);
        mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mSpaceImageTextRecyclerView.setAdapter(textAdaptor);
        mSpaceImageTextRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        // Listen for updates
        mNasaImageViewModel.getTopImageOfStack().observe(this, new Observer<ImageDetails>() {
            @Override
            public void onChanged(@Nullable ImageDetails imageDetails) {

                imageAdaptor.setImageDetails(imageDetails);
                textAdaptor.setImageDetails(imageDetails);
            }
        });
        mSearchResultsRecyclerView.setOnTouchListener(new OnSwipeTouchListener(container.getContext()) {
                @Override
                public void onSwipeRight() {
                    Toast.makeText(container.getContext(), "LIKE", Toast.LENGTH_SHORT);
                    mNasaImageViewModel.popImage();
                }

                @Override
                public void onSwipeLeft() {
                    Toast.makeText(container.getContext(), "DISLIKE", Toast.LENGTH_SHORT);
                    mNasaImageViewModel.popImage();
                }
            });
        return root;
    }

}
