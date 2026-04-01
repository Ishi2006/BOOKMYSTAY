import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

// Inventory (same idea from Use Case 3)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // READ-ONLY access
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // For debugging / view only
    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

// Search Service (READ-ONLY logic)
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, List<Room> rooms) {

        System.out.println("\n--- Available Rooms ---");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getType());

            // Defensive check → only show available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println();
            }
        }
    }
}

// Main class
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Step 1: Setup inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 0); // unavailable
        inventory.addRoomType("Deluxe", 2);

        // Step 2: Create room domain objects
        List<Room> rooms = new ArrayList<>();

        rooms.add(new Room("Single", 1000, "WiFi, AC"));
        rooms.add(new Room("Double", 1800, "WiFi, AC, TV"));
        rooms.add(new Room("Deluxe", 2500, "WiFi, AC, TV, Mini Bar"));

        // Step 3: Guest searches rooms
        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, rooms);
    }
}