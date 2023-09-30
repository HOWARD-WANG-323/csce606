import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CardController implements ActionListener {
    private CardView cardView;

    public CardController(CardView cardView) {
        this.cardView = cardView;
        cardView.getBtnLoad().addActionListener(this);
        cardView.getBtnSave().addActionListener(this);
        cardView.getBtnApply().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cardView.getBtnLoad()){
            System.out.println("load!");
            loadCard();
        } else if (e.getSource() == cardView.getBtnSave()) {
            saveCard();
        } else if (e.getSource() == cardView.getBtnApply()) {
            applyCard();
        } else {
            System.out.println("nothing");
        }
    }

    private void applyCard() {
        // Intentionally left blank for future implementation.
    }

    private void saveCard() {
        Card card = new Card();
        card.setCardNumber(cardView.getCardNumber().getText().trim());
        card.setCardHolderName(cardView.getCardHolderName().getText().trim());
        card.setExpirationDate(cardView.getExpirationDate().getText().trim());
        card.setCvv(Integer.parseInt(cardView.getCvv().getText().trim()));
        if (!isCreditCardValid(card)) {
            return;  // Exit the method if card is not valid
        }

        card.setUserID(Application.getInstance().getCurrentUser().getUserID());
        Application.getInstance().getDataAdapter().saveCard(card);
    }
    private void loadCard() {
        try {
            int userID = Application.getInstance().getCurrentUser().getUserID();

            List<Card> cards = Application.getInstance().getDataAdapter().loadCardsByUserID(userID);

            if (cards == null || cards.isEmpty()) {
                throw new Exception("No cards found for this user in the database!");
            }

            Card selectedCard = (Card) JOptionPane.showInputDialog(null,
                    "Choose a card:",
                    "Select Card",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    cards.toArray(),
                    cards.get(0)
            );

            if (selectedCard == null) {
                throw new Exception("Card selection was cancelled by the user.");
            }

            cardView.getCardNumber().setText(selectedCard.getCardNumber());
            Application.getInstance().getOrderController().setCurrentCard(selectedCard);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
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

            int cvv = card.getCvv();
            if (cvv < 100 || cvv > 999 || String.valueOf(cvv).isEmpty()) {
                throw new IllegalArgumentException("Invalid CVV");
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    public static class CardView extends JFrame {
        private JTextField cardNumber = new JTextField(15);
        private JTextField cardHolderName = new JTextField(15);
        private JTextField expirationDate = new JTextField(15);
        private JTextField cvv = new JTextField(5);

        private JButton btnLoad = new JButton("Load Card");
        private JButton btnSave = new JButton("Save Card");
        private JButton btnApply = new JButton("Apply Card");

        public CardView() {
            this.setTitle("Manage Cards");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            this.setSize(500, 400);

            JPanel panelButton = new JPanel();
            panelButton.add(btnLoad);
            panelButton.add(btnSave);
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
        public JButton getBtnLoad() {
            return btnLoad;
        }

        public JButton getBtnSave() {
            return btnSave;
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