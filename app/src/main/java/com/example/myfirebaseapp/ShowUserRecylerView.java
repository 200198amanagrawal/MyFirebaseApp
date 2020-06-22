package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowUserRecylerView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> list;
    private FirebaseDatabase m_Database;
    private DatabaseReference m_Ref;
    private ChildEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_recyler_view);
        recyclerView=findViewById(R.id.user_view);
        m_Database=FirebaseDatabase.getInstance();
        m_Ref=m_Database.getReference("users");
        list=new ArrayList<>();
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_bar_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.search_view);
        SearchView searchView= (SearchView) searchItem.getActionView();

//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.getFilter().filter(query);
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
