package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText m_writeData,m_writeDataInt;
    private Button m_SendData,m_ReadData;
    private TextView m_data;

    private FirebaseDatabase m_Database;
    private DatabaseReference m_Ref;
    private String TAG="MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        m_Database=FirebaseDatabase.getInstance();//this method will get the firebase whole instance;
        m_Ref=m_Database.getReference("users");//this will take up the root node of the firebase json db.
        //here we are writing 'users' which will basically create a node inside the root instance with the same name.
        m_SendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });


        m_ReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

    }

    private void readData() {

        m_Ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String,Object> data= (Map<String, Object>) dataSnapshot.getValue();
                m_data.append(data.get("Name").toString()+data.get("Age"));
                //this function will iteratively call the child and then will present the data
                //here the datasnapshot consists each and every map of name and age.
                Log.d(TAG, "onChildAdded: "+data.get("Name").toString()+data.get("Age"));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//gives a failure report similar to OnFailure
            }
        });
    }


    private void sendData() {
        String writeData=m_writeData.getText().toString();
        int writeDataInt=Integer.parseInt(m_writeDataInt.getText().toString());
        String key=m_Ref.push().getKey();//this will always generate a Push id which is a unique id and using this
        //we can insert the data and it will not even udpate thr code

        Map<String ,Object> insertValues=new HashMap<>();
        insertValues.put("Name",writeData);
        insertValues.put("Age",writeDataInt);
        m_Ref.child(key).updateChildren(insertValues);//recommended way
        /**
         * an important code to update the values
         * as we know the ref are in child parent relationship and in mref is users so to update users1 data
         * we will mannualy update it as user1/Name,user1/Age etc.
         */
        Map<String,Object> updateValues=new HashMap<>();
        updateValues.put("/user1/Name",writeData);
        updateValues.put("user1/Age",writeDataInt);
        m_Ref.updateChildren(updateValues);
        m_Ref.child("user1").removeValue();//will delete the node
    }

    private void initialize() {
        m_writeData=findViewById(R.id.write_data);
        m_writeDataInt=findViewById(R.id.write_int_data);
        m_data=findViewById(R.id.text_data);
        m_SendData=findViewById(R.id.send_data);
        m_ReadData=findViewById(R.id.read_data);
    }
}
