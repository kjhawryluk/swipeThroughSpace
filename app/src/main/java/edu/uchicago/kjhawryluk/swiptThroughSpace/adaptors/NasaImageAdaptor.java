package edu.uchicago.kjhawryluk.swiptThroughSpace.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import edu.uchicago.kjhawryluk.swipeThroughSpace.R;
import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.swiptThroughSpace.viewmodels.NasaImageViewModel;

public class NasaImageAdaptor extends RecyclerView.Adapter<NasaImageAdaptor.NasaImageViewHolder> {
    private final LayoutInflater mInflater;
    private final NasaImageViewModel mNasaImageViewModel;
    private ImageDetails mImageDetails;


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mSpaceImage;
        public NasaImageViewHolder(View itemView) {
            super(itemView);

            mSpaceImage = itemView.findViewById(R.id.spaceCardImage);
          }
    }


    public NasaImageAdaptor(Context context, NasaImageViewModel nasaImageViewModel) {
        mInflater = LayoutInflater.from(context);
        mNasaImageViewModel = nasaImageViewModel;
    }

    @Override
    public NasaImageAdaptor.NasaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_space_card_image, parent, false);
        return new NasaImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NasaImageAdaptor.NasaImageViewHolder holder, int position) {

        if(mImageDetails != null){
            final ImageDetails current = mImageDetails;
            Glide.with(mInflater.getContext())
                    .load(current.getUri())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .override(1000, 1000)
                    .error(R.drawable.ic_sad_green_alien_whatface)
                    .dontAnimate()
                    .into(holder.mSpaceImage);
        }
    }

    @Override
    public int getItemCount() {
        if(mImageDetails != null)
            return 1;
        return 0;
    }

    public ImageDetails getImageDetails() {
        return mImageDetails;
    }

    public void setImageDetails(ImageDetails imageDetails) {
        mImageDetails = imageDetails;
        notifyDataSetChanged();
    }

}
