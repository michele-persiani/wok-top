package it.unibo.msrehab.util;

import it.unibo.msrehab.model.entities.MSRUser;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static it.unibo.msrehab.model.entities.MSRUser.RoleValue.DEMO;
import static it.unibo.msrehab.model.entities.MSRUser.RoleValue.PATIENT;

public class CookiesManager
{
    public static boolean isUserLogged(HttpServletRequest request)
    {
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

    public static String geLoggedUserName(HttpServletRequest request)
    {
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
    
    public static Integer geLoggedUserId(HttpServletRequest request)
    {
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

    public static Integer getLoggedUserCenter(HttpServletRequest request)
    {
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

    public static boolean isUserAdmin(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        if (cookies==null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if("role".equals(cookie.getName()) &&
                    MSRUser.RoleValue.ADMIN.toString().equals(cookie.getValue()))
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isUserPatientOrDemo(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        if (cookies==null)
            return false;

        for (Cookie cookie : cookies)
        {
            boolean match = "role".equals(cookie.getName()) &&
                    (PATIENT.toString().equals(cookie.getValue()) || DEMO.toString().equals(cookie.getValue()));
            if(match)
                return true;

        }
        return false;
    }


}
