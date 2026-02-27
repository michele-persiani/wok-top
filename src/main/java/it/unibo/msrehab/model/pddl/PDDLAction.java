/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danger
 */
public class PDDLAction {
    
    private String head;
    private String bindings[];
    private List<PDDLGoal> preconditions=new ArrayList<PDDLGoal>();
    private List<PDDLGoal> effects=new ArrayList<PDDLGoal>();
    public PDDLAction(String action){
        String row[]=action.split("\n");
        head=row[0].replaceAll(" ", "");
        
        String b=row[1].substring(row[1].indexOf("(")+1, row[1].lastIndexOf(")"));
        //System.out.println("*"+b+"*");
        bindings=b.split(" - [a-zA-Z]* | - [a-zA-Z]*$");
        for(int j=1;j<bindings.length;j++){
            bindings[j]=bindings[j];
        }
        int i=3;
        while((!row[i].contains(":effect"))&&(i<row.length)){
            if(!row[i].replaceAll(" ", "").equals(""))
                preconditions.add(new PDDLGoal(rOS(row[i])));
            i++;
        }
        preconditions.remove(preconditions.size()-1);
        i++;
        while((!row[i].contains(" )"))&&(i<row.length)){
            if(!row[i].replaceAll(" ", "").equals(""))
                effects.add(new PDDLGoal(rOS(row[i])));
            i++;
        }
    }
    
    private static String rOS(String s){
        if(s.contains("(")&&s.contains(")"))
            return s.substring(s.indexOf("("),s.lastIndexOf(")")+1);
        else{
            //System.out.println("TROUBLE WITH "+s);
            return s;
        }
        
    }
    public String toString(){
        String s="";
        s+="Head: "+head+"\n";
        for(int j=0;j<bindings.length;j++)
            s+=bindings[j]+"\n";
        s+="precondition:\n";
        for(int i=0;i<preconditions.size();i++){
            s+=preconditions.get(i)+"\n";
        }
        s+="effects:\n";
        for(int i=0;i<effects.size();i++){
            s+=effects.get(i)+"\n";
        }
        return s;
    }
    
    public List<PDDLGoal> binds(PDDLGoal action,PDDLProblem p){
        //System.out.println("Goals: "+action.getGoal());
        if(action.getGoal().contains("("+head+" ")){
            System.out.println(head+" this action is eventually bindable");
            List<PDDLGoal> bindedEffects=new ArrayList<PDDLGoal>();
            List<PDDLGoal> bindedBelief=new ArrayList<PDDLGoal>();
            String binds[]=action.getBinds();
            for(int i=0;i<binds.length;i++)
                System.out.println(binds[i]);
            if(bindings.length+1==binds.length){
                System.out.println("I believe I can bind! I believe I can touch the bind");
                for(int i=0;i<preconditions.size();i++){
                    PDDLGoal g=preconditions.get(i);
                    System.out.println("B-Prima della cura:"+g);
                    for(int j=0;j<bindings.length;j++){
                        g=g.copyBind(bindings[j], binds[j+1]);
                    }
                    System.out.println("B-Dopo la cura"+g);
                    bindedBelief.add(g);
                }
                for(int i=0;i<bindedBelief.size();i++){
                    if(p.indexOfBelief(bindedBelief.get(i))==-1){
                    	System.out.println("Ho cercato: " + bindedBelief.get(i) + "e non la ho trovata");
                    	if (!bindedBelief.get(i).isNegate()) {
                    	System.out.println("Dovevo trovarla...");
                        System.out.println("ERRORE MEGA GIGANTE CHE VA RESTITUITO AL MAIN: I don belief in: "+bindedBelief.get(i)+"!");
                        return null;
                    	}
                    }
                }
                for(int i=0;i<effects.size();i++){
                    PDDLGoal g=effects.get(i);
                    System.out.println("E-Prima della cura:"+g);
                    for(int j=0;j<bindings.length;j++){
                        g=g.copyBind(bindings[j], binds[j+1]);
                    }
                    System.out.println("E-Dopo la cura"+g);
                    bindedEffects.add(g);
                }
                return bindedEffects;
            }
        }else{
            //System.out.println("NOPE : "+head);
        }
        return null;
    }
    
}
