import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter {
    private Connection connection;

    public DataAdapter(Connection connection) {
        this.connection = connection;
    }

    public Product loadProduct(int id) {
        try {
            String query = "SELECT * FROM Products WHERE ProductID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getDouble(4));
                resultSet.close();
                statement.close();

                return product;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

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

    public boolean saveProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
            statement.setInt(1, product.getProductID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Products SET Name = ?, Price = ?, Quantity = ? WHERE ProductID = ?");
                statement.setString(1, product.getName());
                statement.setDouble(2, product.getPrice());
                statement.setDouble(3, product.getQuantity());
                statement.setInt(4, product.getProductID());
            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Products VALUES (?, ?, ?, ?, ?)");
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setDouble(4, product.getQuantity());
                statement.setInt(1, product.getProductID());
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

    public Order loadOrder(int id) {
        try {
            Order order = null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Orders WHERE OrderID = " + id);

            if (resultSet.next()) {
                order = new Order();
                order.setOrderID(resultSet.getInt("OrderID"));
                order.setBuyerID(resultSet.getInt("CustomerID"));
                order.setTotalCost(resultSet.getDouble("TotalCost"));
                order.setDate(resultSet.getString("OrderDate"));
                resultSet.close();
                statement.close();
            }

            // loading the order lines for this order
            resultSet = statement.executeQuery("SELECT * FROM OrderLine WHERE OrderID = " + id);

            while (resultSet.next()) {
                OrderLine line = new OrderLine();
                line.setOrderID(resultSet.getInt(1));
                line.setProductID(resultSet.getInt(2));
                line.setQuantity(resultSet.getDouble(3));
                line.setCost(resultSet.getDouble(4));
                order.addLine(line);
            }

            return order;

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveOrder(Order order) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Orders VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, order.getOrderID());
            statement.setInt(3, order.getBuyerID());
            statement.setString(2, order.getDate());
            statement.setDouble(4, order.getTotalCost());
            statement.setDouble(5, order.getTotalTax());

            statement.execute();    // commit to the database;
            statement.close();

            statement = connection.prepareStatement("INSERT INTO OrderLine VALUES (?, ?, ?, ?)");

            for (OrderLine line: order.getLines()) { // store for each order line!
                statement.setInt(1, line.getOrderID());
                statement.setInt(2, line.getProductID());
                statement.setDouble(3, line.getQuantity());
                statement.setDouble(4, line.getCost());

                statement.execute();    // commit to the database;
            }
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
}
