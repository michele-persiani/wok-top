package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.Exercise;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseCategoryValue.*;
import it.unibo.msrehab.model.entities.MSRGroup;
import it.unibo.msrehab.model.entities.MSRSession;
import it.unibo.msrehab.model.MSRSessionForm;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.util.CookiesManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Class that provides servlets for exercise sessions.
 */
@Controller
public class MSRSessionService {

    private static final Logger logger = LoggerFactory
            .getLogger(MSRSessionService.class);
    private final MSRUserController userController;
    private final MSRGroupController groupController;
    private final ExerciseController exerciseController;
    private final MSRSessionController sessionController;
    private final Configuration config;    
    
    public MSRSessionService() {
        super();
        this.userController = new MSRUserController();
        this.groupController = new MSRGroupController();
        this.exerciseController = new ExerciseController();
        this.sessionController = new MSRSessionController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);

    }
    
    @RequestMapping(value = "/groupsessionmanagement", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView groupsessionmanagement(
            HttpServletRequest request,
            Model model) {
        int cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRGroup> groups = groupController.findAllGroupsInCenter(cid);
        List<MSRGroup> notEmptyGroups = new ArrayList<>();
        List<String> hospitalGroupNames = new ArrayList();
        List<String> homeGroupNames = new ArrayList();
        List<List<String>> hospitalExerciseNames = new ArrayList();
        List<List<String>> homeExerciseNames = new ArrayList();
        List<MSRSession> hospitalActiveSessions = sessionController.findAllHospitalActiveForGroups();
        List<MSRSession> homeActiveSessions = sessionController.findAllHomeActiveForGroups();
        List<MSRSession> hospitalActiveSessionsPerCurrentCenter = new ArrayList(); 
        List<MSRSession> homeActiveSessionsPerCurrentCenter = new ArrayList(); 
 
        for (MSRGroup group : groups) {
            if (!userController.findAllPatientsInGroup(group.getId()).isEmpty()) {
                notEmptyGroups.add(group);
            }
        }
        
        if(notEmptyGroups.size()>0) {
            for (MSRSession session : hospitalActiveSessions) {
                int grid = session.getUsrgrpid();
                for (MSRGroup group : groups) {
                    if (group.getId() == grid) {
                        hospitalGroupNames.add(group.getName());
                        String exs = session.getExercises();
                        JSONArray exsJsonArr = new JSONArray(exs);
                        List<String> exerciseNamesGroup = new ArrayList();
                        for (int i = 0; i < exsJsonArr.length(); i++) {
                            JSONObject exJson = exsJsonArr.getJSONObject(i);
                            int exId = exJson.getInt("id");
                            exerciseNamesGroup.add(
                                    exerciseController.findEntity(exId).get().getFullName());
                        }
                        hospitalExerciseNames.add(exerciseNamesGroup);
                        hospitalActiveSessionsPerCurrentCenter.add(session);
                    }
                }
            }
            for (MSRSession session : homeActiveSessions) {
                int grid = session.getUsrgrpid();
                for (MSRGroup group : groups) {
                    if (group.getId() == grid) {
                        homeGroupNames.add(group.getName());
                        String exs = session.getExercises();
                        JSONArray exsJsonArr = new JSONArray(exs);
                        List<String> exerciseNamesGroup = new ArrayList();
                        for (int i = 0; i < exsJsonArr.length(); i++) {
                            JSONObject exJson = exsJsonArr.getJSONObject(i);
                            int exId = exJson.getInt("id");
                            exerciseNamesGroup.add(
                                    exerciseController.findEntity(exId).get().getFullName());
                        }
                        homeExerciseNames.add(exerciseNamesGroup);
                        homeActiveSessionsPerCurrentCenter.add(session);
                    }
                }
            }
            if (hospitalActiveSessionsPerCurrentCenter.isEmpty() && homeActiveSessionsPerCurrentCenter.isEmpty()) {
                model.addAttribute("message", "Nessuna sessione attiva definita");
            }

            model.addAttribute("newsession", notEmptyGroups.size() !=
                    hospitalActiveSessionsPerCurrentCenter.size() + homeActiveSessionsPerCurrentCenter.size());
            model.addAttribute("hospitalsessions", hospitalActiveSessionsPerCurrentCenter);
            model.addAttribute("homesessions",homeActiveSessionsPerCurrentCenter);
            model.addAttribute("hospitalgroupnames", hospitalGroupNames);
            model.addAttribute("homegroupnames", homeGroupNames);
            model.addAttribute("hospitalexercisenames", hospitalExerciseNames);
            model.addAttribute("homeexercisenames", homeExerciseNames);
            return new ModelAndView("group-session-management");
        }
        else {
            //model.addAttribute("message", "Nessun paziente nei gruppi");
            model.addAttribute("message", "Nessun partecipante nei gruppi");
            
            model.addAttribute("back", "adminhome");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }        
    }
    
    @RequestMapping(value = "/patientsessionmanagement", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientsessionmanagement(
            HttpServletRequest request,
            Model model) {
        int cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRUser> patients
                = userController.findAllPatientsInCenter(cid);
        List<String> hospitalPatientNames = new ArrayList<>();
        List<String> homePatientNames = new ArrayList<>();
        List<List<String>> hospitalExerciseNames = new ArrayList<>();
        List<List<String>> homeExerciseNames = new ArrayList<>();
        List<MSRSession> hospitalActiveSessions = sessionController.findAllHospitalActiveForPatients();
        List<MSRSession> homeActiveSessions = sessionController.findAllHomeActiveForPatients();
        List<MSRSession> hospitalActiveSessionsPerCurrentCenter = new ArrayList<>();
        List<MSRSession> homeActiveSessionsPerCurrentCenter = new ArrayList<>();
        
        for (MSRSession session : hospitalActiveSessions)
        {
            int patid = session.getUsrgrpid();
            for (MSRUser patient : patients) {
                if (patient.getId() == patid) {
                    hospitalPatientNames.add(
                            patient.getName() + " " + patient.getSurname());
                    String exs = session.getExercises();
                    JSONArray exsJsonArr = new JSONArray(exs);
                    List<String> exerciseNamesPatient = new ArrayList<>();

                    for (int i = 0; i < exsJsonArr.length(); i++)
                    {
                        JSONObject exJson = exsJsonArr.getJSONObject(i);
                        int exId = exJson.getInt("id");
                        exerciseController.findEntity(exId).map(Exercise::getFullName).ifPresent(exerciseNamesPatient::add);
                    }
                    hospitalExerciseNames.add(exerciseNamesPatient);
                    hospitalActiveSessionsPerCurrentCenter.add(session);
                }
            }
        }
        for (MSRSession session : homeActiveSessions) {
            int patid = session.getUsrgrpid();
            for (MSRUser patient : patients) {
                if (patient.getId() == patid) {
                    homePatientNames.add(
                            patient.getName() + " " + patient.getSurname());
                    String exs = session.getExercises();
                    JSONArray exsJsonArr = new JSONArray(exs);
                    List<String> exerciseNamesPatient = new ArrayList<>();
                    for (int i = 0; i < exsJsonArr.length(); i++) {
                        JSONObject exJson = exsJsonArr.getJSONObject(i);
                        int exId = exJson.getInt("id");
                        exerciseController.findEntity(exId).map(Exercise::getFullName).ifPresent(exerciseNamesPatient::add);
                    }
                    homeExerciseNames.add(exerciseNamesPatient);
                    homeActiveSessionsPerCurrentCenter.add(session);
                }
            }

        }
                    
        if (hospitalActiveSessionsPerCurrentCenter.isEmpty() && homeActiveSessionsPerCurrentCenter.isEmpty()) {
            model.addAttribute("message", "Nessuna sessione attiva definita");
        }        
        
        List<MSRUser>patientsNotInGroupsInCenter=userController.findAllPatientsNotInGroupsInCenter(cid);
        boolean newSession=false;
        for(MSRUser patient: patientsNotInGroupsInCenter) {
            if(sessionController.findAllActiveByUserOrGroup(patient.getId()).isEmpty()) {
                newSession = true;
                break;
            }
        }
        model.addAttribute("newsession", newSession);
        model.addAttribute("hospitalsessions", hospitalActiveSessionsPerCurrentCenter);
        model.addAttribute("homesessions", homeActiveSessionsPerCurrentCenter);
        model.addAttribute("hospitalpatientnames", hospitalPatientNames);
        model.addAttribute("homepatientnames", homePatientNames);
        model.addAttribute("hospitalexercisenames", hospitalExerciseNames);
        model.addAttribute("homeexercisenames", homeExerciseNames);
        return new ModelAndView("patient-session-management");
    }

    @RequestMapping(value = "/patientsession", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientsession(
            HttpServletRequest request,
            Model model) {
        Integer cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRUser> patients = userController.findAllPatientsNotInGroupsInCenter(cid);
        List<MSRUser> patientsWithNoSessions = patients
                .stream()
                .filter(patient -> sessionController.findAllActiveByUserOrGroup(patient.getId()).isEmpty())
                .collect(Collectors.toList());

        
        if(!patientsWithNoSessions.isEmpty())
        {
            model.addAttribute("patients", patientsWithNoSessions);

            Arrays.stream(Exercise.ExerciseCategoryValue.values())
                            .forEach(cat ->
                                    model.addAttribute(cat.toString(), exerciseController.findAllEnabledExercisesByCategory(cat))
                            );
            MSRSessionForm sessionForm = new MSRSessionForm();
            model.addAttribute("sessionForm", sessionForm);
            return new ModelAndView("patient-session");
        }
        else {
            return new ModelAndView("redirect:/patientsessionmanagement");
        }
    }

    @RequestMapping(value = "/buildpatientsession", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView buildpatientsession(
            @ModelAttribute("msrSessionForm") MSRSessionForm msrSessionForm,
            Model model) {
        
        MSRSession session = new MSRSession();
        session.setActive(Boolean.TRUE);
        session.setHospital(Boolean.TRUE);
        session.setDifficulty(msrSessionForm.getDifficulty());
        session.setUsrgrpid(msrSessionForm.getUsrgrpid());
        session.setForgroup(Boolean.FALSE);
        session.setGrpid(-1);
        
        JSONObject json = new JSONObject(msrSessionForm);
        json.remove("usrgrpid");
        json.remove("difficulty");
        
        JSONArray exCat = json.names();
        if(exCat==null) {
            return new ModelAndView("redirect:/patientsession");
        }
        else {
        JSONArray exId = json.toJSONArray(exCat);
        boolean[] done = new boolean[exId.length()];
        Arrays.fill(done, false);
        JSONArray exDone = new JSONArray(done);
        
        JSONArray jsonArr = new JSONArray();
        for(int i=0; i<exId.length(); i++) {
            JSONObject json1 = new JSONObject()
                    .put("id", exId.getInt(i))
                    .put("cat", exCat.getString(i))
                    .put("done", exDone.getBoolean(i));
            jsonArr.put(json1);
        }
                
        session.setExercises(jsonArr.toString());
        
        if(!sessionController.insertEntity(session)) {
            logger.error("...session addition failed");
            model.addAttribute("message", "Assegnazione sessione esercizi fallita");
            model.addAttribute("back", "patient-session-management");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }
        
        return new ModelAndView("redirect:/patientsessionmanagement");
        }
    }
    
    @RequestMapping(value = "/showsessionexercises", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView showsessionexercises(
            @RequestParam("sessionid") Integer sessionid,
            Model model) {
        return new ModelAndView("redirect:/groupsessionmanagement");
    }
    
    @RequestMapping(value = "/buildhomesession", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView buildhomesession(
            @RequestParam("sessionid") Integer sessionid,
            @RequestParam(value = "forpatient", required = true) Boolean forpatient,
            Model model) {

        MSRSession session = sessionController.findEntity(sessionid).get(),
                hospitalSession;
        List<MSRUser> patients = new ArrayList<>();
        
        if (forpatient) {
            patients.add(userController.findEntity(session.getUsrgrpid()).get());
        }
        else {
            Integer grpid = session.getUsrgrpid();
            patients = userController.findAllPatientsInGroup(grpid);
        }

        for (MSRUser patient : patients) {
            hospitalSession
                    = sessionController.findAllHospitalByUserOrGroup(patient.getId()).get(0);
            
            String hospitalExercises = hospitalSession.getExercises();

            JSONArray hospitalExs = new JSONArray(hospitalExercises),
                    homeExs = new JSONArray();

            JSONObject hospitalExJson;
            String exCat;
            List<Exercise> exList;

            for (int i = 0; i < hospitalExs.length(); i++) {
                hospitalExJson = hospitalExs.getJSONObject(i);
                homeExs.put(hospitalExJson);
                exCat = hospitalExJson.getString("cat");
                exList = exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.valueOf(exCat));
                for (Exercise exEl : exList) {
                    if (exEl.getId() != hospitalExJson.getInt("id")) {
                        JSONObject homeExJson
                                = new JSONObject();
                        homeExJson
                                .put("id", exEl.getId())
                                .put("cat", exEl.getCategory())
                                .put("done", "false");
                        homeExs.put(homeExJson);
                    }
                }
            }

            MSRSession homePatientSession = new MSRSession();
            homePatientSession.setExercises(homeExs.toString());
            homePatientSession.setActive(Boolean.TRUE);
            homePatientSession.setHospital(Boolean.FALSE);
            homePatientSession.setDifficulty(hospitalSession.getDifficulty());
            homePatientSession.setForgroup(Boolean.FALSE);
            homePatientSession.setGrpid(hospitalSession.getGrpid());
            homePatientSession.setUsrgrpid(hospitalSession.getUsrgrpid());
            homePatientSession.setFromSessionId(hospitalSession.getId());
            // TODO: manage failure of addRecord
            sessionController.insertEntity(homePatientSession);
            hospitalSession.setActive(Boolean.FALSE);
            hospitalSession.setHospital(Boolean.FALSE);
            // TODO: manage failure of updateRecord
            sessionController.updateEntity(hospitalSession);
        }

        session.setActive(Boolean.FALSE);
        session.setHospital(Boolean.FALSE);
        sessionController.updateEntity(session);
        
        if (forpatient) {
            return new ModelAndView("redirect:/patientsessionmanagement");
        }
        else {
            MSRSession homeGroupSession = new MSRSession();
            homeGroupSession.setActive(Boolean.TRUE);
            homeGroupSession.setDifficulty(session.getDifficulty());
            homeGroupSession.setExercises(session.getExercises());
            homeGroupSession.setForgroup(Boolean.TRUE);
            homeGroupSession.setFromSessionId(sessionid);
            homeGroupSession.setGrpid(-1);
            homeGroupSession.setHospital(Boolean.FALSE);
            homeGroupSession.setUsrgrpid(session.getUsrgrpid());
            // TODO: manage failure of addRecord
            sessionController.insertEntity(homeGroupSession);
            
            return new ModelAndView("redirect:/groupsessionmanagement");
        }
    }
    
    @RequestMapping(value = "/groupsession", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView groupsession(
            HttpServletRequest request,
            Model model) {
        
        int cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRGroup> groups = groupController.findAllGroupsInCenter(cid);
        List<MSRGroup> notEmptygroupsWithNoActiveSessions = new ArrayList<MSRGroup>();
        for(MSRGroup group: groups) {
            if (sessionController.findAllActiveByUserOrGroup(group.getId()).isEmpty()
                    && !userController.findAllPatientsInGroup(group.getId()).isEmpty() ) {
                notEmptygroupsWithNoActiveSessions.add(group);
            }
        }
        if(notEmptygroupsWithNoActiveSessions.size()>0) {
            model.addAttribute("groups", notEmptygroupsWithNoActiveSessions);
            model.addAttribute(ATT_SEL_STD.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_SEL_STD));
            model.addAttribute(ATT_SEL_STD_FAC.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_SEL_STD_FAC));
            model.addAttribute(ATT_SEL_STD_ORI.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_SEL_STD_ORI));
            model.addAttribute(ATT_SEL_FLW.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_SEL_FLW));
            model.addAttribute(ATT_SEL_FLW_FAC.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_SEL_FLW_FAC));
            model.addAttribute(ATT_SEL_FLW_ORI.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_SEL_FLW_ORI));
            model.addAttribute(ATT_ALT.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_ALT));
            model.addAttribute(ATT_ALT_FAC.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_ALT_FAC));
            model.addAttribute(ATT_ALT_ORI.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_ALT_ORI));
            model.addAttribute(ATT_DIV.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_DIV));
            model.addAttribute(ATT_DIV_FAC.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_DIV_FAC));
            model.addAttribute(ATT_DIV_ORI.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.ATT_DIV_ORI));
            model.addAttribute(MEM_VIS_1.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.MEM_VIS_1));
            model.addAttribute(MEM_VIS_1_FAC.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.MEM_VIS_1_FAC));
            model.addAttribute(MEM_VIS_1_ORI.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.MEM_VIS_1_ORI));
            model.addAttribute(MEM_VIS_2.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.MEM_VIS_2));
            model.addAttribute(MEM_VIS_2_FAC.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.MEM_VIS_2_FAC));
            model.addAttribute(MEM_VIS_5.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.MEM_VIS_5));
            model.addAttribute(NBACK.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.NBACK));
            model.addAttribute(NBACK_FAC.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.NBACK_FAC));
            model.addAttribute(NBACK_ORI.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.NBACK_ORI));
            model.addAttribute(MEM_LONG_1.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.MEM_LONG_1));
            model.addAttribute(RES_INH.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.RES_INH));
            model.addAttribute(PLAN_1.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.PLAN_1));
            model.addAttribute(PLAN_2.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.PLAN_2));
            model.addAttribute(PLAN_3.toString(), exerciseController.findAllExercisesByCategory(Exercise.ExerciseCategoryValue.PLAN_3));
            MSRSessionForm sessionForm = new MSRSessionForm();
            model.addAttribute("sessionForm", sessionForm);
            
            return new ModelAndView("group-session");
        }
        else {
            return new ModelAndView("redirect:/groupsessionmanagement");
        }
    }

    @RequestMapping(value = "/buildgroupsession", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView buildgroupsession(
            @ModelAttribute("msrSessionForm") MSRSessionForm msrSessionForm,
            Model model) {
        
        MSRSession session = new MSRSession();
        session.setActive(Boolean.TRUE);
        session.setHospital(Boolean.TRUE);
        session.setDifficulty(msrSessionForm.getDifficulty());
        session.setUsrgrpid(msrSessionForm.getUsrgrpid());
        
        JSONObject json = new JSONObject(msrSessionForm);
        json.remove("usrgrpid");
        json.remove("difficulty");
        
        JSONArray exCat = json.names();
        if(exCat==null) {
            return new ModelAndView("redirect:/groupsession");
        }
        else {
            JSONArray exId = json.toJSONArray(exCat);
            boolean[] done = new boolean[exId.length()];
            Arrays.fill(done, false);
            JSONArray exDone = new JSONArray(done);
        
            JSONArray jsonArr = new JSONArray();
            for(int i=0; i<exId.length(); i++) {
                JSONObject json1 = new JSONObject()
                        .put("id", exId.getInt(i))
                        .put("cat", exCat.getString(i))
                        .put("done", exDone.getBoolean(i));
                jsonArr.put(json1);
            }

            session.setExercises(jsonArr.toString());
            session.setForgroup(true);
            session.setGrpid(-1);
            // TODO: manage failure of addRecord
            sessionController.insertEntity(session);
        
            List<MSRUser> patients =
                userController.findAllPatientsInGroup(msrSessionForm.getUsrgrpid());
        
            for(MSRUser patient: patients) {
                session = new MSRSession();
                session.setActive(Boolean.TRUE);
                session.setHospital(Boolean.TRUE);
                session.setDifficulty(msrSessionForm.getDifficulty());
                session.setExercises(jsonArr.toString());
                session.setUsrgrpid(patient.getId());
                session.setForgroup(false);
                session.setGrpid(msrSessionForm.getUsrgrpid());
                // TODO: manage failure of addRecord
                sessionController.insertEntity(session);
            }
        
            return new ModelAndView("redirect:/groupsessionmanagement");
        }
    }
    
    /**
     * Deletes a session given the session id
     *
     * @param sessionid the id of the group
     * @param forpatient
     * @param model
     * @param redirectAttributes
     *
     * @return 1 if group is deleted, -1 otherwise; HttpStatus.NOT_ACCEPTABLE if
     * the user with the given id was not found; HttpStatus.OK if the group is
     * deleted with success; HttpStatus.SERVICE_UNAVAILABLE if the group cannot
     * be deleted
     */
    @RequestMapping(value = "/deletesession", method = RequestMethod.POST)
    public ModelAndView deleteSession(
            @RequestParam(value = "sessionid", required = true) Integer sessionid,
            @RequestParam(value = "forpatient", required = true) Boolean forpatient,
            Model model,
            final RedirectAttributes redirectAttributes) {
        
        logger.info("Start to delete session " + sessionid);
        MSRSession session = (MSRSession) sessionController.findEntity(sessionid).get();
        if (session == null) {
            logger.warn("Session " + sessionid + " not found");
            model.addAttribute("message", "Tutti i gruppi sono vuoti");
            if(forpatient) {
                model.addAttribute("back", "patientsessionmanagement");
            }
            else {
                model.addAttribute("back", "groupsessionmanagement");
            }
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }
                
        if (sessionController.removeEntity(session)) {
            logger.info("Session " + sessionid + " deleted with success");
            /* TODO Check the following two lines */
            redirectAttributes.addFlashAttribute("css", "success");
            redirectAttributes.addFlashAttribute("msg", "Session is deleted!");
            if(forpatient) {
                return new ModelAndView("redirect:/patientsessionmanagement");
            }
            else {
                List<MSRSession> patientGroupSessions =
                        sessionController.findAllForPatientsInGroup(session.getUsrgrpid());
                for(MSRSession sess: patientGroupSessions) {
                    // TODO: manage failure of deleteRecord
                    sessionController.removeEntity(sess);
                }
                List<MSRSession> otherGroupSessions =
                        sessionController.findAllByUserOrGroup(session.getUsrgrpid());
                for(MSRSession sess: otherGroupSessions) {
                    // TODO: manage failure of deleteRecord
                    sessionController.removeEntity(sess);
                }
                return new ModelAndView("redirect:/groupsessionmanagement");
            }
        }
        else {  // deleteRecord(session) == -1
            logger.error("...session delete failed");
            model.addAttribute("message", "Sessione " + sessionid + ", cancellazione fallita");
            if(forpatient) {
                model.addAttribute("back", "patientsessionmanagement");
            }
            else {
                model.addAttribute("back", "groupsessionmanagement");
            }
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }
    }
    
    // list page
//    @RequestMapping(value = "/showgroupsessions", method = RequestMethod.GET)
//    public ModelAndView showgroupsessions(HttpServletRequest request, Model model) {
//        logger.debug("showgroupsessions()");
//        if (WebPagesUtilities.redirectIfNotLogged(request)) {
//            return new ModelAndView("login");
//        } else {
//            List<MSRSession> sessions = sessionController.findAllForGroups();
//            if(sessions.isEmpty()) {
//                model.addAttribute("message", "Nessuna sessione definita");
//            }
//            else {
//                List<MSRGroup> groups = groupController.findAllGroups();
//                List<String> groupNames = new ArrayList();
//            
//                for(MSRSession session: sessions) {
//                    int grid = session.getUsrgrpid();
//                    for(MSRGroup group: groups) {
//                        if(group.getId()==grid) {
//                            groupNames.add(group.getName());
//                        }
//                    }
//                }
//            
//                model.addAttribute("sessions", sessions);
//                model.addAttribute("groupnames", groupNames);
//            }
//            
//            return new ModelAndView("group-session-management");
//        }
//    }

//    // list page
//    @RequestMapping(value = "/showpatientsessions", method = RequestMethod.GET)
//    public ModelAndView showpatientsessions(HttpServletRequest request) {
//        logger.debug("showpatientsessions()");
//        if (WebPagesUtilities.redirectIfNotLogged(request)) {
//            return new ModelAndView("login");
//        } else {
//            return new ModelAndView("patient-session-management", "sessions", sessionController.findAllForPatients());
//        }
//    }
    
    // edit group session
    @RequestMapping(value = "/editgroupsession", method = RequestMethod.POST)
    public ModelAndView editgroupsession(
            @RequestParam(value = "session", required = true) Integer grpid,
            Model model) {

        logger.debug("editgroupsession() : {}", grpid);
        List<MSRSession> sessionList
                = sessionController.findAllActiveByUserOrGroup(grpid);
        MSRSession session;
        if (sessionList.size() > 0) {
            session = sessionList.get(0);
        } else {
            session = new MSRSession();
            session.setUsrgrpid(grpid);
        }
        model.addAttribute("profileForm", session);
        //model.addAttribute(
        //        "patientname",
        //        userController.getRecord(grpid).getName()
        //        + " " + userController.getRecord(grpid).getSurname());

        return new ModelAndView("group-session");
    }
    

//    
//    @RequestMapping(value = "/easy", method = RequestMethod.GET)
//    @ResponseBody
//    public ModelAndView easy() {
//        return new ModelAndView("patient");
//    }
//    
//    @RequestMapping(value = "/medium", method = RequestMethod.GET)
//    @ResponseBody
//    public ModelAndView medium(Model model) {
//        model.addAttribute("patientid",3951);
//        return new ModelAndView("medium-session");
//    }
//
//    @RequestMapping(value = "/difficult", method = RequestMethod.GET)
//    @ResponseBody
//    public ModelAndView difficult(Model model) {
//        model.addAttribute("patientid",3951);
//        return new ModelAndView("difficult-session");
//    }
}