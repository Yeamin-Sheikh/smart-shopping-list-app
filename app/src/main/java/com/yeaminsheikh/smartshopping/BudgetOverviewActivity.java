package com.yeaminsheikh.smartshopping;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.yeaminsheikh.smartshopping.adapters.BudgetCategoryAdapter;
import com.yeaminsheikh.smartshopping.database.DatabaseHelper;
import com.yeaminsheikh.smartshopping.models.BudgetSummary;
import com.yeaminsheikh.smartshopping.utils.CurrencyFormatter;
import com.yeaminsheikh.smartshopping.utils.PreferenceManager;

import java.util.Locale;
import java.util.Map;

/**
 * Activity for reviewing budget health, cart vs purchased totals,
 * and category-level cost distributions.
 */
public class BudgetOverviewActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private PreferenceManager prefManager;

    private EditText etBudgetLimit;
    private Button btnUpdateBudget;
    private ProgressBar pbBudgetUsage;
    private TextView tvTotalEstimated;
    private TextView tvTotalChecked;
    private TextView tvRemainingBudget;
    private RecyclerView rvBudgetCategories;
    private BudgetCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_overview);

        dbHelper = new DatabaseHelper(this);
        prefManager = new PreferenceManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_budget);
        toolbar.setNavigationOnClickListener(v -> finish());

        etBudgetLimit = findViewById(R.id.et_budget_limit);
        btnUpdateBudget = findViewById(R.id.btn_update_budget);
        pbBudgetUsage = findViewById(R.id.pb_budget_usage);
        tvTotalEstimated = findViewById(R.id.tv_total_estimated);
        tvTotalChecked = findViewById(R.id.tv_total_checked);
        tvRemainingBudget = findViewById(R.id.tv_remaining_budget);
        rvBudgetCategories = findViewById(R.id.rv_budget_categories);

        adapter = new BudgetCategoryAdapter();
        rvBudgetCategories.setLayoutManager(new LinearLayoutManager(this));
        rvBudgetCategories.setAdapter(adapter);

        etBudgetLimit.setText(String.format(Locale.US, "%.2f", prefManager.getBudgetLimit()));

        btnUpdateBudget.setOnClickListener(v -> {
            try {
                double newLimit = Double.parseDouble(etBudgetLimit.getText().toString().trim());
                prefManager.setBudgetLimit(newLimit);
                Toast.makeText(this, "Budget target updated!", Toast.LENGTH_SHORT).show();
                loadBudgetData();
            } catch (Exception e) {
                Toast.makeText(this, "Please enter a valid numeric budget", Toast.LENGTH_SHORT).show();
            }
        });

        loadBudgetData();
    }

    private void loadBudgetData() {
        double limit = prefManager.getBudgetLimit();
        BudgetSummary summary = dbHelper.getBudgetSummary(limit);
        String currency = prefManager.getCurrency();

        tvTotalEstimated.setText(CurrencyFormatter.format(summary.getTotalEstimatedCost(), currency));
        tvTotalChecked.setText(CurrencyFormatter.format(summary.getCheckedEstimatedCost(), currency));
        tvRemainingBudget.setText(CurrencyFormatter.format(summary.getRemainingBudget(), currency));
        pbBudgetUsage.setProgress(summary.getBudgetUsagePercentage());

        Map<String, Double> spending = dbHelper.getCategorySpendingBreakdown();
        adapter.setData(spending, summary.getTotalEstimatedCost(), currency);
    }
}
