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
        try {
            String query = "SELECT * FROM Addresses WHERE UserID = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()) {
                Address address = new Address();
                address.setAddressID(resultSet.getInt(1));
                address.setAddress(resultSet.getString(2));
                address.setUserID(resultSet.getInt(3));
                resultSet.close();
                statement.close();
                return address;
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public Card loadCard(int id) {
        try {
            String query = "SELECT * FROM Cards WHERE CardID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()) {
                Card card = new Card();
                card.setCardID(resultSet.getInt(1));
                card.setUserID(resultSet.getInt(2));
                card.setCard(resultSet.getString(3));
                resultSet.close();
                statement.close();
                return card;
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

//    public boolean saveTicket(Ticket ticket) {
//        return true;
//    }

    public boolean saveAddress(Address address) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Addresses WHERE AddressID = ?");
            statement.setInt(1, address.getAddressID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this address exists, update its fields
                statement = connection.prepareStatement("UPDATE Addresses SET UserID = ?, Address = ? WHERE AddressID = ?");
                statement.setInt(1, address.getUserID());
                statement.setString(2, address.getAddress());
                statement.setInt(3, address.getAddressID());
            }
            else { // this address does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Addresses VALUES (?, ?, ?)");
                statement.setString(2, address.getAddress());
                statement.setInt(3, address.getUserID());
                statement.setInt(1, address.getAddressID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public boolean saveCard(Card card) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Cards WHERE CardID = ?");
            statement.setInt(1, card.getCardID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this address exists, update its fields
                statement = connection.prepareStatement("UPDATE Cards SET UserID = ?, CardNum = ? WHERE CardID = ?");
                statement.setInt(1, card.getUserID());
                statement.setString(2, card.getCard());
                statement.setInt(3, card.getCardID());
            }
            else { // this address does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Cards VALUES (?, ?, ?)");
                statement.setInt(2, card.getUserID());
                statement.setString(3, card.getCard());
                statement.setInt(1, card.getCardID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
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

    public Integer loadLastOrderID(){
        try{
            Integer id = null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(OrderID) FROM Orders");
            if(resultSet.next()){
                id = resultSet.getInt(1);
                System.out.println("id: " + id);
                resultSet.close();
                statement.close();
            }
            else{
                return -1;
            }
            return id;
        }
        catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return null;
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

}
