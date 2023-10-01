import java.util.List;

public class Payment {

    private int paymentID;
    private int userID;
    private double paymentAmount=0;
    private String paymentDate;

    private String paymentStatus;

    private Card card;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }


    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPaymentID() {

        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getCustomerInfo() {
        //get the card holder name
        return Application.getInstance().getShopCartController().getCurrentCard().getCardHolderName();
    }

    public Card getCreditCard() {
        return card;
    }
    public void setCreditCard(Card card) {
        this.card = card;
    }

    public String getDeliveryAddress() {
        return Application.getInstance().getShopCartController().getCurrentAddress().getAddress();
    }

    public void setPaymenAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getTicketDetails() {
        StringBuilder ticketDetails = new StringBuilder();
        List<Ticket> tickets = Application.getInstance().getShopCartController().getTickets();
        for (Ticket ticket : tickets) {
            Event event = Application.getInstance().getDataAdapter().loadEvent(ticket.getEventID());
            String eventName = event.getEventName();
            String eventDate = event.getEventDate();
            double ticketPrice = ticket.getPrice();
            double quantity = ticket.getQuantity();
            ticketDetails.append(eventName).append(" ").append(eventDate).append(" ").append(ticketPrice).append(" ").append(quantity).append("\n");
        }
        return ticketDetails.toString();
    }

    public void setTickets(List<Ticket> addedTickets) {
        Application.getInstance().getShopCartController().setTickets(addedTickets);
    }
}
