package edu.uchicago.kjhawryluk.profinal2019.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import edu.uchicago.kjhawryluk.profinal2019.OnSwipeTouchListener;
import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class NasaImageAdaptor extends RecyclerView.Adapter<NasaImageAdaptor.NasaImageViewHolder> implements ListPreloader.PreloadModelProvider<ImageDetails>{
    private final LayoutInflater mInflater;
    private final NasaImageViewModel mNasaImageViewModel;
    private Stack<ImageDetails> mImageDetails;

    @Override
    @NonNull
    public List<ImageDetails> getPreloadItems(int position){
        ImageDetails url = mImageDetails.get(position);
        if (url == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(url);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(@NonNull ImageDetails imageDetails) {
        return
                Glide.with(mInflater.getContext())
                        .load(imageDetails.getUri())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .placeholder(R.drawable.ic_alien_head)
                        .override(1000, 1000)
                        .error(R.drawable.ic_sad_green_alien_whatface)
                        .dontAnimate();
    }


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView spaceImage;

        public NasaImageViewHolder(View itemView) {
            super(itemView);
            spaceImage = itemView.findViewById(R.id.spaceImage);
        }
    }


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
            Glide.with(mInflater.getContext())
                    .load(current.getUri())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .override(1000, 1000)
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

    public void setImageDetails(Stack<ImageDetails> imageDetails) {
        mImageDetails = imageDetails;
        notifyDataSetChanged();
    }

}
