package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.WhiteTest;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.controller.WhiteTestController;
import java.util.List;
import javax.validation.Valid;
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
 * Class that provides servlets for White test.
 * The services are the following:
 * newwhitetest inserts a White test entry in the database;
 * updatewhitetest: updates a White test ;
 * deletewhitetest: deletes a White test ;
 */

@Controller
public class WhiteTestService {

    private static final Logger logger = LoggerFactory
            .getLogger(WhiteTestService.class);
    private MSRUserController userController;
    private WhiteTestController whiteTestController;
    private final Configuration config;

    public WhiteTestService() {
        super();
        this.whiteTestController = new WhiteTestController();
        this.userController = new MSRUserController();
        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);

    }

    @RequestMapping(value = "/deletewhitetest", method = RequestMethod.POST)
    public ModelAndView deleteWhiteTest(
            @RequestParam(value = "whitetestid", required = true) Integer whitetestid,
            Model model) {

        logger.info("Start to delete profile " + whitetestid);
        if (whitetestid == null) {
            return new ModelAndView("redirect:/showpatients");
        }
        WhiteTest whiteTest = (WhiteTest) whiteTestController.findEntity(whitetestid).get();
        
        if (whiteTest == null) {
            logger.warn("White Test " + whitetestid + " not found");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "White Test non trovato");
            return new ModelAndView("error");            
        }
        if (whiteTestController.removeEntity(whiteTest)) {
            logger.info("White Test " + whitetestid + " deleted with success");
            return new ModelAndView("redirect:/showpatients");
        } else {  // deleteRecord(user) == -1
            logger.error("...white test delete failed");
            model.addAttribute("message", "White test " + whitetestid + ", cancellazione fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");    
        }
    }

   // show update form
    @RequestMapping(value = "/whitetestform", method = RequestMethod.GET)
    public ModelAndView whitetestform(
            @RequestParam(value = "patientid", required = true) Integer  patientid,
            @RequestParam(value = "formid", required = true) Integer formid,
            Model model) {
        
        logger.debug("whitetestform() : {}", patientid);
        
        //if (personalDataController.findAllByUser(patientid).isEmpty()) {
        //    return new ModelAndView("redirect:/showpatients");
        //} else {
        WhiteTest test;
        if (formid != null) {
            test = whiteTestController.findEntity(formid).get();
        } else {
            test = new WhiteTest();
            test.setPatientid(patientid);
        }
        model.addAttribute("patientid", patientid);
        model.addAttribute("whiteTestForm", test);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("whitetest-form");
    }
    
    /**
     * Select White test form
     * 
     * @param patientid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/whitetestsel", method = RequestMethod.GET)
    public ModelAndView whitetestsel(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            Model model) {

        logger.debug("whitetestsel() : {}", patientid);

        List<WhiteTest> whiteTestList
                = whiteTestController.findAllByUser(patientid);
        model.addAttribute("patientid", patientid);
        model.addAttribute("whitetestlist", whiteTestList);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("whitetestsel");
    }

    /**
     * Compares White test forms
     * 
     * @param patientid
     * @param formid1
     * @param formid2
     * @param model
     * @return 
     */
    @RequestMapping(value = "/whitetestcompform", method = RequestMethod.GET)
    public ModelAndView whitetestcompform(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "formid1", required = true) Integer formid1,
            @RequestParam(value = "formid2", required = true) Integer formid2,
            Model model) {

        logger.debug("whitetestcompform() : {}", formid1, formid2);

        WhiteTest test1 = whiteTestController.findEntity(formid1).get(),
                test2 = whiteTestController.findEntity(formid2).get();
        
        model.addAttribute("patientid", patientid);
        model.addAttribute("test1", test1);
        model.addAttribute("test2", test2);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("whitetestcomp-table");
    }
    
    @RequestMapping(value = "/saveorupdatewhitetest", method = RequestMethod.POST)
    public ModelAndView saveorupdatewhitetest(
            @Valid @ModelAttribute("whiteTestForm") WhiteTest whiteTestForm,
            BindingResult result,
            Model model) {

        logger.debug("saveOrUpdateWhiteTest() : {}", whiteTestForm);

        if (result.hasErrors()) {
            model.addAttribute("patientid", whiteTestForm.getPatientid());
            model.addAttribute(
                "patientname", userController.findEntity(whiteTestForm.getPatientid()).get().getName()
                + " " + userController.findEntity(whiteTestForm.getPatientid()).get().getSurname());
            return new ModelAndView("whitetest-form");
        }

        whiteTestForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

        if (whiteTestForm.isNew()) {
            if (whiteTestController.insertEntity(whiteTestForm))
            {
                logger.info("Add of White Test " + whiteTestForm.getId() + " terminated with success");
                return new ModelAndView("redirect:/npsprofileform?patientid="+whiteTestForm.getPatientid());
            } else {  // addRecord(whiteTestForm) == -1
                logger.error("...White test addition failed");
                model.addAttribute("message", "Inserimento White test fallito");
                model.addAttribute("back", "whitetest-form");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");
            }
        } else { // update
            if (whiteTestController.updateEntity(whiteTestForm)) {
                logger.info("Update of White Test " + whiteTestForm.getId() + " terminated with success");
                return new ModelAndView("redirect:/npsprofileform?patientid="+whiteTestForm.getPatientid());
            } else {    // updateRecord(whiteTestForm) == -1
                logger.error("...white test update failed");
                model.addAttribute("message", "Aggiornamento White test fallito");
                model.addAttribute("back", "whitetest-form");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");
            }
        }
    }

}
