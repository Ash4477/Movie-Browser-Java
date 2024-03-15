import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.transform.Result;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WatchlistFrame extends JFrame {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 600;

    JPanel contentPanel;
    JLabel userLabel;
    JButton goBackButton;

    WatchlistFrame(int userID) throws SQLException {
        this.setTitle("Watchlist");
        this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBackground(Color.BLUE);

        Backend DB = new Backend();

        contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(FRAME_WIDTH-40, FRAME_HEIGHT + 200 * (DB.getWatchlistSize(userID)-3)));
        contentPanel.setBackground(Color.BLACK);
        contentPanel.setForeground(Color.WHITE);
        contentPanel.setLayout(null);

        userLabel = new JLabel(getUserName(userID)+" 's Watchlist");
        userLabel.setBounds(30,0,400,100);
        userLabel.setBackground(Color.BLACK);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font(null,Font.BOLD,40));
        contentPanel.add(userLabel);

        ResultSet watchListSet = DB.getQueryResult("SELECT movieTitle,moviePosterPath,movieID FROM (MOVIES NATURAL JOIN WATCHLIST) WHERE userID = " + userID);

        int x=200;
        int y=100;
        while(watchListSet.next()){
            Image img = new ImageIcon(watchListSet.getString(2)).getImage().getScaledInstance(150,150,Image.SCALE_SMOOTH);
            JLabel moviePoster = new JLabel(new ImageIcon(img));
            moviePoster.setBounds(30,y,150,150);
            moviePoster.setBorder(new LineBorder(new Color(100,0,0),5));

            JButton movieTitle = new JButton();
            movieTitle.setText(watchListSet.getString(1));
            movieTitle.setBounds(x,y,FRAME_WIDTH-250,150);
            movieTitle.setBackground(Color.BLACK);
            movieTitle.setForeground(Color.WHITE);
            movieTitle.setFont(new Font(null,Font.BOLD,25));
            movieTitle.setBorder(new LineBorder(new Color(100,0,0),5));
            movieTitle.setFocusable(false);
            int movieID = watchListSet.getInt(3);
            movieTitle.addActionListener(e->{
                try {
                    new MovieProfile(movieID,userID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                this.dispose();
            });

            contentPanel.add(movieTitle);
            contentPanel.add(moviePoster);
            y+=160;
        }

        DB.closeConnection();

        goBackButton = new JButton("GO BACK");
        goBackButton.setBackground(Color.BLACK);
        goBackButton.setFocusable(false);
        goBackButton.setForeground(Color.WHITE);
        goBackButton.setBorder(new LineBorder(new Color(100,0,0),5));
        goBackButton.addActionListener(e->{
            try {
                new MainScreen(userID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            this.dispose();
        });
        goBackButton.setBounds(1050,20,100,50);

        contentPanel.add(goBackButton);
        this.add(new JScrollPane(contentPanel));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    String getUserName(int userID) throws SQLException {
        Backend DB = new Backend();
        ResultSet set = DB.getQueryResult("SELECT * FROM USERS WHERE userID = " + userID);
        set.next();
        return set.getString(1);
    }
}
