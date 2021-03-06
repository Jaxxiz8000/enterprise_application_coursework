/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.PreparedStatement;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JOptionPane;

/**
 *
 * @author bastinl
 */
public class Users {
  
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;
    DataSource ds = null;
    private int clientID;
   
    public Users() {
        
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
        
    }

    public int isValid(String name, String pwd) {
       
        try {
            
            Connection connection = ds.getConnection();

            if (connection != null) {

               //TODO: implement this method so that if the user does not exist, it returns -1.
               // If the username and password are correct, it should return the 'clientID' value from the database.
               String queryString = "SELECT clientid, username, password FROM clients where username=? and password=?";
               pstmt = connection.prepareStatement(queryString);
               pstmt.setString(1, name);
               pstmt.setString(2, pwd);
               rs = pstmt.executeQuery();
               
               if (rs.next()) {
                   clientID = rs.getInt("clientid");
                   return clientID;
               }else {
                   return -1;
               }
  

            }
            else {
                return -1;
            }
        } catch(SQLException e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return -1;
        }
        
        
    }
    
    // TODO (Optional steps 3 and 4) add a user with specified username and password
    public boolean addUser(String name, String pwd) {
       
        //TODO: implement this method so that the specified username and password are inserted into the database.

         try {
            
            Connection connection = ds.getConnection();

            if (connection != null) {
                
                pstmt = connection.prepareStatement("INSERT INTO clients ( username, password) VALUES (?,?)");
                pstmt.setString(1, name);
                pstmt.setString(2, pwd);
                
                int i = pstmt.executeUpdate();
                
                if (i > 0) {
                    return true;
                } else {
                    return false;
                }
                
                // todo check success
            
            } else {
                return false;
            }
            
         }
            catch(SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
                return false;
         }
        
    }
    
    public int returnClientID() {
        return clientID;
    }
}
