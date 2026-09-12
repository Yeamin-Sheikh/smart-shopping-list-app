package com.yeaminsheikh.smartshopping;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.yeaminsheikh.smartshopping.adapters.CategoryChipAdapter;
import com.yeaminsheikh.smartshopping.database.DatabaseHelper;

/**
 * Activity for adding and browsing custom supermarket department categories.
 */
public class CategoryManagerActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNewCategory;
    private Button btnAddCategory;
    private RecyclerView rvCategories;
    private CategoryChipAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        dbHelper = new DatabaseHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_categories);
        toolbar.setNavigationOnClickListener(v -> finish());

        etNewCategory = findViewById(R.id.et_new_category);
        btnAddCategory = findViewById(R.id.btn_add_category);
        rvCategories = findViewById(R.id.rv_manage_categories);

        adapter = new CategoryChipAdapter(null);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            String name = etNewCategory.getText().toString().trim();
            if (name.isEmpty()) {
                etNewCategory.setError("Enter category name");
                return;
            }
            long id = dbHelper.insertCategory(name, "#10B981");
            if (id > 0) {
                Toast.makeText(this, "Category '" + name + "' added!", Toast.LENGTH_SHORT).show();
                etNewCategory.setText("");
                refreshList();
            } else {
                Toast.makeText(this, "Category already exists!", Toast.LENGTH_SHORT).show();
            }
        });

        refreshList();
    }

    private void refreshList() {
        adapter.setCategories(dbHelper.getAllCategories());
    }
}
