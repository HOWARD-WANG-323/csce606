import javax.swing.*;
import java.awt.*;

public class AddressCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Address) {
            Address address = (Address) value;
            String displayValue = address.getStreet() + ", " + address.getCity() + ", " + address.getState() + ", " + address.getPostalCode();
            return super.getListCellRendererComponent(list, displayValue, index, isSelected, cellHasFocus);
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
