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
import android.widget.TextView;

import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageListAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;


public class SwipedImagesListFragment extends Fragment {
    private static final String SWIPED_IMAGE_STATUS = "showFavorites";
    private RecyclerView mSwipedImagesRecyclerView;
    private boolean mShowFavorites;
    private TextView mSwipedImagesListTitle;
    private NasaImageViewModel mNasaImageViewModel;



    public SwipedImagesListFragment() {
        // Required empty public constructor
    }

     public static SwipedImagesListFragment newInstance(boolean showFavorites) {
        SwipedImagesListFragment fragment = new SwipedImagesListFragment();
        Bundle args = new Bundle();
        args.putBoolean(SWIPED_IMAGE_STATUS, showFavorites);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShowFavorites = getArguments().getBoolean(SWIPED_IMAGE_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_swiped_images_list, container, false);

        mSwipedImagesRecyclerView = root.findViewById(R.id.swipedImagesList);
        mNasaImageViewModel = ViewModelProviders.of(this).get(NasaImageViewModel.class);
        addListTitle(root);

        final NasaImageListAdaptor listAdaptor = new NasaImageListAdaptor(container.getContext(), mNasaImageViewModel);
        mSwipedImagesRecyclerView.setAdapter(listAdaptor);
        mSwipedImagesRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mNasaImageViewModel.getSwipedImages(mShowFavorites).observe(this, new Observer<List<ImageDetails>>() {
            @Override
            public void onChanged(@Nullable List<ImageDetails> imageDetails) {
               listAdaptor.setImageDetails(imageDetails);
            }
        });
        return root;
    }

    private void addListTitle(View root) {
        mSwipedImagesListTitle = root.findViewById(R.id.swipedImagesListTitle);
        if(mShowFavorites){
            mSwipedImagesListTitle.setText("Favorites");
        } else {
            mSwipedImagesListTitle.setText("Disliked Pictures");
        }
    }

}
