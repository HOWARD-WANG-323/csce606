import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;

public class AddressController implements ActionListener {
    private AddressView addressView;

    public AddressController(AddressView addressView) {
        this.addressView = addressView;
        addressView.getBtnManage().addActionListener(this);
        addressView.getBtnApply().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addressView.getBtnManage()) {
            manageSavedAddress();
        } else if (e.getSource() == addressView.getBtnApply()) {
            applyAddress();
        } else {
            System.out.println("nothing");
        }
    }

    private void manageSavedAddress() {
        try {
            int userID = Application.getInstance().getCurrentUser().getUserID();
            List<Address> addresses = Application.getInstance().getDataAdapter().loadAddressesByUserID(userID);

            DefaultListModel<Address> listModel = new DefaultListModel<>();
            for (Address address : addresses) {
                listModel.addElement(address);
            }

            JList<Address> addressList = new JList<>(listModel);
            addressList.setCellRenderer(new AddressCellRenderer());
            JScrollPane scrollPane = new JScrollPane(addressList);
            scrollPane.setPreferredSize(new Dimension(250, 150));

            // Select button
            JButton selectButton = new JButton("Select Address");
            selectButton.addActionListener(e -> {
                Address selectedAddress = addressList.getSelectedValue();
                if (selectedAddress != null) {
                    addressView.getStreet().setText(selectedAddress.getStreet());
                    addressView.getCity().setText(selectedAddress.getCity());
                    addressView.getStates().setText(selectedAddress.getState());
                    addressView.getPostalCode().setText(selectedAddress.getPostalCode());

                    // Close the dialog
                    Window window = SwingUtilities.getWindowAncestor(selectButton);
                    if (window != null) {
                        window.dispose();
                    }
                }
            });

            // Delete button
            JButton deleteButton = new JButton("Delete Selected Address");
            deleteButton.addActionListener(e -> {
                Address selectedAddress = addressList.getSelectedValue();
                if (selectedAddress != null) {
                    Application.getInstance().getDataAdapter().deleteAddress(selectedAddress);
                    listModel.removeElement(selectedAddress);
                }
            });

            Object[] message = {
                    scrollPane,
                    selectButton,
                    deleteButton
            };

            JOptionPane.showOptionDialog(null, message, "Manage Saved Addresses",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, new Object[] {}, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void applyAddress() {
        Address address = new Address();

        address.setAddress(addressView.getStreet().getText().trim(),
                addressView.getCity().getText().trim(),
                addressView.getStates().getText().trim(),
                addressView.getPostalCode().getText().trim());


        if (!isAddressValid(address)) {
            // message box
            return;  // Exit the method if address is not valid
        }

        address.setUserID(Application.getInstance().getCurrentUser().getUserID());
        Application.getInstance().getDataAdapter().saveAddress(address);
        Application.getInstance().getShopCartController().setCurrentAddress(address);

        addressView.setVisible(false);


        Application.getInstance().getPayController().setCurrentAddress(address);
        Application.getInstance().getPaymentView().updateCurrentAddressLabel(address);
        JOptionPane.showMessageDialog(null, "Address Applied!");
    }
    private boolean isAddressValid(Address address) {
        try {
            // Verify that the street is not empty
            String street = address.getStreet();
            if (street.isEmpty()) {
                throw new IllegalArgumentException("Invalid street");
            }

            // Verify that the city is not empty
            String city = address.getCity();
            if (city.isEmpty()) {
                throw new IllegalArgumentException("Invalid city");
            }

            // Verify that the state is not empty
            String state = address.getState();
            if (state.isEmpty()) {
                throw new IllegalArgumentException("Invalid state");
            }

            // Verify that the zip code is 5 digits
            String zipCode = address.getPostalCode();
            if (!zipCode.matches("\\d{5}")||zipCode.isEmpty()) {
                throw new IllegalArgumentException("Invalid zip code");
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    // Add other required methods similar to those in CardController
    public static class AddressView extends JFrame {
        private JTextField streetField = new JTextField(20);
        private JTextField cityField = new JTextField(20);
        private JTextField stateField = new JTextField(20);
        private JTextField postalCodeField = new JTextField(10);

        private JButton btnManage = new JButton("Manage Saved Addresses");
        private JButton btnApply = new JButton("Apply Address");

        public AddressView() {
            this.setTitle("Manage Addresses");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            this.setSize(500, 400);

            JPanel panelButton = new JPanel();
            panelButton.add(btnManage);
            panelButton.add(btnApply);
            this.getContentPane().add(panelButton);

            // Address Panel
            JPanel panelStreet = new JPanel();
            panelStreet.add(new JLabel("Street: "));
            panelStreet.add(streetField);
            this.getContentPane().add(panelStreet);

            JPanel panelCity = new JPanel();
            panelCity.add(new JLabel("City: "));
            panelCity.add(cityField);
            this.getContentPane().add(panelCity);

            JPanel panelState = new JPanel();
            panelState.add(new JLabel("State: "));
            panelState.add(stateField);
            this.getContentPane().add(panelState);

            JPanel panelPostalCode = new JPanel();
            panelPostalCode.add(new JLabel("Postal Code: "));
            panelPostalCode.add(postalCodeField);
            this.getContentPane().add(panelPostalCode);
        }

        public JButton getBtnManage() {
            return btnManage;
        }

        public JButton getBtnApply() {
            return btnApply;
        }

        public JTextField getStreet() {
            return streetField;
        }

        public JTextField getCity() {
            return cityField;
        }

        public JTextField getStates() {
            return stateField;
        }

        public JTextField getPostalCode() {
            return postalCodeField;
        }


    }
}


