/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;

import it.unibo.msrehab.model.entities.Environment;
import it.unibo.msrehab.model.controller.PDDLEnvironmentController;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danger
 */
public class PDDLProblemBuilder {
    
    public static String PROBLEM_HEADER="(define (problem mattina-impegni)\n    "
            + "(:domain mattina)\n";
    
    PDDLMap m;
    List<PDDLGoal> goals=new ArrayList<PDDLGoal>();
    List<PDDLGoal> belief=new ArrayList<PDDLGoal>();
    
    int beGoal=0;
    
    int requestedGoal=3;
    int environmentId=1;
    int maxNumberOfTry=5;
    int numberOfTry=0;
    public PDDLProblemBuilder(){
        PDDLEnvironmentController pec=new PDDLEnvironmentController();
        Environment pe=pec.findEntity(environmentId).get();
        init(pe);        
    }
    public PDDLProblemBuilder(int requestedGoal,int environmentId){
        this.requestedGoal=requestedGoal;
        PDDLEnvironmentController pec=new PDDLEnvironmentController();
        Environment pe=pec.findEntity(environmentId).get();
        init(pe);
    }
    
    private void init(Environment pe){
        m=new PDDLMap(pe);
        
        PDDLGoalBuilder gb=new PDDLGoalBuilder(m);
        
        int i=0;
        while((i<requestedGoal)&&(numberOfTry<maxNumberOfTry)){
            PDDLGoal g=gb.buildGoal();      
            if((g!=null)&&(PDDLGoal.indexOfSimilarGoal(goals,g)==-1)){
                generateBelief();
                //System.out.println("Provo ad aggiungere il goal: "+g);
                goals.add(g);
                //System.out.println("Problem:\n"+getProblem());
                PDDLProblem p=new PDDLProblem(getProblemHead(),goals,belief,m.getJSONSchema().toString());
                //System.out.println("Ragiono se questo goal ha una soluzione!");
                if((g.isBeGoal()&&(beGoal>0))||(!p.hasSolution())){
                    System.out.println("Non ha soluzione!");
                    goals.remove(g);
                    numberOfTry++;
                }else{
                    i++;
                    if(g.isBeGoal()){
                        beGoal++;
                    }
                    numberOfTry=0;
                }
            }
        }        
        generateBelief();  //TODO BUGFIX      
        System.out.println(getProblem());
    }
    
    public int getNumberOfPlace(){
        return m.getPl().size();
    }
    
    public PDDLProblem build(){
        return new PDDLProblem(getProblemHead(),goals,belief,m.getJSONSchema().toString());
    }
    
    private void generateBelief(){
        belief=new ArrayList<PDDLGoal>();
        belief.add(new PDDLGoal("(first-number p1)"));
        for(int i=0;i<48;i++){
            belief.add(new PDDLGoal("(next p"+i+" p"+(i+1)+")"));
        }
        for(int i=0;i<=48;i++){
            if(i<m.getInitTime()){
               belief.add(new PDDLGoal("(past p"+i+")")); 
            }else{
                if(i==m.getInitTime()){
                    belief.add(new PDDLGoal("(now p"+i+")")); 
                }else{
                    belief.add(new PDDLGoal("(future p"+i+")")); 
                }
            }
        }
        
        belief.addAll(m.generateBelief());
    }
    
    private String getProblemHead(){
        String p=PROBLEM_HEADER;
        p+="    (:objects\n        ";
        for(int i=0;i<=48;i++){
            p+="p"+i+" ";
        }
        p+=" - Tempo\n";
        
        for(int i=0;i<m.getPl().size();i++){
            PDDLPlace place=m.getPl().get(i);
            p+="        "+place.getName()+" - "+place.getPlaceHead()+"\n";
        }
        
        p+="        macchina - vehicle\n";
        for(int i=0;i<m.getAddedItems().size();i++){
            PDDLItem item=m.getAddedItems().get(i);
            if(!item.getName().equals("macchina"))
                p+="        "+item.getName()+" - item\n";
        }
        return p;
    }
    private String getProblem(){
        String p=getProblemHead();
        
        p+="    )\n";
        p+="    (:init\n";
        p+="        (first-number p1)\n";
        for(int i=0;i<48;i++){
            p+="        (next p"+i+" p"+(i+1)+")\n";
        }
        p+="\n";
        for(int i=0;i<=48;i++){
            if(i<m.getInitTime()){
               p+="        (past p"+i+")\n"; 
            }else{
                if(i==m.getInitTime()){
                    p+="        (now p"+i+")\n"; 
                }else{
                    p+="        (future p"+i+")\n"; 
                }
            }
        }
        
        p+=m.getProblemInit();
        
        p+="    )\n";
        p+="    (:goal (and\n";
        for(int i=0;i<goals.size();i++){
            p+="            "+goals.get(i).toString()+"\n";
        }
        p+="        )\n";
        p+="    )\n";
        p+=")";
        return p;
    }
    
}
