package com.reconciliationhouse.android.loverekindle.ui.settings;

import androidx.fragment.app.FragmentManager;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.SettingsFragmentBinding;
import com.reconciliationhouse.android.loverekindle.dialog.CounsellorshipFormDialog;
import com.reconciliationhouse.android.loverekindle.dialog.InviteUserDialog;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;
import com.reconciliationhouse.android.loverekindle.ui.chat.ChatFragment;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SettingsFragment extends Fragment {
   private SettingsFragmentBinding mBinding;
   private FirebaseAuth mAuth;
   private boolean isEditable;




    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding=SettingsFragmentBinding.inflate(inflater,container,false);
        mAuth=FirebaseAuth.getInstance();
          isEditable=false;
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //check if user is already Authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            mBinding.email.setText(currentUser.getEmail());

            mBinding.name.setText(currentUser.getDisplayName());
            Picasso.get().load(currentUser.getPhotoUrl()).into(mBinding.userProfileImage);
        }
mBinding.changeImage.setEnabled(false);
        mBinding.applyAsCounsellor.setVisibility(View.GONE);
        mBinding.counsellorStatus.setVisibility(View.GONE);

        mBinding.changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().
                        setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(150, 140)
                        .setFixAspectRatio(true)
                        .start(getContext(), SettingsFragment.this);

            }
        });

        mBinding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditable=true;
                if (UserPreferences.getRole(getContext()).equals(String.valueOf(UserModel.Role.Counsellor))){
                    mBinding.counsellorStatus.setText("You are a Counsellor");
                    mBinding.applyAsCounsellor.setVisibility(View.GONE);

                }
                else {
                    mBinding.counsellorStatus.setText("You are a Normal User");
                    mBinding.applyAsCounsellor.setVisibility(View.VISIBLE);
                }
                mBinding.counsellorStatus.setVisibility(View.VISIBLE);
                mBinding.changeImage.setEnabled(true);
                mBinding.email.setFocusable(true);
                mBinding.email.setFocusableInTouchMode(true);
                mBinding.email.setEnabled(true);
                mBinding.name.setFocusable(true);
                mBinding.name.setFocusableInTouchMode(true);
                mBinding.name.setEnabled(true);
                if (isEditable){
                    mBinding.saveProfile.setVisibility(View.VISIBLE);
                    mBinding.editProfile.setVisibility(View.GONE);
                }
                else {
                    mBinding.saveProfile.setVisibility(View.GONE);
                    mBinding.editProfile.setVisibility(View.VISIBLE);
                }

            }
        });
        mBinding.applyAsCounsellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = SettingsFragment.this.getActivity().getSupportFragmentManager();
                CounsellorshipFormDialog dialog=new CounsellorshipFormDialog();
                dialog.show(fm,"");
            }
        });
        mBinding.saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditable=false;
                mBinding.applyAsCounsellor.setVisibility(View.GONE);
                mBinding.counsellorStatus.setVisibility(View.GONE);
                mBinding.changeImage.setEnabled(false);
                mBinding.email.setFocusable(false);
                mBinding.email.setFocusableInTouchMode(false);
                mBinding.email.setEnabled(false);
                mBinding.name.setFocusable(false);
                mBinding.name.setFocusableInTouchMode(false);
                mBinding.name.setEnabled(false);
                if (!isEditable){
                    mBinding.saveProfile.setVisibility(View.GONE);
                    mBinding.editProfile.setVisibility(View.VISIBLE);
                }
//                else {
//                    mBinding.saveProfile.setVisibility(View.VISIBLE);
//                    mBinding.editProfile.setVisibility(View.GONE);
//                }

            }
        });


 mBinding.logout.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         mAuth.signOut();
         NavController controller= Navigation.findNavController(getView());
         controller.navigate(R.id.action_settingsFragment_to_loginFragment);



     }
 });

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
