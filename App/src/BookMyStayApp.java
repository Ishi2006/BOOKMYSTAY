import java.util.*;

// Reservation
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

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void incrementRoom(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> history;

    public BookingHistory() {
        history = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public boolean exists(String reservationId) {
        return history.containsKey(reservationId);
    }

    public Reservation getReservation(String reservationId) {
        return history.get(reservationId);
    }

    public void removeReservation(String reservationId) {
        history.remove(reservationId);
    }
}

// Cancellation Service
class CancellationService {

    // Stack to track released room IDs (LIFO rollback)
    private Stack<String> rollbackStack;

    public CancellationService() {
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Step 1: Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        // Step 2: Get reservation
        Reservation reservation = history.getReservation(reservationId);

        // Step 3: Push to rollback stack
        rollbackStack.push(reservationId);

        // Step 4: Restore inventory
        inventory.incrementRoom(reservation.getRoomType());

        // Step 5: Remove from history
        history.removeReservation(reservationId);

        // Step 6: Confirm cancellation
        System.out.println("Cancellation Successful for " + reservation.getGuestName());
    }

    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (LIFO) ---");
        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        // Step 1: Setup inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 0);
        inventory.addRoomType("Deluxe", 1);

        // Step 2: Setup booking history
        BookingHistory history = new BookingHistory();
        history.addReservation(new Reservation("SI-1", "Alice", "Single"));
        history.addReservation(new Reservation("DE-1", "Bob", "Deluxe"));

        // Step 3: Cancellation service
        CancellationService cancelService = new CancellationService();

        // Step 4: Perform cancellations
        cancelService.cancelBooking("SI-1", history, inventory);
        cancelService.cancelBooking("XYZ-1", history, inventory); // invalid
        cancelService.cancelBooking("DE-1", history, inventory);

        // Step 5: Display rollback stack
        cancelService.displayRollbackStack();

        // Step 6: Display updated inventory
        inventory.displayInventory();
    }
}