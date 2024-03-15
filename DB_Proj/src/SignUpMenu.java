import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SignUpMenu extends JFrame implements ActionListener {

    private Backend DB;
    private static final int MENU_WIDTH = 583;
    private static final int MENU_HEIGHT = 400;
    JLabel backgroundImg;
    JLabel signUpLabel;
    JLabel usernameLabel;
    JLabel emailLabel;
    JLabel passwordLabel;
    JTextField userField;
    JTextField emailField;
    JTextField passField;
    JButton createAccButton;
    JButton resetButton;
    JButton signInButton;


    SignUpMenu(){

        DB = new Backend();

        // Frame Setup
        this.setTitle("Sign-Up");
        this.setSize(MENU_WIDTH,MENU_HEIGHT);
        this.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setResizable(false);

        backgroundImg = new JLabel(new ImageIcon("D:\\Study Shit\\Uni Shit\\DBMS\\DBMS Project\\DB_Proj\\images\\final_mv.png"));

        signUpLabel = new JLabel("Sign Up");
        signUpLabel.setFont(new Font(null,Font.BOLD,50));
        signUpLabel.setBounds(200,10,200,80);
        signUpLabel.setForeground(Color.white);
        signUpLabel.setBackground(Color.white);

        usernameLabel = new JLabel("Username: ");
        usernameLabel.setFont(new Font(null, Font.PLAIN,17));
        usernameLabel.setBounds(150,90,100,50);
        usernameLabel.setForeground(Color.white);
        usernameLabel.setBackground(Color.white);

        emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font(null, Font.PLAIN,17));
        emailLabel.setBounds(150,130,100,50);
        emailLabel.setForeground(Color.white);
        emailLabel.setBackground(Color.white);

        passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font(null, Font.PLAIN,17));
        passwordLabel.setBounds(150,170,150,50);
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

        createAccButton = new JButton("Create Account");
        createAccButton.setBounds(150,240,150,30);
        createAccButton.setBackground(Color.black);
        createAccButton.setForeground(Color.white);
        createAccButton.setFocusable(false);
        createAccButton.setFont(new Font(null,Font.BOLD,13));
        createAccButton.addActionListener(this);

        resetButton = new JButton("Reset");
        resetButton.setBounds(330,240,100,30);
        resetButton.setBackground(Color.black);
        resetButton.setForeground(Color.white);
        resetButton.setFocusable(false);
        resetButton.setFont(new Font(null,Font.BOLD,13));
        resetButton.addActionListener(this);

        signInButton = new JButton("Go Back to Sign-In Menu !");
        signInButton.setBounds(150,280,280,30);
        signInButton.setBackground(Color.black);
        signInButton.setForeground(Color.white);
        signInButton.setFocusable(false);
        signInButton.setFont(new Font(null,Font.BOLD,13));
        signInButton.addActionListener(this);



        this.setContentPane(backgroundImg);
        this.add(signUpLabel);
        this.add(usernameLabel);
        this.add(emailLabel);
        this.add(passwordLabel);
        this.add(userField);
        this.add(emailField);
        this.add(passField);
        this.add(createAccButton);
        this.add(resetButton);
        this.add(signInButton);


        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==createAccButton){
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
                res = DB.createAccount(userField.getText(),emailField.getText(),passField.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (res=='U'){
                JOptionPane.showMessageDialog(null,"Username Already Taken","Message",JOptionPane.INFORMATION_MESSAGE);
                emailField.setText("");
                passField.setText("");
            }
            if (res=='E'){
                JOptionPane.showMessageDialog(null,"Email Already Used","Message",JOptionPane.INFORMATION_MESSAGE);
                emailField.setText("");
                passField.setText("");
            }
            if (res=='S'){
                JOptionPane.showMessageDialog(null,"Account Created Successfully","Message",JOptionPane.PLAIN_MESSAGE);
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
