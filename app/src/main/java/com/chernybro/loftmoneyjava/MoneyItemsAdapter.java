package com.chernybro.loftmoneyjava;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chernybro.loftmoneyjava.models.MoneyItem;

import java.util.ArrayList;
import java.util.List;

// Это адаптер. Он нужен для RecyclerView.
// Он управляет списком. Т.е. добавляет, удаляем элементы.
public class MoneyItemsAdapter extends RecyclerView.Adapter<MoneyItemsAdapter.MoneyViewHolder> {
    // Этим списком управляет адаптер
    private final List<MoneyItem> itemsList = new ArrayList<>();
    // Цвет "стоимости" дохода расхода
    private final int colorId;

    public MoneyItemsAdapter(int colorId) {
        this.colorId = colorId;
    }

    // "Показываем" адаптеру как должна выглядить разметка одного элемента списка
    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_money, null);

        return new MoneyViewHolder(itemView, colorId);
    }

    // Вносим данные из кода в элемент, который будет отображаться на экрана
    @Override
    public void onBindViewHolder(@NonNull final MoneyViewHolder holder, final int position) {
        // Вносим данные из кода в элемент, который будет отображаться на экрана
        holder.bindItem(itemsList.get(position));
    }

    // Просто добавляем один элемент путем добавления этого элемента в список, которым управляет адаптер
    public void addItem(MoneyItem item) {
        itemsList.add(item);
        // Говорим адаптеру, что список изменился, чтобы он отобразил актуальный список
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // Класс, который содержит элементы разметки нашего одного элемента,
    // в нем устанавливаются реальные данные
    static class MoneyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;
        private final TextView mAmountView;

        public MoneyViewHolder(@NonNull final View itemView, int colorId) {
            super(itemView);
            // Находим элементы
            mTextView = itemView.findViewById(R.id.tv_name);
            mAmountView = itemView.findViewById(R.id.tv_amount);
            // Устанавливаем цвет для значения дохода или расхода
            mAmountView.setTextColor(ContextCompat.getColor(mAmountView.getContext(), colorId));
        }

        public void bindItem(@NonNull final MoneyItem item) {
            // Вносим данные в элементы
            mTextView.setText(item.getName());
            mAmountView.setText(String.valueOf(item.getAmount()));
        }
    }
}