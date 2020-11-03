package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.*;
import com.dermacon.securewebapp.service.ErrorService.ERROR_CODE;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import com.dermacon.securewebapp.service.ErrorService;
import com.dermacon.securewebapp.service.MailService;
import com.dermacon.securewebapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.util.Date;

import static com.dermacon.securewebapp.data.UserRole.ROLE_ADMIN;
import static com.dermacon.securewebapp.data.UserRole.ROLE_MANAGER;

/**
 * Controller for view displayed to users with Role: ROLE_MANAGER
 */
@Controller
public class CourseManagerController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    PersonService personService;

    @Autowired
    ErrorService errorService;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    MailService mailService;

    @RequestMapping("/createWorkshop")
    public String createNewWorkshop_get(Model model) {

        if (!personService.matchesAtLeastOneRole(ROLE_MANAGER, ROLE_ADMIN)) {
            return errorService.displayErrorPage(model, ERROR_CODE.INVALID_ROLE);
        }

        // course instance that will be filled in form
        model.addAttribute("inputCourse", new Course());

        return "createWorkshop";
    }

    @RequestMapping(value = "/createWorkshop", method = RequestMethod.POST)
    public String createNewWorkshop_post(@ModelAttribute(value="inputCourse") Course course) {
        course.setHost(personService.getLoggedInPerson());
        // save in database
        LoggerSingleton.getInstance().info("save course: " + course);
        courseRepository.save(course);
        return "redirect:/createWorkshop";
    }

    @RequestMapping("/courses/createAnnouncement")
    public String createNewAnnouncement_get(Model model, @RequestParam String id) {

        if (!personService.matchesAtLeastOneRole(ROLE_MANAGER, ROLE_ADMIN)) {
            return errorService.displayErrorPage(model, ERROR_CODE.INVALID_ROLE);
        }

        Course course = courseRepository.findByCourseId(Long.parseLong(id));
        if (course == null) {
            return errorService.displayErrorPage(model, ERROR_CODE.INVALID_COURSE);
        }

        Announcement inputAnnouncement = new Announcement();

        model.addAttribute("currCourse", course);
        model.addAttribute("inputAnnouncement", inputAnnouncement);
        return "createAnnouncement";
    }

    @RequestMapping(value = "/courses/createAnnouncement", method = RequestMethod.POST)
    public String createNewAnnouncement_post(@ModelAttribute(value="inputAnnouncement") Announcement announcement, @RequestParam String id) {
        // update announcement fields
        Course course = courseRepository.findByCourseId(Long.parseLong(id));
        announcement.setCourse(course);
        announcement.setPublishingDate(new Date(System.currentTimeMillis()));

        // save in database
        LoggerSingleton.getInstance().info("save announcement: " + announcement.getAnnouncementId());
        announcementRepository.save(announcement);
        mailService.sendAnnouncement(course.getParticipants(), announcement);
        return "redirect:/createAnnouncement";
    }

}
