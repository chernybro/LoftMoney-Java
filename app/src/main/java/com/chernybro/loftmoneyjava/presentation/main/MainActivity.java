package com.chernybro.loftmoneyjava.presentation.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.chernybro.loftmoneyjava.R;
import com.chernybro.loftmoneyjava.presentation.add_item.AddItemActivity;
import com.chernybro.loftmoneyjava.presentation.main.fragment_budget.BudgetFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private int currentFragmentPosition = 0;

    private static final int incomeFragmentPosition = 0;
    private static final int expenseFragmentPosition = 1;

    // Один из методов жц активити
    // Здесь находим элементы из нашей верстки и навешиваем всяких setOnClickListener и подобное
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // устанавливаем нашу разметку для этой активити
        setContentView(R.layout.activity_main);

        // находим вью наших "вкладок"
        TabLayout tabLayout = findViewById(R.id.tabs);

        // инициализуруем пейджер, с помощью него будет листать фрагменты
        ViewPager2 viewPager = findViewById(R.id.viewpager);
        // Устанавливаем адаптер, он будет управлять списком наших фрагментов
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentFragmentPosition = position;
            }
        });

        // Находим кнопку
        FloatingActionButton addButton = findViewById(R.id.add_button);
        // Навешиваем на кнопку листенера для запуска активити добавления элемента в список


        Intent intent = new Intent(this, AddItemActivity.class);

        addButton.setOnClickListener(v -> {
            String type = "0";
            switch (currentFragmentPosition) {
                case incomeFragmentPosition:
                    type = "income";
                    break;
                case expenseFragmentPosition:
                    type = "expense";
                    break;
            }
            intent.putExtra(BudgetFragment.TYPE, type);
            startActivity(intent);
        });

        //Здесь просто перечислим наши вкладки
        final String[] fragmentsTitles = new String[]{getString(R.string.incomes), getString(R.string.expenses)};

        // Настраиваем наши вкладки, устанавливаем в них текст
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(fragmentsTitles[position]);
            }
        }).attach();


    }

    // Это обычный адаптер для управления списком, мы создавали адаптер раньше для RecyclerView
    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        // Указываем конструктор для нашего адаптера
        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        // Этот метод будет вызываться каждый раз когда мы будем переключать вкладки.
        // Тут мы указываем на какой фрагмент нам стоит переключиться на i-ой вкладке(счёт с нуля)
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case incomeFragmentPosition:
                    return BudgetFragment.newInstance(R.color.income_color, getString(R.string.income));
                case expenseFragmentPosition:
                    return BudgetFragment.newInstance(R.color.expense_color, getString(R.string.expense));
                case 2:
                    // Тут будет ещё фрагмент
                default:
                    return null;
            }
        }


        @Override
        public int getItemCount() {
            return 2; // здесь указываем сколько у нас фрагментов
        }
    }

}