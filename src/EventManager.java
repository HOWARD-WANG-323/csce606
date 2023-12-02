import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EventManager implements ActionListener {
    private EventManagerView eventManagerView;

    public EventManager(EventManagerView eventManagerView) {
        this.eventManagerView = eventManagerView;
        eventManagerView.getBtnLoad().addActionListener(this);
        eventManagerView.getBtnUpdate().addActionListener(this);
        eventManagerView.getBtnDelete().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == eventManagerView.getBtnLoad()) {
            loadEvent();
        } else if (e.getSource() == eventManagerView.getBtnUpdate()) {
            updateEvent();
        } else if (e.getSource() == eventManagerView.getBtnDelete()) {
            deleteEvent();
        }
    }

    private void loadEvent() {
        // 实现添加事件的逻辑
        // 使用 eventManagerView 获取用户输入
        int eventID = Integer.parseInt(eventManagerView.getTxtEventID().getText());
        Event event = Application.getInstance().getDataAdapter().loadEvent(eventID);
        // display the event info in the text fields
        if (event == null) {
            JOptionPane.showMessageDialog(null, "This event does not exist!");
        }
        else {
            eventManagerView.getTxtEventID().setText(String.valueOf(event.getEventID()));
            eventManagerView.getTxtEventName().setText(event.getEventName());
            eventManagerView.getTxtEventDate().setText(event.getEventDate());
            eventManagerView.getTxtEventLocation().setText(event.getEventLocation());
            eventManagerView.getTxtEventDescription().setText(event.getEventDescription());
        }
    }

    private void updateEvent() {
        Event event = new Event();
        event.setEventID(Integer.parseInt(eventManagerView.getTxtEventID().getText()));
        event.setEventName(eventManagerView.getTxtEventName().getText());
        event.setEventDate(eventManagerView.getTxtEventDate().getText());
        event.setEventLocation(eventManagerView.getTxtEventLocation().getText());
        event.setEventDescription(eventManagerView.getTxtEventDescription().getText());

        DataAdapter dataAdapter = Application.getInstance().getDataAdapter();
        if (dataAdapter.saveEvent(event)) {
            List<Event> updatedEvents = Application.getInstance().getDataAdapter().loadAllEvents();
            //Application.getInstance().getEventView().refreshEventList(updatedEvents);
            JOptionPane.showMessageDialog(null, "Event is saved successfully!");
        }
        else
            JOptionPane.showMessageDialog(null, "Event is NOT saved successfully!");
    }


    private void deleteEvent() {
        // 实现删除事件的逻辑
        // 使用 eventManagerView 获取用户输入
        // 以eventid为主键删除

        int eventID = Integer.parseInt(eventManagerView.getTxtEventID().getText());
        Event event = Application.getInstance().getDataAdapter().loadEvent(eventID);
        if (event == null) {
            JOptionPane.showMessageDialog(null, "This event does not exist!");
        }
        else {
            Application.getInstance().getDataAdapter().deleteEvent(event);
            JOptionPane.showMessageDialog(null, "Event is deleted successfully!");
        }
    }


}
