package ua.yurezcv.popularmovies.moviedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.popularmovies.R;
import ua.yurezcv.popularmovies.data.model.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private final Context mContext;

    private final List<Review> mReviewsList;

    ReviewsAdapter(Context context) {
        mContext = context;
        mReviewsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_review, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        final Review review = mReviewsList.get(position);

        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    public void setAdapterData(List<Review> reviews) {
        if (reviews != null) {
            mReviewsList.addAll(reviews);
        }
    }

    public boolean isAdapterEmpty() {
        return mReviewsList.isEmpty();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mAuthorTextView;
        final TextView mContentTextView;

        ReviewsViewHolder(View view) {
            super(view);
            mView = view;
            mAuthorTextView = view.findViewById(R.id.tv_review_author);
            mContentTextView = view.findViewById(R.id.tv_review_content);
        }

    }
}
