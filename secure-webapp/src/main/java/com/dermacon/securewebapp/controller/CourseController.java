package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @RequestMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("selectedDomain", "home");

        model.addAttribute("allCourses", courseRepository.findAll());

        return "courses";
    }

    @RequestMapping("/courses/specific")
    public String courses(Model model, @RequestParam String id) {

        System.out.println("with id: " + id);

        Long id_long = Long.parseLong(id);
        Course course = courseRepository.findByCourseId(id_long);
        model.addAttribute("currCourse", course);

        return "specificCourse";
    }


}
