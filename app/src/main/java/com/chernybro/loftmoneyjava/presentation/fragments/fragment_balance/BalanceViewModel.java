package com.chernybro.loftmoneyjava.presentation.fragments.fragment_balance;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.presentation.fragments.fragment_balance.models.Balance;
import com.chernybro.loftmoneyjava.remote.MoneyApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BalanceViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<String> _messageString = new MutableLiveData<>("");
    public LiveData<String> messageString = _messageString;

    private final MutableLiveData<Integer> _messageInt = new MutableLiveData<>(-1);
    public LiveData<Integer> messageInt = _messageInt;

    private final MutableLiveData<Balance> _balance = new MutableLiveData<>();
    public LiveData<Balance> balance = _balance;


    public void loadBalance(MoneyApi moneyApi, SharedPreferences sharedPreferences) {
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        compositeDisposable.add(moneyApi.getBalance(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balanceResponse -> {
                    _balance.postValue(Balance.getInstance(balanceResponse));
                }, throwable -> {
                    _messageString.postValue(throwable.getLocalizedMessage());
                }));
    }
}
