package edu.uchicago.kjhawryluk.profinal2019;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageTextAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.util.OnSwipeTouchListener;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;


/**
 * Fragment to show image query results.
 */
public class SearchResultsFragment extends Fragment {

    private NasaImageViewModel mNasaImageViewModel;
    private RecyclerView mSearchResultsRecyclerView;
    private RecyclerView mSpaceImageTextRecyclerView;
    private ImageView mFavoriteIcon;
    private LinearLayout mNoResults;
    private TextView mDislikeTextView;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_results, container, false);
        mNasaImageViewModel = ViewModelProviders.of(this).get(NasaImageViewModel.class);
        mSearchResultsRecyclerView = root.findViewById(R.id.spaceImageRecyclerView);
        mSpaceImageTextRecyclerView = root.findViewById(R.id.spaceImageTextRecyclerView);
        mFavoriteIcon = root.findViewById(R.id.like);
        mNoResults = root.findViewById(R.id.noResults);
        mDislikeTextView = root.findViewById(R.id.dislike);

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
                mFavoriteIcon.setVisibility(View.GONE);
                mDislikeTextView.setVisibility(View.GONE);
                if(imageDetails == null){
                    mNoResults.setVisibility(View.VISIBLE);
                    mSearchResultsRecyclerView.setVisibility(View.INVISIBLE);
                    mSpaceImageTextRecyclerView.setVisibility(View.INVISIBLE);
                }else{
                    mNoResults.setVisibility(View.GONE);
                    mSearchResultsRecyclerView.setVisibility(View.VISIBLE);
                    mSpaceImageTextRecyclerView.setVisibility(View.VISIBLE);
                    imageAdaptor.setImageDetails(imageDetails);
                    textAdaptor.setImageDetails(imageDetails);
                }
            }
        });
        mSearchResultsRecyclerView.setOnTouchListener(new OnSwipeTouchListener(container.getContext()) {
                @Override
                public void onSwipeRight() {
                    swipeAction(mFavoriteIcon, true);
                }

                @Override
                public void onSwipeLeft() {
                    swipeAction(mDislikeTextView, false);
                }
            });
        return root;
    }

    private void swipeAction(View view, final boolean isFavorite) {
        view.setVisibility(View.VISIBLE);
        new CountDownTimer(150, 1000) {
            public void onFinish() {
                mNasaImageViewModel.popImage(isFavorite);
            }
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }

}
