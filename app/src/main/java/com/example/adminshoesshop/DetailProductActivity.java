package com.example.adminshoesshop;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.adminshoesshop.adapters.ProductAdapter;
import com.example.adminshoesshop.databinding.ActivityDetailProductBinding;
import com.example.adminshoesshop.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailProductActivity extends AppCompatActivity {

    ActivityDetailProductBinding binding;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    ProductModel product = new ProductModel();
    String fetchedId;
    String fetchedName;
    String fetchedImgUrl;
    String fetchedDescription;
    String fetchedType;
    long fetchedPrice;
    long fetchedPrice1;
    String fetchedRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_product);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("id");
        db = FirebaseFirestore.getInstance();

        db.collection("AllProduct").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(document.getId().equals(productId)) {

                        fetchedId = document.getId();
                        fetchedName = document.getString("name");
                        fetchedImgUrl = document.getString("img_url");
                        fetchedDescription = document.getString("description");
                        fetchedType = document.getString("type");
                        fetchedPrice = document.getLong("price");
                        fetchedPrice1 = document.getLong("price_1");
                        fetchedRating = document.getString("rating");

                        ProductModel fetchedProduct = new ProductModel(fetchedId, fetchedName, fetchedImgUrl, fetchedDescription, fetchedType, fetchedPrice, fetchedPrice1, fetchedRating);
                        product = fetchedProduct;

                        binding.prodName.setText(product.getName());
                        binding.prodImg.setText(product.getImg_url());
                        binding.prodDescription.setText(product.getDescription());
                        binding.prodType.setText(product.getType());
                        binding.prodPrice.setText(product.getPrice()+"");
                        binding.prodFixedPrice.setText(product.getPrice_1()+"");
                        binding.prodRating.setRating(Float.parseFloat(product.getRating()));
                        return;
                    }
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.prodName.getText().toString().trim();
                String img_url = binding.prodImg.getText().toString().trim();
                String description = binding.prodDescription.getText().toString().trim();
                String type = binding.prodType.getText().toString().trim();
                String price = binding.prodPrice.getText().toString().trim();
                String price_1 = binding.prodFixedPrice.getText().toString().trim();
                String rating = binding.prodRating.getRating()+"";

                if(name.isEmpty() == true) {
                    binding.prodName.setError("Title is required!");
                    return;
                }
                if(img_url.isEmpty() == true) {
                    binding.prodImg.setError("Image is required!");
                    return;
                }
                if(description.isEmpty() == true) {
                    binding.prodDescription.setError("Description is required!");
                    return;
                }
                if(type.isEmpty() == true) {
                    binding.prodType.setError("Type is required!");
                    return;
                }
                if(price.isEmpty() == true) {
                    binding.prodPrice.setError("Price is required!");
                    return;
                }
                if(price_1.isEmpty() == true) {
                    binding.prodFixedPrice.setError("Fix price is required!");
                    return;
                }

                Map<String, Object> newProduct = new HashMap<>();
                newProduct.put("description", description);
                newProduct.put("img_url", img_url);
                newProduct.put("name", name);
                newProduct.put("price", Long.parseLong(price));
                newProduct.put("price_1", Long.parseLong(price_1));
                newProduct.put("rating", rating);
                newProduct.put("type", type);

                db.collection("AllProduct").document(fetchedId).set(newProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(DetailProductActivity.this, "Update product successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.prodName.setText(product.getName());
                binding.prodImg.setText(product.getImg_url());
                binding.prodDescription.setText(product.getDescription());
                binding.prodType.setText(product.getType());
                binding.prodPrice.setText(product.getPrice()+"");
                binding.prodFixedPrice.setText(product.getPrice_1()+"");
                binding.prodRating.setRating(Float.parseFloat(product.getRating()));
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}