package com.example.adminshoesshop.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminshoesshop.R;
import com.example.adminshoesshop.models.OrderModel;
import com.example.adminshoesshop.models.OrderProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {

    Context context;
    ArrayList<OrderProductModel> orderProductList;

    public OrderProductAdapter(Context context, ArrayList<OrderProductModel> orderProductList) {
        this.context = context;
        this.orderProductList = orderProductList;
    }
    private boolean isHttp(String url) {
        String[] parts = url.split(":");
        if (parts[0].equals("https") || parts[0].equals("http"))
            return true;
        else return false;
    }


    @NonNull
    @Override
    public OrderProductAdapter.OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_order, parent, false);

        return new OrderProductAdapter.OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.OrderProductViewHolder holder, int position) {
        OrderProductModel orderProduct = orderProductList.get(position);

        if (!isHttp(orderProduct.getImg_url())) {
            byte[] decodedString = Base64.decode(orderProduct.getImg_url(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.iv_prod_image.setImageBitmap(decodedByte);
        } else {
            Picasso.get().load(orderProduct.getImg_url()).into(holder.iv_prod_image);
        }

        holder.tv_prod_name.setText(orderProduct.getProductName());
        holder.tv_prod_qty.setText(orderProduct.getTotalQuantity()+"");
        holder.tv_prod_price.setText(orderProduct.getProductPrice()+"");
        holder.tv_total_price.setText(orderProduct.getTotalPrice()+"");
    }

    @Override
    public int getItemCount() {
        if (orderProductList == null)
            return 0;
        return orderProductList.size();
    }

    public class OrderProductViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_prod_image;
        TextView tv_prod_name, tv_prod_price, tv_prod_qty, tv_total_price;
        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_prod_image = itemView.findViewById(R.id.iv_prod_image);
            tv_prod_name = itemView.findViewById(R.id.tv_prod_name);
            tv_prod_price = itemView.findViewById(R.id.tv_prod_price);
            tv_prod_qty = itemView.findViewById(R.id.tv_prod_qty);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
        }
    }
}
