package com.example.myfirebaseapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private EditText m_writeData,m_writeDataInt;
    private Button m_SendData,m_ReadData;
    private FirebaseDatabase m_Database;
    private DatabaseReference m_Ref;
    private String TAG="MyTag";
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        m_Database=FirebaseDatabase.getInstance();//this method will get the firebase whole instance;
        m_Ref=m_Database.getReference("users");//this will take up the root node of the firebase json db.
        storageReference= FirebaseStorage.getInstance().getReference("docs");//this will create the folder docs

        m_SendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });



        m_ReadData.setVisibility(View.INVISIBLE);

    }



    private void sendData() {
        StorageReference child = storageReference.child("images/image.jpg");
        Bitmap bitmap=readImage();

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        child.putBytes(byteArrayOutputStream.toByteArray())
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Image inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private Bitmap readImage()
    {
        InputStream inputStream=null;
        try {
            inputStream=getAssets().open("image.png");
            BitmapDrawable bitmapDrawable= (BitmapDrawable) Drawable.createFromStream(inputStream,null );
            return bitmapDrawable.getBitmap();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(inputStream!=null)
        {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private void initialize() {
        m_writeData=findViewById(R.id.write_data);
        m_writeDataInt=findViewById(R.id.write_int_data);
        m_SendData=findViewById(R.id.send_data);
        m_ReadData=findViewById(R.id.read_data);
    }
}
