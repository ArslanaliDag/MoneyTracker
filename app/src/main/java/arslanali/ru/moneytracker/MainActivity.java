package arslanali.ru.moneytracker;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import arslanali.ru.moneytracker.fragments.BalanceFragment;
import arslanali.ru.moneytracker.fragments.ItemsFragment;
import arslanali.ru.moneytracker.pojo.Item;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager pages;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (TabLayout) findViewById(R.id.tabs);
        pages = (ViewPager) findViewById(R.id.pages);
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

            switch (position) {
                case 0:
                case 1:
                    ItemsFragment fragment = new ItemsFragment();
                    Bundle args = new Bundle();
                    // first TYPE_EXPENSE second TYPE_INCOME type from array types
                    args.putString(ItemsFragment.ARG_TYPE, types[position]);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
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
