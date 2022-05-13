package com.chernybro.loftmoneyjava.presentation.main.models;

import com.chernybro.loftmoneyjava.remote.models.money.MoneyItemResponse;

// Класс для данных которые мы потом будем отображать на экрана
public class MoneyItem {
    private String name;
    private int amount;
    private boolean isSelected;

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


    public static MoneyItem getInstance(MoneyItemResponse moneyRemoteItem) {
        return new MoneyItem(moneyRemoteItem.getName(), moneyRemoteItem.getPrice());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}


