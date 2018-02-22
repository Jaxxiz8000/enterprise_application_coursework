/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author bastinl
 */
public class LessonSelection  {
    
    HashMap<String, Lesson> chosenLessons;
    private HashMap<String, Lesson> bookedLessons;
    private int ownerID;
    
    private DataSource ds = null;
    
    private ResultSet rs = null;
    private PreparedStatement st = null;
    
    public LessonSelection() {
        super();
        chosenLessons = new HashMap<String, Lesson>();
    }

    public LessonSelection(int owner) {
        
        chosenLessons = new HashMap<String, Lesson>();
        this.ownerID = owner;

        // You don't need to make any changes to the try/catch code below
        try {
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            ds = (DataSource)envCtx.lookup("jdbc/LessonDatabase");
        }
            catch(Exception e) {
            System.out.println("Exception message is " + e.getMessage());
        }
        
        // Connect to the database - this is a pooled connection, so you don't need to close it afterwards
        
    }

    /**
     * @return the items
     */
    public Set <Entry <String, Lesson>> getItems() {
        return chosenLessons.entrySet();
    }
    
    public void addLesson(Lesson l) {
       
        Lesson i = new Lesson(l);
        this.chosenLessons.put(l.getId(), i);
       
    }

    public Lesson getLesson(String id){
        return this.chosenLessons.get(id);
    }
    
    public int getNumChosen(){
        return this.chosenLessons.size();
    }

    public int getOwner() {
        return this.ownerID;
    }
    
    public boolean checkLessonExists(String lessonid) {
        
        Object[] lessonkeys = chosenLessons.keySet().toArray();
        for (int i=0; i<lessonkeys.length; i++) {
            if ((String)lessonkeys[i] == lessonid) {
                return true;
            }
        }
        return false;
    }
    
    public HashMap getLessons() {
        
        return chosenLessons;
        
    }
    
    
    
    public void updateBooking() {
        
        // Connect to the database - this is a pooled connection, so you don't need to close it afterwards
        try {

            Connection connection = ds.getConnection();

            if (connection != null) {

                String deleteQuery = ("DELETE FROM lessons_booked WHERE clientid = "+ownerID+"");
                st = connection.prepareStatement(deleteQuery);
                //st.setInt(1, ownerID);
                st.execute();
                
                Object[] lessonKeys = chosenLessons.keySet().toArray();
                for (int i=0; i<lessonKeys.length; i++) {
                
                String lessonId = (String) lessonKeys[i];
                // Temporary check to see what the current lesson ID is....
                //system.out.println("Lesson ID is : " + (String)lessonKeys[i]);
                                   
                String insertQuery = ("INSERT INTO `lessons_booked`(`clientid`, `lessonid`) VALUES ("+ownerID+", '"+lessonId+"')");
                st = connection.prepareStatement(insertQuery);
                //st.setInt(1, ownerID);
                //st.setString(2, lessonId);
                int successfulUpdateCheck = st.executeUpdate(insertQuery);
                if(successfulUpdateCheck > 0) {            
                    chosenLessons.clear();
                    } else {
                        System.out.println("failed to update booking table");
                    }
                }
    }
        
        
        }catch(SQLException e){

            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        }
                
    }
    
    public int lessonExists(String lessonId) {
        try {
            Connection connection = ds.getConnection();
            String query = "SELECT count(*) AS count from lessons_booked WHERE clientid = "+ownerID+" AND lessonid = "+lessonId+"  ";
            st = connection.prepareStatement(query);
            st.setInt(1, ownerID);
            st.setString(2, lessonId);
            rs = st.executeQuery(query);

            return rs.getInt("count");
 
        }catch(SQLException e){

            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        }
        return 0;
    }
    
    public HashMap getBookedLessons() {        
        return bookedLessons;  
    }
    
//  public void updateBooking() {
//        
//        // Connect to the database - this is a pooled connection, so you don't need to close it afterwards
//        try {
//
//            Connection connection = ds.getConnection();
//
//            if (connection != null) {
//                Object[] lessonKeys = chosenLessons.keySet().toArray();
//                for (int i=0; i<lessonKeys.length; i++) {
//                
//                String lessonId = (String) lessonKeys[i];
//                // Temporary check to see what the current lesson ID is....
//                //system.out.println("Lesson ID is : " + (String)lessonKeys[i]);
//                
//                if (this.lessonExists(lessonId) == 0) {
//                    
//                    String insertQuery = ("INSERT INTO `lessons_booked`(`clientid`, `lessonid`) VALUES ("+ownerID+", '"+lessonId+"')");
//                    st = connection.prepareStatement(insertQuery);
//                    //st.setInt(1, ownerID);
//                    //st.setString(2, lessonId);
//                    int successfulUpdateCheck = st.executeUpdate(insertQuery);
//                    if(successfulUpdateCheck > 0) {            
//                        chosenLessons.clear();
//                    } else {
//                        System.out.println("failed to update booking table");
//                    }
//                }
//            }
//        }
//        
//        
//        }catch(SQLException e){
//
//            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
//        }
//                
//    }
    
}
