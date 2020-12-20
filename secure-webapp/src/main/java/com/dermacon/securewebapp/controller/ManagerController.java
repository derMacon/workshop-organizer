package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.FormCourseInfo;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
import com.dermacon.securewebapp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.dermacon.securewebapp.exception.ErrorCode.ACCESS_DENIED;
import static com.dermacon.securewebapp.exception.ErrorCode.DUPLICATE_COURSE;

@Controller
@RequestMapping("manager")
public class ManagerController {

    @Autowired
    private CourseService courseService;


    @RequestMapping("/")
    public String root() {
        return "redirect:/courses/created";
    }

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
        System.out.println("hier");
        try {
            courseService.removeCourse(id);
        } catch (ErrorCodeException e) {
            model.addAttribute("errorCode", e.getErrorCode());
            return "error/error";
        }
        return "redirect:/courses/created";
    }


}
