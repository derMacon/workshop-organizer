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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @RequestMapping("/courses")
    public String displayCourseOverview(Model model) {
        model.addAttribute("selectedDomain", "home");

        model.addAttribute("allCourses", courseRepository.findAll());

        return "courses";
    }

    @RequestMapping("/courses/specific")
    public String displaySingleCourse(Model model, @RequestParam String id) {

        Long id_long = Long.parseLong(id);
        Course course = courseRepository.findByCourseId(id_long);

        model.addAttribute("currCourse", course);
        model.addAttribute("meetings", meetingRepository.findAllByCourse(course));

        return "specificCourse";
    }

    @RequestMapping("/createWorkshop")
    public String createNewWorkshop_get(Model model) {

        User user = getLoggedInUser();
        if (user.getRole() != UserRole.ROLE_MANAGER
                && user.getRole() != UserRole.ROLE_ADMIN) {
            model.addAttribute("error_message", "no access - invalid permissions");
            return "error";
        }

        // course instance that will be filled in form
        model.addAttribute("inputCourse", new Course());

        // list of hosts to pick from while creating new course
        Set<User> users = userRepository.findAllByRole(UserRole.ROLE_ADMIN);
        users.addAll(userRepository.findAllByRole(UserRole.ROLE_MANAGER));

        Iterable<Person> possible_hosts = users.stream()
                .map(personRepository::findByUser)
                .collect(Collectors.toList());

        model.addAttribute("hosts", possible_hosts);

        return "createWorkshop";
    }

    @RequestMapping(value = "/createWorkshop", method = RequestMethod.POST)
    public String createNewWorkshop_post(@ModelAttribute(value="inputCourse") Course course) {
        // update course with current user
        Person loggedInPerson = getLoggedInPerson();
        course.setHost(loggedInPerson);

        // save in database
        LoggerSingleton.getInstance().info("save course: " + course);
        courseRepository.save(course);

        return "redirect:/createWorkshop";
    }

    private User getLoggedInUser() {
        // for some reason the id is always 0
        String username = ((User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();

        return userRepository.findByUsername(username);
    }

    /**
     * Determines the currently logged in user
     * @return the currently logged in user
     */
    private Person getLoggedInPerson() {
        return personRepository.findByUser(getLoggedInUser());
    }

    @RequestMapping("/courses/enroll")
    public String courses(Model model, @RequestParam String id) {
        Course course = courseRepository.findByCourseId(Long.parseLong(id));
        Person newParticipant = getLoggedInPerson();

        if (course.getParticipants().contains(newParticipant)) {
            model.addAttribute("error_message", "user already enrolled in course");
            return "error";
        }

        if (course.getHost().equals(newParticipant)) {
            model.addAttribute("error_message", "host cannot enroll in own course");
            return "error";
        }

        if (course.getParticipants().size() >= course.getMaxParticipantCount()) {
            model.addAttribute("error_message", "cannot enroll, course already full");
            return "error";
        }

        LoggerSingleton.getInstance().info("adding new participant to course: " + course);
        course.getParticipants().add(newParticipant);
        courseRepository.save(course);

        return "redirect:/courses/specific?id=" + id;
    }
}
