package com.chernybro.loftmoneyjava;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

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

        //Здесь просто перечислим наши вкладки
        final String[] fragmentsTitles = new String[] { getString(R.string.incomes), getString(R.string.expenses) };

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
                case 0:
                    return BudgetFragment.newInstance(R.color.income_color, getString(R.string.incomes));
                case 1:
                    return BudgetFragment.newInstance(R.color.expense_color, getString(R.string.expenses));
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