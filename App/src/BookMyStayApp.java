import java.util.*;

// Reservation class (Actor)
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

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}

// Booking Request Queue Manager
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all pending requests
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue ---");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.displayReservation();
        }
    }

    // Peek next request (without removing)
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Process next request (dequeue)
    public Reservation processNextRequest() {
        return queue.poll();
    }
}

// Main class
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        // Step 1: Initialize queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Step 2: Guests submit booking requests
        requestQueue.addRequest(new Reservation("Alice", "Single"));
        requestQueue.addRequest(new Reservation("Bob", "Deluxe"));
        requestQueue.addRequest(new Reservation("Charlie", "Single"));

        // Step 3: Display queue (FIFO order)
        requestQueue.displayQueue();

        // Step 4: Peek next request
        Reservation next = requestQueue.peekNextRequest();
        System.out.println("\nNext Request to Process:");
        if (next != null) next.displayReservation();

        // Step 5: Process requests (FIFO)
        System.out.println("\nProcessing Requests...");
        while (requestQueue.peekNextRequest() != null) {
            Reservation processed = requestQueue.processNextRequest();
            System.out.print("Processed: ");
            processed.displayReservation();
        }

        // Step 6: Final queue state
        requestQueue.displayQueue();
    }
}