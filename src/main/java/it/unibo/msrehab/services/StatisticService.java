/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.IntervalTime;
import it.unibo.msrehab.model.entities.MSRGroup;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.FitnessWeightController;
import it.unibo.msrehab.model.controller.HistoryController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.util.CookiesManager;
import it.unibo.msrehab.util.WebPagesUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import org.joda.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
//import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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





    private static final Logger logger = LoggerFactory
            .getLogger(ExerciseService.class);
    private final HistoryController historyController;
    private final Configuration config;
    private final FitnessWeightController fitnessController;
    private final ExerciseController exerciseController;
    private final MSRSessionController sessionController;

    private final MSRUserController userController;
    private final MSRGroupController groupController;

    public enum SessionValue {
        easy, medium, hard;
    };

    public StatisticService(){
        super();
        this.historyController = new HistoryController();
        this.fitnessController  = new FitnessWeightController();
        this.userController = new MSRUserController();
        this.exerciseController = new ExerciseController();
        this.sessionController = new MSRSessionController();
        this.groupController = new MSRGroupController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
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
    
    //public boolean isIn()

    @RequestMapping(value = "/patientchosegraph2", method = RequestMethod.GET)
    public ModelAndView patientchosegraph2(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "kind", required = true) String kind,
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        /*String[] opt=new String[]{"attenzione","ATT_SEL_STD","ATT_SEL_FLW","ATT_ALT","ATT_DIV"};
        if(Arrays.asList(opt).contains(kind)){
            model.addAttribute("category",new String[]{"ATT_SEL_STD","ATT_SEL_FLW","ATT_ALT","ATT_DIV"});
            model.addAttribute("category_testo",new String[]{"Attenzione selettiva standard","Attenzione selettiva scorrimento","Attenzione alternata","Attenzione divisa"});
            model.addAttribute("tipo",getItaNameKind(opt[0]));
            model.addAttribute("kind", opt[0]);
        }

        opt = new String[]{"memoria","MEM_VIS_1","MEM_VIS_2","NBACK"};
        if(Arrays.asList(opt).contains(kind)){
            model.addAttribute("category",new String[]{"MEM_VIS_1","MEM_VIS_2","NBACK"});
            model.addAttribute("category_testo",new String[]{"Memoria visiva 1","Memoria visiva 2","N-Back"});
            model.addAttribute("tipo",getItaNameKind(opt[0]));
            model.addAttribute("kind", opt[0]);
        }

        opt = new String[]{"funzioni esecutive","FUN_EXC"};
        if(Arrays.asList(opt).contains(kind)){
            model.addAttribute("category",new String[]{"FUN_EXC"});
            model.addAttribute("category_testo",new String[]{"Funzioni esecutive"});
            model.addAttribute("tipo",getItaNameKind(opt[0]));
            model.addAttribute("kind", opt[0]);
        }*/

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

        model.addAttribute("patient", userController.findEntity(id).get());

        return new ModelAndView("patientchosegraph2");
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

    public static final int SUCCESS = 0;
    public static final int NPROVE = 1;
    public static final int PERFORMANCE = 2;
   // public static final int RELPERFORMANCE = 3;
    /*public static final int RELPERFORMANCE = 3;
    public static final int ABSPERFORMANCE = 2;*/
    public static final int TIMESTAMP = 4;
    public static final int AVG_C=5;
    public static final int AVG_M=6;
    public static final int AVG_W=7;
    public static final int AVG_TIME=8;
    public static final int MAXTIME=9;
    

    @RequestMapping(value = "/patientbuildgraph", method = RequestMethod.GET)
    public ModelAndView patientbuildgraph(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "kind", required = true) String kind,
            @RequestParam(value = "difficulty", required = false, defaultValue = "general") String difficulty,
            @RequestParam(value = "group", required = false, defaultValue = "day") String group,
            @RequestParam(value = "start", required = false, defaultValue = "") String tstart,
            @RequestParam(value = "end", required = false, defaultValue = "") String tend,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        /*if (difficulty.equals("general")) {
        	int group_n=IntervalTime.daysfromgroup(group);
        	IntervalTime it=new IntervalTime(group, tstart, tend);
            Long end=it.getEnd();
            Long start=it.getStart();
        	List<Object[]> temp=historyController.findAllByTypeNoDifficulty(id,kind,group_n,start,end);
        	JSONObject json_graph = extractJSONPerformanceGeneral(temp,kind);
        	JSONObject json_bar = extractJSONSuccess(temp);
        	model.addAttribute("patient", userController.getRecord(id));
            model.addAttribute("json_graph", json_graph);
            model.addAttribute("json_bar", json_bar);
            model.addAttribute("kind",kind);
            model.addAttribute("tipo",getItaNameKind(kind));
            model.addAttribute("group",group);
            model.addAttribute("difficulty",difficulty);
            model.addAttribute("difficolta",getItaNameDifficult(difficulty));
            model.addAttribute("start",tstart);
            model.addAttribute("end",tend);
        }
        else {
        	List<Object[]> temp=buildgraph(id, kind, difficulty, group, tstart, tend);
        	JSONObject json_graph = extractJSONPerformance(temp);
        	JSONObject json_bar = extractJSONSuccess(temp);
        	model.addAttribute("patient", userController.getRecord(id));
            model.addAttribute("json_graph", json_graph);
            model.addAttribute("json_bar", json_bar);
            model.addAttribute("kind",kind);
            model.addAttribute("tipo",getItaNameKind(kind));
            model.addAttribute("group",group);
            model.addAttribute("difficulty",difficulty);
            model.addAttribute("difficolta",getItaNameDifficult(difficulty));
            model.addAttribute("start",tstart);
            model.addAttribute("end",tend);
        }*/

        /**/List<Object[]> temp=buildgraph(id, kind, difficulty, group, tstart, tend);
        JSONObject json_graph = extractJSONPerformance(temp);
        JSONObject json_bar = extractJSONSuccess(temp);
        model.addAttribute("patient", userController.findEntity(id).get());
        model.addAttribute("json_graph", json_graph);
        model.addAttribute("json_bar", json_bar);
        model.addAttribute("kind",kind);
        /*String[] parts = getItaNameKind(kind).split("-");
    	String mykind= parts[0]+parts[1]+parts[2];
        model.addAttribute("tipo",mykind);//getItaNameKind(kind));*/
        model.addAttribute("tipo",getItaNameKind(kind));
        model.addAttribute("group",group);
        model.addAttribute("difficulty",difficulty);
        model.addAttribute("difficolta",getItaNameDifficult(difficulty));
        model.addAttribute("start",tstart);
        model.addAttribute("end",tend);/**/
        return new ModelAndView("patientgraph");
    }


    @RequestMapping(value = "/patientbuildgraphoverview", method = RequestMethod.GET)
    public ModelAndView patientbuildgraphoverview(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "kind", required = true) String kind,
            @RequestParam(value = "difficulty", required = false, defaultValue = "easy") String difficulty,
            @RequestParam(value = "group", required = false, defaultValue = "day") String group,
            @RequestParam(value = "start", required = false, defaultValue = "") String tstart,
            @RequestParam(value = "end", required = false, defaultValue = "") String tend,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        int group_n=IntervalTime.daysfromgroup(group);

        IntervalTime it=new IntervalTime(group, tstart, tend);
        Long end=it.getEnd();
        Long start=it.getStart();

        int min = exerciseController.findMinIdFromType("attenzione");
        int max = exerciseController.findMaxIdFromType("attenzione");

        List<Object[]> temp1=historyController.findAllByTypeNoDifficulty(id,"attenzione",group_n,start,end);
        

        min = exerciseController.findMinIdFromType("memoria");
        max = exerciseController.findMaxIdFromType("memoria");

        //List<Object[]> temp2=historyController.findAllByTypeNoDifficulty(id,"memoria",group_n,start,end);
        List<Object[]> temp2=historyController.findAllByTypeNoDifficulty(id,"memoria",group_n,start,end);
        
        
        min = exerciseController.findMinIdFromType("funzioni esecutive");
        max = exerciseController.findMaxIdFromType("funzioni esecutive");
        //Non ho capito a cosa serva
        //List<Object[]> temp3=historyController.findAllByTypeNoDifficulty(id,"funzioni esecutive",group_n,start,end);
        List<Object[]> temp3=historyController.findAllByTypeNoDifficulty(id,"funzioni esecutive",group_n,start,end);
        
        
        JSONObject json_graph = extractJSONPerformance(temp1, temp2, temp3);
        //json_graph.append(extractJSONPerformance(temp2,temp22,temp23));
        
        //temp1.addAll(temp2);
        //temp1.addAll(temp3);
        //JSONObject json_bar = extractJSONSuccess(temp1);
        JSONObject json_bar = extractJSONSuccess(temp1, temp2, temp3);
        
        model.addAttribute("patient", userController.findEntity(id).get());
        model.addAttribute("json_graph", json_graph);
        model.addAttribute("json_bar", json_bar);
        model.addAttribute("kind",kind);
        model.addAttribute("tipo","Panoramica");
        model.addAttribute("group",group);
        model.addAttribute("difficulty",difficulty);
        model.addAttribute("start",tstart);
        model.addAttribute("end",tend);
        return new ModelAndView("patientgraphoverview");
    }

    @RequestMapping(value = "/patientbuildgraphsuccandfail", method = RequestMethod.GET)
    public ModelAndView patientbuildgraphsuccandfail(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "kind", required = true) String kind,
            @RequestParam(value = "difficulty", required = false, defaultValue = "easy") String difficulty,
            @RequestParam(value = "group", required = false, defaultValue = "day") String group,
            @RequestParam(value = "start", required = false, defaultValue = "") String tstart,
            @RequestParam(value = "end", required = false, defaultValue = "") String tend,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        int group_n=IntervalTime.daysfromgroup(group);

        IntervalTime it=new IntervalTime(group, tstart, tend);
        Long end=it.getEnd();
        Long start=it.getStart();

        int min = exerciseController.findMinIdFromType("attenzione");
        int max = exerciseController.findMaxIdFromType("attenzione");
        
        List<Object[]> temp1=historyController.findAllByTypeNoDifficulty(id,"attenzione",group_n,start,end);
        
        min = exerciseController.findMinIdFromType("memoria");
        max = exerciseController.findMaxIdFromType("memoria");

        List<Object[]> temp2=historyController.findAllByTypeNoDifficulty(id,"memoria",group_n,start,end);

        min = exerciseController.findMinIdFromType("funzioni esecutive");
        max = exerciseController.findMaxIdFromType("funzioni esecutive");
        //Non ho capito a cosa serva
        List<Object[]> temp3=historyController.findAllByTypeNoDifficulty(id,"funzioni esecutive",group_n,start,end);
        
        JSONObject json_bar = extractJSONSuccess(temp1, temp2, temp3);

        model.addAttribute("patient", userController.findEntity(id));
        //model.addAttribute("json_graph", json_graph);
        model.addAttribute("json_bar", json_bar);
        model.addAttribute("kind",kind);
        model.addAttribute("tipo","Successi/Fallimenti");
        model.addAttribute("group",group);
        model.addAttribute("difficulty",difficulty);
        model.addAttribute("start",tstart);
        model.addAttribute("end",tend);
        //return new ModelAndView("patientgraphoverview");
        return new ModelAndView("patientgraphsuccandfail");
    }


    public String getItaNameDifficult(String difficulty){
        if(difficulty.equals("easy")){
            	return "facile";
        	}
        else if(difficulty.equals("medium")){
                return "medio";
            }
        else if(difficulty.equals("difficult")){
                return "difficile";
            }
        else {
        		return "generale";
        	}
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



    @RequestMapping(value = "/groupgraph", method = RequestMethod.GET)
    public ModelAndView groupgraph(
            @RequestParam(value = "groupid", required = true) Integer groupid,
            HttpServletRequest request,
            Model model) {
        int group_n=86400;

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }

        Long end = LocalDateTime.now().toDateTime().getMillis();
        Calendar clndr = Calendar.getInstance();
        clndr.add(Calendar.DATE, -45);
        Long start = LocalDateTime.fromCalendarFields(clndr).toDateTime().getMillis();

        int min = 0;//exerciseController.findMinIdFromType("attenzione");
        //int max = 53;//exerciseController.findMaxIdFromType("memoria");
        int max = exerciseController.findMaxIdFromType("attenzione");
        if (exerciseController.findMaxIdFromType("memoria")>max) {
        	max = exerciseController.findMaxIdFromType("memoria");
        }
        if (exerciseController.findMaxIdFromType("funzioni esecutive")>max) {
        	max = exerciseController.findMaxIdFromType("funzioni esecutive");
        }
        // TODO MAKE IT MORE MODULAR

        MSRGroup group = groupController.findEntity(groupid).get();

        List<MSRUser> lu= userController.findAllPatientsInGroup(groupid);

        JSONArray ja=new JSONArray();
        JSONArray chiavi = new JSONArray();
        for(MSRUser U: lu){
            List<Object[]> temp1=historyController.findAllByExIdRangeNoDifficulty(U.getId(),min,max,group_n,start,end);
            ja = addJSONPerformance(U, ja, temp1);
            chiavi.put(U.getName()+" "+U.getSurname());
        }

        JSONObject j=new JSONObject();
        j.put("json",ja);
        //j.put("type","graph");
        JSONObject key=new JSONObject();
        key.put("value",chiavi);
        key.put("x","tempo");
        j.put("keys",key);

        model.addAttribute("group", group);
        model.addAttribute("json", j);
        return new ModelAndView("groupGraph");
    }

    @RequestMapping(value = "/groupoverviewjson", method = RequestMethod.GET, headers = "Accept=application/json" )
    public ResponseEntity<String> groupoverviewjson(
            @RequestParam(value = "groupid", required = true) Integer groupid,
            @RequestParam(value = "group", required = true) String group,
            @RequestParam(value = "start", required = true) String tstart,
            @RequestParam(value = "end", required = true) String tend,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }

        int group_n=IntervalTime.daysfromgroup(group);

        IntervalTime it=new IntervalTime(group, tstart, tend);
        Long end=it.getEnd();
        Long start=it.getStart();

        int min = 0;//exerciseController.findMinIdFromType("attenzione");
        //int max = 53;//exerciseController.findMaxIdFromType("memoria");
        int max = exerciseController.findMaxIdFromType("attenzione");
        if (exerciseController.findMaxIdFromType("memoria")>max) {
        	max = exerciseController.findMaxIdFromType("memoria");
        }
        if (exerciseController.findMaxIdFromType("funzioni esecutive")>max) {
        	max = exerciseController.findMaxIdFromType("funzioni esecutive");
        }
        //TODO make it modular


        MSRGroup G = groupController.findEntity(groupid).get();

        List<MSRUser> lu= userController.findAllPatientsInGroup(groupid);

        JSONArray ja=new JSONArray();
        JSONArray chiavi = new JSONArray();
        for(MSRUser U: lu){
            List<Object[]> temp1=historyController.findAllByExIdRangeNoDifficulty(U.getId(),min,max,group_n,start,end);
            ja = addJSONPerformance(U, ja, temp1);
            chiavi.put(U.getName()+" "+U.getSurname());
        }

        JSONObject j=new JSONObject();
        j.put("json",ja);
        JSONObject key=new JSONObject();
        key.put("value",chiavi);
        key.put("x","tempo");
        j.put("keys",key);

        model.addAttribute("group", G);
        //model.addAttribute("patient", lu.get(0));
        model.addAttribute("json", j);

        return new ResponseEntity<String>(j.toString(), headers,
                HttpStatus.OK);
    }


    @RequestMapping(value = "/patientoverviewjson", method = RequestMethod.GET, headers = "Accept=application/json" )
    public ResponseEntity<String> patientoverviewjson(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "group", required = true) String group,
            @RequestParam(value = "start", required = true) String tstart,
            @RequestParam(value = "end", required = true) String tend,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }

        int group_n=IntervalTime.daysfromgroup(group);


        IntervalTime it=new IntervalTime(group, tstart, tend);
        Long end=it.getEnd();
        Long start=it.getStart();

        int min = exerciseController.findMinIdFromType("attenzione");
        int max = exerciseController.findMaxIdFromType("attenzione");

        List<Object[]> temp1=historyController.findAllByTypeNoDifficulty(id,"attenzione",group_n,start,end);
        
        min = exerciseController.findMinIdFromType("memoria");
        max = exerciseController.findMaxIdFromType("memoria");

        List<Object[]> temp2=historyController.findAllByTypeNoDifficulty(id,"memoria",group_n,start,end);

        min = exerciseController.findMinIdFromType("funzioni esecutive");
        max = exerciseController.findMaxIdFromType("funzioni esecutive");
        //Qui "trova" effettivamente l'etichetta --> se cambi "funzioni esecutive" con qualcosa che non esiste si rompe
        List<Object[]> temp3=historyController.findAllByTypeNoDifficulty(id,"funzioni esecutive",group_n,start,end);

        JSONObject j = extractJSONPerformance(temp1, temp2, temp3);
        //boolean exploso = true;
        //JSONObject j= extractJSONPerformance(buildgraph2(id, group,"attenzione", tstart, tend));
        
        //temp1.addAll(temp2);
        //temp1.addAll(temp3);
        
        //JSONObject j2 = extractJSONSuccess(temp1);
        //JSONObject j2 = extractJSONSuccess(temp1, temp2, temp3);
        
        return new ResponseEntity<String>(j.toString(), headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/patientsuccandfailjson", method = RequestMethod.GET, headers = "Accept=application/json" )
    public ResponseEntity<String> patientsuccandfailjson(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "group", required = true) String group,
            @RequestParam(value = "start", required = true) String tstart,
            @RequestParam(value = "end", required = true) String tend,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }

        int group_n=IntervalTime.daysfromgroup(group);


        IntervalTime it=new IntervalTime(group, tstart, tend);
        Long end=it.getEnd();
        Long start=it.getStart();

        int min = exerciseController.findMinIdFromType("attenzione");
        int max = exerciseController.findMaxIdFromType("attenzione");

        List<Object[]> temp1=historyController.findAllByTypeNoDifficulty(id,"attenzione",group_n,start,end);
                
        min = exerciseController.findMinIdFromType("memoria");
        max = exerciseController.findMaxIdFromType("memoria");

        List<Object[]> temp2=historyController.findAllByTypeNoDifficulty(id,"memoria",group_n,start,end);

        min = exerciseController.findMinIdFromType("funzioni esecutive");
        max = exerciseController.findMaxIdFromType("funzioni esecutive");
        //Qui "trova" effettivamente l'etichetta --> se cambi "funzioni esecutive" con qualcosa che non esiste si rompe
        List<Object[]> temp3=historyController.findAllByTypeNoDifficulty(id,"funzioni esecutive",group_n,start,end);
        
        
        JSONObject j = extractJSONSuccess(temp1, temp2, temp3);

        return new ResponseEntity<String>(j.toString(), headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/patientgraphjson", method = RequestMethod.GET, headers = "Accept=application/json" )
    public ResponseEntity<String> patientgraphjson(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "kind", required = true) String kind,
            @RequestParam(value = "group", required = true) String group,
            @RequestParam(value = "start", required = true) String tstart,
            @RequestParam(value = "end", required = true) String tend,
            @RequestParam(value = "exploso", required = true) Boolean exploso,
            HttpServletRequest request,
            Model model) {
        if(exploso==null){
            exploso=false;
        }
        HttpHeaders headers = new HttpHeaders();
        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }

        JSONObject j= extractJSONPerformance(buildgraph(id, kind, difficulty, group, tstart, tend),exploso);

        return new ResponseEntity<String>(j.toString(), headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/patienthistogramjson", method = RequestMethod.GET, headers = "Accept=application/json" )
    public ResponseEntity<String> patienthistogramjson(
            @RequestParam(value = "patientid", required = true) Integer id,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "kind", required = true) String kind,
            @RequestParam(value = "group", required = true) String group,
            @RequestParam(value = "start", required = true) String tstart,
            @RequestParam(value = "end", required = true) String tend,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }
        JSONObject j = extractJSONSuccess(buildgraph(id, kind, difficulty, group, tstart, tend));

        return new ResponseEntity<String>(j.toString(), headers,
                HttpStatus.OK);
    }

    public JSONObject extractJSONPerformance(List<Object[]> temp){
        return extractJSONPerformance(temp, false);
    }

    public JSONObject extractJSONPerformance(List<Object[]> temp,boolean full){
        JSONArray ja = new JSONArray();
        for(Object[] h : temp){
            JSONObject t=new JSONObject();
            Date date=new Date(Long.parseLong(""+h[TIMESTAMP]));

            LocalDate localDate = new LocalDate(date);
            t.put("tempo",localDate.getYear()+"-"+localDate.getMonthOfYear()+"-"+localDate.getDayOfMonth());

            String s= ""+ h[PERFORMANCE];
            t.put("performance",((double)Math.round((Double.parseDouble(s))*100)));
            //t.put("performance",((double)Math.round((Double.parseDouble(s))*100)/100.0));
            //t.put("performance", 1);
            if(full){
            	double total = Double.parseDouble(""+ h[AVG_C])+Double.parseDouble(""+ h[AVG_M])+Double.parseDouble(""+ h[AVG_W]);
                t.put("risposte esatte",((double)Math.round((Double.parseDouble(""+ h[AVG_C]))*100/total)));
                //t.put("risposte esatte",((double)Math.round((Double.parseDouble(""+ h[AVG_C]))*100/total)/100));
                t.put("risposte omesse",((double)Math.round((Double.parseDouble(""+ h[AVG_M]))*100/total)));
                //t.put("risposte omesse",((double)Math.round((Double.parseDouble(""+ h[AVG_M]))*100/total)/100));
                t.put("risposte sbagliate",((double)Math.round((Double.parseDouble(""+ h[AVG_W]))*100/total)));
                //t.put("risposte sbagliate",((double)Math.round((Double.parseDouble(""+ h[AVG_W]))*100/total)/100));
                t.put("tempo impiegato",((double)Math.round((Double.parseDouble(""+ h[AVG_TIME]))/(Double.parseDouble(""+ h[MAXTIME]))*100)));
                //t.put("tempo impiegato",((double)Math.round((Double.parseDouble(""+ h[AVG_TIME]))/(Double.parseDouble(""+ h[MAXTIME]))*100)/100));
                //t.put("tempo massimo",((double)Math.round((Double.parseDouble(""+ h[MAXTIME])))));
            }
            ja.put(t);
        }

        JSONObject j=new JSONObject();
        j.put("json",ja);
        JSONObject key=new JSONObject();
        JSONArray chiavi = new JSONArray();

        if(full){
           chiavi.put("risposte esatte");
           chiavi.put("risposte omesse");
           chiavi.put("risposte sbagliate");
           chiavi.put("tempo impiegato");
           //chiavi.put("tempo massimo");

            /*JSONObject axes=new JSONObject();
            axes.put("avg_time","y2");
            j.put("axes",axes);*/
        }else{
            chiavi.put("performance");
        }
        key.put("value",chiavi);
        key.put("x","tempo");
        j.put("keys",key);

        return j;
    }

    
    
    public JSONObject extractJSONSuccess(List<Object[]> temp){
        JSONArray ja = new JSONArray();
        for(Object[] h : temp){
            JSONObject t=new JSONObject();
            Date date=new Date(Long.parseLong(""+h[TIMESTAMP]));

            LocalDate localDate = new LocalDate(date);
            t.put("tempo",localDate.getYear()+"-"+localDate.getMonthOfYear()+"-"+localDate.getDayOfMonth());

            Integer successi=Integer.parseInt( ""+ h[SUCCESS]);
            t.put("successi",successi);
            Integer fallimenti=Integer.parseInt( ""+ h[NPROVE])-successi;
            t.put("fallimenti",fallimenti);
            ja.put(t);
        }

        JSONObject j=new JSONObject();
        j.put("json",ja);
        j.put("type","bar");
        JSONArray group = new JSONArray();
        group.put("successi");
        group.put("fallimenti");
        JSONArray gg = new JSONArray();
        gg.put(group);
        j.put("groups",gg);
        JSONObject key=new JSONObject();
        JSONArray chiavi = new JSONArray();
        chiavi.put("successi");
        chiavi.put("fallimenti");
        key.put("value",chiavi);
        key.put("x","tempo");
        j.put("keys",key);

        return j;
    }

    public JSONObject extractJSONSuccess(List<Object[]> temp1, List<Object[]> temp2, List<Object[]> temp3){
    	JSONArray ja = new JSONArray();
    	/*List<Object[]> mtemp = new ArrayList<Object[]>(temp1);
    	mtemp.addAll(temp2);
    	mtemp.addAll(temp3);*/
    	//Integer successi=0, fallimenti=0;
        for(Object[] h : temp1){
            JSONObject t=new JSONObject();
            Date date=new Date(Long.parseLong(""+h[TIMESTAMP]));

            LocalDate localDate1 = new LocalDate(date);
            String tempo1 = localDate1.getYear()+"-"+localDate1.getMonthOfYear()+"-"+localDate1.getDayOfMonth();
            t.put("tempo",tempo1);
            
            //double totalprove=Integer.parseInt( ""+ h[NPROVE]);
            Integer successi=Integer.parseInt( ""+ h[SUCCESS]);
            t.put("successi",successi);
            Integer fallimenti=Integer.parseInt( ""+ h[NPROVE])-successi;
            t.put("fallimenti",fallimenti);
            for(Object[] m : temp2){
                Date date2 = new Date(Long.parseLong(""+m[TIMESTAMP]));

                LocalDate localDate2 = new LocalDate(date2);
                String tempo2 = localDate2.getYear()+"-"+localDate2.getMonthOfYear()+"-"+localDate2.getDayOfMonth();

                if(tempo1.equals(tempo2)){

                    t.put("tempo",tempo2);
                    //totalprove+=Integer.parseInt( ""+ m[NPROVE]);
                    successi+=Integer.parseInt( ""+ m[SUCCESS]);
                    Integer msuccessi = Integer.parseInt( ""+ m[SUCCESS]);
                    t.put("successi",successi);
                    fallimenti+=Integer.parseInt( ""+ m[NPROVE])-msuccessi;
                    t.put("fallimenti",fallimenti);
                }
            }
            for(Object[] f : temp3){
                Date date3 = new Date(Long.parseLong(""+f[TIMESTAMP]));

                LocalDate localDate3 = new LocalDate(date3);
                String tempo3 = localDate3.getYear()+"-"+localDate3.getMonthOfYear()+"-"+localDate3.getDayOfMonth();

                if(tempo1.equals(tempo3)){

                    t.put("tempo",tempo3);
                    //totalprove+=Integer.parseInt( ""+ f[NPROVE]);
                    successi+=Integer.parseInt( ""+ f[SUCCESS]);
                    Integer fsuccessi = Integer.parseInt( ""+ f[SUCCESS]);
                    t.put("successi",successi);
                    fallimenti+=Integer.parseInt( ""+ f[NPROVE])-fsuccessi;
                    t.put("fallimenti",fallimenti);
                }
            }
            /*double successi=tsuccessi/totalprove*100;
            double fallimenti=tfallimenti/totalprove*100;
            t.put("successi", successi);
            t.put("fallimenti", fallimenti);*/
            ja.put(t);
        }

        JSONObject j=new JSONObject();
        j.put("json",ja);
        j.put("type","bar");
        JSONArray group = new JSONArray();
        group.put("successi");
        group.put("fallimenti");
        JSONArray gg = new JSONArray();
        gg.put(group);
        j.put("groups",gg);
        JSONObject key=new JSONObject();
        JSONArray chiavi = new JSONArray();
        chiavi.put("successi");
        chiavi.put("fallimenti");
        key.put("value",chiavi);
        key.put("x","tempo");
        j.put("keys",key);

        return j;
    }

    
    public JSONObject extractJSONPerformanceGeneral(List<Object[]> att, String kind){
        JSONArray ja = new JSONArray();
        for(Object[] h : att){
            JSONObject t=new JSONObject();
            Date date=new Date(Long.parseLong(""+h[TIMESTAMP]));

            LocalDate localDate = new LocalDate(date);

            String tempo1=localDate.getYear()+"-"+localDate.getMonthOfYear()+"-"+localDate.getDayOfMonth();
            t.put("tempo",tempo1);

            String s= ""+ h[PERFORMANCE];
            t.put("attenzione",((double)Math.round((Double.parseDouble(s)))));

            ja.put(t);
        }

        JSONObject j=new JSONObject();
        j.put("json",ja);
        //j.put("type","bar");
        JSONObject key=new JSONObject();
        JSONArray chiavi = new JSONArray();
        chiavi.put(kind);
        //chiavi.put("memoria");
        key.put("value",chiavi);
        key.put("x","tempo");
        j.put("keys",key);

        return j;
    }

    public JSONObject extractJSONPerformance(List<Object[]> att,List<Object[]> mem,List<Object[]> fnz){
        JSONArray ja = new JSONArray();
        for(Object[] h : att){
            JSONObject t=new JSONObject();
            Date date=new Date(Long.parseLong(""+h[TIMESTAMP]));

            LocalDate localDate = new LocalDate(date);

            String tempo1=localDate.getYear()+"-"+localDate.getMonthOfYear()+"-"+localDate.getDayOfMonth();
            t.put("tempo",tempo1);

            String s= ""+ h[PERFORMANCE];
            //String bla= ""+h[DIFFICULTY];
            
            t.put("attenzione",((double)(Math.round(Double.parseDouble(s)*100))));
            //t.put("attenzione",((double)(Math.round(Double.parseDouble(s)*100)/100.0)));

            for(Object[] m : mem){
                Date date2 = new Date(Long.parseLong(""+m[TIMESTAMP]));

                LocalDate localDate2 = new LocalDate(date2);
                String tempo2 = localDate2.getYear()+"-"+localDate2.getMonthOfYear()+"-"+localDate2.getDayOfMonth();

                if(tempo1.equals(tempo2)){

                    t.put("tempo",tempo2);
                    String s2= ""+ m[PERFORMANCE];
                   // if ()
                   //JSONObject myj = ((JSONObject)h.get(i)).get("tempo") 
                    t.put("memoria",((double)(Math.round(Double.parseDouble(s2)*100))));
                    //t.put("memoria",((double)(Math.round(Double.parseDouble(s2)*100)/100.0)));
                }
            }
            for(Object[] f : fnz){
                Date date3 = new Date(Long.parseLong(""+f[TIMESTAMP]));

                LocalDate localDate3 = new LocalDate(date3);
                String tempo3 = localDate3.getYear()+"-"+localDate3.getMonthOfYear()+"-"+localDate3.getDayOfMonth();

                if(tempo1.equals(tempo3)){

                    t.put("tempo",tempo3);
                    String s3= ""+ f[PERFORMANCE];
                    //Qui invece "cerca" "funzioni esecutive" come label creata in precedenza, se non la trova non disegna
                    //il grafico delle funzioni esecutive ma lascia la label
                    t.put("funzioni esecutive",((double)(Math.round(Double.parseDouble(s3)*100))));
                    //t.put("funzioni esecutive",((double)(Math.round(Double.parseDouble(s3)*100)/100.0)));
                }
            }
            ja.put(t);
        }

        for(Object[] m : mem){
            Date date2 = new Date(Long.parseLong(""+m[TIMESTAMP]));

            LocalDate localDate2 = new LocalDate(date2);
            String tempo = localDate2.getYear()+"-"+localDate2.getMonthOfYear()+"-"+localDate2.getDayOfMonth();

            boolean trovato = false;
            for(int i=0;i<ja.length();i++){
               if(tempo.equals(((JSONObject)ja.get(i)).get("tempo"))){
                   trovato=true;
                }
            }
            if(!trovato){
                JSONObject t=new JSONObject();
                t.put("tempo",tempo);
                String s2= ""+ m[PERFORMANCE];
                t.put("memoria",((double)(Math.round(Double.parseDouble(s2)*100))));
                //t.put("memoria",((double)(Math.round(Double.parseDouble(s2)*100)/100.0)));
                for(Object[] f : fnz){
                    Date date3 = new Date(Long.parseLong(""+f[TIMESTAMP]));

                    LocalDate localDate3 = new LocalDate(date3);
                    String tempo3 = localDate3.getYear()+"-"+localDate3.getMonthOfYear()+"-"+localDate3.getDayOfMonth();

                    if(tempo.equals(tempo3)){

                        t.put("tempo",tempo3);
                        String s3= ""+ f[PERFORMANCE];
                        //Qui invece "cerca" "funzioni esecutive" come label creata in precedenza, se non la trova non disegna
                        //il grafico delle funzioni esecutive ma lascia la label
                        t.put("funzioni esecutive",((double)(Math.round(Double.parseDouble(s3)*100))));
                        //t.put("funzioni esecutive",((double)(Math.round(Double.parseDouble(s3)*100)/100.0)));
                    }
                }
                ja.put(t);
            }
            
        }

        for(Object[] f : fnz){
            Date date3 = new Date(Long.parseLong(""+f[TIMESTAMP]));

            LocalDate localDate3 = new LocalDate(date3);
            String tempo = localDate3.getYear()+"-"+localDate3.getMonthOfYear()+"-"+localDate3.getDayOfMonth();

            boolean trovato = false;
            for(int i=0;i<ja.length();i++){
               if(tempo.equals(((JSONObject)ja.get(i)).get("tempo"))){
                   trovato=true;
                }
            }
            if(!trovato){
                JSONObject t=new JSONObject();
                t.put("tempo",tempo);
                String s3= ""+ f[PERFORMANCE];
                // Questo non serve a nulla?
                t.put("funzioni esecutive",((double)(Math.round(Double.parseDouble(s3)*100))));
                //t.put("funzioni esecutive",((double)(Math.round(Double.parseDouble(s3)*100)/100.0)));
                ja.put(t);
            }

        }

        JSONObject j=new JSONObject();
        j.put("json",ja);
        //j.put("type","bar");
        JSONObject key=new JSONObject();
        JSONArray chiavi = new JSONArray();
        chiavi.put("attenzione");
        chiavi.put("memoria");
        // Qui crea effettivamente la label --> se non la chiamo "funzioni esecutive" segna la label ma non trova i dati per
        // tracciare il grafico
        chiavi.put("funzioni esecutive");
        key.put("value",chiavi);
        key.put("x","tempo");
        j.put("keys",key);

        return j;
    }


    public JSONArray addJSONPerformance(MSRUser user,JSONArray ja, List<Object[]> att){
        logger.info("Abbiamo trovato ben:"+att.size());
        for(Object[] h : att){
            Date date=new Date(Long.parseLong(""+h[TIMESTAMP]));

            LocalDate localDate = new LocalDate(date);

            String tempo1=localDate.getYear()+"-"+localDate.getMonthOfYear()+"-"+localDate.getDayOfMonth();


            String s= ""+ h[PERFORMANCE];
            String key=user.getName()+" "+user.getSurname();

            boolean find=false;
            for(int i=0;i<ja.length();i++){
                JSONObject m = ja.getJSONObject(i);

                String tempo2=m.getString("tempo");

                if(tempo1.equals(tempo2)){
                   m.put(key,(double)Math.round((Double.parseDouble(s))*100));
                   //m.put(key,(double)Math.round((Double.parseDouble(s))*100)/100);
                   find=true;
                }
            }
            if(!find){
                JSONObject t=new JSONObject();
                t.put("tempo",tempo1);
                t.put(key,(double)Math.round((Double.parseDouble(s))*100));
                //t.put(key,(double)Math.round((Double.parseDouble(s))*100)/100);
                ja.put(t);
            }
        }

        return ja;
    }



    public List<Object[]> buildgraph(int userid,String kind,String difficulty,String group,String tstart,String tend){
        int group_n=IntervalTime.daysfromgroup(group);

        IntervalTime it=new IntervalTime(group, tstart, tend);
        Long end=it.getEnd();
        Long start=it.getStart();

        /*
        String default_graph[]=new String[]{"attenzione","memoria","funzioni esecutive"};
        int min=0;
        int max = 34;
        if(Arrays.asList(default_graph).indexOf(kind)!=-1){
            min = exerciseController.findMinIdFromType(kind);
            max = exerciseController.findMaxIdFromType(kind);
        }else{
            min = exerciseController.findMinIdFromCategory(Exercise.ExerciseCategoryValue.valueOf(kind));
            max = exerciseController.findMaxIdFromCategory(Exercise.ExerciseCategoryValue.valueOf(kind));
        }*/
        List<Object[]> temp;
        //Questa boh, non ho capito a cosa serva
        String[] opt=new String[]{"attenzione","memoria","funzioni esecutive"};
        if(!Arrays.asList(opt).contains(kind)){
        	Exercise.ExerciseCategoryValue category = Exercise.ExerciseCategoryValue.valueOf(kind);
        	if (difficulty.equals("general")) {
            	temp=historyController.findAllByCategoryNoDifficulty(userid,category,group_n,start,end);
            }
        	else {
            //Exercise.ExerciseCategoryValue category = Exercise.ExerciseCategoryValue.valueOf(kind);
            temp = historyController.findAllByCategory(userid,category,difficulty,group_n,start,end);
        	}
        }else
        	if (difficulty.equals("general")) {
            	temp=historyController.findAllByTypeNoDifficulty(userid,kind,group_n,start,end);
            }
        	else {
        		temp=historyController.findAllByType(userid,kind,difficulty,group_n,start,end); //TODO EDIT
        	}
        return temp;
    }
}
