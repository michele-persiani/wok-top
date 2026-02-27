package it.unibo.msrehab.services;

import flexjson.JSONDeserializer;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.controller.ClinicalProfileController;
import it.unibo.msrehab.model.entities.ClinicalProfile;
import it.unibo.msrehab.model.entities.PersonalData;
import it.unibo.msrehab.model.entities.RaoTest;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.controller.PersonalDataController;
import it.unibo.msrehab.model.controller.RaoTestController;
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
 * Class that provides servlets for Rao test. The services are the following:
 newRaoTest inserts a Rao test entry in the database;
 updateraotest updates a Rao test entry;
 deleteraotest: deletes a Rao test entry.
 */
@Controller
public class RaoTestService {

    private static final Logger logger = LoggerFactory
            .getLogger(RaoTestService.class);
    private final RaoTestController raoTestController;
    private final ClinicalProfileController clinicalProfileController;
    private final PersonalDataController personalDataController;
    private final MSRUserController userController;
    private final Configuration config;

    public RaoTestService() {
        super();
        this.raoTestController = new RaoTestController();
        this.clinicalProfileController = new ClinicalProfileController();
        this.personalDataController = new PersonalDataController();
        this.userController = new MSRUserController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);

    }

   // show update form
    @RequestMapping(value = "/raotestform", method = RequestMethod.GET)
    public ModelAndView raotestForm(
            @RequestParam(value = "patientid", required = true) Integer  patientid,
            @RequestParam(value = "formid", required = true) Integer formid,
            Model model) {
        
        logger.debug("raortestForm() : {}", patientid);
        
        //if (personalDataController.findAllByUser(patientid).isEmpty()) {
        //    return new ModelAndView("redirect:/showpatients");
        //} else {
        RaoTest test;
        if (formid != null) {
            test = raoTestController.findEntity(formid).get();
        } else {
            test = new RaoTest();
            test.setUserid(patientid);
        }
        model.addAttribute("patientid", patientid);
        model.addAttribute("raotestForm", test);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("raotest-form");
    }
    
    /**
     * Creates a new Rao test for a given profile, given as a json
     *
     * @param json The patient's Rao test
     *
     * @return the id of the Rao test (>0) if the test is inserted in the
     * database, -1 otherwise; HttpStatus.NOT_ACCEPTABLE the profile's id is not
     * correct; HttpStatus.OK if the test is inserted;
     * HttpStatus.SERVICE_UNAVAILABLE if the test could not be inserted in the
     * database
     */
    @RequestMapping(value = "/newraotest", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> newRaoTest(
            @RequestBody String json) {

        logger.info("Starting register a new rao test...");

        try {
            RaoTest newRaotest
                    = new JSONDeserializer<RaoTest>().deserialize(json, RaoTest.class);

            // Validate Rao test
            Integer profileid = newRaotest.getUserid();
            
            ClinicalProfile p = clinicalProfileController.findEntity(profileid).get();
            PersonalData d = personalDataController.findEntity(p.getUserid()).get();

            if (profileid != null && p == null) {
                logger.warn("...given profile id is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isPasat2Valid(newRaotest.getPasat2raw())) {
                logger.warn("...given pasat 2 is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isPasat3Valid(newRaotest.getPasat3raw())) {
                logger.warn("...given pasat 3 is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isSdmtValid(newRaotest.getSdmtraw())) {
                logger.warn("...given sdmt is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isSpart1036Valid(newRaotest.getSpart1036raw())) {
                logger.warn("...given spart1036 is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isSpartdValid(newRaotest.getSpartdraw())) {
                logger.warn("...given spartd is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }            
            if (!RaoTest.isSrtcltrValid(newRaotest.getSrtcltrraw())) {
                logger.warn("...given srtcltr is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }            
            if (!RaoTest.isSrtdValid(newRaotest.getSrtdraw())) {
                logger.warn("...given srtd is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }       
            if (!RaoTest.isSrtltsValid(newRaotest.getSrtltsraw())) {
                logger.warn("...given srtlts is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }       
            if (!RaoTest.isWlgValid(newRaotest.getWlgraw())) {
                logger.warn("...given wlg is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }       

            // Set derivate parameters
            newRaotest.setPasat2adj(d);
            newRaotest.setPasat2eq();
            newRaotest.setPasat3adj(d);
            newRaotest.setPasat3eq();
            newRaotest.setSdmtadj(d);
            newRaotest.setSdmteq();
            newRaotest.setSpart1036adj(d);
            newRaotest.setSpart1036eq();
            newRaotest.setSpartdadj(d);
            newRaotest.setSpartdeq();
            newRaotest.setSrtcltradj(d);
            newRaotest.setSrtcltreq();
            newRaotest.setSrtdadj(d);
            newRaotest.setSrtdeq();
            newRaotest.setWlgadj(d);
            newRaotest.setWlgeq();
            
            //Set timestamp
            //newRaotest.setTimestamp((Timestamp.valueOf(LocalDateTime.now())));
            newRaotest.setTimestamp(LocalDateTime.now().toDateTime().getMillis());
            
            boolean success = raoTestController.insertEntity(newRaotest);
            if (success) {
                logger.info("...Rao test registered with success with id " + newRaotest.getId());
                return new ResponseEntity<String>(newRaotest.getId().toString(), HttpStatus.OK);
            } else {
                logger.error("...Rao test could not be registered");
                return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Select Rao test form
     * 
     * @param patientid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/raotestsel", method = RequestMethod.GET)
    public ModelAndView raotestsel(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            Model model) {

        logger.debug("raotestsel() : {}", patientid);

        List<RaoTest> raoTestList
                = raoTestController.findAllByUser(patientid);
        model.addAttribute("patientid", patientid);
        model.addAttribute("raotestlist", raoTestList);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("raotestsel");
    }

    /**
     * Compares Rao test forms
     * 
     * @param patientid
     * @param formid1
     * @param formid2
     * @param model
     * @return 
     */
    @RequestMapping(value = "/raotestcompform", method = RequestMethod.GET)
    public ModelAndView raotestcompform(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "formid1", required = true) Integer formid1,
            @RequestParam(value = "formid2", required = true) Integer formid2,
            Model model) {

        logger.debug("raotestcompform() : {}", formid1, formid2);

        RaoTest test1 = raoTestController.findEntity(formid1).get(),
                test2 = raoTestController.findEntity(formid2).get();
        
        model.addAttribute("patientid", patientid);
        model.addAttribute("test1", test1);
        model.addAttribute("test2", test2);
        model.addAttribute(
                "patientname",
                userController.findEntity(patientid).get().getName()
                + " " + userController.findEntity(patientid).get().getSurname());

        return new ModelAndView("raotestcomp-table");
    }
    
    @RequestMapping(value = "/saveorupdateraotest", method = RequestMethod.POST)
    public ModelAndView saveorupdateraotest(
            @Valid @ModelAttribute("raotestForm") RaoTest raoTestForm,
            BindingResult result,
            Model model) {
    
        logger.info("Starting update Rao test");
        
        Integer userid = raoTestForm.getUserid();
        
        if (result.hasErrors()) {
            model.addAttribute("patientid", userid);
            model.addAttribute(
                    "patientname",
                    userController.findEntity(userid).get().getName()
                    + " " + userController.findEntity(userid).get().getSurname());

            return new ModelAndView("raotest-form");
        }
        else {
            PersonalData d = personalDataController.findAllByUser(userid).get(0);
            // Set derivate parameters
            raoTestForm.setPasat2adj(d);
            raoTestForm.setPasat2eq();
            raoTestForm.setPasat3adj(d);
            raoTestForm.setPasat3eq();
            raoTestForm.setSdmtadj(d);
            raoTestForm.setSdmteq();
            raoTestForm.setSpart1036adj(d);
            raoTestForm.setSpart1036eq();
            raoTestForm.setSpartdadj(d);
            raoTestForm.setSpartdeq();
            raoTestForm.setSrtcltradj(d);
            raoTestForm.setSrtcltreq();
            raoTestForm.setSrtdadj(d);
            raoTestForm.setSrtdeq();
            raoTestForm.setSrtltsadj(d);
            raoTestForm.setSrtltseq();
            raoTestForm.setWlgadj(d);
            raoTestForm.setWlgeq();
            
            //Set timestamp
            raoTestForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());
            
            if (raoTestForm.isNew()) {
                if(raoTestController.insertEntity(raoTestForm))
                {
                    logger.info("Add of  raotest " + raoTestForm.getId() + " terminated with success");
                    return new ModelAndView("redirect:/npsprofileform?patientid="+raoTestForm.getUserid());
                }
                else {  // addRecord(raoTestForm) == -1
                    logger.error("...raotest add failed");
                    model.addAttribute("message", "Inserimento Rao test fallito");
                    model.addAttribute("back", "raotest-form");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }
            }
            else { // update
                if (raoTestController.updateEntity(raoTestForm)) {
                    logger.info("Update of  raotest " + raoTestForm.getId() + " terminated with success");
                    return new ModelAndView("redirect:/npsprofileform?patientid="+raoTestForm.getUserid());
                }
                else {    // updateRecord(raoTestForm) == -1
                    logger.error("...Rao test update failed");
                    model.addAttribute("message", "Aggiornamento Rao test fallito");
                    model.addAttribute("back", "raotest-form");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }
            }

        }
    
    }    

    /**
     * Updates a Rao test, given as a json
     *
     * @param json The Rao test
     *
     * @return 1 if the Rao test is updated, -1 otherwise;
     * HttpStatus.NOT_ACCEPTABLE if the Rao test with the given id was not found;
     * HttpStatus.OK if the given Rao test is updated;
     * HttpStatus.SERVICE_UNAVAILABLE if the update failed
     */
    @RequestMapping(value = "/updateraotest", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> updateRaoTest(
            @RequestBody String json) {

        logger.info("Starting update Rao test");

        try {
            RaoTest newRaotest
                    = new JSONDeserializer<RaoTest>().deserialize(json, RaoTest.class);

            Integer raotestid = newRaotest.getId();

            RaoTest oldRaotest = raoTestController.findEntity(raotestid).get();
            if (oldRaotest == null) {
                logger.warn("Rao test " + raotestid + " not found");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            
            if (!RaoTest.isPasat2Valid(newRaotest.getPasat2raw())) {
                logger.warn("...given pasat 2 is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isPasat3Valid(newRaotest.getPasat3raw())) {
                logger.warn("...given pasat 3 is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isSdmtValid(newRaotest.getSdmtraw())) {
                logger.warn("...given sdmt is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isSpart1036Valid(newRaotest.getSpart1036raw())) {
                logger.warn("...given spart1036 is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!RaoTest.isSpartdValid(newRaotest.getSpartdraw())) {
                logger.warn("...given spartd is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }            
            if (!RaoTest.isSrtcltrValid(newRaotest.getSrtcltrraw())) {
                logger.warn("...given srtcltr is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }            
            if (!RaoTest.isSrtdValid(newRaotest.getSrtdraw())) {
                logger.warn("...given srtd is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }       
            if (!RaoTest.isSrtltsValid(newRaotest.getSrtltsraw())) {
                logger.warn("...given srtlts is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }       
            if (!RaoTest.isWlgValid(newRaotest.getWlgraw())) {
                logger.warn("...given wlg is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }       

            // Set derivate parameters
            ClinicalProfile p = clinicalProfileController.findEntity(newRaotest.getUserid()).get();
            PersonalData d = personalDataController.findEntity(newRaotest.getUserid()).get();
                        
            newRaotest.setPasat2adj(d);
            newRaotest.setPasat2eq();
            newRaotest.setPasat3adj(d);
            newRaotest.setPasat3eq();
            newRaotest.setSdmtadj(d);
            newRaotest.setSdmteq();
            newRaotest.setSpart1036adj(d);
            newRaotest.setSpart1036eq();
            newRaotest.setSpartdadj(d);
            newRaotest.setSpartdeq();
            newRaotest.setSrtcltradj(d);
            newRaotest.setSrtcltreq();
            newRaotest.setSrtdadj(d);
            newRaotest.setSrtdeq();
            newRaotest.setWlgadj(d);
            newRaotest.setWlgeq();
            
            //Set timestamp
            //newRaotest.setTimestamp((Timestamp.valueOf(LocalDateTime.now())));
            newRaotest.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

            if (raoTestController.updateEntity(newRaotest)) {
                logger.info("Update of Rao test " + raotestid +
                        " terminated with success");
                return new ResponseEntity<String>("1", HttpStatus.OK);
            } else {    // updateRecord(newRaotest) == -1
                logger.error("...Rao test update failed");
                return new ResponseEntity<String>("-1",
                        HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Deletes a Rao test
     *
     * @param raotestid the id of the Rao test
     *
     * @return 1 if the Rao test is deleted, -1 otherwise;
     * HttpStatus.NOT_ACCEPTABLE if the Rao test with the given id was not found;
     * HttpStatus.OK if the Rao test is deleted with success;
     * HttpStatus.SERVICE_UNAVAILABLE if the Rao test cannot be deleted
     */
    @RequestMapping(value = "/deleteraotest", method = RequestMethod.POST)
    public ModelAndView deleteRaoTest(
            @RequestParam(value = "raotestid", required = true) Integer raotestid,
            Model model) {
        logger.info("Starting detete Rao test " + raotestid);
        if (raotestid == null) {
            return new ModelAndView("redirect:/showpatients");
        }

        RaoTest test = raoTestController.findEntity(raotestid).get();
        if (test == null) {
            logger.warn("Rao test " + raotestid + " not found");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Rao test non trovato");
            return new ModelAndView("error");
        }
        if (raoTestController.removeEntity(test)) {
            logger.info("Rao test " + raotestid + " deleted with success");
            return new ModelAndView("redirect:/showpatients");
        } else {  // deleteRaoTest(test) == -1
            logger.error("...Rao test deletion failed");
            model.addAttribute("message", "Rao test " + raotestid + ", cancellazione fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }
    }
    
//    // save or update profiles
//    @RequestMapping(value = "/saveorupdateraotest", method = RequestMethod.POST)
//    public ModelAndView saveOrUpdateRaotest(@ModelAttribute("raotestForm") @Validated RaoTest raotest,
//            BindingResult result, Model model, final RedirectAttributes redirectAttributes) {
//
//        logger.debug("saveOrUpdateRaotest() : {}", raotest);
//
//        if (result.hasErrors()) {
//            //populateDefaultModel(model);
//            return new ModelAndView("redirect:/showpatients");
//        }
//        else {
//            raotest.setPasat2adj(p);
//            raotest.setPasat2eq();
//            raotest.setPasat3adj(null);
//            raotest.setPasat3eq();
//            raotest.setSdmtadj(null);
//            raotest.setSdmteq();
//            raotest.setSpart1036adj(null);
//            raotest.setSpart1036eq();
//            raotest.setSpartdadj(null);
//            raotest.setSpartdeq();
//            raotest.setSrtcltradj(null);
//            raotest.setSrtcltreq();
//            raotest.setSrtdadj(null);
//            raotest.setSrtdeq();
//            raotest.setSrtltsadj(null);
//            raotest.setSrtltseq();
//            raotest.setWlgadj(null);
//            raotest.setWlgeq();            
//            
//            if (raotest.isNew()) {
//                if(raotestController.addRecord(raotest)>0) {
//                    redirectAttributes.addFlashAttribute("css", "success");
//                    redirectAttributes.addFlashAttribute("msg", "User added successfully!");
//                    logger.info("Add of  user " + raotest.getId() + " terminated with success");
//                    return new ModelAndView("redirect:/showpatients");
//                }
//                else {  // addRecord(user) == -1
//                    logger.error("...user add failed");
//                    //return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
//                    return new ModelAndView("patient-form");
//                }
//            }
//            else { // update
//                if (raotestController.updateRecord(raotest) == 1) {
//                    redirectAttributes.addFlashAttribute("css", "success");
//                    redirectAttributes.addFlashAttribute("msg", "User updated successfully!");
//                    logger.info("Update of  user " + raotest.getId() + " terminated with success");
//                    //return new ResponseEntity<String>("1", HttpStatus.OK);
//                    return new ModelAndView("redirect:/showpatients");
//                }
//                else {    // updateRecord(updatedUser) == -1
//                    logger.error("...user update failed");
//                    //return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
//                    return new ModelAndView("patient-form");
//                }
//            }
//        }
//    }    
}
