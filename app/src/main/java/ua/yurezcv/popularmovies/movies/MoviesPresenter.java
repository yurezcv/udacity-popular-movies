package ua.yurezcv.popularmovies.movies;

import java.util.List;

import ua.yurezcv.popularmovies.data.DataRepository;
import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.data.model.Movie;

public class MoviesPresenter implements MoviesContract.Presenter {

    private DataRepository mDataRepository;

    private MoviesContract.View mView;

    private MoviesFilterType mCurrentFilterSelection;

    private boolean isFirstLoad = true;

    MoviesPresenter() {
        mDataRepository = DataRepository.getInstance();
    }

    @Override
    public void takeView(MoviesContract.View moviesActivityView) {
        mView = moviesActivityView;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void onResume() {
        // set popular movies as default for the first loading
        if(isFirstLoad) {
            loadMovies(MoviesFilterType.POPULAR_MOVIES);
            isFirstLoad = false;
        }
    }

    @Override
    public void loadMovies(MoviesFilterType moviesFilterType) {

        // avoid loading movies if the current selection stays the same
        if(moviesFilterType != mCurrentFilterSelection) {
            mCurrentFilterSelection = moviesFilterType;
        } else {
            return;
        }

        mView.setProgressIndicator(true);

        mDataRepository.loadMovies(mCurrentFilterSelection, new DataSourceContact.LoadMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                mView.showMovies(movies);
                mView.setProgressIndicator(false);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mView.showError(throwable.getMessage());
            }
        });
    }

    @Override
    public void loadMoviesFromPage(int page) {

        // mView.setProgressIndicator(true);

        mDataRepository.loadMovies(mCurrentFilterSelection, page, new DataSourceContact.LoadMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                mView.showMovies(movies);
                // mView.setProgressIndicator(false);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mView.showError(throwable.getMessage());
            }
        });
    }

    @Override
    public void onMovieSelected(int position) {
        mDataRepository.setSelectedPosition(position);
    }
}
