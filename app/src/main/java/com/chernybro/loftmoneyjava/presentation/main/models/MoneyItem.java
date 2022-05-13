package com.chernybro.loftmoneyjava.presentation.main.models;

import com.chernybro.loftmoneyjava.remote.models.money.MoneyItemResponse;

// Класс для данных которые мы потом будем отображать на экрана
public class MoneyItem {
    private String name;
    private int amount;
    private boolean isSelected;
    private int id;

    public MoneyItem(final String name, final int amount, int id) {
        this.name = name;
        this.amount = amount;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static MoneyItem getInstance(MoneyItemResponse moneyRemoteItem) {
        return new MoneyItem(moneyRemoteItem.getName(), moneyRemoteItem.getPrice(), moneyRemoteItem.getId());
    }
}


