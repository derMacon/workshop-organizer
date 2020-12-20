package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.FormCourseInfo;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
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
        return courseRepository.findAllByHost(personService.getLoggedInPerson());
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


    /**
     * Checks if the person created the course or the person is an admin
     * @param course course to check
     * @return true if the person created the course or the person is an admin
     */
    public boolean loggedInPersonCanEditCourse(Course course) {
        UserRole role = personService.getLoggedInUser().getRole();
        Person person = personService.getLoggedInPerson();
        return role == UserRole.ROLE_ADMIN || course.getHost().equals(person);
    }


    public void removeCourse(long id) throws NonExistentCourseException {
        Course course = courseRepository.findByCourseId(id);
        if (course == null) {
            throw new NonExistentCourseException();
        }
        course.setHost(null);
        course.setAnnouncements(null);
        course.setParticipants(null);
        courseRepository.delete(course);
    }

}
