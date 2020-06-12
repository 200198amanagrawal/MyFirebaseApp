package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText m_writeData,m_writeDataInt;
    private Button m_SendData,m_ReadData;
    private TextView m_data;

    private FirebaseDatabase m_Database;
    private DatabaseReference m_Ref;
    private String TAG="MyTag";
    private ChildEventListener listener;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> list;

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

        list=new ArrayList<>();

        m_ReadData.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter=new UserAdapter(this,list);
        recyclerView.setAdapter(userAdapter);
        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(dataSnapshot.getValue(User.class));
                userAdapter.notifyDataSetChanged();//this will notfy the recycler view for any change in data
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
        };
        m_Ref.addChildEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_Ref.removeEventListener(listener);
    }

    private void sendData() {
        String writeData=m_writeData.getText().toString();
        String writeDataInt=m_writeDataInt.getText().toString();
        String key=m_Ref.push().getKey();
        User person=new User(writeData,writeDataInt);
        m_Ref.child(key).setValue(person);

    }

    private void initialize() {
        m_writeData=findViewById(R.id.write_data);
        m_writeDataInt=findViewById(R.id.write_int_data);
        m_data=findViewById(R.id.text_data);
        m_SendData=findViewById(R.id.send_data);
        m_ReadData=findViewById(R.id.read_data);
        recyclerView=findViewById(R.id.user_recyclerview);
    }
}
