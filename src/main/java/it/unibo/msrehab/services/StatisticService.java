/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.services;

import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.util.CookiesManager;
import it.unibo.msrehab.util.WebPagesUtilities;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
//import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author danger
 */
@Controller
public class StatisticService
{
    private final ExerciseController exerciseController;
    private final MSRUserController userController;
    private final MSRGroupController groupController;


    public StatisticService(){
        super();
        this.userController = new MSRUserController();
        this.exerciseController = new ExerciseController();
        this.groupController = new MSRGroupController();
    }

    @RequestMapping(value = "/patientsstatistics", method = RequestMethod.GET)
    public ModelAndView patientsstatistics(
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }
        else {
            int cid = CookiesManager.getLoggedUserCenter(request);
            model.addAttribute("patients", userController.findAllPatientsInCenter(cid));
            return new ModelAndView("selectPatientStatistics");
        }
    }

    @RequestMapping(value = "/patientchosegraph", method = RequestMethod.GET)
    public ModelAndView patientchosegraph(
            @RequestParam(value = "patientid", required = true) Integer id,
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        List<String> types = exerciseController.getAllExerciseTypes();


        model.addAttribute("patient", userController.findEntity(id).get());
        model.addAttribute("types", types);
        return new ModelAndView("patientchosegraph");
    }


    @RequestMapping(value = "/patientchosegraph2", method = RequestMethod.GET)
    public ModelAndView patientchosegraph2(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "kind", required = true) String kind,
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        try{
            Exercise.ExerciseCategoryValue q = Exercise.ExerciseCategoryValue.valueOf(kind);
            String type = exerciseController.findExerciseTypeFromCategory(q);
            if(!type.equals("")){
                kind = type;
            }
        }catch(IllegalArgumentException e){

        }

        List<Exercise> lcategory = exerciseController.findAllExercisesByType(kind);
        ArrayList<String> category = new ArrayList<String>();
        ArrayList<String> category_testo = new ArrayList<String>();
        ArrayList<String> globalcategory = new ArrayList<String>();
        ArrayList<String> category_nested = new ArrayList<String>();

        for(Exercise c: lcategory){
        	/*if (!category.contains(""+c.getCategory())) {
        		category.add(""+c.getCategory());
        	}
        	if (!globalcategory.contains(""+c.getGlobalCategory())) {
        		globalcategory.add(""+c.getGlobalCategory());
        	}
            if (category_testo.contains(""+c.getFullName())) {
        		category_testo.add(""+c.getFullName());
        	}*/
            category.add(""+c.getCategory());
            globalcategory.add(""+c.getGlobalCategory());
            category_nested.add(""+c.getFullName());
            String[] parts = (""+c.getGlobalCategory()).split("(?=-)");
            String temp="";
        	//temp= parts[0]+parts[1]+parts[2];
            for (int i=0; i<2; i++) {
            	temp = temp+parts[i];
            }
            category_testo.add(temp);
            temp = temp+" ";
            category_nested.add(temp);
        }

        model.addAttribute("category",category);
        model.addAttribute("globalcategory",globalcategory);
        model.addAttribute("category_testo",category_testo);
        model.addAttribute("tipo",getItaNameKind(kind));
        model.addAttribute("kind", kind);

        model.addAttribute("patient", userController.getEntity(id));

        return new ModelAndView("patientchosegraph2");
    }



    @RequestMapping(value = "/patientbuildgraph", method = RequestMethod.GET)
    public ModelAndView patientbuildgraph(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "kind", required = true) String kind,
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }


        return new ModelAndView("patientgraphoverview_new")
                .addObject("userId", id)
                .addObject("exerciseCategory", kind)
                ;
    }


    @RequestMapping(value = "/patientbuildgraphoverview", method = RequestMethod.GET)
    public ModelAndView patientbuildgraphoverview(
            @RequestParam Integer patientid,
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        return new ModelAndView("patientgraphoverview_new")
                .addObject("userId", patientid);
    }


    @RequestMapping(value = "/groupstatistics", method = RequestMethod.GET)
    public ModelAndView groupstatistics(
            HttpServletRequest request,
            Model model) {
        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }
        else {
            int cid = CookiesManager.getLoggedUserCenter(request);
            model.addAttribute("groups", groupController.findAllGroupsInCenter(cid));
            return new ModelAndView("selectGroupStatistics");
        }
    }

    @RequestMapping(value = "/groupgraph", method = RequestMethod.GET)
    public ModelAndView groupgraph(
            @RequestParam(value = "groupid", required = true) Integer groupid,
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }


        return new ModelAndView("patientgraphoverview_new")
                .addObject("groupId", groupid)
                ;
    }


    @RequestMapping(value = "/displaystatistics", method = RequestMethod.GET)
    public ModelAndView displaystatistics(
            @RequestParam Integer userid,
            @RequestParam Integer groupid,
            @RequestParam String exercisename,
            @RequestParam String exercisecategory,
            HttpServletRequest request,
            Model model)
    {

        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request))))
        {
            return new ModelAndView("login");
        }

        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request))))
        {
            return new ModelAndView("login");
        }


        return new ModelAndView("patientgraphoverview_new")
                .addObject("UserId", userid)
                .addObject("groupId", groupid)
                .addObject("exerciseName", exercisename)
                .addObject("exerciseCategory", exercisecategory)
                ;
    }

    public String getItaNameKind(String kind){
        /*String[] key=new String[]{"attenzione","memoria","funzioni esecutive","ATT_SEL_STD","ATT_SEL_FLW","ATT_ALT","ATT_DIV","MEM_VIS_1","MEM_VIS_2","NBACK","FUN_EXC"};
        String[] name = new String[]{"Attenzione","Memoria","Funzioni esecutive","Attenzione selettiva standard","Attenzione selettiva scorrimento","Attenzione alternata","Attenzione divisa","Memoria visiva 1","Memoria visiva 2","N-Back","Funzioni esecutive"};
        for(int i=0;i<key.length;i++){
            if(key[i].equals(kind))
                return name[i];
        }*/

        try{
            Exercise.ExerciseCategoryValue q = Exercise.ExerciseCategoryValue.valueOf(kind);
            String s=exerciseController.findGlobalCategory(q);
            return s;
        }catch(IllegalArgumentException e){

        }
        return kind.substring(0,1).toUpperCase()+kind.substring(1,kind.length()).toLowerCase();
        //return "undefined";
    }

}
