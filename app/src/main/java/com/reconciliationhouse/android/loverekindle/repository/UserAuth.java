package com.reconciliationhouse.android.loverekindle.repository;
// This is a class for handling Login and Sigining up

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import static android.content.ContentValues.TAG;

public class UserAuth {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("User").document("details").collection("regular").document();
    private StorageReference mStorage;
    private StorageReference ref;
    public String path = "images//user_profile" + userRef.getId();
    private static final int REQUEST_CODE = 45;
    private boolean mStoragePermissions;
    private StorageReference mStorageReference;

    private FirebaseAuth mAuth;
    private UserModel mUserModel;
    //call this function after checking the below fields is not null
    public void signUpUserWithEmail(final Context context, final String firstName, final String lastName, final String email, final String passWord, final String imageUri ){



        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ((networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable())) {
            Toast.makeText(context, "No Internet Connection",Toast.LENGTH_SHORT).show();
            return;

        } else {
            mAuth.createUserWithEmailAndPassword(email, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(context, "Your registration is successful", Toast.LENGTH_LONG).show();
                        mUserModel=new UserModel();
                        uploadImagesToFirebase(imageUri);
//                        mUserModel.setProfileImageUrl("");
//                        mUserModel.setFirstName(firstName);
//                        mUserModel.setLastName(lastName);
                        //mUserModel.setEmail(email);
                        mUserModel.setRole("regular");
                        mUserModel.setBalance("0");
                        mUserModel.setUserId(userRef.getId());
                        userRef.set(mUserModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(context,"go mainActivty",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context,"Saving user details no Successful",Toast.LENGTH_SHORT).show();
                                }
                                
                            }
                            
                        });
                        
                        


                    } else if(!task.isSuccessful()) {
                        Log.d(TAG, " Is not Successful ");
                        Log.e(TAG, "onComplete: Failed=" + task.getException().getMessage());

                        
                    }
                }
            });
            
            

        
       
        
    }
}

    private void uploadImagesToFirebase(String imageUrl) {
        mStorageReference = FirebaseStorage.getInstance().getReference();
        
        mStorage = mStorageReference.child(path);
        //ref = mStorage.child(uri[i].getLastPathSegment());

        ref.putFile(Uri.parse(imageUrl)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Tasks.whenAll(newProductRef.)
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        
                        userRef.update("profileImageUrl", FieldValue.arrayUnion(String.valueOf(uri)));
                    }
                });


               // Toast.makeText(getApplicationContext(), " Uploading Successful", Toast.LENGTH_LONG).show();

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(getApplicationContext(), " Uploading Successful", Toast.LENGTH_LONG).show();

            }
        });
        
    }
    
}
