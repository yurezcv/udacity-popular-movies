package ua.yurezcv.popularmovies.moviedetail;

import java.util.List;

import ua.yurezcv.popularmovies.BasePresenter;
import ua.yurezcv.popularmovies.BaseView;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.data.model.Review;
import ua.yurezcv.popularmovies.data.model.Trailer;

public interface MovieDetailContract {

    interface View extends BaseView<MovieDetailContract.Presenter> {

        void showMovieDetail(Movie movie);

        void updateMenu();

        void showErrorMessage(String message);

        void showTrailers(List<Trailer> trailers);

        void showReviews(List<Review> reviews);

        void hideReviewsLayout();

        void hideTrailersLayout();
    }

    interface Presenter extends BasePresenter<MovieDetailContract.View> {

        void takeView(MovieDetailContract.View movieDetailContractView);

        void dropView();

        void getSelectedMovie();

        void updateFavoritesValue();

        boolean getFavoritesValue();

        void checkIfMovieInFavorites();

        void loadReviews();

        void loadTrailers();

        void onRestoreState();
    }
}
