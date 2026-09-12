package com.yeaminsheikh.smartshopping.models;

import java.io.Serializable;

/**
 * Model representing a supermarket classification category
 * such as Produce, Dairy, Bakery, Pantry, Meat, or Household.
 */
public class Category implements Serializable {
    private long id;
    private String name;
    private String colorHex;
    private int itemCount;

    public Category() {}

    public Category(long id, String name, String colorHex) {
        this.id = id;
        this.name = name;
        this.colorHex = colorHex;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
}
