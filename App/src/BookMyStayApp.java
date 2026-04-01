import java.util.HashMap;
import java.util.Map;

// RoomInventory class (Actor)
class RoomInventory {

    // Centralized inventory using HashMap
    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Method to add room type with count
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Method to get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update room availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type does not exist!");
        }
    }

    // Method to display entire inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        // Step 1: Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Step 2: Register room types
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 5);
        inventory.addRoomType("Deluxe", 3);

        // Step 3: Display inventory
        inventory.displayInventory();

        // Step 4: Check availability
        System.out.println("\nAvailable Single Rooms: " + inventory.getAvailability("Single"));

        // Step 5: Update availability
        inventory.updateAvailability("Single", 8);

        // Step 6: Display updated inventory
        inventory.displayInventory();
    }
}