package com.chernybro.loftmoneyjava.presentation.activities.add_item;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.R;
import com.chernybro.loftmoneyjava.presentation.fragments.fragment_budget.BudgetFragment;
import com.chernybro.loftmoneyjava.remote.MoneyApi;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.disposables.CompositeDisposable;

public class AddItemActivity extends AppCompatActivity {

    private TextInputEditText nameEditText;
    private TextInputEditText amountEditText;
    private MoneyApi moneyApi;

    private AddItemViewModel addItemViewModel;

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

        addItemViewModel = new ViewModelProvider(this).get(AddItemViewModel.class);
        addItemViewModel.successAddItem.observe(this, isRequestSuccess -> {
            if (isRequestSuccess) {
                finish();
            }
        });

        addItemViewModel.messageString.observe(this, error -> {
            Toast.makeText(AddItemActivity.this, error, Toast.LENGTH_SHORT).show();
        });

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
                addItemViewModel.addItem(moneyApi, name, price, type, token);
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
