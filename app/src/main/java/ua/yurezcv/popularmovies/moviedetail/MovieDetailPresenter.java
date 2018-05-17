package ua.yurezcv.popularmovies.moviedetail;


import android.net.Uri;

import ua.yurezcv.popularmovies.data.DataRepository;
import ua.yurezcv.popularmovies.data.DataSourceContact;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private DataRepository mDataRepository;

    private MovieDetailContract.View mView;

    private boolean isInFavorites = false;

    MovieDetailPresenter(DataRepository dataRepository) {
        mDataRepository = dataRepository;
    }

    @Override
    public void takeView(MovieDetailContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void getSelectedMovie() {
        mView.showMovieDetail(mDataRepository.getLastSelectedMovie());
    }

    @Override
    public void updateFavoritesValue() {
        if (isInFavorites) {
            mDataRepository.removeFromFavorites(new DataSourceContact.RemoveFromFavoritesCallback() {
                @Override
                public void onSuccess(int rowsDeleted) {
                    if (rowsDeleted > 0) {
                        isInFavorites = false;
                        mView.updateMenu();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    mView.showErrorMessage(throwable.getMessage());
                }
            });
        } else {
            mDataRepository.addToFavorites(mDataRepository.getLastSelectedMovie(), new DataSourceContact.AddToFavoritesCallback() {
                @Override
                public void onSuccess(Uri uri) {
                    isInFavorites = true;
                    mView.updateMenu();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    isInFavorites = false;
                    mView.showErrorMessage(throwable.getMessage());
                }
            });
        }
    }

    @Override
    public void checkIfMovieInFavorites() {
        mDataRepository.isMovieInFavorites(new DataSourceContact.IsMovieInFavoritesCallback() {
            @Override
            public void onSuccess(boolean favorites) {
                isInFavorites = favorites;
                if (isInFavorites) {
                    mView.updateMenu();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                mView.showErrorMessage(throwable.getMessage());
            }
        });
    }

    @Override
    public boolean getFavoritesValue() {
        return isInFavorites;
    }

}
