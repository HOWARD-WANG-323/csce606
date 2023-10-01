import java.util.Objects;

public class Ticket {
    private int ticketID;
    private int eventID;
    private String ticketStatus;

    private String ticketType;
    private String seatNumber;
    private String eventName;
    private double price;
    private int userID;

    private Event associatedEvent;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public String toString() {
        return eventName + " - " + ticketType + " - $" + price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return ticketID == ticket.ticketID;  // assuming 'id' is the unique identifier for a ticket
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketID);
    }


    public void setStatus(String status) {
        this.ticketStatus = status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int ID) {
        userID = ID;
    }



    // Getter for associatedEvent
    public Event getAssociatedEvent() {
        return associatedEvent;
    }

    // Setter for associatedEvent
    public void setAssociatedEvent(Event event) {
        this.associatedEvent = event;
    }
    public String getEventDate() {
        if(associatedEvent != null) {
            return associatedEvent.getEventDate(); // assuming Event has a getEventDate method
        }
        return null;  // or handle it differently, maybe throw an exception
    }

    public double getQuantity() {
        return 1;
    }
}
