package com.yeaminsheikh.smartshopping.models;

import java.io.Serializable;
import java.util.Locale;

/**
 * Core model representing an individual grocery or household shopping item.
 * Encapsulates purchase state, category tagging, store aisle routing,
 * and price estimation for budget analytics.
 */
public class ShoppingItem implements Serializable {
    private long id;
    private String name;
    private String category;
    private double quantity;
    private String unit;
    private double estimatedPrice;
    private boolean isChecked;
    private String storeName;
    private String aisle;
    private int priority; // 1 = High, 2 = Medium, 3 = Low
    private String notes;
    private boolean isRecurring;
    private long createdAt;

    public ShoppingItem() {
        this.quantity = 1.0;
        this.unit = "pcs";
        this.priority = 2; // Default Medium
        this.createdAt = System.currentTimeMillis();
    }

    public ShoppingItem(long id, String name, String category, double quantity, String unit,
                        double estimatedPrice, boolean isChecked, String storeName,
                        String aisle, int priority, String notes, boolean isRecurring) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.estimatedPrice = estimatedPrice;
        this.isChecked = isChecked;
        this.storeName = storeName;
        this.aisle = aisle;
        this.priority = priority;
        this.notes = notes;
        this.isRecurring = isRecurring;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public double getEstimatedPrice() { return estimatedPrice; }
    public void setEstimatedPrice(double estimatedPrice) { this.estimatedPrice = estimatedPrice; }

    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getAisle() { return aisle; }
    public void setAisle(String aisle) { this.aisle = aisle; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    /**
     * Calculates total estimated item cost by multiplying quantity by unit price.
     */
    public double getTotalCost() {
        return quantity * estimatedPrice;
    }

    /**
     * Returns a formatted quantity string (e.g., '2 pcs' or '1.5 kg').
     */
    public String getFormattedQuantity() {
        if (quantity == (long) quantity) {
            return String.format(Locale.US, "%d %s", (long) quantity, unit != null ? unit : "");
        } else {
            return String.format(Locale.US, "%.2f %s", quantity, unit != null ? unit : "");
        }
    }
}
