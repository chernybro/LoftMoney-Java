package com.chernybro.loftmoneyjava.presentation.fragments.fragment_budget;


import com.chernybro.loftmoneyjava.presentation.fragments.fragment_budget.models.MoneyItem;

public interface MoneyItemAdapterClick {
    void onCellClick(MoneyItem moneyItem);
    void onLongCellClick(MoneyItem moneyItem);
}
