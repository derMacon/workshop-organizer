package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.MeetingRepository;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for view displayed to users with Role: ROLE_USER
 */
@Controller
public class CourseDisplayController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    PersonService personService;

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


    @RequestMapping("/courses/enroll")
    public String courses(Model model, @RequestParam String id) {
        Course course = courseRepository.findByCourseId(Long.parseLong(id));
        Person newParticipant = personService.getLoggedInPerson();

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
