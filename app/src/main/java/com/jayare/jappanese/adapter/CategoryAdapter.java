package com.jayare.jappanese.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jayare.jappanese.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private List<String> categories = new ArrayList<>();
    private OnItemListener mOnItemListener;

    public CategoryAdapter(OnItemListener onItemListener){
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryHolder(itemView,mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.category.setText(categories.get(position));
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public String getCategoryAt(int position) {
        return categories.get(position);
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtCategory)
        TextView category;
        OnItemListener onItemListener;

        public CategoryHolder(@NonNull View itemView,final OnItemListener onItemListener) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
