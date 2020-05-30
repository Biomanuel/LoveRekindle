package com.reconciliationhouse.android.loverekindle.ui.auth;

import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.ForgetPasswordFragmentBinding;

public class ForgetPasswordFragment extends Fragment {

    private ForgetPasswordViewModel mViewModel;
    ForgetPasswordFragmentBinding binding;

    public static ForgetPasswordFragment newInstance() {
        return new ForgetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
      binding=ForgetPasswordFragmentBinding.inflate(inflater,container,false);
      return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ForgetPasswordViewModel.class);

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVerfication();
            }
        });

    }
    private void requestVerfication(){
        String email=binding.textInputEmail.getEditText().getText().toString();
        if (TextUtils.isEmpty(email)){
            binding.textInputEmail.setError("Input Email");
        }
        else {
            binding.textInputEmail.setError(null);
            FirebaseAuth auth = FirebaseAuth.getInstance();


            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                               showDialog();
                            }
                        }
                    });

        }
    }
private void showDialog(){

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Password reset link  sent ")
                .setCancelable(false)
            .setMessage("Check the email address, a password reset link has been sent ")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    NavController controller=Navigation.findNavController(getView());
                    controller.navigate(R.id.action_forgetPasswordFragment_to_loginFragment);
                }
            })

        .show();
}


}
