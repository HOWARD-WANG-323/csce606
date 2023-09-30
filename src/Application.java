import java.sql.*;

public class Application {

    private static Application instance;   // Singleton pattern

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }
    // Main components of this application

    private Connection connection;

    public Connection getDBConnection() {
        return connection;
    }

    private DataAdapter dataAdapter;

    private User currentUser = null;


    public User getCurrentUser() { return currentUser; }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }


    private ProductController.ProductView productView = new ProductController.ProductView();

    private OrderController.OrderView orderView = new OrderController.OrderView();

    private MainScreen mainScreen = new MainScreen();

    private AddressController.AddressView addressView = new AddressController.AddressView();
    private CardController.CardView cardView = new CardController.CardView();


    public AddressController.AddressView getAddressView(){return addressView;}

    public CardController.CardView getCardView(){return cardView;
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public ProductController.ProductView getProductView() {
        return productView;
    }

    public OrderController.OrderView getOrderView() {
        return orderView;
    }

    public LoginController.LoginScreen loginScreen = new LoginController.LoginScreen();

    public LoginController.LoginScreen getLoginScreen() {
        return loginScreen;
    }

    public LoginController loginController;

    private ProductController productController;

    private AddressController addressController;

    private CardController cardController;

    public ProductController getProductController() {
        return productController;
    }
    public AddressController getAddressController() {
        return addressController;
    }
    public CardController getCardController() {
        return cardController;
    }

    private OrderController orderController;

    public OrderController getOrderController() {
        return orderController;
    }

    public DataAdapter getDataAdapter() {
        return dataAdapter;
    }

    private Application() {
        // create SQLite database connection here!
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:store.db";

            connection = DriverManager.getConnection(url);
            dataAdapter = new DataAdapter(connection);

        }
        catch (ClassNotFoundException ex) {
            System.out.println("SQLite is not installed. System exits with error!");
            ex.printStackTrace();
            System.exit(1);
        }

        catch (SQLException ex) {
            System.out.println("SQLite database is not ready. System exits with error!" + ex.getMessage());

            System.exit(2);
        }

        productController = new ProductController(productView);

        addressController = new AddressController(addressView);

        cardController = new CardController(cardView);

        orderController = new OrderController(orderView);

        loginController = new LoginController(loginScreen);
    }


    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }
}
