import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            loadCard();}
        else
        if (e.getSource() == cardView.getBtnSave()){
            saveCard();}
        else if (e.getSource() == cardView.getBtnApply()) {
            applyCard();
        }
        else{
            System.out.println("nothing");
        }
    }

    private void applyCard(){
//        Card card = Application.getInstance().getOrderController().getCurrentCard();
//        if (card == null){
//            JOptionPane.showMessageDialog(null, "You did not choose your card!");
//            return;
//        }
//        else{
//            return;
//        }
    }
    private void saveCard() {
        int cardID;
        try {
            cardID = Integer.parseInt(cardView.getTxtCardID().getText());
            if (cardID < 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid card ID! Please provide a valid card ID!");
            return;
        }

        String cardName = cardView.getTxtCardName().getText().trim();

        if (cardName.length() < 13 || cardName.length() > 19) {
            JOptionPane.showMessageDialog(null, "Invalid card number! Please provide a card number within 13-19 digits!");
            return;
        }

        // Done all validations! Make an object for this card!

        Card card = new Card();
        card.setCardID(cardID);
        card.setUserID(Application.getInstance().getCurrentUser().getUserID());
        card.setCard(cardName);


        // Store the card to the database

        Application.getInstance().getDataAdapter().saveCard(card);
//        Application.getInstance().getOrderController().setCurrentCard(card);
    }

    private void loadCard() {
        int cardID = 0;
        try {
            cardID = Integer.parseInt(cardView.getTxtCardID().getText());
            if(cardID < 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid card ID! Please provide a valid card ID!");
            return;
        }

        Card card = Application.getInstance().getDataAdapter().loadCard(cardID);

        if (card == null) {
            JOptionPane.showMessageDialog(null, "This card ID does not exist in the database!");
            return;
        }
        else if (card.getUserID() != Application.getInstance().getCurrentUser().getUserID()){
            JOptionPane.showMessageDialog(null, "This card is not belong to this user!");
            return;
        }

        cardView.getTxtCardName().setText(card.getCard());
//        Application.getInstance().getOrderController().setCurrentCard(card);
    }


    public static class CardView extends JFrame{
        private JTextField txtCardID  = new JTextField(10);
        private JTextField txtCardName  = new JTextField(30);

        private JButton btnLoad = new JButton("Load Card");
        private JButton btnSave = new JButton("Save Card");
        private JButton btnApply = new JButton("Apply Card");

        public CardView() {
            this.setTitle("Manage Cards");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            this.setSize(500, 200);

            JPanel panelButton = new JPanel();
            panelButton.add(btnLoad);
            panelButton.add(btnSave);
            panelButton.add(btnApply);
            this.getContentPane().add(panelButton);

            JPanel panelCardID = new JPanel();
            panelCardID.add(new JLabel("Card ID: "));
            panelCardID.add(txtCardID);
            txtCardID.setHorizontalAlignment(JTextField.RIGHT);
            this.getContentPane().add(panelCardID);

            JPanel panelCardName = new JPanel();
            panelCardName.add(new JLabel("Card Name: "));
            panelCardName.add(txtCardName);
            this.getContentPane().add(panelCardName);
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

        public JTextField getTxtCardID() {
            return txtCardID;
        }

        public JTextField getTxtCardName() {
            return txtCardName;
        }
    }
}