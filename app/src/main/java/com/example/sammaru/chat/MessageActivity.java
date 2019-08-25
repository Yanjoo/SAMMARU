package com.example.sammaru.chat;

import android.os.Bundle;

import com.example.sammaru.model.ChatModel;
import com.example.sammaru.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sammaru.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MessageActivity extends AppCompatActivity {

    private String myUid;
    private String destinationUid;
    private String chatRoomUid;

    private TextView name;
    private EditText editText;
    private ImageView macro;
    private ImageView send;
    private ImageView back;
    private ImageView phone;

    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destinationUid = getIntent().getStringExtra("destinationUid");

        name = findViewById(R.id.activity_message_name);
        editText = findViewById(R.id.activity_message_editText);
        macro = findViewById(R.id.activity_message_macro);
        send = findViewById(R.id.activity_message_send);
        back = findViewById(R.id.activity_message_back);
        phone = findViewById(R.id.activity_message_phone);

        recyclerView = findViewById(R.id.activity_message_recyclerview);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myUid, true);
                chatModel.users.put(destinationUid, true);

                if (chatRoomUid == null) {
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = myUid;
                    comment.message = editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatrooms")
                            .child(chatRoomUid).child("comments").push().setValue(comment);
                }


            }
        });
        checkChatRoom();
    }

    // checkChatRoom : DB에 있는 채팅 목록 불러오기
    private void checkChatRoom() {
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+myUid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if (chatModel.users.containsKey(destinationUid)) {
                        chatRoomUid = item.getKey();
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new MessageListAdpater());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class MessageListAdpater extends RecyclerView.Adapter {
        
        List<ChatModel.Comment> comments;
        UserModel userModel;
        
        public MessageListAdpater() {
            comments = new ArrayList<>();
            
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void getMessageList() {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ChatModel.Comment comment = item.getValue(ChatModel.Comment.class);
                        comments.add(comment);
                    }
                    notifyDataSetChanged(); // 메시지 갱신

                    recyclerView.scrollToPosition(comments.size() - 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_message, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);

            // 내가 보낸 메시지
            if (comments.get(position).uid.equals(myUid)) {
                messageViewHolder.message.setText(comments.get(position).message);
                messageViewHolder.message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.destination.setVisibility(View.INVISIBLE);
                messageViewHolder.main.setGravity(Gravity.RIGHT);
            } else { // 상대방이 보낸 메시지
                messageViewHolder.name.setText(userModel.getName());
                messageViewHolder.destination.setVisibility(View.VISIBLE);
                messageViewHolder.message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.message.setText(comments.get(position).message);
                messageViewHolder.main.setGravity(Gravity.LEFT);
            }

            /*long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.timestamp.setText(time);*/

        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView message;
            public TextView name;
            public TextView timestamp;
            public ImageView imageView;
            public LinearLayout destination;
            public LinearLayout main;

            public MessageViewHolder(View view) {
                super(view);
                message = view.findViewById(R.id.messageItem_textview_message);
                name = view.findViewById(R.id.messageItem_textview_name);
                imageView = view.findViewById(R.id.messageItem_imageview_profile);
                destination = view.findViewById(R.id.messageItem_linearlayout_destination);
                main = view.findViewById(R.id.messageItem_linearlayout_main);
                timestamp = view.findViewById(R.id.messageItem_textview_timestamp);
            }
        }
    }

    // onBackPressed : 뒤로가기 버튼 눌었을때
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }


}
