package ua.yurezcv.popularmovies.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.model.Movie;
import ua.yurezcv.popularmovies.utils.Utils;

public class MoviesGridRecyclerViewAdapter extends RecyclerView.Adapter<MoviesGridRecyclerViewAdapter.MoviesViewHolder> {

    private final Context mContext;

    private final List<Movie> mMoviesList;

    private final MoviesGridAdapterOnClickHandler mListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviesGridAdapterOnClickHandler {
        void onClick(int position);
    }

    MoviesGridRecyclerViewAdapter(Context context, MoviesGridAdapterOnClickHandler listener) {
        mContext = context;
        mListener = listener;
        mMoviesList = new ArrayList<>();
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_grid_item, parent, false);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position) {
        Movie movie = mMoviesList.get(position);

        String posterPath = movie.getPosterPath();
        String fullImgUrl = Utils.createPosterUrl(posterPath);

        Picasso.get()
                .load(fullImgUrl)
                .into(holder.mPosterImageView);

        // set content description for accessibility
        String posterContentDescription = mContext.
                getString(R.string.content_desc_iv_movie_poster, movie.getTitle());
        holder.mPosterImageView.setContentDescription(posterContentDescription);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public void setData(List<Movie> movieList) {
        if (movieList != null) {
            mMoviesList.addAll(movieList);
        }
    }

    /* Handle removing an item from the adapter */
    public void removeDataItem(int position) {
        mMoviesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void clearData() {
        mMoviesList.clear();
        notifyDataSetChanged();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mPosterImageView;

        MoviesViewHolder(View view) {
            super(view);
            mView = view;
            mPosterImageView = view.findViewById(R.id.iv_movie_poster);
        }

    }
}
