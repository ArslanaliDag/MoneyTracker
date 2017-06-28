package arslanali.ru.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import arslanali.ru.moneytracker.Item;
import arslanali.ru.moneytracker.R;
import arslanali.ru.moneytracker.adapters.ItemsDohodAdapter;
import arslanali.ru.moneytracker.adapters.ItemsRashodAdapter;

public class ItemsFragment extends Fragment {

    public static final String ARG_TYPE = "type";
    private String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // read, get incoming parameters
        type = getArguments().getString(ARG_TYPE);

        // https://stackoverflow.com/questions/5425568/how-to-use-setarguments-and-getarguments-methods-in-fragments
        if (type == Item.TYPE_EXPENSE) {
            // Init data rashod RecyclerView items
            final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
            items.setAdapter(new ItemsRashodAdapter());

            // todo: use constant INCOME.
        } else if (type == Item.TYPE_INCOME) {
            // Init data dohod RecyclerView items
            final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
            items.setAdapter(new ItemsDohodAdapter());
        }
    }
}
