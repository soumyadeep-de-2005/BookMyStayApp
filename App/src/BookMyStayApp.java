import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }
}

// Booking History (State Holder)
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Get all bookings (READ-ONLY)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history); // defensive programming
    }
}

// Booking Report Service
class BookingReportService {

    // Display full history
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n===== Booking History =====\n");

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n===== Booking Summary Report =====\n");

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomTypeCount.get(type));
        }

        System.out.println("\nTotal Bookings: " + reservations.size());
    }
}

// Main Class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Step 1: Create Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Add confirmed bookings (simulate Use Case 6 output)
        history.addReservation(new Reservation("RES-101", "Amit", "Single", "SI-1"));
        history.addReservation(new Reservation("RES-102", "Priya", "Deluxe", "DE-2"));
        history.addReservation(new Reservation("RES-103", "Rahul", "Single", "SI-3"));
        history.addReservation(new Reservation("RES-104", "Karan", "Double", "DO-4"));

        // Step 3: Admin retrieves history
        List<Reservation> allBookings = history.getAllReservations();

        // Step 4: Reporting Service
        BookingReportService reportService = new BookingReportService();

        // Display full history
        reportService.displayAllBookings(allBookings);

        // Generate summary
        reportService.generateSummary(allBookings);

        System.out.println("\n(Note: Reporting is read-only and does not modify history)");
    }
}
