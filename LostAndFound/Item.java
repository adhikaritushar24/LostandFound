public class Item {
    private int id;
    private String itemName;
    private String category;
    private String description;
    private String location;
    private String status;
    private String date;
    private String contactName;
    private String contactPhone;
    private String reportedBy; // track who added it

    public Item(int id, String itemName, String category, String description,
            String location, String status, String date,
            String contactName, String contactPhone) {
        this(id, itemName, category, description, location, status, date, contactName, contactPhone, "");
    }

    public Item(int id, String itemName, String category, String description,
            String location, String status, String date,
            String contactName, String contactPhone, String reportedBy) {
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.location = location;
        this.status = status;
        this.date = date;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.reportedBy = reportedBy;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContactName(String name) {
        this.contactName = name;
    }

    public void setContactPhone(String phone) {
        this.contactPhone = phone;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    @Override
    public String toString() {
        return "[" + status + "] " + itemName + " | " + category + " | " + location + " | " + date;
    }
}