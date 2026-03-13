package it.unibo.msrehab.services;

import flexjson.JSONDeserializer;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.Exercise.ExerciseCategoryValue;

import it.unibo.msrehab.model.entities.MSRSession;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.model.entities.MSRUser.RoleValue;
import it.unibo.msrehab.model.entities.ClinicalProfile;
import it.unibo.msrehab.model.entities.MSRGroup;
import it.unibo.msrehab.model.entities.NPSTest;
import it.unibo.msrehab.model.entities.PersonalData;
import it.unibo.msrehab.model.entities.RaoTest;
import it.unibo.msrehab.model.entities.WhiteTest;
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
import it.unibo.msrehab.util.CategoryGroup;
import it.unibo.msrehab.util.CookiesManager;
import it.unibo.msrehab.util.CsvHelper;
import it.unibo.msrehab.util.ExerciseCategory;
import it.unibo.msrehab.util.SendMail;
import it.unibo.msrehab.util.WebPagesUtilities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public MSRUserService() {
        super();
        this.userController = new MSRUserController();
        this.groupController = new MSRGroupController();
        this.profileController = new ClinicalProfileController();
        this.npsTestController = new NPSTestController();
        this.personalDataController = new PersonalDataController();
        this.raoTestController = new RaoTestController();
        this.whiteTestController = new WhiteTestController();
        this.sessionController = new MSRSessionController();
        this.exerciseController = new ExerciseController();
        this.historyController = new HistoryController();
        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);

    }

    /**
     * Creates new user given email and password
     *
     * @param json Contains user's email, password, photo, surname, name, phone
     * and role
     *
     * @return the id of the user (>0) if the user is inserted in the database,
     * -1 otherwise; HttpStatus.NOT_ACCEPTABLE if data are not correct or the
     * user is already registered; HttpStatus.OK if the user is inserted;
     * HttpStatus.SERVICE_UNAVAILABLE if the user could not be inserted in the
     * database
     */
    @RequestMapping(
            value = "/newuser",
            method = RequestMethod.POST,
            headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity newUser(@RequestBody String json) {

        logger.info("Starting register a new user...");

        try {  
            MSRUser user = new JSONDeserializer<MSRUser>()
                    .deserialize(json, MSRUser.class);


            
            if (!MSRUser.isEmailValid(user.getEmail())) {
                logger.warn("...given email is not valid");
                return new ResponseEntity("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            
            
            if (!MSRUser.isPasswordValid(user.getPassword())) {
                logger.warn("...given password is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (user.getPhoto() != null && !MSRUser.isPhotoValid(user.getPhoto())) {
                logger.warn("...given photo is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
          
            if (user.getSurname() != null && !MSRUser.isSurnameValid(user.getSurname())) {
                logger.warn("...given surname is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (user.getName() != null && !MSRUser.isNameValid(user.getName())) {
                logger.warn("...given name is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (user.getPhone() != null && !MSRUser.isPhoneValid(user.getPhone())) {
                logger.warn("...given phone is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (userController.findRecord(user.getEmail(), user.getPassword()) == null)
            {
                boolean success = userController.insertEntity(user);
                
                if (success) {
               //     String Username="User"+id.toString();
                 //   user.setUsername(Username);
                    userController.updateEntity(user);
                    logger.info("...user registered with success with id " + user.getId());
                    return new ResponseEntity<String>(user.getId().toString(), HttpStatus.OK);
                } else {
                    logger.info("...user could not be registered");
                    return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
                }
            } else {
                logger.warn("...user already registerd");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    // show patient form
    @RequestMapping(value = "/patientform", method = RequestMethod.POST)
    public ModelAndView patientForm(
           // @RequestParam(value = "username", required = false) String username,//aggiunto per username
            @RequestParam(value = "patientid", required = false) Integer patientid,
            Model model) {

        logger.debug("patientForm() : {}", patientid);
        MSRUser patient = new MSRUser();
        if (patientid != null) {
            patient = (MSRUser) userController.findEntity(patientid).get();
            //aggiunti per username
         //   username="User"+patientid.toString();
         //   model.addAttribute("username", username);
        }
        //aggiunto per username
      //  model.addAttribute("username", username);
        
        model.addAttribute("patientid", patientid); 
        model.addAttribute("patientForm", patient);
        return new ModelAndView("patient-form");
    }

    /**
     * Updates a user entry
     *
     * @param json Contains user's id, to identify the user to be updated. It
     * also contains user's email, password, photo, surname, name, phone and
     * role it they are to be updated
     *
     * @return the success code user update (1 if user is updated, -1
     * otherwise); HttpStatus.NOT_ACCEPTABLE if the user with the given id was
     * not found; HttpStatus.OK if the given user is updated;
     * HttpStatus.SERVICE_UNAVAILABLE if the update failed
     */
    @RequestMapping(value = "/updateuser", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> updateUser(
            @RequestBody String json) {

        try {
            MSRUser updatedUser
                    = new JSONDeserializer<MSRUser>().deserialize(json, MSRUser.class);

            int userid = updatedUser.getId();

            logger.info("Start updating user " + userid);

            MSRUser user = userController.findEntity(userid).get();
            if (user == null) {
                logger.warn("User " + userid + " not found");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }

            // Validate updatedUser
            if (updatedUser.getEmail() != null && !MSRUser.isEmailValid(updatedUser.getEmail())) {
                logger.warn("...given email is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            
             if (updatedUser.getUsername() != null) {
                logger.warn("...given username is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (updatedUser.getEmail() != null && !MSRUser.isPasswordValid(updatedUser.getPassword())) {
                logger.warn("...given password is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (updatedUser.getPhoto() != null && !MSRUser.isPhotoValid(updatedUser.getPhoto())) {
                logger.warn("...given photo is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (updatedUser.getSurname() != null && !MSRUser.isSurnameValid(updatedUser.getSurname())) {
                logger.warn("...given surname is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            
            if (updatedUser.getName() != null && !MSRUser.isNameValid(updatedUser.getName())) {
                logger.warn("...given name is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (updatedUser.getPhone() != null && !MSRUser.isPhoneValid(updatedUser.getPhone())) {
                logger.warn("...given phone is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
           

            logger.info("Updated user: " + json);

            if (userController.updateEntity(updatedUser)) {
                logger.info("Update of  user " + userid + " terminated with success");
                return new ResponseEntity<String>("1", HttpStatus.OK);
            } else {    // updateRecord(updatedUser) == -1
                logger.error("...user update failed");
                return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Deletes a user give the user's id
     *
     * @param patientid the id of the user
     * @param model
     * @param redirectAttributes
     *
     * @return 1 if user is deleted, -1 otherwise; HttpStatus.NOT_ACCEPTABLE if
     * the user with the given id was not found; HttpStatus.OK if the user is
     * deleted with success; HttpStatus.SERVICE_UNAVAILABLE if the user cannot
     * be deleted
     */
    @RequestMapping(value = "/deletepatient", method = RequestMethod.POST)
    public ModelAndView deletePatient(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            Model model,
            final RedirectAttributes redirectAttributes) {

        logger.info("Start to delete user " + patientid);
        MSRUser user = (MSRUser) userController.findEntity(patientid).get();
        if (user == null) {
            logger.warn("User " + patientid + " not found");
            model.addAttribute("message", "Utente non trovato");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }

        // Delete patient group if it contains only the patient being deleted
        if (userController.findAllPatientsInGroup(user.getGid()).size() <= 1) {
            MSRGroup group = groupController.findEntity(user.getGid()).get();
            if (!groupController.removeEntity(group)) {
                logger.error("...group update failed");
                model.addAttribute("message", "Gruppo " + group.getId()+ ", cancellazione fallita");
                model.addAttribute("back", "showgroups");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");

            }
        }
        List<ClinicalProfile> profiles
                = profileController.findAllByUser(patientid);
        for (ClinicalProfile profile : profiles) {
            if(!profileController.removeEntity(profile)) {
                logger.error("...clinical profile delete failed");
                model.addAttribute("message", "Cancellazione del profilo clinico " + profile + " fallita");
                model.addAttribute("back", "showpatients");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");
            }
        }
        List<NPSTest> npsTests
                = npsTestController.findAllByUser(patientid);
        for (NPSTest npsTest : npsTests) {
            // TODO manage nps test deletion failure
            npsTestController.removeEntity(npsTest);
        }
        List<PersonalData> personalData
                = personalDataController.findAllByUser(patientid);
        for (PersonalData pd : personalData) {
            // TODO manage personal data deletion failure
            personalDataController.removeEntity(pd);
        }
        List<RaoTest> raoTests
                = raoTestController.findAllByUser(patientid);
        for (RaoTest raoTest : raoTests) {
            // TODO manage Rao test deletion failure
            raoTestController.removeEntity(raoTest);
        }
        List<WhiteTest> whiteTests
                = whiteTestController.findAllByUser(patientid);
        for (WhiteTest whiteTest : whiteTests) {
            // TODO manage White test deletion failure
            whiteTestController.removeEntity(whiteTest);
        }
        List<MSRSession> sessions
                = sessionController.findAllByUserOrGroup(patientid);
        for (MSRSession session : sessions) {
            // TODO: manage failure of deleteRecord
            sessionController.removeEntity(session);
        }
        // Delete photo
        String photoFile = user.getPhoto();
        if (photoFile != null) {
            String photosPath = config.getPhotosPath();
            String filePath
                    = photosPath + File.separator + photoFile;
            File file = new File(filePath);
            file.delete();
        }

        if (userController.removeEntity(user)) {
            logger.info("User " + patientid + " deleted with success");
            /* TODO Check the following two lines */
            redirectAttributes.addFlashAttribute("css", "success");
            redirectAttributes.addFlashAttribute("msg", "User is deleted!");
            return new ModelAndView("redirect:/showpatients");
        } else {  // deleteRecord(user) == -1
            logger.error("...user update failed");
            model.addAttribute("message", "Utente " + patientid + ", cancellazione fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }
    }

    /**
     * Retrieves user's info given user's email and password
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param response
     * @param model
     *
     * @return the user in JSON format if the user is found, -1 otherwise
     * HttpStatus.NOT_ACCEPTABLE if email or password are not valid;
     * HttpStatus.NO_CONTENT if the user is not present in the database;
     * HttpStatus.OK if the user is found in the database
     */  
   @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView login(
           // @RequestParam(value = "id", required = true) String id,
            //aggiunto per usermane
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse response,
            Model model) {

              logger.info("Look for the user username: "
                + username  + " password: " + password);


     
     MSRUser user = userController.findRecordByUsername(username, password);
       
      if (user == null) {
            logger.info("...no user found in the database");
            return new ModelAndView("login", "message", "Credenziali non valide");
        } else if (user.getMsrrole() == RoleValue.ADMIN) {
     //aggiunto per useranme
            Cookie ck = new Cookie("username", user.getUsername());
            Cookie ck1 = new Cookie("name", user.getName());
            Cookie ck2 = new Cookie("id", user.getId().toString());
            Cookie ck3 = new Cookie("role", RoleValue.ADMIN.toString());
            Cookie ck4 = new Cookie("cid", user.getCid().toString());
                 //aggiunto per useranme
            response.addCookie(ck);
            response.addCookie(ck1);
            response.addCookie(ck2);
            response.addCookie(ck3);
            response.addCookie(ck4);
            //model.addAttribute("name", user.getName());
            //return new ModelAndView("administrator");
            return new ModelAndView("redirect:adminhome");
        } else {
            logger.info("...user found with the given email and password");
            //aggiunto per username
            
            Cookie ck = new Cookie("useranme", user.getUsername());
            Cookie ck1 = new Cookie("name", user.getName());
            Cookie ck2 = new Cookie("id", user.getId().toString());
            Cookie ck3 = new Cookie("role", RoleValue.PATIENT.toString());
             //aggiunto per username
            response.addCookie(ck);
            response.addCookie(ck1);
            response.addCookie(ck2);
            response.addCookie(ck3);
            return new ModelAndView("redirect: patienthome");
        }
    }
    
   
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView logout(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session) {
        if (WebPagesUtilities.redirectIfNotLogged(request)) {
            return new ModelAndView("login");
        } else {
            Cookie ck = new Cookie("id", "");
            ck.setMaxAge(0);
            response.addCookie(ck);
            return new ModelAndView("login");
        }
    }

    /**
     * Retrieves user's info given user's email and password
     *
     * @param email the email of the user
     * @param response
     * @param model
     *
     * @return the user in JSON format if the user is found, -1 otherwise
     * HttpStatus.NOT_ACCEPTABLE if email or password are not valid;
     * HttpStatus.NO_CONTENT if the user is not present in the database;
     * HttpStatus.OK if the user is found in the database
     */
    @RequestMapping(value = "/forgottencredential", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView forgottencredential(
            @RequestParam(value = "email", required = true) String email,
            HttpServletResponse response,
            Model model) {

        logger.info("Look for the user with email: " + email);

        if (!MSRUser.isEmailValid(email)) {
            logger.warn("...given email is not valid");
            return new ModelAndView("login", "message", "Email non valida");
        }

        MSRUser user = userController.findRecordByEmail(email);
        if (user == null) {
            logger.info("...no user found in the database");
            return new ModelAndView("login", "message", "Email non valida");
        } else {
            logger.info("...user found with the given email");

            SendMail.sendCredentials(
                    user.getEmail(),
                    user.getId(),
                    user.getPassword());

            return new ModelAndView("login");
        }
    }

    @RequestMapping(value = "/adminhome", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView adminhome(
            HttpServletRequest request,
            Model model)
    {
        if (WebPagesUtilities.redirectIfNotLogged(request) || WebPagesUtilities.redirectIfNotAdmin(request))
            return new ModelAndView("login");
        else
        {
            int cid = CookiesManager.getLoggedUserCenter(request);
            model.addAttribute("name", CookiesManager.geLoggedUserName(request));
            model.addAttribute("rEnabled", Configuration.getInstance().isRlEnabled());


            if (userController.findAllPatientsInCenter(cid).isEmpty())
                model.addAttribute("existpatients", false);
            else
                model.addAttribute("existpatients", true);


            if (groupController.findAllGroupsInCenter(cid).isEmpty())
                model.addAttribute("existgroups", false);
            else
                model.addAttribute("existgroups", true);


            return new ModelAndView("administrator")
                    .addObject("rlEnabled", Configuration.getInstance().isRlEnabled())
                    ;
        }
    }

    @RequestMapping(value = "/patienttraining", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patienttraining(HttpServletRequest request, Model model) {
        if (WebPagesUtilities.redirectIfNotLogged(request)
                || WebPagesUtilities.redirectIfNotPatient(request)) {
            return new ModelAndView("login");
        } else {
            JSONArray jArr
                    = new JSONArray(
                            "[{\"cat\":\"ATT_SEL_STD\",\"id\":1,\"done\":false},"
                            + "{\"cat\":\"ATT_SEL_STD_FAC\",\"id\":6,\"done\":false},"
                            + "{\"cat\":\"ATT_SEL_STD_ORI\",\"id\":47,\"done\":false},"
                            + "{\"cat\":\"ATT_SEL_FLW\",\"id\":8,\"done\":false},"
                            + "{\"cat\":\"ATT_SEL_FLW_FAC\",\"id\":12,\"done\":false},"
                            + "{\"cat\":\"ATT_SEL_FLW_ORI\",\"id\":48,\"done\":false},"
                            + "{\"cat\":\"ATT_ALT\",\"id\":15,\"done\":false},"
                            + "{\"cat\":\"ATT_ALT_FAC\",\"id\":18,\"done\":false},"
                            + "{\"cat\":\"ATT_ALT_ORI\",\"id\":49,\"done\":false},"
                            + "{\"cat\":\"ATT_DIV\",\"id\":20,\"done\":false},"
                            + "{\"cat\":\"ATT_DIV_FAC\",\"id\":24,\"done\":false},"
                            + "{\"cat\":\"ATT_DIV_ORI\",\"id\":46,\"done\":false},"
                            + "{\"cat\":\"MEM_VIS_1\",\"id\":25,\"done\":false},"
                            + "{\"cat\":\"MEM_VIS_1_FAC\",\"id\":30,\"done\":false},"
                            + "{\"cat\":\"MEM_VIS_1_ORI\",\"id\":50,\"done\":false},"
                            + "{\"cat\":\"MEM_VIS_2\",\"id\":34,\"done\":false},"
                            + "{\"cat\":\"MEM_VIS_5\",\"id\":55,\"done\":false},"
                            + "{\"cat\":\"NBACK\",\"id\":38,\"done\":false},"
                            + "{\"cat\":\"NBACK_FAC\",\"id\":40,\"done\":false},"
                            + "{\"cat\":\"NBACK_ORI\",\"id\":41,\"done\":false},"
                            //CHECK
                            + "{\"cat\":\"MEM_LONG_1\",\"id\":51,\"done\":false},"
                            + "{\"cat\":\"RES_INH\",\"id\":43,\"done\":false},"
                            + "{\"cat\":\"PLAN_1\",\"id\":52,\"done\":false},"
                            + "{\"cat\":\"PLAN_2\",\"id\":53,\"done\":false}],"
                            + "{\"cat\":\"PLAN_3\",\"id\":58,\"done\":false}]"
                    );

            JSONObject json;
            String cat;
              int id;
            boolean done;
            
            // HashMap (Categoria: Esercizi)
            Map<String, ExerciseCategory> categoryExerciseMap = new TreeMap<>();

            for (ExerciseCategoryValue exerciseCategoryValue : ExerciseCategoryValue.values()) {
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
                Exercise exercise = exerciseController.findEntity(id).get();
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
            categoryExerciseMap.forEach((key, exerciseCategory) -> {
                if (!exerciseCategory.getExercises().isEmpty()) {
                    exerciseCategory.setProgress(this.getCategoryProgress(exerciseCategory.getExercises()));
                    String categoryGroup = this.getGroupForCategory(key);
                    groupMap.get(categoryGroup).addExerciseCategory(exerciseCategory);
                }
            });
            Integer userId = CookiesManager.geLoggedUserId(request);
            model.addAttribute("groupMap", groupMap);
            model.addAttribute("patientid", userId);
            model.addAttribute("difficulty", "training");
            model.addAttribute("name", CookiesManager.geLoggedUserName(request));
            model.addAttribute("sessid", -1);

            return new ModelAndView("patient-training");
        }
    }

    @RequestMapping(value = "/patientdemo", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientdemo(HttpServletRequest request, Model model) {
        if (WebPagesUtilities.redirectIfNotLogged(request) || WebPagesUtilities.redirectIfNotPatient(request)) {
            return new ModelAndView("login");
        } else {
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
            boolean done;
            
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
                Exercise exercise = exerciseController.getEntityOrThrow(id);
                // Inserisco l'esercizio nella lista degli esercizi ( inq questo cosa non serve differenziare lo stato)
                ExerciseCategory exerciseCategory = categoryExerciseMap.get(cat);
                exerciseCategory.getExercises().add(exercise);
            }


            // HashMap ( Gruppo : Categoria)
            Map<String, CategoryGroup> groupMap = new LinkedHashMap<>();
            // Inserisco le keys nell'ordine voluto
            groupMap.put("ATTENTION_FIG", new CategoryGroup("ATTENTION_FIG"));
            groupMap.put("MEMORY_FIG", new CategoryGroup("MEMORY_FIG"));
            groupMap.put("EX_FUNC", new CategoryGroup("EX_FUNC"));
            categoryExerciseMap.forEach((key, exerciseCategory) -> {
                if (!exerciseCategory.getExercises().isEmpty()) {
                    exerciseCategory.setProgress(this.getCategoryProgress(exerciseCategory.getExercises()));
                    String categoryGroup = this.getGroupForCategory(key);
                    groupMap.get(categoryGroup).addExerciseCategory(exerciseCategory);
                }
            });


            model.addAttribute("groupMap", groupMap);
            model.addAttribute("patientid", -1);
            model.addAttribute("difficulty", "demo");
            model.addAttribute("name", CookiesManager.geLoggedUserName(request));
            model.addAttribute("sessid", -1);

            return new ModelAndView("patient-demo");
        }
    }
    
    @RequestMapping(value = "/patienthome", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patienthome(HttpServletRequest request, Model model)
    {
        if (WebPagesUtilities.redirectIfNotLogged(request)
                || WebPagesUtilities.redirectIfNotPatient(request)) {
            return new ModelAndView("login");
        }
        else
        {
            model.addAttribute("name", CookiesManager.geLoggedUserName(request));
            
            model.addAttribute("id", CookiesManager.geLoggedUserId(request));
            //return new ModelAndView("attention-motorbike");
            return new ModelAndView("patient");
        }
    }

    @RequestMapping(value = "/patientrehabilitation", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView patientrehabilitaion(HttpServletRequest request, Model model) {
        if (WebPagesUtilities.redirectIfNotLogged(request) || WebPagesUtilities.redirectIfNotPatient(request)) {
            return new ModelAndView("login");
        }
        else
        {
            List<MSRSession> sessionList = sessionController.findAllActiveByUserOrGroup(CookiesManager.geLoggedUserId(request));

            if (sessionList.isEmpty())
            {
                model.addAttribute("back", "patienthome");
                model.addAttribute("home", "patienthome");
                model.addAttribute("message", "Non hai esercizi assegnati");
                return new ModelAndView("error");
            }
            else
            {
                MSRSession session = sessionList.get(0);
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
                    Exercise exercise = exerciseController.getEntityOrThrow(id);
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


                for (ExerciseCategory exerciseCategory : categoryExerciseMap.values()) {
                    Exercise lastInterruptedEx = getExerciseWithId(lastInterruptedExerciseId, exerciseCategory.getInterruptedExercises());
                    if (lastInterruptedEx != null) {
                        Collections.swap(exerciseCategory.getInterruptedExercises(), exerciseCategory.getInterruptedExercises().indexOf(lastInterruptedEx), 0);
                    }
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


                categoryExerciseMap.forEach((key, exerciseCategory) -> {
                    if (!exerciseCategory.getExercises().isEmpty())
                    {
                        exerciseCategory.setProgress(this.getCategoryProgress(exerciseCategory.getExercises()));
                        String categoryGroup = this.getGroupForCategory(key);
                        if (!groupMap.containsKey(categoryGroup)) {
                            groupMap.put(categoryGroup, new CategoryGroup(categoryGroup));
                        }
                        groupMap.get(categoryGroup).addExerciseCategory(exerciseCategory);
                    }
                });


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
        }
    }

    


private Exercise getExerciseWithId(Integer id, List<Exercise> list)
{
        for (Exercise ex : list) {
            if (ex.getId() == id) {
                return ex;
            }
        }
        return null;
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
            return "ATTENTION_RFLXS";
        }
        return "OTHER";
    }

    // list page
    @RequestMapping(value = "/showpatientsold", method = RequestMethod.GET)
    public ModelAndView showpatientsold(HttpServletRequest request) {
        logger.debug("showpatients()");
        if (WebPagesUtilities.redirectIfNotLogged(request)) {
            return new ModelAndView("login");
        } else {
            int cid = CookiesManager.getLoggedUserCenter(request);
            return new ModelAndView("patient-management", "patients", userController.findAllPatientsInCenter(cid));
        }
    }

    @RequestMapping(value = "/showpatients", method = RequestMethod.GET)
    public ModelAndView showPatients(
            HttpServletRequest request,
            Model model
    ) {
        logger.debug("showCenterPatients()");
        if (WebPagesUtilities.redirectIfNotLogged(request)) {
            return new ModelAndView("login");
        } else {
            int uid = CookiesManager.geLoggedUserId(request);
            int cid = CookiesManager.getLoggedUserCenter(request);
            List<MSRUser> patients = userController.findAllPatientsInCenter(cid);
            if (!patients.isEmpty()) {
                model.addAttribute("patients", patients);
                model.addAttribute("email", userController.findEntity(uid).get().getEmail());
                List<MSRGroup> groups = new ArrayList<MSRGroup>();
                for (MSRUser patient : patients) {
                    groupController.findEntity(patient.getGid()).ifPresent(groups::add);
                }
                model.addAttribute("groups", groups);
                model.addAttribute("allgroups", groupController.findAllGroupsInCenter(cid));
            } else {
              //  model.addAttribute("message", "Nessun paziente inserito nel sistema");
              model.addAttribute("message", "Nessun partecipante inserito nel sistema");
            
            }
            return new ModelAndView("patient-management");

        }
    }

    // save or update patients
    @RequestMapping(value = "/saveorupdatepatient", method = RequestMethod.POST)
    public ModelAndView saveOrUpdatePatient(
            HttpServletRequest request,
            @Valid @ModelAttribute("patientForm") MSRUser patientForm,
            BindingResult result,
            Model model,
            @RequestParam("photofile") MultipartFile photoFile
    ) {

        logger.debug("saveOrUpdateUser() : {}", patientForm);

        if (result.hasErrors() //|| !MSRUser.isEmailValid(patientForm.getEmail())
                //|| !MSRUser.isPhoneValid(patientForm.getPhone())
                ) {
            model.addAttribute("patientid", patientForm.getId());
            model.addAttribute("patientForm", patientForm);
            return new ModelAndView("patient-form");
        } else {
            if (!photoFile.isEmpty()) {
                try {
                    byte[] bytes = photoFile.getBytes();

                    String photosPath = config.getPhotosPath();
                    String fileName = System.currentTimeMillis() + ".jpg";
                    String filePath
                            = photosPath + File.separator + fileName;

                    Image img = ImageIO.read(new ByteArrayInputStream(bytes));

                    BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics2D = bufferedImage.createGraphics();
                    graphics2D.drawImage(img, 0, 0, 800, 800, null);
                    graphics2D.dispose();

                    // Create the file on server
                    File serverFile = new File(filePath);
                    serverFile.createNewFile();
                    ImageIO.write(bufferedImage, "jpg", serverFile);
                    //BufferedOutputStream stream
                    //        = new BufferedOutputStream(
                    //                new FileOutputStream(serverFile));

                    //stream.write(((DataBufferByte) bufferedImage.getData().getDataBuffer()).getData());
                    //stream.close();
                    logger.info("Server File Location="
                            + serverFile.getAbsolutePath());
                    patientForm.setPhoto(fileName);

                } catch (Exception e) {
                    return new ModelAndView("patient-form");
                }
            }
            if (patientForm.isNew()) {
                patientForm.setMsrrole(RoleValue.PATIENT);
                patientForm.setGid(-1);
                patientForm.setCid(CookiesManager.getLoggedUserCenter(request));

                String pwd;
                if (patientForm.getPassword() != null) {
                    pwd = patientForm.getPassword();
                } else {
                    pwd = "pass"+RandomStringUtils.random(3, false, true);
                }
                
                patientForm.setPassword(pwd);
                
                //aggiunto per username
                String usr = new String();
                
                //model.addAttribute("message", patientForm.getId() );
                
     
                //patientForm.setPassword(DigestUtils.md5DigestAsHex(pwd.getBytes()));
                //aggiunge il record.
                if (userController.insertEntity(patientForm))
                {
                    logger.info("Add of  user " + patientForm.getId() + " terminated with success");
              //da togliere
              usr= "User"+patientForm.getId().toString();
              patientForm.setUsername(usr);//con l'id modifico il campo appena inserito
             if( userController.updateEntity(patientForm))
                 return new ModelAndView("redirect:/showpatients");
                else{
                     logger.error("...user addition failed");
                    patientForm.setId(null);
                   // model.addAttribute("message", "Inserimento paziente fallita ");
                   model.addAttribute("message", "Inserimento partecipante fallita ");
                     
                   model.addAttribute("back", "showpatients");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }}
                else {  // addRecord(patientForm) == -1
                    logger.error("...user addition failed");
                    patientForm.setId(null);
                    //model.addAttribute("message", "Inserimento paziente fallita ");
                    model.addAttribute("message", "Inserimento partecipante fallita ");
                    
                    model.addAttribute("back", "showpatients");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }
            }
            else {  // update
                if (userController.updateEntity(patientForm)) {
                    logger.info("Update of  user " + patientForm.getId() + " terminated with success");
                    return new ModelAndView("redirect:/showpatients");
                }
                else {  // updateRecord(updatedUser) == -1
                    logger.error("...user update failed");
                    //model.addAttribute("message", "Aggiornamento paziente fallita");
                    model.addAttribute("message", "Aggiornamento partecipante fallita");
                    
                    model.addAttribute("back", "showpatients");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }
            }
        }
    }

    /**
     * Retrieves user's info given user's email and password
     *
     * @param email the email of the user
     * @param password the password of the user
     *
     * @return the user in JSON format if the user is found, -1 otherwise
     * HttpStatus.NOT_ACCEPTABLE if email or password are not valid;
     * HttpStatus.NO_CONTENT if the user is not present in the database;
     * HttpStatus.OK if the user is found in the database
     */
    @RequestMapping(value = "/signinuser", method = RequestMethod.POST)
    public ResponseEntity<String> signinUser(
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "password", required = true) String password) {

        logger.info("Look for the user email: "
                + email + " password: " + password);

        if (!MSRUser.isEmailValid(email)) {
            logger.warn("...given email is not valid");
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!MSRUser.isPasswordValid(password)) {
            logger.warn("...given password is not valid");
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }

        MSRUser user = userController.findRecord(email, password);
        if (user == null) {
            logger.info("...no user found in the database");
            return new ResponseEntity<String>("-1", HttpStatus.NO_CONTENT);
        } else {
            logger.info("...user found with the given email and password");
            return new ResponseEntity<String>(user.toJSONString(), HttpStatus.SWITCHING_PROTOCOLS);
        }

    }

    /**
     * Exports patients
     *
     * @param email
     * @param model
     * @return
     */
    @RequestMapping(value = "/exportpatients", method = RequestMethod.POST)
    public ModelAndView exportpatients(
            @RequestParam(value = "email", required = true) String email,
            Model model) {
        logger.debug("exportpatients()");

        if (CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "msruser", "msrrole='PATIENT'") == 0) {
            logger.debug("... export of patients succeeded");
            SendMail.sendMail(email, "Pazienti", "Dati di tutti i pazienti", config.getCsvsPath() + File.separator + "msruser.csv");
            return new ModelAndView("redirect:/showpatients");
        } else {
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
            @RequestParam(value = "email", required = true) String email,
            Model model) {
        logger.debug("exportpatienthistory()");

        if (CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "history", "1") == 0) {
            logger.debug("... export of patients succeeded");
            SendMail.sendMail(email, "Pazienti", "Dati di tutti i pazienti", config.getCsvsPath() + File.separator + "msruser.csv");
            return new ModelAndView("redirect:/showpatients");
        } else {
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
    public int getCategoryProgress(List<Exercise> exerciseList) {
        int completedCount = 0;
        if (exerciseList.isEmpty()) {
            return 0;
        }
        for(Exercise ex : exerciseList) {
            if ("done".equals(ex.getStatus())) {
                completedCount++;
            }
        }
        int percentage = (completedCount * 100) / exerciseList.size();
        return percentage;
    }
}