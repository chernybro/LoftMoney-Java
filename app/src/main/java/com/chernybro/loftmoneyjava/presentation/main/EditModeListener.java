package com.chernybro.loftmoneyjava.presentation.main;

public interface EditModeListener {
    void onEditModeChanged(boolean status);
    void onCounterChanged(int newCount);
}