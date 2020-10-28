package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.*;
import com.dermacon.securewebapp.service.ErrorService.ERROR_CODE;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import com.dermacon.securewebapp.service.ErrorService;
import com.dermacon.securewebapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/createWorkshop")
    public String createNewWorkshop_get(Model model) {

        if (!personService.matchesAtLeastOneRole(ROLE_MANAGER, ROLE_ADMIN)) {
            return errorService.displayErrorPage(model, ERROR_CODE.INVALID_ROLE);
        }

        // course instance that will be filled in form
        Course inputCourse = new Course();
        inputCourse.setHost(personService.getLoggedInPerson());

        model.addAttribute("inputCourse", inputCourse);

        return "createWorkshop";
    }

    @RequestMapping(value = "/createWorkshop", method = RequestMethod.POST)
    public String createNewWorkshop_post(@ModelAttribute(value="inputCourse") Course course) {
        // save in database
        LoggerSingleton.getInstance().info("save course: " + course);
        courseRepository.save(course);
        return "redirect:/createWorkshop";
    }

    @RequestMapping("/courses/createAnnouncement")
    public String createNewAnnouncement_get(Model model, @RequestParam String id) {

        if (courseRepository.findByCourseId(Long.parseLong(id)) == null) {
            return errorService.displayErrorPage(model, ERROR_CODE.INVALID_COURSE);
        }

        if (!personService.matchesAtLeastOneRole(ROLE_MANAGER, ROLE_ADMIN)) {
            return errorService.displayErrorPage(model, ERROR_CODE.INVALID_ROLE);
        }

        model.addAttribute("inputAnnouncement", new Announcement());
        return "createAnnouncement";
    }

    @RequestMapping(value = "/createAnnouncement", method = RequestMethod.POST)
    public String createNewAnnouncement_post(@ModelAttribute(value="inputAnnouncement") Announcement announcement) {
        LoggerSingleton.getInstance().info("save announcement: " + announcement);
        announcementRepository.save(announcement);
        return "redirect:/createAnnouncement";
    }

}
