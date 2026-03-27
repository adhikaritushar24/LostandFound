# Lost & Found System - Java Swing Project
### Semester Project | Java GUI Application

---

## PROJECT STRUCTURE

```
LostAndFound/
├── Main.java           --> Entry point, starts the app
├── User.java           --> User model (username, password, role)
├── Item.java           --> Item model (all fields for lost/found item)
├── DataManager.java    --> All data stored here (users list, items list)
├── LoginFrame.java     --> Login screen (GUI)
├── DashboardFrame.java --> Main dashboard with table, filter, buttons
└── AddItemDialog.java  --> Popup form to add new item
```

---

## HOW TO RUN

1. Open **Command Prompt / Terminal**
2. Go to the project folder:
   ```
   cd path\to\LostAndFound
   ```
3. Compile all files:
   ```
   javac *.java
   ```
4. Run the app:
   ```
   java Main
   ```

---

## DEFAULT LOGIN ACCOUNTS

| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | admin123  | Admin |
| rahul    | rahul123  | User  |
| priya    | priya123  | User  |

> You can also register a new account from the Login screen.

---

## FEATURES

### Login System
- Existing users can login with username + password
- New users can register directly from login screen
- Two roles: **Admin** and **User**

### Dashboard
- View all Lost & Found items in a table
- Color coded: **Red rows = Lost**, **Green rows = Found**
- Filter: Show All / Lost Only / Found Only
- Item count shown at top

### Add Item
- Any logged-in user can add a new Lost or Found item
- Fields: Name, Category, Status, Description, Location, Date, Contact, Phone
- Date auto-fills to today
- Contact auto-fills to logged-in user's name

### Remove Item
- Only **Admin** can remove items (role-based access control)
- Confirmation popup before deleting

### View Details
- Click any item + "View Details" to see full info

---

## SAMPLE DATA (Pre-loaded)
1. Black Wallet - Lost - Library
2. Blue Pen Drive - Found - Canteen
3. Water Bottle - Lost - Ground Floor Corridor

---

## CONCEPTS USED (For Viva)
- Object Oriented Programming (Classes, Objects)
- Inheritance / Encapsulation
- Java Swing (GUI components)
- JFrame, JDialog, JTable, JComboBox, JPanel
- ArrayList for data storage
- Event Handling (ActionListener)
- Role-based Access Control
- DefaultTableModel, TableCellRenderer
