package edu.uchicago.kjhawryluk.profinal2019.adaptors;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import edu.uchicago.kjhawryluk.profinal2019.MainActivity;
import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageQuery;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class QueryListAdaptor extends RecyclerView.Adapter<QueryListAdaptor.NasaImageViewHolder> {
    private final LayoutInflater mInflater;
    private NasaImageViewModel mNasaImageViewModel;
    private List<ImageQuery> mQueries;


    class NasaImageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mQuery;
        private final ImageButton mDelete;

        public NasaImageViewHolder(View itemView) {
            super(itemView);
            mQuery = itemView.findViewById(R.id.searchQuery);
            mDelete = itemView.findViewById(R.id.deleteQuery);
        }
    }


    public QueryListAdaptor(Context context, NasaImageViewModel nasaImageViewModel) {
        mInflater = LayoutInflater.from(context);
        mNasaImageViewModel = nasaImageViewModel;
    }

    @Override
    public QueryListAdaptor.NasaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.search_query_item, parent, false);
        return new NasaImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QueryListAdaptor.NasaImageViewHolder holder, int position) {

        if (mQueries != null) {
            final ImageQuery current = mQueries.get(position);
            holder.mQuery.setText(current.getQuery());
            holder.mQuery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mInflater.getContext(), MainActivity.class);
                    intent.setAction(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY, current.getQuery());
                    mInflater.getContext().startActivity(intent);
                }
            });


            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNasaImageViewModel.deleteQuery(current);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mQueries != null)
            return mQueries.size();
        return 0;
    }

    public List<ImageQuery> getQueries() {
        return mQueries;
    }

    public void setQueries(List<ImageQuery> queries) {
        mQueries = queries;
        notifyDataSetChanged();
    }

}
