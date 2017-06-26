package arslanali.ru.moneytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView item = (RecyclerView) findViewById(R.id.items);
        item.setAdapter(new ItemAdapter());
    }

    // Inner class item in variable cash.
    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        final List<Item> items = new ArrayList<>();

        // add data in RW
        ItemAdapter() {
            items.add(new Item("Молоко", 35));
            items.add(new Item("Зубная щетка", 1500));
            items.add(new Item("Сковородка Tefal с антипригарный покрытием", 55));
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
            holder.price.setText(String.valueOf(item.price) + getString(R.string.ruble));;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // Inner class. Speed scrolling
    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name, price;

        public ItemViewHolder(View itemView) {
            super(itemView);

            // Without itemView.findViewById this parameter gives an error java.lang.NullPointerException
            name = (TextView) itemView.findViewById(R.id.nameItem);
            price = (TextView) itemView.findViewById(R.id.priceItem);
        }

        @Override
        public void onClick(View view) {
            view.setSelected(true);

        }
    }
}
