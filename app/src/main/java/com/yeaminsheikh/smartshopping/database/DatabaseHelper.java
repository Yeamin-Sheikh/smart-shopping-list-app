package com.yeaminsheikh.smartshopping.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yeaminsheikh.smartshopping.models.BudgetSummary;
import com.yeaminsheikh.smartshopping.models.Category;
import com.yeaminsheikh.smartshopping.models.ShoppingItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQLiteOpenHelper implementation for Smart Shopping List.
 * Handles local relational persistence for shopping items,
 * custom categories, and trip logs without requiring external cloud accounts.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_shopping_list.db";
    private static final int DATABASE_VERSION = 1;

    // Table: shopping_items
    public static final String TABLE_ITEMS = "shopping_items";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_CATEGORY = "category";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_PRICE = "estimated_price";
    public static final String COL_CHECKED = "is_checked";
    public static final String COL_STORE = "store_name";
    public static final String COL_AISLE = "aisle";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_NOTES = "notes";
    public static final String COL_RECURRING = "is_recurring";
    public static final String COL_CREATED_AT = "created_at";

    // Table: categories
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COL_CAT_ID = "id";
    public static final String COL_CAT_NAME = "name";
    public static final String COL_CAT_COLOR = "color_hex";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create shopping items table
        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_CATEGORY + " TEXT, "
                + COL_QUANTITY + " REAL DEFAULT 1.0, "
                + COL_UNIT + " TEXT DEFAULT 'pcs', "
                + COL_PRICE + " REAL DEFAULT 0.0, "
                + COL_CHECKED + " INTEGER DEFAULT 0, "
                + COL_STORE + " TEXT, "
                + COL_AISLE + " TEXT, "
                + COL_PRIORITY + " INTEGER DEFAULT 2, "
                + COL_NOTES + " TEXT, "
                + COL_RECURRING + " INTEGER DEFAULT 0, "
                + COL_CREATED_AT + " INTEGER"
                + ");";
        db.execSQL(createItemsTable);

        // Create categories table
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " ("
                + COL_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CAT_NAME + " TEXT UNIQUE NOT NULL, "
                + COL_CAT_COLOR + " TEXT NOT NULL"
                + ");";
        db.execSQL(createCategoriesTable);

        // Seed default grocery categories
        seedDefaultCategories(db);
        // Seed default popular grocery staples for immediate first-run delight
        seedSampleItems(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    private void seedDefaultCategories(SQLiteDatabase db) {
        String[][] defaults = {
                {"Produce", "#10B981"},
                {"Dairy & Eggs", "#3B82F6"},
                {"Bakery", "#F59E0B"},
                {"Meat & Seafood", "#EF4444"},
                {"Pantry", "#8B5CF6"},
                {"Frozen", "#06B6D4"},
                {"Beverages", "#EC4899"},
                {"Household", "#64748B"}
        };
        for (String[] cat : defaults) {
            ContentValues cv = new ContentValues();
            cv.put(COL_CAT_NAME, cat[0]);
            cv.put(COL_CAT_COLOR, cat[1]);
            db.insert(TABLE_CATEGORIES, null, cv);
        }
    }

    private void seedSampleItems(SQLiteDatabase db) {
        Object[][] sampleItems = {
                {"Organic Hass Avocados", "Produce", 4.0, "pcs", 1.25, 0, "Trader Joe's", "Aisle 1", 1, "Firm for weekend salads", 1},
                {"Oat Milk (Barista Blend)", "Dairy & Eggs", 2.0, "cartons", 4.49, 0, "Whole Foods", "Refrigerated Aisle", 2, "No added sugars", 1},
                {"Wild Sockeye Salmon", "Meat & Seafood", 1.5, "lbs", 12.99, 0, "Costco", "Seafood Counter", 1, "Fresh catch", 0},
                {"Sourdough Boule", "Bakery", 1.0, "loaf", 5.50, 1, "Local Bakery", "Bakery Rack", 2, "Sliced thick", 0},
                {"Extra Virgin Olive Oil", "Pantry", 1.0, "bottle", 14.99, 0, "Costco", "Aisle 8", 3, "Cold pressed Greek", 0}
        };

        for (Object[] item : sampleItems) {
            ContentValues cv = new ContentValues();
            cv.put(COL_NAME, (String) item[0]);
            cv.put(COL_CATEGORY, (String) item[1]);
            cv.put(COL_QUANTITY, (Double) item[2]);
            cv.put(COL_UNIT, (String) item[3]);
            cv.put(COL_PRICE, (Double) item[4]);
            cv.put(COL_CHECKED, (Integer) item[5]);
            cv.put(COL_STORE, (String) item[6]);
            cv.put(COL_AISLE, (String) item[7]);
            cv.put(COL_PRIORITY, (Integer) item[8]);
            cv.put(COL_NOTES, (String) item[9]);
            cv.put(COL_RECURRING, (Integer) item[10]);
            cv.put(COL_CREATED_AT, System.currentTimeMillis());
            db.insert(TABLE_ITEMS, null, cv);
        }
    }

    // -------------------------------------------------------------
    // Item CRUD Operations
    // -------------------------------------------------------------

    public long insertItem(ShoppingItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, item.getName());
        cv.put(COL_CATEGORY, item.getCategory());
        cv.put(COL_QUANTITY, item.getQuantity());
        cv.put(COL_UNIT, item.getUnit());
        cv.put(COL_PRICE, item.getEstimatedPrice());
        cv.put(COL_CHECKED, item.isChecked() ? 1 : 0);
        cv.put(COL_STORE, item.getStoreName());
        cv.put(COL_AISLE, item.getAisle());
        cv.put(COL_PRIORITY, item.getPriority());
        cv.put(COL_NOTES, item.getNotes());
        cv.put(COL_RECURRING, item.isRecurring() ? 1 : 0);
        cv.put(COL_CREATED_AT, item.getCreatedAt() > 0 ? item.getCreatedAt() : System.currentTimeMillis());
        return db.insert(TABLE_ITEMS, null, cv);
    }

    public int updateItem(ShoppingItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, item.getName());
        cv.put(COL_CATEGORY, item.getCategory());
        cv.put(COL_QUANTITY, item.getQuantity());
        cv.put(COL_UNIT, item.getUnit());
        cv.put(COL_PRICE, item.getEstimatedPrice());
        cv.put(COL_CHECKED, item.isChecked() ? 1 : 0);
        cv.put(COL_STORE, item.getStoreName());
        cv.put(COL_AISLE, item.getAisle());
        cv.put(COL_PRIORITY, item.getPriority());
        cv.put(COL_NOTES, item.getNotes());
        cv.put(COL_RECURRING, item.isRecurring() ? 1 : 0);
        return db.update(TABLE_ITEMS, cv, COL_ID + " = ?", new String[]{String.valueOf(item.getId())});
    }

    public int toggleItemChecked(long id, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CHECKED, checked ? 1 : 0);
        return db.update(TABLE_ITEMS, cv, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int clearCheckedItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete all checked items that are NOT marked as recurring
        int deleted = db.delete(TABLE_ITEMS, COL_CHECKED + " = 1 AND " + COL_RECURRING + " = 0", null);
        // For recurring items that were checked, reset them back to unchecked for next week
        ContentValues cv = new ContentValues();
        cv.put(COL_CHECKED, 0);
        int reset = db.update(TABLE_ITEMS, cv, COL_CHECKED + " = 1 AND " + COL_RECURRING + " = 1", null);
        return deleted + reset;
    }

    public void resetAllRecurringStaples() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CHECKED, 0);
        db.update(TABLE_ITEMS, cv, COL_RECURRING + " = 1", null);
    }

    public List<ShoppingItem> getFilteredItems(String query, String categoryFilter) {
        List<ShoppingItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_ITEMS + " WHERE 1=1");
        List<String> args = new ArrayList<>();

        if (categoryFilter != null && !categoryFilter.isEmpty() && !categoryFilter.equalsIgnoreCase("All")) {
            sql.append(" AND ").append(COL_CATEGORY).append(" = ?");
            args.add(categoryFilter);
        }

        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND (").append(COL_NAME).append(" LIKE ? OR ")
               .append(COL_STORE).append(" LIKE ? OR ")
               .append(COL_AISLE).append(" LIKE ?)");
            String wildcard = "%" + query.trim() + "%";
            args.add(wildcard);
            args.add(wildcard);
            args.add(wildcard);
        }

        // Order: unchecked items first, high priority first, then id desc
        sql.append(" ORDER BY ").append(COL_CHECKED).append(" ASC, ")
           .append(COL_PRIORITY).append(" ASC, ")
           .append(COL_ID).append(" DESC");

        Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ShoppingItem item = new ShoppingItem();
                item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)));
                item.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY)));
                item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)));
                item.setEstimatedPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE)));
                item.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CHECKED)) == 1);
                item.setStoreName(cursor.getString(cursor.getColumnIndexOrThrow(COL_STORE)));
                item.setAisle(cursor.getString(cursor.getColumnIndexOrThrow(COL_AISLE)));
                item.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRIORITY)));
                item.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTES)));
                item.setRecurring(cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECURRING)) == 1);
                item.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT)));
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return items;
    }

    public BudgetSummary getBudgetSummary(double budgetLimit) {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalCount = 0;
        int checkedCount = 0;
        double totalCost = 0.0;
        double checkedCost = 0.0;

        String query = "SELECT " + COL_CHECKED + ", " + COL_QUANTITY + ", " + COL_PRICE + " FROM " + TABLE_ITEMS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                boolean checked = cursor.getInt(0) == 1;
                double qty = cursor.getDouble(1);
                double price = cursor.getDouble(2);
                double itemCost = qty * price;

                totalCount++;
                totalCost += itemCost;
                if (checked) {
                    checkedCount++;
                    checkedCost += itemCost;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return new BudgetSummary(totalCount, checkedCount, totalCost, checkedCost, budgetLimit);
    }

    public Map<String, Double> getCategorySpendingBreakdown() {
        Map<String, Double> spending = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + COL_CATEGORY + ", SUM(" + COL_QUANTITY + " * " + COL_PRICE + ") "
                + "FROM " + TABLE_ITEMS + " GROUP BY " + COL_CATEGORY;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String cat = cursor.getString(0);
                double amt = cursor.getDouble(1);
                if (cat == null || cat.isEmpty()) cat = "Uncategorized";
                spending.put(cat, amt);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return spending;
    }

    // -------------------------------------------------------------
    // Category Operations
    // -------------------------------------------------------------

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY " + COL_CAT_NAME + " ASC", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Category c = new Category();
                c.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CAT_ID)));
                c.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_NAME)));
                c.setColorHex(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_COLOR)));
                list.add(c);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public long insertCategory(String name, String colorHex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CAT_NAME, name);
        cv.put(COL_CAT_COLOR, colorHex);
        return db.insert(TABLE_CATEGORIES, null, cv);
    }

    public int deleteCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CATEGORIES, COL_CAT_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
