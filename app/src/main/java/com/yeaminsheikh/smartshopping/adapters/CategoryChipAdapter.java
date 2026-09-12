package com.yeaminsheikh.smartshopping.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeaminsheikh.smartshopping.R;
import com.yeaminsheikh.smartshopping.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Horizontal chip adapter for filtering the active shopping list by category.
 */
public class CategoryChipAdapter extends RecyclerView.Adapter<CategoryChipAdapter.ViewHolder> {

    public interface OnCategorySelectedListener {
        void onCategorySelected(String categoryName);
    }

    private List<Category> categories = new ArrayList<>();
    private String selectedCategory = "All";
    private final OnCategorySelectedListener listener;

    public CategoryChipAdapter(OnCategorySelectedListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<Category> list) {
        this.categories = new ArrayList<>();
        // Prepend 'All' category option
        Category all = new Category(0, "All", "#059669");
        this.categories.add(all);
        if (list != null) {
            this.categories.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setSelectedCategory(String category) {
        this.selectedCategory = category != null ? category : "All";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_chip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category cat = categories.get(position);
        holder.tvChip.setText(cat.getName());

        boolean isSelected = cat.getName().equalsIgnoreCase(selectedCategory);
        if (isSelected) {
            holder.tvChip.setBackgroundColor(Color.parseColor("#059669"));
            holder.tvChip.setTextColor(Color.WHITE);
        } else {
            holder.tvChip.setBackgroundResource(R.drawable.rounded_card_bg);
            holder.tvChip.setTextColor(Color.parseColor("#0F172A"));
        }

        holder.itemView.setOnClickListener(v -> {
            selectedCategory = cat.getName();
            notifyDataSetChanged();
            if (listener != null) {
                listener.onCategorySelected(cat.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChip = itemView.findViewById(R.id.tv_chip_category);
        }
    }
}
