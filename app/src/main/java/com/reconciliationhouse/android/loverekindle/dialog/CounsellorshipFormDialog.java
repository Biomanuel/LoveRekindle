package com.reconciliationhouse.android.loverekindle.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.CounsellorModel;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;

import java.util.HashMap;
import java.util.Objects;

public class CounsellorshipFormDialog extends DialogFragment {
    private Dialog dialog;
    UserModel.Category category1;
      private String category,description;
      private FirebaseAuth mAuth;
public  CounsellorshipFormDialog (){

}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.counsellor_request_form);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        mAuth=FirebaseAuth.getInstance();

        final MaterialSpinner spinner = (MaterialSpinner) dialog.findViewById(R.id.spinner);
        final ProgressBar progressBar=dialog.findViewById(R.id.progress_bar);
        spinner.setItems("Spiritual Growth", "Godly Parenting", "Marriage and Relationship", "Health");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               category=item;
              if (category.equals("Spiritual Growth")){
                  category1= UserModel.Category.Spiritual_Growth;
              }
              else if (category.equals("Godly Parenting")){
                  category1= UserModel.Category.Godly_Parenting;
              }
              else if (category.equals("Marriage and Relationship")){
                  category1= UserModel.Category.Marriage_and_Relationship;
              }
              else {
                  category1= UserModel.Category.Health;
              }
            }
        });

        final TextInputLayout textInputLayout=dialog.findViewById(R.id.text_input_description);
        TextInputEditText editText=dialog.findViewById(R.id.description);
        description= Objects.requireNonNull(editText.getText()).toString();
        Button button=dialog.findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(description)){
                    textInputLayout.setError("Fill this");
                }

                if (TextUtils.isEmpty(category)){
                    spinner.setError("Select Category");

                }
                else {
                    textInputLayout.setError(null);
                    spinner.setError(null);
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();

                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    DocumentReference documentReference=db.collection("CounsellorsRequest").document(UserPreferences.getEmail(getContext()));
                    CounsellorModel model=new CounsellorModel();
                    model.setCounsellorCategory(category1);
                    model.setCounsellorDescription(description);
                    model.setCounsellorEmail(firebaseUser.getEmail());
                    model.setCounsellorDescription(description);
                    model.setCounsellorName(firebaseUser.getDisplayName());
                    model.setCounsellorProfileImage(String.valueOf(firebaseUser.getPhotoUrl()));
                    documentReference.set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                dialog.dismiss();
                                Toast.makeText(getContext(),"Application successful, waiting for approval",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                dialog.dismiss();
                                Toast.makeText(getContext(),"Failed to submit form, try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

        return dialog;
    }
}