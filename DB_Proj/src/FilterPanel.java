import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class FilterPanel extends JPanel implements ChangeListener, ActionListener {

    private FilterListener filterListener;
    final int PANEL_WIDTH = 200;
    final int PANEL_HEIGHT = 600;
    int movRating;
    String movYear;

    JLabel filterLabel;
    JLabel genresLabel;
    JCheckBox[] genresButtons;
    JLabel releaseYearLabel;
    JComboBox releaseYearBox;
    JLabel ratingsLabel;
    JSlider ratingsSlider;
    JButton applyFilterButton;
    JButton removeFilterButton;

    FilterPanel() throws SQLException {
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setLayout(null);
        this.setBorder(new LineBorder(new Color(100,0,0),5,true));


        filterLabel = new JLabel("Filter Tab");
        filterLabel.setBackground(Color.BLACK);
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font(null,Font.BOLD,30));
        filterLabel.setBounds(10,0,200,50);

        genresLabel = new JLabel("Genres:");
        genresLabel.setBackground(Color.BLACK);
        genresLabel.setForeground(Color.WHITE);
        genresLabel.setFont(new Font(null,Font.BOLD,20));
        genresLabel.setBounds(10,20,100,100);

        genresButtons = getGenres();

        releaseYearLabel = new JLabel("Release Year:");
        releaseYearLabel.setBackground(Color.BLACK);
        releaseYearLabel.setForeground(Color.WHITE);
        releaseYearLabel.setFont(new Font(null,Font.BOLD,20));
        releaseYearLabel.setBounds(10,210,150,100);

        releaseYearBox = getReleaseYears();
        releaseYearBox.setBackground(Color.BLACK);
        releaseYearBox.setForeground(Color.WHITE);
        releaseYearBox.setBounds(10,285,100,20);
        releaseYearBox.addActionListener(this);

        ratingsLabel = new JLabel("Rating:");
        ratingsLabel.setBackground(Color.BLACK);
        ratingsLabel.setForeground(Color.WHITE);
        ratingsLabel.setFont(new Font(null,Font.BOLD,20));
        ratingsLabel.setBounds(10,290,150,100);

        ratingsSlider = new JSlider(0,10,5);
        ratingsSlider.setPaintTicks(true);
        ratingsSlider.setMinorTickSpacing(2);
        ratingsSlider.setPaintTrack(true);
        ratingsSlider.setMajorTickSpacing(2);
        ratingsSlider.setPaintLabels(true);
        ratingsSlider.setOrientation(SwingConstants.HORIZONTAL);
        ratingsSlider.setBounds(10,360,150,50);
        ratingsSlider.setBackground(Color.BLACK);
        ratingsSlider.setForeground(Color.WHITE);
        ratingsSlider.addChangeListener(this);

        applyFilterButton = new JButton("Apply Filters");
        applyFilterButton.setBounds(17,430,150,40);
        applyFilterButton.setFont(new Font(null,Font.BOLD,15));
        applyFilterButton.setBorder(new LineBorder(new Color(100,0,0),2));
        applyFilterButton.addActionListener(this);
        applyFilterButton.setBackground(Color.BLACK);
        applyFilterButton.setForeground(Color.WHITE);
        applyFilterButton.setFocusable(false);

        removeFilterButton = new JButton("Remove Filters");
        removeFilterButton.setBounds(17,480,150,40);
        removeFilterButton.setFont(new Font(null,Font.BOLD,15));
        removeFilterButton.setBorder(new LineBorder(new Color(100,0,0),2));
        removeFilterButton.addActionListener(this);
        removeFilterButton.setBackground(Color.BLACK);
        removeFilterButton.setForeground(Color.WHITE);
        removeFilterButton.setFocusable(false);

        this.add(filterLabel);
        this.add(genresLabel);
        for (JCheckBox genresButton : genresButtons) {
            this.add(genresButton);
        }
        this.add(releaseYearLabel);
        this.add(releaseYearBox);
        this.add(ratingsLabel);
        this.add(ratingsSlider);
        this.add(applyFilterButton);
        this.add(removeFilterButton);
    }

    JCheckBox[] getGenres() throws SQLException {
        Backend DB = new Backend();
        ArrayList<String> genresArr = DB.getGenres();
        JCheckBox[] genres = new JCheckBox[genresArr.size()];
        int y = 90;
        for (int i=0;i<genres.length;i++){
            genres[i] = new JCheckBox();
            genres[i].setText(genresArr.get(i));
            genres[i].setFocusable(false);
            genres[i].setBounds(10,y,100,20);
            genres[i].setBackground(Color.BLACK);
            genres[i].setForeground(Color.WHITE);
            y+=20;
        }
        DB.closeConnection();
        return genres;
    }

    JComboBox getReleaseYears() throws SQLException {
        Backend DB = new Backend();
        ArrayList<Integer> years = DB.getReleaseYearsOfMovies();
        DB.closeConnection();
        String[] tempArr = new String[years.size()];
        for (int i=0;i<tempArr.length;i++)
            tempArr[i] = String.valueOf(years.get(i));
        Arrays.sort(tempArr);
        return new JComboBox(tempArr);

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        movRating = ratingsSlider.getValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==releaseYearBox){
            movYear = releaseYearBox.getSelectedItem().toString();
        }

        if (e.getSource()==applyFilterButton){
            applyFilters();
        }

        if (e.getSource()==removeFilterButton){
            removeFilters();
        }
    }

    void applyFilters(){
        String whereQuery = "genreName = '";
        boolean check = false;
        for (JCheckBox genresButton : genresButtons) {
            if (genresButton.isSelected()) {
                if (check) {
                    whereQuery += "' AND genreName = '";
                }
                whereQuery += genresButton.getText();
                check = true;
            }
        }


        whereQuery += "' AND ";

        if (whereQuery.length()<20){
          whereQuery = "SELECT * FROM MOVIES WHERE " + whereQuery;

        }

        else{
            whereQuery = "SELECT * FROM (MOVIES NATURAL JOIN (GENRES NATURAL JOIN MOVIES_GENRES)) WHERE " + whereQuery;
        }

        whereQuery += ("movieRating >= " + movRating);
        whereQuery += (" AND releaseYear >= " + releaseYearBox.getSelectedItem().toString());


        if (filterListener != null) {
            filterListener.onFilterButtonPressed(whereQuery);
        }
    }

    void removeFilters(){
        String whereQuery = "SELECT * FROM MOVIES";
        if (filterListener != null) {
            filterListener.onFilterButtonPressed(whereQuery);
    }
    }

    public void setFilterListener(FilterListener listener){
        this.filterListener = listener;
    }
}

