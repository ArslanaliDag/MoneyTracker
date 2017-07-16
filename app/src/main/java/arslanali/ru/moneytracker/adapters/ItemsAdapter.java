package arslanali.ru.moneytracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import arslanali.ru.moneytracker.pojo.Item;
import arslanali.ru.moneytracker.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private final List<Item> items = new ArrayList<>();
    // Simplified version HachMap in Java, optimized for android
    SparseBooleanArray selectedItems = new SparseBooleanArray();

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
        // We say that item our active
        holder.container.setActivated(selectedItems.get(position, false));
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

    // Dedicated or not allocated items
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        // We inform RecyclerView that the elements have changed
        notifyItemChanged(position);
    }

    // selected items
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    // unselect from items, finish the actionMode
    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    // selected items, return the selected items
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

   // remove selected items
   public Item remove(int pos) {
        final Item item = items.remove(pos);
        notifyItemRemoved(pos);
        return item;
    }

    // Inner class. Speed scrolling
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, price;
        private final View container; // RelativeLayout view container - item

        ItemViewHolder(View itemView) {
            super(itemView);
            // Without itemView.findViewById this parameter gives an error java.lang.NullPointerException
            container = itemView.findViewById(R.id.item_container);
            name = (TextView) itemView.findViewById(R.id.nameItem);
            price = (TextView) itemView.findViewById(R.id.priceItem);
        }
    }
}