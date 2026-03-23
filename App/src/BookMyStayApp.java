import java.util.*;

// Reservation (from previous use case)
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
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}

// Booking Service (Core Logic)
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToIds = new HashMap<>();
    private int idCounter = 1;

    public void processBookings(Queue<Reservation> queue, InventoryService inventory) {

        System.out.println("\n===== Processing Booking Requests =====\n");

        while (!queue.isEmpty()) {

            Reservation request = queue.poll(); // FIFO

            String roomType = request.getRoomType();

            System.out.println("Processing: " + request.getGuestName() +
                    " → " + roomType);

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (Set)
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(roomType);
                }

                // Allocate room
                allocatedRoomIds.add(roomId);

                roomTypeToIds.putIfAbsent(roomType, new HashSet<>());
                roomTypeToIds.get(roomType).add(roomId);

                // Atomic update → inventory
                inventory.decrementRoom(roomType);

                // Confirm booking
                System.out.println("✅ Booking Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println("----------------------------");

            } else {
                System.out.println("❌ Booking Failed (No Availability)");
                System.out.println("----------------------------");
            }
        }
    }

    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + (idCounter++);
    }

    public void displayAllocations() {
        System.out.println("\n===== Allocated Rooms =====\n");

        for (String type : roomTypeToIds.keySet()) {
            System.out.println(type + " → " + roomTypeToIds.get(type));
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);
        inventory.addRoomType("Deluxe", 1);

        // Step 2: Create Booking Queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Amit", "Single"));
        queue.offer(new Reservation("Priya", "Deluxe"));
        queue.offer(new Reservation("Rahul", "Single"));
        queue.offer(new Reservation("Sneha", "Single")); // extra request
        queue.offer(new Reservation("Karan", "Double"));

        // Step 3: Process Bookings
        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);

        // Step 4: Show Final Allocation
        bookingService.displayAllocations();

        // Step 5: Show Remaining Inventory
        inventory.displayInventory();
    }
}
