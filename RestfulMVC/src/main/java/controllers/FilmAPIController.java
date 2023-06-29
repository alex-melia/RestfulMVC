package controllers;

import java.io.IOException;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.FilmDAO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import models.Film;
import models.FilmList;

/**
 * This is the API Servlet for the RESTful web service
 * Requests are sent to the servlet to one of its 4 methods depending on the header of the request
 */
@WebServlet("/films-api")
public class FilmAPIController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Format types
	String typeJson = "application/json";
	String typeXml = "application/xml";
	String typeText = "text/plain";
	
	FilmDAO dao = new FilmDAO();
    
	/**
	 * The doGet method is initialized by a GET request and interacts with the database before sending back a response
	 * 
	 * @param request holds the request sent by the client
	 * @param response holds the response issed by the servlet
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String format = request.getHeader("Accept");
		String query = request.getParameter("query");
		String searchType = request.getParameter("searchType");
		ArrayList<Film> allFilms = dao.getAllFilms();
		
		/**
		 * The value of the header is checked with an if statement to determine which block of code needs to be ran
		 */
		if (format.equals(typeXml)) {
			
			//If there is no query
			if (query == null) {
				
				/**
				 * An instance of JAXB library is implemented which is used to parse the films into XML
				 * If there is an error while parsing, the stack trace will be printed to the console
				 */
				try {
					PrintWriter out = response.getWriter();
					response.setContentType("application/xml");
					FilmList fl = new FilmList(allFilms);
					StringWriter sw = new StringWriter();
					
					JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
					Marshaller marshaller = jaxbContext.createMarshaller();
					
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					marshaller.marshal(fl, sw);
					
					out.write(sw.toString());
					out.close();
					
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			
			}
			
			//If there is a query
			else {
				
				if (searchType.equals("title")) {
					
					try {
						PrintWriter out = response.getWriter();
						ArrayList<Film> filmList = dao.getFilmByTitle(query);
						FilmList fl = new FilmList(filmList);
						StringWriter sw = new StringWriter();
						
						JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
						Marshaller marshaller = jaxbContext.createMarshaller();
						
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
						marshaller.marshal(fl, sw);
						
						out.write(sw.toString());
						out.close();
						
					} catch (JAXBException e) {
						e.printStackTrace();
					}
				}
				
				else if (searchType.equals("year")) {
					
					try {
						PrintWriter out = response.getWriter();	
						Integer queryToInt = Integer.parseInt(query);
						ArrayList<Film> filmList = dao.getFilmByYear(queryToInt);
						FilmList fl = new FilmList(filmList);
						StringWriter sw = new StringWriter();
						
						JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
						Marshaller marshaller = jaxbContext.createMarshaller();
						
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
						marshaller.marshal(fl, sw);
						
						out.write(sw.toString());
						out.close();
						
					} catch (JAXBException e) {
						e.printStackTrace();
					}
				}
				
				else if (searchType.equals("actor")) {
					
					try {
						PrintWriter out = response.getWriter();
						ArrayList<Film> filmList = dao.getFilmByStars(query);
						FilmList fl = new FilmList(filmList);
						StringWriter sw = new StringWriter();
						
						JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
						Marshaller marshaller = jaxbContext.createMarshaller();
						
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
						marshaller.marshal(fl, sw);
						
						out.write(sw.toString());
						out.close();
						
					} catch (JAXBException e) {
						e.printStackTrace();
					}
					
				}
							
			}
			
		}
		
		if (format.equals(typeJson)) {
			
			if (query == null) {
			
				Gson gson = new Gson();
				
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				String json = gson.toJson(allFilms);
				out.write(json);
				out.close();
			}
			
			else {
				
				if (searchType.equals("title")) {
					
					Gson gson = new Gson();
					PrintWriter out = response.getWriter();
					
					ArrayList<Film> filmList = dao.getFilmByTitle(query);
					String json = gson.toJson(filmList);
					out.write(json);
					out.close();
					
				}
				
				else if (searchType.equals("year")) {
					
					Gson gson = new Gson();
					PrintWriter out = response.getWriter();
					
					Integer queryToInt = Integer.parseInt(query);
					ArrayList<Film> filmList = dao.getFilmByYear(queryToInt);
					String json = gson.toJson(filmList);
					out.write(json);
					out.close();
					
				}
				
				else if (searchType.equals("actor")) {
					
					Gson gson = new Gson();
					PrintWriter out = response.getWriter();
					
					ArrayList<Film> filmList = dao.getFilmByStars(query);
					String json = gson.toJson(filmList);
					out.write(json);
					out.close();
					
				}
			}
			
		}
		
		if (format.equals(typeText)) {
			
			if (query == null) {
				
				PrintWriter out = response.getWriter();
				response.setContentType("text/plain");
				
				String result = "";
				
				for(Film f : allFilms) {
					result += "#" + f.getId() + "|" + f.getTitle() + "|" + f.getYear() + "|" + f.getDirector() + "|" + f.getStars() + "|" + f.getReview();
				}
				
				out.write(result);
				out.close();
				
			}
			
			else {
				
				if(searchType.equals("title")) {
					
					PrintWriter out = response.getWriter();			
					String result = "";
					ArrayList<Film> filmList = dao.getFilmByTitle(query);
					
					for(Film f : filmList) {
						result += "#" + f.getId() + "|" + f.getTitle() + "|" + f.getYear() + "|" + f.getDirector() + "|" + f.getStars() + "|" + f.getReview();
					}
					
					out.write(result);
					out.close();
					
				}
				
				else if (searchType.equals("year")) {
					
					PrintWriter out = response.getWriter();			
					String result = "";
					Integer queryToInt = Integer.parseInt(query);
					ArrayList<Film> filmList = dao.getFilmByYear(queryToInt);
					
					for(Film f : filmList) {
						result += "#" + f.getId() + "|" + f.getTitle() + "|" + f.getYear() + "|" + f.getDirector() + "|" + f.getStars() + "|" + f.getReview();
					}
					
					out.write(result);
					out.close();
					
				}
				
				else if (searchType.equals("actor")) {
					
					PrintWriter out = response.getWriter();			
					String result = "";
					ArrayList<Film> filmList = dao.getFilmByStars(query);
					
					for(Film f : filmList) {
						result += "#" + f.getId() + "|" + f.getTitle() + "|" + f.getYear() + "|" + f.getDirector() + "|" + f.getStars() + "|" + f.getReview();
					}
					
					out.write(result);
					out.close();
					
				}
			}	
		}
	}

	/**
	 * The doPost method is initialized by a POST request and interacts with the database before sending back a response
	 * 
	 * @param request holds the request sent by the client
	 * @param response holds the response issed by the servlet
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Data format sent in request header
		String dataFormat = request.getHeader("Accept");

		if (dataFormat.equals(typeXml)) {
					
				try {
					String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
					JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					Film f = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
					
					f.setTitle(f.getTitle());
					f.setYear(f.getYear());
					f.setDirector(f.getDirector());
					f.setStars(f.getStars());
					f.setReview(f.getReview());
					
					try {
						dao.insertFilm(f);
						response.setContentType("text/plain");
						PrintWriter out = response.getWriter();
						out.println("FILM INSERTED: XML");
						System.out.println("Film inserted: XML");
					}
					
					catch (SQLException sqle) {
						sqle.printStackTrace();
					}
				} catch (JAXBException e) {
					e.printStackTrace();
				}
				
					
		}
		
		if (dataFormat.equals(typeJson)) {
			
			String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
			Gson gson = new Gson();
			Film f = gson.fromJson(data, Film.class);
			
			f.setTitle(f.getTitle());
			f.setYear(f.getYear());
			f.setDirector(f.getDirector());
			f.setStars(f.getStars());
			f.setReview(f.getReview());
			
			try {
				dao.insertFilm(f);
				response.setContentType("text/plain");
				PrintWriter out = response.getWriter();
				out.println("FILM INSERTED: JSON");
				System.out.println("Film inserted: JSON");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		if (dataFormat.equals(typeText)) {
			
			String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
			Film f = new Film();
			
			String[] parameters = data.split("\\|");
			
			String[] title = parameters[0].split("\\#");
					
			f.setTitle(title[1]);
			f.setYear(Integer.parseInt(parameters[1]));
			f.setDirector(parameters[2]);
			f.setStars(parameters[3]);
			f.setReview(parameters[4]);
			
			try {
				dao.insertFilm(f);
				response.setContentType("text/plain");
				PrintWriter out = response.getWriter();
				out.println("FILM INSERTED: TEXT");
				System.out.println("Film inserted: Text");
			}
			
			catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * The doPut method is initialized by a PUT request and interacts with the database before sending back a response
	 * It is used to UPDATE the films in the database
	 * 
	 * @param request holds the request sent by the client
	 * @param response holds the response issed by the servlet
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String dataFormat = request.getHeader("Accept");
		
		if (dataFormat.equals(typeJson)) {
			
			String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
			Gson gson = new Gson();
			Film f = gson.fromJson(data, Film.class);
			
			f.setId(f.getId());
			f.setTitle(f.getTitle());
			f.setYear(f.getYear());
			f.setDirector(f.getDirector());
			f.setStars(f.getStars());
			f.setReview(f.getReview());
			
			try {
				dao.updateFilm(f);
				response.setContentType("text/plain");
				PrintWriter out = response.getWriter();
				out.println("FILM UPDATED: JSON");
				System.out.println("Film updated: JSON");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		if (dataFormat.equals(typeXml)) {
		
			try {
				String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);	
				JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film f = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));

				f.setId(f.getId());
				f.setTitle(f.getTitle());
				f.setYear(f.getYear());
				f.setDirector(f.getDirector());
				f.setStars(f.getStars());
				f.setReview(f.getReview());
				
				try {
					dao.updateFilm(f);
					response.setContentType("text/plain");
					PrintWriter out = response.getWriter();
					out.println("FILM UPDATED: XML");
					System.out.println("Film updated: XML");
				}
				
				catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}
			
			catch (JAXBException e) {
				e.printStackTrace();
			}		
		}
		
		if (dataFormat.equals(typeText)) {
			
			String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
			Film f = new Film();
						
			String[] parameters = data.split("\\|");
			String[] id = parameters[0].split("\\#");
			
			f.setId(Integer.parseInt(id[1]));
			f.setTitle(parameters[1]);
			f.setYear(Integer.parseInt(parameters[2]));
			f.setDirector(parameters[3]);
			f.setStars(parameters[4]);
			f.setReview(parameters[5]);
			
			try {
				dao.updateFilm(f);
				response.setContentType("text/plain");
				PrintWriter out = response.getWriter();
				out.println("FILM UPDATED: TEXT");
				System.out.println("Film updated: Text");
			}
			
			catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
	
	/**
	 * The doDelete method is initialized by a DELETE request and interacts with the database before sending back a response
	 * It is used to DELETE the films in the database
	 * 
	 * @param request holds the request sent by the client
	 * @param response holds the response issed by the servlet
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String dataFormat = request.getHeader("Accept");
		
		if (dataFormat.equals(typeJson)) {
			
			String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
			Gson gson = new Gson();
			Film f = gson.fromJson(data, Film.class);
			
			f.setId(f.getId());
			
			try {
				dao.deleteFilm(f);
				response.setContentType("text/plain");
				PrintWriter out = response.getWriter();
				out.println("FILM DELETED: JSON");
				System.out.println("Film deleted: JSON");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		if (dataFormat.equals(typeXml)) {
			
			String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
			
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film f = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));

				f.setId(f.getId());
				
				try {
					dao.deleteFilm(f);
					response.setContentType("text/plain");
					PrintWriter out = response.getWriter();
					out.println("FILM DELETED: XML");
					System.out.println("Film deleted: XML");
				}
				
				catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
					
		}
		
		if (dataFormat.equals(typeText)) {
			
			String data = request.getReader().lines().reduce("",(accumulator, actual) -> accumulator + actual);
			
			Film f = new Film();
			f.setId(Integer.parseInt(data));
			
			try {
				dao.deleteFilm(f);
				response.setContentType("text/plain");
				PrintWriter out = response.getWriter();
				out.println("FILM DELETED: TEXT");
				System.out.println("Film deleted: Text");
			}
			
			catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			
		}
		
	}
}
