package com.chernybro.loftmoneyjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.chernybro.loftmoneyjava.models.MoneyItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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
// shift delete - удалить строку
// ctrl alt навести красоту
public class MainActivity extends AppCompatActivity {

    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_NAME = "name";
    private static final int ADD_ITEM_REQUEST_CODE = 100;

    private MoneyItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(MainActivity.this, AddItemActivity.class), ADD_ITEM_REQUEST_CODE));

        RecyclerView recyclerView = findViewById(R.id.money_list);


        mAdapter = new MoneyItemsAdapter();
        recyclerView.setAdapter(mAdapter);

        mAdapter.addItem(new MoneyItem("Coffee", 300));
        mAdapter.addItem(new MoneyItem("Межконтинентальная баллистическая ракета", 5_000_000_00));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null)
                mAdapter.addItem(
                        new MoneyItem(
                                data.getStringExtra(KEY_NAME),
                                Integer.parseInt(data.getStringExtra(KEY_AMOUNT))
                        )
                );
        }
    }

}