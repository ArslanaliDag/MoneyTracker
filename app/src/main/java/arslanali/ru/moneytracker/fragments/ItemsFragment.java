package arslanali.ru.moneytracker.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import arslanali.ru.moneytracker.AddItemActivity;
import arslanali.ru.moneytracker.LSApp;
import arslanali.ru.moneytracker.R;
import arslanali.ru.moneytracker.adapters.ItemsAdapter;
import arslanali.ru.moneytracker.api.LSApi;
import arslanali.ru.moneytracker.pojo.Item;
import retrofit2.Call;
import retrofit2.Callback;

public class ItemsFragment extends Fragment {

    // https://stackoverflow.com/questions/5425568/how-to-use-setarguments-and-getarguments-methods-in-fragments

    // loader state
    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;

    // include adapter
    private ItemsAdapter itemsAdapter = new ItemsAdapter();

    public static final String ARG_TYPE = "type";
    private String type;
    private LSApi api;
    private SwipeRefreshLayout refreshLayout;

    // Actions
    private GestureDetector gestureDetector;
    private ActionMode actionMode;

    // Interaction with the system call back
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // set menu action mode
//            actionMode.getMenuInflater().inflate(R.menu.action_mode, menu); error
            mode.getMenuInflater().inflate(R.menu.action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_remove:
                    new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle)
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.confirm_remove)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    for (int i = itemsAdapter.getSelectedItems().size() - 1; i >= 0; i--) {
                                        removeSelectedItem(itemsAdapter.remove(itemsAdapter.getSelectedItems().get(i)));
                                    }
                                    itemsAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    // remove selections when clicked on cancellation
                                    destroyActionMode();
                                }
                            })
                            .show();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            destroyActionMode();
        }
    };

    private void removeSelectedItem(final Item item) {

        Call<List<Item>> call = api.removeItem(item.getId());
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, retrofit2.Response<List<Item>> response) {
                try {
                    api.removeItem(item.getId()).execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
            }
        });
    }

    // destroy, there no selected items, finish the actionMode
    private void destroyActionMode() {
        actionMode.finish();
        actionMode = null;
        itemsAdapter.clearSelections();
    }

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

        // read, get incoming parameters of main activity
        type = getArguments().getString(ARG_TYPE);

        // refresh data
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.dark1, R.color.dark2, R.color.dark3);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems(type);
            }
        });

        // Init data RecyclerView getItems
        final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
        items.setAdapter(itemsAdapter);

        // catch long tab
        gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent motionEvent) {

                boolean hasCheckedItems = itemsAdapter.getItemCount() > 0;//Check if any items are already selected or not

                if (hasCheckedItems && actionMode == null) {
                    // there are some selected items, start the actionMode
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                    // find index selected item(find index selected view)
                    toggleSelection(motionEvent, items);
                }
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                // Select or unselect other items by clicking
                // find index selected item(find index selected view)
                toggleSelection(motionEvent, items);
                return super.onSingleTapConfirmed(motionEvent);
            }
        });

        // Necessary for gestures, any touch to the screen
        items.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        // Necessary to call our Application - LSApp
        api = ((LSApp) getActivity().getApplication()).initApi();

        // load expense getItems in create, view screen
        loadItems(type);
    }

    private void toggleSelection(MotionEvent e, RecyclerView items) {
        // without checking for a null, an error occurs NullPointerException
        if (actionMode != null) {
            // get selected item countl
            itemsAdapter.toggleSelection(items.getChildLayoutPosition(items.findChildViewUnder(e.getX(), e.getY())));
            // If no records are selected, then go back
            if (itemsAdapter.getSelectedItemCount() == 0) {
//                fabAdd.setVisibility(View.VISIBLE);
                actionMode.finish();
            } else {
                actionMode.setTitle(String.valueOf(itemsAdapter.getSelectedItemCount()) + " выбрано");
            }
            // hide FAB in select items
            // fabAdd.setVisibility(View.GONE);
        }
    }

    private void loadItems(final String payType) {
        // init Activity loader
        getLoaderManager().initLoader(LOADER_ITEMS, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(getContext()) {
                    @Override
                    public List<Item> loadInBackground() {
                        try {
                            // execute GET request, getting items_fragment rashod
                            return api.getItems(payType).execute().body();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
                // comes the list items_fragment after completion
                if (data == null) {
                    Toast.makeText(getContext(), R.string.errorLoadItems, Toast.LENGTH_SHORT).show();
                    // hide refresh layout
                    refreshLayout.setRefreshing(false);
                } else {
                    itemsAdapter.clear();
                    itemsAdapter.addAll(data); // insert data items_fragment in adapter and view user
                    // hide refresh layout
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), R.string.okLoadItems, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {

            }
        }).forceLoad();
    }

    private void addItem(final int price, final String name, final String type) {
        // init Activity loader
        getLoaderManager().initLoader(LOADER_ADD, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(getContext()) {
                    @Override
                    public List<Item> loadInBackground() {
                        try {
                            // execute POST request
                            return api.addItem(price, name, type).execute().body();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
                // comes the list items_fragment after completion
                if (data == null) {
                    //Toast.makeText(getContext(), R.string.errorAddItem, Toast.LENGTH_SHORT).show();
                    // hide refresh layout
                    refreshLayout.setRefreshing(false);
                } else {
                    itemsAdapter.clear();
                    itemsAdapter.addAll(data); // insert data items_fragment in adapter and view user
                    // hide refresh layout
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), R.string.okAddItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {

            }
        }).forceLoad();
    }

    // getting user added data in AddItemActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AddItemActivity.RC_ADD_ITEM) {
            Item item = (Item) data.getSerializableExtra(AddItemActivity.RESULT_ITEM);
            addItem(item.getPrice(), item.getName(), item.getType());
        }
    }
}
