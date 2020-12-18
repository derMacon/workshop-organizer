package com.dermacon.securewebapp.controller.groceryList;

import java.util.ArrayList;
import java.util.List;

/**
 * Injected into the thymeleaf template to save those items that are
 * checked by the checkbox form
 */
public class SelectedItems {

    /**
     * Selected items in the checkbox form
     */
    private List<Long> checkedItems = new ArrayList<>();

    public List<Long> getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(List<Long> checkedItems) {
        this.checkedItems = checkedItems;
    }
}
