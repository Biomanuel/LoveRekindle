package com.reconciliationhouse.android.loverekindle.ui.chat;


import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.chat.ChatAdapter;
import com.reconciliationhouse.android.loverekindle.dialog.InviteUserDialog;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.ChatType;
import com.reconciliationhouse.android.loverekindle.models.Message;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.models.UserSender;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;
import com.reconciliationhouse.android.loverekindle.utils.NetworkCheck;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private ChatViewModel mViewModel;

    private FirebaseFirestore db;
    private UserModel userModel;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    String name;
    private ChatAdapter adapter;
   CollectionReference messagesReference;
    CollectionReference reference;
    ChatItem model;
    EmojIconActions emojIcon;
    CircleImageView circleImageView;
    TextView counsellorUsername;
    ImageButton addPhoto,addVoiceRecording;
    EmojiconEditText messagesText;
    Toolbar materialToolbar;
    ImageButton sendMessage;
    ImageView addEmoji,showImage;
    RecyclerView  messagesRecyclerView;
    FirebaseStorage fileStorage;
    ProgressBar progressBar ;
    CoordinatorLayout bottomSheetLayout;
    BottomSheetBehavior bottomSheetBehavior;
    private String recordPermission= RECORD_AUDIO;
     List<Message>mMessages;
    private MediaRecorder mRecorder;


    private boolean isRecording =false;

    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    MediaPlayer mPlayer;

    private static final int GALLERY_PICK=123;
    private Chronometer timer;
    private  File audio;
    private String filePath;


    public static ChatFragment newInstance() {
        return new ChatFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;

        view =inflater.inflate(R.layout.chat_fragment,container,false);

       counsellorUsername=view.findViewById(R.id.counsellor_username);
       messagesText=view.findViewById(R.id.messages_text);
       circleImageView=view.findViewById(R.id.circleImageView);
       materialToolbar=view.findViewById(R.id.materialToolbar);
       addEmoji=view.findViewById(R.id.add);
       timer=view.findViewById(R.id.timer);
        messagesText.setEmojiconSize(100);
        progressBar=view.findViewById(R.id.progress_bar);
       sendMessage=view.findViewById(R.id.send_message);
       addPhoto=view.findViewById(R.id.add_photo);
       addVoiceRecording=view.findViewById(R.id.record_voice);

       messagesRecyclerView=view.findViewById(R.id.messages_recycler_view);
       showImage=view.findViewById(R.id.show_image);
        emojIcon = new EmojIconActions(getContext(), messagesText.getRootView(), messagesText,addEmoji );
        emojIcon.setUseSystemEmoji(true);


        emojIcon.ShowEmojIcon();


        emojIcon.setUseSystemEmoji(true);
        messagesText.setUseSystemDefault(true);




        //seenMessages();
        return view;

    }
    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.chat_menu, menu);
        if (model.getChatType().equals(ChatItem.ChatType.Single_Chat)){
            menu.findItem(R.id.add_friend).setVisible(false);
            menu.findItem(R.id.add_call).setVisible(false);
        }
        else {
            menu.findItem(R.id.add_friend).setVisible(true);
            menu.findItem(R.id.add_call).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_friend:{
                FragmentManager fm = ChatFragment.this.getActivity().getSupportFragmentManager();
               InviteUserDialog custom=new InviteUserDialog(model.getChatId(),model.getChatType(),model.getImageUrl());
                custom.show(fm,"");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetLayout=view.findViewById(R.id.bottom_sheet_layout_1);
        setUpBottomSheet();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {



            progressBar.setVisibility(View.VISIBLE);
            Uri imageUri = data.getData();
            assert imageUri != null;
            String imageName = imageUri.getLastPathSegment();
            fileStorage = FirebaseStorage.getInstance();
            String path = null;


            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            CollectionReference reference1 = null;


            if (model.getChatType().equals(ChatItem.ChatType.Single_Chat)) {
                String ref = null;
                if (String.valueOf(UserRepo.user.getRole()).equals(String.valueOf(UserModel.Role.Regular))) {

                    reference1 = db1.collection("Chat").document("single").collection(name + " and " + firebaseUser.getDisplayName());
                    path = name + " and " + firebaseUser.getDisplayName();

                } else {
                    reference1 = db1.collection("Chat").document("single").collection(firebaseUser.getDisplayName() + " and " + name);
                    path = firebaseUser.getDisplayName() + " and " + name;
                }
                String push_id = reference1.getId();

                final StorageReference filepath = fileStorage.getReference("images/chat/" + path).child(imageName + ".jpg");

                Task<UploadTask.TaskSnapshot> uploadTask = filepath.putFile(imageUri);

                final CollectionReference finalReference = reference1;
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();


                            final Message message = new Message(Message.MessageType.IMAGE, new UserSender(firebaseUser.getUid(), firebaseUser.getDisplayName(), String.valueOf(firebaseUser.getPhotoUrl())), String.valueOf(downloadUri), false);

                            finalReference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    progressBar.setVisibility(View.GONE);
                                    messagesText.setText("");


                                }
                            });


                        } else {
                            // Handle failures
                            // ...
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(getView(), "Error sending image ", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });


//                reference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if (task.isSuccessful()) {
//                            messagesText.setText("");
////
//                        }
//                    }
//                });
            } else {
                reference1 = db1.collection("Chat").document("group").collection(model.getChatId());
                String push_id = reference1.getId();

                final StorageReference filepath = fileStorage.getReference("images/chat/" + path).child(imageName + ".jpg");

                Task<UploadTask.TaskSnapshot> uploadTask = filepath.putFile(imageUri);

                final CollectionReference finalReference1 = reference1;
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();


                            final Message message = new Message(Message.MessageType.IMAGE, new UserSender(firebaseUser.getUid(), firebaseUser.getDisplayName(), String.valueOf(firebaseUser.getPhotoUrl())), String.valueOf(downloadUri), false);

                            finalReference1.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    progressBar.setVisibility(View.GONE);
                                    messagesText.setText("");


                                }
                            });


                        } else {
                            // Handle failures
                            // ...
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(getView(), "Error sending image ", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });


            }


        }
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) requireActivity()).setSupportActionBar(materialToolbar);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);


        if (getArguments() != null) {



            ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
            Gson gson = new Gson();
             model = gson.fromJson(args.getCounsellorData(), ChatItem.class);

            name = model.getName();

            if (model.getChatType().equals(ChatItem.ChatType.Single_Chat)) {
                counsellorUsername.setText(model.getName());
                if (model.getImageUrl() == null) {
                    circleImageView.setImageResource(R.drawable.profile);
                } else {
                    Picasso.get().load(model.getImageUrl()).into(circleImageView);
                }


            }
            else {
              counsellorUsername.setText(model.getChatId());
            }
        }


               mMessages=new ArrayList<>();
              adapter = new ChatAdapter(model.getChatType(), new ChatAdapter.OnItemClickListener() {
                  @Override
                  public void onItemClick(View view, int position) {
                      Message message=mMessages.get(position);
                      if (message.getMessageType().equals(Message.MessageType.IMAGE)){
                          Picasso.get().load(message.getImageUrl()).into(showImage);
                          Toast.makeText(getContext(),mMessages.get(position).getDateCreated().toString(),Toast.LENGTH_SHORT).show();
                          bottomSheetLayout.setVisibility(View.VISIBLE);
                       bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                          DisplayMetrics metrics = view.getContext().getResources().getDisplayMetrics();
//                            int width = metrics.widthPixels;
//                            int height = metrics.heightPixels;
//                            int totalScreenHeight=(60*100)*height;



                      }





                  }
              });


              if (model.getChatType().equals(ChatItem.ChatType.Single_Chat)){
        if (String.valueOf(UserRepo.user.getRole()).equals(String.valueOf(UserModel.Role.Regular))){
            mViewModel.getAllSingleChat(name,firebaseUser.getDisplayName()).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
                @Override
                public void onChanged(List<Message> messages) {

                    mMessages=messages;
                    adapter.setMessages(messages);
                    messagesRecyclerView.scrollToPosition(messages.size() - 1);

                }

            });
            messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            messagesRecyclerView.setAdapter(adapter);
        }
        else {
            mViewModel.getAllSingleChat(firebaseUser.getDisplayName(),name).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
                @Override
                public void onChanged(List<Message> messages) {


                    adapter.setMessages(messages);
                    messagesRecyclerView.scrollToPosition(messages.size() - 1);
                    messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                    messagesRecyclerView.setAdapter(adapter);

                }

            });

        }}
              else {
                  mViewModel.getAllGroupChat(model.getChatId()).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
                      @Override
                      public void onChanged(List<Message> messages) {


                          adapter.setMessages(messages);
                          messagesRecyclerView.scrollToPosition(messages.size() - 1);

                      }

                  });
                  messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                  messagesRecyclerView.setAdapter(adapter);

              }



              addVoiceRecording.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {


                      if (isRecording){
                          if (mRecorder!=null){
                          stopRecording();}


                          addVoiceRecording.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_voice_black_24dp));
                          isRecording=false;
                          addEmoji.setVisibility(View.VISIBLE);
                          timer.setVisibility(View.GONE);
                          showDialog(getActivity());
                      }

                      else {
                          if (CheckPermissions()){
                               startRecording();
                               addEmoji.setVisibility(View.GONE);
                               timer.setVisibility(View.VISIBLE);
                              isRecording=true;
                              addVoiceRecording.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_none_black_24dp));
                          }


                      }
                     // Toast.makeText(getContext(), "Clicked", Toast.LENGTH_LONG).show();
//                      if(CheckPermissions()) {
//
//                          Toast.makeText(getContext(), "Passed", Toast.LENGTH_LONG).show();
//
//                          mRecorder = new MediaRecorder();
//                          mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
//                              @Override
//                              public void onInfo(MediaRecorder mr, int what, int extra) {
//
//                              }
//                          });
//                          ;

//
//                      }
//                      else
//                      {
//                          RequestPermissions();
//                      }

                  }
              });




           addPhoto.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent galleryIntent=new Intent();
                   galleryIntent.setType("image/*");
                   galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                   startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
               }
           });

            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String text = Objects.requireNonNull(messagesText.getText()).toString();
                    if (!(TextUtils.isEmpty(text))) {


                            Message message = new Message(Message.MessageType.TEXT, text, new UserSender(firebaseUser.getUid(), firebaseUser.getDisplayName(), String.valueOf(firebaseUser.getPhotoUrl())), false);
                            db = FirebaseFirestore.getInstance();

                            if (model.getChatType().equals(ChatItem.ChatType.Single_Chat)) {
                                if (String.valueOf((UserRepo.user.getRole())).equals("Regular")) {
                                    reference = db.collection("Chat").document("single").collection(name + " and " + firebaseUser.getDisplayName());
                                } else {
                                    reference = db.collection("Chat").document("single").collection(firebaseUser.getDisplayName() + " and " + name);

                                }

                                reference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            messagesText.setText("");
//
                                        }
                                    }
                                });
                            } else {
                                reference = db.collection("Chat").document("group").collection(model.getChatId());
                                reference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            messagesText.setText("");
                                        }

                                    }
                                });

                            }
                        }


                    }
                } );

        }

    private void stopRecording(){

        timer.stop();
        mRecorder.stop();
        mRecorder.release();
        mRecorder=null;

    }
    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        mRecorder=new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mFileName = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat format=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US);
        Date date=new Date();
        String file1 = "AudioRecording "+format.format(date)+".mp3";
        mRecorder.setOutputFile(mFileName+"/"+file1);
        filePath=mFileName+"/"+file1;
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
        Toast.makeText(getContext(), "Recording Started", Toast.LENGTH_LONG).show();
    }

    public void seenMessages(){
        if (model.getChatType().equals(ChatItem.ChatType.Single_Chat)){
            messagesReference=db.collection("Chat").document("single") .collection(counsellorUsername+" and "+name);



            messagesReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        Message message = dc.getDocument().toObject(Message.class);
                        String id =dc.getDocument().getId();
                        UserSender sender=message.getUserSender();
                        if (!sender.getUserId().equals(firebaseUser.getUid())){
                            message.setSeen(true);
                            DocumentReference reference=db.collection("Chat").document("single") .collection(counsellorUsername+" and "+name).document(id);
                            reference.update("isSeen",true);



                        }
                    }

                }
            });

        }
        else {
            messagesReference=db.collection("Chat").document("group") .collection(model.getChatId());

            messagesReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        Message message = dc.getDocument().toObject(Message.class);
                        String id =dc.getDocument().getId();
                        UserSender sender=message.getUserSender();
                        if (!sender.getUserId().equals(firebaseUser.getUid())){
                            message.setSeen(true);
                            DocumentReference reference=db.collection("Chat").document("group").collection(model.getChatId()).document(id);
                            reference.update("isSeen",true);



                        }
                    }

                }
            });


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


    public boolean CheckPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(),recordPermission)==PackageManager.PERMISSION_GRANTED){
            return  true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),new  String[]{recordPermission},REQUEST_AUDIO_PERMISSION_CODE);
            return false;
        }


    }
    private void   showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.record_audio_lay);


        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button send = (Button) dialog.findViewById(R.id.send);
        final ImageView play=dialog.findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio = new File(filePath);





                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(audio.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();







            }
        });


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();
                      audio = new File(filePath);
                    dialog.dismiss();
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    CollectionReference reference1 = null;
                    String path = null;
                    fileStorage = FirebaseStorage.getInstance();


                    if (model.getChatType().equals(ChatItem.ChatType.Single_Chat)) {
                        if (UserPreferences.getRole(getContext()).equals(String.valueOf(UserModel.Role.Regular))) {

                            reference1 = db1.collection("Chat").document("single").collection(name + " and " + firebaseUser.getDisplayName());
                            path = name + " and " + firebaseUser.getDisplayName();
                        } else {
                            reference1 = db1.collection("Chat").document("single").collection(firebaseUser.getDisplayName() + " and " + name);
                            path = firebaseUser.getDisplayName() + " and " + name;
                        }
                        String push_id = reference1.getId();

                        final StorageReference filepath = fileStorage.getReference("audio/chat/" + path).child(audio.getName());

                        Task<UploadTask.TaskSnapshot> uploadTask = filepath.putFile(Uri.fromFile(audio));

                        final CollectionReference finalReference = reference1;
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return filepath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    audio.delete();

                                    Uri downloadUri = task.getResult();


                                    final Message message = new Message(Message.MessageType.Audio, new UserSender(firebaseUser.getUid(), firebaseUser.getDisplayName(), String.valueOf(firebaseUser.getPhotoUrl())), String.valueOf(downloadUri), false);

                                    finalReference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            progressBar.setVisibility(View.GONE);
                                            messagesText.setText("");


                                        }
                                    });


                                } else {
                                    // Handle failures
                                    // ...
                                    progressBar.setVisibility(View.GONE);
                                    Snackbar.make(getView(), "Error sending image ", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });


//                reference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if (task.isSuccessful()) {
//                            messagesText.setText("");
////
//                        }
//                    }
//                });
                    } else {

                        reference1 = db1.collection("Chat").document("group").collection(model.getChatId());
                        String push_id = reference1.getId();
                        path = model.getChatId();

                        final StorageReference filepath = fileStorage.getReference("audio/chat/" + path).child(audio.getName());

                        Task<UploadTask.TaskSnapshot> uploadTask = filepath.putFile(Uri.fromFile(audio));

                        final CollectionReference finalReference = reference1;
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return filepath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    audio.delete();

                                    Uri downloadUri = task.getResult();


                                    final Message message = new Message(Message.MessageType.Audio, new UserSender(firebaseUser.getUid(), firebaseUser.getDisplayName(), String.valueOf(firebaseUser.getPhotoUrl())), String.valueOf(downloadUri), false);

                                    finalReference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            progressBar.setVisibility(View.GONE);
                                            messagesText.setText("");


                                        }
                                    });


                                } else {
                                    // Handle failures
                                    // ...
                                    progressBar.setVisibility(View.GONE);
                                    Snackbar.make(getView(), "Error sending image ", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });



                    }

                }


            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    File audio = new File(filePath);
                    audio.delete();
                    //Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();


                    dialog.dismiss();
                }
            });


            dialog.show();

        }
    }




