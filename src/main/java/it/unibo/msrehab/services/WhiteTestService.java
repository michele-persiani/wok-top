package it.unibo.msrehab.services;

import it.unibo.msrehab.model.JPAModel;
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

    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();

    public WhiteTestService()
    {
        super();
        this.whiteTestController = new WhiteTestController(model);
        this.userController = new MSRUserController(model);

    }

    @RequestMapping(value = "/deletewhitetest", method = RequestMethod.POST)
    public ModelAndView deleteWhiteTest(
            @RequestParam(value = "whitetestid", required = true) Integer whitetestid,
            Model model)
    {

        logger.info("Start to delete profile " + whitetestid);
        if (whitetestid == null)
            return new ModelAndView("redirect:/showpatients");

        WhiteTest whiteTest = whiteTestController.getEntity(whitetestid);

        whiteTestController.removeEntity(whiteTest);
        logger.info("White Test " + whitetestid + " deleted with success");
        return new ModelAndView("redirect:/showpatients");
    }

   // show update form
    @RequestMapping(value = "/whitetestform", method = RequestMethod.GET)
    public ModelAndView whitetestform(
            @RequestParam Integer  patientid,
            @RequestParam Integer formid,
            Model model)
    {
        
        logger.debug("whitetestform() : {}", patientid);

        WhiteTest test;
        if (formid != null)
            test = whiteTestController.getEntity(formid);
        else
        {
            test = new WhiteTest();
            test.setPatientid(patientid);
        }
        model.addAttribute("patientid", patientid);
        model.addAttribute("whiteTestForm", test);
        model.addAttribute(
                "patientname",
                userController.getEntity(patientid).getName()
                + " " + userController.getEntity(patientid).getSurname());

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
            @RequestParam Integer patientid,
            Model model)
    {

        logger.debug("whitetestsel() : {}", patientid);

        List<WhiteTest> whiteTestList = whiteTestController.findAllByUser(patientid);
        model.addAttribute("patientid", patientid);
        model.addAttribute("whitetestlist", whiteTestList);
        model.addAttribute(
                "patientname",
                userController.getEntity(patientid).getName()
                + " " + userController.getEntity(patientid).getSurname());

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
            @RequestParam Integer patientid,
            @RequestParam Integer formid1,
            @RequestParam Integer formid2,
            Model model)
    {

        logger.debug("whitetestcompform() : {}", formid1, formid2);

        WhiteTest test1 = whiteTestController.getEntity(formid1),
                test2 = whiteTestController.getEntity(formid2);
        
        model.addAttribute("patientid", patientid);
        model.addAttribute("test1", test1);
        model.addAttribute("test2", test2);
        model.addAttribute(
                "patientname",
                userController.getEntity(patientid).getName()
                + " " + userController.getEntity(patientid).getSurname());

        return new ModelAndView("whitetestcomp-table");
    }
    
    @RequestMapping(value = "/saveorupdatewhitetest", method = RequestMethod.POST)
    public ModelAndView saveorupdatewhitetest(
            @Valid @ModelAttribute("whiteTestForm") WhiteTest whiteTestForm,
            BindingResult result,
            Model model)
    {

        logger.debug("saveOrUpdateWhiteTest() : {}", whiteTestForm);

        if (result.hasErrors())
        {
            model.addAttribute("patientid", whiteTestForm.getPatientid());
            model.addAttribute(
                "patientname", userController.getEntity(whiteTestForm.getPatientid()).getName()
                + " " + userController.getEntity(whiteTestForm.getPatientid()).getSurname());
            return new ModelAndView("whitetest-form");
        }

        whiteTestForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

        whiteTestController.putEntity(whiteTestForm);
        logger.info("Add of White Test " + whiteTestForm.getId() + " terminated with success");
        return new ModelAndView("redirect:/npsprofileform?patientid="+whiteTestForm.getPatientid());
    }

}
