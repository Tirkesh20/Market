package com.example.market.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.market.MainActivity;
import com.example.market.ProductCategory;
import com.example.market.R;
import com.example.market.users.LoginActivity;
import com.example.market.users.RegisterActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
private String categoryName,description,price,pName,currentData,currentTime,productRandomKey;
private ImageView productImage;
private String downloadImageUrl;
private EditText productName,productDescription,productPrice;
private Button addNewProduct;
private static final int GalleryPick=1;
private Uri imageUri;
private StorageReference imageReference;
private DatabaseReference productsRef;
private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        init();
        productImage.setOnClickListener(v -> openGallery());
        addNewProduct.setOnClickListener(v -> {
            validateProductData();
        });
    }

    private void validateProductData() {
        description=productDescription.getText().toString();
        price=productPrice.getText().toString();
        pName=productName.getText().toString();

        if (imageUri==null){
            Toast.makeText(this,"add image",Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this,"add description",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pName)) {
            Toast.makeText(this,"add name",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this,"add price",Toast.LENGTH_SHORT).show();
        }else{
            storeProductInformation();
        }

    }

    private void storeProductInformation() {
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("ddMMyyyy");
        this.currentData=dateFormat.format(calendar.getTime());
        SimpleDateFormat time=new SimpleDateFormat("HHmmss");
        this.currentTime=dateFormat.format(calendar.getTime());
        productRandomKey=currentData+currentTime;
        StorageReference filepath=imageReference.child(imageUri.getLastPathSegment()+productRandomKey+".jpeg");
        final UploadTask uploadTask=filepath.putFile(imageUri);
        uploadTask.addOnFailureListener(e -> {
            String message=e.toString();
            Toast.makeText(AdminActivity.this,"error:"+message,Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(AdminActivity.this,"image added successfully",Toast.LENGTH_SHORT).show();
            Task<Uri> uriTask=uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                downloadImageUrl=filepath.getDownloadUrl().toString();
                return filepath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                Toast.makeText(AdminActivity.this,"image saved",Toast.LENGTH_SHORT).show();
                saveProductInfoToDatabase();
            });
        });
    }

    private void saveProductInfoToDatabase() {
        loadingBar.setTitle("data loading");
        loadingBar.setMessage("please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pID",productRandomKey);
        productMap.put("date",currentData);
        productMap.put("time",currentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("productName",productName);
        productsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                loadingBar.dismiss();
                Toast.makeText(AdminActivity.this,"product added",Toast.LENGTH_SHORT).show();
                Intent addIntent=new Intent(AdminActivity.this, ProductCategory.class);
                startActivity(addIntent);

            }else {
                String s=task.getException().toString();
                Toast.makeText(AdminActivity.this,"error:"+s,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
            productImage.setImageURI(imageUri);
        }

    }

    private void init(){
        categoryName=getIntent().getExtras().get("category").toString();
    productImage=findViewById(R.id.select_image);
    productName=findViewById(R.id.product_name);
    productDescription=findViewById(R.id.product_description);
    productPrice=findViewById(R.id.product_price);
    addNewProduct=findViewById(R.id.btn_add_new_product);
    imageReference= FirebaseStorage.getInstance().getReference().child("ProductImages");
    productsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar =new ProgressDialog(this);
    }

     private void openGallery(){
         Intent galleryIntent=new Intent();
         galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
         galleryIntent.setType("image/*");
         startActivityForResult(galleryIntent,GalleryPick);
     }
}