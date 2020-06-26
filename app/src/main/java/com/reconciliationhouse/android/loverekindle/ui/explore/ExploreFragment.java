package com.reconciliationhouse.android.loverekindle.ui.explore;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentExploreBinding;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;


public class ExploreFragment extends Fragment {

    private FragmentExploreBinding mBinding;

    public ExploreFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        UserRepo.initializeWithUser((MainActivity) requireActivity());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!= null) {

            Toast.makeText(getContext(), "You are welcome", Toast.LENGTH_SHORT).show();

        }
        else {
            NavController controller = Navigation.findNavController(getView());


            controller.navigate(R.id.action_navigation_explore_to_signUpFragment);
        }
    }


    //    @Nullable
//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if (getParentFragment() != null && getParentFragment().isDetached()) {
//            return AnimationUtils.loadAnimation(getContext(), R.anim.stay);
//        }
//        return super.onCreateAnimation(transit, enter, nextAnim);
//    }
}
