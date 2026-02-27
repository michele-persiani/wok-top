/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.json.JSONObject;

/**
 *
 * @author danger
 */
public class PDDLRoad {
    private int ix;
    private int iy;
    private int ex;
    private int ey;
    
    public enum ROAD_TYPE{
        road, path, both
    }
    private ROAD_TYPE type;

    public ROAD_TYPE getType() {
        return type;
    }

    public void setType(ROAD_TYPE type) {
        this.type = type;
    }

    public PDDLPlace getSrc() {
        return src;
    }

    public void setSrc(PDDLPlace src) {
        this.src = src;
    }

    public PDDLPlace getDst() {
        return dst;
    }

    public void setDst(PDDLPlace dst) {
        this.dst = dst;
    }
    
    private PDDLPlace src;
    private PDDLPlace dst;
    
    public static final int ROAD_FAULT_TOLLERANCE = 80;
    
    public PDDLRoad(PDDLPlace a,PDDLPlace b){
        ix=a.getX();
        iy=a.getY();
        ex=b.getX();
        ey=b.getY();
        src=a;
        dst=b;
        type=ROAD_TYPE.both;
    }
    public PDDLRoad(PDDLPlace a,PDDLPlace b,ROAD_TYPE type){
        ix=a.getX();
        iy=a.getY();
        ex=b.getX();
        ey=b.getY();
        src=a;
        dst=b;
        this.type=type;
    }
    
    public boolean overlap(PDDLPlace p){
        boolean overlap=false;
        if(p.equals(src)||p.equals(dst))
            return false;
        
        if(src.getX()<dst.getX()){
            if(p.getX()+p.SIZE<src.getX()){
                return false;
            }
            if(p.getX()>dst.getX()+p.SIZE){
                return false;
            }
        }else{
            if(p.getX()>src.getX()+p.SIZE){
                return false;
            }
            if(p.getX()+p.SIZE<dst.getX()){
                return false;
            }
        }
        
        if(src.getY()<dst.getY()){
            if(p.getY()+p.SIZE<src.getY()){
                return false;
            }
            if(p.getY()>dst.getY()+p.SIZE){
                return false;
            }
        }else{
            if(p.getY()>src.getY()+p.SIZE){
                return false;
            }
            if(p.getY()+p.SIZE<dst.getY()){
                return false;
            }
        }
        
        
        int y1 = iy+p.SIZE/2;
        int y2 = ey+p.SIZE/2;
        int x1 = ix+p.SIZE/2;
        int x2 = ex+p.SIZE/2;
        Line2D l=new Line2D.Double(x1,y1,x2,y2);
        Point2D c=new Point2D.Double((p.getX()+p.SIZE/2),(p.getY()+p.SIZE/2));
        /*int m = (y2-y1)/(x2-x1);
        double q = -m*x1+y1;
        
        double d = Math.abs((p.getY()+p.SIZE/2)-(m*(p.getX()+p.SIZE/2)+q))/Math.sqrt(1+m*m);
        System.out.println("Sto valutando un posto "+src.getName()+"=>["+p.getName()+"]=>"+dst.getName()+" distante: "+d);*/
        double d = l.ptLineDist(c);
        if(d<ROAD_FAULT_TOLLERANCE){
            System.out.println("LO SPEZZO!");
            overlap=true;
        }
        return overlap;
    }
    public JSONObject getJSONSchema(){
        JSONObject result=new JSONObject();
        result.put("ix", ix);
        result.put("iy", iy);
        result.put("ex", ex);
        result.put("ey", ey);
        result.put("type", ""+type);
        return result;
    }
    
    public boolean duplicated(PDDLRoad b){
        if(this.src.getName().equals(b.dst.getName())&&this.dst.getName().equals(b.src.getName())){
            return true;
        }
        if(this.dst.getName().equals(b.dst.getName())&&this.src.getName().equals(b.src.getName())){
            return true;
        }
        return false;
    }
}
