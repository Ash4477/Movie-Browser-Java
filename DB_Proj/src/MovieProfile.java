import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;

public class MovieProfile extends JFrame {
    private int userID;
    private int movieID;
    Backend DB;

    // PosterPanel Stuff
    JPanel posterPanel;
    JLabel moviePoster;
    JButton watchlistButton;

    // InfoPanel Stuff
    JButton goBackButton;
    JPanel infoPanel;
    JLabel movieTitle;
    JLabel movieGenres;
    JLabel releaseYear;
    JLabel movieDirector;
    JLabel productionComp;
    JLabel movieRating;
    JTextArea movieCast;

    MovieProfile(int movID,int userID) throws SQLException {

        this.movieID = movID;
        this.userID = userID;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200,600);
        this.setBackground(Color.BLACK);
        this.setResizable(false);
        this.setLayout(null);

        DB = new Backend();

        // posterPanel Stuff
        posterPanel = new JPanel();
        posterPanel.setBounds(0,0,400,600);
        posterPanel.setBackground(Color.GRAY);
        posterPanel.setLayout(null);

        Image temp_img = new ImageIcon(DB.getMoviePosterPath(movieID)).getImage().getScaledInstance(400,500, Image.SCALE_SMOOTH);
        moviePoster = new JLabel(new ImageIcon(temp_img));
        moviePoster.setBounds(0,0,400,500);

        watchlistButton = new JButton();
        if (DB.recordExists(this.userID,movieID)){
            watchlistButton.setText("Remove From Watchlist");
            watchlistButton.setBackground(Color.RED);
            watchlistButton.setForeground(Color.BLACK);
        }
        else{
            watchlistButton.setText("Add to Watchlist");
            watchlistButton.setBackground(Color.BLACK);
            watchlistButton.setForeground(Color.WHITE);
        }
        watchlistButton.addActionListener(e -> {
            try {
                if (DB.recordExists(this.userID,movieID)){
                    DB.deleteMovieFromWatchlist(this.userID,movieID);
                    watchlistButton.setText("Add to Watchlist");
                    watchlistButton.setBackground(Color.BLACK);
                    watchlistButton.setForeground(Color.WHITE);
                }
                else{
                    DB.addMovieToWatchlist(this.userID,movieID);
                    watchlistButton.setText("Remove From Watchlist");
                    watchlistButton.setBackground(Color.RED);
                    watchlistButton.setForeground(Color.BLACK);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        });

        watchlistButton.setFocusable(false);
        watchlistButton.setBorder(new LineBorder(new Color(100,0,0),5,true));
        watchlistButton.setBounds(0,500,400,65);

        posterPanel.add(moviePoster);
        posterPanel.add(watchlistButton);


        // infoPanel Stuff
        infoPanel = new JPanel();
        infoPanel.setBounds(400,0,800,600);
        infoPanel.setBackground(Color.BLACK);
        infoPanel.setLayout(null);

        movieTitle = new JLabel(" "+DB.getMovieTitle(movieID));
        movieTitle.setFont(new Font(null,Font.BOLD,40));
        movieTitle.setForeground(Color.WHITE);
        movieTitle.setBounds(20,10,800,100);


        movieGenres = new JLabel("     Genres: " + DB.getMovieGenres(movieID));
        movieGenres.setFont(new Font(null,Font.PLAIN,20));
        movieGenres.setForeground(Color.WHITE);
        movieGenres.setBorder(new LineBorder(new Color(100,0,0),5));
        movieGenres.setBounds(20,110,750,100);

        releaseYear = new JLabel("     Release Year: " + String.valueOf(DB.getMovieReleaseYear(movieID)));
        releaseYear.setFont(new Font(null,Font.PLAIN,20));
        releaseYear.setForeground(Color.WHITE);
        releaseYear.setBorder(new LineBorder(new Color(100,0,0),5));
        releaseYear.setBounds(20,210,400,100);

        movieDirector = new JLabel("   Directed By: " + DB.getMovieDirector(movieID));
        movieDirector.setFont(new Font(null,Font.PLAIN,20));
        movieDirector.setForeground(Color.WHITE);
        movieDirector.setBorder(new LineBorder(new Color(100,0,0),5));
        movieDirector.setBounds(420,210,350,100);

        productionComp = new JLabel("      Produced By: " + DB.getMovieProdComp(movieID));
        productionComp.setFont(new Font(null,Font.PLAIN,20));
        productionComp.setForeground(Color.WHITE);
        productionComp.setBorder(new LineBorder(new Color(100,0,0),5));
        productionComp.setBounds(20,310,400,100);

        movieRating = new JLabel(String.valueOf("     Rating: " + DB.getMovieRating(movieID) +"/10"));
        movieRating.setFont(new Font(null,Font.PLAIN,20));
        movieRating.setForeground(Color.WHITE);
        movieRating.setBorder(new LineBorder(new Color(100,0,0),5));
        movieRating.setBounds(420,310,350,100);


        // THIS IS NOT RIGHT -> HAVE TO CREATE CHARACTER PROFILE TOO !!!!!!!!!
        movieCast = new JTextArea("\n     Movie Cast:\n" + "     " +DB.getMovieCastandChars(movieID));
        movieCast.setFont(new Font(null,Font.PLAIN,20));
        movieCast.setBorder(new LineBorder(new Color(100,0,0),5));
        movieCast.setForeground(Color.WHITE);
        movieCast.setBackground(Color.BLACK);
        movieCast.setBounds(20,410,750,150);
        movieCast.setEditable(false);

        goBackButton = new JButton("GO BACK");
        goBackButton.setBackground(Color.BLACK);
        goBackButton.setFocusable(false);
        goBackButton.setForeground(Color.WHITE);
        goBackButton.setBorder(new LineBorder(new Color(100,0,0),5));
        goBackButton.addActionListener(e->{
            this.dispose();
            try {
                new MainScreen(userID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        goBackButton.setBounds(1070,10,100,50);


        infoPanel.add(movieTitle);
        infoPanel.add(movieGenres);
        infoPanel.add(movieDirector);
        infoPanel.add(releaseYear);
        infoPanel.add(movieRating);
        infoPanel.add(productionComp);
        infoPanel.add(movieCast);

        this.add(goBackButton);
        this.add(posterPanel);
        this.add(infoPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}