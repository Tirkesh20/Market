package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        System.out.println("i am in onCreateAccount");
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
            System.out.println("i am loading bar");
            //loadingBar.show();
            this.ValidatePhone(username,phone,password);
            loadingBar.show();
        }
    }
    private void ValidatePhone(String username,String phone,String password) {
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance("https://market-72816-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("i am in onChange");
                if (!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object>userDataMap=new HashMap<>();
                    userDataMap.put("phone",phone);
                    userDataMap.put("name",username);
                    userDataMap.put("password",password);
                    databaseReference.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this,"Registration  successful",Toast.LENGTH_SHORT).show();
                            Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(loginIntent);
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            System.out.println("i in error" );
                        }

                    });
                }else{
                    System.out.println("i am already registered");

                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"phone"+phone+"already registered",Toast.LENGTH_SHORT).show();
                    Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this,"ok",Toast.LENGTH_SHORT);
                loadingBar.dismiss();
            }
        });

        System.out.println("in valid");
    }

}
