package com.yeaminsheikh.smartshopping;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yeaminsheikh.smartshopping.adapters.CategoryChipAdapter;
import com.yeaminsheikh.smartshopping.adapters.ShoppingListAdapter;
import com.yeaminsheikh.smartshopping.database.DatabaseHelper;
import com.yeaminsheikh.smartshopping.models.BudgetSummary;
import com.yeaminsheikh.smartshopping.models.ShoppingItem;
import com.yeaminsheikh.smartshopping.utils.CurrencyFormatter;
import com.yeaminsheikh.smartshopping.utils.PreferenceManager;

import java.util.List;

/**
 * Main Activity of Smart Shopping List Android App.
 * Houses the search bar, category chips filter, real-time trip progress,
 * and responsive items list with SQLite integration.
 */
public class MainActivity extends AppCompatActivity implements ShoppingListAdapter.OnItemClickListener {

    private DatabaseHelper dbHelper;
    private PreferenceManager prefManager;

    private MaterialToolbar toolbar;
    private EditText etSearch;
    private RecyclerView rvCategoryFilter;
    private RecyclerView rvShoppingItems;
    private TextView tvItemsProgress;
    private TextView tvCartTotal;
    private ProgressBar pbShoppingProgress;
    private LinearLayout layoutEmptyState;
    private FloatingActionButton fabAddItem;

    private ShoppingListAdapter shoppingAdapter;
    private CategoryChipAdapter categoryAdapter;

    private String currentCategoryFilter = "All";
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        prefManager = new PreferenceManager(this);

        initViews();
        setupToolbar();
        setupRecyclerViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCategoryChips();
        loadShoppingItems();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etSearch = findViewById(R.id.et_search);
        rvCategoryFilter = findViewById(R.id.rv_category_filter);
        rvShoppingItems = findViewById(R.id.rv_shopping_items);
        tvItemsProgress = findViewById(R.id.tv_items_progress);
        tvCartTotal = findViewById(R.id.tv_cart_total);
        pbShoppingProgress = findViewById(R.id.pb_shopping_progress);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        fabAddItem = findViewById(R.id.fab_add_item);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerViews() {
        // Items list setup
        shoppingAdapter = new ShoppingListAdapter(this);
        shoppingAdapter.setCurrencySymbol(prefManager.getCurrency());
        rvShoppingItems.setLayoutManager(new LinearLayoutManager(this));
        rvShoppingItems.setAdapter(shoppingAdapter);

        // Category chips setup
        categoryAdapter = new CategoryChipAdapter(categoryName -> {
            currentCategoryFilter = categoryName;
            loadShoppingItems();
        });
        rvCategoryFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategoryFilter.setAdapter(categoryAdapter);
    }

    private void setupListeners() {
        // Search text watcher
        etSearch.addTextChangedWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                loadShoppingItems();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // FAB to open Add Item Activity
        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });

        // Clicking budget summary card opens Budget Analytics
        findViewById(R.id.card_summary).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BudgetOverviewActivity.class);
            startActivity(intent);
        });
    }

    private void refreshCategoryChips() {
        categoryAdapter.setCategories(dbHelper.getAllCategories());
        categoryAdapter.setSelectedCategory(currentCategoryFilter);
    }

    private void loadShoppingItems() {
        List<ShoppingItem> items = dbHelper.getFilteredItems(currentSearchQuery, currentCategoryFilter);
        shoppingAdapter.setItems(items);

        // Update empty state visibility
        if (items.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvShoppingItems.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvShoppingItems.setVisibility(View.VISIBLE);
        }

        // Calculate and update progress & budget banner
        BudgetSummary summary = dbHelper.getBudgetSummary(prefManager.getBudgetLimit());
        String progressText = getString(R.string.items_progress, summary.getCheckedItemCount(), summary.getTotalItemCount());
        tvItemsProgress.setText(progressText);

        String currency = prefManager.getCurrency();
        String cartTotalText = getString(R.string.cart_total_prefix) + CurrencyFormatter.format(summary.getTotalEstimatedCost(), currency);
        tvCartTotal.setText(cartTotalText);

        pbShoppingProgress.setProgress(summary.getProgressPercentage());
    }

    @Override
    public void onItemClick(ShoppingItem item) {
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra("ITEM_ID", item.getId());
        intent.putExtra("ITEM_OBJ", item);
        startActivity(intent);
    }

    @Override
    public void onCheckToggled(ShoppingItem item, boolean isChecked) {
        dbHelper.toggleItemChecked(item.getId(), isChecked);
        loadShoppingItems();
    }

    @Override
    public void onDeleteClick(ShoppingItem item) {
        dbHelper.deleteItem(item.getId());
        Toast.makeText(this, item.getName() + " deleted", Toast.LENGTH_SHORT).show();
        loadShoppingItems();
    }

    // -------------------------------------------------------------
    // Options Menu
    // -------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, R.string.menu_budget);
        menu.add(0, 2, 1, R.string.menu_categories);
        menu.add(0, 3, 2, R.string.menu_clear_completed);
        menu.add(0, 4, 3, R.string.menu_reset_recurring);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            startActivity(new Intent(this, BudgetOverviewActivity.class));
            return true;
        } else if (id == 2) {
            startActivity(new Intent(this, CategoryManagerActivity.class));
            return true;
        } else if (id == 3) {
            showClearCompletedDialog();
            return true;
        } else if (id == 4) {
            dbHelper.resetAllRecurringStaples();
            Toast.makeText(this, "Recurring grocery staples refreshed for new trip!", Toast.LENGTH_LONG).show();
            loadShoppingItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showClearCompletedDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_clear_completed)
                .setMessage(R.string.confirm_clear_completed)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    int cleared = dbHelper.clearCheckedItems();
                    Toast.makeText(MainActivity.this, "Cleared " + cleared + " items", Toast.LENGTH_SHORT).show();
                    loadShoppingItems();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
