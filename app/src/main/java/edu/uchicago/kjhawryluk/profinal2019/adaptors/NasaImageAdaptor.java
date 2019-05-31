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
import java.util.Stack;

import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class NasaImageAdaptor extends RecyclerView.Adapter<NasaImageAdaptor.NasaImageViewHolder> {
    private final LayoutInflater mInflater;
    private final NasaImageViewModel mNasaImageViewModel;
    private ImageDetails mImageDetails;


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mSpaceImage;
        private final TextView mSpaceCardTitle;
        private final TextView mSpaceCardDesc;
        public NasaImageViewHolder(View itemView) {
            super(itemView);
            mSpaceImage = itemView.findViewById(R.id.spaceCardImage);
            mSpaceCardTitle = itemView.findViewById(R.id.spaceCardTitle);
            mSpaceCardDesc = itemView.findViewById(R.id.spaceCardDesc);
        }
    }


    public NasaImageAdaptor(Context context, NasaImageViewModel nasaImageViewModel) {
        mInflater = LayoutInflater.from(context);
        mNasaImageViewModel = nasaImageViewModel;
    }

    @Override
    public NasaImageAdaptor.NasaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_space_card, parent, false);
        return new NasaImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NasaImageAdaptor.NasaImageViewHolder holder, int position) {

        if(mImageDetails != null){
            final ImageDetails current = mImageDetails;
            holder.mSpaceCardTitle.setText(current.getTitle());
            holder.mSpaceCardDesc.setText(current.getDescription());
            Glide.with(mInflater.getContext())
                    .load(current.getUri())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .override(1000, 1000)
                    .placeholder(R.drawable.ic_alien_head)
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
