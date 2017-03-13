package com.looker.market;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.looker.market.bean.Tab;
import com.looker.market.fragment.CartFragment;
import com.looker.market.fragment.CategoryFragment;
import com.looker.market.fragment.HomeFragment;
import com.looker.market.fragment.HotFragment;
import com.looker.market.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private List<Tab> tabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabHost();
    }

    private void initTabHost() {

        mTabHost = (FragmentTabHost) findViewById(R.id.tab_host);

        mTabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);

        Tab tab_home = new Tab(R.string.tab_home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_hot = new Tab(R.string.tab_hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tab_category = new Tab(R.string.tab_category, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.tab_cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tab_user = new Tab(R.string.tab_user, R.drawable.selector_icon_user, MineFragment.class);

        tabs.add(tab_home);
        tabs.add(tab_hot);
        tabs.add(tab_category);
        tabs.add(tab_cart);
        tabs.add(tab_user);

        for (Tab tab : tabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }
    }

    private View buildIndicator(Tab tab){
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        ImageView tabIcon = (ImageView) inflate.findViewById(R.id.item_tab_iv);
        TextView tabText = (TextView) inflate.findViewById(R.id.item_tab_tv);

        tabIcon.setImageResource(tab.getIcon());
        tabText.setText(tab.getTitle());

        return inflate;
    }
}
