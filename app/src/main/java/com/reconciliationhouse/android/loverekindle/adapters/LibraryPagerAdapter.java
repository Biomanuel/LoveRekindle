package com.reconciliationhouse.android.loverekindle.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.reconciliationhouse.android.loverekindle.ui.download.DownloadFragment;

import com.reconciliationhouse.android.loverekindle.ui.purchase.Purchased_Fragment;

public class LibraryPagerAdapter extends FragmentStateAdapter {
    private final static String[] mFragmentTitleList = new String[]{"Purchased Items", "Downloads"
    };

    public LibraryPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    public static CharSequence getPageTitle(int position) {
        return mFragmentTitleList[position];
    }

    @Override
    public int getItemCount() {

        return 2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position) {
            case 0:
                return new Purchased_Fragment();
            case 1:
                return new DownloadFragment();

        }
        return null;
    }

}
