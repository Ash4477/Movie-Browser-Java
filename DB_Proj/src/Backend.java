import java.sql.*;
import java.util.ArrayList;

public class Backend {
    private Connection con = null;
    // Connection Functions
    Backend(){
        // Establishing Connection
        con = openConnection();
    }
    private Connection openConnection(){
        try {
            String url = "jdbc:mysql://localhost:3306/Movies_Database";
            String username = "root";
            String password = "Supremeking472001";

            // loading driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // establishing connection
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successfully Established! :>");

            return connection;
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    void closeConnection(){
        if (con == null){
            System.out.println("Connection already closed!");
            return;
        }

        try {
            con.close();
            System.out.println("Connection Successfully Closed! :>");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void restartConnection() throws SQLException {
        con.close();
        con = openConnection();
    }

    ResultSet getQueryResult(String query){
        try {
            Statement statement = con.createStatement();
            return statement.executeQuery(query);
        }

        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    // User DB Functions
    int accountLogin(String email, String password) throws SQLException {
        ResultSet userSet = getQueryResult("SELECT email,userPassword,userID FROM USERS");
        while(userSet.next()){
            if (email.equals(userSet.getString(1))){
                if (password.equals(userSet.getString(2))){
                    return(userSet.getInt(3));
                }
                else{
                    // incorrect password
                    return(-1);
                }
            }
        }
        // incorrect username
        return(-2);
    }
    char createAccount(String username, String email, String password) throws SQLException {
        if (userNameExists(username))
            return 'U';
        if (userEmailExists(email))
            return 'E';

        PreparedStatement pstm = con.prepareStatement("INSERT INTO USERS(userName,email,userPassword) VALUES (?,?,?)");
        pstm.setString(1,username);
        pstm.setString(2,email);
        pstm.setString(3,password);
        pstm.executeUpdate();

        return 'S';
    }
    char resetPassword(String username, String email, String newPass) throws SQLException {
        if (!userNameExists(username))
            return 'U';
        if (!userEmailExists(email))
            return 'E';

        PreparedStatement pstm = con.prepareStatement("UPDATE users SET userPassword = ? WHERE userName = ?" );
        pstm.setString(1,newPass);
        pstm.setString(2,username);
        pstm.executeUpdate();

        return 'S';
    }
    private boolean userNameExists(String userName) throws SQLException {
        ResultSet set = getQueryResult("SELECT * FROM USERS WHERE userName='" + userName +"'");
        if (set == null)
            return false;
        return set.next();
    }
    private boolean userEmailExists(String email) throws SQLException {
        ResultSet set = getQueryResult("SELECT * FROM USERS WHERE email='" + email +"'");
        if (set == null)
            return false;
        return set.next();
    }

    // Movie DB Functions
    String getMovieTitle(int movieID) throws SQLException {
        ResultSet set = getQueryResult("select movieTitle from Movies where movieID="+movieID);
        set.next();
        return set.getString(1);
    }
    String getMoviePosterPath(int movieID) throws SQLException{
        ResultSet set = getQueryResult("select moviePosterPath from Movies where movieID="+movieID);
        set.next();
        return set.getString(1);
    }
    int getMovieReleaseYear(int movieID) throws SQLException {
        ResultSet set = getQueryResult("select releaseYear from Movies where movieID="+movieID);
        set.next();
        return set.getInt(1);
    }
    String getMovieDirector(int movieID) throws SQLException{
        ResultSet set = getQueryResult("SELECT Directors.first_name, Directors.last_name FROM (Directors NATURAL JOIN (Movies_Directors NATURAL JOIN Movies)) WHERE movieID="+movieID);
        String result = "";
        while(set.next()){
            if (!(result.isEmpty())){
                result += ", ";
            }
            result += set.getString(1) + " " + set.getString(2);
        }
        return result;
    }
    String getMovieGenres(int movieID) throws SQLException{
        ResultSet set = getQueryResult("SELECT genreName FROM (Movies NATURAL JOIN (Movies_Genres NATURAL JOIN Genres)) WHERE movieID=" + movieID);
        String res="";
        while(set.next()) {
            if (!(res.isEmpty())){
                res += ", ";
            }
            res += set.getString(1);
        }
        return res;
    }
    String getMovieProdComp(int movieID) throws SQLException{
        ResultSet set = getQueryResult("SELECT companyName FROM (MOVIES NATURAL JOIN (Movies_ProductionCompany NATURAL JOIN ProductionCompany)) where movieID="+movieID);
        set.next();
        return set.getString(1);
    }
    int getMovieRating(int movieID) throws SQLException{
        ResultSet set = getQueryResult("SELECT movieRating FROM Movies WHERE movieID="+movieID);
        set.next();
        return set.getInt(1);
    }
    String getMovieCastandChars(int movieID) throws SQLException{
        ResultSet set = getQueryResult("SELECT actors.first_name, actors.last_name,Roles.roleName FROM (Movies NATURAL JOIN (ACTORS NATURAL JOIN Roles)) WHERE movieID="+movieID);
        String res="";
        while(set.next()) {
            if (!(res.isEmpty())){
                res += "\n     ";
            }
            res += (set.getString(1) + " " + set.getString(2) + " as " + set.getString(3));
        }
        return res;
    }
    void addMovieToWatchlist(int userID, int movieID){
        try {
            PreparedStatement pstm  = con.prepareStatement("INSERT INTO Watchlist(userID,movieID) VALUES (?,?)");
            pstm.setInt(1,userID);
            pstm.setInt(2,movieID);
            pstm.executeUpdate();
            System.out.println("Query Executed Successfully! :>");
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
    void deleteMovieFromWatchlist(int userID, int movieID){
        try {
            PreparedStatement pstm  = con.prepareStatement("DELETE FROM Watchlist WHERE userID=? AND movieID=?");
            pstm.setInt(1,userID);
            pstm.setInt(2,movieID);
            pstm.executeUpdate();
            System.out.println("Query Executed Successfully! :>");
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
    boolean recordExists(int userID, int movieID) throws SQLException {
        ResultSet set = getQueryResult("SELECT * FROM WATCHLIST WHERE userID=" + userID + " AND movieID=" + movieID);
        return set.next();
    }
    ArrayList<String> getGenres() throws SQLException {
        ResultSet set = getQueryResult("SELECT genreName FROM GENRES");
        ArrayList<String> genres = new ArrayList<>();
        while (set.next()){
            genres.add(set.getString(1));
        }
        return genres;
    }
    ArrayList<Integer> getReleaseYearsOfMovies() throws SQLException{
        ResultSet set = getQueryResult("SELECT DISTINCT releaseYear FROM movies");
        ArrayList<Integer> arr = new ArrayList<>();
        while(set.next()){
            arr.add(set.getInt(1));
        }
        return arr;
    }

    int getWatchlistSize(int userID) throws SQLException {
        ResultSet set = getQueryResult("SELECT * FROM WATCHLIST WHERE userID = " + userID);
        int x = 0;
        while(set.next()) {
            x++;
        }
        return x;
    }
}