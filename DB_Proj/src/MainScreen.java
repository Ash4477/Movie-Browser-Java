import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainScreen extends JFrame  implements FilterListener, ActionListener {

    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGHT = 600;

    FilterPanel fp;
    JPanel tmp;
    JTextField userLabel;
    JButton logOutButton;
    JButton watchlistButton;
    JTextField searchBar;
    JButton searchButton;
    JScrollPane mp;

    private int userID;

    MainScreen(int userID) throws SQLException{
        this.userID = userID;
        this.setTitle("MainScreen");
        this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setForeground(Color.BLACK);
        this.setResizable(false);
        this.setLayout(null);

        // Filter Panel
        fp = new FilterPanel();
        fp.setBounds(0,0,200,600);
        fp.setFilterListener(this);


        // TopMenuPanel
        tmp = new JPanel();
        tmp.setBackground(Color.BLACK);
        tmp.setBounds(200,0,1000,70);
        tmp.setLayout(null);


        userLabel = new JTextField();
        userLabel.setText("        User: " + getUserName(userID));
        userLabel.setBackground(Color.WHITE);
        userLabel.setForeground(new Color(100,0,0));
        userLabel.setBounds(30,15,150,30);
        userLabel.setFont(new Font(null,Font.BOLD,13));
        userLabel.setEditable(false);

        watchlistButton = new JButton("Go To Watchlist");
        watchlistButton.setBackground(Color.WHITE);
        watchlistButton.setForeground(new Color(100,0,0));
        watchlistButton.setFocusable(false);
        watchlistButton.setBounds(tmp.getWidth()-400,15,150,30);
        watchlistButton.addActionListener(this);

        logOutButton = new JButton("Log Out");
        logOutButton.setBackground(Color.WHITE);
        logOutButton.setForeground(new Color(100,0,0));
        logOutButton.setFocusable(false);
        logOutButton.setBounds(tmp.getWidth()-200,15,150,30);
        logOutButton.addActionListener(this);

        searchBar = new JTextField("   Search Movies By Actors/Directors");
        searchBar.setBackground(Color.GRAY);
        searchBar.setForeground(Color.white);
        searchBar.setBounds(220,15,300,30);

        searchButton = new JButton();
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(new Color(100,0,0));
        searchButton.setFocusable(false);
        searchButton.setBounds(530,15,30,30);
        searchButton.addActionListener(this);
        Image temp = new ImageIcon("D:\\Study Shit\\Uni Shit\\DBMS\\DBMS Project\\DB_Proj\\images\\searchIcon.jpg").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        searchButton.setIcon(new ImageIcon(temp));

        tmp.setBorder(new LineBorder(new Color(100,0,0),5));
        tmp.add(userLabel);
        tmp.add(watchlistButton);
        tmp.add(logOutButton);
        tmp.add(searchBar);
        tmp.add(searchButton);


        // Movies Panel
        mp = new JScrollPane(new MoviesPanel(userID,this));
        mp.setBounds(200,70,1000,500);


        this.add(tmp);
        this.add(fp);
        this.add(mp);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    String getUserName(int userID) throws SQLException {
        Backend DB = new Backend();
        ResultSet set = DB.getQueryResult("SELECT * FROM USERS WHERE userID = " + userID);
        set.next();
        return set.getString(1);
    }



    @Override
    public void onFilterButtonPressed(String filterQuery) {
        this.remove(mp);
        try {
            mp = new JScrollPane(new MoviesPanel(userID,filterQuery));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        mp.setBounds(200,70,1000,500);
        this.add(mp);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==logOutButton){
            this.dispose();
            new SignInMenu();
        }

        if (e.getSource()==watchlistButton){
            this.dispose();
            try {
                new WatchlistFrame(userID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource()==searchButton){
            if (searchBar.getText().isEmpty() || searchBar.getText().equals("   Search Movies By Actors/Directors")){
                JOptionPane.showMessageDialog(null,"Search Field is Empty","Message",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            else{
                Backend DB = new Backend();
                String searchQuery = "SELECT * FROM MOVIES WHERE movieTitle = '" + searchBar.getText()+"'";
                System.out.println(searchQuery);
                try {
                    if(!DB.getQueryResult(searchQuery).next()){
                        searchQuery = "SELECT movieID,movieTitle,releaseYear,movieRating,moviePosterPath FROM (MOVIES NATURAL JOIN ACTORS NATURAL JOIN ROLES) WHERE first_name = '" + searchBar.getText() +"'";
                        if(!DB.getQueryResult(searchQuery).next()){
                            searchQuery = "SELECT movieID,movieTitle,releaseYear,movieRating,moviePosterPath FROM (MOVIES NATURAL JOIN DIRECTORS NATURAL JOIN MOVIES_DIRECTORS) WHERE first_name = '" + searchBar.getText()+ "'";
                            if(!DB.getQueryResult(searchQuery).next()){
                                JOptionPane.showMessageDialog(null,"No Such Movie/Actor/Director in Database","Message",JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    this.remove(mp);
                    mp = new JScrollPane(new MoviesPanel(userID,searchQuery));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                mp.setBounds(200,70,1000,500);
                this.add(mp);
                this.revalidate();
                this.repaint();
                DB.closeConnection();
            }


        }
    }
}
