package arslanali.ru.moneytracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import arslanali.ru.moneytracker.fragments.BalanceFragment;
import arslanali.ru.moneytracker.fragments.ItemsFragment;
import arslanali.ru.moneytracker.pojo.Item;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager pages;
    private Toolbar toolbar;
    private FloatingActionButton fabAdd;
    private String type;

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

//            if (position > types.length -1) {
//                position = 0;
//                type = types[position];
//            } else {
//                position = 1;
//                type = types[position];
//            }

            type = types[position];

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

    private void showFab() {
        fabAdd.setVisibility(View.VISIBLE);
    }

    private void hideFab() {
        fabAdd.setVisibility(View.GONE);
    }
}
