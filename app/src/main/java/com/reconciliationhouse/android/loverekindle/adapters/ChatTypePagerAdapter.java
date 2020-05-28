package com.reconciliationhouse.android.loverekindle.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.reconciliationhouse.android.loverekindle.ui.chat.GroupChatFragment;
import com.reconciliationhouse.android.loverekindle.ui.chat.SingleChatFragment;

public class ChatTypePagerAdapter extends FragmentStateAdapter {
    private final static String[] mFragmentTitleList = new String[]{"Single Chat", "Group Chat"
    };

    public ChatTypePagerAdapter(Fragment fragment) {
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
                return new SingleChatFragment();
            case 1:
                return new GroupChatFragment();

        }
        return null;
    }
}
