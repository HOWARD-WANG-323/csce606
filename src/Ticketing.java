import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Ticketing extends JFrame {
    private JList<Ticket> ticketList;
    private DefaultListModel<Ticket> ticketListModel;
    private JButton selectButton = new JButton("Select");
    private JButton returnButton = new JButton("Return to Event Selection");
    private JLabel lblTicketStatus = new JLabel();

    public Ticketing(List<Ticket> tickets) {
        setTitle("Choose a Ticket");
        setSize(300, 200);
        setLayout(new BorderLayout());

        ticketListModel = new DefaultListModel<>();
        boolean allTicketsSoldOut = true; // Assume all tickets are sold out until proven otherwise.

        for (Ticket ticket : tickets) {
            if ("AVAILABLE".equals(ticket.getTicketStatus())) {  // Check if the ticket status is "AVAILABLE".
                ticketListModel.addElement(ticket);
                allTicketsSoldOut = false;
            }
        }

        if (allTicketsSoldOut) {
            lblTicketStatus.setText("Tickets for this event have been sold out.");
            selectButton.setEnabled(false); // Disable the button if all tickets are sold out.
        }

        ticketList = new JList<>(ticketListModel);
        JScrollPane scrollPane = new JScrollPane(ticketList);

        add(scrollPane, BorderLayout.CENTER);

        add(lblTicketStatus, BorderLayout.NORTH);  // Add the status label at the top.
        JPanel buttonPanel = new JPanel();  // Create a new JPanel to contain both buttons.
        buttonPanel.add(selectButton);
        buttonPanel.add(returnButton);  // Add the "Return" button to the JPanel
        add(buttonPanel, BorderLayout.SOUTH);  // Add the JPanel to the frame

        returnButton.addActionListener(e -> {
            this.dispose();  // Close the Ticketing window

            Application.getInstance().getShopCartController().reopenEventList();
        });

    }

    public JButton getSelectButton() {
        return selectButton;
    }

    public Ticket getSelectedTicket() {
        return ticketList.getSelectedValue();
    }
    public void refreshTicketList(List<Ticket> tickets) {
        ticketListModel.clear();
        boolean allTicketsSoldOut = true;

        for (Ticket ticket : tickets) {
            if ("AVAILABLE".equals(ticket.getTicketStatus())) {
                ticketListModel.addElement(ticket);
                allTicketsSoldOut = false;
            }
        }

        if (allTicketsSoldOut) {
            lblTicketStatus.setText("Tickets for this event have been sold out.");
            selectButton.setEnabled(false); // Disable the button if all tickets are sold out.
        } else {
            lblTicketStatus.setText("");
            selectButton.setEnabled(true); // Enable the button if there are available tickets.
        }
    }


}
