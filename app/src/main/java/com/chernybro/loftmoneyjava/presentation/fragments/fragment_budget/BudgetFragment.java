package com.chernybro.loftmoneyjava.presentation.fragments.fragment_budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.R;
import com.chernybro.loftmoneyjava.presentation.fragments.EditModeListener;
import com.chernybro.loftmoneyjava.presentation.fragments.fragment_budget.models.MoneyItem;

// Фрагмент - часть пользовательского интерфейса, их может быть несколько на одном экране
// не может существовать без activity, он к ней прикрепляется
// из одного фрагмента можно перейти в другой, тогда организуется backstack
public class BudgetFragment extends Fragment implements MoneyEditListener {

    // Ниже описаны переменные ключи и коды
    // статичные чтобы можно было обращаться извне без создания объекта
    // final чтобы случайно где то не изменили
    private static final String COLOR_ID = "colorId";
    public static final String TYPE = "fragmentType";

    private String type;
    private BudgetViewModel budgetViewModel;

    // Это наш адаптер, он управляет списком (добавить элемент, удалить и т.п.)
    private MoneyItemsAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    // Мы создали этот метод, чтобы при создании фрагмента задавать ему параметры
    public static BudgetFragment newInstance(final int colorId, final String typeId) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, typeId);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        // Проверяем, не забыли ли положить аргументы при создании фрагмента
        if (getArguments() != null) {
            // Устанавливаем цвет для который положили в аргументы при создании фрагмента
            adapter = new MoneyItemsAdapter(getArguments().getInt(COLOR_ID));
            type = getArguments().getString(TYPE);
        } else {
            adapter = new MoneyItemsAdapter(R.color.purple_500);
        }
        adapter.setMoneyCellAdapterClick(new MoneyItemAdapterClick() {
            @Override
            public void onCellClick(MoneyItem moneyItem) {
                if (budgetViewModel.isEditMode.getValue()) {
                    moneyItem.setSelected(!moneyItem.isSelected());
                    adapter.updateItem(moneyItem);
                    checkSelectedCount();
                }
            }

            @Override
            public void onLongCellClick(MoneyItem moneyItem) {
                if (!budgetViewModel.isEditMode.getValue()) {
                    moneyItem.setSelected(true);
                    adapter.updateItem(moneyItem);
                    budgetViewModel.setEditMode(true);
                    checkSelectedCount();
                }
            }
        });
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

        configureViewModel();

        configureViews(view);

    }

    private void configureViews(View view) {
        // Находим контейнер для нашего списка
        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadItems();
        });

        // Устанавливаем адаптер для списка
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        budgetViewModel.loadIncomes(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0),
                type
        );
    }

    private void configureViewModel() {

        budgetViewModel.isEditMode.observe(getViewLifecycleOwner(), isEditMode -> {
            if (getActivity() instanceof EditModeListener) {
                ((EditModeListener) getActivity()).onEditModeChanged(isEditMode);
            }
        });

        budgetViewModel.selectedCounter.observe(getViewLifecycleOwner(), newCount -> {
            if (getActivity() instanceof EditModeListener) {
                ((EditModeListener) getActivity()).onCounterChanged(newCount);
            }
        });

        budgetViewModel.removeItemDoneSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                loadItems();
            }
        });

        budgetViewModel.moneyItemsList.observe(getViewLifecycleOwner(), moneyItems -> {
            adapter.setData(moneyItems);
        });

        budgetViewModel.messageString.observe(getViewLifecycleOwner(), message -> {
            if (!message.equals("")) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        budgetViewModel.messageInt.observe(getViewLifecycleOwner(), message -> {
            if (message > 0) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        budgetViewModel.isRefreshing.observe(getViewLifecycleOwner(), isRefreshing -> {
            swipeRefreshLayout.setRefreshing(isRefreshing);
        });
    }

    @Override
    public void onClearEdit() {
        budgetViewModel.setEditMode(false);
        budgetViewModel.resetSelectedCounter();

        for (MoneyItem moneyItem : adapter.getMoneyItemList()) {
            if (moneyItem.isSelected()) {
                moneyItem.setSelected(false);
                adapter.updateItem(moneyItem);
            }
        }
    }

    @Override
    public void onClearSelectedClick() {
        budgetViewModel.setEditMode(false);
        budgetViewModel.resetSelectedCounter();
        // TODO: сделать удаление через сервер
        budgetViewModel.removeItem(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0),
                adapter.getMoneyItemList()
        );
        // adapter.deleteSelectedItems();
    }

    private void checkSelectedCount() {
        int selectedItemsCount = 0;
        for (MoneyItem moneyItem : adapter.getMoneyItemList()) {
            if (moneyItem.isSelected()) {
                selectedItemsCount++;
            }
        }

        budgetViewModel.setSelectedItemsCount(selectedItemsCount);
    }
}
