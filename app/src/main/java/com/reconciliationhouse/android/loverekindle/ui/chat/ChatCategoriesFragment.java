package com.reconciliationhouse.android.loverekindle.ui.chat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.ChatCategoriesFragmentBinding;

public class ChatCategoriesFragment extends Fragment {

    private ChatCategoriesViewModel mViewModel;
    private ChatCategoriesFragmentBinding mBinding;

    public static ChatCategoriesFragment newInstance() {
        return new ChatCategoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ChatCategoriesFragmentBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatCategoriesViewModel.class);
        mBinding.spiritualCouncellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(mBinding.getRoot());
                controller.navigate(R.id.action_navigation_chat_to_chatFragment);
            }
        });
        mBinding.healthCouncellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(mBinding.getRoot());
                controller.navigate(R.id.action_navigation_chat_to_chatFragment);
            }
        });
        mBinding.parentingCouncellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(mBinding.getRoot());
                controller.navigate(R.id.action_navigation_chat_to_chatFragment);
            }
        });
        mBinding.relationshipCouncellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(mBinding.getRoot());
                controller.navigate(R.id.action_navigation_chat_to_chatFragment);
            }
        });


    }


}
