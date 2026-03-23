import java.util.*;

// Reservation (Actor)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO Structure)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (READ-ONLY)
    public void viewRequests() {
        System.out.println("\n===== Booking Request Queue =====\n");

        if (queue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : queue) {
            r.displayRequest();
        }

        System.out.println("\n(All requests are in FIFO order)");
    }

    // Get next request (for future processing, no removal here)
    public Reservation peekNextRequest() {
        return queue.peek();
    }
}

// Main Class
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        // Step 1: Create Booking Queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Step 2: Simulate Guest Booking Requests
        Reservation r1 = new Reservation("Amit", "Single");
        Reservation r2 = new Reservation("Priya", "Deluxe");
        Reservation r3 = new Reservation("Rahul", "Double");
        Reservation r4 = new Reservation("Sneha", "Single");

        // Step 3: Add requests to queue (FIFO)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);
        bookingQueue.addRequest(r4);

        // Step 4: View all requests (READ-ONLY)
        bookingQueue.viewRequests();

        // Step 5: Peek next request (no removal)
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            System.out.println("\nNext request to process:");
            next.displayRequest();
        }
    }
}
