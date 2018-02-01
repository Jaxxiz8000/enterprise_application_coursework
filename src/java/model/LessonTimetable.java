/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 *
 * @author bastinl
 */
public class LessonTimetable {

  private Connection connection=null;
  
  private ResultSet rs = null;
  private Statement st = null;
  private HashMap lessons = null;
  
  private DataSource ds = null;
    
    public LessonTimetable() {

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
            // Connect to the database - you can use this connection to 
            // create and prepare statements, and you don't need to worry about closing it
            Connection connection = ds.getConnection();
        
             try {

                if (connection != null) {
                    
                    // TODO instantiate and populate the 'lessons' HashMap by selecting the relevant infromation from the database
                    String findLessons = ("SELECT * FROM lessons");
                    st = connection.prepareStatement(findLessons);
                    rs = st.executeQuery(findLessons);
                    lessons = new HashMap<String, Lesson>();
                    while(rs.next()) {
                        
                    
                    String description=rs.getString("description");
                    Timestamp startDateTime=rs.getTimestamp("startDateTime");
                    Timestamp endDateTime=rs.getTimestamp("endDateTime");
                    Integer lessonLevel=rs.getInt("level");
                    String lessonID=rs.getString("lessonid");
                    
                    lessons.put(lessonID, new model.Lesson(description, startDateTime, endDateTime, lessonLevel, lessonID));
                    }
                    // TODO add code here to retrieve the infromation and create the new Lesson objects
                }

            }catch(SQLException e) {

                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        
            }
        
        
          }catch(SQLException e){

             System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
          }
      
    }
    
   
    /**
     * @return the items
     */
    public Lesson getLesson(String itemID) {
        
        return (Lesson)this.lessons.get(itemID);
    }

    public HashMap getLessons() {
        
        return this.lessons;
        
    }    
}
