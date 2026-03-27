import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static List<User> users = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    private static int nextId = 1;

    // Pre-load some default users
    static {
        users.add(new User("admin", "admin123", "admin"));
        users.add(new User("rahul", "rahul123", "user"));
        users.add(new User("priya", "priya123", "user"));

        // Some sample items for demo
        items.add(new Item(nextId++, "Black Wallet", "Accessory",
                "Leather wallet with ID cards inside",
                "Library", "Lost", "2025-03-10", "Rahul Sharma", "9876543210"));
        items.add(new Item(nextId++, "Blue Pen Drive", "Electronics",
                "16GB SanDisk pendrive",
                "Canteen", "Found", "2025-03-12", "Priya Gupta", "9812345678"));
        items.add(new Item(nextId++, "Water Bottle", "Other",
                "Red Milton bottle with name sticker",
                "Ground Floor Corridor", "Lost", "2025-03-15", "Amit Verma", "9898989898"));
    }

    // ---- User Methods ----

    public static User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static boolean registerUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false; // username already taken
            }
        }
        users.add(new User(username, password, "user"));
        return true;
    }

    // ---- Item Methods ----

    public static List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public static void addItem(Item item) {
        items.add(item);
    }

    public static boolean removeItem(int id) {
        return items.removeIf(item -> item.getId() == id);
    }

    public static int getNextId() {
        return nextId++;
    }

    public static List<Item> getLostItems() {
        List<Item> result = new ArrayList<>();
        for (Item i : items) {
            if (i.getStatus().equals("Lost")) result.add(i);
        }
        return result;
    }

    public static List<Item> getFoundItems() {
        List<Item> result = new ArrayList<>();
        for (Item i : items) {
            if (i.getStatus().equals("Found")) result.add(i);
        }
        return result;
    }
}
