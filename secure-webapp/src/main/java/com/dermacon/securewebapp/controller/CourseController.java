package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
import com.dermacon.securewebapp.exception.UserAlreadyEnrolledException;
import com.dermacon.securewebapp.service.CourseService;
import com.dermacon.securewebapp.service.MeetingService;
import com.dermacon.securewebapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("courses")
public class CourseController extends ModelAttributeProvider {

    private final static String SPECIFIC_PATH = "/courses/specific/";
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

    @RequestMapping(path = "/creators")
    public String showCreatorOverview(Model model){
        model.addAttribute("creators", courseService.getAllCreators());
        return OVERVIEW_PATH + "creatorOverview";
    }

    /* ---------- specific course information ---------- */

    @RequestMapping(path = "/specific")
    public String showSpecificCourse(Model model, @RequestParam long id) {
        Course course = null;

        try {
            course = courseService.getCourse(id);
        } catch (NonExistentCourseException e) {
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }

        model.addAttribute("currCourse", course);
        model.addAttribute("isEnrolled", courseService.currUserIsEnrolled(course));
        model.addAttribute("meetings", meetingService.getAllMeetings(course));

        // if the person created the course or the person is an admin
        return courseService.loggedInPersonCanEditCourse(course)
                ? SPECIFIC_PATH + "specificCourse_manager"
                : SPECIFIC_PATH + "specificCourse_user";
    }


    /* ---------- user related methods ---------- */

    @RequestMapping(path = "/enroll")
    public String enrollLoggedInUser(@RequestParam long courseId, Model model) {
        try {
            courseService.enrollLoggedInPerson(courseId);
        } catch (ErrorCodeException e) {
            // todo log this
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }
        return "redirect:/courses/specific?id=" + courseId;
    }

    @RequestMapping(path = "/dropout")
    public String dropoutLoggedInUser(@RequestParam long courseId, Model model) {
        try {
            courseService.dropoutLoggedInPerson(courseId);
        } catch (ErrorCodeException e) {
            // todo log this
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }
        return "redirect:/courses/all";
    }

}
