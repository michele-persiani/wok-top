package it.unibo.msrehab.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebPagesUtilities {

    /**
     * this method take the request and the response and check is the user has
     * logged in if it has it redirects the to the login page with an error
     * message
     *
     * @param request
     * @return
     * @returns true if it has been logged, false it isn't logged
     */
    public static boolean redirectIfNotLogged(HttpServletRequest request) {
        boolean logged = CookiesManager.isUserLogged(request);
        if (!logged) {
            String message = "Sorry, you have to login first!";
            request.setAttribute("errorLoginMessage", "<p id=\"errorForLogin\">" + message + "</p>");
            return true;
        }
        return false;

    }    
    
//    public static void redirectToErrorSite(HttpServletResponse response, HttpServletRequest request)
//            throws ServletException, IOException {
//        RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/fatal-error.jsp");
//        rd.forward(request, response);
//    }

    public static boolean redirectIfNotAdmin(HttpServletRequest request) {
        boolean admin = CookiesManager.isUserAdmin(request);
        if (!admin) {
            String message = "Sorry, you are not an admin!";
            request.setAttribute("errorLoginMessage", "<p id=\"errorForLogin\">" + message + "</p>");
            return true;
        }
        return false;
    }
    
    public static boolean redirectIfNotPatient(HttpServletRequest request) {
        boolean patient = CookiesManager.isUserPatient(request);
        if (!patient) {
            String message = "Sorry, you are not a patient!";
            request.setAttribute("errorLoginMessage", "<p id=\"errorForLogin\">" + message + "</p>");
            return true;
        }
        return false;
    }
    
}
