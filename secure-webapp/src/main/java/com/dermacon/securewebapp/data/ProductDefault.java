package com.dermacon.securewebapp.data;

public enum ProductDefault {

    KITCHEN_ROLL(new String[] {"kuechenrolle", "küchenrolle"}),
    TOILET_PAPER(new String[] {"klopapier"}),
    COSTUM_SUPPLY(new String[]{});

    private final String[] itemNames;

    ProductDefault(String[] itemNames) {
        this.itemNames = itemNames;
    }

    public String[] getItemNames() {
        return itemNames;
    }
}
