import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ResetPasswordMenu extends JFrame implements ActionListener {

    private Backend DB;
    private static final int MENU_WIDTH = 583;
    private static final int MENU_HEIGHT = 400;
    JLabel backgroundImg;
    JLabel resetPassLabel;
    JLabel usernameLabel;
    JLabel emailLabel;
    JLabel passwordLabel;
    JTextField userField;
    JTextField emailField;
    JTextField passField;
    JButton resetPassButton;
    JButton resetButton;
    JButton signInButton;


    ResetPasswordMenu(){

        DB = new Backend();

        // Frame Setup
        this.setTitle("Reset Password");
        this.setSize(MENU_WIDTH,MENU_HEIGHT);
        this.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setResizable(false);

        backgroundImg = new JLabel(new ImageIcon("D:\\Study Shit\\Uni Shit\\DBMS\\DBMS Project\\DB_Proj\\images\\final_mv.png"));

        resetPassLabel = new JLabel("Reset Password");
        resetPassLabel.setFont(new Font(null,Font.BOLD,50));
        resetPassLabel.setBounds(100,10,400,80);
        resetPassLabel.setForeground(Color.white);
        resetPassLabel.setBackground(Color.white);

        usernameLabel = new JLabel("Username: ");
        usernameLabel.setFont(new Font(null, Font.PLAIN,17));
        usernameLabel.setBounds(120,90,100,50);
        usernameLabel.setForeground(Color.white);
        usernameLabel.setBackground(Color.white);

        emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font(null, Font.PLAIN,17));
        emailLabel.setBounds(120,130,100,50);
        emailLabel.setForeground(Color.white);
        emailLabel.setBackground(Color.white);

        passwordLabel = new JLabel("New Password: ");
        passwordLabel.setFont(new Font(null, Font.PLAIN,17));
        passwordLabel.setBounds(120,170,150,50);
        passwordLabel.setForeground(Color.white);
        passwordLabel.setBackground(Color.white);

        userField = new JTextField();
        userField.setBounds(250,105,180,20);
        userField.setBackground(Color.white);
        userField.setForeground(Color.black);

        emailField = new JTextField();
        emailField.setBounds(250,145,180,20);
        emailField.setBackground(Color.white);
        emailField.setForeground(Color.black);

        passField = new JTextField();
        passField.setBounds(250,185,180,20);
        passField.setBackground(Color.white);
        passField.setForeground(Color.black);

        resetPassButton = new JButton("Reset Password");
        resetPassButton.setBounds(130,240,150,30);
        resetPassButton.setBackground(Color.black);
        resetPassButton.setForeground(Color.white);
        resetPassButton.setFocusable(false);
        resetPassButton.setFont(new Font(null,Font.BOLD,13));
        resetPassButton.addActionListener(this);

        resetButton = new JButton("Reset Fields");
        resetButton.setBounds(290,240,120,30);
        resetButton.setBackground(Color.black);
        resetButton.setForeground(Color.white);
        resetButton.setFocusable(false);
        resetButton.setFont(new Font(null,Font.BOLD,13));
        resetButton.addActionListener(this);

        signInButton = new JButton("Go Back to Sign-In Menu !");
        signInButton.setBounds(130,280,280,30);
        signInButton.setBackground(Color.black);
        signInButton.setForeground(Color.white);
        signInButton.setFocusable(false);
        signInButton.setFont(new Font(null,Font.BOLD,13));
        signInButton.addActionListener(this);


        this.setContentPane(backgroundImg);
        this.add(resetPassLabel);
        this.add(usernameLabel);
        this.add(emailLabel);
        this.add(passwordLabel);
        this.add(userField);
        this.add(emailField);
        this.add(passField);
        this.add(resetPassButton);
        this.add(resetButton);
        this.add(signInButton);


        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==resetPassButton){
            if (userField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Username Field is Empty","Message",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (emailField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Email Field is Empty","Message",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (passField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Password Field is Empty","Message",JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            char res;
            try {
                res = DB.resetPassword(userField.getText(),emailField.getText(),passField.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (res == 'U'){
                JOptionPane.showMessageDialog(null,"Incorrect Username","Message",JOptionPane.INFORMATION_MESSAGE);
            }
            else if (res=='E'){
                JOptionPane.showMessageDialog(null,"Incorrect Email","Message",JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null,"Password Changed Successfully","Message",JOptionPane.PLAIN_MESSAGE);
                userField.setText("");
                emailField.setText("");
                passField.setText("");
            }
        }

        if (e.getSource()==resetButton){
            userField.setText("");
            emailField.setText("");
            passField.setText("");
        }

        if (e.getSource()==signInButton){
            new SignInMenu();
            this.dispose();
            DB.closeConnection();
        }
    }
}
