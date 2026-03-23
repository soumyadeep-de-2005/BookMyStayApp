import java.util.*;

// Reservation
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

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getGuestName() {
        return guestName;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void incrementRoom(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}

// Booking History (tracks active bookings)
class BookingHistory {
    private Map<String, Reservation> activeBookings = new HashMap<>();

    public void addReservation(Reservation r) {
        activeBookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return activeBookings.get(id);
    }

    public void removeReservation(String id) {
        activeBookings.remove(id);
    }

    public boolean exists(String id) {
        return activeBookings.containsKey(id);
    }

    public void displayActiveBookings() {
        System.out.println("\nActive Bookings:");
        for (Reservation r : activeBookings.values()) {
            System.out.println(r.getReservationId() + " → " + r.getGuestName() +
                    " (" + r.getRoomType() + ")");
        }
    }
}

// Cancellation Service (Rollback Logic)
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              InventoryService inventory) {

        System.out.println("\nProcessing Cancellation for: " + reservationId);

        // Step 1: Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("❌ Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation r = history.getReservation(reservationId);

        // Step 2: Push room ID to rollback stack (LIFO)
        rollbackStack.push(r.getRoomId());

        // Step 3: Restore inventory
        inventory.incrementRoom(r.getRoomType());

        // Step 4: Remove booking from active history
        history.removeReservation(reservationId);

        // Step 5: Confirm cancellation
        System.out.println("✅ Booking Cancelled Successfully!");
        System.out.println("Guest: " + r.getGuestName());
        System.out.println("Released Room ID: " + r.getRoomId());
        System.out.println("----------------------------");
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recently Released Rooms):");
        System.out.println(rollbackStack);
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 0);
        inventory.addRoomType("Double", 0);

        // Step 2: Setup Booking History (simulate confirmed bookings)
        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("RES-101", "Amit", "Single", "SI-1"));
        history.addReservation(new Reservation("RES-102", "Priya", "Double", "DO-2"));

        // Step 3: Display current state
        history.displayActiveBookings();
        inventory.displayInventory();

        // Step 4: Cancellation Service
        CancellationService cancelService = new CancellationService();

        // Valid cancellation
        cancelService.cancelBooking("RES-101", history, inventory);

        // Invalid cancellation
        cancelService.cancelBooking("RES-999", history, inventory);

        // Step 5: Final state
        history.displayActiveBookings();
        inventory.displayInventory();
        cancelService.displayRollbackStack();
    }
}
