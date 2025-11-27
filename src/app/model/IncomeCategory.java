package app.model;

/**
 * Standardized income categories for monthly income tracking.
 * Each category represents a common income source with a display name.
 */
public enum IncomeCategory {
    SALARY("Salary"),
    FREELANCE("Freelance"),
    BUSINESS("Business Income"),
    INVESTMENT("Investment Returns"),
    RENTAL("Rental Income"),
    PENSION("Pension"),
    BONUS("Bonus"),
    GIFTS("Gifts"),
    REFUND("Refund"),
    OTHER("Other");

    private final String displayName;

    IncomeCategory(String displayName) {
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
     * Get all income category display names as an array.
     * @return Array of category display names
     */
    public static String[] getDisplayNames() {
        IncomeCategory[] categories = values();
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
    public static IncomeCategory fromDisplayName(String displayName) {
        for (IncomeCategory category : values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return OTHER;
    }
}
