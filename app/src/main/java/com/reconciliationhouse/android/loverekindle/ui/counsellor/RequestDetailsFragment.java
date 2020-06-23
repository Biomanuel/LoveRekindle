package com.reconciliationhouse.android.loverekindle.ui.counsellor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentRequestDetailsBinding;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.CounsellorModel;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.ui.chat.ChatFragmentArgs;
import com.squareup.picasso.Picasso;


public class RequestDetailsFragment extends Fragment {
FragmentRequestDetailsBinding binding;
    public RequestDetailsFragment() {
        // Required empty public constructor}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding=FragmentRequestDetailsBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {


            RequestDetailsFragmentArgs args = RequestDetailsFragmentArgs.fromBundle(getArguments());
            Gson gson = new Gson();
           final CounsellorModel model = gson.fromJson(args.getRequestDetails(), CounsellorModel.class);
           if (model!=null){
               Picasso.get().load(model.getCounsellorProfileImage()).into(binding.circleImageView2);
               binding.name.setText(model.getCounsellorName());
               binding.email.setText(model.getCounsellorEmail());
               binding.description.setText(model.getCounsellorDescription());
               binding.category.setText(String.valueOf(model.getCounsellorCategory()));

               binding.accept.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       binding.progressBar.setVisibility(View.VISIBLE);
                       FirebaseFirestore db=FirebaseFirestore.getInstance();
                       DocumentReference reference=db.collection("User").document(model.getCounsellorEmail());
                       reference.update("role", UserModel.Role.Counsellor);
                       reference.update("category",model.getCounsellorCategory());
                       DocumentReference reference1=db.collection("CounsellorsRequest").document(model.getCounsellorEmail());
                       reference1.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){

                                 binding.progressBar.setVisibility(View.GONE);
                                   NavController controller= Navigation.findNavController(getView());
                                   controller.navigate(R.id.action_requestDetailsFragment_to_listOfRequestFragment);



                               }
                           }
                       });

                   }
               });

           }
        }
    }
}
