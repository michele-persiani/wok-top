/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.entities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import flexjson.JSON;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import it.unibo.msrehab.model.pddl.PDDLGoal;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author danger
 */

@Entity
@Table(name = "environment")
@NamedQueries({
                @NamedQuery(name = "Environment.findAll", query = "SELECT l FROM Environment l")
             })

@XmlRootElement
public class Environment extends BaseEntity
{
    @JSON(include = false)
    private static final long serialVersionUID = 1L;

    @Column(name = "environment", table = "environment")
    @Basic
    private String environment;

    public Environment()
    {

    }

    public Environment(String filename)
    {
        loadFromFile(filename);
    }

    public boolean loadFromFile(String filename)
    {

        try(Scanner scanner = new Scanner(new File(filename)))
        {
            String source = scanner.useDelimiter("\\A").next();
            scanner.close();
            environment = source;
            return true;
        }
        catch(FileNotFoundException ignored)
        {
            return false;
        }
    }


    public String getEnvironment()
    {
        return environment;
    }


    public void setEnvironment(String environment)
    {
        this.environment = environment;
    }




    public String getNaturalLanguageGoal(PDDLGoal g)
    {
        String q="";
        JSONObject j=new JSONObject(environment);
        JSONArray goals=j.getJSONArray("goals");
        String ghead=g.getBinds()[0];
        for(int i=0;i<goals.length();i++){
            JSONObject gg = goals.getJSONObject(i);
            if(ghead.equals(gg.get("name"))){
              //  System.out.println("Trovato binding nlp");
                String[] bind = g.getBinds();
                q=gg.getString("description");
                for(int k=0;k<bind.length;k++){
                    if(bind[k].matches("p[0-9]+")){
                        int h=Integer.parseInt(bind[k].split("p")[1]);
                        int m=h%2*30;
                        h=h/2;                        
                        bind[k]=""+h+":"+twoDigit(m);
                    }else{
                        JSONArray places = j.getJSONArray("places");
                        JSONArray items = j.getJSONArray("items");
                        for(int l=0;l<items.length();l++){
                           // System.out.println(bind[k]+" =?= "+(((JSONObject)items.get(l)).getString("name")));
                            if(bind[k].equals(((JSONObject)items.get(l)).getString("name"))){
                                String article=((JSONObject)items.get(l)).getString("article");
                                q=q.replace("[article]", article);
                            }
                        }
                        for(int l=0;l<places.length();l++){
                          //  System.out.println(bind[k]+" =?= "+(((JSONObject)places.get(l)).getString("name")));
                            if(bind[k].equals(((JSONObject)places.get(l)).getString("name"))){
                                String preposition=((JSONObject)places.get(l)).getString("preposition");
                                q=q.replace("[preposition]", preposition);
                            }
                        }
                    }                    
                    q=q.replace("$"+k, "<b>"+bind[k]+"</b>");
                }
                System.out.println(q);
                return q+"<br />\n";
            }
        }
        return "";
    }

    private String twoDigit(int x)
    {
        String xx=""+x;
        if(xx.length()<2){
            xx="0"+xx;
        }
        return xx;
    }
}
