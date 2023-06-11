package com.example.adminshoesshop;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adminshoesshop.adapters.ProductAdapter;
import com.example.adminshoesshop.databinding.FragmentProductBinding;
import com.example.adminshoesshop.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    private FragmentProductBinding binding;
    RecyclerView recyclerView;
    ArrayList<ProductModel> productList;
    ProductAdapter productAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public void onResume() {
        super.onResume();

        productList.clear();
        eventChangeListener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = binding.rvProducts;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);

        recyclerView.setAdapter(productAdapter);

        eventChangeListener();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });

        binding.btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete all products")
                        .setMessage("Are you sure you want to delete all products?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("AllProduct")
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    document.getReference().delete();
                                                }
                                                Toast.makeText(getContext(), "Delete all product successfully!", Toast.LENGTH_SHORT).show();
                                                productAdapter.notifyDataSetChanged();
                                            } else {
                                                Log.d(TAG, "Error: ", task.getException());
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return root;
    }

    private void eventChangeListener() {
        db.collection("AllProduct").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Log.e("Firestore error! ", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        System.out.println("-------- ID " + dc.getDocument().getId());
                        String fetchedId = dc.getDocument().getId();
                        String fetchedName = dc.getDocument().getString("name");
                        String fetchedImgUrl = dc.getDocument().getString("img_url");
                        String fetchedDescription = dc.getDocument().getString("description");
                        String fetchedType = dc.getDocument().getString("type");
                        String fetchedRating = dc.getDocument().getString("rating");

                        ProductModel fetchedProduct = new ProductModel(fetchedId, fetchedName, fetchedImgUrl, fetchedDescription, fetchedType, 0, 0, fetchedRating);


                        Object fetchedPrice = dc.getDocument().get("price");
                        if (fetchedPrice instanceof String) {
                            String stringValue = (String) fetchedPrice;
                            fetchedProduct.setPrice(Long.parseLong(stringValue));
                        } else if (fetchedPrice instanceof Number) {
                            long numberValue = (long) fetchedPrice;
                            fetchedProduct.setPrice(numberValue);
                        }

                        Object fetchedPrice1 = dc.getDocument().get("price");
                        if (fetchedPrice1 instanceof String) {
                            String stringValue = (String) fetchedPrice1;
                            fetchedProduct.setPrice_1(Long.parseLong(stringValue));
                        } else if (fetchedPrice1 instanceof Number) {
                            long numberValue = (long) fetchedPrice1;
                            fetchedProduct.setPrice_1(numberValue);
                        }

                        productList.add(fetchedProduct);

                    }
                    productAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }


}