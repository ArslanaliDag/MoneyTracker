package arslanali.ru.moneytracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arslanali.ru.moneytracker.Item;
import arslanali.ru.moneytracker.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private final List<Item> items = new ArrayList<>();

    // add data in RW
    public ItemsAdapter() {
        items.add(new Item("Молоко", 35));
        items.add(new Item("Зубная щетка", 1500));
        items.add(new Item("Сковородка Tefal с антипригарный покрытием", 500));
        items.add(new Item("Баранина", 250));
        items.add(new Item("Яблоки", 30));
        items.add(new Item("Масло", 20));
        items.add(new Item("Макароны", 120));
        items.add(new Item("Текст для проверки и еще раз текст для проверки", 10020));
        items.add(new Item("Финики", 150));
        items.add(new Item("Apple IPad", 20000));
        items.add(new Item("Вишня", 100));
        items.add(new Item("Сок", 10));
        items.add(new Item("Рыба", 110));
        items.add(new Item("Смартфон Galaxy S8", 40000));
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        // Insert data in variable. Cashed data
        final Item item = items.get(position);
        holder.name.setText(item.name);
        holder.price.setText(String.valueOf(item.price));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Inner class. Speed scrolling
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, price;

        ItemViewHolder(View itemView) {
            super(itemView);
            // Without itemView.findViewById this parameter gives an error java.lang.NullPointerException
            name = (TextView) itemView.findViewById(R.id.nameItem);
            price = (TextView) itemView.findViewById(R.id.priceItem);
        }
    }
}