package com.reconciliationhouse.android.loverekindle.ui.chat;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.ChatTypePagerAdapter;
import com.reconciliationhouse.android.loverekindle.adapters.LibraryPagerAdapter;

public class ChatHomeFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public static ChatHomeFragment newInstance() {
        return new ChatHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view= inflater.inflate(R.layout.chat_home_fragment, container, false);
         tabLayout=view.findViewById(R.id.chat_tabs);
         viewPager2=view.findViewById(R.id.chat_pager);
        viewPager2.setAdapter(new ChatTypePagerAdapter(this));
        new TabLayoutMediator(tabLayout,viewPager2, new TabLayoutMediator.OnConfigureTabCallback() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(ChatTypePagerAdapter.getPageTitle(position));
            }
        }).attach();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
