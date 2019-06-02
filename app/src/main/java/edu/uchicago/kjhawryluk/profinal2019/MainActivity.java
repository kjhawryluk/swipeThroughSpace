package edu.uchicago.kjhawryluk.profinal2019;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageListAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class MainActivity extends AppCompatActivity implements NasaImageListAdaptor.SwipeThroughSwipedImages {

    SearchView mSearchBar;
    NasaImageViewModel mNasaImageViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNasaImageViewModel = new NasaImageViewModel(this.getApplication());
        mSearchBar = findViewById(R.id.searchBar);
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // If there's an actual query, process it.
            if(query != null && !query.isEmpty()) {
                mNasaImageViewModel.queryNasaImages(query);
                SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                swapInFragment(searchResultsFragment);
                getIntent().removeExtra(SearchManager.QUERY);
            }
        } else{
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            swapInFragment(welcomeFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.showFavorites) {
            SwipedImagesListFragment swipedImagesListFragment = SwipedImagesListFragment.newInstance(true);
            swapInFragment(swipedImagesListFragment);
            return true;
        }
        if (id == R.id.showDisliked) {
            SwipedImagesListFragment swipedImagesListFragment = SwipedImagesListFragment.newInstance(false);
            swapInFragment(swipedImagesListFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchBar).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }


    void swapInFragment(Fragment fragment) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void swipeThroughSwipedImages(ImageDetails imageDetails){
        Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LinearLayout spaceImageLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_space_card_image
                , null);
        ImageView spaceCardImage = spaceImageLayout.findViewById(R.id.spaceCardImageDialog);

        Glide.with(this)
                .load(imageDetails.getUri())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(1000, 1000)
                .error(R.drawable.ic_sad_green_alien_whatface)
                .dontAnimate()
                .into(spaceCardImage);
        settingsDialog.setContentView(spaceImageLayout);

        settingsDialog.show();
    }
}
