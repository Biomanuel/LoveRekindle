package com.reconciliationhouse.android.loverekindle.adapters.chat;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reconciliationhouse.android.loverekindle.R;

import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.Message;

import com.reconciliationhouse.android.loverekindle.models.UserSender;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Message>mList;
        ChatItem.ChatType chatType;
     CoordinatorLayout bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView imageView;

        public ChatAdapter(ChatItem.ChatType chatType, CoordinatorLayout coordinatorLayout,ImageView imageView){
            this.chatType=chatType;
            this.bottomSheetLayout=coordinatorLayout;
            this.imageView=imageView;


        }

    @Override
    public int getItemViewType(int position) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        UserSender userSender=mList.get(position).getUserSender();

        assert user != null;
        if (userSender.getName().equals(user.getDisplayName())){
            return  1;
        }
       else {
           return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.chat_receiver_layout,parent,false);
            return new ReceiverItemHolder(view);
        }
        else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.chat_sender_layout,parent,false);
            return new SenderItemHolder(view);
        }



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()){
                case 0: {
                    UserSender senderItem = mList.get(position).getUserSender();

                    String message = mList.get(position).getMessage();

                    ReceiverItemHolder receiverItemHolder = (ReceiverItemHolder) holder;
                    if (mList.get(position).getMessageType().equals(Message.MessageType.TEXT)){
                        receiverItemHolder.receiverMessage.setText(message);
                        receiverItemHolder.imageView.setVisibility(View.GONE);


                    }
                    else {
                        if (mList.get(position).getImageUrl()!=null){
                            Picasso.get().load(mList.get(position).getImageUrl()).into(receiverItemHolder.imageView);
                            receiverItemHolder.receiverMessage.setVisibility(View.GONE);

                        }
                    }

                    if (mList.get(position).getDateCreated()!=null) {

                        String date = new SimpleDateFormat("hh:mm:aa").format(mList.get(position).getDateCreated());
                        receiverItemHolder.time.setText(date);
                    }



                         if (chatType.equals(ChatItem.ChatType.Group_Chat)){
                             receiverItemHolder.receiverImage.setVisibility(View.VISIBLE);
                             if (senderItem.getProfileImageUrl()!=null){
                       Picasso.get().load(senderItem.getProfileImageUrl()).into(receiverItemHolder.receiverImage);
                   }
                   else {
                       receiverItemHolder.receiverImage.setImageResource(R.drawable.profile);
                   }

                         }
                         else {
                             receiverItemHolder.receiverImage.setVisibility(View.GONE);
                         }

                          if (position==mList.size()-1) {
                              if (mList.get(position).getSeen()) {
                                  receiverItemHolder.seenImg.setVisibility(View.VISIBLE);

                              } else {
                                  receiverItemHolder.seenImg.setVisibility(View.GONE);
                              }
                          }
                          else {
                              receiverItemHolder.seenImg.setVisibility(View.GONE);
                          }
                    receiverItemHolder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DisplayMetrics metrics = v.getContext().getResources().getDisplayMetrics();
                            int width = metrics.widthPixels;
                            int height = metrics.heightPixels;
                            int totalScreenHeight=(60*100)*height;
                            //imageView.Picasso.get().load(mList.get(position).getImageUrl()).into(imageView);




                            setUpBottomSheet();
                            bottomSheetLayout.setVisibility(View.VISIBLE);

                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    });


//
                }
                break;
                case 1:{

                    String message = mList.get(position).getMessage();
                    final SenderItemHolder  senderItemHolder = (SenderItemHolder) holder;
                    senderItemHolder.senderMessage.setText(message);
                    if (mList.get(position).getDateCreated()!=null) {
                        String date = new SimpleDateFormat("hh:mm:aa").format(mList.get(position).getDateCreated());
                        senderItemHolder.time.setText(date);
                    }
                    if (mList.get(position).getMessageType().equals(Message.MessageType.TEXT)){

                        senderItemHolder.senderMessage.setText(message);
                        senderItemHolder.imageView.setVisibility(View.GONE);


                    }
                    else {
                        if (mList.get(position).getImageUrl()!=null){
                            Picasso.get().load(mList.get(position).getImageUrl()).into(senderItemHolder.imageView);
                            senderItemHolder.senderMessage.setVisibility(View.GONE);

                        }
                    }
                    senderItemHolder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            DisplayMetrics metrics = v.getContext().getResources().getDisplayMetrics();
                            int width = metrics.widthPixels;
                            int height = metrics.heightPixels;
                            int totalScreenHeight=(60*100)*height;
//                            Picasso.get().load(mList.get(position).getImageUrl()).resize(width,totalScreenHeight).into(imageView);
                            setUpBottomSheet();

                            bottomSheetLayout.setVisibility(View.VISIBLE);

                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    });

                }
                break;
                default:
                    break;
            }


    }
    private void setUpBottomSheet() {


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        // set callback for changes

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSheetLayout.setVisibility(View.GONE);

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        bottomSheetLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:


                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }

        });

        //Log.d(TAG, "onStateChanged: " + newState);
    }

    @Override
    public int getItemCount() {
          return mList.size();
    }
    public void setMessages(@NonNull List<Message> messages) {
        if (this.mList != null) {
            this.mList.clear();
            this.mList.addAll(messages);
            notifyDataSetChanged();
            return;
        }
        this.mList = new ArrayList<>();
        this.mList.addAll(messages);
        notifyDataSetChanged();
    }



}
class ReceiverItemHolder extends RecyclerView.ViewHolder {
    CircleImageView receiverImage;
    TextView senderMessage,receiverMessage;
    TextView date, time,uuu;
    ImageView imageView;
    ImageView seenImg;

    public ReceiverItemHolder(@NonNull View itemView) {
        super(itemView);
        receiverImage=itemView.findViewById(R.id.circleImageView);
        receiverMessage=itemView.findViewById(R.id.receiver_message);
        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);
        imageView=itemView.findViewById(R.id.ImageView_image);
        seenImg=itemView.findViewById(R.id.seen_img);



    }
}

class SenderItemHolder extends RecyclerView.ViewHolder {
    CircleImageView receiverImage;
    TextView senderMessage;
    ImageView imageView;
    TextView date, time;

    public SenderItemHolder(@NonNull View itemView) {
        super(itemView);

        senderMessage=itemView.findViewById(R.id.sender_message);

        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);
        imageView=itemView.findViewById(R.id.ImageView_image);



    }

}
