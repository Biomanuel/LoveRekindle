package com.reconciliationhouse.android.loverekindle.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.reconciliationhouse.android.loverekindle.R;

import java.util.Objects;

public class AuthDialog extends DialogFragment {
    private Fragment fragment;
    private FrameLayout layout;

    public AuthDialog (Fragment fragment, FrameLayout layout){
        this.fragment=fragment;
        this.layout=layout;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@androidx.annotation.Nullable Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.auth_prompt_layout, null);

         layout.setVisibility(View.GONE);
        Button login=dialogView.findViewById(R.id.login);
        Button signUp=dialogView.findViewById(R.id.sign_up);
        login.setOnClickListener(loginBtn);
        signUp.setOnClickListener(signUpBtn);

        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getActivity());
        builder.setView(dialogView);



        return builder.create();
    }
    private  View.OnClickListener loginBtn =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          alertDialog();
            Objects.requireNonNull(getDialog()).dismiss();

        }
    } ;

    private View.OnClickListener signUpBtn=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Objects.requireNonNull(getDialog()).dismiss();
            layout.setVisibility(View.VISIBLE);

        }
    };
    private void alertDialog(){
        MaterialAlertDialogBuilder alertDialogBuilder= new MaterialAlertDialogBuilder(fragment.getContext());
        alertDialogBuilder.setTitle("Are You a Counsellor");
        setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(R.id.action_signUpFragment_to_counsellorLoginFragment);
                dialog.dismiss();

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(R.id.action_signUpFragment_to_loginFragment);
                dialog.dismiss();


            }
        });
        alertDialogBuilder.show();

    }
}
