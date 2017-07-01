package arslanali.ru.moneytracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arslanali.ru.moneytracker.pojo.Item;
import arslanali.ru.moneytracker.R;

public class ItemsRashodAdapter extends RecyclerView.Adapter<ItemsRashodAdapter.ItemViewHolder> {
    private final List<Item> items = new ArrayList<>();

    // add data in RW - HardCode
//    public ItemsRashodAdapter() {
//        getItems.add(new Item("Молоко", 35, Item.TYPE_EXPENSE));
//        getItems.add(new Item("Зубная щетка", 150, Item.TYPE_EXPENSE));
//        getItems.add(new Item("Сковородка Tefal с антипригарный покрытием", 500, Item.TYPE_EXPENSE));
//    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        // Insert data in variable. Cashed data
        final Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(List<Item> data) {
        items.addAll(data);
        notifyDataSetChanged();
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