package com.reconciliationhouse.android.loverekindle.ui.counsellor;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.adapters.counsellor.AllCounsellorAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.AllCounsellorsFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AllCounsellorsFragment extends Fragment {

    private AllCounsellorsViewModel mViewModel;
    private AllCounsellorsFragmentBinding binding;
    AllCounsellorAdapter adapters;
    private List<UserModel>models;
    public static AllCounsellorsFragment newInstance() {
        return new AllCounsellorsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding=AllCounsellorsFragmentBinding.inflate(inflater,container,false);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AllCounsellorsViewModel.class);
        models=new ArrayList<>();


        FirebaseFirestore db=FirebaseFirestore.getInstance();

        CollectionReference collectionReference=db.collection("User");
        Query query = collectionReference
                .whereEqualTo("role", UserModel.Role.Counsellor);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                UserModel model = document.toObject(UserModel.class);
                                models.add(model);

                            }
                            adapters = new AllCounsellorAdapter(AllCounsellorsFragment.this);

                            adapters.setCounsellors(models);
//                            listMutableLiveData.setValue(models);
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(),String.valueOf(models.size()),Toast.LENGTH_SHORT).show();
                            binding.recylerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.recylerview.setAdapter(adapters);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}
