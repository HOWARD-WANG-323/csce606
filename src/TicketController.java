import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TicketController implements ActionListener {
    private TicketView ticketView;

    public TicketController(TicketView ticketView) {
        this.ticketView = ticketView;
        ticketView.getBtnLoad().addActionListener(this);
        ticketView.getBtnSave().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ticketView.getBtnLoad()){
            loadTicket();}
        else
        if (e.getSource() == ticketView.getBtnSave())
            saveTicket();
    }

    private void saveTicket() {
        // save ticket info into database
        Ticket ticket = new Ticket();
        ticket.setTicketID(Integer.parseInt(ticketView.getTxtTicketID().getText()));
        ticket.setEventID(Integer.parseInt(ticketView.getTxtEventID().getText()));
        ticket.setTicketStatus(ticketView.getCmbTicketStatus().getSelectedItem().toString());
        ticket.setPrice(Double.parseDouble(ticketView.getTxtTicketPrice().getText()));
        ticket.setTicketType(ticketView.getTxtTicketType().getText());

        DataAdapter dataAdapter = Application.getInstance().getDataAdapter();
        if (dataAdapter.saveTicket(ticket)) {
            List<Ticket> updatedTickets = Application.getInstance().getDataAdapter().loadTicketsByEventId(ticket.getEventID());
            Application.getInstance().getTicketing().refreshTicketList(updatedTickets);
            JOptionPane.showMessageDialog(null, "Ticket is saved successfully!");
        }
        else
            JOptionPane.showMessageDialog(null, "Ticket is NOT saved successfully!");
    }

    private void loadTicket() {
        // load ticket info from database
        int ticketID = Integer.parseInt(ticketView.getTxtTicketID().getText());
        Ticket ticket = Application.getInstance().getDataAdapter().loadTicket(ticketID);
        // display the ticket info in the text fields
        if (ticket == null) {
            JOptionPane.showMessageDialog(null, "This ticket does not exist!");
        }
        else {
            ticketView.getTxtEventID().setText(String.valueOf(ticket.getEventID()));
            ticketView.getCmbTicketStatus().setSelectedItem(ticket.getTicketStatus());
            ticketView.getTxtTicketPrice().setText(String.valueOf(ticket.getPrice()));
            ticketView.getTxtTicketType().setText(ticket.getTicketType());
        }
    }


    public static class TicketView extends JFrame {
        private JTextField txtTicketID = new JTextField(10);
        private JTextField txtEventID = new JTextField(10);
        private JComboBox<String> cmbTicketStatus = new JComboBox<>(new String[]{"AVAILABLE", "SOLD", "USED"});
        private JTextField txtTicketPrice = new JTextField(10);
        private JTextField txtTicketType = new JTextField(10);

        private JButton btnLoad = new JButton("Load Ticket");
        private JButton btnSave = new JButton("Save Ticket");

        public TicketView() {
            this.setTitle("Manage Tickets");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            this.setSize(600, 300);

            JPanel panelButton = new JPanel();
            panelButton.add(btnLoad);
            panelButton.add(btnSave);
            this.getContentPane().add(panelButton);

            JPanel panelTicketID = new JPanel();
            panelTicketID.add(new JLabel("Ticket ID: "));
            panelTicketID.add(txtTicketID);
            txtTicketID.setHorizontalAlignment(JTextField.RIGHT);
            this.getContentPane().add(panelTicketID);

            JPanel panelEventID = new JPanel();
            panelEventID.add(new JLabel("Event ID: "));
            panelEventID.add(txtEventID);
            this.getContentPane().add(panelEventID);

            JPanel panelTicketStatus = new JPanel();
            panelTicketStatus.add(new JLabel("Ticket Status: "));
            panelTicketStatus.add(cmbTicketStatus);
            this.getContentPane().add(panelTicketStatus);

            JPanel panelTicketPrice = new JPanel();
            panelTicketPrice.add(new JLabel("Price: "));
            panelTicketPrice.add(txtTicketPrice);
            txtTicketPrice.setHorizontalAlignment(JTextField.RIGHT);
            this.getContentPane().add(panelTicketPrice);

            JPanel panelTicketType = new JPanel();
            panelTicketType.add(new JLabel("Ticket Type: "));
            panelTicketType.add(txtTicketType);
            this.getContentPane().add(panelTicketType);
        }

        public JButton getBtnLoad() {
            return btnLoad;
        }

        public JButton getBtnSave() {
            return btnSave;
        }

        public JTextField getTxtTicketID() {
            return txtTicketID;
        }

        public JTextField getTxtEventID() {
            return txtEventID;
        }

        public JComboBox<String> getCmbTicketStatus() {
            return cmbTicketStatus;
        }

        public JTextField getTxtTicketPrice() {
            return txtTicketPrice;
        }

        public JTextField getTxtTicketType() {
            return txtTicketType;
        }


    }
}