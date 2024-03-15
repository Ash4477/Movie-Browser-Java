import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SignInMenu extends JFrame implements ActionListener {

    private Backend DB;
    private int userID;

    // Menu Stuff
    private static final int MENU_WIDTH = 1200;
    private static final int MENU_HEIGHT = 600;

    // Login Side Panel Stuff
    JPanel loginSide;
    JLabel cineflixLabel;
    JLabel signInLabel;
    JLabel emailLabel;
    JLabel passwordLabel;
    JTextField emailField;
    JPasswordField passField;
    JLabel signupLabel;
    JButton loginButton;
    JButton resetButton;
    JButton forgetPasswordButton;
    JButton signupButton;

    // Movie Side Panel Stuff
    JPanel movieSide;
    JLabel movieImg;
    JLabel movieImg2;
    JLabel movieImg3;
    JLabel movieImg4;

    SignInMenu() {

        DB = new Backend();

        // Frame Setup
        this.setTitle("Sign-In");
        this.setSize(MENU_WIDTH, MENU_HEIGHT);
        this.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setResizable(false);
        this.setLayout(null);

        // LoginSidePanel Stuff
        loginSide = new JPanel();
        loginSide.setBackground(Color.black);
        loginSide.setBounds(900, 0, 300, MENU_HEIGHT);
        loginSide.setLayout(null);

        cineflixLabel = new JLabel("CINEFLIX");
        cineflixLabel.setFont(new Font("BebasNeue", Font.BOLD, 30));
        cineflixLabel.setBackground(Color.black);
        cineflixLabel.setForeground(Color.RED);
        cineflixLabel.setBounds(70, 0, 200, 100);

        signInLabel = new JLabel("Sign In");
        signInLabel.setFont(new Font(null, Font.BOLD, 25));
        signInLabel.setBounds(15, 140, 100, 50);
        signInLabel.setForeground(Color.white);
        signInLabel.setBackground(Color.white);

        emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font(null, Font.PLAIN, 15));
        emailLabel.setBounds(15, 190, 50, 50);
        emailLabel.setForeground(Color.white);
        emailLabel.setBackground(Color.white);

        passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font(null, Font.PLAIN, 15));
        passwordLabel.setBounds(15, 220, 100, 50);
        passwordLabel.setForeground(Color.white);
        passwordLabel.setBackground(Color.white);

        emailField = new JTextField();
        emailField.setBounds(90, 205, 180, 20);
        emailField.setBackground(Color.gray);
        emailField.setForeground(Color.white);

        passField = new JPasswordField();
        passField.setBounds(90, 235, 180, 20);
        passField.setBackground(Color.gray);
        passField.setForeground(Color.white);

        signupLabel = new JLabel("Don't have an account?");
        signupLabel.setBackground(Color.black);
        signupLabel.setForeground(Color.white);
        signupLabel.setBounds(60, 430, 200, 100);
        signupLabel.setFont(new Font(null, Font.PLAIN, 15));

        loginButton = new JButton("Login");
        loginButton.setBounds(30, 280, 100, 30);
        loginButton.setBackground(Color.black);
        loginButton.setForeground(Color.white);
        loginButton.setFocusable(false);
        loginButton.setFont(new Font(null, Font.BOLD, 13));
        loginButton.addActionListener(this);

        resetButton = new JButton("Reset");
        resetButton.setBounds(160, 280, 100, 30);
        resetButton.setBackground(Color.black);
        resetButton.setForeground(Color.white);
        resetButton.setFocusable(false);
        resetButton.setFont(new Font(null, Font.BOLD, 13));
        resetButton.addActionListener(this);

        forgetPasswordButton = new JButton("Forgot Password?");
        forgetPasswordButton.setBounds(30, 330, 230, 30);
        forgetPasswordButton.setBackground(Color.black);
        forgetPasswordButton.setForeground(Color.red);
        forgetPasswordButton.setFocusable(false);
        forgetPasswordButton.setFont(new Font(null, Font.BOLD, 13));
        forgetPasswordButton.addActionListener(this);

        signupButton = new JButton("Sign-Up Here");
        signupButton.setBounds(60, 500, 150, 40);
        signupButton.setBackground(Color.black);
        signupButton.setForeground(Color.blue);
        signupButton.setFocusable(false);
        signupButton.setFont(new Font(null, Font.BOLD, 13));
        signupButton.setBorderPainted(false);
        signupButton.addActionListener(this);

        loginSide.add(cineflixLabel);
        loginSide.add(emailField);
        loginSide.add(passField);
        loginSide.add(signInLabel);
        loginSide.add(emailLabel);
        loginSide.add(passwordLabel);
        loginSide.add(signupLabel);
        loginSide.add(loginButton);
        loginSide.add(resetButton);
        loginSide.add(forgetPasswordButton);
        loginSide.add(signupButton);

        // MovieSidePanel Stuff
        movieSide = new JPanel();
        movieSide.setBackground(Color.red);
        movieSide.setBounds(0, 0, 900, MENU_HEIGHT);
        movieSide.setLayout(null);

        movieImg = new JLabel(
                new ImageIcon(".\\images\\taxidriver.jpg"));
        movieImg.setBounds(0, 0, 250, movieSide.getHeight());
        movieImg.setVisible(true);
        movieSide.add(movieImg);

        movieImg2 = new JLabel(new ImageIcon(".\\images\\2049.png"));
        movieImg2.setBounds(250, 0, 250, movieSide.getHeight());
        movieImg2.setVisible(true);
        movieSide.add(movieImg2);

        movieImg3 = new JLabel(
                new ImageIcon(".\\images\\darkknight.jpg"));
        movieImg3.setBounds(450, 0, 250, movieSide.getHeight());
        movieImg3.setVisible(true);
        movieSide.add(movieImg3);

        movieImg4 = new JLabel(
                new ImageIcon(".\\images\\backtofuture.jpg"));
        movieImg4.setBounds(650, 0, 250, movieSide.getHeight());
        movieImg4.setVisible(true);
        movieSide.add(movieImg4);

        // Adding Components
        this.add(loginSide);
        this.add(movieSide);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginButton) {
            if (emailField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Email Field is Empty", "Message", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (passField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Password Field is Empty", "Message",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int res;
            try {
                res = DB.accountLogin(emailField.getText(), new String(passField.getPassword()));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (res == -2) {
                JOptionPane.showMessageDialog(null, "Incorrect Email", "Message", JOptionPane.WARNING_MESSAGE);
                emailField.setText("");
                passField.setText("");
            } else if (res == -1) {
                JOptionPane.showMessageDialog(null, "Incorrect Password", "Message", JOptionPane.WARNING_MESSAGE);
                emailField.setText("");
                passField.setText("");
            } else {
                try {
                    new MainScreen(res);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                DB.closeConnection();
                this.dispose();
            }
        }

        if (e.getSource() == resetButton) {
            emailField.setText("");
            passField.setText("");
        }

        if (e.getSource() == forgetPasswordButton) {
            new ResetPasswordMenu();
            this.dispose();
            DB.closeConnection();
        }

        if (e.getSource() == signupButton) {
            new SignUpMenu();
            this.dispose();
            DB.closeConnection();
        }
    }
}
