package com.yeaminsheikh.smartshopping;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yeaminsheikh.smartshopping.database.DatabaseHelper;
import com.yeaminsheikh.smartshopping.models.Category;
import com.yeaminsheikh.smartshopping.models.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for creating a new shopping item or editing an existing one.
 */
public class AddEditItemActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ShoppingItem existingItem = null;

    private TextView tvTitle;
    private EditText etName;
    private Spinner spCategory;
    private EditText etQuantity;
    private EditText etUnit;
    private EditText etPrice;
    private EditText etStore;
    private EditText etAisle;
    private RadioGroup rgPriority;
    private EditText etNotes;
    private CheckBox cbRecurring;
    private Button btnSave;

    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupCategorySpinner();

        if (getIntent().hasExtra("ITEM_OBJ")) {
            existingItem = (ShoppingItem) getIntent().getSerializableExtra("ITEM_OBJ");
            populateFields(existingItem);
        }

        btnSave.setOnClickListener(v -> saveItem());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_form_title);
        etName = findViewById(R.id.et_name);
        spCategory = findViewById(R.id.sp_category);
        etQuantity = findViewById(R.id.et_quantity);
        etUnit = findViewById(R.id.et_unit);
        etPrice = findViewById(R.id.et_estimated_price);
        etStore = findViewById(R.id.et_store);
        etAisle = findViewById(R.id.et_aisle);
        rgPriority = findViewById(R.id.rg_priority);
        etNotes = findViewById(R.id.et_notes);
        cbRecurring = findViewById(R.id.cb_recurring);
        btnSave = findViewById(R.id.btn_save);
    }

    private void setupCategorySpinner() {
        categoryList = dbHelper.getAllCategories();
        List<String> names = new ArrayList<>();
        for (Category c : categoryList) {
            names.add(c.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
        spCategory.setAdapter(adapter);
    }

    private void populateFields(ShoppingItem item) {
        tvTitle.setText(R.string.title_edit_item);
        etName.setText(item.getName());
        etQuantity.setText(String.valueOf(item.getQuantity()));
        etUnit.setText(item.getUnit());
        etPrice.setText(String.valueOf(item.getEstimatedPrice()));
        etStore.setText(item.getStoreName() != null ? item.getStoreName() : "");
        etAisle.setText(item.getAisle() != null ? item.getAisle() : "");
        etNotes.setText(item.getNotes() != null ? item.getNotes() : "");
        cbRecurring.setChecked(item.isRecurring());

        // Select category in spinner
        if (item.getCategory() != null) {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getName().equalsIgnoreCase(item.getCategory())) {
                    spCategory.setSelection(i);
                    break;
                }
            }
        }

        // Priority
        if (item.getPriority() == 1) {
            rgPriority.check(R.id.rb_priority_high);
        } else if (item.getPriority() == 3) {
            rgPriority.check(R.id.rb_priority_low);
        } else {
            rgPriority.check(R.id.rb_priority_med);
        }
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Item name cannot be empty");
            etName.requestFocus();
            return;
        }

        String category = (String) spCategory.getSelectedItem();
        double quantity = 1.0;
        try {
            quantity = Double.parseDouble(etQuantity.getText().toString().trim());
        } catch (Exception ignored) {}

        String unit = etUnit.getText().toString().trim();
        if (unit.isEmpty()) unit = "pcs";

        double price = 0.0;
        try {
            price = Double.parseDouble(etPrice.getText().toString().trim());
        } catch (Exception ignored) {}

        String store = etStore.getText().toString().trim();
        String aisle = etAisle.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        boolean recurring = cbRecurring.isChecked();

        int priority = 2; // medium
        int checkedRb = rgPriority.getCheckedRadioButtonId();
        if (checkedRb == R.id.rb_priority_high) priority = 1;
        else if (checkedRb == R.id.rb_priority_low) priority = 3;

        if (existingItem != null) {
            existingItem.setName(name);
            existingItem.setCategory(category);
            existingItem.setQuantity(quantity);
            existingItem.setUnit(unit);
            existingItem.setEstimatedPrice(price);
            existingItem.setStoreName(store);
            existingItem.setAisle(aisle);
            existingItem.setPriority(priority);
            existingItem.setNotes(notes);
            existingItem.setRecurring(recurring);
            dbHelper.updateItem(existingItem);
            Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();
        } else {
            ShoppingItem newItem = new ShoppingItem(0, name, category, quantity, unit, price, false, store, aisle, priority, notes, recurring);
            dbHelper.insertItem(newItem);
            Toast.makeText(this, "Item added to list!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
