/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


/**
 *
 * @author bastinl
 */
public class BookedLessons {

  private Connection connection=null;
  
  private ResultSet rs = null;
  private Statement st = null;
  private HashMap bookedLessons = null;
  private int ownerID;
  
  private DataSource ds = null;
    
    public BookedLessons() {
        super();
        bookedLessons = new HashMap<String, Lesson>();
        
        
             
    }
    
    public BookedLessons(int clientID) {
        this.ownerID = clientID;
        
        bookedLessons = new HashMap<String, Lesson>();
        // You don't need to make any changes to the try/catch code below
        try {
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            ds = (DataSource)envCtx.lookup("jdbc/LessonDatabase");
        }
            catch(NamingException e) {
            System.out.println("Exception message is " + e.getMessage());
        }
        
      try {
          //        try {
//
//            connection = ds.getConnection();
//
//            if (connection != null) {
//
//                String queryString = ("SELECT lessons.* FROM lessons, lessons_booked WHERE lessons.lessonid = lessons_booked.lessonid AND lessons_booked.clientid = "+ownerID+"");
//                st = connection.prepareStatement(queryString);
//                //st.setInt(1, ownerID);
//                rs = st.executeQuery(queryString);
//                while(rs.next()) {
//
//                    String description=rs.getString("description");
//                    Timestamp startDateTime=rs.getTimestamp("startDateTime");
//                    Timestamp endDateTime=rs.getTimestamp("endDateTime");
//                    Integer lessonLevel=rs.getInt("level");
//                    String lessonID=rs.getString("lessonid");
//
////                    Lesson selectedLesson =
////                            new Lesson(description, startDateTime, 
////                                    endDateTime, lessonLevel, lessonID);
//
////                    bookedLessons.put(lessonID, new model.Lesson(description, startDateTime, 
////                                    endDateTime, lessonLevel, lessonID));
//                    bookedLessons.put(lessonID, new model.Lesson(description, startDateTime, endDateTime, lessonLevel, lessonID));
//                    
//                }
//
//            }
//        }catch(SQLException e){
//
//            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
//        }
      this.getBookedLessons();
      } catch (SQLException ex) {
          Logger.getLogger(BookedLessons.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
   
    /**
     * @return the items
     */
    
    
    public Lesson getLesson(String itemID) {
        
        return (Lesson)this.bookedLessons.get(itemID);
    }

    public HashMap getLessons() {
        
        return this.bookedLessons;
        
    }
    
    public int setOwnerID(int ownerId) {
        return ownerID = ownerId;
    }
    
    public int getNumChosen(){
        return this.bookedLessons.size();
    }
    
    public void getBookedLessons() throws SQLException {
        
        try {

            connection = ds.getConnection();

            if (connection != null) {

                String queryString = ("SELECT lessons.* FROM lessons, lessons_booked WHERE lessons.lessonid = lessons_booked.lessonid AND lessons_booked.clientid = "+ownerID+"");
                st = connection.prepareStatement(queryString);
                if (bookedLessons.isEmpty()) {
                    
                    rs = st.executeQuery(queryString);
                    while(rs.next()) {

                        String description=rs.getString("description");
                        Timestamp startDateTime=rs.getTimestamp("startDateTime");
                        Timestamp endDateTime=rs.getTimestamp("endDateTime");
                        Integer lessonLevel=rs.getInt("level");
                        String lessonID=rs.getString("lessonid");

                        bookedLessons.put(lessonID, new model.Lesson(description, startDateTime, endDateTime, lessonLevel, lessonID));

                    }
                } else {
                    bookedLessons.clear();
                    
                    rs = st.executeQuery(queryString);
                    while(rs.next()) {

                        String description=rs.getString("description");
                        Timestamp startDateTime=rs.getTimestamp("startDateTime");
                        Timestamp endDateTime=rs.getTimestamp("endDateTime");
                        Integer lessonLevel=rs.getInt("level");
                        String lessonID=rs.getString("lessonid");

                        bookedLessons.put(lessonID, new model.Lesson(description, startDateTime, endDateTime, lessonLevel, lessonID));
                    }

                }   
            }    
        
        } catch(SQLException e){

            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        } 
    }
        
}
