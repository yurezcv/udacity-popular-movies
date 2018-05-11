package ua.yurezcv.popularmovies.movies;

import java.util.List;

import ua.yurezcv.popularmovies.BasePresenter;
import ua.yurezcv.popularmovies.BaseView;
import ua.yurezcv.popularmovies.data.model.Movie;

public interface MoviesContract {

    interface View extends BaseView<Presenter> {

        void setProgressIndicator(boolean active);

        void showMovies(List<Movie> movies);

        void showError(String errorMessage);
    }

    interface Presenter extends BasePresenter<View> {

        void takeView(MoviesContract.View moviesActivityView);

        void dropView();

        void onResume();

        void loadMovies(int moviesFilterType);

        void loadMoviesFromPage(int page);

        void onMovieSelected(int position);
    }
}
