/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.ChangeDifficulty;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.History;
import it.unibo.msrehab.model.entities.MSRGroup;
import it.unibo.msrehab.model.entities.MSRSession;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.model.controller.ChangeDifficultyController;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.FitnessWeightController;
import it.unibo.msrehab.model.controller.HistoryController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.util.CookiesManager;
import it.unibo.msrehab.util.WebPagesUtilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class ExerciseLiveService {

    private static final Logger logger = LoggerFactory
            .getLogger(ExerciseService.class);
    private final HistoryController historyController;
    private final Configuration config;
    private final FitnessWeightController fitnessController;
    private final ExerciseController exerciseController;
    private final MSRSessionController sessionController;
    private final ChangeDifficultyController changeDiffController;

    private final MSRUserController userController;
    private final MSRGroupController groupController;

    public static final int MAX_DIFFICULTY_LEVEL = 10;
    public static final int MIN_DIFFICULTY_LEVEL = 1;

    public enum SessionValue {
        easy, medium, hard;
    };

    public ExerciseLiveService() {
        super();
        this.historyController = new HistoryController();
        this.fitnessController = new FitnessWeightController();
        this.userController = new MSRUserController();
        this.exerciseController = new ExerciseController();
        this.sessionController = new MSRSessionController();
        this.changeDiffController = new ChangeDifficultyController();
        this.groupController = new MSRGroupController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
    }

    public String getItaNameKind(String kind) {
        try {
            Exercise.ExerciseCategoryValue q = Exercise.ExerciseCategoryValue.valueOf(kind);
            String s = exerciseController.getExerciseFullName(q);
            return s;
        } catch (IllegalArgumentException e) {

        }
        return kind.substring(0, 1).toUpperCase() + kind.substring(1, kind.length()).toLowerCase();
    }

    private String findName(int id, List<Exercise> es) {
        for (Exercise e : es) {
            if (id == e.getId()) {
                //return getItaNameKind("" + e.getCategory());
                return e.getFullName();
            }
        }
        return "";
    }

    private int findExid(int id, List<Exercise> es) {
        for (Exercise e : es) {
            if (id == e.getId()) {
                return e.getId();
            }
        }
        return -1;
    }

    @RequestMapping(value = "/showlivelist", method = RequestMethod.GET)
    public ModelAndView showlivelist(
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        } else {
            int cid = CookiesManager.getLoggedUserCenter(request);
            model.addAttribute("patients", userController.findAllPatientsInCenter(cid));
            return new ModelAndView("patientlivelist");
        }
    }

    @RequestMapping(value = "/showgrouplivelist", method = RequestMethod.GET)
    public ModelAndView showgrouplivelist(
            HttpServletRequest request,
            Model model) {
        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        } else {
            int cid = CookiesManager.getLoggedUserCenter(request);

            model.addAttribute("groups", groupController.findAllGroupsInCenter(cid));
            return new ModelAndView("grouplivelist");
        }
    }

    @RequestMapping(value = "/patientlive", method = RequestMethod.GET)
    public ModelAndView patientlive(
            @RequestParam(value = "id", required = true) Integer patientid,
            HttpServletRequest request,
            Model model) {
        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }
        MSRUser patient = userController.findEntity(patientid).orElse(null);


        if(patient == null)
            return new ModelAndView("/error")
                    .addObject("message", "Patient not found");


        List<Exercise> es = exerciseController.getAllEntities();

        List<History> lh = new ArrayList<>(); //historyController.findAllByUser(patientid);
        JSONArray jhistory = new JSONArray();

        jhistory = loadFilterHistory(lh, es, jhistory, patient);


        model.addAttribute("exercise", es);
        model.addAttribute("history", jhistory);
        model.addAttribute("patient", patient);
        return new ModelAndView("patientlive");
    }

    @RequestMapping(value = "/patientlivejson", method = RequestMethod.GET)
    public ResponseEntity<String> patientlivejson(
            @RequestParam(value = "id", required = true) Integer patientid,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();
        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                    HttpStatus.UNAUTHORIZED);
        }
        MSRUser patient = userController.findEntity(patientid).get();

        List<Exercise> es;
        es = exerciseController.getAllEntities();

        List<History> lh = new ArrayList<History>(); //historyController.findAllByUser(patientid);
        JSONArray jhistory = new JSONArray();

        jhistory = loadFilterHistory(lh, es, jhistory, patient);

        return new ResponseEntity<>(jhistory.toString(), headers,
                HttpStatus.OK);
    }

    private JSONArray loadFilterHistory(List<History> lh, List<Exercise> es, JSONArray jhistory, MSRUser patient)
    {
        Integer patientid = patient.getId();
        List<MSRSession> sessions = sessionController.findAllActiveByUserOrGroup(patientid);
        if (sessions.isEmpty())
            return new JSONArray();


        MSRSession session = sessions.get(0);
        String json = session.getExercises();
        JSONArray js = new JSONArray(json);
        for (int i = 0; i < js.length(); i++)
        {
            JSONObject o = js.getJSONObject(i);
            if (!o.getBoolean("done")) {
                List<History> lhe = historyController.findAllByUserAndExerciseAndSessid(patientid, o.getInt("id"), session.getId());
                if (!lhe.isEmpty()) {
                    List<ChangeDifficulty> cdlist = changeDiffController.findFromHistory(lhe.get(0).getId());//FIXME PLEASE

                    lh.add(lhe.get(0));
                    JSONObject ob = new JSONObject();
                    ob.put("id", lhe.get(0).getExid());
                    ob.put("historyid", lhe.get(0).getId());
                    ob.put("performance", lhe.get(0).getRelperformance());
                    ob.put("difficulty", lhe.get(0).getDifficulty());
                    ob.put("name", findName(lhe.get(0).getExid(), es));
                    ob.put("username", patient.getName() + " " + patient.getSurname());
                    ob.put("time", lhe.get(0).getTimestamp());
                    if ((cdlist != null) && (!cdlist.isEmpty())) {
                        ob.put("level", cdlist.get(0).getLevel());
                        ob.put("changed", true);
                        ob.put("oldlevel", lhe.get(0).getLevel());
                    } else {
                        ob.put("level", lhe.get(0).getLevel());
                        ob.put("changed", false);
                        ob.put("oldlevel", lhe.get(0).getLevel());
                    }

                    int exid = lhe.get(0).getExid();
                    int maxdiff = exerciseController.findEntity(exid).get().getMaxLevel();
                    ob.put("maxdiff", maxdiff);

                    if (lhe.size() > 1) {
                        if (lhe.get(0).getRelperformance() > lhe.get(1).getRelperformance()) {
                            ob.put("direction", 1);
                        }
                        if (lhe.get(0).getRelperformance() < lhe.get(1).getRelperformance()) {
                            ob.put("direction", -1);
                        }
                        if (Double.compare(lhe.get(0).getRelperformance(), lhe.get(1).getRelperformance()) == 0) {
                            ob.put("direction", 0);
                        }
                    }
                    jhistory.put(ob);

                }
                else{
                    //lhe = historyController.findAllByUserAndExerciseAndSessid(patientid, o.getInt("id"), session.getId());
                    System.out.println("entering");
                    //System.out.println("Exercises: "+js);
                    //System.out.println("id: "+ o.getInt("id"));
                    List<ChangeDifficulty> cdlist = changeDiffController.findFromHistory(o.getInt("id"));//FIXME PLEASE
                    History myRecord = new History();

                    //myRecord.setId(patientid+findExid(o.getInt("id"), es));
                    myRecord.setUserid(patientid);
                    myRecord.setExid(findExid(o.getInt("id"), es));
                    myRecord.setSessid(session.getId());
                    myRecord.setAbsperformance(0.0);
                    myRecord.setRelperformance(0.0);
                    myRecord.setPassed(false);
                    long tmstmp=0;
                    myRecord.setTimestamp(tmstmp);
                    myRecord.setpTime(0.0);
                    myRecord.setpCorrect(0);
                    myRecord.setpWrong(0);
                    myRecord.setpMissed(0);
                    myRecord.setLevel(1);
                    myRecord.setDifficulty("easy");
                    myRecord.setMaxtime(0.0);

                    historyController.insertEntity(myRecord);

                    //lh.add(o.getInt("id"));
                    JSONObject ob = new JSONObject();
                    ob.put("id", myRecord.getExid());//o.getInt("id"));
                    ob.put("historyid", myRecord.getId());
                    ob.put("performance", myRecord.getRelperformance());
                    ob.put("difficulty", myRecord.getDifficulty());
                    ob.put("name", findName(myRecord.getExid(), es));
                    ob.put("username", patient.getName() + " " + patient.getSurname());
                    ob.put("time", myRecord.getpTime());
                    ob.put("level", 1);
                    ob.put("changed", false);
                    ob.put("oldlevel", 1);
                    /*if ((cdlist != null) && (!cdlist.isEmpty())) {
                        ob.put("level", 1);
                        ob.put("changed", false);
                        ob.put("oldlevel", 1);
                    } else {
                        ob.put("level", 1);
                        ob.put("changed", false);
                        ob.put("oldlevel", 1);
                    }*/

                    int exid = o.getInt("id");
                    int maxdiff = exerciseController.findEntity(exid).get().getMaxLevel();
                    ob.put("maxdiff", maxdiff);

                    //if (lhe.size() > 1) {
                    //    if (lhe.get(0).getRelperformance() > lhe.get(1).getRelperformance()) {
                    //        ob.put("direction", 1);
                    //    }
                    //    if (lhe.get(0).getRelperformance() < lhe.get(1).getRelperformance()) {
                    //        ob.put("direction", -1);
                    //    }
                    //    if (Double.compare(lhe.get(0).getRelperformance(), lhe.get(1).getRelperformance()) == 0) {
                    //        ob.put("direction", 0);
                    //    }
                    //}
                    jhistory.put(ob);
                }
            }
        }
        return jhistory;
    }

    @RequestMapping(value = "/grouplive", method = RequestMethod.GET)
    public ModelAndView grouplive(
            @RequestParam(value = "id", required = true) Integer groupid,
            HttpServletRequest request,
            Model model) {

        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ModelAndView("login");
        }
        MSRGroup group = groupController.findEntity(groupid).get();

        List<Exercise> es;
        es = exerciseController.getAllEntities();

        List<History> lh = new ArrayList<History>(); //historyController.findAllByUser(patientid);
        JSONArray jhistory = new JSONArray();

        List<MSRUser> lu = userController.findAllPatientsInGroup(groupid);

        for (MSRUser U : lu) {
            jhistory = loadFilterHistory(lh, es, jhistory, U);
        }

        model.addAttribute("exercise", es);
        model.addAttribute("history", jhistory);
        model.addAttribute("group", group);
        return new ModelAndView("grouplive");
    }

    @RequestMapping(value = "/patientgrouplivejson", method = RequestMethod.GET)
    public ResponseEntity<String> patientgrouplivejson(
            @RequestParam(value = "id", required = true) Integer groupid,
            HttpServletRequest request,
            Model model) {
        HttpHeaders headers = new HttpHeaders();
        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                    HttpStatus.UNAUTHORIZED);
        }
        MSRGroup group = groupController.findEntity(groupid).get();

        List<Exercise> es;
        es = exerciseController.getAllEntities();

        List<History> lh = new ArrayList<History>(); //historyController.findAllByUser(patientid);
        JSONArray jhistory = new JSONArray();

        List<MSRUser> lu = userController.findAllPatientsInGroup(groupid);

        for (MSRUser U : lu) {
            jhistory = loadFilterHistory(lh, es, jhistory, U);
        }

        return new ResponseEntity<String>(jhistory.toString(), headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/changelevel", method = RequestMethod.POST)
    public ResponseEntity<String> changelevel(
            @RequestParam(value = "historyid", required = true) Integer historyid,
            @RequestParam(value = "level", required = true) Integer level,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();

        if (WebPagesUtilities.redirectIfNotLogged(request) || ((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                    HttpStatus.UNAUTHORIZED);
        }

        Integer exid = historyController.findEntity(historyid)
                .map(History::getExid)
                .orElse(null);
        Integer maxdiff = exerciseController.findEntity(exid)
                .map(Exercise::getMaxLevel)
                .orElse(null);

        if(exid == null || maxdiff == null)
            return new ResponseEntity<>(headers,HttpStatus.BAD_REQUEST);

        if (level < MIN_DIFFICULTY_LEVEL) {
            level = MIN_DIFFICULTY_LEVEL;
        } else if (level > maxdiff)
                level = maxdiff;


        List<ChangeDifficulty> cdl = changeDiffController.findFromHistory(historyid);
        if ((cdl != null) && (!cdl.isEmpty())) {
            ChangeDifficulty cd = cdl.get(0);
            cd.setLevel(level);
            if (!changeDiffController.updateEntity(cd)) {
                logger.error("Error updating ChangeDifficulty to DB");
                return new ResponseEntity<>(headers,HttpStatus.SERVICE_UNAVAILABLE);
            }
        }
        else {
            ChangeDifficulty cd = new ChangeDifficulty();
            cd.setHistoryid(historyid);
            cd.setLevel(level);
            if (!changeDiffController.insertEntity(cd))
                return new ResponseEntity<>(headers,HttpStatus.SERVICE_UNAVAILABLE);

        }

        return new ResponseEntity<>(level + "/" + maxdiff, headers, HttpStatus.OK);

    }
}
