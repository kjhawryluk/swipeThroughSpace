package edu.uchicago.kjhawryluk.profinal2019.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class NasaImageListAdaptor extends RecyclerView.Adapter<NasaImageListAdaptor.NasaImageViewHolder> {
    private final LayoutInflater mInflater;
    private final NasaImageViewModel mNasaImageViewModel;
    private List<ImageDetails> mImageDetails;


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mSpaceImage;
        private final TextView mSpaceTitle;
        public NasaImageViewHolder(View itemView) {
            super(itemView);

            mSpaceImage = itemView.findViewById(R.id.spaceImageThumb);
            mSpaceTitle = itemView.findViewById(R.id.swipedImagesList);
          }
    }


    public NasaImageListAdaptor(Context context, NasaImageViewModel nasaImageViewModel) {
        mInflater = LayoutInflater.from(context);
        mNasaImageViewModel = nasaImageViewModel;
    }

    @Override
    public NasaImageListAdaptor.NasaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.space_image_list_item, parent, false);
        return new NasaImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NasaImageListAdaptor.NasaImageViewHolder holder, int position) {

        if(mImageDetails != null){
            final ImageDetails current = mImageDetails.get(position);
            holder.mSpaceTitle.setText(current.getTitle());
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
            return mImageDetails.size();
        return 0;
    }

    public List<ImageDetails> getImageDetails() {
        return mImageDetails;
    }

    public void setImageDetails(List<ImageDetails> imageDetails) {
        mImageDetails = imageDetails;
        notifyDataSetChanged();
    }

}
