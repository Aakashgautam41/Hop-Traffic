package com.example.aakash.hoptraffic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class DepartmentActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        //Storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Getting listview from ui
        listView = findViewById(R.id.listview);

        CustomAdapter customAdapter = new CustomAdapter(mDatabase);
        listView.setAdapter(customAdapter);

    }

    //Custom Adapter class
    class CustomAdapter extends BaseAdapter{

        private ArrayList<DataSnapshot> mSnapShot;

        //Child event listener
        private ChildEventListener myListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mSnapShot.add(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Constructor of CustomAdapter
        public CustomAdapter(DatabaseReference ref){
            mDatabase = ref.child("complaints");
            mSnapShot = new ArrayList<>();

            //add listener
            mDatabase.addChildEventListener(myListener);

        }

        @Override
        public int getCount() {
            return mSnapShot.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_list_view,null);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView description = view.findViewById(R.id.textView_description);
            TextView location= view.findViewById(R.id.textView_location);


            //Setting values in listview
//            String value = Objects.requireNonNull(mSnapShot.get(i).getValue()).toString();
//            Log.i("JSON : ",value);

            try {

                JSONObject jsonObject = new JSONObject(mSnapShot.get(i).getValue().toString());
                String issue = (String) jsonObject.get("Issue");
                description.setText(issue);
                Log.i("Description :: ",issue);

                String loc = jsonObject.getString("Location");
                location.setText(loc);
                Log.i("Location :: ", loc);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("ERROR :: ", e.toString());
            }

            return view;
        }
    }

}
