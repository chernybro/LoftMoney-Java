package com.chernybro.loftmoneyjava.presentation.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chernybro.loftmoneyjava.remote.AuthApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<String> _messageString = new MutableLiveData<>("");
    public LiveData<String> messageString = _messageString;

    private final MutableLiveData<String> _authToken = new MutableLiveData<>("");
    public LiveData<String> authToken = _authToken;

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    void makeLogin(AuthApi authApi, String userId) {
        compositeDisposable.add(authApi.makeLogin(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    _authToken.postValue(authResponse.getAuthToken());
                }, throwable -> {
                    _messageString.postValue(throwable.getLocalizedMessage());
                }));
    }
}