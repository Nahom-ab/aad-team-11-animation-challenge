package com.team11.animation_challenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {
    Activity mActivity;
    private final Context mContext;
    private final ArrayList<CategoryModel> mCategories;
    private final LayoutInflater mLayoutInflater;

    public CategoryRecyclerAdapter(Context context, ArrayList<CategoryModel> categories) {
        this.mContext = context;
        this.mCategories = categories;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.category_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel categories = mCategories.get(position);
        holder.mCategoryTitle.setText(categories.title);
        holder.mCategoryIcon.setImageResource(categories.icon);
        holder.mUrl = categories.url;
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mCategoryTitle;
        public final ImageView mCategoryIcon;
        public String mUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            mCategoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TriviaActivity.class);
                    intent.putExtra(TriviaActivity.CATEGORY_URL, mUrl);
                    intent.putExtra(TriviaActivity.CATEGORY_TITLE, mCategoryTitle.getText());
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
                }
            });
        }
    }
}
