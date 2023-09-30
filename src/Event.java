public class Event {
    private int eventID;
    private String eventName;
    
    private String eventDate;
    private String eventLocation;
    private String eventDescription;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getId() {
        return eventID;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }
}
