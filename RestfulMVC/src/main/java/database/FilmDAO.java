package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Film;

import java.sql.*;

/**
 * This is the DAO that is used to interact with the database
 */
public class FilmDAO {
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
    
    //Define connection strings
    String user = "meliaale";
    String password = "Berglent3";
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;
    
	public FilmDAO() {}
	
	/**
	 * Opens a connection to the database
	 */
	private void openConnection(){
        try {
            // loading jdbc driver for mysql
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connecting to database
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC driver failed to load");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
    }
	
	/**
	 * Closes a connection to the database
	 */
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the next film in a ResultSet
	 * 
	 * @param rs variable of type ResultSet that stores results
	 */
	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	
	
	/**
	 * Gets all films from the database
	 */
	public ArrayList<Film> getAllFilms(){
	   
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
   }

	/**
	 * Gets a film from the database using its id
	 * 
	 * @param id Integer that represents the id
	 */
	public Film getFilmByID(int id){
	   
		openConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where id="+id;
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneFilm;
   }
   
	/**
	 * Gets a film/s from the database using its title
	 * 
	 * @param title String that represents the title
	 */
	public ArrayList<Film> getFilmByTitle(String input) {
	   
	   ArrayList<Film> allFilms = new ArrayList<Film>();
	   openConnection();
	   
	   try {
		   String selectSQL = "SELECT * from films where title LIKE '%"+input+"%'";
		   ResultSet rs1 = stmt.executeQuery(selectSQL);
		   //Retrieve results
		   while(rs1.next()) {
			   oneFilm = getNextFilm(rs1);
			   allFilms.add(oneFilm);
		   }
		   
		   stmt.close();
		   closeConnection();
	   } catch(SQLException sqle) {
		   System.out.println(sqle);
	   }
	   return allFilms;
   }
	
	/**
	 * Gets a film/s from the database using its stars
	 * 
	 * @param input String that represents the user input
	 */
	public ArrayList<Film> getFilmByStars(String input) {
	   
	   ArrayList<Film> allFilms = new ArrayList<Film>();
	   openConnection();
	   
	   try {
		   String selectSQL = "SELECT * from films where stars LIKE '%"+input+"%'";
		   ResultSet rs1 = stmt.executeQuery(selectSQL);
		   //Retrieve results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }
		    
		   stmt.close();
		   closeConnection();
	   } catch(SQLException sqle) {
		   System.out.println(sqle);
	   }
	return allFilms;
   }
   
	
	/**
	 * Gets a film/s from the database using its year
	 * 
	 * @param input Integer that represents the user input
	 */
	public ArrayList<Film> getFilmByYear(Integer input) {
	   
	   ArrayList<Film> allFilms = new ArrayList<Film>();
	   openConnection();
	   
	   try {
		   String selectSQL = "SELECT * from films where year ="+input;
		   ResultSet rs1 = stmt.executeQuery(selectSQL);
		   //Retrieve results
		   while(rs1.next()) {
			   oneFilm = getNextFilm(rs1);
			   allFilms.add(oneFilm);
		   }
		   stmt.close();
		   closeConnection();
	   } catch(SQLException sqle) {
		   System.out.println(sqle);
	   }
	   return allFilms;
   }
   
	/**
	 * Inserts a film into the database
	 * 
	 * @param f Variable of type Film that represents a film
	 */
	public boolean insertFilm(Film f) throws SQLException {
	   
	   boolean filmInserted = false;
	   
	   try {
		   openConnection();
		   String insertSQL = "insert into films (title, year, director, stars, review) values ('" + f.getTitle() + "','" + f.getYear() + "','" + f.getDirector() + "','" + f.getStars() + "','" + f.getReview() + "');";
		   stmt.executeUpdate(insertSQL);
		   stmt.close();
		   closeConnection();
		   
		   filmInserted = true;
	   }
	   catch (SQLException sqle) {
		   sqle.printStackTrace();
		   System.out.println("Film not inserted!");
	   }
	   
	   return filmInserted;
   }
   
	/**
	 * Updates a film in the database
	 * 
	 * @param f Variable of type Film that represents a film
	 */
   public boolean updateFilm(Film f) throws SQLException {
	   
	   boolean filmUpdated = false;
	   
	   try {
		   openConnection();
		   String updateSQL = "update films set title = '" + f.getTitle() + "', year = '" + f.getYear() + "', director = '" + f.getDirector() + "', stars = '" + f.getStars() + "', review = '" + f.getReview() + "' WHERE id = '" + f.getId() + "';";
		   stmt.executeUpdate(updateSQL);
		   stmt.close();
		   closeConnection();
		   
		   filmUpdated = true;
	   }
	   catch (SQLException sqle) {
		   sqle.printStackTrace();
	   }
	   
	   return filmUpdated;
	   
   }
   
	/**
	 * Deletes a film from the database
	 * 
	 * @param f Variable of type Film that represents a film
	 */
   public boolean deleteFilm(Film f) throws SQLException {
	   
	   boolean filmDeleted = false;
	   
	   try {
		   openConnection();
		   String deleteSQL = "delete from films where id = '" + f.getId() + "';";
		   stmt.executeUpdate(deleteSQL);
		   stmt.close();
		   closeConnection();
		   filmDeleted = true;
	   }
	   catch (SQLException sqle) {
		   sqle.printStackTrace();
	   }
	   
	   return filmDeleted;
   }
   
}
