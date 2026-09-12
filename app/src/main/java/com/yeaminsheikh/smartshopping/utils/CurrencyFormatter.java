package com.yeaminsheikh.smartshopping.utils;

import java.util.Locale;

/**
 * Utility for rendering user-friendly localized currency strings.
 */
public class CurrencyFormatter {
    public static String format(double amount, String currencySymbol) {
        if (currencySymbol == null || currencySymbol.isEmpty()) {
            currencySymbol = "$";
        }
        return String.format(Locale.US, "%s%.2f", currencySymbol, amount);
    }
}
