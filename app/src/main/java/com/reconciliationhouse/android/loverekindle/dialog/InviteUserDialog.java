package com.reconciliationhouse.android.loverekindle.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.chat.InviteUsersAdapter;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InviteUserDialog extends DialogFragment {
    Dialog dialog;
    private String groupName;
    private ChatItem.ChatType chatType;
    private String imageUrl;

    public InviteUserDialog(String groupName, ChatItem.ChatType chatType, String imageUrl) {
        this.groupName = groupName;
        this.chatType = chatType;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.invite_friend_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final RecyclerView recyclerView=dialog.findViewById(R.id.recylerview);


      final    ProgressBar progressBar=dialog.findViewById(R.id.progress_bar);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final List<UserModel>models=new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        CollectionReference collectionReference=db.collection("User");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            progressBar.setVisibility(View.GONE);
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                UserModel model = document.toObject(UserModel.class);
                                models.add(model);

                            }
                            Toast.makeText(getContext(),String.valueOf(models.size()),Toast.LENGTH_SHORT).show();
                            InviteUsersAdapter adapter=new InviteUsersAdapter(groupName,chatType,imageUrl);
                            adapter.setList(models);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Error getting documents:",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        dialog.show();
         return dialog;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //inflate layout with recycler view
//        View dialogView = inflater.inflate(R.layout.invite_friend_layout, container, false);
//

////        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
////        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
////        //setadapter
////        CustomAdapter adapter = new MyRecyclerAdapter(context, customList);
////        mRecyclerView.setAdapter(adapter);
////        //get your recycler view and populate it.
//        return dialogView;
//    }


}
