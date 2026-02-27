/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.joda.time.LocalDateTime;

/**
 *
 * @author danger
 */
public class IntervalTime
{
    public long start;
    public long end;
    
    public IntervalTime(String group,String tstart,String tend){
        if(group.equals("day")&&(tstart.equals(""))){
            end = LocalDateTime.now().toDateTime().getMillis();
            Calendar clndr = Calendar.getInstance();
            clndr.add(Calendar.DATE, -7);
            start = LocalDateTime.fromCalendarFields(clndr).toDateTime().getMillis();
        }else{
            end = LocalDateTime.now().toDateTime().getMillis();
            Calendar clndr = Calendar.getInstance();
            clndr.add(Calendar.DATE, -7);
            start = LocalDateTime.fromCalendarFields(clndr).toDateTime().getMillis();
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.ITALIAN);
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(tstart));
                start = LocalDateTime.fromCalendarFields(cal).toDateTime().getMillis();
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(sdf.parse(tend));
                cal2.add(Calendar.DATE,1);
                end = LocalDateTime.fromCalendarFields(cal2).toDateTime().getMillis();
            }catch(ParseException p){
            }
        }
    }
    
    public long getStart(){
        return start;
    }

    public long getEnd(){
        return end;
    }
    
    public static int daysfromgroup(String group){
        int group_n=86400;
        if(group.equals("week")){
            group_n=86400*7;
        }
        if(group.equals("month")){
            group_n= 2628000;
        }
        return group_n;
    }
}
