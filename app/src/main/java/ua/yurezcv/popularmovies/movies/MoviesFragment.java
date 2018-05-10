package ua.yurezcv.popularmovies.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.moviedetail.MovieDetail;
import ua.yurezcv.popularmovies.utils.EndlessRecyclerViewScrollListener;

import java.util.List;

/**
 * A fragment representing a grid of Movies.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View, MoviesGridRecyclerViewAdapter.MoviesGridAdapterOnClickHandler {

    private static final String ARG_COLUMN_COUNT = "column-count";

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

        mPresenter = new MoviesPresenter();

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

        // Retain an instance so that you can call `resetState()` for fresh searches
        mScrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) mMoviesGridRecycleView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                // mPresenter.loadMovies();
                // loadNextDataFromApi(page);
                Log.d("MoviesFragment", "onLoadMore in RecyclerView");
            }
        };
        // Adds the scroll listener to RecyclerView
        mMoviesGridRecycleView.addOnScrollListener(mScrollListener);

        mMoviesGridRecycleView.setAdapter(mMoviesGridAdapter);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                // show most popular movies
                mPresenter.loadMovies(MoviesFilterType.POPULAR_MOVIES);
                break;
            case R.id.menu_highest_rated:
                // show highest rated movies
                mPresenter.loadMovies(MoviesFilterType.HIGHEST_RATED_MOVIES);
                break;
            case R.id.menu_favorites:
                // show user's favorite movies
                mPresenter.loadMovies(MoviesFilterType.FAVORITES);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_filter_menu, menu);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if(active) {
            mMoviesGridRecycleView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mMoviesGridRecycleView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMoviesGridAdapter.setData(movies);
    }

    @Override
    public void showError(String errorMessage) {
        mErrorTextView.setText(errorMessage);
        mMoviesGridRecycleView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int position) {
        mPresenter.onMovieSelected(position);
        Intent movieDetailIntent = new Intent(getActivity(), MovieDetail.class);
        startActivity(movieDetailIntent);
    }
}
