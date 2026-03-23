import java.util.*;

// Reservation (already confirmed in Use Case 6)
class Reservation {
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
}

// Add-On Service (Optional Feature)
class AddOnService {
    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void displayService() {
        System.out.println(serviceName + " (₹" + price + ")");
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service: " + service.getServiceName() +
                " to Reservation: " + reservationId);
    }

    // View services for a reservation
    public void viewServices(String reservationId) {
        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService service : services) {
            service.displayService();
        }
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService service : services) {
                total += service.getPrice();
            }
        }

        return total;
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Step 1: Create confirmed reservations (from previous use case)
        Reservation r1 = new Reservation("RES-101", "Amit", "Single");
        Reservation r2 = new Reservation("RES-102", "Priya", "Deluxe");

        // Step 2: Create Add-On Services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa", 1500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800);

        // Step 3: Create Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Step 4: Guest selects services
        manager.addService(r1.getReservationId(), breakfast);
        manager.addService(r1.getReservationId(), spa);

        manager.addService(r2.getReservationId(), airportPickup);

        // Step 5: View services
        manager.viewServices(r1.getReservationId());
        manager.viewServices(r2.getReservationId());

        // Step 6: Calculate total add-on cost
        double total1 = manager.calculateTotalCost(r1.getReservationId());
        double total2 = manager.calculateTotalCost(r2.getReservationId());

        System.out.println("\nTotal Add-On Cost for " + r1.getReservationId() + ": ₹" + total1);
        System.out.println("Total Add-On Cost for " + r2.getReservationId() + ": ₹" + total2);

        System.out.println("\n(Note: Core booking & inventory remain unchanged)");
    }
}
