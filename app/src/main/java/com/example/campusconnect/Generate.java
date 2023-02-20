package com.example.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.io.FileOutputStream;

public class Generate extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Read read = new Read();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        // Declare Components
        EditText id = findViewById(R.id.id_EditText);
        TextView prompt = findViewById(R.id.prompt);
        Button submit = findViewById(R.id.submit_Button);
        Button generateQR = findViewById(R.id.generateQR_Button);
        Button generateDTR = findViewById(R.id.generateDTR_Button);
        Button generateTAMS = findViewById(R.id.generateTAMS_Button);

        // Hide buttons
        generateQR.setVisibility(View.GONE);
        generateDTR.setVisibility(View.GONE);
        generateTAMS.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setId(id.getText().toString());

                read.readRecord(school.getSchoolID() + "/employee/" + save.getId(), new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            generateQR.setVisibility(View.VISIBLE);
                            generateDTR.setVisibility(View.VISIBLE);
                            generateTAMS.setVisibility(View.VISIBLE);

                            id.setVisibility(View.GONE);
                            prompt.setVisibility(View.GONE);
                            submit.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "ID Number not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        // handle error here
                    }
                });
            }
        });


        // Generate QR for employee
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Instance
                GenerateQR generateQR = new GenerateQR();

                // Declare ImageView
                ImageView qr = findViewById(R.id.qrCode_ImageView);
                // call generateQRCode method from GenerateQR class
                qr.setImageBitmap(generateQR.generateQRCode(save.getId()));


                // Download qr if a ImageView is clicked
                qr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadImageView imageDownloader = new DownloadImageView(qr);
                        imageDownloader.downloadImage();
                        //Intent intent = new Intent(Generate.this, Attendance.class);
                        //startActivity(intent);
                    }
                });
            }
        });

        // Generate DTR for employee
        generateDTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate.this, GenerateDTR.class);
                startActivity(intent);
            }
        });

        // TODO Generate TAMS file for Employee
        generateTAMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}