/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.services;

import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.controller.ExerciseController;
import it.unibo.msrehab.model.controller.MSRGroupController;
import it.unibo.msrehab.model.controller.MSRUserController;
import it.unibo.msrehab.model.entities.MSRGroup;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.util.CookiesManager;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
//import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author danger
 */
@Controller
public class StatisticService
{
    private final ExerciseController exerciseController;
    private final MSRUserController userController;
    private final MSRGroupController groupController;

    private final it.unibo.msrehab.model.Model model = JPAModel.getInstance();

    public StatisticService(){
        super();
        this.userController = new MSRUserController(model);
        this.exerciseController = new ExerciseController(model);
        this.groupController = new MSRGroupController(model);
    }

    /**
     * Vista per selezionare il paziente
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/patientsstatistics", method = RequestMethod.GET)
    public ModelAndView patientsstatistics(
            HttpServletRequest request,
            Model model)
    {

        if (!CookiesManager.isUserLogged(request)||((!CookiesManager.isUserAdmin(request))))
            return new ModelAndView("login");
        else
        {
            int cid = CookiesManager.getLoggedUserCenter(request);
            List<MSRUser> usersInCenter = userController.findAllPatientsInCenter(cid);
            model.addAttribute("patients", usersInCenter);
            return new ModelAndView("selectPatientStatistics");
        }
    }

    /**
     *  Vista per scegliere i grafici per le varie categorie di esercizi
      */

    @RequestMapping(value = "/patientchosegraph", method = RequestMethod.GET)
    public ModelAndView patientchosegraph(
            HttpServletRequest request,
            @RequestParam Integer patientid,
            Model model)
    {

        if (!CookiesManager.isUserLogged(request)||((!CookiesManager.isUserAdmin(request))))
            return new ModelAndView("login");


        List<String> types = exerciseController.getAllExerciseTypes();
        MSRUser user = userController.getEntity(patientid);

        model.addAttribute("patient", user);
        model.addAttribute("types", types);
        return new ModelAndView("patientchosegraph");
    }

    /**
     * Vista per scegliere l'esercizio data una sua categoria (kind)
     */

    @RequestMapping(value = "/patientchosegraph2", method = RequestMethod.GET)
    public ModelAndView patientchosegraph2(
            HttpServletRequest request,
            @RequestParam Integer patientid,
            @RequestParam(value = "kind") String type,
            Model model)
    {

        if (!CookiesManager.isUserLogged(request)||((!CookiesManager.isUserAdmin(request))))
            return new ModelAndView("login");


        List<Exercise> exercises = exerciseController.findAllExercisesByType(type)
                .stream()
                .filter(Exercise::isEnabled)
                .collect(Collectors.toList());

        ArrayList<String> category = new ArrayList<>();
        ArrayList<String> category_testo = new ArrayList<>();
        ArrayList<String> globalcategory = new ArrayList<>();

        for(Exercise ex: exercises)
        {
            category.add(ex.getCategory().toString());

            globalcategory.add(exerciseController.getDisplayName(ex));

            String[] parts = ex.getGlobalCategory().split("(?=-)");
            String displayText = Arrays.stream(parts)
                    .limit(2)
                    .map(String::trim)
                    .collect(Collectors.joining(" "));

            category_testo.add(displayText);
        }

        MSRUser patient = userController.getEntity(patientid);

        model.addAttribute("category",category);
        model.addAttribute("globalcategory",globalcategory);
        model.addAttribute("category_testo",category_testo);
        model.addAttribute("kind", type);
        model.addAttribute("patient", patient);

        return new ModelAndView("patientchosegraph2");
    }

    /**
     * Vista per visualizzare le statistiche dato un utente e tipo di esercizio
     */
    @RequestMapping(value = "/patientbuildgraph", method = RequestMethod.GET)
    public ModelAndView patientbuildgraph(
            @RequestParam Integer patientid,
            @RequestParam String kind,
            HttpServletRequest request
    )
    {
        if (!CookiesManager.isUserLogged(request)||((!CookiesManager.isUserAdmin(request))))
            return new ModelAndView("login");



        return new ModelAndView("patientgraphoverview_new")
                .addObject("userId", patientid)
                .addObject("exerciseCategory", kind)
                ;
    }

    /**
     * Vista per visualizzare le statistiche generali di un utente
     */
    @RequestMapping(value = "/patientbuildgraphoverview", method = RequestMethod.GET)
    public ModelAndView patientbuildgraphoverview(
            @RequestParam Integer patientid,
            HttpServletRequest request
    )
    {

        if (!CookiesManager.isUserLogged(request) || ((!CookiesManager.isUserAdmin(request))))
        {
            return new ModelAndView("login");
        }

        return new ModelAndView("patientgraphoverview_new")
                .addObject("userId", patientid);
    }

    /**
     * Vista per selezionare il gruppo da visualizzare le statistiche
     */
    @RequestMapping(value = "/groupstatistics", method = RequestMethod.GET)
    public ModelAndView groupstatistics(
            HttpServletRequest request,
            Model model)
    {
        if (!CookiesManager.isUserLogged(request) || ((!CookiesManager.isUserAdmin(request))))
            return new ModelAndView("login");

        int cid = CookiesManager.getLoggedUserCenter(request);
        List<MSRGroup> groupsInCenter = groupController.findAllGroupsInCenter(cid);

        model.addAttribute("groups", groupsInCenter);
        return new ModelAndView("selectGroupStatistics");
    }

    /**
     * Vista per visualizzare le statistiche del gruppo
     */
    @RequestMapping(value = "/groupgraph", method = RequestMethod.GET)
    public ModelAndView groupgraph(
            HttpServletRequest request,
            @RequestParam Integer groupid
    )
    {

        if (!CookiesManager.isUserLogged(request) || !CookiesManager.isUserAdmin(request))
            return new ModelAndView("login");


        return new ModelAndView("patientgraphoverview_new")
                .addObject("groupId", groupid)
                ;
    }


    @RequestMapping(value = "/downloadstatistics", method = RequestMethod.GET)
    public ModelAndView downloadStatisticsForm()
    {

        List<MSRUser> patients = userController.getAllEntities()
                .stream()
                .filter(u -> MSRUser.RoleValue.PATIENT.equals(u.getMsrrole()))
                .collect(Collectors.toList());

        return new ModelAndView("patient-statistics-downloader")
                .addObject("patients", patients)
                .addObject("downloadForm", new DownloadForm())
                ;
    }

    public static class DownloadForm
    {
        private List<Integer> patients = new ArrayList<>();

        private String email;

        private String phone;

        public List<Integer> getPatients()
        {
            return patients;
        }

        public void setPatients(List<Integer> patients)
        {
            this.patients = patients;
        }

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public String getPhone()
        {
            return phone;
        }

        public void setPhone(String phone)
        {
            this.phone = phone;
        }
    }
}
