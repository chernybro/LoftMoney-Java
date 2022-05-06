package com.chernybro.loftmoneyjava.presentation.add_item;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.R;
import com.chernybro.loftmoneyjava.presentation.main.fragment_budget.BudgetFragment;
import com.chernybro.loftmoneyjava.remote.MoneyApi;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {

    // Ниже описаны переменные ключи и коды
    // статичные чтобы можно было обращаться извне без создания объекта
    // final чтобы случайно где то не изменили
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NAME = "name";

    private TextInputEditText nameEditText;
    private TextInputEditText amountEditText;
    private MoneyApi moneyApi;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button addButton = findViewById(R.id.add_button);

        nameEditText = findViewById(R.id.et_name);
        amountEditText = findViewById(R.id.et_amount);

        // Добавляем слежку за текстом
        setTextWatcher(nameEditText, addButton);
        setTextWatcher(amountEditText, addButton);

        // Находим тулбар (верхняя плашка на экране)
        Toolbar toolbar = findViewById(R.id.toolbar);
        // При нажатии на кнопочку "назад" вернёмся на предыдущий экран
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        moneyApi = ((LoftApp) getApplication()).moneyApi;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Получаем заполненные поля
                String name = nameEditText.getText().toString();
                int price = Integer.parseInt(amountEditText.getText().toString());
                Bundle arguments = getIntent().getExtras();
                String type = arguments.getString(BudgetFragment.TYPE);
                String token = getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.AUTH_KEY, "");
                Disposable disposable = moneyApi.addItem(price, name, type, token)
                        // Подписываем функцию на новый поток
                        .subscribeOn(Schedulers.io())
                        // Указываем на каком потоке будем получать данные из функции
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> finish(),
                                error -> Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT)
                                        .show()
                        );
                compositeDisposable.add(disposable);
                // Закрываем нашу активити, здесь мы всё сделали
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void setTextWatcher(TextInputEditText editText, Button addButton) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // После того как пользователь закончил вводить текст, проверяем, не осталось ли поле пустым
                // Если поле не пустое, включаем нашу кнопку(выключена по умолчанию в разметке)
                if (!nameEditText.getText().toString().isEmpty() && !amountEditText.getText().toString().isEmpty()) {
                    addButton.setEnabled(true);
                } else {
                    addButton.setEnabled(false);
                }
            }
        });
    }


}
