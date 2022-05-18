package com.chernybro.loftmoneyjava.presentation.fragments;

public interface EditModeListener {
    void onEditModeChanged(boolean status);
    void onCounterChanged(int newCount);
}