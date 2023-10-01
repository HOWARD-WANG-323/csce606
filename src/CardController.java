import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;
public class CardController implements ActionListener {
    private CardView cardView;

    public CardController(CardView cardView) {
        this.cardView = cardView;
        cardView.getBtnManage().addActionListener(this);
        cardView.getBtnApply().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cardView.getBtnManage()){
            manageSavedCard();
        } else if (e.getSource() == cardView.getBtnApply()) {
            applyCard();
        } else {
            System.out.println("nothing");
        }
    }
    private void manageSavedCard() {
        try {
            int userID = Application.getInstance().getCurrentUser().getUserID();
            List<Card> cards = Application.getInstance().getDataAdapter().loadCardsByUserID(userID);

            DefaultListModel<Card> listModel = new DefaultListModel<>();
            for (Card card : cards) {
                listModel.addElement(card);
            }

            JList<Card> cardList = new JList<>(listModel);
            cardList.setCellRenderer(new CardCellRenderer());
            JScrollPane scrollPane = new JScrollPane(cardList);
            scrollPane.setPreferredSize(new Dimension(250, 150));

            // Select button
            JButton selectButton = new JButton("Select Card");
            selectButton.addActionListener(e -> {
                Card selectedCard = cardList.getSelectedValue();
                if (selectedCard != null) {
                    cardView.getCardNumber().setText(selectedCard.getCardNumber());
                    cardView.getCardHolderName().setText(selectedCard.getCardHolderName());
                    cardView.getExpirationDate().setText(selectedCard.getExpiryDate());

                    // Close the dialog
                    Window window = SwingUtilities.getWindowAncestor(selectButton);
                    if (window != null) {
                        window.dispose();

                    }
                }
            });

            // Delete button
            JButton deleteButton = new JButton("Delete Selected Card");
            deleteButton.addActionListener(e -> {
                Card selectedCard = cardList.getSelectedValue();
                if (selectedCard != null) {
                    Application.getInstance().getDataAdapter().deleteCard(selectedCard);
                    listModel.removeElement(selectedCard);
                }
            });

            Object[] message = {
                    scrollPane,
                    selectButton,
                    deleteButton
            };

            JOptionPane.showOptionDialog(null, message, "Manage Saved Cards",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, new Object[] {}, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    private void applyCard() {
        Card card = new Card();
        card.setCardNumber(cardView.getCardNumber().getText().trim());
        card.setCardHolderName(cardView.getCardHolderName().getText().trim());
        card.setExpirationDate(cardView.getExpirationDate().getText().trim());
        // CVV validation
        String cvvText = cardView.getCvv().getText().trim();
        if(cvvText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "CVV cannot be empty!");
            return;
        }
        try {
            int cvv = Integer.parseInt(cvvText);
            card.setCvv(cvv);
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "CVV is invalid!");
            return;
        }
        if (isCreditCardValid(card)) {
            //message box
            return;  // Exit the method if card is not valid
        }

        card.setUserID(Application.getInstance().getCurrentUser().getUserID());
        Application.getInstance().getDataAdapter().saveCard(card);
        Application.getInstance().getPaymentController().setCurrentCard(card);
        cardView.setVisible(false);
        //message box
        Application.getInstance().getPayController().setCurrentCard(card);
        Application.getInstance().getPaymentView().updateCurrentCardLabel(card);
        JOptionPane.showMessageDialog(null, "Card Applied!");
    }



    private boolean isCreditCardValid(Card card) {
        try {
            String cardNumber = card.getCardNumber();
            if (!cardNumber.matches("\\d{16}") || cardNumber.isEmpty()) {
                throw new IllegalArgumentException("Invalid card number");
            }

            String cardHolderName = card.getCardHolderName();
            if (cardHolderName.isEmpty()) {
                throw new IllegalArgumentException("Invalid card holder name");
            }

            String[] dateParts = card.getExpiryDate().split("/");
            if (dateParts.length != 2) {
                throw new IllegalArgumentException("Invalid date format");
            }

            int month = Integer.parseInt(dateParts[0]);
            int year = Integer.parseInt(dateParts[1]);
            String currentDate = java.time.LocalDate.now().toString().replace("-", "/");
            String[] currentDateParts = currentDate.split("/");
            int currentMonth = Integer.parseInt(currentDateParts[1]);
            int currentYear = Integer.parseInt(currentDateParts[0].substring(2, 4));

            if (year < currentYear || month < 1 || month > 12 || (year == currentYear && month < currentMonth) || String.valueOf(month).isEmpty() || String.valueOf(year).isEmpty()) {
                throw new IllegalArgumentException("Invalid expiration date");
            }

            String cvvStr = String.valueOf(card.getCvv()).trim();
            if (cvvStr.isEmpty()) {
                throw new IllegalArgumentException("Invalid CVV");
            }

            int cvv;
            try {
                cvv = Integer.parseInt(cvvStr);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid CVV");
            }

            if (cvv < 100 || cvv > 999) {
                throw new IllegalArgumentException("Invalid CVV");
            }

            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return true;
        }
    }
    public static class CardView extends JFrame {
        private JTextField cardNumber = new JTextField(15);
        private JTextField cardHolderName = new JTextField(15);
        private JTextField expirationDate = new JTextField(15);
        private JTextField cvv = new JTextField(5);

        private JButton btnManage = new JButton("Manage Saved Card");
        private JButton btnApply = new JButton("Apply Card");

        public CardView() {
            this.setTitle("Manage Cards");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            this.setSize(500, 400);


            JPanel panelButton = new JPanel();
            panelButton.add(btnManage);
            panelButton.add(btnApply);
            this.getContentPane().add(panelButton);

            // Card Number Panel
            JPanel panelCardNumber = new JPanel();
            panelCardNumber.add(new JLabel("Card Number: "));
            panelCardNumber.add(cardNumber);
            this.getContentPane().add(panelCardNumber);

            // Card Holder Name Panel
            JPanel panelCardHolderName = new JPanel();
            panelCardHolderName.add(new JLabel("Card Holder Name: "));
            panelCardHolderName.add(cardHolderName);
            this.getContentPane().add(panelCardHolderName);

            // Expiration Date Panel
            JPanel panelExpirationDate = new JPanel();
            panelExpirationDate.add(new JLabel("Expiration Date (MM/YY): "));
            panelExpirationDate.add(expirationDate);
            this.getContentPane().add(panelExpirationDate);

            // CVV Panel
            JPanel panelCVV = new JPanel();
            panelCVV.add(new JLabel("CVV (3 digits): "));
            panelCVV.add(cvv);
            this.getContentPane().add(panelCVV);
        }
        public JButton getBtnManage() {
            return btnManage;
        }

        public JButton getBtnApply() {
            return btnApply;
        }

        public JTextField getCardNumber() {
            return cardNumber;
        }

        public JTextField getCardHolderName() {
            return cardHolderName;
        }

        public JTextField getExpirationDate() {
            return expirationDate;
        }

        public JTextField getCvv() {
            return cvv;
        }



    }
}