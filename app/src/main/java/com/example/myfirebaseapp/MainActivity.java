package com.example.myfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText m_writeData;
    private Button m_SendData;
    private TextView m_data;

    private FirebaseDatabase m_Database;
    private DatabaseReference m_Ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        m_Database=FirebaseDatabase.getInstance();//this method will get the firebase whole instance;
        m_Ref=m_Database.getReference();//this will take up the root node of the firebase json db.

        m_SendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    private void sendData() {
        String writeData=m_writeData.getText().toString();
        m_Ref.setValue(writeData);//this function will attach the data with rootref as key and writedata as value.
        //this data will always be updated and will not be added with new data by this method.
    }

    private void initialize() {
        m_writeData=findViewById(R.id.write_data);
        m_data=findViewById(R.id.text_data);
        m_SendData=findViewById(R.id.send_data);
    }
}
