package edu.uchicago.kjhawryluk.swiptThroughSpace.adaptors;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;
import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.swiptThroughSpace.viewmodels.NasaImageViewModel;

public class NasaImageListAdaptor extends RecyclerView.Adapter<NasaImageListAdaptor.NasaImageViewHolder> {
    private final LayoutInflater mInflater;
    private NasaImageViewModel mNasaImageViewModel;
    private boolean mShowFavorites;
    private List<ImageDetails> mImageDetails;


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mSpaceImage;
        private final TextView mSpaceTitle;
        private ImageButton mFavoriteButton;
        private ImageButton mRemoveButton;
        private CardView mCardView;

        public NasaImageViewHolder(View itemView) {
            super(itemView);

            mSpaceImage = itemView.findViewById(R.id.spaceImageThumb);
            mSpaceTitle = itemView.findViewById(R.id.swipedImagesList);
            mFavoriteButton = itemView.findViewById(R.id.favorite);
            mRemoveButton = itemView.findViewById(R.id.remove);
            mCardView = itemView.findViewById(R.id.card_view);
        }
    }


    public NasaImageListAdaptor(Context context, NasaImageViewModel nasaImageViewModel, boolean showFavorites) {
        mInflater = LayoutInflater.from(context);
        mShowFavorites = showFavorites;
        mNasaImageViewModel = nasaImageViewModel;
    }

    @Override
    public NasaImageListAdaptor.NasaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.space_image_list_item, parent, false);
        return new NasaImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NasaImageListAdaptor.NasaImageViewHolder holder, int position) {

        if (mImageDetails != null) {
            final ImageDetails current = mImageDetails.get(position);
            holder.mSpaceTitle.setText(current.getTitle());
            Glide.with(mInflater.getContext())
                    .load(current.getUri())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .override(1000, 1000)
                    .error(R.drawable.ic_sad_green_alien_whatface)
                    .dontAnimate()
                    .into(holder.mSpaceImage);

            holder.mCardView.setOnClickListener(new ShowSelectedImageListener(current));

            // Show the correct image
            if (mShowFavorites) {
                holder.mFavoriteButton.setVisibility(View.VISIBLE);
                holder.mRemoveButton.setVisibility(View.GONE);
                holder.mFavoriteButton.setOnClickListener(new UpdateSavedSwipedImage(current, holder));
            } else {
                holder.mFavoriteButton.setVisibility(View.GONE);
                holder.mRemoveButton.setVisibility(View.VISIBLE);
                holder.mRemoveButton.setOnClickListener(new UpdateSavedSwipedImage(current, holder));
            }
        }
    }

    class ShowSelectedImageListener implements View.OnClickListener{
        ImageDetails mImageDetails;

        public ShowSelectedImageListener(ImageDetails imageDetails) {
            mImageDetails = imageDetails;
        }

        @Override
        public void onClick(View v) {
           // mNasaImageViewModel.pushImage(mImageDetails);
            SwipeThroughSwipedImages ctx =  (SwipeThroughSwipedImages) mInflater.getContext();
            ctx.swipeThroughSwipedImages(mImageDetails);
        }
    }

    class UpdateSavedSwipedImage implements View.OnClickListener {
        ImageDetails mImageDetails;
        NasaImageViewHolder mNasaImageViewHolder;

        public UpdateSavedSwipedImage(ImageDetails imageDetails, NasaImageViewHolder viewHolder) {
            mImageDetails = imageDetails;
            mNasaImageViewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            boolean newStatus = !mShowFavorites;
            // Show the new icon
            if(newStatus){
                mNasaImageViewHolder.mFavoriteButton.setVisibility(View.VISIBLE);
                mNasaImageViewHolder.mRemoveButton.setVisibility(View.GONE);
            } else {
                mNasaImageViewHolder.mFavoriteButton.setVisibility(View.GONE);
                mNasaImageViewHolder.mRemoveButton.setVisibility(View.VISIBLE);
            }

            // Wait for a moment and then save the new status
            new CountDownTimer(150, 1000) {
                public void onFinish() {
                    mNasaImageViewModel.saveImageDetails(newStatus, mImageDetails);
                }
                public void onTick(long millisUntilFinished) {

                }
            }.start();
        }
    }

    @Override
    public int getItemCount() {
        if (mImageDetails != null)
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

    public interface SwipeThroughSwipedImages{
        void swipeThroughSwipedImages(ImageDetails imageDetails);
    }
}
