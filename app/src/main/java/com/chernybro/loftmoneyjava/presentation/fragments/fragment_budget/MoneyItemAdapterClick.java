package com.chernybro.loftmoneyjava.presentation.main.fragment_budget;

import com.chernybro.loftmoneyjava.presentation.main.models.MoneyItem;

public interface MoneyItemAdapterClick {
    void onCellClick(MoneyItem moneyItem);
    void onLongCellClick(MoneyItem moneyItem);
}
