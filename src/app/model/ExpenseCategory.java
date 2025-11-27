package app.model;

/**
 * Standardized expense categories for daily spending tracking.
 * Each category represents a common expense type with a display name.
 */
public enum ExpenseCategory {
    FOOD_DINING("Food & Dining"),
    GROCERIES("Groceries"),
    TRANSPORTATION("Transportation"),
    UTILITIES("Utilities"),
    RENT_MORTGAGE("Rent/Mortgage"),
    HEALTHCARE("Healthcare"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    EDUCATION("Education"),
    INSURANCE("Insurance"),
    PERSONAL_CARE("Personal Care"),
    TRAVEL("Travel"),
    SUBSCRIPTIONS("Subscriptions"),
    GIFTS_DONATIONS("Gifts & Donations"),
    OTHER("Other");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Get all expense category display names as an array.
     * @return Array of category display names
     */
    public static String[] getDisplayNames() {
        ExpenseCategory[] categories = values();
        String[] names = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            names[i] = categories[i].getDisplayName();
        }
        return names;
    }

    /**
     * Find category by display name.
     * @param displayName The display name to search for
     * @return The matching category, or OTHER if not found
     */
    public static ExpenseCategory fromDisplayName(String displayName) {
        for (ExpenseCategory category : values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return OTHER;
    }
}
