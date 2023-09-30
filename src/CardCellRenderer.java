import javax.swing.*;
import java.awt.*;

class CardCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Card card) {
            String details = "<html>Card Number: " + card.getCardNumber()
                    + "<br>Card Holder Name: " + card.getCardHolderName()
                    + "<br>Expiration Date: " + card.getExpiryDate() + "</html>";
            return super.getListCellRendererComponent(list, details, index, isSelected, cellHasFocus);
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
