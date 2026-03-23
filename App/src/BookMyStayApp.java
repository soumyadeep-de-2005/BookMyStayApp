import java.util.*;

// Domain Model: Room
class Room {
    private String roomType;
    private double price;
    private List<String> amenities;

    public Room(String roomType, double price, List<String> amenities) {
        this.roomType = roomType;
        this.price = price;
        this.amenities = amenities;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("----------------------------");
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> availabilityMap;

    public Inventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoom(String roomType, int count) {
        availabilityMap.put(roomType, count);
    }

    // READ-ONLY ACCESS
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availabilityMap); // defensive programming
    }
}

// Search Service (Separation of Concerns)
class SearchService {

    public static void searchAvailableRooms(Inventory inventory, Map<String, Room> roomMap) {
        System.out.println("\n===== Available Rooms =====\n");

        Map<String, Integer> availability = inventory.getAllAvailability();

        for (String roomType : availability.keySet()) {
            int count = availability.get(roomType);

            // Validation Logic → only show available rooms
            if (count > 0) {
                Room room = roomMap.get(roomType);

                System.out.println("Available Count: " + count);
                room.displayDetails();
            }
        }

        System.out.println("Search completed (Read-Only Operation)");
    }
}

// Main Class
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Step 1: Create Room Objects (Domain Model)
        Room single = new Room("Single", 2000,
                Arrays.asList("WiFi", "AC", "TV"));

        Room doubleRoom = new Room("Double", 3500,
                Arrays.asList("WiFi", "AC", "TV", "Mini Fridge"));

        Room deluxe = new Room("Deluxe", 5000,
                Arrays.asList("WiFi", "AC", "TV", "Mini Fridge", "Balcony"));

        // Step 2: Store Room Objects
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("Single", single);
        roomMap.put("Double", doubleRoom);
        roomMap.put("Deluxe", deluxe);

        // Step 3: Setup Inventory (State Holder)
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);   // unavailable
        inventory.addRoom("Deluxe", 2);

        // Step 4: Guest searches rooms (READ-ONLY)
        SearchService.searchAvailableRooms(inventory, roomMap);
    }
}
