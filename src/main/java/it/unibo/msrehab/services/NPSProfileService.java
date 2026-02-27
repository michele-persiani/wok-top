package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.entities.NPSTest;
import it.unibo.msrehab.model.entities.PersonalData;
import it.unibo.msrehab.model.controller.ClinicalProfileController;
import it.unibo.msrehab.model.controller.NPSTestController;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class that provides servlets for patient profiles. The services are the
 following: newnpProfile inserts a patient profile entry in the database;
 updateprofile updates a patient profile entry; deleteprofile: deletes a
 patient profile entry.
 */
@Controller
public class NPSProfileService {

    private static final Logger logger = LoggerFactory
            .getLogger(NPSProfileService.class);
    private final MSRUserController userController;
    private final PersonalDataController personalDataController;
    private final ClinicalProfileController clinicalProfileController;
    private final NPSTestController npsTestController;
    private final Configuration config;
    
    public NPSProfileService() {
        super();
        this.userController = new MSRUserController();
        this.personalDataController = new PersonalDataController();
        this.clinicalProfileController = new ClinicalProfileController();
        this.npsTestController = new NPSTestController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
    }


    // show profile form
    @RequestMapping(value = "/npsprofileform", method = RequestMethod.GET)
    public ModelAndView npsprofileForm(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            Model model) {

        logger.debug("npprofileForm() : {}", patientid);
        
        if (clinicalProfileController.findAllByUser(patientid).isEmpty()) {            
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Compilare prima il profilo anagrafico e il profilo clinico");                
            return new ModelAndView("error");
        }
        else {
            List<NPSTest> npstestList
                    = npsTestController.findAllByUser(patientid);
            model.addAttribute("npslist", npstestList);
            model.addAttribute("patientid", patientid);
            model.addAttribute(
                    "patientname",
                    userController.findEntity(patientid).get().getName()
                    + " " + userController.findEntity(patientid).get().getSurname());

            return new ModelAndView("npsprofile-form");
        }
    }
    
    
    
    /**
     * Deletes a patient's nps test
     * @param testid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/deletenpstest", method = RequestMethod.POST)
    public ModelAndView deleteNPSTest(
            @RequestParam(value = "testid", required = true) Integer testid,
            Model model) {

        logger.info("Start to delete nps test " + testid);
        if (testid == null) {
            return new ModelAndView("redirect:/showpatients");
        }
        NPSTest test = (NPSTest) npsTestController.findEntity(testid).get();
        
        if (test == null) {
            logger.warn("NPS test " + testid + " not found");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Test neuropsicologico non trovato");                
            return new ModelAndView("error");
            
        }
        if (npsTestController.removeEntity(test)) {
            logger.info("NPS test " + testid + " deleted with success");
            return new ModelAndView("redirect:/showpatients");
        } else {  // deleteRecord(user) == -1
            logger.error("...NPS test delete failed");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Test neuropsicologico" + testid + ", cancellazione fallita");                
            return new ModelAndView("error");            
        }
    }

    /**
     * Shows a NPS test form
     * 
     * @param patientid
     * @param formid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/npstestform", method = RequestMethod.GET)
    public ModelAndView npstestform(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "formid", required = true) Integer formid,
            Model model) {

        logger.debug("npstestform() : {}", formid);

        //if (personalDataController.findAllByUser(patientid).isEmpty()) {
        //    return new ModelAndView("redirect:/showpatients");
        //} else {
        NPSTest test;
        if (formid != null) {
            test = npsTestController.findEntity(formid).get();
        } else {
            test = new NPSTest();
            test.setUserid(patientid);
        }
        model.addAttribute("patientid", patientid);
        model.addAttribute("npstestForm", test);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("npstest-form");
    }

    /**
     * Compares NPS test forms
     * 
     * @param patientid
     * @param formid1
     * @param formid2
     * @param model
     * @return 
     */
    @RequestMapping(value = "/npstestcompform", method = RequestMethod.GET)
    public ModelAndView npstestcompform(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "formid1", required = true) Integer formid1,
            @RequestParam(value = "formid2", required = true) Integer formid2,
            Model model) {

        logger.debug("npstestcompform() : {}", formid1, formid2);

        NPSTest test1 = npsTestController.findEntity(formid1).get(),
                test2 = npsTestController.findEntity(formid2).get();
        
        model.addAttribute("patientid", patientid);
        model.addAttribute("test1", test1);
        model.addAttribute("test2", test2);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("npstestcomp-table");
    }
    
    /**
     * Selects NPS test form
     * 
     * @param patientid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/npstestsel", method = RequestMethod.GET)
    public ModelAndView npstestsel(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            Model model) {

        logger.debug("npstestsel() : {}", patientid);

        List<NPSTest> npsTestList
                = npsTestController.findAllByUser(patientid);
        model.addAttribute("patientid", patientid);
        model.addAttribute("npstestlist", npsTestList);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("npstestsel");
    }
    
    /**
     * Saves or updates a patient nps test
     * @param npstestForm
     * @param result
     * @param model
     * @return 1 if profile is deleted, -1 otherwise; HttpStatus.NOT_ACCEPTABLE
     * if the profile with the given id was not found; HttpStatus.OK if the
     * profile is deleted with success; HttpStatus.SERVICE_UNAVAILABLE if the
     * profile cannot be deleted
     */
    @RequestMapping(value = "/saveorupdatenpstest", method = RequestMethod.POST)
    public ModelAndView saveorupdatenpstest(
            @Valid @ModelAttribute("npstestForm") NPSTest npstestForm,
            BindingResult result,
            Model model) {

        logger.info("Starting update NPS test");
        
        Integer userid = npstestForm.getUserid();
        
        if (result.hasErrors()) {
            model.addAttribute("patientid", userid);
            model.addAttribute(
                    "patientname",
                    userController.findEntity(userid).get().getName()
                    + " " + userController.findEntity(userid).get().getSurname());
            
            return new ModelAndView("npstest-form");
        }
        else {
            PersonalData pd = personalDataController.findAllByUser(npstestForm.getUserid()).get(0);
            int birthyear = pd.getBirthyear();
            int educationyears = pd.getEducationyears();
        
            npstestForm.setMfistot();
            npstestForm.setTmtaadj(birthyear, educationyears);
            npstestForm.setTmtaeqFromTmtaadj();
            npstestForm.setTmtbaadj(birthyear, educationyears);
            npstestForm.setTmtbadj(birthyear, educationyears);
            npstestForm.setTmtbaeqFromTmtbaadj();
            npstestForm.setTmtbaraw();
            npstestForm.setTmtbeqFromTmtbadj();
            
//            npstestForm.setHadstot();
//            npstestForm.setMmseadj(birthyear, educationyears);

            npstestForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

            if (npstestForm.isNew()) {
                if (npsTestController.insertEntity(npstestForm))
                {
                    logger.info("Add of nps test " + npstestForm.getId() + " terminated with success");
                    return new ModelAndView("redirect:/npsprofileform?patientid="+npstestForm.getUserid());
                }
                else {  // addRecord(npstestForm) == -1
                    logger.error("...nps test addition failed");
                    model.addAttribute("message", "Aggiunta nps test fallita");
                    model.addAttribute("back", "npstest-form");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");
                }
            }
            else { // update
                if (npsTestController.updateEntity(npstestForm)) {
                    logger.info("Update of nps test " + npstestForm.getId() + " terminated with success");
                    return new ModelAndView("redirect:/npsprofileform?patientid="+npstestForm.getUserid());
                }
                else {    // updateRecord(updatedUser) == -1
                    logger.error("...nps test update failed");
                    model.addAttribute("message", "Aggiornamento nps test fallita");
                    model.addAttribute("back", "npstest-form");
                    model.addAttribute("home", "patienthome");
                    return new ModelAndView("error");
                }
            }
        }
    }
    
    /**
     * Exports NPS profiles
     * @param email
     * @param model
     * @return 
     */
    @RequestMapping(value = "/exportnpsprofiles", method = RequestMethod.POST)
    public ModelAndView exportnpsprofiles(
            @RequestParam(value = "email", required = true) String email,
            Model model) {
        
        logger.debug("exportnpsprofiles()");

        int nsptestExportResult = CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "npstest", "1");
        if(nsptestExportResult==0) {
            logger.debug("... export of NPS tests succeeded");
            SendMail.sendMail(email, "NPS test", "NPS test di tutti i pazienti", config.getCsvsPath()+File.separator+"npstest.csv");
        }
        
        int raotestExportResult = CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "raotest", "1");
        if(raotestExportResult==0) {
            logger.debug("... export of Rao tests succeeded");
            SendMail.sendMail(email, "Rao test", "Rao test di tutti i pazienti", config.getCsvsPath()+File.separator+"raotest.csv");
        }
        
        int whitetestExportResult = CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "whitetest", "1");
        if(whitetestExportResult==0) {
            logger.debug("... export of White tests succeeded");
            SendMail.sendMail(email, "White test", "White test di tutti i pazienti", config.getCsvsPath()+File.separator+"whitetest.csv");
        }

        if(nsptestExportResult == 0
                && raotestExportResult == 0
                && whitetestExportResult == 0) {
            return new ModelAndView("redirect:/showpatients"); 
        }
        else {
            logger.debug("... export of NPS test failed");
            model.addAttribute("message", "Esportazione profili NPS fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }

    }
    
}
