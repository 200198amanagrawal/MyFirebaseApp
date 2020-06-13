package com.example.myfirebaseapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EditText m_writeData, m_writeDataInt;
    private Button m_SendData, m_ReadData;
    private FirebaseDatabase m_Database;
    private DatabaseReference m_Ref;
    private String TAG = "MyTag";
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private ProgressBar mProgressBar;
    private boolean mGranted;
    private TextView mProgressText;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        m_Database = FirebaseDatabase.getInstance();//this method will get the firebase whole instance;
        m_Ref = m_Database.getReference("users");//this will take up the root node of the firebase json db.
        storageReference = FirebaseStorage.getInstance().getReference("docs");//this will create the folder docs

        m_SendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });


        m_ReadData.setVisibility(View.INVISIBLE);
        getPermissions();

    }


    private void sendData() {
//        //this particular code will ask the permission inside the app to open gallery
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!mGranted) {
//                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//                    return;
//                }
//            }
//        }
//        //code to open gallery as sson as this is triggered onAcrivityResult will be called
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
//        final File outputFile = new File(Environment.getExternalStorageDirectory(), "image.png");
//        long ONE_MEGABYTE = 1024 * 1024;
//        storageReference.child("images/image.jpg").getBytes(ONE_MEGABYTE)//hardocded image
//                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//
//                        mImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
//                        Toast.makeText(MainActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();
//
//                        try {
//                            FileOutputStream fos = new FileOutputStream(outputFile);
//                            fos.write(bytes);
//                            fos.close();//this code will store the image with the name given above
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, "Download error", Toast.LENGTH_SHORT).show();
//                    }
//                });
        /*
        File outputFile = new File(Environment.getExternalStorageDirectory(), "mynewimage.jpeg");

        long ONE_MEGABYTE = 1024 * 1024;

        mRef.child("images/90").getFile(outputFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(MainActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: File Downloaded: " + outputFile);

                        mImageView.setImageURI(Uri.fromFile(outputFile));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Download error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: Error: " + e.getMessage());
                    }
                });
         */
        storageReference.child("images/image.jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(MainActivity.this, "URL received", Toast.LENGTH_SHORT).show();

                        downloadFile(uri);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void downloadFile(Uri uri) {

        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle("File Download")//this will appear as a noti while downloading
                .setDescription("This is file download demo")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file));

        downloadManager.enqueue(request);

    }
    private Bitmap readImage() {
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("image.png");
            BitmapDrawable bitmapDrawable = (BitmapDrawable) Drawable.createFromStream(inputStream, null);
            return bitmapDrawable.getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void initialize() {
        m_writeData = findViewById(R.id.write_data);
        m_writeDataInt = findViewById(R.id.write_int_data);
        m_SendData = findViewById(R.id.send_data);
        m_ReadData = findViewById(R.id.read_data);
        mProgressBar = findViewById(R.id.progress_circular);
        mProgressText = findViewById(R.id.loading_text);
        mImageView=findViewById(R.id.imageView);
    }

    private void getPermissions() {

        String externalReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        String externalWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, externalReadPermission) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, externalWritePermission) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{externalReadPermission, externalWritePermission}, STORAGE_PERMISSION_CODE);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                mGranted = true;
            } else {
                Toast.makeText(this, "Please allow the permission to read data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null) {

            Uri imageUri = data.getData();
            StorageReference child = storageReference.child("images/" + imageUri.getLastPathSegment());
            ;
            //basically the child will be stored with this particular imagename that is given above
            // with its proper last directory it is stored.
            UploadTask uploadTask = child.putFile(imageUri);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressText.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(false);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mProgressBar.setProgress((int) progress, true);
                    }
                    mProgressText.setText(progress + " %");

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        //when the task is completed successfully or usuccessfully it will notify accordingly
                        Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        mProgressText.append(" File Uploaded");
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Timer t = new Timer(false);
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        mProgressText.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }, 5000);
                    }
                }
            });

        }
    }
}
