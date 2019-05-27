package edu.uchicago.kjhawryluk.profinal2019;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class MainActivity extends AppCompatActivity {

    SearchView mSearchBar;
    NasaImageViewModel mNasaImageViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNasaImageViewModel = new NasaImageViewModel(this.getApplication());
        mSearchBar = findViewById(R.id.searchBar);
        mSearchBar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNasaImageViewModel.queryNasaImages(mSearchBar.getQuery().toString());
            }
        });
    }
}
