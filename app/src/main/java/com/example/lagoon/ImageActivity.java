package com.example.lagoon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageActivity extends AppCompatActivity {
    // Creates an instance of the database and the storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Gets the reference to the storage and database
    StorageReference storageRef = storage.getReference();
    DatabaseReference databaseRef = database.getReference();

    // Vsriables for the parts in the display
    Button choose, upload;
    ImageView img;
    EditText name;

    public Uri imguri;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        // Connects specifically to the Images folder and table
        storageRef = FirebaseStorage.getInstance().getReference("Images");
        databaseRef = FirebaseDatabase.getInstance().getReference("Images");

        // Set variables to the actual displays by using their IDs
        choose = (Button)findViewById(R.id.c_file);
        upload = (Button)findViewById(R.id.u_file);
        name = (EditText)findViewById(R.id.filename);
        img = (ImageView)findViewById(R.id.imageView);


        // When the choose button is pressed go to choose file function to pick the image
        choose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ChooseFile();
            }
        });

        // When the upload button is pressed go to the file upload function
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FileUpload();
            }
        });
    }

    // Function that when called will take the user to the page containing all the images
    public void goto_view(View view){
        Intent intent = new Intent(this, ViewImgActivity.class);
        startActivity(intent);
    }
    
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    // This function uploads the image chosen to the Firebase cloud storage
    private void FileUpload(){
        if(imguri != null) {
            StorageReference ref = storageRef.child(System.currentTimeMillis() + "." +
                    getExtension(imguri));

            ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ImageActivity.this, "Image Uploaded Successfully",
                                    Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while(!urlTask.isSuccessful());
                                Uri downloadUrl = urlTask.getResult();
                                Upload upload = new Upload(name.getText()
                                        .toString().trim(),downloadUrl.toString());
                                String uploadId = databaseRef.push().getKey();
                                databaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(ImageActivity.this, "Image Unable to Upload",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else{
            Toast.makeText(ImageActivity.this, "No Image Selected",
                Toast.LENGTH_LONG).show();
        }
    }

    // Allows user to choose which image to upload
    private void ChooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri = data.getData();
            img.setImageURI(imguri);
        }
    }
}