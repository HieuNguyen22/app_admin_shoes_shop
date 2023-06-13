package com.example.adminshoesshop.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminshoesshop.AddProductActivity;
import com.example.adminshoesshop.DetailProductActivity;
import com.example.adminshoesshop.R;
import com.example.adminshoesshop.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<ProductModel> productList;
    FirebaseFirestore db;

    public ProductAdapter(Context context, ArrayList<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    private boolean isHttp(String url) {
        String[] parts = url.split(":");
        if (parts[0].equals("https") || parts[0].equals("http"))
            return true;
        else return false;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        db = FirebaseFirestore.getInstance();

        if (!isHttp(product.getImg_url())) {
            byte[] decodedString = Base64.decode(product.getImg_url(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.image.setImageBitmap(decodedByte);
        } else {
            Picasso.get().load(product.getImg_url()).into(holder.image);
        }

        holder.name.setText(product.getName());
        holder.price1.setText(product.getPrice() + "");
        holder.price2.setText(String.valueOf(product.getPrice_1()));
        holder.description.setText(product.getDescription());
        holder.ratingBar.setRating(Float.parseFloat(product.getRating()));

        // EVENT LAYOUT
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailProductActivity.class);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                intent.putExtra("id", product.getId());
                activity.startActivity(intent);
            }
        });

        // EVENT DELETE
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete product")
                        .setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("AllProduct").document(product.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Delete product successfully!", Toast.LENGTH_SHORT).show();
                                        productList.remove(product);
                                        notifyDataSetChanged();
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

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductAdapter.ProductViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (productList == null)
            return 0;
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price1, price2;
        ImageView image, delete;
        RatingBar ratingBar;
        CardView cardView;
        ImageView btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name_dish);
            price2 = itemView.findViewById(R.id.price_dish);
            price1 = itemView.findViewById(R.id.total_dish);
            delete = itemView.findViewById(R.id.delete);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            description = itemView.findViewById(R.id.prod_description);
            cardView = itemView.findViewById(R.id.cart_recycler_view);
            btnDelete = itemView.findViewById(R.id.delete);
        }
    }
}
