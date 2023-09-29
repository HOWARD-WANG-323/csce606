import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddressController implements ActionListener {
    private AddressView addressView;

    public AddressController(AddressView addressView) {
        this.addressView = addressView;
        addressView.getBtnLoad().addActionListener(this);
        addressView.getBtnSave().addActionListener(this);
        addressView.getBtnApply().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addressView.getBtnLoad()){
            System.out.println("load!");
            loadAddress();}
        else
        if (e.getSource() == addressView.getBtnSave()){
            saveAddress();}
        else if (e.getSource() == addressView.getBtnApply()) {
            applyAddress();
        }
        else{
            System.out.println("nothing");
        }
    }

    private void applyAddress(){
        Address address = Application.getInstance().getOrderController().getCurrentAddress();
        if (address == null){
            JOptionPane.showMessageDialog(null, "You did not choose your address!");
            return;
        }
        else{
            return;
        }
    }
    private void saveAddress() {
        int addressID;
        try {
            addressID = Integer.parseInt(addressView.getTxtAddressID().getText());
            if (addressID < 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid address ID! Please provide a valid address ID!");
            return;
        }

        String addressName = addressView.getTxtAddressName().getText().trim();

        if (addressName.length() == 0) {
            JOptionPane.showMessageDialog(null, "Invalid address name! Please provide a non-empty address name!");
            return;
        }

        // Done all validations! Make an object for this address!

        Address address = new Address();
        address.setAddressID(addressID);
        address.setUserID(Application.getInstance().getCurrentUser().getUserID());
        address.setAddress(addressName);


        // Store the address to the database

        Application.getInstance().getDataAdapter().saveAddress(address);
        Application.getInstance().getOrderController().setCurrentAddress(address);
    }

    private void loadAddress() {
        int addressID = 0;
        try {
            addressID = Integer.parseInt(addressView.getTxtAddressID().getText());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid address ID! Please provide a valid address ID!");
            return;
        }

        Address address = Application.getInstance().getDataAdapter().loadAddress(addressID);

        if (address == null) {
            JOptionPane.showMessageDialog(null, "This address ID does not exist in the database!");
            return;
        }
        else if (address.getUserID() != Application.getInstance().getCurrentUser().getUserID()){
            JOptionPane.showMessageDialog(null, "This address is not belong to this user!");
            return;
        }

        addressView.getTxtAddressName().setText(address.getAddress());
        Application.getInstance().getOrderController().setCurrentAddress(address);
    }


    public static class AddressView extends JFrame{
        private JTextField txtAddressID  = new JTextField(10);
        private JTextField txtAddressName  = new JTextField(30);

        private JButton btnLoad = new JButton("Load Address");
        private JButton btnSave = new JButton("Save Address");
        private JButton btnApply = new JButton("Apply Address");

        public AddressView() {
            this.setTitle("Manage Addresss");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            this.setSize(500, 200);

            JPanel panelButton = new JPanel();
            panelButton.add(btnLoad);
            panelButton.add(btnSave);
            panelButton.add(btnApply);
            this.getContentPane().add(panelButton);

            JPanel panelAddressID = new JPanel();
            panelAddressID.add(new JLabel("Address ID: "));
            panelAddressID.add(txtAddressID);
            txtAddressID.setHorizontalAlignment(JTextField.RIGHT);
            this.getContentPane().add(panelAddressID);

            JPanel panelAddressName = new JPanel();
            panelAddressName.add(new JLabel("Address Name: "));
            panelAddressName.add(txtAddressName);
            this.getContentPane().add(panelAddressName);
        }

        public JButton getBtnLoad() {
            return btnLoad;
        }

        public JButton getBtnSave() {
            return btnSave;
        }

        public JButton getBtnApply() {
            return btnApply;
        }

        public JTextField getTxtAddressID() {
            return txtAddressID;
        }

        public JTextField getTxtAddressName() {
            return txtAddressName;
        }
    }
}