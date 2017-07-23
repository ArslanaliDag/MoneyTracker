package arslanali.ru.moneytracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import arslanali.ru.moneytracker.adapters.ItemsAdapter;
import arslanali.ru.moneytracker.api.LSApi;
import arslanali.ru.moneytracker.fragments.BalanceFragment;
import arslanali.ru.moneytracker.fragments.ItemsFragment;
import arslanali.ru.moneytracker.pojo.Item;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager pages;
    private Toolbar toolbar;
    private FloatingActionButton fabAdd;
    private String type;
    private LSApi api;
    private static final int LOADER_ADD = 1;
    private SwipeRefreshLayout refreshLayout;
    // include adapter
    private ItemsAdapter itemsAdapter = new ItemsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (TabLayout) findViewById(R.id.tabs);
        pages = (ViewPager) findViewById(R.id.pages);

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra(AddItemActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, AddItemActivity.RC_ADD_ITEM);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!((LSApp) getApplication()).isLoggedIn()) {
            // not init UI
            startActivity(new Intent(this, AuthActivity.class));
        } else {
            // init UI
            initUI();
        }
    }

    // init user interface
    private void initUI() {
        if (pages.getAdapter() != null)
            return;
        setSupportActionBar(toolbar);
        pages.setAdapter(new MainPagerAdapter());
        tabs.setupWithViewPager(pages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds getItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles;
        private final String[] types = {Item.TYPE_EXPENSE, Item.TYPE_INCOME};

        MainPagerAdapter() {
            super(getSupportFragmentManager());
            titles = getResources().getStringArray(R.array.main_pager_titles);
        }

        @Override
        public Fragment getItem(int position) {

            if (position > types.length -1) {
                position = 0;
                type = types[position];
            } else {
                position = 1;
                type = types[position];
            }

            switch (position) {
                // combining case clocks
                case 0:
                case 1:
                    showFab();
                    ItemsFragment fragment = new ItemsFragment();
                    Bundle args = new Bundle();
                    // first TYPE_EXPENSE second TYPE_INCOME type from array types
                    args.putString(ItemsFragment.ARG_TYPE, type);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    hideFab();
                    return new BalanceFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

    // add item
    private void addItem(final int price, final String name, final String type) {
        // init Activity loader
        getLoaderManager().initLoader(LOADER_ADD, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(MainActivity.this) {
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

    private void showFab() {
        fabAdd.setVisibility(View.GONE);
    }

    private void hideFab() {
        fabAdd.setVisibility(View.VISIBLE);
    }

//    private class MainPagerAdapter extends FragmentPagerAdapter {
//        private final String[] titles;
//
//        MainPagerAdapter() {
//            super(getSupportFragmentManager());
//            titles = getResources().getStringArray(R.array.main_pager_titles);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    ItemsFragment fragmentRashod = new ItemsFragment();
//                    // pass the parameter
//                    Bundle argsRashod = new Bundle();
//                    // set type expense
//                    argsRashod.putString(ItemsFragment.ARG_TYPE, Item.TYPE_EXPENSE);
//                    fragmentRashod.setArguments(argsRashod);
//                    return fragmentRashod;
//                case 1:
//                    ItemsFragment fragmentDohod = new ItemsFragment();
//                    // pass the parameter
//                    Bundle argsDohod = new Bundle();
//                    // set type expense
//                    argsDohod.putString(ItemsFragment.ARG_TYPE, Item.TYPE_INCOME);
//                    fragmentDohod.setArguments(argsDohod);
//                    return fragmentDohod;
//                case 2:
//                    return new BalanceFragment();
//            }
//            return new Fragment();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles[position];
//        }
//
//        @Override
//        public int getCount() {
//            return titles.length;
//        }
//    }
}
