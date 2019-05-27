package edu.uchicago.kjhawryluk.profinal2019;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContainerFragment extends Fragment {

    ImageView spaceImage;

    public ContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_container, container, false);
        return root;
    }
/**
 * Do query.
 * Clear out non favorited old results from db.
 * Query id (timestamp?), and Favorite bool as custom fields to json
 * save results in db
 * load results as live data
 * controller takes nasa id and creates url:
 * https://images-assets.nasa.gov/image/{assetName}/{assetName}~medium.jpg"
 * to inject into glide and pull on the fly (as above).
 * Long click flips (look into animation) to show description.
 * Title is under the image.
 *
 *
 * Can set shared preferences to decide to dl as small, medium or large.
 * Shared preferences set how much old queries to save (save last x queries for easy retrieval)
 * Can share
 * Can view favorites
 *
 */


}
