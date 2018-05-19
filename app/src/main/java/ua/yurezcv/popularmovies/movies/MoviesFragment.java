package ua.yurezcv.popularmovies.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executors;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.DataRepository;
import ua.yurezcv.popularmovies.data.DataSourceContact;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.moviedetail.MovieDetail;
import ua.yurezcv.popularmovies.utils.EndlessRecyclerViewScrollListener;
import ua.yurezcv.popularmovies.utils.threading.AppExecutors;
import ua.yurezcv.popularmovies.utils.threading.DiskIOThreadExecutor;

/**
 * A fragment representing a grid of Movies.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View, MoviesGridRecyclerViewAdapter.MoviesGridAdapterOnClickHandler {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private static final String KEY_FILTER_STATE = "key-filter-state";

    private int mColumnCount = 2;

    // UI elements
    private RecyclerView mMoviesGridRecycleView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    private MoviesPresenter mPresenter;

    private MoviesGridRecyclerViewAdapter mMoviesGridAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    public MoviesFragment() {
    }

    public static MoviesFragment newInstance(int columnCount) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        AppExecutors appExecutors = AppExecutors.getInstance(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(AppExecutors.THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
        DataRepository dataRepository = DataRepository.getInstance(getActivity().getApplicationContext(), appExecutors);

        mPresenter = new MoviesPresenter(dataRepository);

        mMoviesGridAdapter = new MoviesGridRecyclerViewAdapter(getContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // bind view to the presenter and load movies
        mPresenter.takeView(this);
        // run first get movie query
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        mPresenter.dropView();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        mMoviesGridRecycleView = view.findViewById(R.id.rv_movies_grid);
        mProgressBar = view.findViewById(R.id.pb_loading_indicator);
        mErrorTextView = view.findViewById(R.id.tv_error_message);

        // init the recycler view and set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            mMoviesGridRecycleView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mMoviesGridRecycleView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // init onScrollListener for pagination loading in the movies grid
        mScrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) mMoviesGridRecycleView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // API page count starts from 1, EndlessRecyclerViewScrollListener counts from 0
                // that's why we need to add +1 to the page value
                mPresenter.loadMoviesFromPage(page+1);
            }
        };
        // Adds the scroll listener to RecyclerView
        mMoviesGridRecycleView.addOnScrollListener(mScrollListener);

        mMoviesGridRecycleView.setAdapter(mMoviesGridAdapter);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_filter_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // get current filter value from the presenter
        int currentMenu = mPresenter.updateMenuItem();

        // init menu items
        MenuItem popular = menu.findItem(R.id.menu_most_popular);
        MenuItem highestRated = menu.findItem(R.id.menu_highest_rated);
        MenuItem favorites = menu.findItem(R.id.menu_favorites);

        // enable/disable menu items based on active filter selection
        switch (currentMenu) {
            case DataSourceContact.FILTER_MOST_POPULAR:
                popular.setEnabled(false);
                highestRated.setEnabled(true);
                favorites.setEnabled(true);
                break;
            case DataSourceContact.FILTER_HIGHEST_RATED:
                popular.setEnabled(true);
                highestRated.setEnabled(false);
                favorites.setEnabled(true);
                break;
            case DataSourceContact.FILTER_FAVORITES:
                popular.setEnabled(true);
                highestRated.setEnabled(true);
                favorites.setEnabled(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                // show most popular movies
                clearAdapterData();
                mPresenter.loadMovies(DataSourceContact.FILTER_MOST_POPULAR);
                break;
            case R.id.menu_highest_rated:
                // show highest rated movies
                clearAdapterData();
                mPresenter.loadMovies(DataSourceContact.FILTER_HIGHEST_RATED);
                break;
            case R.id.menu_favorites:
                // show user's favorite movies
                clearAdapterData();
                mPresenter.loadMovies(DataSourceContact.FILTER_FAVORITES);
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the last selected filter value
        outState.putInt(KEY_FILTER_STATE, mPresenter.onSaveFilterState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            // restore the last selected filter value
            mPresenter.onRestoreFilterState(savedInstanceState.getInt(KEY_FILTER_STATE));
        }
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if(active) {
            mMoviesGridRecycleView.setVisibility(View.INVISIBLE);
            // hide an error text before showing the progress bar
            mErrorTextView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mMoviesGridRecycleView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMovies(final List<Movie> movies) {
        final int currentSize = mMoviesGridAdapter.getItemCount();

        mMoviesGridRecycleView.post(new Runnable() {
            @Override
            public void run() {
                mMoviesGridAdapter.setData(movies);
                mMoviesGridAdapter.notifyItemRangeInserted(currentSize, movies.size());
            }
        });
    }

    @Override
    public void showError(String errorMessage) {
        mErrorTextView.setText(errorMessage);
        mMoviesGridRecycleView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mScrollListener.resetState();
    }

    @Override
    public void notifyAdapterItemRemoved(int position) {
        mMoviesGridAdapter.removeDataItem(position);
    }

    @Override
    public void onClick(int position) {
        mPresenter.onMovieSelected(position);
        Intent movieDetailIntent = new Intent(getActivity(), MovieDetail.class);
        startActivity(movieDetailIntent);
    }

    // Simple method to clear the adapter data when a user changes the filter setting
    private void clearAdapterData() {
        mScrollListener.resetState();
        mMoviesGridAdapter.clearData();
        getActivity().invalidateOptionsMenu();
    }
}
