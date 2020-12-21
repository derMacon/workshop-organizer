package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.AnnouncementRepository;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.FormAnnouncementInfo;
import com.dermacon.securewebapp.data.FormCourseInfo;
import com.dermacon.securewebapp.exception.AnnouncementNonExistentException;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
import com.dermacon.securewebapp.service.AnnouncementService;
import com.dermacon.securewebapp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("manager")
public class ManagerController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("/")
    public String root() {
        return "redirect:/courses/created";
    }


    /* ---------- create / remove entity ---------- */

    @RequestMapping("/createCourse")
    public String createCoursePage_get(Model model) {
        // course instance that will be filled in form
        model.addAttribute("inputCourse", new FormCourseInfo());
        return "manager/createCourse";
    }

    @PostMapping("/createCourse")
    public String createCoursePage_post(@ModelAttribute("inputCourse") FormCourseInfo formInput,
                                        Model model) {
        try {
            courseService.createCourse(formInput);
        } catch (DuplicateCourseException e) {
            // todo logger
            System.out.println(e.getErrorCode());
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }
        // todo pop up / alert showing everything is fine
        return "redirect:/manager/createCourse";
    }


    @RequestMapping("/removeCourse")
    public String removeCoursePage_post(@RequestParam long id, Model model) {
        try {
            courseService.removeCourse(id);
        } catch (ErrorCodeException e) {
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }
        return "redirect:/courses/created";
    }


    /* ---------- create / remove entity ---------- */

    @RequestMapping("/createAnnouncement")
    public String createNewAnnouncement_get(Model model, @RequestParam long courseId) {
        Course course = null;
        try {
            course = courseService.getCourse(courseId);
        } catch (NonExistentCourseException e) {
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }

        // todo implement as pop up
        model.addAttribute("currCourse", course);
        model.addAttribute("inputAnnouncement", new FormAnnouncementInfo());
        return "courses/announcements/view_create_announcement";
    }

    @RequestMapping(value = "/createAnnouncement", method = RequestMethod.POST)
    public String createNewAnnouncement_post(@ModelAttribute(value="inputAnnouncement") FormAnnouncementInfo announcementInfo,
                                             @RequestParam long courseId, Model model) {
        try {
            courseService.createAnnouncement(announcementInfo, courseId);
        } catch (NonExistentCourseException e) {
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }

        return "redirect:/courses/specific?id=" + courseId;
    }

    @RequestMapping("/deleteAnnouncement")
    public String deleteSpecificAnnouncement(Model model, @RequestParam long announcementId) {
        try {
            courseService.deleteAnnouncement(announcementId);
        } catch (AnnouncementNonExistentException e) {
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }
        return "redirect:/courses/";
    }

}
