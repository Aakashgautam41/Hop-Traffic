package com.example.aakash.hoptraffic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DepartmentActivity extends AppCompatActivity {

    ImageView downloadImage;
    EditText editText;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        downloadImage = findViewById(R.id.downloadImage);
        editText = findViewById(R.id.textView);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        final long ONE_MEGABYTE = 1024 * 1024;
        mStorageRef.child("Images/e1592af0-2fdc-4830-8ef6-d91f79f1ab7f").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Toast.makeText(DepartmentActivity.this,"Download Successful", Toast.LENGTH_SHORT).show();
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                downloadImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, downloadImage.getWidth(), downloadImage.getHeight(), false));
                editText.setText(bytes.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                editText.setText(e.toString());
                Toast.makeText(DepartmentActivity.this,"Download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
