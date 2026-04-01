import java.util.*;

// Reservation (same as Use Case 5)
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
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Update inventory after allocation
    public void decrementRoom(String type) {
        int count = inventory.getOrDefault(type, 0);
        if (count > 0) {
            inventory.put(type, count - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n--- Updated Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Service (Core Logic)
class BookingService {

    private Queue<Reservation> requestQueue;

    // Map: RoomType → Set of allocated Room IDs
    private HashMap<String, Set<String>> allocatedRooms;

    // Global set to ensure uniqueness
    private Set<String> allAllocatedRoomIds;

    private int roomCounter = 1;

    public BookingService(Queue<Reservation> requestQueue) {
        this.requestQueue = requestQueue;
        this.allocatedRooms = new HashMap<>();
        this.allAllocatedRoomIds = new HashSet<>();
    }

    // Generate unique Room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + (roomCounter++);
    }

    // Process bookings
    public void processBookings(RoomInventory inventory) {

        System.out.println("\n--- Processing Booking Requests ---");

        while (!requestQueue.isEmpty()) {

            Reservation reservation = requestQueue.poll();
            String type = reservation.getRoomType();

            System.out.println("\nProcessing request for: " + reservation.getGuestName());

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                String roomId;

                // Ensure unique ID
                do {
                    roomId = generateRoomId(type);
                } while (allAllocatedRoomIds.contains(roomId));

                // Add to global set
                allAllocatedRoomIds.add(roomId);

                // Add to type-specific set
                allocatedRooms
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory (atomic step)
                inventory.decrementRoom(type);

                // Confirm reservation
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + reservation.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed - No rooms available for " + type);
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---");

        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Deluxe", 1);

        // Step 2: Setup Queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single"));
        queue.offer(new Reservation("Bob", "Single"));
        queue.offer(new Reservation("Charlie", "Single")); // should fail
        queue.offer(new Reservation("David", "Deluxe"));

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(queue);

        // Step 4: Process Bookings
        bookingService.processBookings(inventory);

        // Step 5: Show Results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}