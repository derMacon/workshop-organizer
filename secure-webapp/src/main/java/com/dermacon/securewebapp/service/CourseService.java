package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.FormAnnouncementInfo;
import com.dermacon.securewebapp.data.FormCourseInfo;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.exception.AnnouncementNonExistentException;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import com.dermacon.securewebapp.exception.HostEnrollOwnCourseException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
import com.dermacon.securewebapp.exception.UserAlreadyEnrolledException;
import com.dermacon.securewebapp.exception.UserNotEnrolledAtDropoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private AnnouncementService announcementService;


    /* ---------- information for the displaying data ---------- */

    public Course getCourse(long courseId) throws NonExistentCourseException {
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            throw new NonExistentCourseException();
        }
        return courseRepository.findByCourseId(courseId);
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



    /* ---------- course entities (add / delete) ---------- */

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

    public void removeCourse(long id) throws NonExistentCourseException {
        Course course = getCourse(id);
        // first delete all foreign key references
        course.setHost(null);
        course.setParticipants(null);
        Set<Announcement> announcements = course.getAnnouncements();
        announcementService.deleteAnnouncements(announcements);
        courseRepository.delete(course);
    }


    /* ---------- person information ---------- */

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


    /* ---------- person information ---------- */

    /**
     * Enroll logged in person in a given course
     *
     * @param courseId id of the course
     * @throws NonExistentCourseException the specified course does not exist
     * @throws UserAlreadyEnrolledException the person is already enrolled in the course
     * @throws HostEnrollOwnCourseException the person has created the course
     */
    public void enrollLoggedInPerson(long courseId) throws NonExistentCourseException, UserAlreadyEnrolledException, HostEnrollOwnCourseException {
        Course course = getCourse(courseId);

        Person newParticipant = personService.getLoggedInPerson();
        if (course.getParticipants().contains(newParticipant)) {
            throw new UserAlreadyEnrolledException();
        }

        if (course.getHost().equals(newParticipant)) {
            throw new HostEnrollOwnCourseException();
        }

        course.addNewParticipant(newParticipant);
        courseRepository.save(course);
//        mailService.sendGreeting(newParticipant, course);
    }

    public void dropoutLoggedInPerson(long courseId) throws NonExistentCourseException, UserNotEnrolledAtDropoutException {
        Course course = getCourse(courseId);

        Person participant = personService.getLoggedInPerson();
        if (!currUserIsEnrolled(course)) {
            throw new UserNotEnrolledAtDropoutException();
        }

        course.removeParticipant(participant);
        courseRepository.save(course);
//        mailService.sendDropoutConfirmation(participant, course);
    }


    /* ---------- announcements ---------- */

    public void createAnnouncement(FormAnnouncementInfo announcementInfo, long courseId) throws NonExistentCourseException {
        Course course = getCourse(courseId);
        announcementService.createNewAnnouncement(course, announcementInfo);
    }

    public void deleteAnnouncement(long announcementId) throws AnnouncementNonExistentException {
        announcementService.deleteAnnouncement(announcementId);
    }

}
