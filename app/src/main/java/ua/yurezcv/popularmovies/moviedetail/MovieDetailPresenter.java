package ua.yurezcv.popularmovies.moviedetail;


import ua.yurezcv.popularmovies.data.DataRepository;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private DataRepository mDataRepository;

    private MovieDetailContract.View mView;

    MovieDetailPresenter() {
        mDataRepository = DataRepository.getInstance();
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
}
