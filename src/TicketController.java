import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    }

    private void loadTicket() {

    }


    public static class TicketView extends JFrame{
        private JTextField txtTicketID  = new JTextField(10);
        private JTextField txtTicketName  = new JTextField(30);
        private JTextField txtTicketPrice  = new JTextField(10);
        private JTextField txtTicketQuantity  = new JTextField(10);


        private JButton btnLoad = new JButton("Load Ticket");
        private JButton btnSave = new JButton("Save Ticket");

        public TicketView() {
            this.setTitle("Manage Tickets");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            this.setSize(500, 200);

            JPanel panelButton = new JPanel();
            panelButton.add(btnLoad);
            panelButton.add(btnSave);
            this.getContentPane().add(panelButton);

            JPanel panelTicketID = new JPanel();
            panelTicketID.add(new JLabel("Ticket ID: "));
            panelTicketID.add(txtTicketID);
            txtTicketID.setHorizontalAlignment(JTextField.RIGHT);
            this.getContentPane().add(panelTicketID);

            JPanel panelTicketName = new JPanel();
            panelTicketName.add(new JLabel("Event Name: "));
            panelTicketName.add(txtTicketName);
            this.getContentPane().add(panelTicketName);

            JPanel panelTicketInfo = new JPanel();
            panelTicketInfo.add(new JLabel("Price: "));
            panelTicketInfo.add(txtTicketPrice);
            txtTicketPrice.setHorizontalAlignment(JTextField.RIGHT);

            this.getContentPane().add(panelTicketInfo);

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

        public JTextField getTxtTicketName() {
            return txtTicketName;
        }

        public JTextField getTxtTicketPrice() {
            return txtTicketPrice;
        }
        
    }
}