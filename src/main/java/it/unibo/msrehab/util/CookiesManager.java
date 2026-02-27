package it.unibo.msrehab.util;

import it.unibo.msrehab.model.entities.MSRUser;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookiesManager {

    public static boolean isUserLogged(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies==null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if ("id".equals(cookie.getName())) {
                return true;
            }
        }
        return false;

    }

    public static String geLoggedUserName(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies==null) {
            return null;
        }
        
        for (Cookie cookie : cookies) {
            if ("name".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;

    }
    
    public static Integer geLoggedUserId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies==null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("id".equals(cookie.getName())) {
                return Integer.parseInt(cookie.getValue());
            }
        }
        return null;

    }

    public static Integer getLoggedUserCenter(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies==null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("cid".equals(cookie.getName())) {
                return Integer.parseInt(cookie.getValue());
            }
        }
        return null;

    }

    static boolean isUserAdmin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies==null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if("role".equals(cookie.getName()) &&
                    MSRUser.RoleValue.ADMIN.toString().equals(cookie.getValue())) {
                return true;
            }
        }
        return false;
    }
    
    static boolean isUserPatient(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies==null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if("role".equals(cookie.getName()) &&
                    MSRUser.RoleValue.PATIENT.toString().equals(cookie.getValue())) {
                return true;
            }
        }
        return false;
    }
    
    
}
