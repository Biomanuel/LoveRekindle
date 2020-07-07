package com.reconciliationhouse.android.loverekindle.adapters.chat;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Message>mList;
        ChatItem.ChatType chatType;

    boolean isPlaying = false;
    MediaPlayer mediaPlayer;





    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

        public ChatAdapter(ChatItem.ChatType chatType, OnItemClickListener onItemClickListener){
            this.chatType=chatType;
            this.mOnItemClickListener=onItemClickListener;


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
//            final RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
//            };
//
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
//                }
//            });
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

                    final ReceiverItemHolder receiverItemHolder = (ReceiverItemHolder) holder;
                    if (mList.get(position).getMessageType().equals(Message.MessageType.TEXT)){
                        receiverItemHolder.receiverMessage.setText(message);
                        receiverItemHolder.imageView.setVisibility(View.GONE);
                        receiverItemHolder.mediaLayout.setVisibility(View.GONE);


                    }

                   else if (mList.get(position).getMessageType().equals(Message.MessageType.Audio)){
                       receiverItemHolder.mediaLayout.setVisibility(View.VISIBLE);
                      // receiverItemHolder.seenImg.setVisibility(View.GONE);
                       receiverItemHolder.imageView.setVisibility(View.GONE);
                       receiverItemHolder.receiverMessage.setVisibility(View.GONE);

                    }
                    else {
                        if (mList.get(position).getImageUrl()!=null){
                            receiverItemHolder.mediaLayout.setVisibility(View.GONE);
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
                            mOnItemClickListener.onItemClick(v, receiverItemHolder.getAdapterPosition());
                        }
                    });
                    receiverItemHolder.play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isPlaying){
                                stopAudio();


                                receiverItemHolder.play.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                                isPlaying=false;




                            }
                            else {


                                String url = mList.get(position).getImageUrl(); // your URL here
                                playAudio(url);
                                receiverItemHolder.play.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_pause_black_24dp));

                                isPlaying=true;

                                //startPlaying()

                            }


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
                       senderItemHolder.senderMessage.setVisibility(View.VISIBLE);
                        senderItemHolder.senderMessage.setText(message);
                        senderItemHolder.imageView.setVisibility(View.GONE);
                        senderItemHolder.mediaLayout.setVisibility(View.GONE);


                    }
                    else if (mList.get(position).getMessageType().equals(Message.MessageType.Audio)){
                        senderItemHolder.mediaLayout.setVisibility(View.VISIBLE);
                        // receiverItemHolder.seenImg.setVisibility(View.GONE);
                        senderItemHolder.imageView.setVisibility(View.GONE);
                        senderItemHolder.senderMessage.setVisibility(View.GONE);


                    }
                    else if (mList.get(position).getMessageType().equals(Message.MessageType.IMAGE)) {
                        senderItemHolder.senderMessage.setVisibility(View.GONE);
                        senderItemHolder.mediaLayout.setVisibility(View.GONE);
                        senderItemHolder.imageView.setVisibility(View.VISIBLE);
                        if (mList.get(position).getImageUrl()!=null){
                            Picasso.get().load(mList.get(position).getImageUrl()).into(senderItemHolder.imageView);


                        }
                    }

                    senderItemHolder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(v, senderItemHolder.getAdapterPosition());
                        }
                    });


                    senderItemHolder.play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isPlaying){
                                stopAudio();


                             senderItemHolder.play.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                               isPlaying=false;




                            }
                            else {


                                String url = mList.get(position).getImageUrl(); // your URL here
                                playAudio(url);
                                senderItemHolder.play.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_pause_black_24dp));

                                isPlaying=true;

                                //startPlaying()

                            }

                        }
                    });


                }
                break;
                default:
                    break;
            }


    }
//    private void updateSeekBar(SeekBar seekBar){
//        if (mediaPlayer.isPlaying()){
//            seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
//        }
//    }

    private void playAudio(String url){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();


    }
    private void stopAudio(){

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
    CardView mediaLayout;
    ImageView play;
    SeekBar seekBar;


    public ReceiverItemHolder(@NonNull View itemView) {
        super(itemView);
        receiverImage=itemView.findViewById(R.id.circleImageView);
        receiverMessage=itemView.findViewById(R.id.receiver_message);
        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);
        imageView=itemView.findViewById(R.id.ImageView_image);
        seenImg=itemView.findViewById(R.id.seen_img);
        mediaLayout=itemView.findViewById(R.id.media_layout);
        seekBar=itemView.findViewById(R.id.seekbar);
        play=itemView.findViewById(R.id.play);



    }
}

class SenderItemHolder extends RecyclerView.ViewHolder {
    CircleImageView receiverImage;
    TextView senderMessage;
    ImageView imageView;
    TextView date, time;
    CardView mediaLayout;
    ImageView play;
    SeekBar seekBar;

    public SenderItemHolder(@NonNull View itemView) {
        super(itemView);

        senderMessage=itemView.findViewById(R.id.sender_message);

        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);
        imageView=itemView.findViewById(R.id.ImageView_image);
        mediaLayout=itemView.findViewById(R.id.media_layout);
        seekBar=itemView.findViewById(R.id.seekbar);
        play=itemView.findViewById(R.id.play);



    }


}
