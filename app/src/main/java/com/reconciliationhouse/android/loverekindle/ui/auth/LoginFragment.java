package com.reconciliationhouse.android.loverekindle.ui.auth;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.LoginFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.UserDetails;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginFragment extends Fragment {
    private  final int RC_SIGN_IN =145 ;
    LoginFragmentBinding binding;

    private LoginViewModel mViewModel;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding =LoginFragmentBinding.inflate(inflater,container,false);


        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);


        mAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(getContext(), gso);

        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_loginFragment_to_forgetPasswordFragment);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            NavController controller=Navigation.findNavController(getView());
            controller.navigate(R.id.action_loginFragment_to_navigation_explore);
        }


    }
    private  void login(){
        String email= Objects.requireNonNull(binding.textInputEmail.getEditText()).getText().toString();
        String password= Objects.requireNonNull(binding.textInputPassword.getEditText()).getText().toString();
        if (TextUtils.isEmpty(email)){
            binding.textInputEmail.setError("Enter Email Address");

        }
       else if (TextUtils.isEmpty(password)){
            binding.textInputPassword.setError("Enter Password");
        }
       else {
           binding.progressBar.setVisibility(View.VISIBLE);
            binding.textInputPassword.setError(null);
            binding.textInputEmail.setError(null);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                binding.progressBar.setVisibility(View.GONE);
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseFirestore db=FirebaseFirestore.getInstance();
                               // DocumentReference collectionReference = db.collection("User").document("counsellor").collection("spiritual").document(Objects.requireNonNull(user.getDisplayName()));
                               DocumentReference collectionReference = db.collection("User").document(user.getEmail());
                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()){
                                            UserModel model = task.getResult().toObject(UserModel.class);


                                            assert model != null;
                                UserPreferences.saveCategory(String.valueOf(model.getCategory()),getContext());
                                           UserPreferences.saveId(user.getUid(),getContext());
                                            UserPreferences.saveUserName(user.getDisplayName(),getContext());
                                            UserPreferences.saveEmail(user.getEmail(),getContext());
                                            UserPreferences.saveRole(String.valueOf(model.getRole()),getContext());
                                           UserPreferences.saveBalance(model.getBalance(),getContext());
                                            NavController controller=Navigation.findNavController(getView());
                                            controller.navigate(R.id.action_loginFragment_to_navigation_explore);
                                        }


                                    }
                                });



                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                // ...
                            }

                            // ...
                        }
                    });

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(String idToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            binding.progressBar.setVisibility(View.GONE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            NavController controller=Navigation.findNavController(getView());
                            controller.navigate(R.id.action_loginFragment_to_navigation_explore);
                            //updateUI(user);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(binding.googleSignInBtn, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

}
