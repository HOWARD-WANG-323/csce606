import javax.swing.*;
import java.awt.*;

class EventCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Event event) {
            String details = "<html>Event ID: " + event.getEventID()
                    + "<br>Event Name: " + event.getEventName()
                    + "<br>Event Date: " + event.getEventDate()  // Assuming `getEventDate()` exists in your Event class
                    + "<br>Location: " + event.getEventLocation()
                    + "<br>Description: " + event.getEventDescription()  + "</html>";  // Assuming `getTicketPrice()` exists in your Event class
            return super.getListCellRendererComponent(list, details, index, isSelected, cellHasFocus);
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
