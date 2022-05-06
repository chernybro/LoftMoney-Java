package com.chernybro.loftmoneyjava.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.R;
import com.chernybro.loftmoneyjava.presentation.models.MoneyItem;
import com.chernybro.loftmoneyjava.remote.MoneyApi;
import com.chernybro.loftmoneyjava.remote.models.money.MoneyItemResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// Фрагмент - часть пользовательского интерфейса, их может быть несколько на одном экране
// не может существовать без activity, он к ней прикрепляется
// из одного фрагмента можно перейти в другой, тогда организуется backstack
public class BudgetFragment extends Fragment {

    // Ниже описаны переменные ключи и коды
    // статичные чтобы можно было обращаться извне без создания объекта
    // final чтобы случайно где то не изменили
    private static final String COLOR_ID = "colorId";
    public static final String TYPE = "fragmentType";

    private String type;
    private MoneyApi moneyApi;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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

    public String getType() {
        return type;
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

        moneyApi = ((LoftApp) getActivity().getApplication()).moneyApi;

        // Находим контейнер для нашего списка
        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);


        // Проверяем, не забыли ли положить аргументы при создании фрагмента
        if (getArguments() != null) {
            // Устанавливаем цвет для который положили в аргументы при создании фрагмента
            mAdapter = new MoneyItemsAdapter(getArguments().getInt(COLOR_ID));
            type = getArguments().getString(TYPE);

        } else {
            //
            mAdapter = new MoneyItemsAdapter(R.color.purple_500);
        }
        // Устанавливаем адаптер для списка
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        loadItems();
    }

    private void loadItems() {
        Disposable disposable = moneyApi.getItems(type)
                // Подписываем функцию на новый поток
                .subscribeOn(Schedulers.io())
                // Указываем на каком потоке будем получать данные из функции
                .observeOn(AndroidSchedulers.mainThread())
                //
                .subscribe(moneyListResponse -> {
                    List<MoneyItem> moneyItems = new ArrayList<>();

                    for (MoneyItemResponse moneyRemoteItem : moneyListResponse.getItemList()) {
                        moneyItems.add(MoneyItem.getInstance(moneyRemoteItem));
                    }

                    mAdapter.setData(moneyItems);

                }, error -> {
                    Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT)
                            .show();
                });

        compositeDisposable.add(disposable);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
