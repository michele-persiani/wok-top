package it.unibo.msrehab.services;

import it.unibo.msrehab.config.ApplicationContextLoader;
import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.entities.MSRGroup;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.controller.NPSTestController;
import it.unibo.msrehab.model.controller.RaoTestController;
import it.unibo.msrehab.model.controller.WhiteTestController;
import it.unibo.msrehab.util.CookiesManager;
import it.unibo.msrehab.util.WebPagesUtilities;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Class that provides servlets for system groups. 
 */
@Controller
public class MSRGroupService {

    private static final Logger logger = LoggerFactory
            .getLogger(MSRGroupService.class);
    private final MSRGroupController groupController;
    private final MSRUserController userController;
    private final MSRSessionController sessionController;
    private final Configuration config;
    private final NPSTestController npsTestController;
    private final RaoTestController raoTestController;
    private final WhiteTestController whiteTestController;

    public MSRGroupService() {
        super();
        this.groupController = new MSRGroupController();
        this.userController = new MSRUserController();
        this.sessionController = new MSRSessionController();
        this.npsTestController = new NPSTestController();
        this.raoTestController = new RaoTestController();
        this.whiteTestController = new WhiteTestController();

        ApplicationContextLoader l = new ApplicationContextLoader();
        l.load(Configuration.class,
                "META-INF/spring/applicationContext.xml");
        this.config = l.getApplicationContext().getBean(Configuration.class);

    }
    

    // show update form
    @RequestMapping(value = "/groupform", method = RequestMethod.POST)
    public ModelAndView groupForm(
            @RequestParam(value = "groupid", required = false) Integer  groupid,
                    Model model) {
        
        logger.debug("groupForm() : {}", groupid);
        MSRGroup group = new MSRGroup();
        if(groupid != null) {
            group = (MSRGroup) groupController.findEntity(groupid).get();
        }
        model.addAttribute("groupid", groupid);
        model.addAttribute("groupForm", group);
        return new ModelAndView("group-form");
    }
    
    @RequestMapping(value = "/patientgroup", method = RequestMethod.GET)
    public ModelAndView patientGroup(
            @RequestParam(value = "groupid", required = false) Integer  groupid,
            @RequestParam(value = "cid", required = false) Integer  cid,
            @RequestParam(value = "back", required = false) String  back,
            @RequestParam(value = "home", required = false) String  home,
            Model model) {

        logger.debug("patientGroup() : {}", groupid);
        MSRGroup group = new MSRGroup();
        if(groupid != null) {
            group = (MSRGroup) groupController.findEntity(groupid).get();
        }
        model.addAttribute("group", group);
        model.addAttribute("back", back);
        model.addAttribute("home", home);
        model.addAttribute("cid", cid);
        model.addAttribute("patientsoutgroups", userController.findAllPatientsNotInGroupsInCenter(cid));
        model.addAttribute("patientsingroup", userController.findAllPatientsInGroup(groupid));
        return new ModelAndView("patientgroup");
    }

    @RequestMapping(value = "/patientgroupform", method = RequestMethod.GET)
    public ModelAndView patientGroupForm(
            HttpServletRequest request,            
            @RequestParam(value = "patientid", required = false) Integer  patientid,
            @RequestParam(value = "back", required = false) String  back,
            @RequestParam(value = "home", required = false) String  home,
            Model model) {

        logger.debug("patientGroupForm() : {}", patientid);
        if (npsTestController.findAllByUser(patientid).isEmpty()
                || raoTestController.findAllByUser(patientid).isEmpty()
                || whiteTestController.findAllByUser(patientid).isEmpty()) {            
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            //model.addAttribute("message", "Compilare prima il profilo del paziente");                
            model.addAttribute("message", "Compilare prima il profilo del partecipante");                
            
            return new ModelAndView("error");
        }
        else {
            int cid = CookiesManager.getLoggedUserCenter(request);
            model.addAttribute("patientid", patientid);
            model.addAttribute("back", back);
            model.addAttribute("home", home);
            model.addAttribute("groups", groupController.findAllGroupsInCenter(cid));
            return new ModelAndView("patientgroup-form");
        }
    }
    
    /**
     * Deletes a group given the group id
     *
     * @param groupid the id of the group
     * @param model
     * @param redirectAttributes
     *
     * @return 1 if group is deleted, -1 otherwise; HttpStatus.NOT_ACCEPTABLE if
     * the user with the given id was not found; HttpStatus.OK if the group is
     * deleted with success; HttpStatus.SERVICE_UNAVAILABLE if the group cannot
     * be deleted
     */
    @RequestMapping(value = "/deletegroup", method = RequestMethod.POST)
    public ModelAndView deleteGroup(
            @RequestParam(value = "groupid", required = true) Integer groupid,
            Model model,
            final RedirectAttributes redirectAttributes) {
        
        logger.info("Start to delete group " + groupid);
        MSRGroup group = (MSRGroup) groupController.findEntity(groupid).get();
        if (group == null) {
            logger.warn("Group " + groupid + " not found");
            model.addAttribute("message", "Gruppo non trovato");
            model.addAttribute("back", "showgroups");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }
        
        List<MSRUser> patientsInGroup = 
                userController.findAllPatientsInGroup(groupid);
        
        for(MSRUser patient: patientsInGroup) {
            patient.setGid(-1);
            if(!userController.updateEntity(patient)) {
                logger.error("...group update failed");
                //model.addAttribute("message", "Gruppo " + groupid + ", cancellazione paziente fallita");
                model.addAttribute("message", "Gruppo " + groupid + ", cancellazione partecipante fallita");
                
                model.addAttribute("back", "showgroups");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");
            }
        }
        
        if (groupController.removeEntity(group)) {
            logger.info("Group " + groupid + " deleted with success");
            /* TODO Check the following two lines */
            redirectAttributes.addFlashAttribute("css", "success");
            redirectAttributes.addFlashAttribute("msg", "Group is deleted!");
            return new ModelAndView("redirect:/showgroups");
        }
        else {  // deleteRecord(group) == -1
            logger.error("...group update failed");
            model.addAttribute("message", "Gruppo " + groupid + ", cancellazione fallita");
            model.addAttribute("back", "showgroups");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }
    }
    
    
    // list page
    @RequestMapping(value = "/showgroups", method = RequestMethod.GET)
    public ModelAndView showgroups(
            HttpServletRequest request,
            Model model) {
        logger.debug("showgroups()");
        if (WebPagesUtilities.redirectIfNotLogged(request)) {
            return new ModelAndView("login");
        }
        else {
            int cid = CookiesManager.getLoggedUserCenter(request);
            List<MSRGroup> groups = groupController.findAllGroupsInCenter(cid);
            if(groups.isEmpty()) {
                model.addAttribute("message", "Nessun gruppo definito");
            }
            else {
                List<MSRGroup> notActiveGroups = new ArrayList<MSRGroup>();
                List<MSRGroup> activeGroups = new ArrayList<MSRGroup>();
                for(MSRGroup group: groups) {
                    if(sessionController.findAllActiveByUserOrGroup(group.getId()).isEmpty()) {
                        notActiveGroups.add(group);
                    }
                    else {
                        activeGroups.add(group);
                    }
                }
                model.addAttribute("notactivegroups", notActiveGroups);
                model.addAttribute("activegroups", activeGroups);
                model.addAttribute("cid", cid);
                model.addAttribute("back", "showgroups");
                model.addAttribute("home", "adminhome");
            }
            return new ModelAndView("group-management");
        }
    }
    
    // save or update patients
    @RequestMapping(value = "/saveorupdategroup", method = RequestMethod.POST)
    public ModelAndView saveOrUpdateGroup(
            HttpServletRequest request,            
            @Valid @ModelAttribute("groupForm") MSRGroup groupForm,
            BindingResult result,
            Model model) {

        logger.debug("saveOrUpdateGroup() : {}", groupForm);

        if (result.hasErrors()) {
            model.addAttribute("groupid", groupForm.getId());
            model.addAttribute("groupForm", groupForm);
            return new ModelAndView("group-form");
        }
        else if (groupForm.isNew()) {
            groupForm.setCid(CookiesManager.getLoggedUserCenter(request));
            if (groupController.insertEntity(groupForm))
            {
                logger.info("Add of group " + groupForm.getId() + " terminated with success");
                return new ModelAndView("redirect:/showgroups");
            }
            else {  // addRecord(groupForm) == -1
                logger.error("...group addition failed");
                model.addAttribute("message", "Aggiunta gruppo fallita");
                model.addAttribute("back", "showgroups");
                model.addAttribute("home", "patienthome");
                return new ModelAndView("error");
            }
        }
        else {  // update
            if (groupController.updateEntity(groupForm)) {
                logger.info("Update of group " + groupForm.getId() + " terminated with success");
                return new ModelAndView("redirect:/showgroups");
            }
            else {  // updateRecord(groupForm) == -1
                logger.error("...group update failed");
                model.addAttribute("message", "Aggiornamento gruppo fallita");
                model.addAttribute("back", "showgroups");
                model.addAttribute("home", "adminhome");
                return new ModelAndView("error");
            }
        }
    }
    
    @RequestMapping(value = "/deletepatientfromgroup", method = RequestMethod.POST)
    public ModelAndView deletePatientFromGroup(
            @RequestParam(value = "groupid", required = true) Integer groupid,
            @RequestParam(value = "patientid", required = true) Integer patientid,
            @RequestParam(value = "cid",required=true) Integer cid,
            @RequestParam(value = "back", required = true) String back,
            @RequestParam(value = "home", required = true) String home,
            Model model,
            final RedirectAttributes redirectAttributes) {
        
        logger.info("Start to delete patient from group " + groupid);
        MSRUser patient = (MSRUser) userController.findEntity(patientid).get();

        if(patient==null) {
            logger.warn("Patient " + patientid + " not found");
           // model.addAttribute("message", "Paziente non trovato");
            model.addAttribute("message", "Partecipante non trovato");
             
           model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }
        if (!patient.getGid().equals(groupid)) {
            logger.warn("Patient " + patientid + " does not belong to group " + groupid);
            //model.addAttribute("message", "Paziente non nel gruppo");
             model.addAttribute("message", "Partecipante non trovato");
            
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");                        
        }
        
        patient.setGid(-1);
        if (userController.updateEntity(patient)) {
            logger.info("Patient " + patientid + " deleted with success from group " + groupid);
            /* TODO Check the following two lines */
            redirectAttributes.addFlashAttribute("css", "success");
            redirectAttributes.addFlashAttribute("msg", "Patient is deleted from group!");
            //return new ModelAndView("   redirect:/showgroups");
            //model.addAttribute("back", "showpatients");
            //model.addAttribute("home", "adminhome");            
            return new ModelAndView("redirect:/patientgroup?groupid="+groupid+
                    "&cid="+cid+"&back="+back+"&home="+home);
            
        }
        else {
            logger.error("...patient update failed");
           // model.addAttribute("message", "Paziente " + patientid + ", cancellazione da gruppo fallita");
            model.addAttribute("message", "Partecipante " + patientid + ", cancellazione da gruppo fallita");
             
           model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");                                    
        }
    }
    
    @RequestMapping(value = "/addpatienttogroup", method = RequestMethod.POST)
    public ModelAndView addPatientToGroup(
            @RequestParam(value = "back", required = true) String back,
            @RequestParam(value = "home", required = true) String home, 
            @RequestParam(value="cid",required=true) Integer cid,
            @ModelAttribute("groupid") Integer groupid,
            @ModelAttribute("patientid") Integer patientid,
            Model model,
            final RedirectAttributes redirectAttributes) {
        
        logger.info("Start to add patient to group " + groupid);
        MSRUser patient = (MSRUser) userController.findEntity(patientid).get();
        
        if(patient==null) {
            logger.warn("Patient " + patientid + " not found");
          //  model.addAttribute("message", "Paziente non trovato");
            model.addAttribute("message", "Partecipante non trovato");
              
          model.addAttribute("back", "showgroups");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");                                                
        }
        
        patient.setGid(groupid);
        if (userController.updateEntity(patient)) {
            logger.info("Patient " + patientid + " added with success to group " + groupid);
            /* TODO Check the following two lines */
            redirectAttributes.addFlashAttribute("css", "success");
            redirectAttributes.addFlashAttribute("msg", "Patient is added to group!");
            //return new ModelAndView("redirect:/showgroups");
            return new ModelAndView("redirect:/patientgroup?groupid="+groupid+
                    "&cid="+cid+"&back="+back+"&home="+home);
            
        }
        else {
            logger.error("...patient update failed");
            //model.addAttribute("message", "Paziente " + patientid + ", inserimento in gruppo fallito");
            model.addAttribute("message", "Partecipante " + patientid + ", inserimento in gruppo fallito");
            
            model.addAttribute("back", "showgroups");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");            
        }
    }
    
    
    @RequestMapping(value = "/getusersfromgroup", method = RequestMethod.GET, headers = "Accept=application/json" )
    public ResponseEntity<String> getUsersFromGroup(
            @RequestParam(value = "groupid", required = true) Integer id){
        HttpHeaders headers = new HttpHeaders();
        
        
        List<MSRUser> userlist=userController.findAllPatientsInGroup(id);
        
        JSONArray ja=new JSONArray();
        String photosPath = "/MS-rehab"+config.getPhotosPath().split("MS-rehab")[1];
        for(MSRUser u : userlist){
            JSONObject ju=new JSONObject();
            ju.put("surname",u.getSurname());
            ju.put("name",u.getName());
            if(u.getPhoto()!=null)
                //ju.put("photo", photosPath + File.separator + u.getPhoto());
                ju.put("photo", photosPath + File.separator +u.getPhoto());
            else
                //ju.put("photo", photosPath + File.separator + "default-portrait.png");
                ju.put("photo", "/MS-rehab/resources/images/photos/default-portrait.png");
            ju.put("id",u.getId());
            ja.put(ju);
        }
        JSONObject j=new JSONObject();
        j.put("group", ja);
        
        return new ResponseEntity<String>(ja.toString(), headers,
                HttpStatus.OK);
    }
    
}
