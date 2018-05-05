package ua.yurezcv.popularmovies.moviedetail;

import ua.yurezcv.popularmovies.BasePresenter;
import ua.yurezcv.popularmovies.BaseView;
import ua.yurezcv.popularmovies.data.model.Movie;

public interface MovieDetailContract {

    interface View extends BaseView<MovieDetailContract.Presenter> {
        void showMovieDetail(Movie movie);
    }

    interface Presenter extends BasePresenter<MovieDetailContract.View> {

        void takeView(MovieDetailContract.View movieDetailContractView);

        void dropView();

        void getSelectedMovie();
    }
}
