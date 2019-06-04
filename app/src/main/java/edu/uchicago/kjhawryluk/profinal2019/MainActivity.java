package edu.uchicago.kjhawryluk.profinal2019;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import edu.uchicago.kjhawryluk.profinal2019.adaptors.NasaImageListAdaptor;
import edu.uchicago.kjhawryluk.profinal2019.data.local.entity.ImageDetails;
import edu.uchicago.kjhawryluk.profinal2019.util.PrefsMgr;
import edu.uchicago.kjhawryluk.profinal2019.viewmodels.NasaImageViewModel;

public class MainActivity extends AppCompatActivity implements NasaImageListAdaptor.SwipeThroughSwipedImages {
    public static final String TUTORIAL_SHOWN = "TUTORIAL_SHOWN";
    private static final String USE_DARK_THEME = "USE_DARK_THEME";
    RadioButton darkThemeButton;
    RadioButton lightThemeButton;

    SearchView mSearchBar;
    NasaImageViewModel mNasaImageViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean useDarkTheme = PrefsMgr.getBoolean(this, USE_DARK_THEME, true);
        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }
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

        // Show tutorial dialog
        boolean bTutorialShown = PrefsMgr.getBoolean(this, TUTORIAL_SHOWN, false);
        if (!bTutorialShown){
            launchTutorialDialog(this);
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
        if (id == R.id.settings) {
            launchSettingsDialog(this);
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

    public void launchTutorialDialog(Context ctx){
        Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View tutorialView = getLayoutInflater().inflate(R.layout.tutorial_dialog, null);
        settingsDialog.setContentView(tutorialView);
        settingsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                PrefsMgr.setBoolean(ctx, TUTORIAL_SHOWN, true);
            }
        });
        settingsDialog.show();
    }


    public void launchSettingsDialog(Context ctx){
        Dialog settingsDialog = new Dialog(this);
        View settingsView = getLayoutInflater().inflate(R.layout.settings_dialog, null);

        // Position Dialog and dim background
        // https://stackoverflow.com/questions/9467026/changing-position-of-the-dialog-on-screen-android
        Window window = settingsDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        darkThemeButton = settingsView.findViewById(R.id.darkThemButton);
        lightThemeButton = settingsView.findViewById(R.id.lightThemButton);
        RadioGroup radioGroup = settingsView.findViewById(R.id.themeRadioGroup);

        // Create and set up theme radio group
        boolean useDarkTheme = PrefsMgr.getBoolean(ctx, USE_DARK_THEME, true);

        setThemeButtons(useDarkTheme);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isDarkTheme = checkedId == R.id.darkThemButton;
                PrefsMgr.setBoolean(ctx, USE_DARK_THEME, isDarkTheme);
                // Change theme
                recreate();
            }
        });

        // Create and set up tutorial checkbox
        CheckBox tutorialSeenCheckbox = settingsView.findViewById(R.id.tutorialSeen);
        boolean tutorialSeen = PrefsMgr.getBoolean(ctx, TUTORIAL_SHOWN, true);
        tutorialSeenCheckbox.setChecked(tutorialSeen);
        tutorialSeenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefsMgr.setBoolean(ctx, TUTORIAL_SHOWN,!tutorialSeen);
            }
        });
        settingsDialog.setContentView(settingsView);
        settingsDialog.show();
    }

    private void setThemeButtons(boolean useDarkTheme) {
        if (!useDarkTheme) {
            lightThemeButton.setChecked(true);
        } else{
            darkThemeButton.setChecked(true);
        }
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
