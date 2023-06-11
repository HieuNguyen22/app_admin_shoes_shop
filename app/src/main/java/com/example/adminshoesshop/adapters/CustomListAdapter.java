package com.example.adminshoesshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminshoesshop.R;
import com.example.adminshoesshop.models.OrderProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<OrderProductModel> listOrder;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext,  ArrayList<OrderProductModel> listOrder) {
        this.context = aContext;
        this.listOrder = listOrder;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        if (listOrder == null)
            return 0;
        return listOrder.size();
    }

    @Override
    public Object getItem(int i) {
        return listOrder.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_product_order, null);
            holder = new ViewHolder();
            holder.iv_prod_image = (ImageView) convertView.findViewById(R.id.iv_prod_image);
            holder.tv_prod_qty = (TextView) convertView.findViewById(R.id.tv_prod_qty);
            holder.tv_total_price = (TextView) convertView.findViewById(R.id.tv_total_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderProductModel orderProduct = this.listOrder.get(i);
        holder.tv_prod_qty.setText(orderProduct.getTotalQuantity()+"");
        holder.tv_total_price.setText(orderProduct.getTotalPrice()+"");
        Picasso.get().load(orderProduct.getImg_url()).into(holder.iv_prod_image);

        return convertView;
    }

    static class ViewHolder {
        ImageView iv_prod_image;
        TextView tv_prod_qty;
        TextView tv_total_price;
    }
}