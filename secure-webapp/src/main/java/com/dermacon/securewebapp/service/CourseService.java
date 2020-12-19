package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonService personService;

    public Course getCourse(long id) {
        return courseRepository.findByCourseId(id);
    }

    public Iterable<Course> allCourses() {
        return courseRepository.findAll();
    }

    public Iterable<Course> createdCourses() {
        return null;
    }

    public boolean currUserIsEnrolled(Course course) {
        return course.getParticipants().contains(personService.getLoggedInPerson());
    }

}
