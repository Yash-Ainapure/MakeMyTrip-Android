package com.example.makemytrip;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MyAccount extends AppCompatActivity {


    Button dobPicker , expiryPicker,saveButton;
    ImageView profileImage;
    private String selectedDates="1/02/2024";

    String UserId;
    EditText acc_email_edt,acc_phone_edt,acc_name_edt,acc_gender_edt;
    EditText acc_city_edt,acc_state_edt,acc_nationality_edt,acc_PassNo_edt,acc_issuecountry_edt,acc_PanNo_edt;
    private DatabaseReference databaseReference;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        acc_gender_edt=findViewById(R.id.acc_gender_edt);
        acc_city_edt=findViewById(R.id.acc_city_edt);
        acc_state_edt=findViewById(R.id.acc_state_edt);
        acc_nationality_edt=findViewById(R.id.acc_nationality_edt);
        acc_PassNo_edt=findViewById(R.id.acc_PassNo_edt);
        acc_issuecountry_edt=findViewById(R.id.acc_issuecountry_edt);
        acc_PanNo_edt=findViewById(R.id.acc_PanNo_edt);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        getSupportActionBar().setTitle("Edit Profile");
        UserId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        saveButton=findViewById(R.id.saveData);
        acc_phone_edt = findViewById(R.id.acc_phone_edt);
        acc_name_edt = findViewById(R.id.acc_name_edt);
        acc_email_edt=findViewById(R.id.acc_email_edt);
        profileImage=findViewById(R.id.profileImage);
        dobPicker = findViewById(R.id.acc_dob_edt);
        expiryPicker = findViewById(R.id.acc_expiry_edt);
        dobPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dobPicker);
            }
        });
        expiryPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(expiryPicker);
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(MyAccount.this);
                alert.setMessage("Do you want to change profile image?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in=new Intent(MyAccount.this,ChangeImage.class);
                        startActivity(in);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog=alert.create();
                alertDialog.show();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UserId).child("userInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo=snapshot.getValue(UserInfo.class);
                acc_name_edt.setText(userInfo.getFirstName());
                acc_email_edt.setText(userInfo.getEmail());
                acc_phone_edt.setText(userInfo.getPhoneNumber());

                if(!userInfo.getGender().equals(""))
                    acc_gender_edt.setText(userInfo.getGender());
                if(!userInfo.getCity().equals(""))
                    acc_city_edt.setText(userInfo.getCity());
                if(!userInfo.getState().equals(""))
                    acc_state_edt.setText(userInfo.getState());
                if(!userInfo.getNationality().equals(""))
                    acc_nationality_edt.setText(userInfo.getNationality());
                if(!userInfo.getPassportNo().equals(""))
                    acc_PassNo_edt.setText(userInfo.getPassportNo());
                if(!userInfo.getIssuingCountry().equals(""))
                    acc_issuecountry_edt.setText(userInfo.getIssuingCountry());
                if(!userInfo.getPanCard().equals(""))
                    acc_PanNo_edt.setText(userInfo.getPanCard());
                if(!userInfo.getDob().equals(""))
                    dobPicker.setText(userInfo.getDob());
                if(!userInfo.getPanExpiry().equals(""))
                    expiryPicker.setText(userInfo.getPanExpiry());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyAccount.this, "failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
        StorageReference storageReference;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String Uid=user.getUid();

        storageReference= FirebaseStorage.getInstance().getReference("UserProfileImages/"+Uid);
        try {
            File localfile= File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    DisplayMetrics dm=new DisplayMetrics();
                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    profileImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyAccount.this, "failed to load profile image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveButton.setOnClickListener(v->{
            UserInfo info=new UserInfo(FirebaseAuth.getInstance().getUid(),acc_name_edt.getText().toString(),acc_phone_edt.getText().toString()
                    ,acc_email_edt.getText().toString(),acc_gender_edt.getText().toString(),acc_city_edt.getText().toString()
                    ,acc_state_edt.getText().toString(),acc_nationality_edt.getText().toString()
            ,acc_PassNo_edt.getText().toString(),acc_issuecountry_edt.getText().toString()
                    ,acc_PanNo_edt.getText().toString(),expiryPicker.getText().toString(),dobPicker.getText().toString());

            databaseReference.setValue(info);
            Toast.makeText(this, "data saved successfully", Toast.LENGTH_SHORT).show();

        });

    }

    private void loadProfileImage() {
        StorageReference storageReference;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String Uid = user.getUid();

        storageReference = FirebaseStorage.getInstance().getReference("UserProfileImages/" + Uid);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    DisplayMetrics dm = new DisplayMetrics();
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    profileImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyAccount.this, "failed to load profile image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Call the method to load profile image when the activity resumes
        loadProfileImage();
    }

    private void showDatePickerDialog(Button buttonDatePickerx) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                selectedDates = dayOfMonth + "/" + (month + 1) + "/" + year;
                //buttonDatePicker.setText(selectedDates);
                buttonDatePickerx.setText(selectedDates);
            }
        };
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MyAccount.this,
                dateSetListener,
                year, month, day
        );
        datePickerDialog.show();
    }
}