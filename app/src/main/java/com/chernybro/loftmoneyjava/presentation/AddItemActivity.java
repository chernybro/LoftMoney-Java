package com.chernybro.loftmoneyjava.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chernybro.loftmoneyjava.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddItemActivity extends AppCompatActivity {

    // Ниже описаны переменные ключи и коды
    // статичные чтобы можно было обращаться извне без создания объекта
    // final чтобы случайно где то не изменили
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NAME = "name";

    private TextInputEditText nameEditText;
    private TextInputEditText amountEditText;

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


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Получаем заполненные поля
                String name = nameEditText.getText().toString();
                String price = amountEditText.getText().toString();
                // Создаем интент в который ложим наш результат
                Intent intent = new Intent();
                intent.putExtra(KEY_NAME, name);
                intent.putExtra(KEY_AMOUNT, price);
                // Указываем что всё прошло хорошо, и интент с результатом
                setResult(RESULT_OK, intent);

                // Закрываем нашу активити, здесь мы всё сделали
                finish();
            }
        });
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
