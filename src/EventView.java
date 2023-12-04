import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EventView extends JFrame {
    private JList<Event> eventList;
    private DefaultListModel<Event> eventListModel;
    private JButton selectButton = new JButton("Select");

    public EventView(List<Event> events) {
        setTitle("Choose an Event");
        setSize(500, 300);  // Increased the size to better fit the details

        eventListModel = new DefaultListModel<>();
        for (Event event : events) {
            eventListModel.addElement(event);
        }
        eventList = new JList<>(eventListModel);
        eventList.setCellRenderer(new EventCellRenderer()); // set the custom renderer
        JScrollPane scrollPane = new JScrollPane(eventList);
        add(scrollPane, BorderLayout.CENTER);
        add(selectButton, BorderLayout.SOUTH);
    }

    public JButton getSelectButton() {
        return selectButton;
    }

    public Event getSelectedEvent() {
        return eventList.getSelectedValue();
    }

}
