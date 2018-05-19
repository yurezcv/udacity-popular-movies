package ua.yurezcv.popularmovies.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Executors;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.DataRepository;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.Review;
import ua.yurezcv.popularmovies.data.model.Trailer;
import ua.yurezcv.popularmovies.utils.Utils;
import ua.yurezcv.popularmovies.utils.threading.AppExecutors;
import ua.yurezcv.popularmovies.utils.threading.DiskIOThreadExecutor;

public class MovieDetail extends AppCompatActivity implements MovieDetailContract.View {

    private MovieDetailContract.Presenter mPresenter;

    private ActionBar mActionBar;

    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private ImageView mBackdropImageView;
    private ImageView mPosterImageView;
    private TextView mOverviewTextView;
    private RatingBar mRatingBar;
    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    private LinearLayout mTrailersLayout;
    private LinearLayout mReviewLayout;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mReleaseDateTextView = findViewById(R.id.tv_movie_release_date);
        mBackdropImageView = findViewById(R.id.iv_detail_backdrop);
        mPosterImageView = findViewById(R.id.iv_detail_poster);
        mOverviewTextView = findViewById(R.id.tv_movie_overview);
        mRatingBar = findViewById(R.id.rb_movie_rating);
        mTrailersLayout = findViewById(R.id.ll_trailers);
        mReviewLayout = findViewById(R.id.ll_reviews);

        mActionBar = getSupportActionBar();

        mTrailersRecyclerView = findViewById(R.id.rv_trailers);
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTrailersAdapter = new TrailersAdapter(this, new TrailersAdapter.TrailersAdapterOnClickHandler() {
            @Override
            public void onShareButtonClick(Trailer trailer) {
                shareTrailer(trailer.getKey());
            }

            @Override
            public void onViewClick(Trailer trailer) {
                openYoutubeVideo(trailer.getKey());
            }
        });

        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        mReviewsRecyclerView = findViewById(R.id.rv_reviews);
        mReviewsAdapter = new ReviewsAdapter(this);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        AppExecutors appExecutors = AppExecutors.getInstance(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(AppExecutors.THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());

        mPresenter = new MovieDetailPresenter(DataRepository.getInstance(getApplicationContext(), appExecutors));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        mPresenter.getSelectedMovie();
        invalidateOptionsMenu();
        mPresenter.checkIfMovieInFavorites();
    }

    @Override
    protected void onPause() {
        mPresenter.dropView();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_add_to_favorites);
        item.setIcon(getFavoritesIconId());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_to_favorites:
                // show most popular movies
                mPresenter.updateFavoritesValue();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return true;
    }

    @Override
    public void showMovieDetail(Movie movie) {
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(movie.getTitle());

        mTitleTextView.setText(movie.getTitle());
        mOverviewTextView.setText(movie.getOverview());
        mRatingBar.setRating(movie.getVoteAverage());

        try {
            mReleaseDateTextView.setText(movie.getFormattedReleaseDate());
        } catch (ParseException e) {
            mReleaseDateTextView.setText(movie.getReleaseDate());
            e.printStackTrace();
        }

        String backdropPathPath = movie.getBackdropPath();
        String backdropFullUrl = Utils.createLargePosterUrl(backdropPathPath);
        String posterPath = movie.getPosterPath();
        String posterFullUrl = Utils.createPosterUrl(posterPath);

        Picasso.get()
                .load(backdropFullUrl)
                .into(mBackdropImageView);

        Picasso.get()
                .load(posterFullUrl)
                .into(mPosterImageView);

        // check if review have been loaded already
        if(mReviewsAdapter.isAdapterEmpty()) {
            mPresenter.loadReviews();
        }

        // check if trailers have been loaded already
        if(mTrailersAdapter.isAdapterEmpty()) {
            mPresenter.loadTrailers();
        }
    }

    @Override
    public void updateMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTrailers(final List<Trailer> trailers) {
        // hide trailers layout if there is no trailers for the movie
        if(trailers.isEmpty()) {
            hideTrailersLayout();
        } else {
            final int currentSize = mTrailersAdapter.getItemCount();
            mTrailersRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mTrailersAdapter.setAdapterData(trailers);
                    mTrailersAdapter.notifyItemRangeInserted(currentSize, trailers.size());
                }
            });
        }
    }

    @Override
    public void showReviews(final List<Review> reviews) {
        if(reviews.isEmpty()) {
            hideReviewsLayout();
        } else {
            final int currentSize = mReviewsAdapter.getItemCount();
            mReviewsRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mReviewsAdapter.setAdapterData(reviews);
                    mReviewsAdapter.notifyItemRangeInserted(currentSize, reviews.size());
                }
            });
        }
    }

    @Override
    public void hideReviewsLayout() {
        mReviewLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideTrailersLayout() {
        mTrailersLayout.setVisibility(View.GONE);
    }

    private void openYoutubeVideo(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        // Check if the youtube app exists on the device
        if (intent.resolveActivity(getPackageManager()) == null) {
            // use a browser otherwise
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Utils.makeYoutubeLink(key)));
        }

        startActivity(intent);
    }

    private void shareTrailer(String key) {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("Hey friend, check out this awesome trailer! " +
                        Utils.makeYoutubeLink(key) + " #udacity-popular-movies")
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(shareIntent);
    }

    private int getFavoritesIconId() {
        return mPresenter.getFavoritesValue()
                ? R.drawable.ic_menu_favorites : R.drawable.ic_menu_favorites_outline;
    }
}
