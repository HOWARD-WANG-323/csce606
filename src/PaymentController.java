import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaymentController implements ActionListener {
    private PaymentView view;
    private Receipt receipt = null;

    private Address currentAddress = null;
    private Card currentCard = null;

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

        receipt = new Receipt();
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnAdd())
            addProduct();
        else
        if (e.getSource() == view.getBtnPay()){}
//            makeOrder();
        else if (e.getSource() == view.getBtnSetCard()) {
            setCard();
        } else if (e.getSource() == view.getBtnSetAddress()) {
            setAddress();
        }
    }


    private void setCard(){
        Application.getInstance().getCardView().setVisible(true);
    }

    private void setAddress(){
        Application.getInstance().getAddressView().setVisible(true);
    }

//    private void makeOrder() {
//        if (currentAddress == null){
//            JOptionPane.showMessageDialog(null, "Please aet your shipping address first!");
//            return;
//        }
//        if (currentCard == null){
//            JOptionPane.showMessageDialog(null, "Please aet your paying card first!");
//            return;
//        }
//        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
//        order.setDate(sdf.format(new Date()));
//
//        List<OrderLine> lines = order.getLines();
//        if (lines.isEmpty()){
//            JOptionPane.showMessageDialog(null, "The order is empty!");
//            return;
//        }
//        String receiptContent = "";
//        receiptContent = receiptContent + "OrderID: " + order.getOrderID() + '\n';
//        receiptContent = receiptContent + "Customer ID: " + Integer.toString(Application.getInstance().getCurrentUser().getUserID()) + '\n';
//        receiptContent = receiptContent + "Customer Name: " + Application.getInstance().getCurrentUser().getUsername() + '\n';
//        receiptContent = receiptContent + "Order's Date Time: " + order.getDate() + '\n';
//        receiptContent = receiptContent + "Total Cost: " + order.getTotalCost() + '\n';
////        for(OrderLine line: lines){
//////            System.out.println(line.getProductID());
////            int id = line.getProductID();
////            Ticket ticket = Application.getInstance().getDataAdapter().loadProduct(id);
////            double quantity = line.getQuantity();
////            ticket.setQuantity(ticket.getQuantity() - quantity); // update new quantity!!
////            Application.getInstance().getDataAdapter().saveProduct(ticket);
////            receiptContent = receiptContent + '\t' + ticket.getName() + '\t' + "cost: " + ticket.getPrice() + " quantity: " + quantity + '\n';
////        }
//
//
//        Application.getInstance().getDataAdapter().saveOrder(order);
//
//        receiptContent = receiptContent + "Total Tax: " + order.getTotalTax() + '\n';
//        receiptContent = receiptContent + "Shipping Address: " + currentAddress.getAddress() + '\n';
//        String card = currentCard.getCard();
//        receiptContent = receiptContent + "Paying Card: " + "****" + card.substring(card.length() - 4);
//        receipt.setContent(receiptContent);
//        receipt.setUserID(Application.getInstance().getCurrentUser().getUserID());
//        receipt.setOrderID(order.getOrderID());
//        System.out.println(receiptContent);
//        Application.getInstance().getDataAdapter().saveReceipt(receipt);
//    }

    private void addProduct() {
//        int orderID = Application.getInstance().getDataAdapter().loadLastOrderID();
//        if (orderID == -1){
//            order.setOrderID(0);
//        }
//        else{
//            order.setOrderID(orderID + 1);
//        }
//        String id = JOptionPane.showInputDialog("Enter ProductID: ");
//        Ticket ticket = Application.getInstance().getDataAdapter().loadProduct(Integer.parseInt(id));
//        if (ticket == null) {
//            JOptionPane.showMessageDialog(null, "This product does not exist!");
//            return;
//        }

//        double quantity = Double.parseDouble(JOptionPane.showInputDialog(null,"Enter quantity: "));
//
//        if (quantity < 0 || quantity > ticket.getQuantity()) {
//            JOptionPane.showMessageDialog(null, "This quantity is not valid!");
//            return;
//        }

//        OrderLine line = new OrderLine();
//        line.setOrderID(this.order.getOrderID());
//        line.setProductID(ticket.getProductID());
//        line.setQuantity(quantity);
//        line.setCost(quantity * ticket.getPrice());
//        order.getLines().add(line);
//        order.setTotalCost(order.getTotalCost() + line.getCost());



//        Object[] row = new Object[5];
//        row[0] = line.getProductID();
//        row[1] = ticket.getName();
//        row[2] = ticket.getPrice();
//        row[3] = line.getQuantity();
//        row[4] = line.getCost();

//        this.view.addRow(row);
//        this.view.getLabTotal().setText("Total: $" + order.getTotalCost());
//        this.view.invalidate();
    }

    public static class PaymentView extends JFrame {

        private JButton btnSetTicket = new JButton("Choose a Ticket");
        private JButton btnSetAddress = new JButton("Set Address");
        private JButton btnSetCard = new JButton("Set Card");
        private JButton btnPay = new JButton("Make a payment");

        private DefaultTableModel items = new DefaultTableModel(); // store information for the table!

        private JTable tblItems = new JTable(items);
        private JLabel labTotal = new JLabel("Total: ");

        public PaymentView() {

            this.setTitle("Order View");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
            this.setSize(400, 600);
            
            items.addColumn("Ticket ID");
            items.addColumn("Event Name");
            items.addColumn("Price");

            JPanel panelOrder = new JPanel();
            panelOrder.setPreferredSize(new Dimension(400, 450));
            panelOrder.setLayout(new BoxLayout(panelOrder, BoxLayout.PAGE_AXIS));
            tblItems.setBounds(0, 0, 400, 350);
            panelOrder.add(tblItems.getTableHeader());
            panelOrder.add(tblItems);
            panelOrder.add(labTotal);
            tblItems.setFillsViewportHeight(true);
            this.getContentPane().add(panelOrder);

            JPanel panelButton = new JPanel();
            panelButton.setPreferredSize(new Dimension(400, 100));
            panelButton.add(btnSetTicket);
            panelButton.add(btnPay);
            panelButton.add(btnSetAddress);
            panelButton.add(btnSetCard);
            this.getContentPane().add(panelButton);

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
    }
}