package com.chernybro.loftmoneyjava;

import static android.app.Activity.RESULT_OK;

import static com.chernybro.loftmoneyjava.AddItemActivity.KEY_AMOUNT;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chernybro.loftmoneyjava.models.MoneyItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BudgetFragment extends Fragment {

    // Ниже описаны переменные ключи и коды
    // статичные чтобы можно было обращаться извне без создания объекта
    // final чтобы случайно где то не изменили
    private static final int ADD_ITEM_REQUEST_CODE = 100;
    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";

    // Это наш адаптер, он управляет списком (добавить элемент, удалить и т.п.)
    private MoneyItemsAdapter mAdapter;

    // Мы создали этот метод, чтобы при создании фрагмента задавать ему параметры
    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    // Метод жц фрагмента, тут мы указываем какую разметку должен показать фрагмент
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    // Ещё один метод жц фрагмента, здесь находим элементы разметки и сеттим всякие листенеры и т.п.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Находим кнопку
        FloatingActionButton addButton = view.findViewById(R.id.add_button);
        // Навешиваем на не листенера для запуска активити добавления элемента в список
        addButton.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(getActivity(), AddItemActivity.class), ADD_ITEM_REQUEST_CODE));

        // Находим контейнер для нашего списка
        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        // Проверяем, не забыли ли положить аргументы при создании фрагмента
        if (getArguments() != null) {
            // Устанавливаем цвет для который положили в аргументы при создании фрагмента
            mAdapter = new MoneyItemsAdapter(getArguments().getInt(COLOR_ID));
        } else {
            //
            mAdapter = new MoneyItemsAdapter(R.color.purple_500);
        }
        // Устанавливаем адаптер для списка
        recyclerView.setAdapter(mAdapter);


        // Тестовые данные
        mAdapter.addItem(new MoneyItem("Coffee", 300));
        mAdapter.addItem(new MoneyItem("Межконтинентальная баллистическая ракета", 5_000_000_00));

    }


    // Обрабатываем результат из AddItemActivity
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Проверяем код успеха и ADD_ITEM_REQUEST_CODE (по коду этого запроса понимаем что именно добавление обрабатываем в данном if)
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null)
                // Добавляем в адаптер элемент который только что заполняли в AddItemActivity
                mAdapter.addItem(
                        new MoneyItem(
                                data.getStringExtra(AddItemActivity.KEY_NAME),
                                Integer.parseInt(data.getStringExtra(KEY_AMOUNT))
                        )
                );
        }
    }
}
