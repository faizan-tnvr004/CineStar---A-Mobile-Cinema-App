package com.example.assignment1;

public class Booking {
    private String bookingId;
    private String userId;
    private String movieName;
    private int seats;
    private double totalPrice;
    private String dateTime;
    private String posterDrawableName;

    // Required empty constructor for Firebase
    public Booking() {}

    public Booking(String bookingId, String userId, String movieName, int seats,
                   double totalPrice, String dateTime, String posterDrawableName) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.posterDrawableName = posterDrawableName;
    }

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getPosterDrawableName() { return posterDrawableName; }
    public void setPosterDrawableName(String posterDrawableName) { this.posterDrawableName = posterDrawableName; }
}
