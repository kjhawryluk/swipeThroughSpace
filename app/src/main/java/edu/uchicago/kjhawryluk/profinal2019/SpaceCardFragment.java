package edu.uchicago.kjhawryluk.profinal2019;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import edu.uchicago.kjhawryluk.profinal2019.data.NasaImageRepository;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;


public class SpaceCardFragment extends Fragment {

    ImageView mSpaceCardImage;
    TextView mSpaceCardTitle;
    TextView mSpaceCardDesc;
    private NasaImageViewModel mNasaImageViewModel;
    ImageDetails mImageDetails;

    public SpaceCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_space_card, container, false);
        mSpaceCardImage = root.findViewById(R.id.spaceCardImage);
        mSpaceCardTitle = root.findViewById(R.id.spaceCardTitle);
        mSpaceCardDesc = root.findViewById(R.id.spaceCardDesc);
        mSpaceCardImage.setOnTouchListener(new OnSwipeTouchListener(inflater.getContext()));
        mNasaImageViewModel = ViewModelProviders.of(this).get(NasaImageViewModel.class);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
       // setNextImage();
        if(mImageDetails != null)
            setCardValues();
    }

    private void setCardValues() {
        mSpaceCardTitle.setText(mImageDetails.getTitle());
        mSpaceCardDesc.setText(mImageDetails.getDescription());
        Glide.with(this.getContext())
                .load(mImageDetails.getUri())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(500, 500)
                .placeholder(R.drawable.ic_alien_head)
                .error(R.drawable.ic_sad_green_alien_whatface)
                .dontAnimate()
                .into(mSpaceCardImage);
    }

//    public void setNextImage(){
//        if(mNasaImageViewModel != null){
//            mImageDetails = mNasaImageViewModel.popImage();
//        }
//    }
}
