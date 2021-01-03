package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.form_input.FormSignupInfo;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.service.MailService;
import com.dermacon.securewebapp.service.PersonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;

/**
 * This class contains a Mail API developed using Spring Boot
 *
 * @author MukulJaiswal
 *
 */
@Controller
@RequestMapping("registration")
public class RegistrationController {

    private static Logger log = Logger.getLogger(ManagerController.class);

    @Autowired
    private MailService notificationService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;


    @RequestMapping(value={"/", "/signup"})
    public String signup_get(Model model) {
        model.addAttribute("signupInfo", new FormSignupInfo());
        return "registration/registration";
    }


    @PostMapping(value={"/", "/signup"})
    public String signup_post(@ModelAttribute("signupInfo") FormSignupInfo formSignupInfo,
                              Model model) {
        try {
            personService.register(formSignupInfo);
        } catch (ErrorCodeException e) {
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }
        return "redirect:/";
    }



    /**
     *
     * @return
     */
    @RequestMapping("send-mail")
    public String send() {

        Person receiver = personRepository.findAll().iterator().next();

        /*
         * Here we will call sendEmail() for Sending mail to the sender.
         */
        try {
            notificationService.sendEmail(receiver);
        } catch (MailException mailException) {
            System.out.println(mailException);
        }
        return "Congratulations! Your mail has been send to the user.";
    }

    /**
     *
     * @return
     * @throws MessagingException
     */
    @RequestMapping("send-mail-attachment")
    public String sendWithAttachment() throws MessagingException {

        /*
         * Creating a User with the help of User class that we have declared. Setting
         * the First,Last and Email address of the sender.
         */
//        user.setFirstName("Mukul");
//        user.setLastName("Jaiswal");
//        user.setEmailAddress("mukul.jaiswal786@gmail.com"); //Receiver's email address

        /*
         * Here we will call sendEmailWithAttachment() for Sending mail to the sender
         * that contains a attachment.
         */
        try {
//            notificationService.sendEmailWithAttachment(user);
        } catch (MailException mailException) {
            System.out.println(mailException);
        }
        return "Congratulations! Your mail has been send to the user.";
    }
}
