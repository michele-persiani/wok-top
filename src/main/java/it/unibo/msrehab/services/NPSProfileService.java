package it.unibo.msrehab.services;

import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.JPAModel;
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
    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();
    
    public NPSProfileService() {
        super();
        this.userController = new MSRUserController(model);
        this.personalDataController = new PersonalDataController(model);
        this.clinicalProfileController = new ClinicalProfileController(model);
        this.npsTestController = new NPSTestController(model);

        this.config = Configuration.getInstance();
    }


    // show profile form
    @RequestMapping(value = "/npsprofileform", method = RequestMethod.GET)
    public ModelAndView npsprofileForm(
            @RequestParam Integer patientid,
            Model model)
    {

        logger.debug("npprofileForm() : {}", patientid);
        
        if (clinicalProfileController.findAllByUser(patientid).isEmpty())
        {
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Compilare prima il profilo anagrafico e il profilo clinico");                
            return new ModelAndView("error");
        }

        List<NPSTest> npstestList = npsTestController.findAllByUser(patientid);
        model.addAttribute("npslist", npstestList);
        model.addAttribute("patientid", patientid);
        model.addAttribute(
                "patientname",
                userController.getEntity(patientid).getName()
                        + " " + userController.getEntity(patientid).getSurname());

        return new ModelAndView("npsprofile-form");

    }
    
    
    
    /**
     * Deletes a patient's nps test
     * @param testid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/deletenpstest", method = RequestMethod.POST)
    public ModelAndView deleteNPSTest(
            @RequestParam Integer testid
    )
    {
        logger.info("Start to delete nps test " + testid);
        if (testid == null)
            return new ModelAndView("redirect:/showpatients");

        NPSTest test = npsTestController.getEntity(testid);

        npsTestController.removeEntity(test);
        logger.info("NPS test " + testid + " deleted with success");
        return new ModelAndView("redirect:/showpatients");
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
            @RequestParam Integer patientid,
            @RequestParam Integer formid,
            Model model)
    {

        logger.debug("npstestform() : {}", formid);

        NPSTest test;
        if (formid != null)
            test = npsTestController.getEntity(formid);
        else
        {
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
            @RequestParam Integer patientid,
            @RequestParam Integer formid1,
            @RequestParam Integer formid2,
            Model model)
    {

        logger.debug("npstestcompform() : {}", formid1, formid2);

        NPSTest test1 = npsTestController.getEntity(formid1);
        NPSTest test2 = npsTestController.getEntity(formid2);
        
        model.addAttribute("patientid", patientid);
        model.addAttribute("test1", test1);
        model.addAttribute("test2", test2);
        model.addAttribute(
                "patientname",
                userController.getEntity(patientid).getName()
                + " " + userController.getEntity(patientid).getSurname()
        );

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
            @RequestParam Integer patientid,
            Model model)
    {
        logger.debug("npstestsel() : {}", patientid);

        List<NPSTest> npsTestList = npsTestController.findAllByUser(patientid);
        model.addAttribute("patientid", patientid);
        model.addAttribute("npstestlist", npsTestList);
        model.addAttribute(
                "patientname",
                userController.getEntity(patientid).getName()
                + " " + userController.getEntity(patientid).getSurname());

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
            @Valid @ModelAttribute NPSTest npstestForm,
            BindingResult result,
            Model model)
    {

        logger.info("Starting update NPS test");
        
        Integer userid = npstestForm.getUserid();
        
        if (result.hasErrors())
        {
            model.addAttribute("patientid", userid);
            model.addAttribute(
                    "patientname",
                    userController.findEntity(userid).get().getName()
                    + " " + userController.findEntity(userid).get().getSurname());
            
            return new ModelAndView("npstest-form");
        }

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

        npstestForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

        npsTestController.putEntity(npstestForm);

        logger.info("Add of nps test " + npstestForm.getId() + " terminated with success");
        return new ModelAndView("redirect:/npsprofileform?patientid="+npstestForm.getUserid());
    }
    
    /**
     * Exports NPS profiles
     * @param email
     * @param model
     * @return 
     */
    @RequestMapping(value = "/exportnpsprofiles", method = RequestMethod.POST)
    public ModelAndView exportnpsprofiles(
            @RequestParam String email,
            Model model)
    {
        
        logger.debug("exportnpsprofiles()");

        int nsptestExportResult = CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "npstest", "1");
        if(nsptestExportResult==0)
        {
            logger.debug("... export of NPS tests succeeded");
            SendMail.sendMail(email, "NPS test", "NPS test di tutti i pazienti", config.getCsvsPath()+File.separator+"npstest.csv");
        }
        
        int raotestExportResult = CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "raotest", "1");
        if(raotestExportResult==0)
        {
            logger.debug("... export of Rao tests succeeded");
            SendMail.sendMail(email, "Rao test", "Rao test di tutti i pazienti", config.getCsvsPath()+File.separator+"raotest.csv");
        }
        
        int whitetestExportResult = CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "whitetest", "1");
        if(whitetestExportResult==0)
        {
            logger.debug("... export of White tests succeeded");
            SendMail.sendMail(email, "White test", "White test di tutti i pazienti", config.getCsvsPath()+File.separator+"whitetest.csv");
        }

        if(nsptestExportResult == 0 && raotestExportResult == 0 && whitetestExportResult == 0)
            return new ModelAndView("redirect:/showpatients");

        logger.debug("... export of NPS test failed");
        model.addAttribute("message", "Esportazione profili NPS fallita");
        model.addAttribute("back", "showpatients");
        model.addAttribute("home", "adminhome");
        return new ModelAndView("error");
    }
    
}
