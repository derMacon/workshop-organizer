package com.dermacon.securewebapp.data;

public enum  SupplyCategory {
    KITCHEN_SUPPLY ("Küchenutensil"),
    BATHROOM_SUPPLY ("Badausstattung"),
    CLEANING_SUPPLY ("Putzutensil"),
    CUSTOM_SUPPLY ("Eigenbedarf");

    private final String categoryName;

    SupplyCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
