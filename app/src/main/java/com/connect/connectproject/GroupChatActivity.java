package com.connect.connectproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.connect.connectproject.Adapter.ChatAdapter;
import com.connect.connectproject.Models.MessageModel;
import com.connect.connectproject.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.backkArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userName.setText("Group Chat");

        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Group Chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        messageModels.clear();

                        //Fetch and Show the Data
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(model);

                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });


        //Send Message Click Func
        binding.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a String variable for get text from when entering a message in a text box
                final String message = binding.enterMessage.getText().toString();

                //Create an object for MessageModel Class
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());

                //For set the Message on DB
                binding.enterMessage.setText("");
                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(GroupChatActivity.this, "Message Send", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
}