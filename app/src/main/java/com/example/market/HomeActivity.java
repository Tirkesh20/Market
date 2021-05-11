package com.example.market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logoutBtn=(Button)findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(v -> {
            Paper.book().destroy();
            Intent logOutIntent=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(logOutIntent);
        });
    }
}