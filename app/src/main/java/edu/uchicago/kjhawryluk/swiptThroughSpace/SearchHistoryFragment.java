package edu.uchicago.kjhawryluk.swiptThroughSpace;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uchicago.kjhawryluk.profinal2019.R;
import edu.uchicago.kjhawryluk.swiptThroughSpace.adaptors.QueryListAdaptor;
import edu.uchicago.kjhawryluk.swiptThroughSpace.data.local.entity.ImageQuery;
import edu.uchicago.kjhawryluk.swiptThroughSpace.viewmodels.NasaImageViewModel;


public class SearchHistoryFragment extends Fragment {
    private RecyclerView mQueryRecyclerView;
    private NasaImageViewModel mNasaImageViewModel;



    public SearchHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_query_history_list, container, false);

        mQueryRecyclerView = root.findViewById(R.id.queryList);
        mNasaImageViewModel = ViewModelProviders.of(this).get(NasaImageViewModel.class);

        final QueryListAdaptor listAdaptor = new QueryListAdaptor(container.getContext(), mNasaImageViewModel);
        mQueryRecyclerView.setAdapter(listAdaptor);
        mQueryRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mNasaImageViewModel.getQueryHistory().observe(this, new Observer<List<ImageQuery>>() {
            @Override
            public void onChanged(@Nullable List<ImageQuery> imageQueries) {
               listAdaptor.setQueries(imageQueries);
            }
        });
        return root;
    }
}
