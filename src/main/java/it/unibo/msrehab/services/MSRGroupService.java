package it.unibo.msrehab.services;

import it.unibo.msrehab.config.Configuration;
import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.entities.*;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRSessionController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.controller.NPSTestController;
import it.unibo.msrehab.model.controller.RaoTestController;
import it.unibo.msrehab.model.controller.WhiteTestController;
import it.unibo.msrehab.util.CookiesManager;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import it.unibo.msrehab.util.PhotoManager;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class MSRGroupService
{

    private static final Logger logger = LoggerFactory
            .getLogger(MSRGroupService.class);
    private final MSRGroupController groupController;
    private final MSRUserController userController;
    private final MSRSessionController sessionController;
    private final Configuration config;
    private final NPSTestController npsTestController;
    private final RaoTestController raoTestController;
    private final WhiteTestController whiteTestController;

    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();

    public MSRGroupService()
    {
        super();
        this.groupController = new MSRGroupController(model);
        this.userController = new MSRUserController(model);
        this.sessionController = new MSRSessionController(model);
        this.npsTestController = new NPSTestController(model);
        this.raoTestController = new RaoTestController(model);
        this.whiteTestController = new WhiteTestController(model);

        this.config = Configuration.getInstance();

    }


    // show update form
    @RequestMapping(value = "/groupform", method = RequestMethod.POST)
    public ModelAndView groupForm(
            @RequestParam(required = false) Integer  groupid,
            Model model
    )
    {
        logger.debug("groupForm() : {}", groupid);
        MSRGroup group = new MSRGroup();

        if(groupid != null)
            group =  groupController.getEntity(groupid);


        model.addAttribute("groupid", groupid);
        model.addAttribute("groupForm", group);
        return new ModelAndView("group-form");
    }
    
    @RequestMapping(value = "/patientgroup", method = RequestMethod.GET)
    public ModelAndView patientGroup(
            @RequestParam(required = false) Integer  groupid,
            @RequestParam(required = false) Integer  cid,
            @RequestParam(required = false) String  back,
            @RequestParam(required = false) String  home,
            Model model)
    {

        logger.debug("patientGroup() : {}", groupid);
        MSRGroup group = new MSRGroup();

        if(groupid != null)
            group = groupController.getEntity(groupid);

        List<MSRUser> patientsoutgroups = userController.findAllPatientsNotInGroupsInCenter(cid);
        List<MSRUser> patientsingroup = userController.findAllPatientsInGroup(groupid);

        model.addAttribute("group", group);
        model.addAttribute("back", back);
        model.addAttribute("home", home);
        model.addAttribute("cid", cid);
        model.addAttribute("patientsoutgroups", patientsoutgroups);
        model.addAttribute("patientsingroup", patientsingroup);
        return new ModelAndView("patientgroup");
    }

    @RequestMapping(value = "/patientgroupform", method = RequestMethod.GET)
    public ModelAndView patientGroupForm(
            HttpServletRequest request,            
            @RequestParam(required = false) Integer  patientid,
            @RequestParam(required = false) String  back,
            @RequestParam(required = false) String  home,
            Model model) {

        logger.debug("patientGroupForm() : {}", patientid);

        List<NPSTest> npsTests = npsTestController.findAllByUser(patientid);
        List<RaoTest> raoTests = raoTestController.findAllByUser(patientid);
        List<WhiteTest> whiteTests = whiteTestController.findAllByUser(patientid);

        if (npsTests.isEmpty() || raoTests.isEmpty() || whiteTests.isEmpty())
        {
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            model.addAttribute("message", "Compilare prima il profilo del partecipante");
            return new ModelAndView("error");
        }

        int cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRGroup> groupsInCenter = groupController.findAllGroupsInCenter(cid);
        model.addAttribute("patientid", patientid);
        model.addAttribute("back", back);
        model.addAttribute("home", home);
        model.addAttribute("groups", groupsInCenter);
        return new ModelAndView("patientgroup-form");

    }


    @RequestMapping(value = "/deletegroup", method = RequestMethod.POST)
    public ModelAndView deleteGroup(
            @RequestParam Integer groupid
    )
    {
        
        logger.info("Start to delete group " + groupid);
        MSRGroup group = groupController.getEntity(groupid);
        
        List<MSRUser> patientsInGroup = userController.findAllPatientsInGroup(groupid);
        
        for(MSRUser patient: patientsInGroup)
        {
            patient.setGid(-1);
            userController.updateEntity(patient);
        }

        groupController.removeEntity(group);

        logger.info("Group " + groupid + " deleted with success");
        return new ModelAndView("redirect:/showgroups");
    }


    // list page
    @RequestMapping(value = "/showgroups", method = RequestMethod.GET)
    public ModelAndView showgroups(
            HttpServletRequest request,
            Model model)
    {
        logger.debug("showgroups()");

        if (!CookiesManager.isUserLogged(request))
            return new ModelAndView("login");

        int cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRGroup> groups = groupController.findAllGroupsInCenter(cid);

        if(groups.isEmpty())
        {
            model.addAttribute("message", "Nessun gruppo definito");
            return new ModelAndView("group-management");
        }

        List<MSRGroup> notActiveGroups = new ArrayList<>();
        List<MSRGroup> activeGroups = new ArrayList<>();

        for(MSRGroup group: groups)
        {
            List<MSRSession> activeSessions = sessionController.findAllActiveByUserOrGroup(group.getId());
            if(activeSessions.isEmpty())
                notActiveGroups.add(group);
            else
                activeGroups.add(group);
        }

        model.addAttribute("notactivegroups", notActiveGroups);
        model.addAttribute("activegroups", activeGroups);
        model.addAttribute("cid", cid);
        model.addAttribute("back", "showgroups");
        model.addAttribute("home", "adminhome");

        return new ModelAndView("group-management");

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

        int cid = CookiesManager.getLoggedUserCenter(request);
        groupForm.setCid(cid);

        groupController.putEntity(groupForm);

        logger.info("Add of group " + groupForm.getId() + " terminated with success");
        return new ModelAndView("redirect:/showgroups");
    }
    
    @RequestMapping(value = "/deletepatientfromgroup", method = RequestMethod.POST)
    public ModelAndView deletePatientFromGroup(
            @RequestParam Integer groupid,
            @RequestParam Integer patientid,
            @RequestParam Integer cid,
            @RequestParam String back,
            @RequestParam String home,
            Model model,
            final RedirectAttributes redirectAttributes) {
        
        logger.info("Start to delete patient from group " + groupid);
        MSRUser patient = userController.getEntity(patientid);


        if (!patient.getGid().equals(groupid))
        {
            logger.warn("Patient " + patientid + " does not belong to group " + groupid);
            model.addAttribute("message", String.format("Partecipante non non gruppo %s", groupid));
            model.addAttribute("back", "showpatients");
            model.addAttribute("home", "adminhome");
            return new ModelAndView("error");
        }

        patient.setGid(-1);
        userController.updateEntity(patient);

        logger.info("Patient " + patientid + " deleted with success from group " + groupid);
        redirectAttributes.addFlashAttribute("css", "success");
        redirectAttributes.addFlashAttribute("msg", "Patient is deleted from group!");
        return new ModelAndView("redirect:/patientgroup?groupid="+groupid+
                "&cid="+cid+"&back="+back+"&home="+home);
    }
    
    @RequestMapping(value = "/addpatienttogroup", method = RequestMethod.POST)
    public ModelAndView addPatientToGroup(
            @RequestParam String back,
            @RequestParam String home,
            @RequestParam Integer cid,
            @RequestParam Integer groupid,
            @RequestParam Integer patientid,
            Model model,
            final RedirectAttributes redirectAttributes)
    {
        
        logger.info("Start to add patient to group " + groupid);
        MSRUser patient = userController.getEntity(patientid);

        patient.setGid(groupid);
        userController.updateEntity(patient);

        logger.info("Patient " + patientid + " added with success to group " + groupid);

        redirectAttributes.addFlashAttribute("css", "success");
        redirectAttributes.addFlashAttribute("msg", "Patient is added to group!");
        return new ModelAndView("redirect:/patientgroup?groupid="+groupid+
                "&cid="+cid+"&back="+back+"&home="+home);

    }


    @RequestMapping(value = "/getusersfromgroup", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> getUsersFromGroup(
            HttpServletRequest request,
            @RequestParam Integer groupid
    )
    {

        List<MSRUser> userlist = userController.findAllPatientsInGroup(groupid);

        JSONArray ja = new JSONArray();
        for (MSRUser u : userlist)
        {
            JSONObject ju = new JSONObject();
            ju.put("surname", u.getSurname());
            ju.put("name", u.getName());
            String photo;
            if (u.getPhoto() != null)
                photo = u.getPhoto();
            else
                photo = PhotoManager.DEFAULT_PORTRAIT_FILENAME;
            ju.put("photo", PhotoManager.getInstance().getPhotoUrl(request, photo));
            ju.put("id", u.getId());
            ja.put(ju);
        }
        JSONObject j = new JSONObject();
        j.put("group", ja);

        return new ResponseEntity<>(ja.toString(), HttpStatus.OK);
    }
    
}
