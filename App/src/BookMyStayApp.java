import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Input
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
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, -1);
    }

    public void decrementRoom(String type) throws InvalidBookingException {
        int available = inventory.getOrDefault(type, -1);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        inventory.put(type, available - 1);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }
}

// Validator (Fail-Fast)
class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        // Check guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Check room type exists
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Check availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("No availability for room type: " + reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    public void processBooking(Reservation reservation, InventoryService inventory) {

        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Safe allocation
            inventory.decrementRoom(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("✅ Booking Confirmed!");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + reservation.getRoomType());
            System.out.println("----------------------------");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("❌ Booking Failed: " + e.getMessage());
            System.out.println("----------------------------");
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Deluxe", 0);

        // Step 2: Create Booking Service
        BookingService bookingService = new BookingService();

        // Step 3: Test Cases

        // Valid booking
        Reservation r1 = new Reservation("Amit", "Single");

        // Invalid room type
        Reservation r2 = new Reservation("Priya", "Suite");

        // No availability
        Reservation r3 = new Reservation("Rahul", "Deluxe");

        // Empty guest name
        Reservation r4 = new Reservation("", "Single");

        // Process bookings
        bookingService.processBooking(r1, inventory);
        bookingService.processBooking(r2, inventory);
        bookingService.processBooking(r3, inventory);
        bookingService.processBooking(r4, inventory);
    }
}
