package it.unibo.msrehab.services;

import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.entities.*;
import it.unibo.msrehab.model.entities.Exercise.ExerciseCategoryValue;

import it.unibo.msrehab.model.entities.MSRUser.RoleValue;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.HistoryController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.controller.ClinicalProfileController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.NPSTestController;
import it.unibo.msrehab.model.controller.PersonalDataController;
import it.unibo.msrehab.model.controller.RaoTestController;
import it.unibo.msrehab.model.controller.WhiteTestController;
import it.unibo.msrehab.util.*;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


/**
 * Class that provides servlets for system users. The services are the
 * following: newUser inserts a user entry in the database; updateUser: updates
 * a user entry; deletePatient: deletes a user entry; getUser: retrieves a user
 * entry
 */
@Controller
public class MSRUserService
{
    private static final Logger logger = LoggerFactory
            .getLogger(MSRUserService.class);
    private final MSRUserController userController;
    private final MSRGroupController groupController;
    private final ClinicalProfileController profileController;
    private final NPSTestController npsTestController;
    private final PersonalDataController personalDataController;
    private final RaoTestController raoTestController;
    private final WhiteTestController whiteTestController;
    private final MSRSessionController sessionController;
    private final ExerciseController exerciseController;
    private final HistoryController historyController;
    private final Configuration config;

    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();


    public MSRUserService()
    {
        super();
        this.userController = new MSRUserController(model);
        this.groupController = new MSRGroupController(model);
        this.profileController = new ClinicalProfileController(model);
        this.npsTestController = new NPSTestController(model);
        this.personalDataController = new PersonalDataController(model);
        this.raoTestController = new RaoTestController(model);
        this.whiteTestController = new WhiteTestController(model);
        this.sessionController = new MSRSessionController(model);
        this.exerciseController = new ExerciseController(model);
        this.historyController = new HistoryController(model);

        this.config = Configuration.getInstance();
    }



    @RequestMapping(value = "/patientform", method = RequestMethod.POST)
    public ModelAndView patientForm(
            @RequestParam(required = false) Integer patientid,
            Model model)
    {

        logger.debug("patientForm() : {}", patientid);
        MSRUser patient = new MSRUser();

        if (patientid != null)
            patient = userController.getEntity(patientid);

        
        model.addAttribute("patientid", patientid); 
        model.addAttribute("patientForm", patient);
        return new ModelAndView("patient-form");
    }


    @RequestMapping(value = "/deletepatient", method = RequestMethod.POST)
    public ModelAndView deletePatient(
            @RequestParam Integer patientid
    )
    {
        logger.info("Start to delete user " + patientid);
        MSRUser user = userController.getEntity(patientid);


        // Delete patient group if it contains only the patient being deleted
        if (userController.findAllPatientsInGroup(user.getGid()).size() <= 1)
        {
            MSRGroup group = groupController.getEntity(user.getGid());
            groupController.removeEntity(group);
        }

        List<ClinicalProfile> profiles = profileController.findAllByUser(patientid);
        for (ClinicalProfile profile : profiles)
            profileController.removeEntity(profile);

        List<NPSTest> npsTests = npsTestController.findAllByUser(patientid);
        for (NPSTest npsTest : npsTests)
            npsTestController.removeEntity(npsTest);

        List<PersonalData> personalData = personalDataController.findAllByUser(patientid);
        for (PersonalData pd : personalData)
            personalDataController.removeEntity(pd);

        List<RaoTest> raoTests = raoTestController.findAllByUser(patientid);
        for (RaoTest raoTest : raoTests)
            raoTestController.removeEntity(raoTest);

        List<WhiteTest> whiteTests = whiteTestController.findAllByUser(patientid);
        for (WhiteTest whiteTest : whiteTests)
            whiteTestController.removeEntity(whiteTest);

        List<MSRSession> sessions = sessionController.findAllByUserOrGroup(patientid);
        for (MSRSession session : sessions)
            sessionController.removeEntity(session);


        List<History> histories = historyController.findAllByUser(patientid);
        for(History history : histories)
            historyController.removeEntity(history);


        // Delete photo
        String photoFile = user.getPhoto();
        if (photoFile != null)
        {
            String photosPath = config.getPhotosPath();
            String filePath = photosPath + File.separator + photoFile;
            File file = new File(filePath);
            file.delete();
        }
        userController.removeEntity(user);

        logger.info("User " + patientid + " deleted with success");
        return new ModelAndView("redirect:/showpatients");
    }


    /**
     * Retrieves user's info given user's email and password
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param response
     *
     * @return the user in JSON format if the user is found, -1 otherwise
     * HttpStatus.NOT_ACCEPTABLE if email or password are not valid;
     * HttpStatus.NO_CONTENT if the user is not present in the database;
     * HttpStatus.OK if the user is found in the database
     */
   @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
   public ModelAndView login(
           HttpServletResponse response,
           @RequestParam String username,
           @RequestParam String password
   )
   {
       logger.info("Look for the user username: " + username  + " password: " + password);


     MSRUser user = userController.findByUsernameAndPassword(username, password);

      if (user == null)
      {
          logger.info("...no user found in the database");
          return new ModelAndView("login", "message", "Credenziali non valide");
      }

       Cookie ck = new Cookie("username", user.getUsername());
       Cookie ck1 = new Cookie("name", user.getName());
       Cookie ck2 = new Cookie("id", user.getId().toString());
       Cookie ck3 = new Cookie("role", user.getMsrrole().toString());
       Cookie ck4 = new Cookie("cid", user.getCid().toString());

       response.addCookie(ck);
       response.addCookie(ck1);
       response.addCookie(ck2);
       response.addCookie(ck3);
       response.addCookie(ck4);

      if (user.getMsrrole() == RoleValue.ADMIN)
          return new ModelAndView("redirect:adminhome");
      else
          return new ModelAndView("redirect: patienthome");

   }


   
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView logout(
            HttpServletRequest request,
            HttpServletResponse response
    )
    {
        if (!CookiesManager.isUserLogged(request))
            return new ModelAndView("login");

        Cookie ck = new Cookie("id", "");
        ck.setMaxAge(0);
        response.addCookie(ck);
        return new ModelAndView("login");

    }


    // TODO Funzione disabilitata
    @RequestMapping(value = "/forgottencredential", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView forgottencredential(
            @RequestParam String email
    )
    {

        logger.info("Look for the user with email: " + email);

        if (!MSRUser.isEmailValid(email)) {
            logger.warn("...given email is not valid");
            return new ModelAndView("login", "message", "Email non valida");
        }

        MSRUser user = userController.findRecordByEmail(email);
        if (user == null)
        {
            logger.info("...no user found in the database");
            return new ModelAndView("login", "message", "Email non valida");
        }
        logger.info("...user found with the given email");

        SendMail.sendCredentials(user);

        return new ModelAndView("login");

    }



    @RequestMapping(value = "/adminhome", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView adminhome(
            HttpServletRequest request,
            Model model)
    {

        if (!CookiesManager.isUserLogged(request) || !CookiesManager.isUserAdmin(request))
            return new ModelAndView("login");

        int cid = CookiesManager.getLoggedUserCenter(request);

        model.addAttribute("name", CookiesManager.geLoggedUserName(request));
        List<MSRUser> centerUsers = userController.findAllPatientsInCenter(cid);
        List<MSRGroup> centerGroups = groupController.findAllGroupsInCenter(cid);

        if (centerUsers.isEmpty())
            model.addAttribute("existpatients", false);
        else
            model.addAttribute("existpatients", true);


        if (centerGroups.isEmpty())
            model.addAttribute("existgroups", false);
        else
            model.addAttribute("existgroups", true);


        return new ModelAndView("administrator");

    }

    /**
    Visualizza gli esercizi di training per l'utente demo
     */
    @RequestMapping(value = "/patienttraining", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patienttraining(HttpServletRequest request, Model model)
    {
        if (!CookiesManager.isUserLogged(request) || !CookiesManager.isUserPatientOrDemo(request))
            return new ModelAndView("login");

        JSONArray jArr = new JSONArray(
                "[{\"cat\":\"ATT_SEL_STD\",\"id\":1,\"done\":false},"
                        + "{\"cat\":\"ATT_SEL_STD_FAC\",\"id\":6,\"done\":false},"
                        + "{\"cat\":\"ATT_SEL_STD_ORI\",\"id\":47,\"done\":false},"
                        + "{\"cat\":\"ATT_SEL_FLW\",\"id\":8,\"done\":false},"
                        + "{\"cat\":\"ATT_SEL_FLW_FAC\",\"id\":12,\"done\":false},"
                        + "{\"cat\":\"ATT_SEL_FLW_ORI\",\"id\":48,\"done\":false},"
                        //+ "{\"cat\":\"ATT_ALT\",\"id\":15,\"done\":false},"
                        //+ "{\"cat\":\"ATT_ALT_FAC\",\"id\":18,\"done\":false},"
                        //+ "{\"cat\":\"ATT_ALT_ORI\",\"id\":49,\"done\":false},"
                        + "{\"cat\":\"ATT_DIV\",\"id\":20,\"done\":false},"
                        + "{\"cat\":\"ATT_DIV_FAC\",\"id\":24,\"done\":false},"
                        + "{\"cat\":\"ATT_DIV_ORI\",\"id\":46,\"done\":false},"
                        //+ "{\"cat\":\"MEM_VIS_1\",\"id\":25,\"done\":false},"
                        //+ "{\"cat\":\"MEM_VIS_1_FAC\",\"id\":30,\"done\":false},"
                        //+ "{\"cat\":\"MEM_VIS_1_ORI\",\"id\":50,\"done\":false},"
                        + "{\"cat\":\"MEM_VIS_2\",\"id\":34,\"done\":false},"
                        //+ "{\"cat\":\"MEM_VIS_5\",\"id\":55,\"done\":false},"
                        + "{\"cat\":\"NBACK\",\"id\":38,\"done\":false},"
                        + "{\"cat\":\"NBACK_FAC\",\"id\":40,\"done\":false},"
                        + "{\"cat\":\"NBACK_ORI\",\"id\":41,\"done\":false},"
                        //CHECK
                        //+ "{\"cat\":\"MEM_LONG_1\",\"id\":51,\"done\":false},"
                        //+ "{\"cat\":\"RES_INH\",\"id\":43,\"done\":false},"
                        //+ "{\"cat\":\"PLAN_1\",\"id\":52,\"done\":false},"
                        //+ "{\"cat\":\"PLAN_2\",\"id\":53,\"done\":false},"
                        //+ "{\"cat\":\"PLAN_3\",\"id\":58,\"done\":false},"
                        + "{\"cat\":\"ATT_RFLXS\",\"id\":65,\"done\":false}"
                        + "]"
        );

        JSONObject json;
        String cat;
        int id;

        // HashMap (Categoria: Esercizi)
        Map<String, ExerciseCategory> categoryExerciseMap = new TreeMap<>();

        for (ExerciseCategoryValue exerciseCategoryValue : ExerciseCategoryValue.values())
        {
            ArrayList<Exercise> tmpAllExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpToDoExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpDoneExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpInterruptedExerciseList = new ArrayList<>();
            categoryExerciseMap.put(exerciseCategoryValue.toString(), new ExerciseCategory(tmpAllExerciseList,
                    tmpToDoExerciseList,
                    tmpDoneExerciseList,
                    tmpInterruptedExerciseList,
                    0, exerciseCategoryValue.toString()));
        }


        for (int i = 0; i < jArr.length(); i++)
        {
            json = jArr.getJSONObject(i);
            cat = json.getString("cat");
            id = json.getInt("id");
            Exercise exercise = exerciseController.getEntity(id);
            // Inserisco l'esercizio nella lista degli esercizi ( inq questo cosa non serve differenziare lo stato)
            ExerciseCategory exerciseCategory = categoryExerciseMap.get(cat);
            exerciseCategory.getExercises().add(exercise);
        }


        // HashMap ( Gruppo : Categoria)
        Map<String, CategoryGroup> groupMap = new LinkedHashMap<>();
        // Inserisco le keys nell'ordine voluto
        groupMap.put("ATTENTION_FIG", new CategoryGroup("ATTENTION_FIG"));
        groupMap.put("ATTENTION_FAC", new CategoryGroup("ATTENTION_FAC"));
        groupMap.put("ATTENTION_ORI", new CategoryGroup("ATTENTION_ORI"));
        groupMap.put("MEMORY_FIG", new CategoryGroup("MEMORY_FIG"));
        groupMap.put("MEMORY_FAC", new CategoryGroup("MEMORY_FAC"));
        groupMap.put("MEMORY_ORI", new CategoryGroup("MEMORY_ORI"));
        groupMap.put("EX_FUNC", new CategoryGroup("EX_FUNC"));
        groupMap.put("ATT_RFLXS", new CategoryGroup("ATT_RFLXS"));

        for(String category : categoryExerciseMap.keySet())
        {
            ExerciseCategory exerciseCategory = categoryExerciseMap.get(category);
            if (!exerciseCategory.getExercises().isEmpty())
            {
                exerciseCategory.setProgress(this.getCategoryProgress(exerciseCategory.getExercises()));
                String categoryGroup = this.getGroupForCategory(category);
                groupMap.get(categoryGroup).addExerciseCategory(exerciseCategory);
            }
        }


        Integer userId = CookiesManager.geLoggedUserId(request);
        model.addAttribute("groupMap", groupMap);
        model.addAttribute("patientid", userId);
        model.addAttribute("difficulty", "training");
        model.addAttribute("name", CookiesManager.geLoggedUserName(request));
        model.addAttribute("sessid", -1);

        return new ModelAndView("patient-training");

    }


    @RequestMapping(value = "/patientdemo", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientdemo(HttpServletRequest request, Model model)
    {
        if (!CookiesManager.isUserLogged(request) || !CookiesManager.isUserPatientOrDemo(request))
            return new ModelAndView("login");

        JSONArray jArr = new JSONArray(
                "[{\"cat\":\"ATT_SEL_STD\",\"id\":1,\"done\":false},"
                        + "{\"cat\":\"ATT_SEL_FLW\",\"id\":8,\"done\":false},"
                        + "{\"cat\":\"MEM_VIS_1\",\"id\":25,\"done\":false},"
                        + "{\"cat\":\"NBACK\",\"id\":38,\"done\":false},"
                        //CHECK
                        //+ "{\"cat\":\"MEM_LONG_1\",\"id\":51,\"done\":false},"
                        + "{\"cat\":\"RES_INH\",\"id\":43,\"done\":false}]"
        );

        JSONObject json;
        String cat;
        int id;

        // HashMap (Categoria: Esercizi)
        Map<String, ExerciseCategory> categoryExerciseMap = new TreeMap<>();

        for (ExerciseCategoryValue exerciseCategoryValue : ExerciseCategoryValue.values())
        {
            ArrayList<Exercise> tmpAllExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpToDoExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpDoneExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpInterruptedExerciseList = new ArrayList<>();
            categoryExerciseMap.put(exerciseCategoryValue.toString(), new ExerciseCategory(tmpAllExerciseList,
                    tmpToDoExerciseList,
                    tmpDoneExerciseList,
                    tmpInterruptedExerciseList,
                    0, exerciseCategoryValue.toString()));
        }


        for (int i = 0; i < jArr.length(); i++)
        {
            json = jArr.getJSONObject(i);
            cat = json.getString("cat");
            id = json.getInt("id");
            Exercise exercise = exerciseController.getEntity(id);
            // Inserisco l'esercizio nella lista degli esercizi (in questo cosa non serve differenziare lo stato)
            ExerciseCategory exerciseCategory = categoryExerciseMap.get(cat);
            exerciseCategory.getExercises().add(exercise);
        }


        // HashMap ( Gruppo : Categoria)
        Map<String, CategoryGroup> groupMap = new LinkedHashMap<>();
        // Inserisco le keys nell'ordine voluto
        groupMap.put("ATTENTION_FIG", new CategoryGroup("ATTENTION_FIG"));
        groupMap.put("MEMORY_FIG", new CategoryGroup("MEMORY_FIG"));
        groupMap.put("EX_FUNC", new CategoryGroup("EX_FUNC"));

        for(String category : categoryExerciseMap.keySet())
        {
            ExerciseCategory exerciseCategory = categoryExerciseMap.get(category);
            if (!exerciseCategory.getExercises().isEmpty())
            {
                exerciseCategory.setProgress(this.getCategoryProgress(exerciseCategory.getExercises()));
                String categoryGroup = this.getGroupForCategory(category);
                groupMap.get(categoryGroup).addExerciseCategory(exerciseCategory);
            }
        }


        model.addAttribute("groupMap", groupMap);
        model.addAttribute("patientid", -1);
        model.addAttribute("difficulty", "demo");
        model.addAttribute("name", CookiesManager.geLoggedUserName(request));
        model.addAttribute("sessid", -1);

        return new ModelAndView("patient-demo");

    }



    @RequestMapping(value = "/instructions", method = RequestMethod.GET)
    public ModelAndView instructions()
    {
        return new ModelAndView("instructions");
    }


    @RequestMapping(value = "/patienthome", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patienthome(HttpServletRequest request, Model model)
    {
        if (!CookiesManager.isUserLogged(request) || !CookiesManager.isUserPatientOrDemo(request))
            return new ModelAndView("login");

        Integer userId = CookiesManager.geLoggedUserId(request);
        MSRUser user = userController.getEntity(userId);
        boolean demo = user.getMsrrole() == RoleValue.DEMO;
        String userName = CookiesManager.geLoggedUserName(request);

        model.addAttribute("name", userName);
        model.addAttribute("id", userId);
        model.addAttribute("demo", demo);

        return new ModelAndView("patient");

    }


    @RequestMapping(value = "/patientrehabilitation", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientrehabilitation(HttpServletRequest request, Model model)
    {
        if (!CookiesManager.isUserLogged(request) || !CookiesManager.isUserPatientOrDemo(request))
            return new ModelAndView("login");

        List<MSRSession> sessionList = sessionController.findAllActiveByUserOrGroup(CookiesManager.geLoggedUserId(request))
                .stream()
                .sorted(Comparator.comparing(sess -> -sess.getId())) // Last inserted is first (by sequence of ids)
                .collect(Collectors.toList());

        if (sessionList.isEmpty() || sessionList.stream().noneMatch(sessionController::hasVisibleExercises))
        {
            model.addAttribute("back", "patienthome");
            model.addAttribute("home", "patienthome");
            model.addAttribute("message", "Non hai esercizi assegnati");
            return new ModelAndView("error");
        }

        /*
        There could be multiple sessions per user because of groups
         */
        MSRSession session = sessionList.get(0);                // The first session is the most recent one
        JSONArray jArr = new JSONArray(session.getExercises());

        JSONObject json;
        String cat;
        int id;
        boolean done;
        Integer lastInterruptedExerciseId = 0;
        List<Exercise> interruptedExercisesList = new ArrayList<>();
        // HashMap (Categoria: Esercizi)
        Map<String, ExerciseCategory> categoryExerciseMap = new TreeMap<>();

        for (ExerciseCategoryValue exerciseCategoryValue : ExerciseCategoryValue.values())
        {
            ArrayList<Exercise> tmpAllExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpToDoExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpDoneExerciseList = new ArrayList<>();
            ArrayList<Exercise> tmpInterruptedExerciseList = new ArrayList<>();
            categoryExerciseMap.put(exerciseCategoryValue.toString(), new ExerciseCategory(tmpAllExerciseList,
                    tmpToDoExerciseList,
                    tmpDoneExerciseList,
                    tmpInterruptedExerciseList,
                    0, exerciseCategoryValue.toString()));
        }


        for (int i = 0; i < jArr.length(); i++)
        {
            json = jArr.getJSONObject(i);


            cat = json.getString("cat");

            id = json.getInt("id");
            done = json.getBoolean("done");

            if(!sessionController.getExerciseVisibilityInSession(session, id))
                continue;


            Exercise exercise = exerciseController.getEntity(id);
            exercise.setStatus(historyController.isExerciseInHistory(id, session.getId()), done);
            // se l'esercizio è da iniziare, controllo se il paziente lo aveva già iniziato durante la sessione in ospedale
            if (exercise.getStatus().equals("todo") && session.getFromSessionId() != null) {
                exercise.setStatus(historyController.isExerciseInHistory(id, session.getFromSessionId()), false);
            }
            // Inserisco l'esercizio nella lista correspondente al suo status
            ExerciseCategory exerciseCategory = categoryExerciseMap.get(cat);
            if ("interrupted".equals(exercise.getStatus())) {
                exerciseCategory.getInterruptedExercises().add(exercise);
                // Lista di tutti gli esercizi interrotti per calcolare l'ultimo interrotto
                interruptedExercisesList.add(exercise);
            } else if ("todo".equals(exercise.getStatus())) {
                exerciseCategory.getToDoExercises().add(exercise);
            } else if ("done".equals(exercise.getStatus())) {
                exerciseCategory.getCompletedExercises().add(exercise);
            }
        }


        if (!interruptedExercisesList.isEmpty())
        {
            List<Integer> interruptedExsIds = new ArrayList<>();
            for (Exercise ex : interruptedExercisesList)
                interruptedExsIds.add(ex.getId());

            lastInterruptedExerciseId = historyController.getLastInterruptedExercise(interruptedExsIds, session.getId(), session.getFromSessionId());
        }


        String lastInterruptedExerciseCategory = null;

        for (Exercise ex : interruptedExercisesList)
            if (ex.getId() == lastInterruptedExerciseId)
                lastInterruptedExerciseCategory = ex.getCategory().toString();


        for (ExerciseCategory exerciseCategory : categoryExerciseMap.values())
        {
            int interruptedId = lastInterruptedExerciseId;
            Exercise lastInterruptedEx = exerciseCategory.getInterruptedExercises()
                    .stream()
                    .filter(ex -> ex.getId() == interruptedId)
                    .findFirst()
                    .orElse(null);

            if (lastInterruptedEx != null)
                Collections.swap(exerciseCategory.getInterruptedExercises(), exerciseCategory.getInterruptedExercises().indexOf(lastInterruptedEx), 0);

            // Aggiungo gli esercizi alla lista completa degli esercizi ordinati a seconda del loro stato
            exerciseCategory.getExercises().addAll(exerciseCategory.getInterruptedExercises());
            exerciseCategory.getExercises().addAll(exerciseCategory.getToDoExercises());
            exerciseCategory.getExercises().addAll(exerciseCategory.getCompletedExercises());
        }

        // HashMap ( Gruppo : Categoria)
        Map<String, CategoryGroup> groupMap = new LinkedHashMap<>();
        // Inserisco le keys nell'ordine voluto
        groupMap.put("ATTENTION_FIG", new CategoryGroup("ATTENTION_FIG"));
        groupMap.put("ATTENTION_FAC", new CategoryGroup("ATTENTION_FAC"));
        groupMap.put("ATTENTION_ORI", new CategoryGroup("ATTENTION_ORI"));
        groupMap.put("MEMORY_FIG", new CategoryGroup("MEMORY_FIG"));
        groupMap.put("MEMORY_FAC", new CategoryGroup("MEMORY_FAC"));
        groupMap.put("MEMORY_ORI", new CategoryGroup("MEMORY_ORI"));
        groupMap.put("EX_FUNC", new CategoryGroup("EX_FUNC"));
        groupMap.put("ATTENTION_RFLXS", new CategoryGroup("ATTENTION_RFLXS"));

        for(String category : categoryExerciseMap.keySet())
        {
            ExerciseCategory exerciseCategory = categoryExerciseMap.get(category);
            if (!exerciseCategory.getExercises().isEmpty())
            {
                exerciseCategory.setProgress(this.getCategoryProgress(exerciseCategory.getExercises()));
                String categoryGroup = this.getGroupForCategory(category);
                if (!groupMap.containsKey(categoryGroup)) {
                    groupMap.put(categoryGroup, new CategoryGroup(categoryGroup));
                }
                groupMap.get(categoryGroup).addExerciseCategory(exerciseCategory);
            }
        }


        model.addAttribute("groupMap", groupMap);
        model.addAttribute("lastInterruptedExerciseCategory", lastInterruptedExerciseCategory);
        model.addAttribute("lastInterruptedExercise", lastInterruptedExerciseId);
        model.addAttribute("patientid", session.getUsrgrpid());
        model.addAttribute("difficulty", session.getDifficulty());
        model.addAttribute("name", CookiesManager.geLoggedUserName(request));
        model.addAttribute("sessid", session.getId());
        model.addAttribute("inHospital", session.getHospital());

        return new ModelAndView("patient-rehabilitation");

    }


    private String getGroupForCategory(String key)
    {
        if (key.matches("ATT_SEL_STD|ATT_SEL_FLW|ATT_ALT|ATT_DIV")) {
            return "ATTENTION_FIG";
        } else if (key.matches("ATT_SEL_STD_FAC|ATT_SEL_FLW_FAC|ATT_ALT_FAC|ATT_DIV_FAC")) {
            return "ATTENTION_FAC";
        } else if (key.matches("ATT_SEL_STD_ORI|ATT_SEL_FLW_ORI|ATT_ALT_ORI|ATT_DIV_ORI")) {
            return "ATTENTION_ORI";
        } else if (key.matches("MEM_VIS_1|MEM_VIS_2|NBACK|MEM_VIS_5")) {
            return "MEMORY_FIG";
        } else if (key.matches("MEM_VIS_1_FAC|MEM_VIS_2_FAC|NBACK_FAC|MEM_LONG_1")) {
            return "MEMORY_FAC";
        } else if (key.matches("MEM_VIS_1_ORI|NBACK_ORI")) {
            return "MEMORY_ORI";
        } else if (key.matches("RES_INH|PLAN_1|PLAN_2|PLAN_3")) {
            return "EX_FUNC";
        } else if (key.matches("ATT_RFLXS")) {
            return "ATT_RFLXS";
        }
        return "OTHER";
    }



    // list page
    @RequestMapping(value = "/showpatientsold", method = RequestMethod.GET)
    public ModelAndView showpatientsold(HttpServletRequest request)
    {
        logger.debug("showpatients()");
        if (!CookiesManager.isUserLogged(request))
            return new ModelAndView("login");

        int cid = CookiesManager.getLoggedUserCenter(request);
        return new ModelAndView("patient-management", "patients", userController.findAllPatientsInCenter(cid));

    }


    @RequestMapping(value = "/showpatients", method = RequestMethod.GET)
    public ModelAndView showPatients(
            HttpServletRequest request,
            Model model
    )
    {
        logger.debug("showCenterPatients()");
        if (!CookiesManager.isUserLogged(request))
            return new ModelAndView("login");

        int uid = CookiesManager.geLoggedUserId(request);
        int cid = CookiesManager.getLoggedUserCenter(request);

        List<MSRUser> patients = userController.findAllPatientsInCenter(cid);
        if (!patients.isEmpty())
        {
            model.addAttribute("patients", patients);
            model.addAttribute("email", userController.getEntity(uid).getEmail());
            List<MSRGroup> groups = new ArrayList<MSRGroup>();
            for (MSRUser patient : patients)
                groupController.findEntity(patient.getGid()).ifPresent(groups::add);

            model.addAttribute("groups", groups);
            model.addAttribute("allgroups", groupController.findAllGroupsInCenter(cid));
        }
        else
            model.addAttribute("message", "Nessun partecipante inserito nel sistema");


        return new ModelAndView("patient-management");
    }


    // save or update patients
    @RequestMapping(value = "/saveorupdatepatient", method = RequestMethod.POST)
    public ModelAndView saveOrUpdatePatient(
            HttpServletRequest request,
            @Valid @ModelAttribute("patientForm") MSRUser patientForm,
            @RequestParam(required = false) MultipartFile photofile
    )
    {

        logger.debug("saveOrUpdateUser() : {}", patientForm);


        if (photofile != null && !photofile.isEmpty() && false) // DEACTIVATED
        {
            try
            {
                byte[] bytes = photofile.getBytes();

                Image img = ImageIO.read(new ByteArrayInputStream(bytes));

                BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = bufferedImage.createGraphics();
                graphics2D.drawImage(img, 0, 0, 800, 800, null);
                graphics2D.dispose();


                // Create the file on server
                String fileName = System.currentTimeMillis() + ".jpg";
                String filePath = PhotoManager.getInstance().getPhotoUrl(request, fileName);
                File serverFile = new File(filePath);
                serverFile.createNewFile();
                ImageIO.write(bufferedImage, "jpg", serverFile);

                logger.info("Server File Location=" + serverFile.getAbsolutePath());

                patientForm.setPhoto(fileName);

            } catch (Exception e)
            {
                return new ModelAndView("patient-form");
            }
        }


        MSRUser user = new MSRUser();

        if (patientForm.getId() != null)
            user = userController.getEntity(patientForm.getId());

        user.setMsrrole(patientForm.getMsrrole());
        user.setName(patientForm.getName());
        user.setSurname(patientForm.getSurname());
        user.setEmail(patientForm.getEmail());
        user.setPhone(patientForm.getPhone());
        user.setNotificationsEnabled(patientForm.getNotificationsEnabled());

        if (user.isNew())
        {
            user.setGid(-1);
            user.setCid(CookiesManager.getLoggedUserCenter(request));

            String pwd = "pass" + RandomStringUtils.random(3, false, true);
            String usr = "User" + RandomStringUtils.random(5, false, true);
            user.setUsername(usr);
            user.setPassword(pwd);
        }
        userController.putEntity(user);
        logger.info("Update of  user " + patientForm.getId() + " terminated with success");
        return new ModelAndView("redirect:/showpatients");
    }


    @RequestMapping(value = "/exportpatients", method = RequestMethod.POST)
    public ModelAndView exportpatients(
            @RequestParam String email,
            Model model)
    {
        logger.debug("exportpatients()");

        if (CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "msruser", "msrrole='PATIENT'") == 0)
        {
            logger.debug("... export of patients succeeded");
            SendMail.sendMail(email, "Pazienti", "Dati di tutti i pazienti", config.getCsvsPath() + File.separator + "msruser.csv");
            return new ModelAndView("redirect:/showpatients");
        }
        else
        {
            logger.debug("... export patients failed");
            model.addAttribute("message", "Esportazione pazienti fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }

    }



    /**
     * Exports patient history
     *
     * @param email
     * @param model
     * @return
     */
    @RequestMapping(value = "/exportpatienthistory", method = RequestMethod.POST)
    public ModelAndView exportpatienthistory(
            @RequestParam String email,
            Model model)
    {
        logger.debug("exportpatienthistory()");

        if (CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "history", "1") == 0)
        {
            logger.debug("... export of patients succeeded");
            SendMail.sendMail(email, "Pazienti", "Dati di tutti i pazienti", config.getCsvsPath() + File.separator + "msruser.csv");
            return new ModelAndView("redirect:/showpatients");
        }
        else
        {
            logger.debug("... export patients failed");
            model.addAttribute("message", "Esportazione storia pazienti fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }

    }



    /**
     * util function to get overall category progress
     *
     * @param exerciseList list of exercise to check
     * @return percentage of completed
     */
    public int getCategoryProgress(List<Exercise> exerciseList)
    {
        int completedCount = 0;
        if (exerciseList.isEmpty())
            return 0;

        for(Exercise ex : exerciseList)
            if ("done".equals(ex.getStatus()))
                completedCount++;

        return completedCount / exerciseList.size() * 100;
    }
}