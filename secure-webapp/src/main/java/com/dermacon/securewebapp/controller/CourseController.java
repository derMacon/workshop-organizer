package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.Meeting;
import com.dermacon.securewebapp.data.MeetingRepository;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String courses(Model model) {
        model.addAttribute("selectedDomain", "home");

        model.addAttribute("allCourses", courseRepository.findAll());

        return "courses";
    }

    @RequestMapping("/courses/specific")
    public String courses(Model model, @RequestParam String id) {

        Long id_long = Long.parseLong(id);
        Course course = courseRepository.findByCourseId(id_long);

        model.addAttribute("currCourse", course);
        model.addAttribute("meetings", meetingRepository.findAllByCourse(course));

        return "specificCourse";
    }

    @RequestMapping("/createWorkshop")
    public String createNewWorkshop_get(Model model) {
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
        LoggerSingleton.getInstance().info("save course: " + course);
        courseRepository.save(course);

        return "redirect:/createWorkshop";
    }

}
