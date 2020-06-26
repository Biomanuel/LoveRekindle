package com.reconciliationhouse.android.loverekindle.ui.counsellor;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.counsellor.AllCounsellorAdapter;
import com.reconciliationhouse.android.loverekindle.adapters.counsellor.CounsellorRegAdapters;
import com.reconciliationhouse.android.loverekindle.models.CounsellorModel;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.ui.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class ListOfRequestFragment extends Fragment {

    private ListOfRequestViewModel mViewModel;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<CounsellorModel>mList;
    private Menu mMenu;
    private ProgressBar progressBar;

    public static ListOfRequestFragment newInstance() {
        return new ListOfRequestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
         view=inflater.inflate(R.layout.list_of_request_fragment, container, false);
        toolbar=view.findViewById(R.id.materialToolbar2);
        recyclerView=view.findViewById(R.id.recylerview);

        progressBar=view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList=new ArrayList<>();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        final CounsellorRegAdapters adapter=new CounsellorRegAdapters(ListOfRequestFragment.this);

        CollectionReference collectionReference=db.collection("CounsellorsRequest");
        Query query = collectionReference.orderBy("timeStamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        CounsellorModel model = document.toObject(CounsellorModel.class);
                        mList.add(model);



                    }
                       progressBar.setVisibility(View.GONE);

                    adapter.setCounsellors(mList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ListOfRequestViewModel.class);

        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


    }
    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.counsellor, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_counsellor:

                NavController navController = NavHostFragment.findNavController(ListOfRequestFragment.this);
                navController.navigate(R.id.action_listOfRequestFragment_to_allCounsellorsFragment);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

}
