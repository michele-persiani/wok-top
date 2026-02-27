package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.ChangeDifficulty;
import it.unibo.msrehab.model.entities.Environment;
import it.unibo.msrehab.model.entities.History;
import it.unibo.msrehab.model.pddl.PDDLGoal;
import it.unibo.msrehab.model.pddl.PDDLGoalBuilder;
import it.unibo.msrehab.model.pddl.PDDLMap;
import it.unibo.msrehab.model.pddl.PDDLPlace;
import it.unibo.msrehab.model.pddl.PDDLProblemBuilder;
import it.unibo.msrehab.model.pddl.PDDLProblem;
import it.unibo.msrehab.model.entities.Problem;
import it.unibo.msrehab.model.controller.ChangeDifficultyController;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.FitnessWeightController;
import it.unibo.msrehab.model.controller.HistoryController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.controller.PDDLEnvironmentController;
import it.unibo.msrehab.model.controller.PDDLProblemController;
import it.unibo.msrehab.util.WebPagesUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDateTime;
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
public class PddlBrainService {
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
    private static final int MEDIUM_MIN = 6;
    private static final int DIFFICULT_MIN = 11;
    
    public PddlBrainService(){
        super();
        this.historyController = new HistoryController();
        this.fitnessController  = new FitnessWeightController();
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

    @RequestMapping(value = "/pianificazione1", method = RequestMethod.GET)
    public ModelAndView pianificazione1(
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "exname", required = false) String exname,
            @RequestParam(value = "exdescr", required = false) String exdescr,
            Model model) {

        logger.debug("pianificazione1()");

        model.addAttribute("difficulty", difficulty);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("exname", exname);
        model.addAttribute("exdescr", exdescr);
        return new ModelAndView("pianificazione1");
    }
    
    @RequestMapping(value = "/pianificazione1phase1", method = RequestMethod.GET)
    public ModelAndView pianificazione1(
            HttpServletRequest request,
            @RequestParam(value = "difficulty", required = true) String difficulty,
            @RequestParam(value = "level", required = false, defaultValue = "1") Integer level,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "exerciseid", required = true) Integer exerciseid,
            @RequestParam(value = "sessid", required = true) Integer sessid,
            @RequestParam(value = "color", required = true, defaultValue="true") Boolean color,
            Model model) {
        Random r=new Random();
        if (difficulty.equals("training") || difficulty.equals("demo")) {
            level = 1;
        }
        else {
            List<History> historyList = historyController.findAllByUserAndExerciseAndSessid(patientid, exerciseid, sessid);
            if (historyList.isEmpty()) {
                if(difficulty.equals("easy")){
                    level=EASY_MIN;
                }
                if(difficulty.equals("medium")){
                    level=MEDIUM_MIN;
                    color=r.nextBoolean();
                }
                if(difficulty.equals("difficult")){
                    level=DIFFICULT_MIN;
                    color=false;
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
                    color=r.nextBoolean();
                }
                else { // level >= MAXIMUM_MIN
                    difficulty = "difficult";
                    color = false;
                }
            }
        }
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        int id; //chose from db a number
        PDDLProblemController ppc=new PDDLProblemController();
        
        List<Problem> lp=ppc.getRecordFromDifficulty(level);
        if(level>=10 &&level <=11)
        {
        lp.addAll(ppc.getRecordFromDifficulty(level+1));//TODO REMOVE THIS
        }
        if(level>11)
        {
        lp.addAll(ppc.getRecordFromDifficulty(level+1));//TODO REMOVE THIS
        lp.addAll(ppc.getRecordFromDifficulty(level+2));//TODO REMOVE THIS
        lp.addAll(ppc.getRecordFromDifficulty(level+3));//TODO REMOVE THIS
        }
        
        System.out.println(lp.size());
        int idx = r.nextInt(lp.size());
        Problem p=lp.get(idx);       
        
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("idproblem", p.getId());
        model.addAttribute("color", color);
        return new ModelAndView("pddl-pianificazione1a");
    }
    
    @RequestMapping(value = "/pianificazione1phase2", method = RequestMethod.GET)
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
        
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("level", level);
        model.addAttribute("patientid", patientid);
        model.addAttribute("exerciseid", exerciseid);
        model.addAttribute("sessid", sessid);
        model.addAttribute("idproblem", idproblem);
        model.addAttribute("color", color);
        return new ModelAndView("pddl-pianificazione1b");
    }
    
    private History calculateFitness(String[] actions, Double pTime, String difficulty, int idproblem, boolean passed, int patientid, int exerciseid, int sessid) {
        int nstep = 0;
        List<PDDLGoal> a = new ArrayList<PDDLGoal>();
        for (String action : actions) {
            a.add(new PDDLGoal(action));
        }

        PDDLProblemController ppc = new PDDLProblemController();
        Problem p = ppc.findEntity(idproblem).get();
        List<PDDLGoal> step = PDDLProblem.splitStringInGoals(p.getSolutions());
        nstep = step.size();
        PDDLProblem problem = p.getPDDLproblem();
        int pCorrect = 0;
        int pMissed = 0;
        int pWrong = 0;

        if (passed) {
            pCorrect = step.size();
            if (a.size() - step.size() > 0) {
                pWrong = a.size() - step.size();
            }
        } else {
            for (int i = 0; i < a.size() - 1; i++) {
                problem.binds(a.get(i));
            }
            String solution = problem.thinkSolution();
            if (!solution.equals("")) {
                String[] stepMissed = solution.split("\n");
                pCorrect = a.size() - 1;
                if (stepMissed.length + a.size() - 1 - step.size() > 0) {
                    pWrong = stepMissed.length + a.size() - 1 - step.size();
                    if (pCorrect - pWrong > 0) {
                        pCorrect -= pWrong;
                        pWrong++;
                    } else {
                        pCorrect = 0;
                    }
                }
                if (stepMissed.length > 0) {
                    pMissed = stepMissed.length;
                }
            } else {
                System.out.println("Errore nella risoluzione dell'esercizio! aiuto");
            }
        }

        //List<History> lastHistory = historyController.findAllByUserAndExerciseAndSessid(patientid, exerciseid, sessid);
        //int newLevel = ExerciseService.findChangedLevel(changeDiffController, lastHistory);

        History history = new History();
        history.setExid(exerciseid);
        history.setSessid(sessid);
        history.setUserid(patientid);
        history.setPassed(passed);
        history.setpTime(pTime);
        history.setLevel(p.getDifficulty());
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
    
    @RequestMapping(value = "/getproblemperformance", method = RequestMethod.GET)
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
            String belief,
            @RequestParam(value = "color", required = false, defaultValue = "false") boolean color,
            Model model) {
         HttpHeaders headers = new HttpHeaders();
         System.out.println("Entrato nella richiesta");
         
         History h=calculateFitness(actions, pTime, difficulty, idproblem, passed, patientid, exerciseid, sessid);
         String html = "<p>Performance: "+(Math.floor(h.getAbsperformance() * 100))+" % </p>";

         if(Boolean.FALSE.equals(passed)){ 
            PDDLProblemController ppc=new PDDLProblemController();
            Problem p=ppc.findEntity(idproblem).get();
            PDDLProblem problem=p.getPDDLproblem();
            List<PDDLGoal> b=PDDLProblem.splitStringInGoals(belief);
            problem.setBelief(b);
            List<PDDLGoal> unsolved =problem.unsolvedGoal();
            if(unsolved.size()>0){
                PDDLEnvironmentController pec=new PDDLEnvironmentController();
                Environment pe=pec.findEntity(p.getIdenvironment()).get();
                html+="<h4>Obiettivi non risolti:</h4></ul>";            
                for (PDDLGoal unsolved1 : unsolved) {
                    html += "<li>" + pe.getNaturalLanguageGoal(unsolved1) + "</li>";
                }
                html+="</ul>";
            }
         }else{
             System.out.println("esercizio superato!");
         }
        
        return new ResponseEntity<String>(html, headers,
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/pianificazione1phase3", method = RequestMethod.GET)
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
        
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        int nstep=0;

        if (patientid == -1) {
            if (difficulty.equals("training")) {
                return new ModelAndView("redirect: patienttraining");
            }
            else {  //difficulty.equals("demo"))
                return new ModelAndView("redirect: patientdemo");
            }
        }
        else {
            if (difficulty.equals("training") || difficulty.equals("demo")) {
                return new ModelAndView("redirect: patientrehabilitation");
            }
        
        else {
            List<PDDLGoal> a=new ArrayList<PDDLGoal>();
            for (String action : actions) {
                a.add(new PDDLGoal(action));
            }
            
            PDDLProblemController ppc=new PDDLProblemController();
            Problem p=ppc.findEntity(idproblem).get();
            List<PDDLGoal> step=PDDLProblem.splitStringInGoals(p.getSolutions());
            nstep=step.size();
            PDDLProblem problem=p.getPDDLproblem();
            int pCorrect=0;
            int pMissed=0;
            int pWrong=0;
            
            if(passed){
                pCorrect=step.size();
                if(a.size()-step.size()>0)
                    pWrong=a.size()-step.size();
            }else{
                for(int i=0;i<a.size()-1;i++){
                    problem.binds(a.get(i));
                }
                String solution=problem.thinkSolution();
                if(!solution.equals("")){
                    String stepMissed[] = solution.split("\n");
                    pCorrect = a.size()-1;
                    if(stepMissed.length + a.size() - 1 - step.size()>0){
                        pWrong = stepMissed.length + a.size() - 1 - step.size();
                        if(pCorrect-pWrong>0) {
                            pCorrect-=pWrong;
                            pWrong++;
                        }
                        else {
                            pCorrect=0;
                        }
                    }
                    if(stepMissed.length >0)
                        pMissed = stepMissed.length;
                }else{
                    System.out.println("Errore nella risoluzione dell'esercizio! aiuto");
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
           // history.setLevel(p.getDifficulty());
            history.setDifficulty(difficulty);

            // Calculate performance
            double ft = Double.NaN;
            ft = fitnessController.calculateFitness(false, history);
            history.setAbsperformance(ft);
            ft = fitnessController.calculateFitness(true, history);
            history.setRelperformance(ft);

            Long t = LocalDateTime.now().toDateTime().getMillis();
            history.setTimestamp(t);
            history.setLevel(p.getDifficulty());
            history.setSessid(sessid);

            if (!historyController.insertEntity(history))
            {
                logger.error("Error adding History to DB");
                model.addAttribute("message", "ERRORE GRAVE: interrompere l'uso del sistema e contattare l'assistenza");
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }
               
        }
        //pianificazione1phase1
        String url = "/pianificazione1phase1"
                + "?difficulty=" + difficulty
                + "&level=" + level
                + "&patientid=" + patientid
                + "&exerciseid=" + exerciseid
                + "&color=" + color
                + "&sessid=" + sessid;
                
        return new ModelAndView("redirect:" + url);
        }
    }
    @RequestMapping(value = "/getproblemjson", method = RequestMethod.GET)
    public ResponseEntity<String> getproblemjson(
            HttpServletRequest request,
            int id,
            Model model) {
        
        HttpHeaders headers = new HttpHeaders();
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        PDDLProblemController ppc=new PDDLProblemController();
        Problem p=ppc.findEntity(id).get();

        JSONObject res=new JSONObject();
        res.put("map", p.getMapjson());
        res.put("belief",p.getBelief());
        return new ResponseEntity<String>(res.toString(), headers,
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getproblemsolution", method = RequestMethod.GET)
    public ResponseEntity<String> getproblemsolution(
            HttpServletRequest request,
            int id,
            Model model) {
        
        HttpHeaders headers = new HttpHeaders();
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        PDDLProblemController ppc=new PDDLProblemController();
        Problem p=ppc.findEntity(id).get();
        
        return new ResponseEntity<String>(p.getSolutions(), headers,
                HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/getproblemdescription", method = RequestMethod.GET)
    public ResponseEntity<String> getproblemdescription(
            HttpServletRequest request,
            int id,
            Model model) {
        
        HttpHeaders headers = new HttpHeaders();
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        PDDLProblemController ppc=new PDDLProblemController();
        PDDLEnvironmentController pec=new PDDLEnvironmentController();
        Problem p=ppc.findEntity(id).get();
        
        Environment pe=pec.findEntity(p.getIdenvironment()).get();
        PDDLProblem problem=p.getPDDLproblem();
        String description = problem.getProblemDescription(pe);
        
        return new ResponseEntity<String>(description, headers,
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/doaction", method = RequestMethod.POST)
    public ResponseEntity<String> doaction(
            HttpServletRequest request,
            int id,//?ma sta cosa qui si può fare?
            String belief,
            String action,
            Model model) {
        
        HttpHeaders headers = new HttpHeaders();
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        PDDLProblemController ppc=new PDDLProblemController();
        Problem p=ppc.findEntity(id).get();
        PDDLProblem problem=p.getPDDLproblem();
        List<PDDLGoal> b=PDDLProblem.splitStringInGoals(belief);
        System.out.println("Abbiamo ricevuto: "+b.size()+" belief");
        problem.setBelief(b);        
        PDDLGoal goal_action = new PDDLGoal(action);
        System.out.println("Lazione in question: "+goal_action);
        boolean canApply=problem.binds(goal_action);
        System.out.println("Posso applicare il bind: "+canApply);
        boolean hasSolution=false;
        boolean isSolved=false;
        
        JSONObject res=new JSONObject();
        if(canApply){
            //TODO VERIFICA CHE LA LISTA DEI GOAL NON SIA SOTTOINSIEME DELLA LISTA BELIEF (ALTIRMENTI È RISOLTO)
            if(problem.isSolved()){
                System.out.println("Il problema è risolto!!!");
                isSolved = true;
                hasSolution=true;
            }else{
                hasSolution=problem.hasSolution();
            }
        }else{
            
        }     
        
        res.put("isSolved", isSolved);
        res.put("hasSolution", hasSolution);
        res.put("belief",PDDLProblem.concatGoalsInString(problem.getBelief()));
        return new ResponseEntity<String>(res.toString(), headers,
                HttpStatus.OK);
    }
    public static int MINIMUM_NUMBER_OF_STEP_FOR_SOLUTION=4;
    public static Problem generateProblemFromNumberOfGoals(int numberOfGoals,int idEnvironment){
        Random r=new Random();
        PDDLPlace.r=r;
        PDDLMap.r=r;
        PDDLGoalBuilder.r=r;
        PDDLProblemBuilder p=new PDDLProblemBuilder(numberOfGoals,idEnvironment);
        PDDLProblem p1 = p.build();
        String solution = p1.thinkSolution();
        
        PDDLProblemController ppc=new PDDLProblemController();
        Problem problema =new Problem();
        problema.setProblemHead(p1.getProblemHead());
        problema.setMapjson(p1.getMapjson());
        problema.setBelief(PDDLProblem.concatGoalsInString(p1.getBelief()));
        problema.setGoals(PDDLProblem.concatGoalsInString(p1.getGoals()));
        problema.setIdenvironment(idEnvironment);
        problema.setSolutions(solution);
        //int difficulty=Math.max(solution.split("\n").length-MINIMUM_NUMBER_OF_STEP_FOR_SOLUTION,0);
        int difficulty = Math.max(p1.calculateDifficultyFromSolution(solution)-MINIMUM_NUMBER_OF_STEP_FOR_SOLUTION,0);
        difficulty+= p.getNumberOfPlace()/3;
        
        problema.setDifficulty(difficulty);
        // TODO manage problem addition failure
        boolean success = ppc.insertEntity(problema);
        return problema;
    }
    
    @RequestMapping(value = "/createproblem", method = RequestMethod.GET)
    public ResponseEntity<String> createproblem(
            HttpServletRequest request,
            Model model) {
        
        HttpHeaders headers = new HttpHeaders();
        /*if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }*/
        
        Problem problema = generateProblemFromNumberOfGoals(3, 1);
        
       
        return new ResponseEntity<String>("Id: "+problema.getId()+"\n<br />difficulty: "+problema.getDifficulty(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getsolution", method = RequestMethod.GET)
    public ResponseEntity<String> getsolution(
            HttpServletRequest request,
            Model model) {
        
        HttpHeaders headers = new HttpHeaders();
        if (WebPagesUtilities.redirectIfNotLogged(request)||((WebPagesUtilities.redirectIfNotAdmin(request)))) {
            return new ResponseEntity<String>("", headers,
                HttpStatus.UNAUTHORIZED);
        }
        //PDDLproblem p = new PDDLProblem();
        //String solution = p.thinkSolution();
        return new ResponseEntity<String>("".toString(), headers,
                HttpStatus.OK);
    }
    
}
