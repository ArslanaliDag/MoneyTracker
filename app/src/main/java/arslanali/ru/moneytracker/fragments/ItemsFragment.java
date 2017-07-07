package arslanali.ru.moneytracker.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import arslanali.ru.moneytracker.AddItemActivity;
import arslanali.ru.moneytracker.LSApp;
import arslanali.ru.moneytracker.api.LSApi;
import arslanali.ru.moneytracker.pojo.Item;
import arslanali.ru.moneytracker.R;
import arslanali.ru.moneytracker.adapters.ItemsAdapter;

public class ItemsFragment extends Fragment {

    // loader state
    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;
    private static final int LOADER_REMOVE = 2;

    // include adapter
    private ItemsAdapter itemsAdapter = new ItemsAdapter();

    public static final String ARG_TYPE = "type";
    private String type;
    private LSApi api;
    private FloatingActionButton fabAdd;
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
                                    for (Integer selecedItemId : itemsAdapter.getSelectedItems())
                                        removeSelectedItem(selecedItemId);
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

    // destroy, there no selected items, finish the actionMode
    private void destroyActionMode() {
        actionMode.finish();
        actionMode = null;
        fabAdd.setVisibility(View.VISIBLE);
        itemsAdapter.clearSelections();
    }

    // remove selected item in server
    private void removeSelectedItem(Integer itemId) {
        Log.i("ID_ITEM", String.valueOf(itemId) + " на удаление.");
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
        // set color in refresh
        refreshLayout.setColorSchemeResources(R.color.dark1, R.color.dark2, R.color.dark3);

        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, AddItemActivity.RC_ADD_ITEM);
            }
        });

        // https://stackoverflow.com/questions/5425568/how-to-use-setarguments-and-getarguments-methods-in-fragments
        if (Objects.equals(type, Item.TYPE_EXPENSE)) {
            // Init data rashod RecyclerView getItems
            final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
            items.setAdapter(itemsAdapter);

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadItems(type);
                }
            });

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
            api = ((LSApp) getActivity().getApplication()).api();

            // load expense getItems in create, view screen
            loadItems(type);

        } else if (Objects.equals(type, Item.TYPE_INCOME)) {
            // Init data dohod RecyclerView getItems
            final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
            items.setAdapter(itemsAdapter);

            // Necessary to call our Application - LSApp
            api = ((LSApp) getActivity().getApplication()).api();

            // load income getItems in create, view screen
            loadItems(type);
        }
    }

    private void toggleSelection(MotionEvent e, RecyclerView items) {
        // without checking for a null, an error occurs NullPointerException
        if (actionMode != null) {
            // get selected item countl
            itemsAdapter.toggleSelection(items.getChildLayoutPosition(items.findChildViewUnder(e.getX(), e.getY())));
            // If no records are selected, then go back
            if (itemsAdapter.getSelectedItemCount() == 0) {
                actionMode.finish();
            } else {
                actionMode.setTitle(String.valueOf(itemsAdapter.getSelectedItemCount()) + " выбрано");
            }
            // hide FAB in select items
            fabAdd.setVisibility(View.GONE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AddItemActivity.RC_ADD_ITEM) {
            Item item = (Item) data.getSerializableExtra(AddItemActivity.RESULT_ITEM);
            Toast.makeText(getContext(), String.valueOf(item.getPrice()), Toast.LENGTH_LONG).show();
        }
    }
}
