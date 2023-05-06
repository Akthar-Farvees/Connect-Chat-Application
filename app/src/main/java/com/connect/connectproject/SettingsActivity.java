
package com.connect.connectproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.connect.connectproject.Models.Users;
import com.connect.connectproject.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        //For Back Navigation Functionality
        binding.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //Save Button Functionality
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check weather the Text fields are empty or not
                if(!binding.editTextTextStatus.getText().toString().equals("") && !binding.editTextTextStatus.getText().toString().equals("")){

                    //Initialize the value for both About and Username Text field in the settings layout
                    String status = binding.editTextTextStatus.getText().toString();
                    String userName = binding.editTextTextUserName.getText().toString();

                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("userName", userName);
                    obj.put("status",status);

                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);

                    Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }

             else {
                    Toast.makeText(SettingsActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //Fetch the image from database
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //Get the image in the avtar in settings
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfilePic())
                                .placeholder(R.drawable.avatar)
                                .into(binding.profilePicture);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });



        //Plus button Click Listner
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 25 && resultCode == RESULT_OK && data != null) {
            Uri sFile = data.getData();
            binding.profilePicture.setImageURI(sFile);

            //Creating a folder in the Firebase Storage
            final StorageReference reference = storage.getReference().child("profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        //Store the image url under the Users in the RealTime DB
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(SettingsActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}