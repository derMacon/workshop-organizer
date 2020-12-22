package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Set;


/**
 *
 * @author Mukuljaiswal
 *
 */
@Service
public class MailService {

    /*
     * The Spring Framework provides an easy abstraction for sending email by using
     * the JavaMailSender interface, and Spring Boot provides auto-configuration for
     * it as well as a starter module.
     */
    private JavaMailSender javaMailSender;

    /**
     *
     * @param javaMailSender
     */
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendAccountConfirmation(Person person) {
        System.out.println("send conf: " + person);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(person.getEmail());
        mail.setSubject("Registration Confirmation");
        mail.setText("You can now login to the website with the provided data.");

//        LoggerSingleton.getInstance().info("sending mail: " + mail.toString());
        // todo uncomment
        javaMailSender.send(mail);
    }








    /**
     * Send greeting mail to person who entered a course
     * @param person person new to the course
     * @param course course to which a the user subscribed
     */
    public void sendGreeting(Person person, Course course) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(person.getEmail());
        mail.setSubject("Welcome to " + course.getCourseName() + " workshop");
        mail.setText(course.toString());

        LoggerSingleton.getInstance().info("sending mail: " + mail.toString());
        // todo uncomment
//        javaMailSender.send(mail);
    }

    /**
     * Sends a mail to confirm the user he unsubscribed from the course
     * @param person person to which the mail will be send
     * @param course course he left
     */
    public void sendDropoutConfirmation(Person person, Course course) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(person.getEmail());
        mail.setSubject("Confirmation for checkout");
        mail.setText("leaving course:\n" + course.toString());

        LoggerSingleton.getInstance().info("sending mail: " + mail.toString());
        // todo uncomment
//        javaMailSender.send(mail);
    }

    /**
     * Send announcement to a given set of persons
     * @param persons set of people
     * @param announcement announcement to send per mail
     */
    public void sendAnnouncement(Set<Person> persons, Announcement announcement) {
        for (Person person : persons) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(person.getEmail());
            mail.setSubject("Announcement: " + announcement.getTitle());
            mail.setText(announcement.getContent());

            LoggerSingleton.getInstance().info("sending mail: " + mail.toString());
            // todo uncomment
//        javaMailSender.send(mail);
        }
    }


    /**
     *
     * @throws MailException
     */

    public void sendEmail(Person person) throws MailException {

        /*
         * This JavaMailSender Interface is used to send Mail in Spring Boot. This
         * JavaMailSender extends the MailSender Interface which contains send()
         * function. SimpleMailMessage Object is required because send() function uses
         * object of SimpleMailMessage as a Parameter
         */

        LoggerSingleton.getInstance().info("new mail: " + person);

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo("rwl93183@cuoly.com");
//        mail.setTo(person.getEmail());
        mail.setSubject("Testing Mail API");
        mail.setText("Hurray ! You have done that dude...");

        /*
         * This send() contains an Object of SimpleMailMessage as an Parameter
         */
        javaMailSender.send(mail);
    }

    /**
     * This function is used to send mail that contains a attachment.
     *
     * @param user
     * @throws MailException
     * @throws MessagingException
     */
    public void sendEmailWithAttachment(User user) throws MailException, MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

//        helper.setTo(user.getEmailAddress());
        helper.setSubject("Testing Mail API with Attachment");
        helper.setText("Please find the attached document below.");


        ClassPathResource classPathResource = new ClassPathResource("Attachment.pdf");
        helper.addAttachment(classPathResource.getFilename(), classPathResource);

        javaMailSender.send(message);
    }

}
