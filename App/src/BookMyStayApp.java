import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
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
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) throws InvalidBookingException {
        int count = getAvailability(type);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        inventory.put(type, count - 1);
    }
}

// Validator (Actor)
class BookingValidator {

    public void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "No available rooms for type: " + reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;
    private BookingValidator validator;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.validator = new BookingValidator();
    }

    public void processBooking(Reservation reservation) {

        try {
            // Step 1: Validate input (FAIL FAST)
            validator.validate(reservation, inventory);

            // Step 2: Allocate room
            inventory.decrementRoom(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("Booking Successful for " + reservation.getGuestName() +
                    " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        // Step 1: Setup inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Deluxe", 0);

        // Step 2: Booking service
        BookingService service = new BookingService(inventory);

        // Step 3: Test cases

        // Valid booking
        service.processBooking(new Reservation("Alice", "Single"));

        // Invalid room type
        service.processBooking(new Reservation("Bob", "Suite"));

        // No availability
        service.processBooking(new Reservation("Charlie", "Deluxe"));

        // Empty guest name
        service.processBooking(new Reservation("", "Single"));

        // Another booking after inventory exhausted
        service.processBooking(new Reservation("David", "Single"));
    }
}