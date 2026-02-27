/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.ChangeDifficulty;
import it.unibo.msrehab.model.entities.History;
import it.unibo.msrehab.model.controller.PDDLGeneratorZooMap;
import it.unibo.msrehab.model.controller.ChangeDifficultyController;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.FitnessWeightController;
import it.unibo.msrehab.model.controller.HistoryController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDateTime;
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
 * @author Bartolomeo Lombardi
 */
@Controller
public class PddlZooMapService {

    private static final Logger logger = LoggerFactory
            .getLogger(ExerciseService.class);
    private final HistoryController historyController;
    private final Configuration config;
    private final FitnessWeightController fitnessController;
    private final ExerciseController exerciseController;
    private final MSRSessionController sessionController;

    private final MSRUserController userController;
    private final MSRGroupController groupController;
    private final ChangeDifficultyController changeDiffController;

    private static final int EASY_MIN = 1;
    private static final int MEDIUM_MIN = 4;
    private static final int DIFFICULT_MIN = 7;

    private List<String> PDDLsolution;
    private List<String> listGoals;

    private HashMap<Integer, Integer> targetGoals = new HashMap<>();

    public PddlZooMapService() {
        super();
        this.historyController = new HistoryController();
        this.fitnessController = new FitnessWeightController();
        this.userController = new MSRUserController();
        this.exerciseController = new ExerciseController();
        this.sessionController = new MSRSessionController();
        this.groupController = new MSRGroupController();
        this.changeDiffController = new ChangeDifficultyController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);

    }

    @RequestMapping(value = "/pianificazione2", method = RequestMethod.GET)
    public ModelAndView pianificazione2(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model) {

        logger.debug("pianificazion21()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("pianificazione2");
    }
    
    @RequestMapping(value = "/pianificazione2phase1", method = RequestMethod.GET)
    public ModelAndView pianificazione2(
            HttpServletRequest request,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = false, defaultValue = "1") Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "color", required = true, defaultValue = "true") Boolean color,
            Model model) {

        Random r = new Random();
        if (difficulty.equals("training") || difficulty.equals("demo")) {
            level = 1;
        }
        else {
            List<History> historyList = historyController.findAllByUserAndExerciseAndSessid(patientid, exerciseid, sessid);
            if (historyList.isEmpty()) {
                if (difficulty.equals("easy")) {
                    level = EASY_MIN;
                }
                if (difficulty.equals("medium")) {
                    level = MEDIUM_MIN;
                    //color = r.nextBoolean();
                }
                if (difficulty.equals("difficult")) {
                    level = DIFFICULT_MIN;
                    //color = false;
                   // color = r.nextBoolean(); // color or bw are chosen with equal probability
                }
            }
            else {
                level = historyList.get(0).getLevel();
                if(historyList.size()>=2
                        && historyList.get(0).getPassed().equals(true)&&historyList.get(1).getPassed().equals(true)
                        && historyList.get(0).getLevel().equals(historyList.get(1).getLevel())) {    
                    level=historyList.get(0).getLevel()+1;
                }
                List<ChangeDifficulty> cdl = changeDiffController.findFromHistory(historyList.get(0).getId());
                if ((cdl != null) && (!cdl.isEmpty())) {
                    level = cdl.get(0).getLevel();
                }
                if (level >= EASY_MIN && level < MEDIUM_MIN) {
                    difficulty = "easy";
                    color=true;
                }
                else if (level >= MEDIUM_MIN && level < DIFFICULT_MIN) {
                    difficulty="medium";
                    //color=r.nextBoolean();
                }
                else { // level >= MAXIMUM_MIN
                    difficulty = "difficult";
                   // color = r.nextBoolean();
                }
            }
        }

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("idproblem", 1);
        model.addAttribute("color", color);
        return new ModelAndView("pddl-pianificazione2a");
    }

    @RequestMapping(value = "/pianificazione2phase2", method = RequestMethod.GET)
    public ModelAndView pianificazione2(
            HttpServletRequest request,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "idproblem", required = true) Integer idproblem,
            @RequestParam(value = "color", required = false, defaultValue = "false") boolean color,
            Model model) {

        listGoals = new ArrayList<>();
        PDDLsolution = new ArrayList<>();
        StringBuilder inFinalPlace = new StringBuilder();
        int[][] gameMap = null;

        // definisco per ogni livello il numero di target da visitare
        targetGoals.put(1, 2); //easy min
        targetGoals.put(2, 4);
        targetGoals.put(3, 6); //easy max
        targetGoals.put(4, 2); //medimum min
        targetGoals.put(5, 4);
        targetGoals.put(6, 6); //medimum max
        targetGoals.put(7, 2); //difficult min
        targetGoals.put(8, 4);
        targetGoals.put(9, 6);
        targetGoals.put(10, 7); //difficult max
        //targetGoals.put(11, 7);
        //targetGoals.put(12, 7);
        //targetGoals.put(13, 7);
        //targetGoals.put(14, 7);
        //targetGoals.put(15, 7);

        //cambio mappa in base alla difficolta
        switch (difficulty) {
            case "easy":
                gameMap = PDDLGeneratorZooMap.easyMapSafariZoo(listGoals, inFinalPlace, PDDLsolution, targetGoals.get(level));
                break;

            case "medium":
                gameMap = PDDLGeneratorZooMap.mediumMapSafariZoo(listGoals, inFinalPlace, PDDLsolution, targetGoals.get(level));
                break;

            case "difficult":
                gameMap = PDDLGeneratorZooMap.difficultMapSafariZoo(listGoals, inFinalPlace, PDDLsolution, targetGoals.get(level));
                break;

            case "training":
                gameMap = PDDLGeneratorZooMap.easyMapSafariZoo(listGoals, inFinalPlace, PDDLsolution, targetGoals.get(level));
                break;

            case "demo":
                gameMap = PDDLGeneratorZooMap.easyMapSafariZoo(listGoals, inFinalPlace, PDDLsolution, targetGoals.get(level));
                break;

            default:
                break;
        }

        request.setAttribute("gameMap", gameMap);
        request.setAttribute("listGoals", listGoals);
        request.setAttribute("inFinalPlace", inFinalPlace.toString());
        request.setAttribute("parameters", PDDLsolution);

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("idproblem", idproblem);
        model.addAttribute("color", color);
        return new ModelAndView("pddl-pianificazione2b");
    }

    private History calculateFitness(String[] actions, Double pTime, String difficulty, int idproblem, boolean passed, int patientid, int exerciseid, int sessid) {

        int nstep = PDDLsolution.size();
        List<String> actionsList = Arrays.asList(actions);

        int pCorrect = 0;
        int pMissed = 0;
        int pWrong = 0;

        if (passed) {
            pCorrect = PDDLsolution.size();
            if (actionsList.size() - PDDLsolution.size() > 0) {
                pWrong = actionsList.size() - PDDLsolution.size();
            }
        } else {
            for (String action : actionsList) {
                if (PDDLsolution.contains(action)) {
                    ++pCorrect;
                } else {
                    ++pWrong;
                }
            }

            pMissed = PDDLsolution.stream().filter((solutionAction) -> (!actionsList.contains(solutionAction))).map((_item) -> 1).reduce(pMissed, Integer::sum);
        }

        //List<History> lastHistory = historyController.findAllByUserAndExerciseAndSessid(patientid, exerciseid, sessid);
        //int newLevel = ExerciseService.findChangedLevel(changeDiffController, lastHistory);
        History history = new History();
        history.setExid(exerciseid);
        history.setSessid(sessid);
        history.setUserid(patientid);
        history.setPassed(passed);
        history.setpTime(pTime);
        history.setDifficulty(difficulty);
        history.setpCorrect(pCorrect);
        history.setpMissed(pMissed);
        history.setpWrong(pWrong);
        history.setMaxtime((double) nstep * 20);    // 20 secs per action

        // Calculate performance
        double ft = Double.NaN;
        ft = fitnessController.calculateFitness(false, history);
        history.setAbsperformance(ft);
        Long t = LocalDateTime.now().toDateTime().getMillis();
        history.setTimestamp(t);

        /*
         history.setLevel(level);
         history.setDifficulty(difficulty);
         history.setpTime(pTime);*/
        return history;
    }

    @RequestMapping(value = "/getzooperformance", method = RequestMethod.GET)
    public ResponseEntity<String> getproblemperformance(
            HttpServletRequest request,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "idproblem", required = true) Integer idproblem,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "actions", required = true) String[] actions,
            @RequestParam(value = "color", required = false, defaultValue = "false") boolean color,
            Model model) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println("Entrato nella richiesta");

        History h = calculateFitness(actions, pTime, difficulty, idproblem, passed, patientid, exerciseid, sessid);
        String html = "<p>Performance: " + (Math.floor(h.getAbsperformance() * 100)) + " % </p>";

        if (Boolean.FALSE.equals(passed)) {

            List<String> unsolved = new ArrayList<>();
            List<String> actionsList = Arrays.asList(actions);

            for (String goal : listGoals) {
                if (!actionsList.contains(goal)) {
                    unsolved.add(goal);
                }
            }

            if (unsolved.size() > 0) {
                html += "<h4>Obiettivi non risolti:</h4><ul>";
                for (String unsolved1 : unsolved) {
                    html += "<li>" + unsolved1 + "</li>";
                }
                html += "</ul>";
            }
        } else {
            System.out.println("esercizio superato!");
        }

        return new ResponseEntity<String>(html, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/pianificazione2phase3", method = RequestMethod.GET)
    public ModelAndView pianificazione3(
            HttpServletRequest request,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "idproblem", required = true) Integer idproblem,
            @RequestParam(value = "passed", required = true) Boolean passed,
            @RequestParam(value = "pTime", required = true) Double pTime,
            @RequestParam(value = "actions", required = true) String[] actions,
            @RequestParam(value = "color", required = false, defaultValue = "false") boolean color,
            Model model) {

        if (patientid == -1) {
            if (difficulty.equals("training")) {
                return new ModelAndView("redirect: patienttraining");
            } else {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        } else {
            if (difficulty.equals("training") || difficulty.equals("demo")) {
                return new ModelAndView("redirect: patientrehabilitation");
            }

            int nstep = PDDLsolution.size();
            List<String> actionsList = Arrays.asList(actions);

            int pCorrect = 0;
            int pMissed = 0;
            int pWrong = 0;

            if (passed) {
                pCorrect = PDDLsolution.size();
                if (actionsList.size() - PDDLsolution.size() > 0) {
                    pWrong = actionsList.size() - PDDLsolution.size();
                }
            } else {

                for (String action : actionsList) {
                    if (PDDLsolution.contains(action)) {
                        ++pCorrect;
                    } else {
                        ++pWrong;
                    }
                }

                for (String solutionAction : PDDLsolution) {
                    if (!actionsList.contains(solutionAction)) {
                        ++pMissed;
                    }
                }
            }

            //List<History> lastHistory = historyController.findAllByUserAndExerciseAndSessid(patientid, exerciseid, sessid);
            //int newLevel = ExerciseService.findChangedLevel(changeDiffController,lastHistory);
            History history = new History();
            history.setExid(exerciseid);
            history.setPassed(passed);
            history.setUserid(patientid);
            history.setpTime(pTime);
            history.setpCorrect(pCorrect);
            history.setpMissed(pMissed);
            history.setpWrong(pWrong);
            history.setMaxtime((double) nstep * 20);    // 20 secs per action
            //history.setDifficulty(session);

            history.setDifficulty(difficulty);
            
            // Calculate performance
            double ft = Double.NaN;
            ft = fitnessController.calculateFitness(false, history);
            history.setAbsperformance(ft);
            ft = fitnessController.calculateFitness(true, history);
            history.setRelperformance(ft);

            Long t = LocalDateTime.now().toDateTime().getMillis();
            history.setTimestamp(t);
            history.setLevel(level);
            history.setSessid(sessid);

            if (!historyController.insertEntity(history)) {
                logger.error("Error adding History to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }

        }
        //pianificazione1phase1
        String url = "/pianificazione2phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&color=" + color
                + "&sessid=" + sessid;

        return new ModelAndView("redirect:" + url);
    }
}
