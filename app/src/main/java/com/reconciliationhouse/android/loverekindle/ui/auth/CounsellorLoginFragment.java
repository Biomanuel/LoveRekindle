package com.reconciliationhouse.android.loverekindle.ui.auth;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentCounsellorLoginBinding;
import com.reconciliationhouse.android.loverekindle.models.UserDetails;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CounsellorLoginFragment extends Fragment {
    FragmentCounsellorLoginBinding binding;
    String counsellorCategory,email,password;
    private FirebaseAuth mAuth;



    public CounsellorLoginFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentCounsellorLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth=FirebaseAuth.getInstance();


        binding.spinner.setItems("Spiritual Growth", "Godly Parenting", "Marriage and Relationship", "Health");
        binding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               counsellorCategory=item;
            }
        });
        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_counsellorLoginFragment_to_forgetPasswordFragment);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    private  void login(){
         email= Objects.requireNonNull(binding.textInputEmail.getEditText()).getText().toString();
         password= Objects.requireNonNull(binding.textInputPassword.getEditText()).getText().toString();
        if (TextUtils.isEmpty(email)){
            binding.textInputEmail.setError("Enter Email Address");

        }
        else if (TextUtils.isEmpty(password)){
            binding.textInputPassword.setError("Enter Password");
        }
        else if (TextUtils.isEmpty(counsellorCategory)){
            binding.spinner.setError("Select a Category");
        }
        else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.textInputPassword.setError(null);
            binding.textInputEmail.setError(null);
            binding.spinner.setError(null);
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
                                DocumentReference collectionReference = db.collection("User").document("counsellor").collection(counsellorCategory).document(Objects.requireNonNull(user.getDisplayName()));
                                // DocumentReference collectionReference = db.collection("User").document("regular").collection("users").document(user.getDisplayName());
                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()){
                                            UserModel model=task.getResult().toObject(UserModel.class);

                                            assert model != null;
                                            UserPreferences.saveCategory(model.getCategory(),getContext());
                                            UserPreferences.saveId(user.getUid(),getContext());
                                            UserPreferences.saveUserName(user.getDisplayName(),getContext());
                                            UserPreferences.saveEmail(user.getEmail(),getContext());
                                            UserPreferences.saveRole(model.getRole(),getContext());
                                            UserPreferences.saveBalance(model.getBalance(),getContext());
                                            NavController controller= Navigation.findNavController(getView());
                                            controller.navigate(R.id.action_counsellorLoginFragment_to_navigation_explore);
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
}
