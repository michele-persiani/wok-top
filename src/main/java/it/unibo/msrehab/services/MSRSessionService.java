package it.unibo.msrehab.services;

import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.entities.*;

import static it.unibo.msrehab.model.entities.Exercise.ExerciseCategoryValue.*;

import it.unibo.msrehab.model.MSRSessionForm;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.util.CookiesManager;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
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
public class MSRSessionService
{

    private static final Logger logger = LoggerFactory
            .getLogger(MSRSessionService.class);
    private final MSRUserController userController;
    private final MSRGroupController groupController;
    private final ExerciseController exerciseController;
    private final MSRSessionController sessionController;
    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();

    public MSRSessionService()
    {
        super();
        this.userController = new MSRUserController(model);
        this.groupController = new MSRGroupController(model);
        this.exerciseController = new ExerciseController(model);
        this.sessionController = new MSRSessionController(model);
    }

    @RequestMapping(value = "/groupsessionmanagement", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView groupsessionmanagement(
            HttpServletRequest request,
            Model model)
    {

        int cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRGroup> groups = groupController.findAllGroupsInCenter(cid);
        List<MSRGroup> notEmptyGroups = new ArrayList<>();
        List<String> hospitalGroupNames = new ArrayList<>();
        List<String> homeGroupNames = new ArrayList<>();
        List<List<String>> hospitalExerciseNames = new ArrayList<>();
        List<List<String>> homeExerciseNames = new ArrayList<>();
        List<MSRSession> hospitalActiveSessions = sessionController.findAllHospitalActiveForGroups();
        List<MSRSession> homeActiveSessions = sessionController.findAllHomeActiveForGroups();
        List<MSRSession> hospitalActiveSessionsPerCurrentCenter = new ArrayList<>();
        List<MSRSession> homeActiveSessionsPerCurrentCenter = new ArrayList<>();

        for (MSRGroup group : groups)
            if (!userController.findAllPatientsInGroup(group.getId()).isEmpty())
                notEmptyGroups.add(group);

        if (notEmptyGroups.isEmpty())
        {
            model.addAttribute("message", "Nessun partecipante nei gruppi");
            model.addAttribute("back", "adminhome");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }

        for (MSRSession session : hospitalActiveSessions)
        {
            int grid = session.getUsrgrpid();
            for (MSRGroup group : groups)
            {
                if (group.getId() == grid)
                {
                    hospitalGroupNames.add(group.getName());
                    String exs = session.getExercises();
                    JSONArray exsJsonArr = new JSONArray(exs);
                    List<String> exerciseNamesGroup = new ArrayList<>();
                    for (int i = 0; i < exsJsonArr.length(); i++)
                    {
                        JSONObject exJson = exsJsonArr.getJSONObject(i);
                        int exId = exJson.getInt("id");
                        exerciseNamesGroup.add(
                                exerciseController.getEntity(exId).getFullName());
                    }
                    hospitalExerciseNames.add(exerciseNamesGroup);
                    hospitalActiveSessionsPerCurrentCenter.add(session);
                }
            }
        }
        for (MSRSession session : homeActiveSessions)
        {
            int grid = session.getUsrgrpid();
            for (MSRGroup group : groups)
            {
                if (group.getId() == grid)
                {
                    homeGroupNames.add(group.getName());
                    String exs = session.getExercises();
                    JSONArray exsJsonArr = new JSONArray(exs);
                    List<String> exerciseNamesGroup = new ArrayList<>();
                    for (int i = 0; i < exsJsonArr.length(); i++)
                    {
                        JSONObject exJson = exsJsonArr.getJSONObject(i);
                        int exId = exJson.getInt("id");
                        exerciseNamesGroup.add(
                                exerciseController.getEntity(exId).getFullName());
                    }
                    homeExerciseNames.add(exerciseNamesGroup);
                    homeActiveSessionsPerCurrentCenter.add(session);
                }
            }
        }
        if (hospitalActiveSessionsPerCurrentCenter.isEmpty() && homeActiveSessionsPerCurrentCenter.isEmpty())
            model.addAttribute("message", "Nessuna sessione attiva definita");


        model.addAttribute("newsession", notEmptyGroups.size() !=
                hospitalActiveSessionsPerCurrentCenter.size() + homeActiveSessionsPerCurrentCenter.size());
        model.addAttribute("hospitalsessions", hospitalActiveSessionsPerCurrentCenter);
        model.addAttribute("homesessions", homeActiveSessionsPerCurrentCenter);
        model.addAttribute("hospitalgroupnames", hospitalGroupNames);
        model.addAttribute("homegroupnames", homeGroupNames);
        model.addAttribute("hospitalexercisenames", hospitalExerciseNames);
        model.addAttribute("homeexercisenames", homeExerciseNames);
        return new ModelAndView("group-session-management");
    }


    private class ExerciseNamePlaceholder implements Comparable<ExerciseNamePlaceholder>
    {
        private MSRSession session;

        private MSRUser patient;

        private ExerciseNamePlaceholder(MSRSession session)
        {
            this.session = session;
            this.patient = userController.getEntity(session.getUsrgrpid());
        }

        public MSRSession getSession()
        {
            return session;
        }

        public String getPatientName()
        {
            return patient.getName() + " " + patient.getSurname();
        }

        public boolean isHospital()
        {
            return session.getHospital();
        }

        public boolean isHome()
        {
            return !session.getHospital();
        }

        public List<JSONObject> getExerciseNames()
        {
            String exs = session.getExercises();
            JSONArray exsJsonArr = new JSONArray(exs);
            List<JSONObject> exerciseNamesPatient = new ArrayList<>();

            for (int i = 0; i < exsJsonArr.length(); i++)
            {
                JSONObject exJson = exsJsonArr.getJSONObject(i);
                int exId = exJson.getInt("id");
                boolean visible = sessionController.getExerciseVisibilityInSession(session, exId);
                Exercise ex = exerciseController.getEntity(exId);
                String exName = exerciseController.getDisplayName(ex);
                String exElCategory = ex.getExelementcat() != null ? ex.getExelementcat().getDisplayName() : "";

                exJson.put("sessionid", session.getId());
                exJson.put("name", String.format("%s - %s", exName, exElCategory));
                exJson.put("visible", visible);
                exerciseNamesPatient.add(exJson);

            }
            exerciseNamesPatient.sort(Comparator.comparing(jobj -> jobj.getString("name")));
            return exerciseNamesPatient;
        }

        @Override
        public int compareTo(@NotNull MSRSessionService.ExerciseNamePlaceholder other)
        {
            return getPatientName().toLowerCase().compareTo(other.getPatientName().toLowerCase());
        }
    }


    @RequestMapping(value = "/patientsessionmanagement", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientsessionmanagement(
            HttpServletRequest request,
            Model model)
    {
        int cid = CookiesManager.getLoggedUserCenter(request);

        List<ExerciseNamePlaceholder> names = sessionController.getAllEntities()
                .stream()
                .filter(MSRSession::getActive)
                .map(ExerciseNamePlaceholder::new)
                .sorted()
                .collect(Collectors.toList());


        List<String> hospitalPatientNames = names.stream()
                .filter(ExerciseNamePlaceholder::isHospital)
                .map(ExerciseNamePlaceholder::getPatientName)
                .collect(Collectors.toList());

        List<String> homePatientNames = names.stream()
                .filter(ExerciseNamePlaceholder::isHome)
                .map(ExerciseNamePlaceholder::getPatientName)
                .collect(Collectors.toList());;

        List<List<JSONObject>> hospitalExerciseNames = names.stream()
                .filter(ExerciseNamePlaceholder::isHospital)
                .map(ExerciseNamePlaceholder::getExerciseNames)
                .collect(Collectors.toList());

        List<List<JSONObject>> homeExerciseNames = names.stream()
                .filter(ExerciseNamePlaceholder::isHome)
                .map(ExerciseNamePlaceholder::getExerciseNames)
                .collect(Collectors.toList());

        List<MSRSession> hospitalActiveSessions = names.stream()
                .filter(ExerciseNamePlaceholder::isHospital)
                .map(ExerciseNamePlaceholder::getSession)
                .collect(Collectors.toList());

        List<MSRSession> homeActiveSessions = names.stream()
                .filter(ExerciseNamePlaceholder::isHome)
                .map(ExerciseNamePlaceholder::getSession)
                .collect(Collectors.toList());

                    
        if (hospitalActiveSessions.isEmpty() && homeActiveSessions.isEmpty())
            model.addAttribute("message", "Nessuna sessione attiva definita");


        boolean newSession = userController.findAllPatientsNotInGroupsInCenter(cid)
                .stream()
                .anyMatch(patient -> sessionController.findAllActiveByUserOrGroup(patient.getId()).isEmpty());

        model.addAttribute("newsession", newSession);
        model.addAttribute("hospitalsessions", hospitalActiveSessions);
        model.addAttribute("homesessions", homeActiveSessions);
        model.addAttribute("hospitalpatientnames", hospitalPatientNames);
        model.addAttribute("homepatientnames", homePatientNames);
        model.addAttribute("hospitalexercisenames", hospitalExerciseNames);
        model.addAttribute("homeexercisenames", homeExerciseNames);
        return new ModelAndView("patient-session-management");
    }


    @RequestMapping(value = "/setexercisevisibility", method = RequestMethod.POST)
    public HttpEntity<String> setExerciseVisibiltiy(
            int sessionid,
            int exerciseid,
            boolean visible
    )
    {
        MSRSession session = sessionController.getEntity(sessionid);
        sessionController.setExerciseVisibilityInSession(session, exerciseid, visible);
        return ResponseEntity.ok("Success");
    }


    @RequestMapping(value = "/getexercisevisibility", method = RequestMethod.GET)
    public HttpEntity<Boolean> getExerciseVisibiltiy(
            int sessionid,
            int exerciseid
    )
    {
        MSRSession session = sessionController.getEntity(sessionid);
        boolean visible = sessionController.getExerciseVisibilityInSession(session, exerciseid);
        return ResponseEntity.ok(visible);
    }


    @RequestMapping(value = "/patientsession", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientsession(
            HttpServletRequest request,
            Model model)
    {
        Integer cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRUser> patients = userController.findAllPatientsNotInGroupsInCenter(cid);
        List<MSRUser> patientsWithNoSessions = patients
                .stream()
                .filter(patient -> sessionController.findAllActiveByUserOrGroup(patient.getId()).isEmpty())
                .collect(Collectors.toList());

        if(patientsWithNoSessions.isEmpty())
            return new ModelAndView("redirect:/patientsessionmanagement");


        model.addAttribute("patients", patientsWithNoSessions);

        Arrays.stream(Exercise.ExerciseCategoryValue.values())
                .forEach(cat ->
                        model.addAttribute(cat.toString(), exerciseController.findAllEnabledExercisesByCategory(cat))
                );
        MSRSessionForm sessionForm = new MSRSessionForm();
        model.addAttribute("sessionForm", sessionForm);
        return new ModelAndView("patient-session");

    }

    @RequestMapping(value = "/buildpatientsession", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView buildpatientsession(
            @ModelAttribute MSRSessionForm msrSessionForm
    )
    {
        
        MSRSession session = new MSRSession();
        session.setActive(Boolean.TRUE);
        session.setHospital(Boolean.TRUE);
        String difficulty = msrSessionForm.getDifficulty();
        if(difficulty == null)
            difficulty = "easy";
        session.setDifficulty(difficulty);
        session.setUsrgrpid(msrSessionForm.getUsrgrpid());
        session.setForgroup(Boolean.FALSE);
        session.setGrpid(-1);


        List<Integer> exerciseIds = new ArrayList<>();

        if(msrSessionForm.isAllSelected())
        {
            exerciseIds = exerciseController
                    .getAllEntities()
                    .stream()
                    .filter(Exercise::isEnabled)
                    .map(Exercise::getId)
                    .collect(Collectors.toList())
                    ;
        }
        else
        {
            JSONObject json = new JSONObject(msrSessionForm);
            json.remove("usrgrpid");
            json.remove("difficulty");
            json.remove("allSelected");

            JSONArray exCat = json.names();
            JSONArray exId = json.toJSONArray(exCat);


            for (int i = 0; i < exId.length(); i++)
            {
                JSONArray ids = exId.getJSONArray(i);
                for (int j = 0; j < ids.length(); j++)
                    exerciseIds.add(ids.getInt(j));
            }
        }
            JSONArray jsonArr = new JSONArray();

            exerciseIds.forEach(id -> {

                        JSONObject jobj =  new JSONObject()
                                .put("id", id)
                                .put("cat", exerciseController.findEntity(id).map(Exercise::getCategory).orElseThrow(() -> new RuntimeException("exercise not found!")))
                                .put("visible", true)
                                .put("done", false);
                        jsonArr.put(jobj);
                    });



            session.setExercises(jsonArr.toString());


        sessionController.insertEntity(session);
        
        return new ModelAndView("redirect:/patientsessionmanagement");
    }
    
    @RequestMapping(value = "/buildhomesession", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView buildhomesession(
            @RequestParam Integer sessionid,
            @RequestParam Boolean forpatient
    ) {

        MSRSession session = sessionController.getEntity(sessionid),
                lastHospitalSession;
        List<MSRUser> patients = new ArrayList<>();
        
        if (forpatient)
            patients.add(userController.getEntity(session.getUsrgrpid()));
        else
        {
            Integer grpid = session.getUsrgrpid();
            patients = userController.findAllPatientsInGroup(grpid);
        }

        for (MSRUser patient : patients)
        {
            lastHospitalSession = sessionController.findAllHospitalByUserOrGroup(patient.getId()).get(0);
            
            String hospitalExercises = lastHospitalSession.getExercises();

            JSONArray hospitalExs = new JSONArray(hospitalExercises),
                    homeExs = new JSONArray();

            JSONObject hospitalExJson;
            String exCat;
            List<Exercise> exList;

            for (int i = 0; i < hospitalExs.length(); i++)
            {
                hospitalExJson = hospitalExs.getJSONObject(i);
                homeExs.put(hospitalExJson);
            }

            MSRSession homePatientSession = new MSRSession();
            homePatientSession.setExercises(homeExs.toString());
            homePatientSession.setActive(Boolean.TRUE);
            homePatientSession.setHospital(Boolean.FALSE);
            homePatientSession.setDifficulty(lastHospitalSession.getDifficulty());
            homePatientSession.setForgroup(Boolean.FALSE);
            homePatientSession.setGrpid(lastHospitalSession.getGrpid());
            homePatientSession.setUsrgrpid(lastHospitalSession.getUsrgrpid());
            homePatientSession.setFromSessionId(lastHospitalSession.getId());
            sessionController.insertEntity(homePatientSession);

            lastHospitalSession.setActive(Boolean.FALSE);
            lastHospitalSession.setHospital(Boolean.FALSE);
            sessionController.updateEntity(lastHospitalSession);
        }

        session.setActive(Boolean.FALSE);
        session.setHospital(Boolean.FALSE);
        sessionController.updateEntity(session);

        if (forpatient)
            return new ModelAndView("redirect:/patientsessionmanagement");

        MSRSession homeGroupSession = new MSRSession();
        homeGroupSession.setActive(Boolean.TRUE);
        homeGroupSession.setDifficulty(session.getDifficulty());
        homeGroupSession.setExercises(session.getExercises());
        homeGroupSession.setForgroup(Boolean.TRUE);
        homeGroupSession.setFromSessionId(sessionid);
        homeGroupSession.setGrpid(-1);
        homeGroupSession.setHospital(Boolean.FALSE);
        homeGroupSession.setUsrgrpid(session.getUsrgrpid());

        sessionController.insertEntity(homeGroupSession);

        return new ModelAndView("redirect:/groupsessionmanagement");

    }
    
    @RequestMapping(value = "/groupsession", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView groupsession(
            HttpServletRequest request,
            Model model)
    {
        
        int cid = CookiesManager.getLoggedUserCenter(request);

        List<MSRGroup> groups = groupController.findAllGroupsInCenter(cid);
        List<MSRGroup> notEmptygroupsWithNoActiveSessions = new ArrayList<MSRGroup>();
        for(MSRGroup group: groups)
            if (sessionController.findAllActiveByUserOrGroup(group.getId()).isEmpty() && !userController.findAllPatientsInGroup(group.getId()).isEmpty() )
                notEmptygroupsWithNoActiveSessions.add(group);

        if(notEmptygroupsWithNoActiveSessions.isEmpty())
            return new ModelAndView("redirect:/groupsessionmanagement");

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

    @RequestMapping(value = "/buildgroupsession", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView buildgroupsession(
            @ModelAttribute MSRSessionForm msrSessionForm
    )
    {
        
        MSRSession session = new MSRSession();
        session.setActive(Boolean.TRUE);
        session.setHospital(Boolean.TRUE);
        session.setDifficulty(msrSessionForm.getDifficulty());
        session.setUsrgrpid(msrSessionForm.getUsrgrpid());
        
        JSONObject json = new JSONObject(msrSessionForm);
        json.remove("usrgrpid");
        json.remove("difficulty");

        JSONArray exCat = json.names();
        if(exCat==null)
            return new ModelAndView("redirect:/groupsession");

        JSONArray exId = json.toJSONArray(exCat);

        JSONArray jsonArr = new JSONArray();

        List<String> allCategoryNames = Arrays.stream(Exercise.ExerciseCategoryValue.values()).map(Exercise.ExerciseCategoryValue::toString).collect(Collectors.toList());

        for(int i=0; i<exCat.length(); i++)
        {
            String categoryName = exCat.getString(i);
            if(!allCategoryNames.contains(categoryName)) // To filter the allSelectedField
                continue;
            JSONArray ids = json.getJSONArray(categoryName);
            for(int j = 0; j < ids.length(); j++)
            {
                if(ids.isNull(j))
                    continue;
                JSONObject json1 = new JSONObject()
                        .put("id", ids.getInt(j))
                        .put("cat", categoryName)
                        .put("done", false);
                jsonArr.put(json1);
            }

        }

        session.setExercises(jsonArr.toString());
        session.setForgroup(true);
        session.setGrpid(-1);
        // TODO: manage failure of addRecord
        sessionController.insertEntity(session);

        List<MSRUser> patients =
                userController.findAllPatientsInGroup(msrSessionForm.getUsrgrpid());

        for(MSRUser patient: patients)
        {
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
            @RequestParam Integer sessionid,
            @RequestParam Boolean forpatient,
            Model model,
            final RedirectAttributes redirectAttributes)
    {
        
        logger.info("Start to delete session " + sessionid);
        MSRSession session =  sessionController.getEntity(sessionid);

        sessionController.removeEntity(session);
        logger.info("Session " + sessionid + " deleted with success");
        /* TODO Check the following two lines */
        redirectAttributes.addFlashAttribute("css", "success");
        redirectAttributes.addFlashAttribute("msg", "Session is deleted!");

        if(forpatient)
            return new ModelAndView("redirect:/patientsessionmanagement");

        List<MSRSession> patientGroupSessions = sessionController.findAllForPatientsInGroup(session.getUsrgrpid());
        for(MSRSession sess: patientGroupSessions)
            sessionController.removeEntity(sess);

        List<MSRSession> otherGroupSessions = sessionController.findAllByUserOrGroup(session.getUsrgrpid());
        for(MSRSession sess: otherGroupSessions)
            sessionController.removeEntity(sess);

        return new ModelAndView("redirect:/groupsessionmanagement");

    }

    
    // edit group session
    @RequestMapping(value = "/editgroupsession", method = RequestMethod.POST)
    public ModelAndView editgroupsession(
            @RequestParam(value = "session", required = true) Integer grpid,
            Model model)
    {

        logger.debug("editgroupsession() : {}", grpid);
        List<MSRSession> sessionList = sessionController.findAllActiveByUserOrGroup(grpid);
        MSRSession session;
        if (!sessionList.isEmpty())
            session = sessionList.get(0);
        else
        {
            session = new MSRSession();
            session.setUsrgrpid(grpid);
        }
        model.addAttribute("profileForm", session);
        return new ModelAndView("group-session");
    }
}