package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SnackAdapter extends ArrayAdapter<Snack> {

    private Context context;
    private ArrayList<Snack> snackList;
    private OnSnackQuantityChanged listener;

    // Interface so SnacksFragment gets notified when quantity changes
    public interface OnSnackQuantityChanged {
        void onChanged();
    }

    public SnackAdapter(Context context, ArrayList<Snack> snackList, OnSnackQuantityChanged listener) {
        super(context, 0, snackList);
        this.context   = context;
        this.snackList = snackList;
        this.listener  = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_snack, parent, false);
        }

        Snack snack = snackList.get(position);

        ImageView imgSnack   = convertView.findViewById(R.id.imgSnack);
        TextView tvName      = convertView.findViewById(R.id.tvSnackName);
        TextView tvPrice     = convertView.findViewById(R.id.tvSnackPrice);
        TextView tvQty       = convertView.findViewById(R.id.tvQty);
        Button btnPlus       = convertView.findViewById(R.id.btnPlus);
        Button btnMinus      = convertView.findViewById(R.id.btnMinus);

        imgSnack.setImageResource(snack.getImageResId());
        tvName.setText(snack.getName());
        tvPrice.setText(String.format("$%.2f", snack.getPrice()));
        tvQty.setText(String.valueOf(snack.getQuantity()));

        btnPlus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            tvQty.setText(String.valueOf(snack.getQuantity()));
            listener.onChanged(); // notify fragment to update total
        });

        btnMinus.setOnClickListener(v -> {
            if (snack.getQuantity() > 0) {
                snack.setQuantity(snack.getQuantity() - 1);
                tvQty.setText(String.valueOf(snack.getQuantity()));
                listener.onChanged();
            }
        });

        return convertView;
    }
}