package it.unibo.msrehab.services;

import flexjson.JSONDeserializer;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.entities.PersonalData;
import it.unibo.msrehab.model.controller.PersonalDataController;
import it.unibo.msrehab.util.CsvHelper;
import it.unibo.msrehab.util.SendMail;
import java.io.File;
import java.util.List;
import javax.validation.Valid;
//import java.time.LocalDateTime;
import org.joda.time.LocalDateTime;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * Class that provides servlets for patient personal data.
 */
@Controller
public class PersonalDataService {

    private static final Logger logger = LoggerFactory
            .getLogger(PersonalDataService.class);
    private final PersonalDataController personalDataController;
    private final MSRUserController userController;
    private final Configuration config;
    
    public PersonalDataService() {
        super();
        this.personalDataController = new PersonalDataController();
        this.userController = new MSRUserController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
    }

    @RequestMapping(value = "/deletepersonaldata", method = RequestMethod.POST)
    public ModelAndView deletePersonaldata(
            @RequestParam(value = "personaldataid", required = true) Integer personaldataid,
            Model model) {

        logger.info("Start to delete personal data " + personaldataid);
        if (personaldataid == null) {
            return new ModelAndView("redirect:/showpatients");
        }
        PersonalData personalData = (PersonalData) personalDataController.findEntity(personaldataid).get();
        
        if (personalData == null) {
            logger.warn("Personal data " + personaldataid + " not found");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Profilo anagrafico non trovato");
            return new ModelAndView("error");            
        }
        if (personalDataController.removeEntity(personalData)) {
            logger.info("Parsonal data " + personaldataid + " deleted with success");
            return new ModelAndView("redirect:/showpatients");
        } else {  // deleteRecord(user) == -1
            logger.error("...personal data delete failed");
            model.addAttribute("message", "Profilo anagrafico " + personaldataid + ", cancellazione fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }
    }

    // show personal data form
    @RequestMapping(value = "/personaldataform", method = RequestMethod.GET)
    public ModelAndView personalDataForm(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            Model model) {

        logger.debug("personalDataForm() : {}", patientid);
        List<PersonalData> personaldataList
                = personalDataController.findAllByUser(patientid);
        PersonalData personaldata;
        if (personaldataList.size() > 0) {
            personaldata = personaldataList.get(0);
        } else {
            personaldata = new PersonalData();
            personaldata.setUserid(patientid);
        }
        model.addAttribute("personaldataForm", personaldata);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("personaldata-form");
    }

    /**
     * Creates a new personal data for a given patient, given as a json
     *
     * @param json The patient's profile
     *
     * @return the id of the profile (>0) if the profile is inserted in the
     * database, -1 otherwise; HttpStatus.NOT_ACCEPTABLE the patient's id is not
     * correct; HttpStatus.OK if the profile is inserted;
     * HttpStatus.SERVICE_UNAVAILABLE if the profile could not be inserted in
     * the database
     */
    @RequestMapping(value = "/newpersonaldata", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> newPersonaldata(
            @RequestBody String json) {

        logger.info("Starting register a new patient personal data...");

        try {
            PersonalData personaldata
                    = new JSONDeserializer<PersonalData>().deserialize(json, PersonalData.class);

            // Validate personal data
            Integer userid = personaldata.getUserid();

            if (userid != null && userController.findEntity(userid) == null) {
                logger.warn("...given user id is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (personaldata.getBirthyear() != null
                    && !PersonalData.isBirthyearValid(personaldata.getBirthyear())) {
                logger.warn("...given birthday year is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (personaldata.getEducationyears()!= null
                    && !PersonalData.isEducationYearsValid(personaldata.getEducationyears())) {
                logger.warn("...given education years is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (personaldata.getGender() == null) {
                logger.warn("...given gender is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (personaldata.getJob() == null) {
                logger.warn("...given job is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (personaldata.getMaritalstatus() == null) {
                logger.warn("...given marital status is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (personaldata.getSonnumber() < 0) {
                logger.warn("...given son number is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }

            //Set timestamp
            //Timestamp t = Timestamp.valueOf(LocalDateTime.now());
            //profile.setTimestamp((Timestamp.valueOf(LocalDateTime.now())));
            Long t = LocalDateTime.now().toDateTime().getMillis();
            personaldata.setTimestamp(t);

            boolean success = personalDataController.insertEntity(personaldata);
            if (success) {
                logger.info("...personal data registered with success with id " + personaldata.getId());
                return new ResponseEntity<String>(personaldata.getId().toString(), HttpStatus.OK);
            } else {
                logger.error("...personal data could not be registered");
                return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Updates a patient personal data, given as a json
     *
     * @param json The patient's personal data
     *
     * @return 1 if the personal data is updated, -1 otherwise;
     * HttpStatus.NOT_ACCEPTABLE if the personal data with the given id was not found;
     * HttpStatus.OK if the given personal data is updated;
     * HttpStatus.SERVICE_UNAVAILABLE if the update failed
     */
    @RequestMapping(value = "/updatepersonaldata", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> updatePersonaldata(
            @RequestBody String json) {

        logger.info("Starting update personal data");

        try {
            PersonalData newPersonaldata
                    = new JSONDeserializer<PersonalData>().deserialize(json, PersonalData.class);

            Integer personaldataid = newPersonaldata.getId();

            PersonalData oldPersonalData = personalDataController.findEntity(personaldataid).get();
            if (oldPersonalData == null) {
                logger.warn("Personal data " + personaldataid + " not found");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (newPersonaldata.getBirthyear() != null
                    && !PersonalData.isBirthyearValid(newPersonaldata.getBirthyear())) {
                logger.warn("...given birthday year is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (newPersonaldata.getEducationyears() != null
                    && !PersonalData.isEducationYearsValid(newPersonaldata.getEducationyears())) {
                logger.warn("...given education years is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            
            if (newPersonaldata.getGender() == null) {
                logger.warn("...given gender is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (newPersonaldata.getJob() == null) {
                logger.warn("...given job is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (newPersonaldata.getMaritalstatus() == null) {
                logger.warn("...given marital status is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (newPersonaldata.getSonnumber() < 0) {
                logger.warn("...given son number is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }

            logger.info("new profile: " + json);

            newPersonaldata.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

            if (personalDataController.updateEntity(newPersonaldata)) {
                logger.info("Update of personal data " + personaldataid + " terminated with success");
                return new ResponseEntity<String>("1", HttpStatus.OK);
            } else {    // updateRecord(profile) == -1
                logger.error("...personal data update failed");
                return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    
    /**
     * Saves or updates a patient personal data
     * @param personalDataForm
     * @param result
     * @param model
     * @return 1 if profile is deleted, -1 otherwise; HttpStatus.NOT_ACCEPTABLE
     * if the profile with the given id was not found; HttpStatus.OK if the
     * profile is deleted with success; HttpStatus.SERVICE_UNAVAILABLE if the
     * profile cannot be deleted
     */
    @RequestMapping(value = "/saveorupdatepersonaldata", method = RequestMethod.POST)
    public ModelAndView saveOrUpdatePersonalData(
            @Valid @ModelAttribute("personaldataForm") PersonalData personalDataForm,
            BindingResult result,
            Model model) {

        logger.debug("saveOrUpdatePersonalData() : {}", personalDataForm);

        if (result.hasErrors()) {
            model.addAttribute("personaldataForm", personalDataForm);
            model.addAttribute(
                "patientname", userController.findEntity(personalDataForm.getUserid()).get().getName()
                + " " + userController.findEntity(personalDataForm.getUserid()).get().getSurname());
            return new ModelAndView("personaldata-form");
        }

        personalDataForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

        if (personalDataForm.isNew()) {
            if (personalDataController.insertEntity(personalDataForm))
            {
                logger.info("Add of personal data " + personalDataForm.getId() + " terminated with success");
                return new ModelAndView("redirect:/showpatients");
            } else {  // addRecord(profile) == -1
                logger.error("...personal data addiion failed");
                model.addAttribute("message", "Inserimento dati persanali fallito");
                model.addAttribute("back", "patient-form");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");
            }
        } else { // update
            if (personalDataController.updateEntity(personalDataForm)) {
                    //redirectAttributes.addFlashAttribute("css", "success");
                //redirectAttributes.addFlashAttribute("msg", "ClinicalProfile updated successfully!");
                logger.info("Update of personal data " + personalDataForm.getId() + " terminated with success");
                //return new ResponseEntity<String>("1", HttpStatus.OK);
                return new ModelAndView("redirect:/showpatients");
            } else {    // updateRecord(personalDataForm) == -1
                logger.error("...personal data update failed");
                model.addAttribute("message", "Aggiornamento dati personali fallito");
                model.addAttribute("back", "patient-form");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");
            }
        }
    }
    
    /**
     * Exports personal profiles
     * @param email
     * @param model
     * @return 
     */
    @RequestMapping(value = "/exportpersonalprofiles", method = RequestMethod.POST)
    public ModelAndView exportpersonalprofiles(
            @RequestParam(value = "email", required = true) String email,
            Model model) {
        logger.debug("exportpersonalprofiles()");

        if (CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "personaldata", "1") == 0) {
            logger.debug("... export of personal profiles succeeded");
            SendMail.sendMail(email, "Dati personali pazienti", "Dati personali di tutti i pazienti", config.getCsvsPath()+File.separator+"personaldata.csv");
            return new ModelAndView("redirect:/showpatients"); 
        }
        else {
            logger.error("... export of personal profiles failed");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Esportazione profili anagrafici fallita");
            return new ModelAndView("error");
        }

    }
    
}
