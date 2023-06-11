package com.example.adminshoesshop;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminshoesshop.adapters.OrderAdapter;
import com.example.adminshoesshop.adapters.ProductAdapter;
import com.example.adminshoesshop.databinding.FragmentOrderBinding;
import com.example.adminshoesshop.databinding.FragmentProductBinding;
import com.example.adminshoesshop.models.OrderModel;
import com.example.adminshoesshop.models.OrderProductModel;
import com.example.adminshoesshop.models.ProductModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    FirebaseFirestore db;
    ArrayList<OrderModel> orderList;
    ProgressDialog progressDialog;
    OrderAdapter orderAdapter;
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = binding.rvProducts;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), orderList);
        recyclerView.setAdapter(orderAdapter);

        getData();

        return root;
    }

    private void getData() {
        orderList.clear();
        db.collection("CurrentUser").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        String userId = dc.getDocument().getId();

                        db.collection("CurrentUser").document(userId).collection("MyOrder").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                        String orderId = dc.getDocument().getId();
                                        String address = dc.getDocument().getString("address");
                                        String dateOrder = dc.getDocument().getString("dateOrder");
                                        long status = dc.getDocument().getLong("status");
                                        String timeOrder = dc.getDocument().getString("timeOrder");
                                        String totalAmount = dc.getDocument().getString("totalAmount");
                                        ArrayList<OrderProductModel> cartList = new ArrayList<>();

                                        db.collection("CurrentUser").document(userId).collection("MyOrder").document(orderId).get().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    if (document.contains("cartList")) {
                                                        System.out.println("--------------------- FETCHED DATA");
                                                        List<Map<String, Object>> arrayData = (List<Map<String, Object>>) document.get("cartList");
                                                        for (Map<String, Object> map : arrayData) {
                                                            String currentDate = (String) map.get("currentDate");
                                                            String currentTime = (String) map.get("currentTime");
                                                            String documentId = (String) map.get("documentId");
                                                            String img_url = (String) map.get("img_url");
                                                            String productName = (String) map.get("productName");
                                                            long productPrice = (long) map.get("productPrice");
                                                            long totalPrice = (long) map.get("totalPrice");
                                                            long totalQuantity = (long) map.get("totalQuantity");

                                                            OrderProductModel orderProduct = new OrderProductModel(currentDate, currentTime, documentId, img_url, productName, productPrice, totalPrice, totalQuantity);
                                                            cartList.add(orderProduct);
                                                            orderAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                } else {
                                                    Log.d(TAG, "Not found!");
                                                }
                                            } else {
                                                Log.d(TAG, "Error: ", task.getException());
                                            }
                                        });

                                        System.out.println("--------------- CART LIST " +cartList.size());

                                        OrderModel fetchedOrder = new OrderModel(userId, orderId, address, cartList, dateOrder, status, timeOrder, totalAmount);

                                        orderList.add(fetchedOrder);
                                        orderAdapter.notifyDataSetChanged();
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }
                                }

                            }
                        });
                    }
                }
            }
        });
    }
}