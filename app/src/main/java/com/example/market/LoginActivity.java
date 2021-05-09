package com.example.market;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
private Button loginButton;
private EditText loginPhoneInput, loginPasswordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton=(Button)findViewById(R.id.login_btn);
        loginPhoneInput=(EditText)findViewById(R.id.login_phone_input);
        loginPasswordInput=(EditText)findViewById(R.id.login_password_input);

    }
}