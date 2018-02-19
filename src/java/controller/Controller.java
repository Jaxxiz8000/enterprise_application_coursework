/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import model.BookedLessons;
import model.Lesson;
import model.LessonTimetable;
import model.Users;
import model.LessonSelection;

/**
 *
 * @author bastinl
 */
public class Controller extends HttpServlet {

   private Users users;
   private LessonTimetable availableLessons;
   private BookedLessons bookedLessons;
   private Integer clientID;
   //private LessonSelection selectedLesson;

    public void init() {
         users = new Users();
         availableLessons = new LessonTimetable();
         //this.getServletContext().setAttribute("lessonTimetable", availableLessons);
        
    }
    
    public void destroy() {
        
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getPathInfo();
        
        //RequestDispatcher dispatcher = null;
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
        
        HttpSession session = request.getSession(false);
        
        if (action.equals("/login")) {
            String user = null;
            String pwd = null;

            if(request.getParameter("username") != null){
                user = request.getParameter("username");
            }

            if(request.getParameter("password") != null){
                pwd = request.getParameter("password");
            }

            clientID = users.isValid(user, pwd);
            
            if (clientID == -1) {
                dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
                //response.sendRedirect(request.getParameter("from"));
            }else {
                    session = request.getSession();
                    LessonSelection selectedLesson = new LessonSelection(clientID);
                    bookedLessons = new BookedLessons(clientID);
                    session.setAttribute("lessons", selectedLesson);
                    session.setAttribute("bookedLessons", bookedLessons);
                    session.setAttribute("clientID", clientID);
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
                }
            
        } else if (session.getAttribute("lessons") != null) {
            
                if (action.equals("/chooseLesson")) {
                            
                    Lesson lesson = this.availableLessons.getLesson(request.getParameter("lessonID"));
                    
                    //TODO check what is needed here when passing the selected lesson to chooseLesson
                    LessonSelection lessons = (LessonSelection) session.getAttribute("lessons");
                    BookedLessons bookedLessonsNew = (BookedLessons) session.getAttribute("bookedLessons");
//                    if (previousHistoryOfItem != null) {
//                        lesson.numOfLessons(lessonQuantity + previousHistoryOfItem.getNumOfLessons());
//                    } else {
//                        lesson.numOfLessons(lessonQuantity);
//                    }
                    bookedLessonsNew.setOwnerID(clientID);
                    lessons.addLesson(lesson);
                    session.setAttribute("lessons", lessons);
                    session.setAttribute("bookedLesson", bookedLessonsNew);
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
                    
                } else if (action.equals("/finaliseBooking")) {
                  LessonSelection lessons = (LessonSelection) session.getAttribute("lessons");
                  lessons.updateBooking();
                  
                    try {
                        bookedLessons.getBookedLessons();
                    } catch (SQLException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                  session.setAttribute("bookedLesson", bookedLessons);
                  
                  dispatcher = this.getServletContext().getRequestDispatcher("/LessonsBookedView.jspx");
                  
                } else if (action.equals("/viewSelectedLessons")) {
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
                } else if (action.equals("/lessonTimetableView")) {
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
                } else if (action.equals("/LessonsBookedView")) {
                    try {
                        bookedLessons.getBookedLessons();
                    } catch (SQLException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    session.setAttribute("bookedLesson", bookedLessons);
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonsBookedView.jspx");
                    
                } else if (action.equals("logout")) {
                    session.invalidate();
                }
        }
        
        dispatcher.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
