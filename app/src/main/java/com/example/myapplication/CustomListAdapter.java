package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Producto> items; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Context context, ArrayList<Producto> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_view_row_items, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Producto currentItem = (Producto) getItem(position);

        viewHolder.itemName.setText(currentItem.getNombre_Producto().toUpperCase());
        viewHolder.itemDescription.setText(currentItem.getDescripcion());
        viewHolder.itemImagen.setImageResource(R.drawable.ic_baseline_fastfood_24);

        return convertView;
    }

    private class ViewHolder {
        TextView itemName;
        TextView itemDescription;
        ImageView itemImagen;

        public ViewHolder(View view) {
            itemName = (TextView)view.findViewById(R.id.text_view_item_name);
            itemDescription = (TextView) view.findViewById(R.id.text_view_item_description);
            itemImagen = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}