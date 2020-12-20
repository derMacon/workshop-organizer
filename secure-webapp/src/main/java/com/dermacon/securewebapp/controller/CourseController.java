package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.service.CourseService;
import com.dermacon.securewebapp.service.MeetingService;
import com.dermacon.securewebapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.function.Supplier;

@Controller
@RequestMapping("courses")
public class CourseController extends ModelAttributeProvider {

    private final static String SPECIFIC_PATH = "/courses/specific/specificCourse";
    private final static String OVERVIEW_PATH = "/courses/overview/";
    private final static Logger LOGGER = Logger.getLogger(CourseController.class.getName());

    @Autowired
    private CourseService courseService;

    @Autowired
    private PersonService personService;

    @Autowired
    private MeetingService meetingService;


    /* ---------- Courses overview ---------- */

    @RequestMapping(value = {"/", "/all"})
    public String showAllCourses(Model model) {
        return showCoursesOverview(model, courseService.allCourses());
    }

    @RequestMapping(path = "/enrolled")
    public String showEnrolledCourses(Model model) {
        return showCoursesOverview(model, personService.getLoggedInPerson().getCourses());
    }

    @RequestMapping(path = "/created")
    public String showCreatedCourses(Model model) {
        return showCoursesOverview(model, courseService.createdCourses());
    }

    private String showCoursesOverview(Model model, Iterable<Course> courses) {
        model.addAttribute("specifiedCourses", courses);
        return OVERVIEW_PATH + "coursesOverview";
    }


    /* ---------- specific course information ---------- */

    @RequestMapping(path = "/specific")
    public String showSpecificCourse(Model model, @RequestParam long id) {
        Course course = courseService.getCourse(id);

        model.addAttribute("currCourse", course);
        model.addAttribute("isEnrolled", courseService.currUserIsEnrolled(course));
        model.addAttribute("meetings", meetingService.getAllMeetings(course));

        return SPECIFIC_PATH;
    }

}
