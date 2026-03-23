import java.util.*;

// Reservation Request
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

// Thread-Safe Inventory
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory State:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized enqueue
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    // synchronized dequeue
    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(String name, BookingQueue queue, InventoryService inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // synchronized access to queue
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) {
                break; // no more requests
            }

            processBooking(r);
        }
    }

    private void processBooking(Reservation r) {

        System.out.println(Thread.currentThread().getName() +
                " processing → " + r.getGuestName());

        // Critical Section (Inventory)
        boolean success = inventory.allocateRoom(r.getRoomType());

        if (success) {
            System.out.println("✅ " + r.getGuestName() +
                    " booked " + r.getRoomType());
        } else {
            System.out.println("❌ " + r.getGuestName() +
                    " failed (No availability)");
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);

        // Step 2: Shared Booking Queue
        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Amit", "Single"));
        queue.addRequest(new Reservation("Priya", "Single"));
        queue.addRequest(new Reservation("Rahul", "Single"));
        queue.addRequest(new Reservation("Sneha", "Single"));

        // Step 3: Create multiple threads
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        // Step 4: Start threads
        t1.start();
        t2.start();

        // Step 5: Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 6: Final inventory
        inventory.displayInventory();

        System.out.println("\n(All bookings processed safely with synchronization)");
    }
}
