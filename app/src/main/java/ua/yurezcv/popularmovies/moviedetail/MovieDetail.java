package ua.yurezcv.popularmovies.moviedetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.DataRepository;
import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.Review;
import ua.yurezcv.popularmovies.data.model.Trailer;
import ua.yurezcv.popularmovies.utils.Utils;

public class MovieDetail extends AppCompatActivity implements MovieDetailContract.View {

    private MovieDetailContract.Presenter mPresenter;

    private ActionBar mActionBar;

    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private ImageView mPosterImageView;
    private TextView mOverviewTextView;
    private RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mReleaseDateTextView = findViewById(R.id.tv_movie_release_date);
        mPosterImageView = findViewById(R.id.iv_detail_poster);
        mOverviewTextView = findViewById(R.id.tv_movie_overview);
        mRatingBar = findViewById(R.id.rb_movie_rating);

        mActionBar = getSupportActionBar();
        mPresenter = new MovieDetailPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        mPresenter.getSelectedMovie();
    }

    @Override
    protected void onPause() {
        mPresenter.dropView();
        super.onPause();
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

        String posterPath = movie.getBackdropPath();
        String fullImgUrl = Utils.createLargePosterUrl(posterPath);

        Picasso.get()
                .load(fullImgUrl)
                .into(mPosterImageView);
    }
}
