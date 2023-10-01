import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter {
    private Connection connection;

    public DataAdapter(Connection connection) {
        this.connection = connection;
    }

//    public Ticket loadTicket(int id) {
//
//    }

    public Address loadAddress(int id) {
        Address address = null;
        String query = "SELECT * FROM Addresses WHERE UserID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    address = new Address();
                    address.setAddressID(resultSet.getInt(1));
                    address.setUserID(resultSet.getInt(2));
                    address.setAddress(resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                }
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return address;
    }

    public Card loadCard(int id) {
        Card card = null;
        String query = "SELECT * FROM Cards WHERE CardID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    card = new Card();
                    card.setCardID(resultSet.getInt(1));
                    card.setUserID(resultSet.getInt(2));
                    card.setCardNumber(resultSet.getString(3));
                    card.setCardHolderName(resultSet.getString(4));
                    card.setExpirationDate(resultSet.getString(5));
                }
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return card;
    }

//    public boolean saveTicket(Ticket ticket) {
//        return true;
//    }

    public boolean saveAddress(Address address) {
        boolean isSaved = false;
        try {
            String checkQuery = "SELECT * FROM Addresses WHERE AddressID = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, address.getAddressID());
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next()) { // this address exists, update its fields
                        String updateQuery = "UPDATE Addresses SET UserID = ?, Address = ? WHERE AddressID = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, address.getUserID());
                            updateStmt.setString(2, address.getAddress());
                            updateStmt.setInt(3, address.getAddressID());
                            updateStmt.executeUpdate();
                            isSaved = true;
                        }
                    } else { // this address does not exist, use insert into
                        String insertQuery = "INSERT INTO Addresses VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, address.getAddressID());
                            insertStmt.setInt(2, address.getUserID());
                            insertStmt.setString(3, address.getAddress());
                            insertStmt.executeUpdate();
                            isSaved = true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return isSaved;
    }

    public boolean saveCard(Card card) {
        boolean isSaved = false;
        try {
            String checkQuery = "SELECT * FROM Cards WHERE CardID = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, card.getCardID());
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next()) {
                        String updateQuery = "UPDATE Cards SET UserID = ?, CardNumber = ?, CardHolderName = ?, ExpirationDate = ? WHERE CardID = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, card.getUserID());
                            updateStmt.setString(2, card.getCardNumber());
                            updateStmt.setString(3, card.getCardHolderName());
                            updateStmt.setString(4, card.getExpiryDate());
                            updateStmt.setInt(5, card.getCardID());
                            updateStmt.executeUpdate();
                            isSaved = true;
                        }
                    } else {
                        String insertQuery = "INSERT INTO Cards VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, card.getCardID());
                            insertStmt.setInt(2, card.getUserID());
                            insertStmt.setString(3, card.getCardNumber());
                            insertStmt.setString(4, card.getCardHolderName());
                            insertStmt.setString(5, card.getExpiryDate());
                            insertStmt.executeUpdate();
                            isSaved = true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return isSaved;
    }


    public boolean saveReceipt(Receipt receipt) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Receipts VALUES (?, ?, ?)");
            statement.setInt(1, receipt.getUserID());
            statement.setInt(2, receipt.getOrderID());
            statement.setString(3, receipt.getContent());

            statement.execute();    // commit to the database;
            statement.close();
            return true; // save successfully!
        }
        catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false;
        }
    }


    public User loadUser(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE UserName = ? AND Password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("UserID"));
                user.setUsername(resultSet.getString("UserName"));
                user.setPassword(resultSet.getString("Password"));
                user.setFullName(resultSet.getString("DisplayName"));
                resultSet.close();
                statement.close();

                return user;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public List<Event> loadAllEvents() {
        List<Event> events = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Events");

            while (resultSet.next()) {
                Event event = new Event();
                event.setEventID(resultSet.getInt("eventID"));
                event.setEventName(resultSet.getString("eventName"));
                event.setEventDate(resultSet.getString("eventDate"));
                event.setEventDescription(resultSet.getString("eventDescription"));
                event.setEventLocation(resultSet.getString("eventLocation"));
                events.add(event);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return events;
    }
    public List<Ticket> loadTicketsByEventId(int eventId) {
        List<Ticket> tickets = new ArrayList<>();

        try {
            String query = "SELECT t.TicketID, t.EventID, e.EventName, t.TicketType, t.SeatNumber, t.Price " +
                    "FROM Tickets t " +
                    "JOIN Events e ON t.EventID = e.EventID " +
                    "WHERE t.EventID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, eventId);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Ticket ticket = new Ticket();

                ticket.setTicketID(resultSet.getInt("TicketID"));
                ticket.setEventID(resultSet.getInt("EventID"));
                ticket.setEventName(resultSet.getString("EventName"));
                ticket.setTicketType(resultSet.getString("TicketType"));
                ticket.setSeatNumber(resultSet.getString("SeatNumber"));
                ticket.setPrice(resultSet.getDouble("Price"));

                tickets.add(ticket);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }

        return tickets;
    }

    public List<Card> loadCardsByUserID(int userID) {
        //load cards from database by userID
        List<Card> cards = new ArrayList<>();
        try {
            String query = "SELECT * FROM Cards WHERE UserID = " + userID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Card card = new Card();
                card.setCardID(resultSet.getInt(1));
                card.setUserID(resultSet.getInt(2));
                card.setCardNumber(resultSet.getString(3));
                card.setCardHolderName(resultSet.getString(4));
                card.setExpirationDate(resultSet.getString(5));
                cards.add(card);
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return cards;
    }

    public void deleteCard(Card selectedCard) {
        String query = "DELETE FROM Cards WHERE CardID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedCard.getCardID());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
    }

    public void deleteAddress(Address selectedAddress) {

    }

    public List<Address> loadAddressesByUserID(int userID) {
        //load addresses from database by userID
        List<Address> addresses = new ArrayList<>();
        try{
            String query = "SELECT * FROM Addresses WHERE UserID = " + userID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                Address address = new Address();
                address.setAddressID(resultSet.getInt(1));
                address.setUserID(resultSet.getInt(2));
                address.setAddress(resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6));

                addresses.add(address);
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return addresses;
    }
}
