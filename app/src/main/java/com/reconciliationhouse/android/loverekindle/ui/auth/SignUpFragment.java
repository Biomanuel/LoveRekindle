package com.reconciliationhouse.android.loverekindle.ui.auth;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.SignUpFragmentBinding;
import com.reconciliationhouse.android.loverekindle.dialog.AuthDialog;
import com.reconciliationhouse.android.loverekindle.dialog.ChatTypeDialog;
import com.reconciliationhouse.android.loverekindle.models.UserDetails;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SignUpFragment extends Fragment {


    private final Pattern mPattern = Pattern.compile("^" +

            "(?=.*[0-9])" +   //at least digits
            "(?=.*[a-z])" +  //at least one lowercase
            "(?=.*[A-Z])" +        //at least one uppercase
            "(?=.*[@#^%$=])" +     /// at least one specila spaces
            "(?=\\s+$)" +         // no white spaces
            ".{6,}" +// at least six characters
            "$");
    private final Pattern mPasswordPattern = Pattern.compile("^" +
            ".{6,}" +
            "$");
    private final Pattern mPhoneNumberPattern = Pattern.compile("^" + "(?=.*[0-9])" +   //at least digits
            ".{11,}" +// at least six characters
            "$");
    StorageReference reference;
    private SignUpViewModel mViewModel;
    private String imagePath;
    private SignUpFragmentBinding binding;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseUser user;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SignUpFragmentBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser== null) {
            FragmentManager fm = this.requireActivity().getSupportFragmentManager();
            AuthDialog custom = new AuthDialog(this,binding.lay);
            custom.show(fm,"");

        }

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        binding.changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().
                        setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(150, 140)
                        .setFixAspectRatio(true)
                        .start(getContext(), SignUpFragment.this);

            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                binding.userProfileImage.setImageURI(resultUri);
                imagePath = String.valueOf(resultUri);
                binding.userProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Snackbar.make(getView(), String.valueOf(error), Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    private void login() {
        final String name = Objects.requireNonNull(binding.textInputFirstName.getEditText()).getText().toString();
        String email = Objects.requireNonNull(binding.textInputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(binding.textInputPassword.getEditText()).getText().toString();
        String password1 = Objects.requireNonNull(binding.textInputReTypePassword.getEditText()).getText().toString();

        if (TextUtils.isEmpty(name)) {
            binding.textInputFirstName.setError("Enter Your Names");
        } else if (!name.contains(" ")) {
            binding.textInputFirstName.setError("Enter Last Name");
        } else if (TextUtils.isEmpty(email)) {
            binding.textInputEmail.setError("Enter Email Address");

        } else if (!mPasswordPattern.matcher(password).matches()) {
            binding.textInputPassword.setError("Password Too Short");

        } else if (!(password.equals(password1))) {
            binding.textInputReTypePassword.setError("Password didn't match");

        } else if (!(email.matches(".*[gmail.com].*")) || !(email.matches(".*[yahoo.com].*")) || !(email.matches(".*[hotmail.com].*")) || !(email.matches(".*[oulook.com].*"))) {
            binding.textInputEmail.setError("Enter a valid Email ");

        } else if (imagePath == null) {

            Snackbar.make(binding.userProfileImage, "Select a profile Image", Snackbar.LENGTH_SHORT).show();

        } else {
            binding.textInputReTypePassword.setError(null);
            binding.textInputFirstName.setError(null);
            binding.textInputEmail.setError(null);
            binding.textInputEmail.setError(null);
            binding.textInputFirstName.setError(null);
            binding.progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                user = mAuth.getCurrentUser();
                                uploadUserImageToStorage("images/user_profile/", name);


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null) {

            Toast.makeText(getContext(), "You are welcome", Toast.LENGTH_SHORT).show();
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_signUpFragment_to_navigation_explore);
        }
    }

    private void uploadUserImageToStorage(String path, final String name) {
        final Uri uri = Uri.parse(imagePath);
        storageReference = FirebaseStorage.getInstance().getReference();
        reference = storageReference.child(path + name);
        UploadTask uploadTask = reference.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(task.getResult())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference collectionReference = db.collection("User").document("regular").collection("users").document(user.getDisplayName());
                                                // for counsellor
                                                //DocumentReference collectionReference = db.collection("User").document("counsellor").collection("Spiritual Growth").document(Objects.requireNonNull(user.getDisplayName()));
                                               //final UserModel model = new UserModel(user.getUid(), user.getDisplayName(), String.valueOf(user.getPhotoUrl()), user.getEmail(), "0", "counsellor","Spiritual Growth");
                                               final UserModel model = new UserModel(user.getUid(), user.getDisplayName(), String.valueOf(user.getPhotoUrl()), user.getEmail(), "0", "regular");



                                                collectionReference.set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            binding.progressBar.setVisibility(View.GONE);
                                                            UserPreferences.saveRole(model.getRole(),getContext());
                                                            UserPreferences.saveId(user.getUid(),getContext());
                                                            UserPreferences.saveUserName(user.getDisplayName(),getContext());
                                                            UserPreferences.saveEmail(user.getEmail(),getContext());
                                                            UserPreferences.saveBalance(model.getBalance(),getContext());
                                                            NavController controller = Navigation.findNavController(getView());
                                                            controller.navigate(R.id.action_signUpFragment_to_navigation_explore);
                                                        } else {
                                                            binding.progressBar.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getContext(), "Sign UpNot Successful", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });


                                            }
                                        }
                                    });
                        } else {

                        }
                    }

                });

            }
        });

    }
}
