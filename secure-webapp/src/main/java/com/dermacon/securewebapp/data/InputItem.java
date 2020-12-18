package com.dermacon.securewebapp.data;

/**
 * Very similar to the item class. Only difference, it is only necessary
 * to provide a room id instead of a full room object
 */
public class InputItem {

    private int itemCount = 1;
    private String itemName;

    public InputItem() {}

    public InputItem(int itemCount, String itemName) {
        this.itemCount = itemCount;
        this.itemName = itemName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "InputItem{" +
                "itemCount=" + itemCount +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    public boolean isValid() {
        return this.getItemCount() > 0
                && !this.itemName.isBlank()
                && !this.itemName.isEmpty();
    }
}
