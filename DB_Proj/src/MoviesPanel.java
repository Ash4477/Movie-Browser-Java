import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoviesPanel extends JPanel
{
    ResultSet movieSet;
    Backend DB;
    String movieQuery ;
    int userID;

    MainScreen disposablePanel;

    MoviesPanel(int userID,MainScreen disposablePanel) throws SQLException {
        this(userID,"SELECT * FROM MOVIES");
        this.disposablePanel = disposablePanel;
    }
    MoviesPanel(int userID, String movieQuery) throws SQLException{
        this.userID = userID;
        this.movieQuery = movieQuery;
        this.setBackground(Color.BLACK);
        this.setSize(1000,550);
        this.setLayout(new GridLayout(0,3));


        DB = new Backend();
        movieSet = DB.getQueryResult(movieQuery);


        while(movieSet.next()){
            JPanel movieData = new JPanel();
            movieData.setPreferredSize(new Dimension(300,500));
            movieData.setBackground(new Color(0,0,0));
            movieData.setLayout(null);
            movieData.setBorder(new LineBorder(new Color(100,0,0),5));


            Image tempImg = new ImageIcon(movieSet.getString(5)).getImage().getScaledInstance(200,350, Image.SCALE_SMOOTH);

            JLabel moviePoster = new JLabel(new ImageIcon(tempImg));
            moviePoster.setBounds(60,40,200,350);
            moviePoster.setVisible(true);

            JButton movieTitle = new JButton(movieSet.getString(2));
            movieTitle.setFont(new Font("Oswald",Font.BOLD,15));
            movieTitle.setBounds(10,400,300,50);
            movieTitle.setBackground(new Color(0,0,0));
            movieTitle.setForeground(Color.WHITE);
            movieTitle.setFocusable(false);
            movieTitle.setBorderPainted(false);
            int movieID = movieSet.getInt(1);
            movieTitle.addActionListener(e -> {
                try {
                    new MovieProfile(movieID, userID);
                    disposablePanel.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            movieData.add(movieTitle);
            movieData.add(moviePoster);
            this.add(movieData);
        }
        DB.closeConnection();
    }
}

