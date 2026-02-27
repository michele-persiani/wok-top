/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;
import it.unibo.msrehab.model.entities.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.*;


/**
 *
 * @author danger
 */
public class PDDLMap {
    
    
    public final int sizeX=800;
    public final int sizeY=600;

    public List<PDDLPlace> getPl() {
        return pl;
    }
    public List<PDDLPlace> getPlFilter(String name) {
        List<PDDLPlace> plf=new ArrayList<PDDLPlace>();
        for(int i=0;i<pl.size();i++){
            if(!pl.get(i).getName().contains(name))
                plf.add(pl.get(i));
        }
        return plf;
    }

    public void setPl(List<PDDLPlace> pl) {
        this.pl = pl;
    }

    public List<PDDLRoad> getRoads() {
        return roads;
    }

    public void setRoads(List<PDDLRoad> roads) {
        this.roads = roads;
    }

    public List<PDDLItem> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(List<PDDLItem> addedItems) {
        this.addedItems = addedItems;
    }
    public int getInitTime() {
        return initTime;
    }

    public void setInitTime(int initTime) {
        this.initTime = initTime;
    }

    public int getInitPlace() {
        return initPlace;
    }

    public void setInitPlace(int initPlace) {
        this.initPlace = initPlace;
    }
    
    private List<PDDLPlace> pl;
    private List<PDDLRoad> roads;
    private List<PDDLItem> addedItems=new ArrayList<PDDLItem>();
    private int initTime=16;
    private int initPlace=0;
   
    public static Random r =new Random();
    public PDDLMap(Environment pe){
        System.out.println(pe.getId());
            
        String environment = pe.getEnvironment();
        JSONObject p = new JSONObject(environment);
        JSONArray places = p.getJSONArray("places");
        JSONArray items = p.getJSONArray("items");
        JSONArray placesName = p.getJSONArray("places-list");
        JSONArray itemsName = p.getJSONArray("item-list");
        int numberOfPlace = 4 + r.nextInt(4);
        numberOfPlace = Math.min(numberOfPlace,placesName.length());//non posso mettere più posti di quelli che ci sono
        //numberOfPlace=4+2;//TODO REMOVE THIS



        int[][] romanPlant=generatePlace(numberOfPlace, placesName, places, items);

        int numberOfRoad;
        numberOfRoad =  1 + r.nextInt((int)(pl.size()/2));
        //numberOfRoad = 1 + pl.size()/2; //TODO REMOVE THIS

        generateRoad(romanPlant,numberOfRoad);

        /*for(int i=0;i<pl.size();i++){
            System.out.println(pl.get(i).getName()+" "+pl.get(i).getX()+" "+pl.get(i).getY());

        }*/    
        //System.out.println(getJSONSchema());
            
        
    }
    
    
    public JSONObject getJSONSchema(){
        JSONObject result=new JSONObject();
        result.put("time",initTime);
        result.put("initplace",initPlace);
        JSONArray result_place=new JSONArray();
        JSONArray result_road=new JSONArray();
        for(int i=0;i<pl.size();i++)
            result_place.put(pl.get(i).getJSONSchema());
        
        for(int i=0;i<roads.size();i++)
            result_road.put(roads.get(i).getJSONSchema());
        
        result.put("places",result_place);
        result.put("roads",result_road);
        return result;
    }
    
    public String getProblemInit(){
        String p="";
        
        for(int i=0;i<roads.size();i++){
            p+="        (path "+roads.get(i).getSrc().getName()+" "+roads.get(i).getDst().getName()+")\n";
            p+="        (path "+roads.get(i).getDst().getName()+" "+roads.get(i).getSrc().getName()+")\n";
        }
        p+="        (be "+pl.get(initPlace).getName()+")\n";
        for(int i=0;i<pl.size();i++){
            p+=pl.get(i).getProblemInit(addedItems);
        }
        return p;
    }
    
    public List<PDDLGoal> generateBelief(){
        List<PDDLGoal> belief=new ArrayList<PDDLGoal>();
        for(int i=0;i<roads.size();i++){
            //TODO scegli se una l'altra o entrambe!
            switch(roads.get(i).getType()){
                case both:
                    belief.add(new PDDLGoal("(path "+roads.get(i).getSrc().getName()+" "+roads.get(i).getDst().getName()+")"));
                    belief.add(new PDDLGoal("(path "+roads.get(i).getDst().getName()+" "+roads.get(i).getSrc().getName()+")"));
                    belief.add(new PDDLGoal("(road "+roads.get(i).getSrc().getName()+" "+roads.get(i).getDst().getName()+")"));
                    belief.add(new PDDLGoal("(road "+roads.get(i).getDst().getName()+" "+roads.get(i).getSrc().getName()+")"));
                break;
                case path:
                    belief.add(new PDDLGoal("(path "+roads.get(i).getSrc().getName()+" "+roads.get(i).getDst().getName()+")"));
                    belief.add(new PDDLGoal("(path "+roads.get(i).getDst().getName()+" "+roads.get(i).getSrc().getName()+")"));
                break; 
                case road:
                    belief.add(new PDDLGoal("(road "+roads.get(i).getSrc().getName()+" "+roads.get(i).getDst().getName()+")"));
                    belief.add(new PDDLGoal("(road "+roads.get(i).getDst().getName()+" "+roads.get(i).getSrc().getName()+")"));
                break;
            }
        }
        belief.add(new PDDLGoal("(be "+pl.get(initPlace).getName()+")"));
        for(int i=0;i<pl.size();i++){
        	System.out.println("Generating beliefs for : "  + pl.get(initPlace).getName() + ", addedItemsL: " + addedItems.toString() );
            belief.addAll(pl.get(i).generateBelief(addedItems));
        }
        return belief;
    }
    
    private JSONObject findPlace(String name,JSONArray places){
        for(int i=0;i<places.length();i++){
            if(places.getJSONObject(i).getString("name").equals(name)){
                return places.getJSONObject(i);
            }
        }
        return null;
    }
    
    private int indexOfItem(String name,List<PDDLItem> items){
        for(int i=0;i<items.size();i++){
            if(items.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    private void noOverlappingPlaces(PDDLPlace p){
        boolean overlap=false;
        do{
            overlap=false;
            for(int i=0;i<pl.size();i++){
                if(pl.get(i).overlap(p)){
                    overlap=true;
                    break;
                }
            }
            if(overlap)
                p.randomMove(sizeX, sizeY);
        }while(overlap);
    }
    private boolean isOverlappingRoad(PDDLRoad p){
        boolean overlap=false;
        for(int i=0;i<pl.size();i++){
            if(p.overlap(pl.get(i))){
                overlap=true;
                break;
            }
        }
        return overlap;
    }
    private PDDLPlace getPlaceOverlappingRoad(PDDLRoad p){
        for(int i=0;i<pl.size();i++){
            if(p.overlap(pl.get(i))){
                return pl.get(i);
            }
        }
        return null;
    }
    
    public List<PDDLRoad> splitRoad(PDDLRoad road){
        List<PDDLRoad> rl=new ArrayList<PDDLRoad>();
        PDDLPlace q=getPlaceOverlappingRoad(road);
        if(q==null){
           rl.add(road);
        }else{
            PDDLRoad r1=new PDDLRoad(road.getSrc(),q);
            rl.addAll(splitRoad(r1));
            PDDLRoad r2=new PDDLRoad(q,road.getDst());
            rl.addAll(splitRoad(r2));
        }
        return rl;
    }
    
    private int isPlace(int[][] map,int i, int j){
        if(i<0)
            return 0;
        if(i>=map.length)
            return 0;
        if(j<0)
            return 0;
        if(j>=map[0].length)
            return 0;
        if((map[i][j]<100)&&(map[i][j]>-1))
            return 1;
        return -1;
    }
    private int isUsefull(int[][] map,int i, int j,int threshold){
        if(map[i][j]>=100){
            int v = 0;
            v+= isPlace(map, i-1, j);
            v+= isPlace(map, i+1, j);
            v+= isPlace(map, i, j-1);
            v+= isPlace(map, i, j+1);
            return v;
        }else{
            return threshold+1;
        }           
    }
    private boolean isRoundUsefull(int[][] map,int threshold){
        boolean res = true;
        for(int i=0;i<map.length;i++)
            for(int j=0;j<map[0].length;j++){
                if(isUsefull(map, i, j,threshold)<threshold){
                    res = false;
                }
            }
        return res;
    }
    
    private int[][] swapRandomRow(int[][] map, int NC,int NR, int nswap){
        for(int i=0;i<nswap;i++){
            int r1,r2,c1,c2;
            r1=r.nextInt(NC);
            c1=r.nextInt(NR);
            r2=r.nextInt(NC);
            c2=r.nextInt(NR);
            int box=map[r1][c1];
            map[r1][c1]=map[r2][c2];
            map[r2][c2]=box;
        }
        return map;
    }
    private int[][] generatePlace(int numberOfPlace, JSONArray placesName,JSONArray places,JSONArray items){
        pl=new ArrayList<PDDLPlace>();
        int max = placesName.length();
        int maxItem=0;
        int itemAdded=0;
        
        /*for(int i=0;i<numberOfPlace;i++){            
            int q = r.nextInt(max-pl.size());            
            PDDLPlace newPlace = new PDDLPlace(findPlace(placesName.getString(q),places),sizeX,sizeY);
            noOverlappingPlaces(newPlace);
            pl.add(newPlace);
            placesName.remove(q);
            maxItem+=newPlace.getMaxItem();
        }*/
        //Numero di rotonde 1 ~ 4
        int numberOfRound=r.nextInt(Math.min(numberOfPlace-2, 4))+1;
        
        // stile pianta stradale
        int romanOption[][]={{3,2},{3,3},{4,3}};
        //pianta scelta
        int ro=0;
        if((numberOfPlace+numberOfRound)>(romanOption[0][0]*romanOption[0][1])){
            ro=1;
        }
        if((numberOfPlace+numberOfRound)>(romanOption[1][0]*romanOption[1][1])){
            ro=2;
        }
        
        //numero colonne
        int NC=romanOption[ro][0];
        //numero righe
        int NR=romanOption[ro][1];
        
        //pianta stradale -1 = vuoto 1-100 = place 100> rotonda
        int[][] roman = new int[NC][NR];
        for(int i=0;i<NC;i++){
            for(int j=0;j<NR;j++)
                roman[i][j]=-1;
        }
        //conteggio righe
        int ri=0;
        int c=0;
        for(int i=0;i<(numberOfPlace+numberOfRound);i++){
            ri=i%NC;
            if(i<numberOfPlace){
                roman[ri][c]=i;
            }
            else{
                roman[ri][c]=i+100;
            }
            if((i!=0)&&((i%NC)==0))
                c++;
        }
        // scambia a caso righe e colonne
        roman = swapRandomRow(roman,NC,NR, 50);
        
        int accepting = 1;
        int tring = 1;
        //controllo coerenza mappa!
        while(!isRoundUsefull(roman, accepting)){
            roman = swapRandomRow(roman,NC,NR, 3);
            tring++;
            if(tring%10==0){
                accepting--;
                System.out.println("Accetto vincolo morbido: "+accepting);
            }
        }
        
        
        
        //generazione mappa effettiva con edifici e rotonde vere!
        for(int j=0;j<NR;j++){
            for(int i=0;i<NC;i++){            
                if(roman[i][j]!=-1){
                    if(roman[i][j]<100){                        
                        int q = 0;
                        if(max-pl.size()>0)
                            r.nextInt(max-pl.size());            
                        PDDLPlace newPlace = new PDDLPlace(findPlace(placesName.getString(q),places),((sizeX-100)/NC)*i+50,((sizeY-150)/NR)*j+100);
                        noOverlappingPlaces(newPlace);
                        pl.add(newPlace);
                        placesName.remove(q);
                        maxItem+=newPlace.getMaxItem();
                        roman[i][j]=pl.size()-1;
                    }else{
                        PDDLPlace newPlace = new PDDLPlace("rotonda"+i+j,"rotonda",((sizeX-100)/NC)*i+50,((sizeY-150)/NR)*j+100,1,48);
                        pl.add(newPlace);
                        roman[i][j]=pl.size()-1;
                    }
                }
            }
        }
        
        int minrandom= Math.min(maxItem-5,items.length()-5);
        minrandom= Math.max(minrandom,0);
        int numberOfItem = 5 + r.nextInt(minrandom);
        
        System.out.println("Voglio mettere "+numberOfItem+" oggetti nella mappa!");
        
        int[] nOfItem = new int[pl.size()];
        
        int counter=0;
        int j=0;
        int maxNumberOfTry=20;
        int numberOfTry=0;
        while((counter<numberOfItem)&&(numberOfTry<maxNumberOfTry)){//TODO CAN DIVERGE!!!!!!
            if(nOfItem[j]<pl.get(j).getMaxItem()){
                if(r.nextBoolean()){
                    PDDLItem added = pl.get(j).addRandomItem(items, addedItems);
                    if(added!=null){
                        counter++;
                        nOfItem[j]++;
                        numberOfTry=0;
                    }else{
                        numberOfTry++;
                    }
                }
            }else{
                numberOfTry++;
            }
            j=(j+1)%pl.size();
        }
        
        return roman;
    }
    
    private void generateRoad(int[][] romanPlant,int numberOfRoads){
        boolean hasCar=false;
        if(indexOfItem("macchina", addedItems)!=-1){
            hasCar=true;
        }
        
        roads=new ArrayList<PDDLRoad>();
        for(int i=0;i<pl.size()-1;i++){
            PDDLRoad tr=new PDDLRoad(pl.get(i),pl.get(i+1));
            roads.addAll(splitRoad(tr));
        }
        for(int i=0;i<pl.size();i++){
            if(pl.get(i).getName().startsWith("rotonda")){
                for(int k=0;k<romanPlant.length;k++)
                    for(int j=0;j<romanPlant[0].length;j++)
                        if(romanPlant[k][j]==i){
                            List<PDDLPlace> l=getAdiacent(romanPlant, j, k);
                            boolean done = false;
                            while(!done){
                                if(l.size()>0){
                                    PDDLPlace b=l.remove(r.nextInt(l.size()));
                                    PDDLRoad tr=new PDDLRoad(pl.get(i),b,PDDLRoad.ROAD_TYPE.path);
                                    int n = roads.size();
                                    roads = addNotDuplicated(roads, tr);
                                    if(n>roads.size()){
                                        done = true;
                                    }
                                }else{
                                    done = true;
                                }
                            }
                        }
            }
        }
        for(int i=0;i<numberOfRoads;i++){
            int nr;
            int nc;
            do{
                nr=r.nextInt(romanPlant[0].length);
                nc=r.nextInt(romanPlant.length);
            }while(romanPlant[nc][nr]==-1);
            PDDLPlace a = pl.get(romanPlant[nc][nr]);
            PDDLPlace b = null;
            List<PDDLPlace> l=getAdiacent(romanPlant, nr, nc);
            if(l.size()>0){
                b=l.get(r.nextInt(l.size()));
            }
            if(b!=null){
                PDDLRoad tr;
                if(hasCar){
                    switch(r.nextInt(3)){
                        case 0:
                            tr=new PDDLRoad(a,b,PDDLRoad.ROAD_TYPE.road);
                        break;
                        case 1:
                            tr=new PDDLRoad(a,b,PDDLRoad.ROAD_TYPE.path);
                        break;
                        case 3:
                            tr=new PDDLRoad(a,b,PDDLRoad.ROAD_TYPE.both);
                        break;
                        default:
                            tr=new PDDLRoad(a,b,PDDLRoad.ROAD_TYPE.path);
                        break;
                    }
                }else{
                    tr=new PDDLRoad(a,b,PDDLRoad.ROAD_TYPE.path);
                }
                roads = addNotDuplicated(roads, tr);
                
            }
        }
    }
    
    private List<PDDLRoad> addNotDuplicated(List<PDDLRoad> roads,PDDLRoad tr){
        List<PDDLRoad> lr=splitRoad(tr);
        for(int j=0;j<lr.size();j++){//add only not duplicated road!
            boolean dubled=false;
            for(int k=0;k<roads.size();k++){
                if(roads.get(k).duplicated(lr.get(j))){
                    dubled=true;
                }
            }
            if(!dubled)
                roads.add(lr.get(j));
        }
        return roads;        
    }
    private List<PDDLPlace> getAdiacent(int[][] romanPlant,int nr,int nc){
        List<PDDLPlace> l=new ArrayList<PDDLPlace>();
        for(int i=0;i<romanPlant.length;i++){
            if((i!=nc)&&(romanPlant[i][nr]!=-1)){
                l.add(pl.get(romanPlant[i][nr]));
            }
        }
        for(int j=0;j<romanPlant[0].length;j++){
            if((j!=nr)&&(romanPlant[nc][j]!=-1)){
                l.add(pl.get(romanPlant[nc][j]));
            }
        }
        return l;
    }
    /*private void generateRoad(int numberOfRoads){
        roads=new ArrayList<PDDLRoad>();
        for(int i=0;i<pl.size()-1;i++){
            PDDLRoad tr=new PDDLRoad(pl.get(i),pl.get(i+1));
            roads.addAll(splitRoad(tr));
        }
        for(int i=0;i<numberOfRoads;i++){
            int a = r.nextInt(pl.size());
            int b;
            do{
                b= r.nextInt(pl.size());
            }while((a==b)||(a+1==b)||(a-1==b));
            PDDLRoad tr=new PDDLRoad(pl.get(a),pl.get(b));  
            roads.addAll(splitRoad(tr));
        }
    }*/
    
}
