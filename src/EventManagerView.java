import javax.swing.*;
import java.awt.*;

public class EventManagerView extends JFrame {
    // 定义字段
    private JTextField txtEventID = new JTextField(10);
    private JTextField txtEventName = new JTextField(20);
    private JTextField txtEventDate = new JTextField(10);
    private JTextField txtEventLocation = new JTextField(20);
    private JTextArea txtEventDescription = new JTextArea(5, 20);

    private JButton btnLoad = new JButton("Load Event");
    private JButton btnUpdate = new JButton("Save Event");
    private JButton btnDelete = new JButton("Delete Event");

    public EventManagerView() {
        this.setTitle("Manage Events");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(600, 400);

        JPanel panelButton = new JPanel();
        panelButton.add(btnLoad);
        panelButton.add(btnUpdate);
        panelButton.add(btnDelete);
        this.getContentPane().add(panelButton);

        JPanel panelEventID = new JPanel();
        panelEventID.add(new JLabel("Event ID: "));
        panelEventID.add(txtEventID);
        txtEventID.setHorizontalAlignment(JTextField.RIGHT);
        this.getContentPane().add(panelEventID);

        JPanel panelEventName = new JPanel();
        panelEventName.add(new JLabel("Event Name: "));
        panelEventName.add(txtEventName);
        this.getContentPane().add(panelEventName);

        JPanel panelEventDate = new JPanel();
        panelEventDate.add(new JLabel("Event Date: "));
        panelEventDate.add(txtEventDate);
        txtEventDate.setHorizontalAlignment(JTextField.RIGHT);
        this.getContentPane().add(panelEventDate);

        JPanel panelEventLocation = new JPanel();
        panelEventLocation.add(new JLabel("Event Location: "));
        panelEventLocation.add(txtEventLocation);
        this.getContentPane().add(panelEventLocation);


        txtEventDescription.setLineWrap(true);
        txtEventDescription.setWrapStyleWord(true);

        // 将文本区放入滚动窗格
        JScrollPane scrollPane = new JScrollPane(txtEventDescription);

        JPanel panelEventDescription = new JPanel();
        panelEventDescription.add(new JLabel("Event Description: "));
        panelEventDescription.add(scrollPane); // 添加滚动窗格而不是直接添加文本区
        this.getContentPane().add(panelEventDescription);

    }

    public JButton getBtnLoad() {
        return btnLoad;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JTextField getTxtEventID() {
        return txtEventID;
    }

    public JTextField getTxtEventName() {
        return txtEventName;
    }

    public JTextField getTxtEventDate() {
        return txtEventDate;
    }

    public JTextField getTxtEventLocation() {
        return txtEventLocation;
    }

    public JTextArea getTxtEventDescription() {
        return txtEventDescription;
    }


}

