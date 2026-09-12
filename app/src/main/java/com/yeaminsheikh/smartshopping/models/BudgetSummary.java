package com.yeaminsheikh.smartshopping.models;

/**
 * Aggregates real-time financial and shopping list progress metrics:
 * Total items, checked items, estimated cart total, purchased total,
 * user budget ceiling, and remaining spending capacity.
 */
public class BudgetSummary {
    private int totalItemCount;
    private int checkedItemCount;
    private double totalEstimatedCost;
    private double checkedEstimatedCost;
    private double budgetLimit;

    public BudgetSummary(int totalItemCount, int checkedItemCount,
                         double totalEstimatedCost, double checkedEstimatedCost,
                         double budgetLimit) {
        this.totalItemCount = totalItemCount;
        this.checkedItemCount = checkedItemCount;
        this.totalEstimatedCost = totalEstimatedCost;
        this.checkedEstimatedCost = checkedEstimatedCost;
        this.budgetLimit = budgetLimit;
    }

    public int getTotalItemCount() { return totalItemCount; }
    public int getCheckedItemCount() { return checkedItemCount; }
    public double getTotalEstimatedCost() { return totalEstimatedCost; }
    public double getCheckedEstimatedCost() { return checkedEstimatedCost; }
    public double getBudgetLimit() { return budgetLimit; }

    public double getRemainingBudget() {
        return Math.max(0.0, budgetLimit - totalEstimatedCost);
    }

    public int getProgressPercentage() {
        if (totalItemCount == 0) return 0;
        return (int) Math.round(((double) checkedItemCount / totalItemCount) * 100);
    }

    public int getBudgetUsagePercentage() {
        if (budgetLimit <= 0) return 0;
        return (int) Math.min(100, Math.round((totalEstimatedCost / budgetLimit) * 100));
    }
}
