package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogIn extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        EditText adminUsername = findViewById(R.id.adminUsername_EditText);
        EditText adminPassword = findViewById(R.id.adminPassword_EditText);
        Button adminLogIn = findViewById(R.id.adminLogIn_Button);

        adminLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if admin account is school level or higher admin
                if(adminUsername.getText().toString().equals(save.getAdminUsername()) && adminPassword.getText().toString().equals(save.getAdminPassword())){
                    // TODO add/separate system admin screen
                    Toast.makeText(getApplicationContext(), "Welcome System Admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogIn.this, AdminScreen.class);
                    startActivity(intent);
                }
                else if (adminUsername.getText().toString().equals(school.getAdminUsername()) && adminPassword.getText().toString().equals(school.getAdminPassword())) {
                    Toast.makeText(getApplicationContext(), "Welcome " + school.getSchoolName() + " Admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogIn.this, AdminScreen.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}