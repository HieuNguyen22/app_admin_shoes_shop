package com.example.adminshoesshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.adminshoesshop.databinding.ActivityAddProductBinding;
import com.example.adminshoesshop.databinding.ActivityDetailProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // CLICK ADD
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
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

                db.collection("AllProduct").document().set(newProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddProductActivity.this, "Add product successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}