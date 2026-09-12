package com.yeaminsheikh.smartshopping.adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeaminsheikh.smartshopping.R;
import com.yeaminsheikh.smartshopping.models.ShoppingItem;
import com.yeaminsheikh.smartshopping.utils.CurrencyFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter for displaying shopping list items.
 * Handles checkbox toggles, strikethrough styling, item editing,
 * and deletion callbacks.
 */
public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ShoppingItem item);
        void onCheckToggled(ShoppingItem item, boolean isChecked);
        void onDeleteClick(ShoppingItem item);
    }

    private List<ShoppingItem> items = new ArrayList<>();
    private final OnItemClickListener listener;
    private String currencySymbol = "$";

    public ShoppingListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<ShoppingItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setCurrencySymbol(String symbol) {
        this.currencySymbol = symbol;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = items.get(position);

        // Bind title
        holder.tvName.setText(item.getName());

        // Strikethrough effect when checked
        if (item.isChecked()) {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvName.setAlpha(0.5f);
        } else {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvName.setAlpha(1.0f);
        }

        // Details: quantity and category
        String details = item.getFormattedQuantity() + " • " + (item.getCategory() != null ? item.getCategory() : "General");
        holder.tvDetails.setText(details);

        // Store & Aisle information
        if (item.getStoreName() != null && !item.getStoreName().trim().isEmpty()) {
            String storeInfo = item.getStoreName();
            if (item.getAisle() != null && !item.getAisle().trim().isEmpty()) {
                storeInfo += " (" + item.getAisle() + ")";
            }
            holder.tvStore.setText(storeInfo);
            holder.tvStore.setVisibility(View.VISIBLE);
        } else {
            holder.tvStore.setVisibility(View.GONE);
        }

        // Price formatting
        holder.tvPrice.setText(CurrencyFormatter.format(item.getTotalCost(), currencySymbol));

        // Priority strip coloring (1 = High/Red, 2 = Med/Amber, 3 = Low/Blue)
        int priorityColor = Color.parseColor("#F59E0B"); // default medium
        if (item.getPriority() == 1) {
            priorityColor = Color.parseColor("#EF4444"); // high
        } else if (item.getPriority() == 3) {
            priorityColor = Color.parseColor("#3B82F6"); // low
        }
        holder.viewPriorityStrip.setBackgroundColor(priorityColor);

        // Checkbox listener
        holder.cbChecked.setOnCheckedChangeListener(null);
        holder.cbChecked.setChecked(item.isChecked());
        holder.cbChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onCheckToggled(item, isChecked);
            }
        });

        // Click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View viewPriorityStrip;
        CheckBox cbChecked;
        TextView tvName;
        TextView tvDetails;
        TextView tvStore;
        TextView tvPrice;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPriorityStrip = itemView.findViewById(R.id.view_priority_strip);
            cbChecked = itemView.findViewById(R.id.cb_checked);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDetails = itemView.findViewById(R.id.tv_item_details);
            tvStore = itemView.findViewById(R.id.tv_item_store);
            tvPrice = itemView.findViewById(R.id.tv_item_price);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
