package com.e.vemaybay.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.e.vemaybay.AddFlight;
import com.e.vemaybay.BookTicket;
import com.e.vemaybay.ChangeRules;
import com.e.vemaybay.Customer;
import com.e.vemaybay.ListFlight;
import com.e.vemaybay.R;
import com.e.vemaybay.SellTicket;
import com.e.vemaybay.Statistic;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5, R.string.tab_text_6,R.string.tab_text_7};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new AddFlight();
                break;
            case 1:
                fragment = new SellTicket();
                break;
            case 2:
                fragment = new BookTicket();
                break;
            case 3:
                fragment = new ListFlight();
                break;
            case 4:
                fragment = new Statistic();
                break;
            case 5:
                fragment = new ChangeRules();
                break;
            case 6:
                fragment = new Customer();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 7;
    }
}