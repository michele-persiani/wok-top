/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author danger
 */
public class PDDLGoalBuilder {
    
    private List<PDDLPlace> pl;
    private List<PDDLItem> addedItems;
    private int minhour;
    
    public static Random r=new Random();
    public PDDLGoalBuilder(PDDLMap m){
        this.pl=m.getPlFilter("rotonda");
        this.minhour=m.getInitTime();
        this.addedItems=m.getAddedItems();
    }
    
    public PDDLGoal buildGoal(){
        PDDLGoal g=null;
        if(r.nextBoolean()){//TODO VERIFICARE SE L'AZIONE PUÒ ESSERE FATTA IN QUEL LUOGO
            //CREATE A PLACE-GOAL
            g=buildPlaceGoal();
        }else{
            //CREATE A ITEM-GOAL
            g=buildItemGoal();
        }       
        return g;
    }
    
    private PDDLGoal buildItemGoal(){
        PDDLGoal g=null;
        int option = r.nextInt(3);
        int src=-1;
        int time=-1;
        int item;
        switch(option){
            case 0:
                //got
                if(r.nextBoolean()){//got easy something
                    if(addedItems.size()>0){
                        item = r.nextInt(addedItems.size());
                        g = new PDDLGoal("(got "+addedItems.get(item).getName()+")");
                    }
                }else{//got something in change of something!
                    List<PDDLItem> canChange=new ArrayList<PDDLItem>();
                    ArrayList<ArrayList<PDDLPlace>> llp = new ArrayList<ArrayList<PDDLPlace>>();
                    int count=-1;
                    for(int i=0;i<addedItems.size();i++){//verifico su quali oggetti posso applicare la change
                        if(addedItems.get(i).canDoActionOn("change-item")){
                            System.out.println("1. Posso cambiare "+addedItems.get(i).getName());
                            for(int j=0;j<pl.size();j++){
                                System.out.println("1.1 Posso cambiare "+addedItems.get(i).getName()+" in "+pl.get(j).getName());
                                if((pl.get(j).getItems().size()>0)&&(pl.get(j).canDoActionHere("change-item"))&&(pl.get(j).canDropHere(addedItems.get(i).getName()))){
                                    System.out.println("2. Posso cambiare "+addedItems.get(i).getName()+" qui "+pl.get(j).getName());
                                    //aggiungo i posti nei quali posso scambiare l'oggetto i e che hanno qualcosa da scambiare
                                    if(canChange.contains(addedItems.get(i))){
                                        System.out.println("3.2 Aggiungo il posto alla coda "+count);
                                        llp.get(count).add(pl.get(j));
                                    }else{
                                        count++;
                                        System.out.println("3.1 Creo una nuova coda "+count);
                                        canChange.add(addedItems.get(i));
                                        llp.add(new ArrayList<PDDLPlace>());
                                        llp.get(count).add(pl.get(j));
                                    }
                                }
                            }
                        }
                    }
                    
                    if(canChange.size()>0){
                        for(int q=0;q<canChange.size();q++){
                            //item = r.nextInt(canChange.size());//scelgo l'oggetto da scambiare nell'elenco
                            item = q;
                            System.out.println("4. Ci sono oggetti da scambiare scelgo: "+canChange.get(item).getName());
                            
                            src = r.nextInt(llp.get(item).size());//scelgo il posto nel quale scambiarlo tra quelli disponibili
                            PDDLPlace tempp = llp.get(item).get(src);
                            System.out.println("5. Il posto dello scambio scelto è: "+tempp.getName()+" ci sono ben "+tempp.getItems().size());
                            for(int i=0;i<tempp.getItems().size();i++){//scelgo un oggetto nel posto sul quale posso applicare lo scambio 
                                System.out.println("5.1 Posso applicare lo scambio di "+canChange.get(item).getName()+" con: "+tempp.getItems().get(i).getName()+" posso? "+tempp.getItems().get(i).canDoActionOn("change-item"));
                                if((!tempp.getItems().get(i).equals(canChange.get(item)))&&(tempp.getItems().get(i).canDoActionOn("change-item"))){
                                    System.out.println("6. Scambio con: "+tempp.getItems().get(i).getName());
                                    tempp.getItems().get(i).setChangeWith(canChange.get(item)); //aggiungo il fatto che deve esserci uno scambio per tale oggetto e non può essere solo preso
                                    g = new PDDLGoal("(got-in-change "+canChange.get(item).getName()+" "+tempp.getItems().get(i).getName()+")");
                                    break;
                                }
                            }
                        }
                    }
                }                
                break;
            case 1:
                
                List<PDDLItem> canTakeAt = canDoSomething("take-item-at",addedItems);
                //got-at
                if(canTakeAt.size()>0){
                    item = r.nextInt(canTakeAt.size());
                    for(int i=0;i<pl.size();i++){
                        if(pl.get(i).getItems().contains(canTakeAt.get(item))){
                            src = i;
                        }
                    }
                    if(src!=-1){
                        time = r.nextInt(pl.get(src).getCloseTime()-pl.get(src).getOpenTime())+pl.get(src).getOpenTime();
                        time = Math.max(minhour,time);
                        g = new PDDLGoal("(got-at "+canTakeAt.get(item).getName()+" "+pl.get(src).getName()+" p"+time+")");
                    }
                }
                break;
            case 2:
                //drop-at
                List<PDDLItem> canDrop=canDoSomething("drop-item",addedItems);
                if(canDrop.size()>0){
                    item = r.nextInt(canDrop.size());
                    for(int i=0;i<pl.size();i++){//verifico dove posso droppare tale oggetto
                        if(pl.get(i).canDropHere(canDrop.get(item).getName())){
                            src = i;
                            time = r.nextInt(pl.get(src).getCloseTime()-pl.get(src).getOpenTime())+pl.get(src).getOpenTime();
                            time = Math.max(minhour,time);
                            break;
                        }
                    }
                    if(src!=-1){
                        g = new PDDLGoal("(drop-at "+canDrop.get(item).getName()+" "+pl.get(src).getName()+" p"+time+")");
                    }
                }
                
                break;
        }
        
        return g;
    }
    
    public static List<PDDLItem> canDoSomething(String action,List<PDDLItem> addedItems ){
        List<PDDLItem> canDo=new ArrayList<PDDLItem>();
        for(int i=0;i<addedItems.size();i++){//verifico su quali oggetti posso applicare la drop
            if(addedItems.get(i).canDoActionOn(action)){
                canDo.add(addedItems.get(i));
            }
        }
        return canDo;
    }
    
    private PDDLGoal buildPlaceGoal(){
        PDDLGoal g=null;
        int option = r.nextInt(5);
        int src;
        int dst;
        int time;
        switch(option){
            case 0:
                //be
                src = r.nextInt(pl.size());
                g = new PDDLGoal("(be "+pl.get(src).getName()+")");
                break;
            case 1:
                //be-at
                List<PDDLPlace> canBeat=new ArrayList<PDDLPlace>();
                for(int i=0;i<pl.size();i++){
                    if(pl.get(i).canDoActionHere("be-at")){
                        canBeat.add(pl.get(i));
                    }
                }
                if(canBeat.size()>0){
                    src = r.nextInt(canBeat.size());
                    time = r.nextInt(canBeat.get(src).getCloseTime()-canBeat.get(src).getOpenTime())+canBeat.get(src).getOpenTime();
                    if(minhour<canBeat.get(src).getCloseTime()){
                        time = Math.max(minhour,time);
                        g = new PDDLGoal("(be-at "+canBeat.get(src).getName()+" p"+time+")");
                    }
                }
                break;
            case 2:
                //stand 1-4
                List<PDDLPlace> canStill=new ArrayList<PDDLPlace>();
                for(int i=0;i<pl.size();i++){
                    if(pl.get(i).canDoActionHere("still")){
                        canStill.add(pl.get(i));
                    }
                }
                if(canStill.size()>0){
                    src = r.nextInt(canStill.size());
                    //time = canStill.get(src).getCloseTime()-canStill.get(src).getOpenTime(); //Posso stare in un posto anche a negozio chiuso!!!
                    time = r.nextInt(3)+1;
                    g = new PDDLGoal("(stand-"+time+"h "+canStill.get(src).getName()+")");                    
                }
                break;
                case 3:
                //be-at2
                List<PDDLPlace> canBeat2=new ArrayList<PDDLPlace>();
                for(int i=0;i<pl.size();i++){
                    if(pl.get(i).canDoActionHere("be-at2")){
                        canBeat2.add(pl.get(i));
                    }
                }
                if(canBeat2.size()>0){
                    dst= r.nextInt(canBeat2.size());
                    src = r.nextInt(canBeat2.size());
                    time = r.nextInt(canBeat2.get(dst).getCloseTime()-canBeat2.get(dst).getOpenTime())+canBeat2.get(dst).getOpenTime();
                    if(minhour<canBeat2.get(dst).getCloseTime()){
                        time = Math.max(minhour,time);
                        g = new PDDLGoal("(be-at2 "+canBeat2.get(src).getName() +canBeat2.get(dst).getName()+" p"+time+")");
                    }
                }
                break;
                case 4:
                //be-at3
                List<PDDLPlace> canBeat3=new ArrayList<PDDLPlace>();
                for(int i=0;i<pl.size();i++){
                    if(pl.get(i).canDoActionHere("be-at3")){
                        canBeat3.add(pl.get(i));
                    }
                }
                if(canBeat3.size()>0){
                    dst= r.nextInt(canBeat3.size());
                    src = r.nextInt(canBeat3.size());
                    time = r.nextInt(canBeat3.get(dst).getCloseTime()-canBeat3.get(dst).getOpenTime())+canBeat3.get(dst).getOpenTime();
                    if(minhour<canBeat3.get(dst).getCloseTime()){
                        time = Math.max(minhour,time);
                        g = new PDDLGoal("(be-at3 "+canBeat3.get(src).getName() +canBeat3.get(dst).getName()+" p"+time+")");
                    }
                }
                break;
        }
        
        return g;
    }
}
