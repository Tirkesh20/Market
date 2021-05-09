package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText nameInput,phoneInput,passwordInput;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton=(Button)findViewById(R.id.register_btn);
        nameInput=(EditText)findViewById(R.id.register_name_input);
        phoneInput=(EditText)findViewById(R.id.register_phone_input);
        passwordInput=(EditText)findViewById(R.id.register_password_input);
        loadingBar=new ProgressDialog(this);
        registerButton.setOnClickListener(v -> {
            createAccount();
        });
    }
    private void createAccount(){
        String username=nameInput.getText().toString();
        String phone=phoneInput.getText().toString();
        String password=passwordInput.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Enter number",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        } else{
            loadingBar.setTitle("Creating an account");
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhone(username,phone,password);
        }
    }
    private void ValidatePhone(String username,String phone,String password){
        final DatabaseReference Rootref;
        Rootref=FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object>userDataMap=new HashMap<>();
                    userDataMap.put("phone",phone);
                    userDataMap.put("name",username);
                    userDataMap.put("password",password);
                    Rootref.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener((OnCompleteListener<Void>) task -> {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this,"Registration  successful",Toast.LENGTH_SHORT).show();
                            Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(loginIntent);
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }

                    });
                }else{
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"phone"+phone+"already registered",Toast.LENGTH_SHORT).show();
                    Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}