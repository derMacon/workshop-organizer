package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.MeetingRepository;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;
import java.util.stream.Collectors;

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

    @RequestMapping("/createWorkshop")
    public String createNewWorkshop_get(Model model) {

        User user = personService.getLoggedInUser();
        if (user.getRole() != UserRole.ROLE_MANAGER
                && user.getRole() != UserRole.ROLE_ADMIN) {
            model.addAttribute("error_message", "no access - invalid permissions");
            return "error";
        }

        // course instance that will be filled in form
        Course inputCourse = new Course();
        inputCourse.setHost(personService.getLoggedInPerson());

        model.addAttribute("inputCourse", inputCourse);

        return "createWorkshop";
    }

    @RequestMapping(value = "/createWorkshop", method = RequestMethod.POST)
    public String createNewWorkshop_post(@ModelAttribute(value="inputCourse") Course course) {
        // update course with current user
        Person loggedInPerson = personService.getLoggedInPerson();
        course.setHost(loggedInPerson);

        // save in database
        LoggerSingleton.getInstance().info("save course: " + course);
        courseRepository.save(course);

        return "redirect:/createWorkshop";
    }

}
