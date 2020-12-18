package com.dermacon.securewebapp.controller.admin;

import java.util.ArrayList;
import java.util.List;

public class SelectedElements {

    /**
     * Selected Flatmate IDs in the checkbox form
     */
    private List<Long> checkedElements = new ArrayList<>();

    public List<Long> getCheckedElements() {
        return checkedElements;
    }

    public void setCheckedElements(List<Long> checkedElements) {
        this.checkedElements = checkedElements;
    }
}
