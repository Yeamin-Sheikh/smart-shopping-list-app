package com.yeaminsheikh.smartshopping.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages local user preferences like target budget ceiling,
 * preferred currency symbol, and sorting defaults.
 */
public class PreferenceManager {
    private static final String PREF_NAME = "smart_shopping_prefs";
    private static final String KEY_BUDGET_LIMIT = "pref_budget_limit";
    private static final String KEY_CURRENCY = "pref_currency";

    private final SharedPreferences prefs;

    public PreferenceManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public double getBudgetLimit() {
        return (double) prefs.getFloat(KEY_BUDGET_LIMIT, 250.0f);
    }

    public void setBudgetLimit(double limit) {
        prefs.edit().putFloat(KEY_BUDGET_LIMIT, (float) limit).apply();
    }

    public String getCurrency() {
        return prefs.getString(KEY_CURRENCY, "$");
    }

    public void setCurrency(String symbol) {
        prefs.edit().putString(KEY_CURRENCY, symbol).apply();
    }
}
