package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.market.model.User;
import com.example.market.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
private Button loginButton;
private Button registerButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton=(Button)findViewById(R.id.main_register_btn);
        loginButton=(Button)findViewById(R.id.main_login_btn);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);
        loginButton.setOnClickListener(v -> {
            Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
        });
        registerButton.setOnClickListener(v -> {
            Intent registerIntent=new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(registerIntent);
        });
        String userPhoneKey=Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey=Paper.book().read(Prevalent.userPasswordKey);
        if (userPhoneKey!=""&&userPasswordKey!=""){
            if (!TextUtils.isEmpty(userPhoneKey)&&!TextUtils.isEmpty(userPasswordKey)){
            validateUser(userPhoneKey,userPasswordKey);
            }
        }
    }
    private void validateUser(String phone, String password) {

        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance("https://market-72816-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phone).exists()){
                    User userData=snapshot.child("Users").child(phone).getValue(User.class);
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Logged in",Toast.LENGTH_SHORT);
                            Intent homeActivity= new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(homeActivity);

                        }else{
                            loadingBar.dismiss();
                        }
                    }

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this,"account wasn t found",Toast.LENGTH_SHORT);
                    Intent registerIntent=new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}