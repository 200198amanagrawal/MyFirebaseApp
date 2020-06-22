package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

        m_writeData.setVisibility(View.GONE);
        m_ReadData.setVisibility(View.GONE);
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
                User user=dataSnapshot.getValue(User.class);
                list.add(user);
                user.setUid(dataSnapshot.getKey());
                userAdapter.notifyDataSetChanged();//this will notfy the recycler view for any change in data

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //if we remove the data from adaptor then this function will be called
                User user=dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());
                list.remove(user);
                userAdapter.notifyDataSetChanged();
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
        Intent intent=new Intent(MainActivity.this,ShowUserRecylerView.class);
        startActivity(intent);
    }

    private void initialize() {
        m_writeData=findViewById(R.id.write_data);
        m_writeDataInt=findViewById(R.id.write_int_data);
        m_SendData=findViewById(R.id.send_data);
        m_ReadData=findViewById(R.id.read_data);
        recyclerView=findViewById(R.id.user_recyclerview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_bar_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.search_view);
        SearchView searchView= (SearchView) searchItem.getActionView();

//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

}
