import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Ticketing extends JFrame {
    private JList<Ticket> ticketList;
    private DefaultListModel<Ticket> ticketListModel;
    private JButton selectButton = new JButton("Select");

    public Ticketing(List<Ticket> tickets) {
        setTitle("Choose a Ticket");
        setSize(300, 200);

        ticketListModel = new DefaultListModel<>();
        for (Ticket ticket : tickets) {
            ticketListModel.addElement(ticket);
        }

        ticketList = new JList<>(ticketListModel);
        JScrollPane scrollPane = new JScrollPane(ticketList);
        add(scrollPane, BorderLayout.CENTER);
        add(selectButton, BorderLayout.SOUTH);
    }

    public JButton getSelectButton() {
        return selectButton;
    }

    public Ticket getSelectedTicket() {
        return ticketList.getSelectedValue();
    }
}
