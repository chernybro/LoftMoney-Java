package com.chernybro.loftmoneyjava.models;

// Класс для данных которые мы потом будем отображать на экрана
public class MoneyItem {
    private String name;
    private int amount;

    public MoneyItem(final String name, final int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }
}
