package edu.uchicago.kjhawryluk.profinal2019.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class NasaImageTextAdaptor extends RecyclerView.Adapter<NasaImageTextAdaptor.NasaImageViewHolder> {
    private final LayoutInflater mInflater;
    private ImageDetails mImageDetails;


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mSpaceCardTitle;
        private final TextView mSpaceCardDesc;
        public NasaImageViewHolder(View itemView) {
            super(itemView);

            mSpaceCardTitle = itemView.findViewById(R.id.spaceCardTitle);
            mSpaceCardDesc = itemView.findViewById(R.id.spaceCardDesc);
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
