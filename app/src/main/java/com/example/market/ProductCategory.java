package com.example.market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.market.admin.AdminActivity;

public class ProductCategory extends AppCompatActivity {

    private ImageView phone,micro,laptop,headphones,graphicCard,monitor,box,mouse,keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        init();

        phone.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","phone");
            startActivity(intent);
        });
        micro.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","micro");
            startActivity(intent);
        });
        laptop.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","laptop");
            startActivity(intent);
        });
        headphones.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","headphones");
            startActivity(intent);
        });
        graphicCard.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","graphicCard");
            startActivity(intent);
        });
        monitor.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","monitor");
            startActivity(intent);
        });
        mouse.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","mouse");
            startActivity(intent);
        });
        box.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","box");
            startActivity(intent);
        });
        keyboard.setOnClickListener(v -> {
            Intent intent=new Intent(ProductCategory.this, AdminActivity.class);
            intent.putExtra("category","keyboard");
            startActivity(intent);
        });
    }

    private void init(){
        phone=findViewById(R.id.phone);
        micro=findViewById(R.id.microphone);
        laptop=findViewById(R.id.laptop);
        headphones=findViewById(R.id.headphone);

        graphicCard=findViewById(R.id.graph);
        monitor=findViewById(R.id.monitor);
        box=findViewById(R.id.box);
        mouse=findViewById(R.id.mouse);

        keyboard=findViewById(R.id.keyboard);
    }
}