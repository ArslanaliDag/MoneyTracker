package arslanali.ru.moneytracker;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import arslanali.ru.moneytracker.fragments.BalansFragment;
import arslanali.ru.moneytracker.fragments.ItemsFragment;
import arslanali.ru.moneytracker.pojo.Item;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add toolbar
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        final ViewPager pages = (ViewPager) findViewById(R.id.pages);
        pages.setAdapter(new MainPagerAdapter());
        tabs.setupWithViewPager(pages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds getItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {
        private final String[] titles;

        MainPagerAdapter() {
            super(getSupportFragmentManager());
            titles = getResources().getStringArray(R.array.main_pager_titles);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ItemsFragment fragmentRashod = new ItemsFragment();
                    // pass the parameter
                    Bundle argsRashod = new Bundle();
                    argsRashod.putString(ItemsFragment.ARG_TYPE, Item.TYPE_EXPENSE);
                    fragmentRashod.setArguments(argsRashod);
                    return fragmentRashod;
                case 1:
                    ItemsFragment fragmentDohod = new ItemsFragment();
                    // pass the parameter
                    Bundle argsDohod = new Bundle();
                    argsDohod.putString(ItemsFragment.ARG_TYPE, Item.TYPE_INCOME);
                    fragmentDohod.setArguments(argsDohod);
                    return fragmentDohod;
                case 2:
                    return new BalansFragment();
            }
            return new Fragment();
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
}
