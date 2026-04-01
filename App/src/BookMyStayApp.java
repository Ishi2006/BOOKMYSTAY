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
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }

    public void displayHistory() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting fresh.");
        }

        return null;
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        // Step 1: Try loading previous state
        SystemState state = persistence.load();

        RoomInventory inventory;
        BookingHistory history;

        if (state == null) {
            // Fresh start
            inventory = new RoomInventory();
            history = new BookingHistory();

            inventory.addRoomType("Single", 5);
            inventory.addRoomType("Deluxe", 2);

            history.addReservation(new Reservation("SI-1", "Alice", "Single"));
            history.addReservation(new Reservation("DE-1", "Bob", "Deluxe"));

        } else {
            // Restore state
            inventory = state.inventory;
            history = state.history;
        }

        // Step 2: Display current state
        inventory.displayInventory();
        history.displayHistory();

        // Step 3: Save state before shutdown
        persistence.save(new SystemState(inventory, history));
    }
}