package edu.uchicago.kjhawryluk.swiptThroughSpace.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.uchicago.kjhawryluk.swipeThroughSpace.R;
import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageDetails;

public class NasaImageTextAdaptor extends RecyclerView.Adapter<NasaImageTextAdaptor.NasaImageViewHolder> {
    private final LayoutInflater mInflater;
    private ImageDetails mImageDetails;


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mSpaceCardTitle;
        private final TextView mSpaceCardDesc;
        private final LinearLayout mShareButton;
        public NasaImageViewHolder(View itemView) {
            super(itemView);
            mSpaceCardTitle = itemView.findViewById(R.id.spaceCardTitle);
            mSpaceCardDesc = itemView.findViewById(R.id.spaceCardDesc);
            mShareButton = itemView.findViewById(R.id.shareButton);
        }
    }


    public NasaImageTextAdaptor(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public NasaImageTextAdaptor.NasaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_space_card_text, parent, false);
        return new NasaImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NasaImageTextAdaptor.NasaImageViewHolder holder, int position) {

        if(mImageDetails != null){
            final ImageDetails current = mImageDetails;
            holder.mSpaceCardTitle.setText(current.getTitle());
            holder.mSpaceCardDesc.setText(current.getDescription());
            holder.mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareImage(current);
                }
            });

            // Reset Ellipses
            holder.mSpaceCardDesc.setMaxLines(100);
            holder.mSpaceCardDesc.setEllipsize(null);

            //https://stackoverflow.com/questions/2876116/how-to-find-out-if-textview-text-content-has-been-cut-off
            holder.mSpaceCardDesc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = holder.mSpaceCardDesc.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                    int height = holder.mSpaceCardDesc.getHeight();
                    int scrollY = holder.mSpaceCardDesc.getScrollY();
                    Layout layout = holder.mSpaceCardDesc.getLayout();
                    //check is latest line fully visible
                    int lastVisibleLineNumber = layout.getLineForVertical(height + scrollY);

                    // If it's overflowing, truncate it.
                    if (holder.mSpaceCardDesc.getHeight() < layout.getLineBottom(lastVisibleLineNumber)) {
                        holder.mSpaceCardDesc.setMaxLines(lastVisibleLineNumber);
                        holder.mSpaceCardDesc.setEllipsize(TextUtils.TruncateAt.END);
                    }
                }
            });
        }
    }

    /**
     * Shares the currently shown image.
     * @param current
     */
    private void shareImage(ImageDetails current) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, current.getUri());
        sendIntent.setType("image/jpeg");
        mInflater.getContext().startActivity(sendIntent);
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
