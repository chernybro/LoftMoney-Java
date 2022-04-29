package com.chernybro.loftmoneyjava;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddItemActivity extends AppCompatActivity {

    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_NAME = "name";

    private TextInputEditText nameEditText;
    private TextInputEditText amountEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button addButton = findViewById(R.id.add_button);

        nameEditText = findViewById(R.id.et_name);
        amountEditText = findViewById(R.id.et_amount);

        setTextWatcher(nameEditText, addButton);
        setTextWatcher(amountEditText, addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String name = nameEditText.getText().toString();
                String price = amountEditText.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_NAME, name);
                    intent.putExtra(KEY_AMOUNT, price);

                    setResult(RESULT_OK, intent);

                    finish();
                }
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
                if (!nameEditText.getText().toString().isEmpty() && !amountEditText.getText().toString().isEmpty()) {
                    addButton.setEnabled(true);
                } else {
                    addButton.setEnabled(false);
                }
            }
        });
    }


}
