package ua.yurezcv.popularmovies.moviedetail;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.concurrent.Executors;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.DataRepository;
import ua.yurezcv.popularmovies.data.model.Movie;
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

        mActionBar = getSupportActionBar();

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

        // TODO add trailers and reviews to this screen

        // TODO add sharing for YouTube trailers

        // TODO add offline access for Favorites

        // TODO handle network status in MoviesFragment

/*        DataRepository.getInstance().loadMovieTrailers(movie.getId(), new DataSourceContact.LoadTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                Log.d("MAIN_ACTIVITY", trailers.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MAIN_ACTIVITY", throwable.getMessage());
            }
        });

        DataRepository.getInstance().loadMovieReviews(movie.getId(), new DataSourceContact.LoadReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                Log.d("MAIN_ACTIVITY", reviews.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MAIN_ACTIVITY", throwable.getMessage());
            }
        });*/
    }

    @Override
    public void updateMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private int getFavoritesIconId() {
        return mPresenter.getFavoritesValue()
                ? R.drawable.ic_menu_favorites : R.drawable.ic_menu_favorites_outline;
    }
}
