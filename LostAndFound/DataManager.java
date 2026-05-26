import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataManager {

    private static List<User> users = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    private static int nextId = 1;

    static {
        users.add(new User("admin", "admin123", "admin", "admin@lostandfound.com", "9000000000"));
        users.add(new User("rahul", "rahul123", "user", "rahul@email.com", "9876543210"));
        users.add(new User("priya", "priya123", "user", "priya@email.com", "9812345678"));

        items.add(new Item(nextId++, "Black Wallet", "Accessory",
                "Leather wallet with ID cards inside",
                "Library", "Lost", "2025-03-10", "Rahul Sharma", "9876543210", "rahul"));
        items.add(new Item(nextId++, "Blue Pen Drive", "Electronics",
                "16GB SanDisk pendrive",
                "Canteen", "Found", "2025-03-12", "Priya Gupta", "9812345678", "priya"));
        items.add(new Item(nextId++, "Water Bottle", "Other",
                "Red Milton bottle with name sticker",
                "Ground Floor Corridor", "Lost", "2025-03-15", "Amit Verma", "9898989898", "rahul"));
        items.add(new Item(nextId++, "College ID Card", "ID/Documents",
                "Student ID card - Roll No. 2021CS045",
                "Main Gate", "Found", "2025-03-18", "Security Guard", "9811111111", "admin"));
        items.add(new Item(nextId++, "Blue Backpack", "Other",
                "Navy blue Wildcraft bag with laptop inside",
                "Parking Lot", "Lost", "2025-03-20", "Sneha Patel", "9822222222", "rahul"));
        items.add(new Item(nextId++, "Spectacles", "Accessory",
                "Black frame reading glasses in brown case",
                "Classroom 204", "Found", "2025-03-22", "Ravi Kumar", "9833333333", "priya"));
        items.add(new Item(nextId++, "Physics Textbook", "Book/Stationery",
                "HC Verma Part 1, name written inside",
                "Reading Room", "Lost", "2025-03-25", "Aman Singh", "9844444444", "rahul"));
        items.add(new Item(nextId++, "Umbrella", "Other",
                "Black folding umbrella with red handle",
                "Canteen", "Claimed", "2025-03-28", "Admin", "9000000000", "admin"));
    }

    // ---- AUTH ----

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
            if (u.getUsername().equalsIgnoreCase(username))
                return false;
        }
        users.add(new User(username, password, "user"));
        return true;
    }

    public static boolean changePassword(String username, String oldPass, String newPass) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(oldPass)) {
                u.setPassword(newPass);
                return true;
            }
        }
        return false;
    }

    // ---- USER MANAGEMENT ----

    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public static boolean promoteUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                u.setRole("admin");
                return true;
            }
        }
        return false;
    }

    public static boolean demoteUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                u.setRole("user");
                return true;
            }
        }
        return false;
    }

    public static boolean deleteUser(String username) {
        return users.removeIf(u -> u.getUsername().equals(username));
    }

    public static User getUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    // ---- ITEM CRUD ----

    public static List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public static void addItem(Item item) {
        items.add(item);
    }

    public static boolean removeItem(int id) {
        return items.removeIf(item -> item.getId() == id);
    }

    public static Item getItemById(int id) {
        for (Item i : items) {
            if (i.getId() == id)
                return i;
        }
        return null;
    }

    public static int getNextId() {
        return nextId++;
    }

    // ---- FILTERS ----

    public static List<Item> getLostItems() {
        return items.stream().filter(i -> "Lost".equals(i.getStatus())).collect(Collectors.toList());
    }

    public static List<Item> getFoundItems() {
        return items.stream().filter(i -> "Found".equals(i.getStatus())).collect(Collectors.toList());
    }

    public static List<Item> getClaimedItems() {
        return items.stream().filter(i -> "Claimed".equals(i.getStatus())).collect(Collectors.toList());
    }

    public static List<Item> searchItems(String keyword, String statusFilter, String categoryFilter) {
        String kw = keyword == null ? "" : keyword.toLowerCase().trim();
        return items.stream().filter(i -> {
            boolean matchStatus = "All".equals(statusFilter) || i.getStatus().equals(statusFilter);
            boolean matchCategory = "All".equals(categoryFilter) || i.getCategory().equals(categoryFilter);
            boolean matchKeyword = kw.isEmpty()
                    || i.getItemName().toLowerCase().contains(kw)
                    || i.getDescription().toLowerCase().contains(kw)
                    || i.getLocation().toLowerCase().contains(kw)
                    || i.getContactName().toLowerCase().contains(kw)
                    || i.getCategory().toLowerCase().contains(kw);
            return matchStatus && matchCategory && matchKeyword;
        }).collect(Collectors.toList());
    }

    public static List<Item> getItemsByUser(String username) {
        return items.stream().filter(i -> username.equals(i.getReportedBy())).collect(Collectors.toList());
    }

    // ---- STATISTICS ----

    public static int countTotal() {
        return items.size();
    }

    public static int countLost() {
        return (int) items.stream().filter(i -> "Lost".equals(i.getStatus())).count();
    }

    public static int countFound() {
        return (int) items.stream().filter(i -> "Found".equals(i.getStatus())).count();
    }

    public static int countClaimed() {
        return (int) items.stream().filter(i -> "Claimed".equals(i.getStatus())).count();
    }

    public static int countUsers() {
        return users.size();
    }

    // Category count
    public static int countByCategory(String category) {
        return (int) items.stream().filter(i -> i.getCategory().equals(category)).count();
    }

    // ---- EXPORT ----

    public static String exportToCSV(List<Item> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Item Name,Category,Description,Location,Status,Date,Contact Name,Phone,Reported By\n");
        for (Item i : list) {
            sb.append(i.getId()).append(",")
                    .append(csvEscape(i.getItemName())).append(",")
                    .append(csvEscape(i.getCategory())).append(",")
                    .append(csvEscape(i.getDescription())).append(",")
                    .append(csvEscape(i.getLocation())).append(",")
                    .append(csvEscape(i.getStatus())).append(",")
                    .append(csvEscape(i.getDate())).append(",")
                    .append(csvEscape(i.getContactName())).append(",")
                    .append(csvEscape(i.getContactPhone())).append(",")
                    .append(csvEscape(i.getReportedBy())).append("\n");
        }
        return sb.toString();
    }

    private static String csvEscape(String s) {
        if (s == null)
            return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}