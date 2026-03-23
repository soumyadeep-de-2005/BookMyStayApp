import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// Inventory (Serializable)
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}

// Wrapper for full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> reservations;
    InventoryService inventory;

    public SystemState(List<Reservation> reservations, InventoryService inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // SAVE STATE
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // LOAD STATE
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("✅ System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading state. Starting safe default.");
        }

        return null;
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Step 1: Try loading previous state
        SystemState state = PersistenceService.load();

        List<Reservation> reservations;
        InventoryService inventory;

        if (state == null) {
            // Fresh start
            reservations = new ArrayList<>();
            inventory = new InventoryService();

            inventory.addRoomType("Single", 2);
            inventory.addRoomType("Deluxe", 1);

            reservations.add(new Reservation("RES-101", "Amit", "Single"));
            reservations.add(new Reservation("RES-102", "Priya", "Deluxe"));

            System.out.println("\nStarting new system state...");
        } else {
            // Restore state
            reservations = state.reservations;
            inventory = state.inventory;

            System.out.println("\nRecovered previous system state...");
        }

        // Step 2: Display data
        System.out.println("\n===== Reservations =====");
        for (Reservation r : reservations) {
            r.display();
        }

        inventory.display();

        // Step 3: Simulate system shutdown → save state
        SystemState newState = new SystemState(reservations, inventory);
        PersistenceService.save(newState);

        System.out.println("\n(System shutdown complete. Restart to test recovery)");
    }
}
