package arslanali.ru.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import arslanali.ru.moneytracker.LSApp;
import arslanali.ru.moneytracker.api.LSApi;
import arslanali.ru.moneytracker.pojo.Item;
import arslanali.ru.moneytracker.R;
import arslanali.ru.moneytracker.adapters.ItemsDohodAdapter;
import arslanali.ru.moneytracker.adapters.ItemsRashodAdapter;

public class ItemsFragment extends Fragment {

    // loader state
    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;
    private static final int LOADER_REMOVE = 2;

    // include adapter
    private ItemsRashodAdapter rashodAdapter = new ItemsRashodAdapter();

    public static final String ARG_TYPE = "type";
    private String type;
    private LSApi api;

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
        if (Objects.equals(type, Item.TYPE_EXPENSE)) {
            // Init data rashod RecyclerView getItems
            final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
            items.setAdapter(rashodAdapter);

            // Necessary to call our Application - LSApp
            api = ((LSApp) getActivity().getApplication()).api();

            // load getItems in create, view screen
            LoadItems();

        } else if (Objects.equals(type, Item.TYPE_INCOME)) {
            // Init data dohod RecyclerView getItems
            final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
            items.setAdapter(new ItemsDohodAdapter());
        }
    }

    private void LoadItems() {
        // init Activity loader
        getLoaderManager().initLoader(LOADER_ITEMS, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(getContext()) {
                    @Override
                    public List<Item> loadInBackground() {
                        try {
                            // execute GET request, getting items rashod
                            return api.getItems(type).execute().body();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
                // comes the list items after completion
                if (data == null) {
                    Toast.makeText(getContext(), R.string.errorLoadItems, Toast.LENGTH_LONG).show();
                } else {
                    rashodAdapter.clear();
                    rashodAdapter.addAll(data); // insert data items in adapter and view user
                    Toast.makeText(getContext(), R.string.okLoadItems, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {

            }
        }).forceLoad();
    }
}
