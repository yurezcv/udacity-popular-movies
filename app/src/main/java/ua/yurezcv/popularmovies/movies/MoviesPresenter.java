package ua.yurezcv.popularmovies.movies;

import android.util.Log;

import java.util.List;

import ua.yurezcv.popularmovies.data.DataRepository;
import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.data.model.Movie;

public class MoviesPresenter implements MoviesContract.Presenter {

    private DataRepository mDataRepository;

    private MoviesContract.View mView;

    private int mCurrentFilterSelection;

    private boolean isFirstLoad;
    private boolean isOnRestoreState;

    MoviesPresenter(DataRepository dataRepository) {
        mDataRepository = dataRepository;
        isFirstLoad = true;
        isOnRestoreState = false;
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
        if (isFirstLoad) {
            loadMovies(DataSourceContact.FILTER_MOST_POPULAR);
            isFirstLoad = false;
        }

        // load movies if it's on restore fragment state
        if(isOnRestoreState) {
            loadMovies(mCurrentFilterSelection);
            isOnRestoreState = false;
        }

        // reload list of favorites if the filter is set to show Favorites
        // and a movie was removed from favorites
        if (mCurrentFilterSelection == DataSourceContact.FILTER_FAVORITES
                && mDataRepository.isFavoritesUpdated()) {
            // remove favorites movie from the list if the change is flagged
            mView.notifyAdapterItemRemoved(mDataRepository.getSelectedPosition());
        }
        // clear favorites updated flag
        mDataRepository.clearIsFavoritesUpdatedFlag();
    }

    @Override
    public void loadMovies(int moviesFilterType) {

        // avoid loading movies if the current selection stays the same
        if (moviesFilterType != mCurrentFilterSelection
                || mDataRepository.isFavoritesUpdated()
                || isOnRestoreState) {
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
        // TODO add a progress bar for page loading
        // pagination load only for non-favorite movies
        if (mCurrentFilterSelection != DataSourceContact.FILTER_FAVORITES) {
            mDataRepository.loadMovies(mCurrentFilterSelection, page, new DataSourceContact.LoadMoviesCallback() {
                @Override
                public void onSuccess(List<Movie> movies) {
                    mView.showMovies(movies);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    mView.showError(throwable.getMessage());
                }
            });
        }
    }

    @Override
    public void onMovieSelected(int position) {
        mDataRepository.setSelectedPosition(position);
    }

    @Override
    public int onSaveFilterState() {
        return mCurrentFilterSelection;
    }

    @Override
    public void onRestoreFilterState(int filter) {
        mCurrentFilterSelection = filter;
        isOnRestoreState = true;
        isFirstLoad = false;
    }

    @Override
    public int updateMenuItem() {
        return mCurrentFilterSelection;
    }

}
