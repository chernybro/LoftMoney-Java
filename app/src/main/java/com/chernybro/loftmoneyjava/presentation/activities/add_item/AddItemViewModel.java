package com.chernybro.loftmoneyjava.presentation.activities.add_item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chernybro.loftmoneyjava.remote.MoneyApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemViewModel extends ViewModel{
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<String> _messageString = new MutableLiveData<>("");
    public LiveData<String> messageString = _messageString;

    private final MutableLiveData<Integer> _messageInt = new MutableLiveData<>(-1);
    public LiveData<Integer> messageInt = _messageInt;

    private final MutableLiveData<Boolean> _successAddItem = new MutableLiveData<>();
    public LiveData<Boolean> successAddItem = _successAddItem;


    public void addItem(MoneyApi moneyApi, String name, int price, String type, String token) {
        compositeDisposable.add(moneyApi.addItem(price, name, type, token)
                // Подписываем функцию на новый поток
                .subscribeOn(Schedulers.io())
                // Указываем на каком потоке будем получать данные из функции
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            _successAddItem.postValue(true);
                        },
                        error -> {
                            _successAddItem.postValue(false);
                            _messageString.postValue(error.getLocalizedMessage());
                        }
                ));
    }
}
