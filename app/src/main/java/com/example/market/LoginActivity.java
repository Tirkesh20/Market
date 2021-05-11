package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.market.model.User;
import com.example.market.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText nameInput, phoneInput, passwordInput;
    private ProgressDialog loadingBar;
    private String parentUserName = "Users";
    private CheckBox rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.loginButton = (Button) findViewById(R.id.login_btn);
        this.phoneInput = (EditText) findViewById(R.id.login_phone_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        rememberMe=(CheckBox)findViewById(R.id.login_checkbox);
        Paper.init(this);
        loginButton.setOnClickListener(v -> {
            this.loginUser();
        });
    }

    private void loginUser() {
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Enter number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login in account");
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            this.ValidateUser(phone, password);
            loadingBar.show();
        }


    }

    private void ValidateUser(String phone, String password) {
        if (rememberMe.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance("https://market-72816-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.child(parentUserName).child(phone).exists()){
                     User userData=snapshot.child(parentUserName).child(phone).getValue(User.class);
                     if (userData.getPhone().equals(phone)){
                         if (userData.getPassword().equals(password)){
                             loadingBar.dismiss();
                             Toast.makeText(LoginActivity.this,"Logged in",Toast.LENGTH_SHORT);
                             Intent homeActivity= new Intent(LoginActivity.this,HomeActivity.class);
                             startActivity(homeActivity);

                         }else{
                             loadingBar.dismiss();
                             Toast.makeText(LoginActivity.this," Incorrect password",Toast.LENGTH_SHORT);
                         }
                     }

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this,"account wasn t found",Toast.LENGTH_SHORT);
                    Intent registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(registerIntent);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}