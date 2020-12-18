package com.dermacon.securewebapp.data;

public class SelectedSupplyCategory {

    private SupplyCategory selected;


    public SelectedSupplyCategory() {}

    public SelectedSupplyCategory(SupplyCategory selected) {
        this.selected = selected;
    }

    public SupplyCategory getSelected() {
        return selected;
    }

    public void setSelected(SupplyCategory selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SelectedSupplyCategory{" +
                "selected=" + selected +
                '}';
    }
}
