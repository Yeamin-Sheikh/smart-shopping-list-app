package com.yeaminsheikh.smartshopping.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeaminsheikh.smartshopping.R;
import com.yeaminsheikh.smartshopping.utils.CurrencyFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter presenting spending breakdown across product categories.
 */
public class BudgetCategoryAdapter extends RecyclerView.Adapter<BudgetCategoryAdapter.ViewHolder> {

    public static class CategorySpendItem {
        String category;
        double amount;
        int percentage;
    }

    private List<CategorySpendItem> items = new ArrayList<>();
    private String currencySymbol = "$";

    public void setData(Map<String, Double> categoryMap, double totalCost, String currencySymbol) {
        this.currencySymbol = currencySymbol;
        this.items = new ArrayList<>();
        if (categoryMap != null) {
            for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
                CategorySpendItem item = new CategorySpendItem();
                item.category = entry.getKey();
                item.amount = entry.getValue();
                item.percentage = totalCost > 0 ? (int) Math.round((item.amount / totalCost) * 100) : 0;
                items.add(item);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategorySpendItem item = items.get(position);
        holder.tvName.setText(item.category);
        holder.tvAmount.setText(String.format("%s (%d%%)", CurrencyFormatter.format(item.amount, currencySymbol), item.percentage));
        holder.progressBar.setProgress(item.percentage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAmount;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_cat_name);
            tvAmount = itemView.findViewById(R.id.tv_cat_amount);
            progressBar = itemView.findViewById(R.id.pb_cat_progress);
        }
    }
}
