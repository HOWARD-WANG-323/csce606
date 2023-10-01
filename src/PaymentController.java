import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PaymentController implements ActionListener {
    private PaymentView view;
    private Receipt receipt = null;

    private Address currentAddress = null;
    private Card currentCard = null;
    private List<Ticket> addedTickets = new ArrayList<>();
    public Address getCurrentAddress() {
        System.out.println("get current");
        return currentAddress;
        }
    public Card getCurrentCard() { return currentCard; }

    public void setCurrentAddress(Address address) {
        this.currentAddress = address;
    }
    public void setCurrentCard(Card card) {this.currentCard = card;}
    public PaymentController(PaymentView view) {
        this.view = view;

        view.getBtnAdd().addActionListener(this);
        view.getBtnPay().addActionListener(this);
        view.getBtnSetAddress().addActionListener(this);
        view.getBtnSetCard().addActionListener(this);
        view.getBtnDeleteSelected().addActionListener(this);

        receipt = new Receipt();
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnAdd())
            addTicket();
        else if (e.getSource() == view.getBtnPay()){}
//            makeOrder();
        else if (e.getSource() == view.getBtnSetCard()) {
            setCard();
        } else if (e.getSource() == view.getBtnSetAddress()) {
            setAddress();
        }else if (e.getSource() == view.getBtnDeleteSelected()) {
            deleteSelected();
        }
    }

    private void deleteSelected(){
        int selectedRow = view.getTblItems().getSelectedRow();
        if (selectedRow >= 0) {
            // remove the selected row from the table model
            view.items.removeRow(selectedRow);// remove the selected ticket from the list of added tickets
            addedTickets.remove(selectedRow);
            // refresh the layout
            view.revalidate();
            view.repaint();
        }
    }


    private void setCard(){
        Application.getInstance().getCardView().setVisible(true);
    }

    private void setAddress(){
        Application.getInstance().getAddressView().setVisible(true);
    }
    {
        //todo: set address
    }

    private void addTicket() {
        // Get all events from the database
        List<Event> events = Application.getInstance().getDataAdapter().loadAllEvents();

        EventView eventView = new EventView(events);
        eventView.getSelectButton().addActionListener(e -> {
            Event selectedEvent = eventView.getSelectedEvent();

            // Get tickets based on the selected event
            List<Ticket> tickets = Application.getInstance().getDataAdapter().loadTicketsByEventId(selectedEvent.getId());
            tickets.removeAll(addedTickets);  // remove already added tickets from the list

            Ticketing ticketView = new Ticketing(tickets);
            ticketView.getSelectButton().addActionListener(te -> {
                Ticket selectedTicket = ticketView.getSelectedTicket();
                // Add the selected ticket to the shopping cart

                view.addRow(new Object[]{

                        selectedEvent.getEventName(),  // Assuming you can access the event's name from the selectedEvent object
                        selectedTicket.getTicketType(),
                        selectedEvent.getEventDate(),
                        selectedTicket.getPrice()
                });
                addedTickets.add(selectedTicket);  // add to the list of added tickets
                tickets.remove(selectedTicket);// remove from the available tickets list
                ticketView.revalidate();
                ticketView.repaint();
                ticketView.dispose();
            });
            ticketView.setVisible(true);
            eventView.revalidate();
            eventView.repaint();
            eventView.dispose();
        });
        eventView.setVisible(true);


    }




    public static class PaymentView extends JFrame {

        private JButton btnSetTicket = new JButton("Choose a Ticket");
        private JButton btnSetAddress = new JButton("Set Address");
        private JButton btnSetCard = new JButton("Set Card");
        private JButton btnPay = new JButton("Make a payment");

        private JButton btnDeleteSelected = new JButton("Delete Selected");

        private DefaultTableModel items = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        private JTable tblItems = new JTable(items);
        private JLabel labTotal = new JLabel("Total: ");

        private JLabel labCardInfo = new JLabel("No card selected.");
        public PaymentView() {

            this.setTitle("Tickets Shopping Cart");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
            this.setSize(400, 600);

            items.addColumn("Event Name");
            items.addColumn("Ticket Type");
            items.addColumn("Event Date");
            items.addColumn("Price");


            JPanel panelOrder = new JPanel();
            panelOrder.setPreferredSize(new Dimension(400, 450));
            panelOrder.setLayout(new BoxLayout(panelOrder, BoxLayout.PAGE_AXIS));
            tblItems.setBounds(0, 0, 400, 350);
            tblItems.getTableHeader().setReorderingAllowed(false);

            panelOrder.add(tblItems.getTableHeader());
            panelOrder.add(tblItems);
            panelOrder.add(labTotal);
            tblItems.setFillsViewportHeight(true);
            this.getContentPane().add(panelOrder);


            //credit card info
            JPanel panelCardInfo = new JPanel();
            panelCardInfo.setPreferredSize(new Dimension(400, 50));
            panelCardInfo.add(labCardInfo);
            this.getContentPane().add(panelCardInfo);


            JPanel panelButton = new JPanel();
            panelButton.setPreferredSize(new Dimension(400, 100));
            panelButton.add(btnSetTicket);
            panelButton.add(btnDeleteSelected);

            panelButton.add(btnPay);
            panelButton.add(btnSetAddress);
            panelButton.add(btnSetCard);

            this.getContentPane().add(panelButton);

        }

        public void updateCurrentCardLabel(Card card) {
            if (card != null) {
                labCardInfo.setText("Card Holder: " + card.getCardHolderName() + ", Card Number: ****" + card.getCardNumber().substring(card.getCardNumber().length() - 4));
                labCardInfo.setVisible(true);
            } else {
                labCardInfo.setVisible(false);
            }
            this.revalidate();  // refresh the layout
            this.repaint();
        }


        public JButton getBtnAdd() {
            return btnSetTicket;
        }
        public JButton getBtnPay() {
            return btnPay;
        }
        public JButton getBtnSetAddress() {
            return btnSetAddress;
        }
        public JButton getBtnSetCard() {
            return btnSetCard;
        }
        public JLabel getLabTotal() {
            return labTotal;
        }

        public void addRow(Object[] row) {
            items.addRow(row);
        }

        public JLabel getLabCardInfo() {
            return labCardInfo;
        }

        public JButton getBtnDeleteSelected() {
            return btnDeleteSelected;
        }

        public JTable getTblItems() {
            return tblItems;
        }

    }
}