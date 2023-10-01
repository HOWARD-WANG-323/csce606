import java.sql.*;
import java.util.List;

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


    private TicketController.TicketView ticketView = new TicketController.TicketView();

    private ShoppingCartController.PaymentView paymentView = new ShoppingCartController.PaymentView();


    private MainScreen mainScreen = new MainScreen();

    private AddressController.AddressView addressView = new AddressController.AddressView();
    private CardController.CardView cardView = new CardController.CardView();


    public AddressController.AddressView getAddressView(){return addressView;}

    public CardController.CardView getCardView(){return cardView;
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public TicketController.TicketView getTicketView() {
        return ticketView;
    }

    public ShoppingCartController.PaymentView getPaymentView() {
        return paymentView;
    }


    public LoginController.LoginScreen loginScreen = new LoginController.LoginScreen();

    public LoginController.LoginScreen getLoginScreen() {
        return loginScreen;
    }

    public LoginController loginController;

    private TicketController ticketController;
    private ShoppingCartController shoppingCartController;

    private AddressController addressController;

    private CardController cardController;

    public TicketController getTicketController() {
        return ticketController;
    }

    public ShoppingCartController getShopCartController() {
        return shoppingCartController;
    }
    public AddressController getAddressController() {
        return addressController;
    }
    public CardController getCardController() {
        return cardController;
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

        ticketController = new TicketController(ticketView);

        shoppingCartController = new ShoppingCartController(paymentView);

        addressController = new AddressController(addressView);

        cardController = new CardController(cardView);

        loginController = new LoginController(loginScreen);
    }


    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }

    public ShoppingCartController getPayController() {
        return shoppingCartController;
    }


}
