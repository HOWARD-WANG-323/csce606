import java.awt.*;
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


    private TicketController.TicketView ticketView = new TicketController.TicketView();

    private ShoppingCartController.ShopCartView shopCartView = new ShoppingCartController.ShopCartView();


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

    public ShoppingCartController.ShopCartView getPaymentView() {
        return shopCartView;
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
    
    private EventManager eventManager;
    
    private EventManagerView eventManagerView = new EventManagerView();

    private Ticketing ticketing = new Ticketing();

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

    public Ticketing getTicketing() {
        return ticketing;
    }

    public void setTicketing(Ticketing ticketing) {
        this.ticketing = ticketing;
    }

    public DataAdapter getDataAdapter() {
        return dataAdapter;
    }

    private Application() {
        // create SQLite database connection here!
        try {
            dataAdapter = new DataAdapter();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());

            System.exit(2);
        }

        ticketController = new TicketController(ticketView);

        shoppingCartController = new ShoppingCartController(shopCartView);

        addressController = new AddressController(addressView);

        cardController = new CardController(cardView);

        loginController = new LoginController(loginScreen);
        
        eventManager = new EventManager(eventManagerView);
        
    }


    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }

    public ShoppingCartController getPayController() {
        return shoppingCartController;
    }


    public Component getEventManagerView() {
        return eventManagerView;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
