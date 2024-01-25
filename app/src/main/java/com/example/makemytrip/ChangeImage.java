package com.example.makemytrip;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChangeImage extends AppCompatActivity {

    Button b1,b2;
    ImageView iv;
    Uri imageuri;
    ProgressDialog pd;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        b1=(Button) findViewById(R.id.select);
        b2=(Button) findViewById(R.id.upload);
        iv=(ImageView) findViewById(R.id.imageView3);

        mAuth = FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }
    private void uploadImage() {
        pd=new ProgressDialog(this);
        pd.setTitle("Uploading File....");
        pd.show();
        FirebaseUser user = mAuth.getCurrentUser();
        String UserId=user.getUid();
//        SessionManagement sessionManagement=new SessionManagement(this);
//        String UserId=sessionManagement.getSession();
        storageReference= FirebaseStorage.getInstance().getReference("UserProfileImages/"+UserId);
        storageReference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        iv.setImageURI(null);
                        Toast.makeText(ChangeImage.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                        if(pd.isShowing()){
                            pd.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangeImage.this, "upload Failed", Toast.LENGTH_SHORT).show();
                        if(pd.isShowing()){
                            pd.dismiss();
                        }
                    }
                });
    }
    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && data!=null && data.getData() !=null){
            imageuri=data.getData();
            iv.setImageURI(imageuri);
        }
    }
}