package it.unibo.msrehab.services;

import flexjson.JSONDeserializer;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.entities.MagneticResonance;
import it.unibo.msrehab.model.controller.MagneticResonanceController;

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
 * Class that provides servlets for patient resonance.
 */
@Controller
public class MagneticResonanceService {

    private static final Logger logger = LoggerFactory
            .getLogger(MagneticResonanceService.class);
    private final MagneticResonanceController resonanceController;
    private final MSRUserController userController;

    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();
    
    public MagneticResonanceService() {
        super();
        this.resonanceController = new MagneticResonanceController(model);
        this.userController = new MSRUserController(model);
    }

    @RequestMapping(value = "/deleteresonance", method = RequestMethod.POST)
    public ModelAndView deleteResonance(
            @RequestParam(value = "resonanceid", required = true) Integer resonanceid,
            Model model) {

        logger.info("Start to delete resonance " + resonanceid);
        if (resonanceid == null) {
            return new ModelAndView("redirect:/showpatients");
        }
        MagneticResonance resonance = resonanceController.findEntity(resonanceid).orElse(null);
        
        if (resonance == null) {
            logger.warn("Resonance " + resonanceid + " not found");
            model.addAttribute("message", "Risonanza non trovata");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }
        resonanceController.removeEntity(resonance);
        logger.info("Resonance " + resonanceid + " deleted with success");
        return new ModelAndView("redirect:/showpatients");
    }

    // show resonance form
    @RequestMapping(value = "/resonanceform", method = RequestMethod.GET)
    public ModelAndView resonanceForm(
            @RequestParam(value = "patientid", required = true) Integer clinicalprofileid,
            Model model) {

        logger.debug("magneticresonanceForm() : {}", clinicalprofileid);
        List<MagneticResonance> resonanceList
                = resonanceController.findAllByClinicalprofileid(clinicalprofileid);
        MagneticResonance resonance;
        if (resonanceList.size() > 0) {
            resonance = resonanceList.get(0);
        } else {
            resonance = new MagneticResonance();
            resonance.setClinicalprofileid(clinicalprofileid);
        }
        model.addAttribute("resonanceForm", resonance);
        model.addAttribute(
                "patientname",
                userController.findEntity(clinicalprofileid).get().getName()
                + " " + userController.findEntity(clinicalprofileid).get().getSurname());

        return new ModelAndView("resonance-form");
    }

    /**
     * Creates a new resonance for a given patient, given as a json
     *
     * @param json The patient's resonance
     *
     * @return the id of the profile (>0) if the profile is inserted in the
     * database, -1 otherwise; HttpStatus.NOT_ACCEPTABLE the patient's id is not
     * correct; HttpStatus.OK if the profile is inserted;
     * HttpStatus.SERVICE_UNAVAILABLE if the profile could not be inserted in
     * the database
     */
    @RequestMapping(value = "/newresonance", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> newResonance(
            @RequestBody String json) {

        logger.info("Starting register a new patient resonance...");

        try {
            MagneticResonance resonance
                    = new JSONDeserializer<MagneticResonance>().deserialize(json, MagneticResonance.class);

            // Validate profile
            Integer userid = resonance.getClinicalprofileid();

            if (userid != null && userController.findEntity(userid) == null) {
                logger.warn("...given user id is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }

            //Set timestamp
            Long t = LocalDateTime.now().toDateTime().getMillis();
            resonance.setTimestamp(t);

            resonanceController.insertEntity(resonance);
            logger.info("...resonance registered with success with id " + resonance.getId());
            return new ResponseEntity<>(resonance.getId().toString(), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Updates a patient resonance, given as a json
     *
     * @param json The patient's resonance
     *
     * @return 1 if the profile is updated, -1 otherwise;
     * HttpStatus.NOT_ACCEPTABLE if the profile with the given id was not found;
     * HttpStatus.OK if the given profile is updated;
     * HttpStatus.SERVICE_UNAVAILABLE if the update failed
     */
    @RequestMapping(value = "/updateresonance", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> updateResonance(
            @RequestBody String json) {

        logger.info("Starting update resonance");

        try {
            MagneticResonance newResonance
                    = new JSONDeserializer<MagneticResonance>().deserialize(json, MagneticResonance.class);

            Integer profileid = newResonance.getId();

            MagneticResonance oldResonance = resonanceController.findEntity(profileid).get();
            if (oldResonance == null) {
                logger.warn("Resonance " + profileid + " not found");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }

            logger.info("new resonance: " + json);

            newResonance.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

            resonanceController.updateEntity(newResonance);
            logger.info("Update of resonance " + profileid + " terminated with success");
            return new ResponseEntity<String>("1", HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    
    /**
     * Saves or updates a patient resonance
     * @param resonanceForm
     * @param result
     * @param model
     * @return 
     */
    @RequestMapping(value = "/saveorupdateresonance", method = RequestMethod.POST)
    public ModelAndView saveOrUpdateResonance(
            @Valid @ModelAttribute("resonanceForm") MagneticResonance resonanceForm,
            BindingResult result,
            Model model) {

        logger.debug("saveOrUpdateResonance() : {}", resonanceForm);

        if (result.hasErrors()) {
            model.addAttribute(
                "patientname", userController.findEntity(resonanceForm.getClinicalprofileid()).get().getName()
                + " " + userController.findEntity(resonanceForm.getClinicalprofileid()).get().getSurname());
            return new ModelAndView("resonance-form");
        }

        resonanceForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

        resonanceController.putEntity(resonanceForm);
        logger.info("Add of resonance " + resonanceForm.getId() + " terminated with success");
        return new ModelAndView("redirect:/showpatients");

    }
}
