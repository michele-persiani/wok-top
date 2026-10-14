package it.unibo.msrehab.services;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.entities.ChangeDifficulty;
import it.unibo.msrehab.model.entities.History;
import it.unibo.msrehab.model.entities.PerformanceWEInRome;
import it.unibo.msrehab.model.controller.PerformanceWEInRomeController;
import it.unibo.msrehab.model.controller.WeekendInRomeController;
import it.unibo.msrehab.model.weekendInRome.Problem;
import it.unibo.msrehab.model.weekendInRome.Goal;
import it.unibo.msrehab.model.controller.ChangeDifficultyController;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.FitnessWeightController;
import it.unibo.msrehab.model.controller.HistoryController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import it.unibo.msrehab.model.entities.MSRSession;

import javax.servlet.http.HttpSession;
import org.json.JSONArray;

/**
 * Service for the Weekend In Rome exercise
 * @author Margherita Donnici
 */
@Controller
public class PddlWeekendRomeService
{
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
	    logger.warn("Returning HTTP 400 Bad Request", e);
	    System.out.println(e);
	}

    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);
    private final HistoryController historyController;
    private final FitnessWeightController fitnessController;
    private final MSRSessionController sessionController;
    private final ChangeDifficultyController changeDiffController;
    private final WeekendInRomeController weekendInRomeController;
    private final PerformanceWEInRomeController performanceWEInRomeController;
    private static final int EASY_MIN = 1;
    private static final int MEDIUM_MIN = 4;
    private static final int DIFFICULT_MIN = 7;

    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();

    private Problem problem;
    
    public PddlWeekendRomeService() {
        super();
        this.historyController = new HistoryController(model);
        this.fitnessController = new FitnessWeightController(model);
        this.sessionController = new MSRSessionController(model);
        this.changeDiffController = new ChangeDifficultyController(model);
        this.weekendInRomeController = new WeekendInRomeController();
        this.performanceWEInRomeController=new PerformanceWEInRomeController(model);
    }

   @RequestMapping(value = "/pianificazione3", method = RequestMethod.GET)
    public ModelAndView pianificazione3(
            @RequestParam String difficulty,
            @RequestParam Integer patientid,
            @RequestParam Integer exerciseid,
            @RequestParam Integer sessid,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model)
   {

        logger.debug("pianificazion3()");
        
         Integer level;
        if (difficulty.equals("training") || difficulty.equals("demo"))
            level = 1;
        else
        {
            List<History> historyList = historyController.findAllSolvedByUserAndExerciseAndSessidAndAgent(patientid, exerciseid, sessid, null);
            if (historyList.isEmpty())
            {
                if ("easy".equals(difficulty))
                    level = 1;
                else if ("medium".equals(difficulty))
                    level = 4;
                else
                    level = 7;
            }
            else
            {
                level = historyList.get(0).getLevel();
                List<ChangeDifficulty> cdl = changeDiffController.findFromHistory(historyList.get(0).getId());
                if ((cdl != null) && (!cdl.isEmpty()))
                    level = cdl.get(0).getLevel();

                if (level >= 1 && level <= 3)
                    difficulty = "easy";
                else if (level >= 3 && level <= 6)
                    difficulty = "medium";
                else if (level >= 7)
                    difficulty = "difficult";
            }
        }
 
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("pianificazione3");
    }
    
    
    @RequestMapping(value = "/pianificazione3phase1", method = RequestMethod.GET)
    public ModelAndView pianificazione1(
            HttpServletRequest request,
            @RequestParam String difficulty,
            @RequestParam(value = "level",  required = false, defaultValue = "1") Integer level,
            @RequestParam Integer patientid,
            @RequestParam Integer exerciseid,
            @RequestParam Integer sessid,
            Model model)
    {


        if (difficulty.equals("training") || difficulty.equals("demo"))
            level = 1;

        else
        {
            List<History> historyList = historyController.findAllSolvedByUserAndExerciseAndSessidAndAgent(patientid, exerciseid, sessid, null);
            if (historyList.isEmpty())
            {
                if (difficulty.equals("easy"))
                    level = EASY_MIN;

                if (difficulty.equals("medium"))
                    level = MEDIUM_MIN;

                if (difficulty.equals("difficult"))
                    level = DIFFICULT_MIN;
            }
            else
            {
                level = historyList.get(0).getLevel();
                if(historyList.size()>=2
                        && historyList.get(0).getPassed().equals(true)&&historyList.get(1).getPassed().equals(true)
                        && historyList.get(0).getLevel().equals(historyList.get(1).getLevel())) {
                    level=historyList.get(0).getLevel()+1;
                }
                List<ChangeDifficulty> cdl = changeDiffController.findFromHistory(historyList.get(0).getId());
                if ((cdl != null) && (!cdl.isEmpty()))
                    level = cdl.get(0).getLevel();

                if (level >= EASY_MIN && level < MEDIUM_MIN)
                    difficulty = "easy";
                else if (level >= MEDIUM_MIN && level < DIFFICULT_MIN)
                    difficulty="medium";
                else
                    difficulty = "difficult";
            }
        }
        model.addAttribute("idproblem", 1);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        return new ModelAndView("pddl-pianificazione3a");
    }
    
    @RequestMapping(value = "/pianificazione3phase2", method = RequestMethod.GET)
    public ModelAndView pianificazione2(
            HttpServletRequest request,
            @RequestParam Integer idproblem,
            @RequestParam String difficulty,
            @RequestParam(value = "level", required = false, defaultValue = "1") Integer level,
            @RequestParam Integer patientid,
            @RequestParam Integer exerciseid,
            @RequestParam Integer sessid,
            Model model) {

        // Generate problem
        problem = weekendInRomeController.generateProblem(level);
        if (problem==null)
        {
            MSRSession sess = sessionController.getEntity(sessid);
            JSONArray jsonArr = new JSONArray(sess.getExercises());
            JSONObject json;
            boolean sessionEnded = true;
            for (int i = 0; i < jsonArr.length(); i++)
            {
                json = jsonArr.getJSONObject(i);
                if (json.getInt("id") == exerciseid)
                {
                    json.put("done", true);
                    jsonArr.put(i, json);
                }
                sessionEnded = sessionEnded && json.getBoolean("done");
            }
            sess.setExercises(jsonArr.toString());
            sess.setActive(!sessionEnded);
            sessionController.updateEntity(sess);
            return new ModelAndView("redirect:"+"patienthome");
        }
        model.addAttribute("sleepHour", problem.getSleepHour());
    	model.addAttribute("trains", problem.getTrainTrips());
    	model.addAttribute("goals", problem.getGoals());
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("idproblem", idproblem);
        return new ModelAndView("pddl-pianificazione3b");
    }
    

    @RequestMapping(value = "/verify", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> verify(
            HttpServletRequest request,
            @RequestBody  ArrayList<String> actions,
            Model model)
    {
    	boolean hasSolution;

        ArrayList<String> actionList = new ArrayList<>();
        actionList.addAll(actions);

        problem.updateProblemString(actionList);
        hasSolution = weekendInRomeController.isSolvable(problem);

        HttpHeaders headers = new HttpHeaders();
        JSONObject res=new JSONObject();
        res.put("hasSolution", hasSolution);
        return new ResponseEntity<String>(res.toString(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getHint", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> getHint(
            HttpServletRequest request,
            @RequestBody  ArrayList<String> actions,
            Model model)
    {
    	boolean hasSolution;
    	String nextAction;

        ArrayList<String> actionList = new ArrayList<String>();
        actionList.addAll(actions);

        problem.updateProblemString(actionList);
        String actionString = weekendInRomeController.getNextAction(problem);
        if (actionString.isEmpty())
        {
        	// The problem has no solution
        	hasSolution = false;
        	nextAction = "";
        }
        else
        {
        	nextAction = problem.getNextActionUserFriendlyString(actionString);
        	hasSolution = true;
        }
    	// Build response
        HttpHeaders headers = new HttpHeaders();
        JSONObject res=new JSONObject();
        res.put("hasSolution", hasSolution);
        res.put("nextAction", nextAction);
        return new ResponseEntity<String>(res.toString(), headers, HttpStatus.OK);
    }
    
    //CALCOLO DELLA PERFORMANCE SARA
    
    
      private History calculateFitness(int nCorrect, int nWrong, String difficulty, int idproblem, boolean passed, int patientid, int exerciseid, int sessid) {

       // int nstep = PDDLsolution.size();
        
        int pCorrect = nCorrect;
        int pMissed = 0;
        int pWrong = nWrong;



        // TODO: Attualmente restituisce solo l'agente reattivo
        List<History> lastHistory = historyController.findAllSolvedByUserAndExerciseAndSessidAndAgent(patientid, exerciseid, sessid, null);
        int newLevel = ExerciseService.findChangedLevel(changeDiffController, lastHistory);
        History history = new History();
        history.setExid(exerciseid);
        history.setSessid(sessid);
        history.setUserid(patientid);
        history.setPassed(passed);
        history.setpTime(0.0);
        history.setDifficulty(difficulty);
        history.setLevel(newLevel);
        history.setpCorrect(pCorrect);
        history.setpMissed(pMissed);
        history.setpWrong(pWrong);
        history.setMaxtime((double)20*(pCorrect+pMissed+pWrong));    // 20 secs per action
        //inserire attività, inserire goals inserire performance 
        history.getId();
        // Calculate performance
        double ft = Double.NaN;
        ft = fitnessController.calculateFitness(false, history);
        history.setAbsperformance(ft);
        Long t = LocalDateTime.now().toDateTime().getMillis();
        history.setCreateTimestamp(t);

        return history;
    }


    
    
    
        @RequestMapping(value = "/getweperformance", method = RequestMethod.GET)
          public ResponseEntity<String> getproblemperformance(
            HttpServletRequest request,
            @RequestParam String difficulty,
            @RequestParam Integer level,
            @RequestParam Integer patientid,
            @RequestParam Integer exerciseid,
            @RequestParam Integer sessid,
            @RequestParam Integer idproblem,
            @RequestParam Boolean passed,
            @RequestParam int nWrong,
            @RequestParam int nCorrect,
            Model model)
        {
        System.out.println("Entrato nella richiesta");

        History h = calculateFitness(nCorrect, nWrong, difficulty, idproblem, passed, patientid, exerciseid, sessid);
        
         double perf = fitnessController.calculateFitness(false, h);

        double thr = fitnessController.getEntity(exerciseid).getThr();
        passed = (perf >= thr);
        
        
        JSONObject res = new JSONObject();
        res.put("perf", perf);
        res.put("passed", passed);
        res.put("thr", thr);
        
        return new ResponseEntity<>(res.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/pianificazione3phase3", method = RequestMethod.GET)
    public ModelAndView pianificazione3(
            HttpServletRequest request,
             @RequestParam String difficulty,
            @RequestParam Integer level,
            @RequestParam Integer patientid,
            @RequestParam Integer exerciseid,
            @RequestParam Integer idproblem,
            @RequestParam Boolean passed,
            @RequestParam Integer nWrong,
            @RequestParam Integer nCorrect,
            @RequestParam Integer sessid,
            @RequestParam String actions,
            @RequestParam Double initTime,
            @RequestParam Double endTime,
            @RequestParam Integer nClickTarget,
            @RequestParam String targetNonRaggi,
            @RequestParam Integer nClickPrenotazioni,
            Model model)
    {
        String url;

        if (patientid == -1)
            if (difficulty.equals("training"))
                return new ModelAndView("redirect: patienttraining");
            else
                return new ModelAndView("redirect: patientdemo");
        else
        {
            if (difficulty.equals("training") || difficulty.equals("demo"))
                return new ModelAndView("redirect: patientrehabilitation");
            else
            {

                int pCorrect = nCorrect;
                int pMissed = 0;
                int pWrong = nWrong;

                HttpSession httpSess = request.getSession();
                int newLevel=level;
                // TODO: Attualmente restituisce solo l'agente reattivo
                List<History> lastHistory = historyController.findAllSolvedByUserAndExerciseAndSessidAndAgent(patientid, exerciseid, sessid, null);
                if(lastHistory==null&&level==1)
                    newLevel=level;
                else
                {
                    newLevel = ExerciseService.findChangedLevel(changeDiffController,lastHistory);
                    if(newLevel==-1 && level!=-1)
                        newLevel=level;
                }
                History history = new History();
                history.setExid(exerciseid);
                history.setPassed(passed);
                history.setUserid(patientid);
                history.setpTime(0.0);
                history.setpCorrect(pCorrect);
                history.setpMissed(pMissed);
                history.setpWrong(pWrong);
                history.setMaxtime((double) (pWrong+pMissed+pCorrect) * 20);    // 20 secs per action
                //history.setDifficulty(session);

                history.setDifficulty(difficulty);

                // Calculate performance
                double ft = Double.NaN;
                ft = fitnessController.calculateFitness(false, history);
                history.setAbsperformance(ft);
                ft = fitnessController.calculateFitness(true, history);
                history.setRelperformance(ft);

                Long t = LocalDateTime.now().toDateTime().getMillis();
                history.setCreateTimestamp(t);
                history.setLevel(newLevel);
                history.setSessid(sessid);


                PerformanceWEInRome pwe = new PerformanceWEInRome();
                pwe.setHistoryid(history.getId());
                pwe.setActionuser(actions);
                ArrayList<Goal> gls=problem.getGoals();
                String s="";
                for (Goal gl : gls)
                {
                    if (!gl.getUserFriendlyString().isEmpty())
                        s = s + gl.getUserFriendlyString() + "\n";
                }
                s=s+" Dormi alle "+problem.getSleepHour()+"\n";
                String treno[]=actions.split(",");
                String orari[]=treno[0].split(" ");
                s=s+"Tornare a casa alle "+orari[5].substring(4,6) ;

                pwe.setGoals(s);
                pwe.setProblem(problem.getProblemString());
                pwe.setEndTime(endTime);
                pwe.setInitTime(initTime);
                pwe.setNoGoalsCompleted(targetNonRaggi);
                pwe.setnClickTarget(nClickTarget);
                pwe.setnClickPrenotazioni(nClickPrenotazioni);

                historyController.insertEntity(history);
                performanceWEInRomeController.insertEntity(pwe);


                if(newLevel>=10)
                {
                    MSRSession sess = sessionController.findEntity(sessid).get();
                    JSONArray jsonArr = new JSONArray(sess.getExercises());
                    JSONObject json;
                    boolean sessionEnded = true;
                    for (int i = 0; i < jsonArr.length(); i++) {
                        json = jsonArr.getJSONObject(i);
                        if (json.getInt("id") == exerciseid) {
                            json.put("done", true);
                            jsonArr.put(i, json);
                        }
                        sessionEnded = sessionEnded && json.getBoolean("done");
                    }
                    sess.setExercises(jsonArr.toString());
                    sess.setActive(!sessionEnded);
                    sessionController.updateEntity(sess);
                    url = "patienthome";
                }
                else
                {

                    url = "/pianificazione3phase1"
                            + "?difficulty=" + difficulty
                            + "&level=" + newLevel
                            + "&patientid=" + patientid
                            + "&exerciseid=" + exerciseid
                            + "&sessid=" + sessid;
                }

                return new ModelAndView("redirect:" + url);
            }
       }
    }
}
     

        
    
    
    
    
    
    
    
    

