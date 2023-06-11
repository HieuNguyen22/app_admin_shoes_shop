package com.example.adminshoesshop.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminshoesshop.R;
import com.example.adminshoesshop.models.OrderModel;
import com.example.adminshoesshop.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
    ArrayList<OrderModel> orderList;

    public OrderAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.userId.setText(order.getUserId());
        holder.address.setText(order.getAddress());
        holder.date.setText(order.getDateOrder());
        holder.time.setText(order.getTimeOrder());
        if (order.getStatus() == 0) {
            holder.tvStatus.setTextColor(Color.RED);
            holder.status.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setTextColor(Color.GREEN);
            holder.status.setTextColor(Color.GREEN);
        }
        holder.status.setText(order.getStatus() + "");
        holder.total.setText(order.getTotalAmount());
        holder.listView.setAdapter(new CustomListAdapter(context, order.getCartList()));

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Update product status")
                        .setMessage("Are you sure you want to update product status?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                int newStatus = 0;
                                Map<String, Object> map = new HashMap<String, Object>();
                                if (order.getStatus() == 0) {
                                    newStatus = 1;
                                } else {
                                    newStatus = 0;
                                }
                                map.put("status", newStatus);

//                                OrderModel newOrder = order;
//                                newOrder.setStatus(newStatus);

                                int finalNewStatus = newStatus;
                                db.collection("CurrentUser").document(order.getUserId()).collection("MyOrder").document(order.getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        order.setStatus(finalNewStatus);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Update product status successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (orderList == null)
            return 0;
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView address, total, tvStatus, status, date, time, userId;
        ListView listView;
        Button btnUpdate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            userId = itemView.findViewById(R.id.tv_user_id);
            address = itemView.findViewById(R.id.tv_address_order);
            total = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            status = itemView.findViewById(R.id.tv_status_order);
            date = itemView.findViewById(R.id.tv_date_order);
            time = itemView.findViewById(R.id.tv_time_order);
            listView = itemView.findViewById(R.id.lv_product_order);
            btnUpdate = itemView.findViewById(R.id.btn_update_order);
        }
    }
}
