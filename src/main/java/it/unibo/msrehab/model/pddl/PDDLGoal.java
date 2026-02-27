/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;

import it.unibo.msrehab.model.entities.Environment;

import java.util.List;

/**
 *
 * @author danger
 */
public class PDDLGoal {

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
    
    private String goal;
    public PDDLGoal(String goal){
        this.goal=goal;
    }
    
    public String toString(){
        return goal;
    }
    
    public String getHead(){
        return getBinds()[0];
    }
    
    
    @Override
    public boolean equals(Object g){
        return (g instanceof PDDLGoal) && goal.equals(((PDDLGoal)g).getGoal());
    }
    @Override
    public int hashCode(){
        return goal.hashCode();
    }
    public void negate(){
        String a=goal.substring(0, 5);
        if(a.equals("(not ")){
            goal=goal.substring(5,goal.length()-1);
        }else{
            goal = "(not "+goal+")";
        }        
    }
    
    public PDDLGoal getNegate(){
        PDDLGoal g=new PDDLGoal(goal);
        g.negate();
        return g;
    }
    
    public boolean isNegate(){
        String a=goal.substring(0, 5);
         return a.equals("(not ");
    }
    
    public boolean isBeGoal(){
        String a=goal.substring(0, 4);
        if(a.equals("(be ")){
            return true;
        }else
            return false;
    }
    
    public String[] getBinds(){
        String tg=goal;
        tg=tg.substring(1,tg.length()-1);
        return tg.split(" ");
    }
    
    public void binds(String key,String value){
        goal=goal.replace(key+" ", value+" ");
        goal=goal.replace(key+")", value+")");
    }
    
    public PDDLGoal copyBind(String key,String value){
        PDDLGoal q=new PDDLGoal(goal);
        q.binds(key, value);
        return q;
    }
    
    public String getNaturalLanguage(Environment e){
        return e.getNaturalLanguageGoal(this);
    }
    
    public int calculateSimilarity(PDDLGoal b){
        int sim=0;
        String a_bind[]=getBinds();
        String b_bind[]=b.getBinds();
        String a_head=a_bind[0].split("-")[0];
        String b_head=b_bind[0].split("-")[0];
        if(a_head.contains(b_head)||b_head.contains(a_head)){
            sim++;
            for(int i=1;i<Math.min(a_bind.length,b_bind.length);i++){
                if(a_bind[i].equals(b_bind[i])){
                    sim++;
                }else{
                    sim--;
                }
            }
            //sim+=Math.max(a_bind.length,b_bind.length)-Math.min(a_bind.length,b_bind.length);
        }
        if(sim<0)
            sim=0;
        return sim;
    }
    
    public static int indexOfSimilarGoal(List<PDDLGoal> list,PDDLGoal b){
        int idx=-1;
        for(int i=0;i<list.size();i++){
            System.out.println("mostro "+ i + list.get(i).toString());
             System.out.println("confronto con "+b.toString());
            if(list.get(i).calculateSimilarity(b)>0){
                return i;
            }
        }
        return idx;
    }
}
