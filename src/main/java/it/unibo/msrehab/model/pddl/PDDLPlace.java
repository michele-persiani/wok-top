/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author danger
 */
public class PDDLPlace {

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }
    
    public int getMaxItem() {
        return maxItem;
    }
    
    public List<PDDLItem> getItems() {
        return items;
    }

    public void setItems(List<PDDLItem> items) {
        this.items = items;
    }
    
    public final static int SIZE=100;
    public final static int PADDING=100;
    private int x;
    private int y;
    private String name;
    private String img;
    private int openTime;
    private int closeTime;
    private boolean wait=false;
   
    private List<PDDLItem> items=new ArrayList<PDDLItem>();

    
    private int maxItem=0;
    
    private JSONObject place;
    
    public static Random r=new Random();
    
    public PDDLPlace(String name,String img,int x,int y,int openTime, int closeTime){
        this.x=x;
        this.y=y;
        this.name=name;
        this.img=img;
        this.openTime=openTime;
        this.closeTime=closeTime;
        this.place=new JSONObject();
        place.put("name",name);
        place.put("img",img);
        place.put("min-hour",openTime);
        place.put("max-hour",closeTime);
        place.put("items-take",new JSONArray());
        place.put("items-leave",new JSONArray());
        place.put("actions",new JSONArray());
        place.put("wait","false");
        
    }
    public PDDLPlace(JSONObject place,int maxX,int maxY){
        this.place=place;
        
        this.x=maxX;//r.nextInt(maxX-SIZE);
        this.y=maxY;//r.nextInt(maxY-SIZE);
        //avoidTopRightCorner();
        this.name=place.getString("name");
        this.img=place.getString("img");
        
        if(place.getInt("max-hour")-place.getInt("min-hour")-place.getInt("min-open-hour")>0)
            this.openTime=r.nextInt(place.getInt("max-hour")-place.getInt("min-hour")-place.getInt("min-open-hour"))+place.getInt("min-hour");
        else
            this.openTime=place.getInt("min-hour");
        
        if(place.getInt("max-hour")-openTime-place.getInt("min-open-hour")>0)
            this.closeTime=r.nextInt(place.getInt("max-hour")-openTime-place.getInt("min-open-hour"))+openTime+place.getInt("min-open-hour");
        else
            this.closeTime=openTime+place.getInt("min-open-hour");
        this.maxItem = place.getJSONArray("items-take").length();
        
        this.wait = place.getBoolean("wait");
    }
    
    public boolean overlap(PDDLPlace b){
        int xx = (x+SIZE/2)-(b.getX()+SIZE/2);
        xx= xx*xx;
        int yy = (y+SIZE/2)-(b.getY()+SIZE/2);
        yy=yy*yy;
        return Math.sqrt(xx+yy)<Math.sqrt(SIZE/2*SIZE/2)+PADDING;
    }
    private void avoidTopRightCorner(){
        int CORNER_LEFT = 670;
        int CORNER_TOP = 100;
        if(x>(CORNER_LEFT-SIZE) && y<CORNER_TOP){
            if(r.nextBoolean()){
                x=CORNER_LEFT-5-SIZE;
            }else{
                y=CORNER_TOP+5;
            }
        }
    }
    
    public PDDLItem addRandomItem(JSONArray itemsDetail, List<PDDLItem> li){
        JSONArray availableItem = place.getJSONArray("items-take"); 
        
        int it = availableItem.length();        
        String nameItemChoosen = availableItem.getString(r.nextInt(it));
        for(int i=0;i<availableItem.length();i++){
            if(availableItem.get(i).equals("macchina")){
                nameItemChoosen="macchina";
            }
        }
        PDDLItem itemChoosen = itemChoosen = new PDDLItem(findItem(nameItemChoosen, itemsDetail));
        int i=0;
        while((isAlreadyAdded(itemChoosen, li))&&(i<availableItem.length())){
            availableItem.getString(i);
            itemChoosen = new PDDLItem(findItem(nameItemChoosen, itemsDetail));
            i++;
        }
        
        if(i<availableItem.length()){
            li.add(itemChoosen);
            items.add(itemChoosen);
        }else{
            itemChoosen=null;
        }
        return itemChoosen;
    }
    
    private static boolean isAlreadyAdded(PDDLItem itemChoosen,List<PDDLItem> li){
        for(int i=0;i<li.size();i++){
            if(li.get(i).getName().equals(itemChoosen.getName())){
                return true;
            }
        }
        return false;
    } 
    
    public void randomMove(int maxX,int maxY){
        System.out.println("DBG ["+name+"]"+x+" "+y);
        if((r.nextBoolean()&&(0<(maxX-x-SIZE*2)))||(x<=SIZE))
            this.x=r.nextInt(maxX-x-SIZE*2)+x+SIZE;
        else
            this.x=r.nextInt(x-SIZE)+SIZE;
        if((r.nextBoolean()&&(0<(maxY-y-SIZE*2)))||(y<=SIZE))
            this.y=r.nextInt(maxY-y-SIZE*2)+y+SIZE;
        else
            this.y=r.nextInt(y-SIZE)+SIZE;
        avoidTopRightCorner();
    }
    
    public JSONObject getJSONSchema(){
    	System.out.println("PPDLPlace getJSONSchema");
        JSONObject result=new JSONObject();
        result.put("name", name);
        result.put("img", img);
        result.put("x", x);
        result.put("y", y);
        result.put("open-time",openTime);
        result.put("close-time",closeTime);
        result.put("wait",wait);
        JSONArray result_items=new JSONArray();
        for(int i=0;i<items.size();i++){
            result_items.put(items.get(i).getJSONSchema());
        }        
        result.put("items", result_items);
        // Add items which can be dropped here
        JSONArray canBeDropped = place.getJSONArray("items-leave");
        result.put("can-drop", canBeDropped);
        //System.out.println("Gli elementi: " + canBeDropped.toString());
        //System.out.println("Adding items which can be exchanged");
        // Add items which can be exchanged
        JSONArray allowedExchanges = new JSONArray();
        for(int i=0;i<items.size();i++){
            if(items.get(i).getChangeWith() != null){
            	// If the item can be exchanged with something, create an "exchange" object and add it to the array
            	JSONObject exchange = new JSONObject();
            	exchange.put("item1", items.get(i).getName());
            	exchange.put("item2", items.get(i).getChangeWith().getName());
            	allowedExchanges.put(exchange);
            }
        }
        result.put("can-exchange", allowedExchanges);
        //System.out.println("These items can be exchanged here: " + allowedExchanges.toString());
        return result;
    }
    
    public String getProblemInit(List<PDDLItem> addedItems){
        String p="";
        p+="        (open-hour "+name+" p"+openTime+" p"+closeTime+")\n";
        for(int i=0;i<items.size();i++){
            if(items.get(i).getChangeWith()==null){
                p+="        (have "+name+" "+items.get(i).getName()+")\n";
           }else{
                p+="        (have-in-change "+name+" "+items.get(i).getName()+" "+items.get(i).getChangeWith().getName()+")\n";
            }
        	// All items can be changed with anything
        	p+="        (have "+name+" "+items.get(i).getName()+")\n";
        }
        //AGGIUNGERE GLI OGGETTI CHE POSSONO ESSERE DROPPATI QUI!!! OMG NON SO COME AVERLI!!!
        List<String> nItems = new ArrayList<String>();
        for(int i=0;i<addedItems.size();i++){
            nItems.add(addedItems.get(i).getName());
        }
        
        JSONArray leave = place.getJSONArray("items-leave");
        for(int i=0;i<leave.length();i++){
            if(nItems.contains(leave.getString(i))){
                p+="        (can-drop "+leave.getString(i)+" "+name+")\n";
           }
        }
        
        return p;
    }
    
    public List<PDDLGoal> generateBelief(List<PDDLItem> addedItems){
        List<PDDLGoal> belief=new ArrayList<PDDLGoal>();
        belief.add(new PDDLGoal("(open-hour "+name+" p"+openTime+" p"+closeTime+")"));
        for(int i=0;i<items.size();i++){
            if(items.get(i).getChangeWith()==null){
                belief.add(new PDDLGoal("(have "+name+" "+items.get(i).getName()+")"));
           }else{
                belief.add(new PDDLGoal("(have-in-change "+name+" "+items.get(i).getName()+" "+items.get(i).getChangeWith().getName()+")"));
            }
        	belief.add(new PDDLGoal("(have "+name+" "+items.get(i).getName()+")"));
        }
        //AGGIUNGERE GLI OGGETTI CHE POSSONO ESSERE DROPPATI QUI!!! OMG NON SO COME AVERLI!!!
        List<String> nItems = new ArrayList<String>();
        for(int i=0;i<addedItems.size();i++){
            nItems.add(addedItems.get(i).getName());
        }
        System.out.print("PDDLPlace - generateBelief");
        JSONArray leave = place.getJSONArray("items-leave");
        for(int i=0;i<leave.length();i++){
            if(nItems.contains(leave.getString(i))){
                belief.add(new PDDLGoal("(can-drop "+leave.getString(i)+" "+name+")"));
           }
        }
        return belief;
    }
    
    public static JSONObject findItem(String name,JSONArray items){
        for(int i=0;i<items.length();i++){
            if(items.getJSONObject(i).getString("name").equals(name)){
                return items.getJSONObject(i);
            }
        }
        System.out.println("QUI NON CI SI DEVE MAI ARRIVARE: "+name);
        return null;
    }
    
    
    public boolean canDoActionHere(String action){
        JSONArray actions = place.getJSONArray("actions");
        for(int i=0;i<actions.length();i++){
            String a= actions.getString(i);
            if(a.contains(action)){
                return true;
            }
        }
        return false;
    }
    
    public boolean canDropHere(String item){
        boolean b=false;
        JSONArray droppableItem = place.getJSONArray("items-leave");
        for(int i=0;i<droppableItem.length();i++){
            String a= droppableItem.getString(i);
            if(a.equals(item)){
                b=true;
            }
        }
        
        for(int i=0;i<items.size();i++){
            if(items.get(i).getName().equals(item)){
                b=false;
            }
        }
        return b;
    }
    
    
    public String getPlaceHead(){
        if(!wait){
            return "place";
        }else{
            return "waitplace";
        }
    }
}
