package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

//    @Autowired
//    private CourseP

    @Autowired
    private PersonService personService;

    public Iterable<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Iterable<Course> enrolledCourses() {
//        courseRepository.find
        return null;
    }

}
