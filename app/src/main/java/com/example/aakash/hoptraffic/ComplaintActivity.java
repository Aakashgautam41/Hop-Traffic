package com.example.aakash.hoptraffic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ComplaintActivity extends AppCompatActivity {

    EditText describe;
    EditText location;
    ImageView image;
    Button takePhoto;
    Button submit;
    private Uri filePath;

    //Request code for getting image
    private static final int Gallary = 10;

    //Getting firebase instance
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Setting root reference
    DatabaseReference myRef = database.getReference("complaints");
    //Getting firebase storage reference
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        //Firebase storage instance
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Getting UI elements
        describe = findViewById(R.id.describe);
        location = findViewById(R.id.location);
        takePhoto = findViewById(R.id.takePhoto);
        image = findViewById(R.id.imageView);
        submit = findViewById(R.id.submit);

        //Setting onClickListener on button to open camera
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,Gallary);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Gallary);
                image .setVisibility(View.VISIBLE);
            }
        });

        //Setting OnClickListener on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIssue = describe.getText().toString();
                String userLocation = location.getText().toString();

                boolean cancel = false;
                View focusView = null;

                //Description field validation
                if(TextUtils.isEmpty(userIssue)){
                    describe.setError("Required field is empty");
                    focusView = describe;
                    cancel = true;
                }

                //Location field validation
                if (TextUtils.isEmpty(userLocation)){
                    location.setError("Required field is empty");
                    focusView = location;
                    cancel = true;
                }

                if (cancel){
                    focusView.requestFocus();
                }
                else {
                    HashMap<String,String> userComplaints = new HashMap<>();
                    userComplaints.put("Issue",userIssue);
                    userComplaints.put("Location",userLocation);
                    myRef.push().setValue(userComplaints);

                    Toast.makeText(ComplaintActivity.this, "Your Complaint has been successfully registered", Toast.LENGTH_SHORT).show();

                    //Uploading image to firebase

                    final StorageReference fileName = mStorageRef.child("Images/"+ UUID.randomUUID().toString());
                    fileName.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Toast.makeText(ComplaintActivity.this,"Upload Successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.i("ERROR","ERROR : "+ exception.toString());
                                    describe.setText(exception.toString());
                                    location.setText(fileName.toString());
                                    Toast.makeText(ComplaintActivity.this,"Upload Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    //Overriding method to set image in image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallary){
//            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//            image.setImageBitmap(bitmap);

//            Uri file = data.getData();
//            image.setImageURI(file);

            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(bitmap);
//            image.setImageURI(filePath);


        }
    }
}
