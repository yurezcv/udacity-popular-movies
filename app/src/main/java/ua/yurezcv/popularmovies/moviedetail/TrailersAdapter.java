package ua.yurezcv.popularmovies.moviedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.model.Trailer;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private final Context mContext;

    private final ArrayList<Trailer> mTrailersList;

    private final TrailersAdapterOnClickHandler mListener;

    TrailersAdapter(Context context, TrailersAdapterOnClickHandler listener) {
        mContext = context;
        mListener = listener;
        mTrailersList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, int position) {
        final Trailer trailer = mTrailersList.get(position);

        holder.mTrailerTitleTextView.setText(trailer.getName());
        holder.mTrailerTypeTextView.setText(trailer.getType());

        holder.mShareTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onShareButtonClick(trailer);
                }
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onViewClick(trailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailersList.size();
    }

    public void setAdapterData(List<Trailer> trailers) {
        if (trailers != null) {
            mTrailersList.addAll(trailers);
        }
    }

    public ArrayList<Trailer> getAdapterData() {
        return mTrailersList;
    }

    public boolean isAdapterEmpty() {
        return mTrailersList.isEmpty();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final Button mShareTrailerButton;
        final TextView mTrailerTitleTextView;
        final TextView mTrailerTypeTextView;

        TrailerViewHolder(View view) {
            super(view);
            mView = view;
            mShareTrailerButton = view.findViewById(R.id.btn_share_trailer);
            mTrailerTitleTextView = view.findViewById(R.id.tv_trailer_title);
            mTrailerTypeTextView = view.findViewById(R.id.tv_trailer_type);
        }

    }

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailersAdapterOnClickHandler {
        void onShareButtonClick(Trailer trailer);
        void onViewClick(Trailer trailer);
    }
}
