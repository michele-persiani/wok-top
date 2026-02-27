/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.pddl;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author danger
 */
public class PDDLItem {

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

    public JSONObject getItem() {
        return item;
    }

    public void setItem(JSONObject item) {
        this.item = item;
    }
    public PDDLItem getChangeWith() {
        return changeWith;
    }

    public void setChangeWith(PDDLItem changeWith) {
        this.changeWith = changeWith;
    }
    
    private String name;
    private String img;
    
    private JSONObject item;
    private PDDLItem changeWith;
    
    public PDDLItem(JSONObject item){
        this.item=item;
        name = item.getString("name");
        img = item.getString("img");
        changeWith=null;
    }
    
    public JSONObject getJSONSchema(){
        JSONObject result=new JSONObject();
        result.put("name", name);
        result.put("img", img);
        return result;
    }
    
    
    public boolean canDoActionOn(String action){
        JSONArray actions = item.getJSONArray("actions");
        for(int i=0;i<actions.length();i++){
            String a= actions.getString(i);
            if(a.contains(action)){
                return true;
            }
        }
        return false;
    }
}
