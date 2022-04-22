package com.chernybro.loftmoneyjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Hotkeys
// ctrl a - выделить всё
// ctrl c ctrl v ctrl x
// ctrl z

// ctrl d дублировать строку
// shift shift поиск по проекту
// ctrl f поиск по файлу
// ctrl shift f поиск по файлам в проекте
// tab - автозаполнение
// alt enter - помощь от студии
//
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent activity = new Intent(this, AddItemActivity.class);

        startActivity(activity);


    }
}