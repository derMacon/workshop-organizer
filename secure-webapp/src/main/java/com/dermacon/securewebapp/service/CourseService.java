package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.FormCourseInfo;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonService personService;


    /* ---------- information for the displaying data ---------- */

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



    /* ---------- creating stuff ---------- */

    public void createCourse(FormCourseInfo courseInfo) throws DuplicateCourseException {
        // todo move this to repository
        boolean exists = false;
        String inputCourseName = courseInfo.getCourseName().toLowerCase();
        for (Course course : courseRepository.findAll()) {
            exists = exists || course.getCourseName().toLowerCase().equals(inputCourseName);
        }

        if (exists) {
            throw new DuplicateCourseException();
        }

        // todo builder
        Course newCourse = new Course(
                personService.getLoggedInPerson(),
                new HashSet<>(),
                courseInfo.getCourseName(),
                courseInfo.getCourseSummary(),
                courseInfo.getCourseDescription(),
                courseInfo.getMaxParticipantCount(),
                new HashSet<>()
        );
        courseRepository.save(newCourse);
    }

}
