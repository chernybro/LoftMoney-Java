package com.chernybro.loftmoneyjava;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button button = findViewById(R.id.add_button);
        EditText textIncomes = findViewById(R.id.incomes_tv);
        textIncomes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(getApplicationContext(),
                        "Пора покормить кота!",
                        Toast.LENGTH_SHORT).show();  }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
    }
}
