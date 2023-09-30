import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {
    private LoginScreen loginScreen;

    public LoginController(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
        this.loginScreen.getBtnLogin().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginScreen.getBtnLogin()) {
            String username = loginScreen.getTxtUserName().getText().trim();
            String password = loginScreen.getTxtPassword().getText().trim();

            System.out.println("Login with username = " + username + " and password = " + password);
            User user = Application.getInstance().getDataAdapter().loadUser(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(null, "This user does not exist!");
            }
            else {
                Application.getInstance().setCurrentUser(user);
                this.loginScreen.setVisible(false);
                Application.getInstance().getMainScreen().logined();
                Application.getInstance().getMainScreen().setVisible(true);
            }
        }
    }

    public static class LoginScreen extends JFrame {
        private JTextField txtUserName = new JTextField(10);
        private JTextField txtPassword = new JTextField(10);
        private JButton    btnLogin    = new JButton("Login");

        public JButton getBtnLogin() {
            return btnLogin;
        }

        public JTextField getTxtPassword() {
            return txtPassword;
        }

        public JTextField getTxtUserName() {
            return txtUserName;
        }

        public LoginScreen() {


            this.setSize(300, 450);
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

            this.getContentPane().add(new JLabel ("Event Online Ticketing System"));

            JPanel main = new JPanel(new SpringLayout());
            main.add(new JLabel("Username:"));
            //fix the field size
            Dimension size = new Dimension(150, 25);
            txtUserName.setPreferredSize(size);
            txtUserName.setMinimumSize(size);
            txtUserName.setMaximumSize(size);
            main.add(txtUserName);
            main.add(new JLabel("Password:"));
            txtPassword.setPreferredSize(size);
            txtPassword.setMinimumSize(size);
            txtPassword.setMaximumSize(size);
            main.add(txtPassword);

            SpringUtilities.makeCompactGrid(main,
                    2, 2, //rows, cols
                    6, 6,        //initX, initY
                    6, 6);       //xPad, yPad

            this.getContentPane().add(main);
            this.getContentPane().add(btnLogin);
        }
    }
}
