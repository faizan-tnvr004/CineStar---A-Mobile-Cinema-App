package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private ArrayList<Booking> bookingList;
    private Context context;
    private OnBookingCancelListener cancelListener;

    public interface OnBookingCancelListener {
        void onCancelClick(Booking booking, int position);
    }

    public BookingAdapter(Context context, ArrayList<Booking> bookingList,
                          OnBookingCancelListener cancelListener) {
        this.context = context;
        this.bookingList = bookingList;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvMovieName.setText(booking.getMovieName());
        holder.tvDateTime.setText(booking.getDateTime());
        holder.tvTickets.setText(booking.getSeats() + " Tickets");
        holder.tvPrice.setText(String.format("$%.2f", booking.getTotalPrice()));

        // Set poster from drawable name
        if (booking.getPosterDrawableName() != null && !booking.getPosterDrawableName().isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    booking.getPosterDrawableName(), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.imgPoster.setImageResource(resId);
            } else {
                holder.imgPoster.setImageResource(R.drawable.frank);
            }
        } else {
            holder.imgPoster.setImageResource(R.drawable.frank);
        }

        // Cancel button
        holder.btnCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancelClick(booking, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void removeItem(int position) {
        bookingList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bookingList.size());
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvMovieName, tvDateTime, tvTickets, tvPrice;
        ImageButton btnCancel;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgBookingPoster);
            tvMovieName = itemView.findViewById(R.id.tvBookingMovieName);
            tvDateTime = itemView.findViewById(R.id.tvBookingDateTime);
            tvTickets = itemView.findViewById(R.id.tvBookingTickets);
            tvPrice = itemView.findViewById(R.id.tvBookingPrice);
            btnCancel = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}
