package it.unibo.msrehab.services;

import flexjson.JSONDeserializer;
import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.controller.ClinicalProfileController;
import it.unibo.msrehab.model.entities.ClinicalProfile;
import it.unibo.msrehab.model.entities.MagneticResonance;
import it.unibo.msrehab.model.controller.MagneticResonanceController;
import it.unibo.msrehab.model.controller.PersonalDataController;
import it.unibo.msrehab.util.CsvHelper;
import it.unibo.msrehab.util.SendMail;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class that provides servlets for patient profiles. The services are the
 following: newClinicalProfile inserts a patient profile entry in the database;
 updateprofile updates a patient profile entry; deleteprofile: deletes a
 patient profile entry.
 */
@Controller
public class ClinicalProfileService {

    private static final Logger logger = LoggerFactory
            .getLogger(ClinicalProfileService.class);
    private final PersonalDataController personalDataController;
    private final ClinicalProfileController clinicalProfileController;
    private final MSRUserController userController;
    private final MagneticResonanceController mrController;
    private final Configuration config;
    
    private final String MR_REPORT_NAME = "mr-report-";
    
    public ClinicalProfileService() {
        super();
        this.clinicalProfileController = new ClinicalProfileController();
        this.personalDataController = new PersonalDataController();
        this.userController = new MSRUserController();
        this.mrController = new MagneticResonanceController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);
    }

    /**
     * Deletes a patient's clinical profile
     * @param profileid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/deleteclinicalprofile", method = RequestMethod.POST)
    public ModelAndView deleteClinicalProfile(
            @RequestParam(value = "profileid", required = true) Integer profileid,
            Model model) {

        logger.info("Start to delete clincal profile " + profileid);
        if (profileid == null) {
            return new ModelAndView("redirect:/showpatients");
        }
        ClinicalProfile profile = (ClinicalProfile) clinicalProfileController.findEntity(profileid).get();
        
        if (profile == null) {
            logger.error("Clinical profile " + profileid + " not found");
            model.addAttribute("message", "Profilo clinico non trovato");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }
        if (clinicalProfileController.removeEntity(profile)) {
            logger.info("Clinical profile " + profileid + " deleted with success");
            return new ModelAndView("redirect:/showpatients");
        }
        else {  // deleteRecord(user) == -1
            logger.error("...clinical profile delete failed");
            model.addAttribute("message", "Cancellazione del profilo clinico " + profileid + " fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }
    }

    /**
     * Shows a clinical profile form
     * 
     * @param patientid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/clinicalprofileform", method = RequestMethod.GET)
    public ModelAndView clinicalprofileForm(
            @RequestParam(value = "patientid", required = true) Integer patientid,
            Model model) {

        logger.debug("clinicalprofileForm() : {}", patientid);

        if (personalDataController.findAllByUser(patientid).isEmpty()) {
            model.addAttribute("back", "showpatients");                
            model.addAttribute("home", "adminhome");                
            model.addAttribute("message", "Compilare prima il profilo anagrafico");                
            return new ModelAndView("error");
        } else {
            List<ClinicalProfile> profileList
                    = clinicalProfileController.findAllByUser(patientid);
            ClinicalProfile profile;
            if (profileList.size() > 0) {
                profile = profileList.get(0);
            } else {
                profile = new ClinicalProfile();
                profile.setUserid(patientid);
            }
            
            List<MagneticResonance> mrList = mrController.findAllByClinicalprofileid(profile.getId());
            
            model.addAttribute("clinicalprofileForm", profile);
            model.addAttribute("mrlist", mrList);
            model.addAttribute(
                    "patientname",
                    userController.findEntity(patientid).get().getName()
                    + " " + userController.findEntity(patientid).get().getSurname());

            return new ModelAndView("clinicalprofile-form");
        }
    }

    /**
     * Creates a new profile for a given patient, given as a json
     *
     * @param json The patient's profile
     *
     * @return the id of the profile (>0) if the profile is inserted in the
     * database, -1 otherwise; HttpStatus.NOT_ACCEPTABLE the patient's id is not
     * correct; HttpStatus.OK if the profile is inserted;
     * HttpStatus.SERVICE_UNAVAILABLE if the profile could not be inserted in
     * the database
     */
    @RequestMapping(value = "/newclinicalprofile", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> newClinicalProfile(
            @RequestBody String json) {

        logger.info("Starting register a new patient clinical profile...");

        try {
            ClinicalProfile profile
                    = new JSONDeserializer<ClinicalProfile>().deserialize(json, ClinicalProfile.class);

            // Validate profile
            Integer userid = profile.getUserid();

            if (userid != null && userController.findEntity(userid) == null) {
                logger.warn("...given user id is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (profile.getDiagnosysyear() != null
                    && !ClinicalProfile.isDignosysyearValid(profile.getDiagnosysyear())) {
                logger.warn("...given dignosys year is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
//            if (profile.getMmseraw() != null
//                    && !ClinicalProfile.isMmseValid(profile.getMmseraw())) {
//                logger.warn("...given mmse is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (profile.getMfisc() != null
//                    && !ClinicalProfile.isMfiscValid(profile.getMfisc())) {
//                logger.warn("...given mfisc is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (profile.getMfisp() != null
//                    && !ClinicalProfile.isMfispValid(profile.getMfisp())) {
//                logger.warn("...given mfisp is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (profile.getMfisps() != null
//                    && !ClinicalProfile.isMfispsValid(profile.getMfisps())) {
//                logger.warn("...given mfisps is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (profile.getHadsa() != null
//                    && !ClinicalProfile.isHadsaValid(profile.getHadsa())) {
//                logger.warn("...given hadsa is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (profile.getHadsd() != null
//                    && !ClinicalProfile.isHadsdValid(profile.getHadsd())) {
//                logger.warn("...given hadsd is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (profile.getTmtaraw() != null
//                    && !ClinicalProfile.isTmtarawValid(profile.getTmtaraw())) {
//                logger.warn("...given tmta is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (profile.getTmtbraw() != null
//                    && !ClinicalProfile.isTmtbrawValid(profile.getTmtbraw())) {
//                logger.warn("...given tmtb is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }

            // Set derivate parameters
//            int birthyear = personalDataController.getRecord(profile.getUserid()).getBirthyear();
//            int educationyears = personalDataController.getRecord(profile.getUserid()).getEducationyears();
            
//            profile.setHadstot();
//            profile.setMfistot();
//            profile.setMmseadj(birthyear, educationyears);
//            profile.setTmtaadj(birthyear, educationyears);
//            profile.setTmtbadj(birthyear, educationyears);
//            profile.setTmtbaraw();
//            profile.setTmtbaadj(birthyear, educationyears);
//            profile.setTmtaeqFromTmtaadj();
//            profile.setTmtbeqFromTmtbadj();
//            profile.setTmtbaeqFromTmtbaadj();

            //Set timestamp
            Long t = LocalDateTime.now().toDateTime().getMillis();
            profile.setTimestamp(t);

            boolean success = clinicalProfileController.insertEntity(profile);
            if (success) {
                logger.info("...clinical profile registered with success with id " + profile.getId());
                return new ResponseEntity<String>(profile.getId().toString(), HttpStatus.OK);
            } else {
                logger.error("...clinial profile could not be registered");
                return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    
    /**
     * Updates a patient's clinical profile, given as a json
     *
     * @param json The patient's clinical profile
     *
     * @return 1 if the profile is updated, -1 otherwise;
     * HttpStatus.NOT_ACCEPTABLE if the profile with the given id was not found;
     * HttpStatus.OK if the given profile is updated;
     * HttpStatus.SERVICE_UNAVAILABLE if the update failed
     */
    @RequestMapping(value = "/updateclinicalprofile", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> updateCinicalProfile(
            @RequestBody String json) {

        logger.info("Starting update clinical profile");

        try {
            ClinicalProfile newProfile
                    = new JSONDeserializer<ClinicalProfile>().deserialize(json, ClinicalProfile.class);

            Integer profileid = newProfile.getId();

            ClinicalProfile oldProfile = clinicalProfileController.findEntity(profileid).get();
            if (oldProfile == null) {
                logger.warn("Clinical profile " + profileid + " not found");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
            if (newProfile.getDiagnosysyear() != null
                    && !ClinicalProfile.isDignosysyearValid(newProfile.getDiagnosysyear())) {
                logger.warn("...given dignosys year is not valid");
                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
            }
//            if (newProfile.getMmseraw() != null
//                    && !ClinicalProfile.isMmseValid(newProfile.getMmseraw())) {
//                logger.warn("...given mmse is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (newProfile.getMfisc() != null
//                    && !ClinicalProfile.isMfiscValid(newProfile.getMfisc())) {
//                logger.warn("...given mfisc is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (newProfile.getMfisp() != null
//                    && !ClinicalProfile.isMfispValid(newProfile.getMfisp())) {
//                logger.warn("...given mfisp is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (newProfile.getMfisps() != null
//                    && !ClinicalProfile.isMfispsValid(newProfile.getMfisps())) {
//                logger.warn("...given mfisps is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (newProfile.getTmtaraw() != null
//                    && !ClinicalProfile.isTmtarawValid(newProfile.getTmtaraw())) {
//                logger.warn("...given tmta is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (newProfile.getTmtbraw() != null
//                    && !ClinicalProfile.isTmtbrawValid(newProfile.getTmtbraw())) {
//                logger.warn("...given tmtb is not valid");
//                return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
//            }

            logger.info("new clinical profile: " + json);

            // Set derivate parameters
//            int birthyear = personalDataController.getRecord(oldProfile.getUserid()).getBirthyear();
//            int educationyears = personalDataController.getRecord(oldProfile.getUserid()).getEducationyears();
            
//            newProfile.setHadstot();
//            newProfile.setMfistot();
//            newProfile.setMmseadj(birthyear, educationyears);
//            newProfile.setTmtaadj(birthyear, educationyears);
//            newProfile.setTmtbadj(birthyear, educationyears);
//            newProfile.setTmtbaraw();
//            newProfile.setTmtbaadj(birthyear, educationyears);
//            newProfile.setTmtaeqFromTmtaadj();
//            newProfile.setTmtbeqFromTmtbadj();
//            newProfile.setTmtbaeqFromTmtbaadj();
            //newProfile.setTimestamp((Timestamp.valueOf(LocalDateTime.now())));

            //newProfile.setTimestamp(new Timestamp(LocalDateTime.now().toDateTime().getMillis()));
            newProfile.setTimestamp(LocalDateTime.now().toDateTime().getMillis());

            if (clinicalProfileController.updateEntity(newProfile)) {
                logger.info("Update of clinical profile " + profileid + " terminated with success");
                return new ResponseEntity<String>("1", HttpStatus.OK);
            } else {    // updateRecord(profile) == -1
                logger.error("...clinical profile update failed");
                return new ResponseEntity<String>("-1", HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            logger.error("... wrong data: " + e.getMessage());
            return new ResponseEntity<String>("-1", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    
    /**
     * Saves or updates a patient clinical profile
     * @param clinicalProfileForm
     * @param result
     * @param model
     * @param mrlastdate
     * @param mrlastreportfile
     * @return 1 if profile is deleted, -1 otherwise; HttpStatus.NOT_ACCEPTABLE
     * if the profile with the given id was not found; HttpStatus.OK if the
     * profile is deleted with success; HttpStatus.SERVICE_UNAVAILABLE if the
     * profile cannot be deleted
     */
    @RequestMapping(value = "/saveorupdateclinicalprofile", method = RequestMethod.POST)
    public ModelAndView saveOrUpdateClinicalProfile(
            @Valid @ModelAttribute("clinicalprofileForm") ClinicalProfile clinicalProfileForm,
            BindingResult result,
            Model model,
            @RequestParam(value = "mrlastdate", required = false) Date mrlastdate,
            @RequestParam(value = "mrlastreportfile", required = false) MultipartFile mrlastreportfile) {

            logger.debug("saveOrUpdateClinicalProfile() : {}", clinicalProfileForm);

        if (result.hasErrors()) {
            List<MagneticResonance> mrList = mrController.findAllByClinicalprofileid(clinicalProfileForm.getId());
            model.addAttribute("mrlist", mrList);
            model.addAttribute(
                "patientname", userController.findEntity(clinicalProfileForm.getUserid()).get().getName()
                + " " + userController.findEntity(clinicalProfileForm.getUserid()).get().getSurname());
            return new ModelAndView("clinicalprofile-form");
        }
        else {
            clinicalProfileForm.setTimestamp(LocalDateTime.now().toDateTime().getMillis());
            MagneticResonance mr = null;            
            if (!mrlastreportfile.isEmpty()) {
                try {
                    // Upload the magnetic risonance file
                    byte[] bytes = mrlastreportfile.getBytes();
                    String originalFileName = mrlastreportfile.getOriginalFilename();

                    String mrReportsPath = config.getMrReportsPath();

                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
                    LocalDateTime today = LocalDateTime.now();
                    String reportDate = df.format(today.toDate());

                    String fileName = MR_REPORT_NAME + reportDate + "." + FilenameUtils.getExtension(originalFileName);
                    String filePath
                            //= mrReportsPath + File.separator + mrReportsPath;
                            = mrReportsPath + File.separator + fileName;

                    // Create the file on server
                    File serverFile = new File(filePath);
                    serverFile.createNewFile();
                    BufferedOutputStream stream
                            = new BufferedOutputStream(
                                    new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();

                    logger.info("Server File Location=" + serverFile.getAbsolutePath());

                    // Create a magnentic resonance record to be stored in the DB
                    mr = new MagneticResonance();
                    mr.setClinicalprofileid(clinicalProfileForm.getId());
                    mr.setReport(fileName);
                    mr.setDate(today);
                    Long t = today.toDateTime().getMillis();
                    mr.setTimestamp(t);
                }
                catch (IOException ex) {
                    logger.error("Error uploading magnetic resonance file: " + ex.getMessage());
                    model.addAttribute("message", "Caricamento file risonanza fallito");
                    model.addAttribute("back", "patient-form");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }
            }
            if (clinicalProfileForm.isNew()) {
                if (clinicalProfileController.insertEntity(clinicalProfileForm)) {
                    if(!mrlastreportfile.isEmpty()) {
                        if(mrController.insertEntity(mr)) {
                            logger.info("Addition of magnetic resonance record " + mr.getId() + " terminated with success");
                        }
                        else {
                            logger.error("...magnetic resonance record " + mr.getId() + " addition failed");
                            model.addAttribute("message", "Aggiunta risonanza magnetica fallita");
                            model.addAttribute("back", "patient-form");
                            model.addAttribute("home", "adminhome");
                            return new ModelAndView("error");
                        }
                    }
                    logger.info("Add of clinical profile " + clinicalProfileForm.getId() + " terminated with success");
                    return new ModelAndView("redirect:/showpatients");
                }
                else {  // addRecord(profile) == -1
                    logger.error("...clinical profile addition failed");
                    model.addAttribute("message", "Inserimento profilo clinico fallito");
                    model.addAttribute("back", "patient-form");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }
            }
            else { // update
                if (clinicalProfileController.updateEntity(clinicalProfileForm)) {
                    //redirectAttributes.addFlashAttribute("css", "success");
                    //redirectAttributes.addFlashAttribute("msg", "ClinicalProfile updated successfully!");
                    if(!mrlastreportfile.isEmpty()) {
                        if(mr.isNew()) {
                            if(mrController.insertEntity(mr))
                                logger.info("Add of magnetic resonance " + mr.getId() + " terminated with success");

                            else {
                                logger.error("...magnetic resonance " + mr.getId() + " addition failed");
                                model.addAttribute("message", "Aggiunta risonanza magnetica fallita");
                                model.addAttribute("back", "patient-form");
                                model.addAttribute("home", "adminhome");
                                return new ModelAndView("error");
                            }
                        }
                        else {
                            if(mrController.updateEntity(mr)) {
                                logger.info("Update of magnetic resonance " + mr.getId() + " terminated with success");
                            }
                            else {
                                logger.error("...magnetic resonance " + mr.getId() + " update failed");
                                model.addAttribute("message", "Aggiornamento risonanza magnetica fallita");
                                model.addAttribute("back", "patient-form");
                                model.addAttribute("home", "adminhome");
                                return new ModelAndView("error");
                            }
                        }
                    }
                    logger.info("Update of clinical profile " + clinicalProfileForm.getId() + " terminated with success");
                    //return new ResponseEntity<String>("1", HttpStatus.OK);
                    return new ModelAndView("redirect:/showpatients");
                }
                else {    // updateRecord(updatedUser) == -1
                    logger.error("...clinical profile " + clinicalProfileForm.getId() + " update failed");
                    model.addAttribute("message", "Aggiornamento profilo "+clinicalProfileForm.getId()+" clinico fallito");
                    model.addAttribute("back", "showpatients");
                    model.addAttribute("home", "adminhome");
                    return new ModelAndView("error");
                }
            }
        }
    }
    
    /**
     * Exports clinical profiles
     * @param email
     * @param model
     * @return 
     */
    @RequestMapping(value = "/exportclinicalprofiles", method = RequestMethod.POST)
    public ModelAndView exportclinicalprofiles(
            @RequestParam(value = "email", required = true) String email,            
            Model model) {
        logger.debug("exportclinicalprofiles()");

        if (CsvHelper.export(config.getDbPath(), config.getCsvsPath(), "clinicalprofile", "1") == 0) {
            logger.debug("... export of clinical profiles succeeded");
            SendMail.sendMail(email, "Profili clinici", "Profili clinici di tutti i pazienti", config.getCsvsPath()+File.separator+"clinicalprofile.csv");
            return new ModelAndView("redirect:/showpatients"); 
        }
        else {
            logger.debug("... export of clinical profiles failed");
            model.addAttribute("message", "Esportazione profili clinici fallita");
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }

    }
    
}