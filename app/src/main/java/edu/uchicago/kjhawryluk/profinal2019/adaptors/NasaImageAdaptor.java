package edu.uchicago.kjhawryluk.profinal2019.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class NasaImageAdaptor extends RecyclerView.Adapter<NasaImageAdaptor.NasaImageViewHolder> {

    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView spaceImage;

        public NasaImageViewHolder(View itemView) {
            super(itemView);
            spaceImage = itemView.findViewById(R.id.spaceImage);
        }
    }

    private final LayoutInflater mInflater;
    private final NasaImageViewModel mNasaImageViewModel;
    private List<ImageDetails> mImageDetails;

    public NasaImageAdaptor(Context context, NasaImageViewModel nasaImageViewModel) {
        mInflater = LayoutInflater.from(context);
        mNasaImageViewModel = nasaImageViewModel;
    }

    @Override
    public NasaImageAdaptor.NasaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.nasa_image_details, parent, false);
        return new NasaImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NasaImageAdaptor.NasaImageViewHolder holder, int position) {
        if(mImageDetails != null){
            final ImageDetails current = mImageDetails.get(position);
            Log.i("image uri",current.getUri().toString());
            Glide.with(mInflater.getContext())
                    .load(current.getUri())
                    .placeholder(R.drawable.ic_alien_head)
                    .error(R.drawable.ic_sad_green_alien_whatface)
                    .dontAnimate()
                    .into(holder.spaceImage);
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
