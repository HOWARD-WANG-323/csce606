import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {

    private JButton btnBuy = new JButton("Order View");
    private JButton btnSell = new JButton("Product View");

    public void logined(){
        User curUser = Application.getInstance().getCurrentUser();
        JLabel user = new JLabel("Current User: " + curUser.getUsername());
        user.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelUser = new JPanel();
        panelUser.add(user);
        this.getContentPane().add(panelUser);
        this.invalidate();
    }


    public MainScreen() {

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);

        btnSell.setPreferredSize(new Dimension(120, 50));
        btnBuy.setPreferredSize(new Dimension(120, 50));
        System.out.println("mainScreen load");
//        User curUser = Application.getInstance().getCurrentUser();

        JLabel title = new JLabel("Store Management System");
//        JLabel user = new JLabel(curUser.getUsername());
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
//        panelTitle.add(user);
        this.getContentPane().add(panelTitle);

        JPanel panelButton = new JPanel();
        panelButton.add(btnBuy);
        panelButton.add(btnSell);

        this.getContentPane().add(panelButton);

        btnBuy.addActionListener(new ActionListener() { // when controller is simple, we can declare it on the fly
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getOrderView().setVisible(true);            }
        });


        btnSell.addActionListener(new ActionListener() { // when controller is simple, we can declare it on the fly
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getProductView().setVisible(true);
            }
        });
    }
}
