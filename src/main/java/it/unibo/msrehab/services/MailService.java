/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.services;

import it.unibo.msrehab.util.SendMail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author danger
 */
@Controller
public class MailService
{



    @RequestMapping(value = "/mailcontact", method = RequestMethod.POST)
    public String mailcontact(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam(required = false) String phone,
            @RequestParam String email,
            @RequestParam String message,
            Model model)
    {
        
        //HttpHeaders headers = new HttpHeaders();
        String me = "floriano.zini@unibo.it";
        String subject = "Messaggio da MS-rehab";
        String okMessage = "Messaggio dal sito rehab.cs.unibo.it inviato correttamente! Sarete contattati quanto prima.";
        
        String mex = "Messaggio di "+name+" "+surname+"\nphone: "+phone+"\nEmail: "+email+"\nTesto:\n"+message+"\n";
        SendMail.sendMail(me,subject,mex);
        SendMail.sendMail(email,subject,okMessage);
        //headers.add("Location", "https://rehab.cs.unibo.it/MS-rehab-website/");    
        //return new ResponseEntity<String>("done", headers, HttpStatus.OK);
        return "redirect: https://rehab.cs.unibo.it/MS-rehab-website/";
    }

    @RequestMapping(value = "/mailexport", method = RequestMethod.POST)
    public ResponseEntity<String> mailexport(
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam String attachment,
            Model model)
    {
        
        HttpHeaders headers = new HttpHeaders();
        SendMail.sendMail(email,subject,message,attachment);
        return new ResponseEntity<String>("done", headers,
                HttpStatus.OK);
    }
    
}
