package com.chernybro.loftmoneyjava.presentation.fragments.fragment_budget;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.presentation.fragments.fragment_budget.models.MoneyItem;
import com.chernybro.loftmoneyjava.remote.MoneyApi;
import com.chernybro.loftmoneyjava.remote.models.money.MoneyItemResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BudgetViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<MoneyItem>> _moneyItemsList = new MutableLiveData<>();
    public LiveData<List<MoneyItem>> moneyItemsList = _moneyItemsList;

    private final MutableLiveData<String> _messageString = new MutableLiveData<>("");
    public LiveData<String> messageString = _messageString;

    private final MutableLiveData<Integer> _messageInt = new MutableLiveData<>(-1);
    public LiveData<Integer> messageInt = _messageInt;

    private final MutableLiveData<Boolean> _isRefreshing = new MutableLiveData<>(false);
    public LiveData<Boolean> isRefreshing = _isRefreshing;

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void loadIncomes(MoneyApi moneyApi, SharedPreferences sharedPreferences, String type) {
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        compositeDisposable.add(moneyApi.getItems(type, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moneyRemoteItems -> {
                    List<MoneyItem> moneyItems = new ArrayList<>();

                    for (MoneyItemResponse moneyRemoteItem : moneyRemoteItems) {
                        moneyItems.add(MoneyItem.getInstance(moneyRemoteItem));
                    }

                    _moneyItemsList.postValue(moneyItems);
                    _isRefreshing.postValue(false);
                }, throwable -> {
                    _messageString.postValue(throwable.getLocalizedMessage());
                    _isRefreshing.postValue(false);
                }));
    }
}