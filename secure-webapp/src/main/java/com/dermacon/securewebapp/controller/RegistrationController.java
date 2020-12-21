package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.formInput.FormSignupInfo;
import com.dermacon.securewebapp.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private MailService notificationService;

    @Autowired
    private PersonRepository personRepository;


    @RequestMapping(value={"/", "/signup"})
    public String showSignupView(Model model) {
        System.out.println("ist da");
        model.addAttribute("signupInfo", new FormSignupInfo());
        return "registration/registration";
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
