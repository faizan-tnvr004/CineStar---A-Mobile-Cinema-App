package com.example.assignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyBookingsFragment extends Fragment implements BookingAdapter.OnBookingCancelListener {

    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private BookingAdapter adapter;
    private ArrayList<Booking> bookingList = new ArrayList<>();
    private DatabaseReference bookingsRef;
    private ValueEventListener bookingsListener;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        recyclerView = view.findViewById(R.id.recyclerBookings);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }
        
        View btnMenu = view.findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openDrawer();
                }
            });
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(requireContext(), bookingList, this);
        recyclerView.setAdapter(adapter);

        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            bookingsRef = FirebaseDatabase.getInstance().getReference("bookings").child(userId);
            loadBookings();
        } else {
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText("Please login to view bookings");
        }

        return view;
    }

    private void loadBookings() {
        // Use addListenerForSingleValueEvent to prevent duplicate loading
        bookingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    Booking booking = bookingSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        booking.setBookingId(bookingSnapshot.getKey());
                        bookingList.add(booking);
                    }
                }
                adapter.notifyDataSetChanged();
                updateEmptyState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load bookings: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        bookingsRef.addListenerForSingleValueEvent(bookingsListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up listener to prevent leaks
        if (bookingsRef != null && bookingsListener != null) {
            bookingsRef.removeEventListener(bookingsListener);
        }
    }

    private void updateEmptyState() {
        if (bookingList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCancelClick(Booking booking, int position) {
        // Check if booking is in the future
        if (!isBookingInFuture(booking.getDateTime())) {
            Toast.makeText(getContext(), "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show confirmation dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> cancelBooking(booking, position))
                .setNegativeButton("No", null)
                .show();
    }

    private boolean isBookingInFuture(String dateTimeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date bookingDate = sdf.parse(dateTimeStr);
            Date now = new Date();
            return bookingDate != null && bookingDate.after(now);
        } catch (ParseException e) {
            // Try alternative format
            try {
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault());
                Date bookingDate = sdf2.parse(dateTimeStr);
                Date now = new Date();
                return bookingDate != null && bookingDate.after(now);
            } catch (ParseException e2) {
                e2.printStackTrace();
                return false;
            }
        }
    }

    private void cancelBooking(Booking booking, int position) {
        bookingsRef.child(booking.getBookingId()).removeValue()
                .addOnSuccessListener(unused -> {
                    adapter.removeItem(position);
                    updateEmptyState();
                    Toast.makeText(getContext(), "Booking Cancelled Successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to cancel: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }
}
